package jp.go.aist.rtm.systemeditor.ui.views.logview;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.go.aist.rtm.systemeditor.nl.Messages;
import jp.go.aist.rtm.systemeditor.ui.util.LoggerHandler;
import jp.go.aist.rtm.toolscommon.model.component.SystemDiagram;
import jp.go.aist.rtm.toolscommon.model.component.SystemDiagramKind;
import jp.go.aist.rtm.toolscommon.util.AdapterUtil;

public class LogView extends ViewPart {
	private LoggerHandler handler;

	private TableViewer rtclogTableViewer;
	private Table rtclogTable;
	private Button btnFilter ;

	private SystemDiagram targetDiagram;

	private LogViewerFilter filter;
	private LogLabelProvider provider;
	private List<LogParam> logList = new ArrayList<LogParam>();
	
	private FilteringParam filteringParam = null;
	
	public LogView() {
	}

	public LogView getLogView() {
		return this;
	}
	
	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl = new GridLayout();
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		gl.numColumns = 2;
		parent.setLayout(gl);
		
		createControlPart(parent);
		createRTCLogPart(parent);
		createButtonPart(parent);
		setSiteSelection();
	}

	private void createButtonPart(Composite parent) {
		GridLayout gl;
		GridData gd;
		
		Composite composite = new Composite(parent, SWT.FILL);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.horizontalSpan = 1;
		composite.setLayoutData(gd);
		
		gl = new GridLayout();
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		gl.numColumns = 1;
		composite.setLayout(gl);
		
		Button btnClear = new Button(composite, SWT.NONE);
		gd = new GridData();
		gd.widthHint = 80;
		btnClear.setLayoutData(gd);
		btnClear.setText(Messages.getString("LogView.btnClear"));
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				logList.clear();
				rtclogTableViewer.refresh();
			}
		});
		
		Label lblDummuy03 = new Label(composite, SWT.NONE);
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		lblDummuy03.setLayoutData(gd);
		
		Button btnLoad = new Button(composite, SWT.NONE);
		gd = new GridData();
		gd.widthHint = 80;
		btnLoad.setLayoutData(gd);
		btnLoad.setText(Messages.getString("LogView.btnLoad"));
		btnLoad.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] ext = {"*.json", "*.csv", "*.*"};
				FileDialog loadDialog = new FileDialog(getSite().getShell(), SWT.OPEN);
				loadDialog.setFilterExtensions(ext);
				String loadFile = loadDialog.open();
				if(loadFile==null) return;
				
				logList.clear();
				if(loadFile.endsWith("csv")) {
					try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(loadFile))))) {
			            CSVParser parse = CSVFormat.EXCEL.parse(br);
			            List<CSVRecord> recordList = parse.getRecords();
			            for (CSVRecord record : recordList) {
			            	LogParam info = new LogParam();
							logList.add(info);
			            	if(record.isSet(0)) info.setTime(record.get(0));
			            	if(record.isSet(1)) info.setLevel(record.get(1));
			            	if(record.isSet(2)) info.setHost(record.get(5));
			            	if(record.isSet(3)) info.setPid(record.get(4));
			            	if(record.isSet(4)) info.setManager(record.get(2));
			            	if(record.isSet(5)) info.setName(record.get(3));
			            	if(record.isSet(6)) info.setMessage(record.get(6));
			            }
			        } catch (UnsupportedEncodingException e1) {
			        } catch (FileNotFoundException e2) {
			        } catch (IOException e3) {
			        }					
				} else {
					ObjectMapper mapper = new ObjectMapper();
					try {
						File file = new File(loadFile);
						BufferedReader br = new BufferedReader(new FileReader(file));
						StringBuilder builder = new StringBuilder();
						String str;
						while((str = br.readLine()) != null){
							builder.append(str);
						}
					  	br.close();

					  	List<LogParam> paramList = mapper.readValue(builder.toString(), new TypeReference<List<LogParam>>() {});
						int a = 0;
						logList.addAll(paramList);

					}catch(FileNotFoundException e1){
					}catch(IOException e2){
					}
				}
				rtclogTableViewer.refresh();
			}
		});
		
		Button btnSave = new Button(composite, SWT.NONE);
		gd = new GridData();
		gd.widthHint = 80;
		btnSave.setLayoutData(gd);
		btnSave.setText(Messages.getString("LogView.btnSave"));
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] ext = {"*.json", "*.csv", "*.*"};
				FileDialog saveDialog = new FileDialog(getSite().getShell(), SWT.SAVE);
				saveDialog.setFilterExtensions(ext);
				String saveFile = saveDialog.open();
				if(saveFile==null) return;
				
				if(saveFile.endsWith("csv")) {
					try (CSVPrinter printer = new CSVPrinter(new FileWriter(saveFile), CSVFormat.EXCEL)) {
						for(int index=0; index<rtclogTable.getItemCount(); index++) {
							TableItem item = rtclogTable.getItem(index);
							LogParam param = (LogParam)item.getData();
							printer.printRecord(param.getTime(),
												param.getLevel(),
												param.getHost(),
												param.getPid(),
												param.getManager(),
												param.getName(),
												param.getMessage());
						}
					} catch (IOException ex) {
					    ex.printStackTrace();
					}					
				} else {
					StringBuilder builder = new StringBuilder();
					ObjectMapper mapper = new ObjectMapper();
					List<LogParam> logList = new ArrayList<LogParam>();
					for(int index=0; index<rtclogTable.getItemCount(); index++) {
						TableItem item = rtclogTable.getItem(index);
						LogParam param = (LogParam)item.getData();
						logList.add(param);
					}
					try{
						String strLog = mapper.writeValueAsString(logList);
						
						File file = new File(saveFile);
						FileWriter filewriter = new FileWriter(file);
						filewriter.write(strLog);
						filewriter.close();
					} catch (IOException ex) {
					}
				}
				//
				MessageDialog.openInformation(getSite().getShell(),
						Messages.getString("LogView.btnSave"),
						Messages.getString("LogView.msgSave"));
			}
		});
	}
	
	private void createControlPart(Composite parent) {
		GridLayout gl;
		GridData gd;
		
		Composite composite = new Composite(parent, SWT.FILL);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 2;
		composite.setLayoutData(gd);
		
		gl = new GridLayout();
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		gl.numColumns = 7;
		composite.setLayout(gl);
		
		Label portNo = new Label(composite, SWT.NONE);
		portNo.setText(Messages.getString("LogView.PortNo"));

		Text txtPort = new Text(composite, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 100;
		txtPort.setLayoutData(gd);
		txtPort.setText("24224");
		
		Button btnStart = new Button(composite, SWT.TOGGLE);
		gd = new GridData();
		gd.widthHint = 120;
		btnStart.setLayoutData(gd);
		btnStart.setText(Messages.getString("LogView.btnStart"));
		btnStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(btnStart.getSelection()) {
					String strPort = txtPort.getText();
					int portNo = Integer.parseInt(strPort);
					try {
						handler = new LoggerHandler();
						handler.startServer(portNo, rtclogTableViewer);
						btnStart.setText(Messages.getString("LogView.btnStop"));
					} catch(Exception ex) {
						MessageDialog.openError(getSite().getShell(),
								"Error",
								Messages.getString("LogView.startError"));
						return;
					}
				} else {
					if(handler!=null) {
						try {
							handler.stopServer();
						} catch (Exception ex) {
						}
					}
					btnStart.setText(Messages.getString("LogView.btnStart"));
				}
			}
		});
		
		Label lblDummuy01 = new Label(composite, SWT.NONE);
		gd = new GridData();
		gd.widthHint = 100;
		lblDummuy01.setLayoutData(gd);
		
		Button chkFilter = new Button(composite, SWT.CHECK);
		chkFilter.setText(Messages.getString("LogView.chkFilter"));
		chkFilter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnFilter.setEnabled(chkFilter.getSelection());
				rtclogTableViewer.resetFilters();
				if(chkFilter.getSelection()) {
					rtclogTableViewer.addFilter(filter);
				}
			}
		});

		btnFilter = new Button(composite, SWT.NONE);
		gd = new GridData();
		gd.widthHint = 80;
		btnFilter.setLayoutData(gd);
		btnFilter.setText(Messages.getString("LogView.btnFilter"));
		btnFilter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LogFilterDialog dialog = new LogFilterDialog(getSite().getShell());
				dialog.setRootParam(filteringParam);
				int open = dialog.open();
				if (open != IDialogConstants.OK_ID) {
					return;
				}
				filteringParam = dialog.getRootParam();
				filter.setCondition(filteringParam);
				rtclogTableViewer.resetFilters();
				rtclogTableViewer.addFilter(filter);
			}
		});
		btnFilter.setEnabled(false);
		
		Label lblDummuy02 = new Label(composite, SWT.NONE);
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		lblDummuy02.setLayoutData(gd);
	}

	Composite createRTCLogPart(Composite parent) {
		GridLayout gl;
		GridData gd;

		Composite composite = new Composite(parent, SWT.FILL);
		gd = new GridData();
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 1;
		composite.setLayoutData(gd);
		
		gl = new GridLayout();
		gl.numColumns = 1;
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		composite.setLayout(gl);

		rtclogTableViewer = new TableViewer(composite, SWT.FULL_SELECTION
				| SWT.SINGLE | SWT.BORDER);
		rtclogTableViewer.setContentProvider(new ArrayContentProvider());
		rtclogTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				if(selection.isEmpty() == false) {
					handler.setAutoScroll(false);
				} else {
					handler.setAutoScroll(true);
				}
			}
		});
		rtclogTableViewer.setInput(logList);
		rtclogTableViewer.getTable().getVerticalBar().addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				 int topIndex = rtclogTableViewer.getTable().getTopIndex();
		        int itemCount = rtclogTableViewer.getTable().getItemCount();
		        int visibleRowCount = rtclogTableViewer.getTable().getClientArea().height / rtclogTableViewer.getTable().getItemHeight();
		        if(itemCount <= topIndex + visibleRowCount) {
		        	handler.setAutoScroll(true);
		        } else {
		        	handler.setAutoScroll(false);
		        }
			}
        });
		
		filter = new LogViewerFilter();
		rtclogTableViewer.setFilters(new ViewerFilter[] { filter });

		rtclogTable = rtclogTableViewer.getTable();
		rtclogTable.setLinesVisible(true);

		gd = new GridData();
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		rtclogTable.setLayoutData(gd);
		rtclogTable.setHeaderVisible(true);

		createColumn(rtclogTableViewer, Messages.getString("LogView.columnTime"), 120, true);
		createColumn(rtclogTableViewer, Messages.getString("LogView.columnLevel"), 60, true);
		createColumn(rtclogTableViewer, Messages.getString("LogView.columnHost"), 100, false);
		createColumn(rtclogTableViewer, Messages.getString("LogView.columnPid"), 50, false);
		createColumn(rtclogTableViewer, Messages.getString("LogView.columnManager"), 80, false);
		createColumn(rtclogTableViewer, Messages.getString("LogView.columnID"), 100, false);
		createColumn(rtclogTableViewer, Messages.getString("LogView.columnMessage"), 250, false);
		
		provider = new LogLabelProvider();
		rtclogTableViewer.setLabelProvider(provider);
		rtclogTableViewer.setComparator(new LogSorter(rtclogTableViewer));
		
		return composite;
	}

	TableViewerColumn createColumn(TableViewer viewer, String title, int width, boolean canSort) {
		TableViewerColumn col;
		col = new TableViewerColumn(viewer, SWT.NONE);
		col.getColumn().setText(title);
		col.getColumn().setWidth(width);
		if(canSort) {
			col.getColumn().addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					LogSorter sorter = (LogSorter) viewer.getComparator();
					TableColumn selectedColumn = (TableColumn) e.widget;
					sorter.setColumn(selectedColumn);
				}
			});
		}
		return col;
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void setFocus() {
	}

	/** ログ一覧表示のLabelProvider */
	public class LogLabelProvider extends LabelProvider implements
			ITableLabelProvider, ITableColorProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			LogParam entry = (LogParam) element;
			switch(columnIndex) {
			case 0:
				return entry.getTime();
			case 1:
				if(entry.getLevel() == null) return null;
				return entry.getLevel().toString();
			case 2:
				return entry.getHost();
			case 3:
				return entry.getPid();
			case 4:
				return entry.getManager();
			case 5:
				return entry.getName();
			default:
				return entry.getMessage();
			}
		}

		@Override
		public Color getBackground(Object element, int columnIndex) {
			return null;
		}

		@Override
		public Color getForeground(Object element, int columnIndex) {
			return null;
		}
	}
	
	ISelectionListener selectionListener = new ISelectionListener() {
		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			targetDiagram = null;
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection ss = (IStructuredSelection) selection;
				Object firstElement = ss.getFirstElement();
				Object adapter = AdapterUtil.getAdapter(firstElement,
						SystemDiagram.class);
				if (adapter != null) {
					SystemDiagram sd = (SystemDiagram) adapter;
					if (SystemDiagramKind.ONLINE_LITERAL.equals(sd.getKind())) {
						targetDiagram = sd;
					}
				}
			}
		}
	};

	void setSiteSelection() {
		if (getSite() == null) {
			return;
		}
		getSite().getWorkbenchWindow().getSelectionService()
				.addSelectionListener(selectionListener);

		// SelectionProviderを登録(プロパティ・ビュー連携)
		getSite().setSelectionProvider(new ISelectionProvider() {
			public void addSelectionChangedListener(
					ISelectionChangedListener listener) {
			}

			public ISelection getSelection() {
				StructuredSelection result = null;
				if (targetDiagram != null) {
					result = new StructuredSelection(targetDiagram);
				}
				return result;
			}

			public void removeSelectionChangedListener(
					ISelectionChangedListener listener) {
			}

			public void setSelection(ISelection selection) {
			}
		});
	}

}
