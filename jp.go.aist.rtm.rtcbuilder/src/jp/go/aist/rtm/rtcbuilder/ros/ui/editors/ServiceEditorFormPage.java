package jp.go.aist.rtm.rtcbuilder.ros.ui.editors;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.nl.Messages;
import jp.go.aist.rtm.rtcbuilder.ros.IRtcBuilderConstantsROS;
import jp.go.aist.rtm.rtcbuilder.ros.param.ROSParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.ServiceParam;
import jp.go.aist.rtm.rtcbuilder.ui.editors.AbstractEditorFormPage;
import jp.go.aist.rtm.rtcbuilder.ui.editors.IMessageConstants;
import jp.go.aist.rtm.rtcbuilder.util.StringUtil;

/**
 * Serviceページ
 */
public class ServiceEditorFormPage extends AbstractEditorFormPage {

	private TableViewer serverTableViewer;
	private TableViewer clientTableViewer;
	//
	private Text serviceNameText;
	private Combo serviceTypeCombo;
	private Text typePackageText;
	private Text variableNameText;
	
	private Text descriptionText;
	private Text argumentText;
	private Text returnText;
	//
	private ServiceParam preSelection;
	private ServiceParam selectParam;
	//
	private List<String> typeList = new ArrayList<String>();
	private List<String> currentList = new ArrayList<String>();

	/**
	 * コンストラクタ
	 *
	 * @param editor
	 *            親のエディタ
	 */
	public ServiceEditorFormPage(ROSBuilderEditor editor) {
		super(editor, "id", Messages.getString("IMC.ROS_SERVICE_SECTION"));
		//
		preSelection = null;
		updateDefaultValue();
	}

	public void updateDefaultValue() {
		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();
		typeList.clear();
		typeList.addAll(extractROSEtcTypes(rosParam.getOutputProject(), "srv"));
		typeList.sort(null);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = super.createBase(managedForm, Messages.getString("IMC.ROS_SERVICE_SECTION"));
		FormToolkit toolkit = managedForm.getToolkit();
		//
		final Composite composite = createSectionBaseWithLabel(toolkit, form,
				Messages.getString("IMC.ROS_SERVICE_TITLE"), Messages.getString("IMC.ROS_SERVICE_EXPL"), 4);
		
		createLabel(toolkit, composite,
				IMessageConstants.REQUIRED + Messages.getString("IMC.ROS_SERVICE_TBLLBL_SERVER"),
				2,
				getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_RED));
		createLabel(toolkit, composite,
				IMessageConstants.REQUIRED + Messages.getString("IMC.ROS_SERVICE_TBLLBL_CLENT"),
				2,
				getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_RED));

		serverTableViewer = createPortSection(toolkit, composite, "", 0,
				IRtcBuilderConstantsROS.SPEC_SERVICE_SERVER);
		clientTableViewer = createPortSection(toolkit, composite, "", 1,
				IRtcBuilderConstantsROS.SPEC_SERVICE_CLIENT);
		createHintSection(toolkit, form);

		createDetailSection(toolkit, form);
		//
		// 言語・環境ページより先にこのページが表示された場合、ここで言語を判断する
		editor.setEnabledInfoByLang();

		load();
	}

	private void createHintSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createHintSectionBase(toolkit, form, 2);
		//
		createHintLabel(Messages.getString("IMC.ROS_SERVICE_HINT_SERVICE_TITLE"), IMessageConstantsROS.SERVICE_HINT_SERVICE_EXPL, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_SERVICE_HINT_SERVER_TITLE"), Messages.getString("IMC.ROS_SERVICE_HINT_SERVER_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_SERVICE_HINT_CLIENT_TITLE"), Messages.getString("IMC.ROS_SERVICE_HINT_CLIENT_DESC"), toolkit, composite);
		createHintSpace(toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_SERVICE_LBL_SERVICENAME"), Messages.getString("IMC.ROS_SERVICE_HINT_SERVICE_NAME_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_SERVICE_LBL_SERVICETYPE"), Messages.getString("IMC.ROS_SERVICE_HINT_SERVICE_NAME_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_SERVICE_LBL_PACKAGE_TITLE"), IMessageConstantsROS.SERVICE_HINT_SERVICE_PACKAGE_EXPL, toolkit, composite);
		createHintLabel(IMessageConstantsROS.SERVICE_CALLBACK_LBL, "", toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_SERVICE_HINT_VARNAME_SERVER_TITLE"), IMessageConstantsROS.SERVICE_HINT_VAR_SERVER_EXPL, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_SERVICE_HINT_VARNAME_CLIENT_TITLE"), IMessageConstantsROS.SERVICE_HINT_VAR_CLIENT_EXPL, toolkit, composite);
		//
		createHintSpace(toolkit, composite);
		createHintLabel(Messages.getString("IMC.HINT_DOCUMENT_TITLE"), "", toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_TOPIC_LBL_DESCRIPTION"), IMessageConstantsROS.SERVICE_HINT_OVERVIEW_EXPL, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_SERVICE_LBL_ARGUMENT"), IMessageConstantsROS.SERVICE_HINT_ARGUMENT_EXPL, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_SERVICE_LBL_RETURN"), IMessageConstantsROS.SERVICE_HINT_RETURN_EXPL, toolkit, composite);
	}

	private void createDetailSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createSectionBaseWithLabel(toolkit, form,
				"Detail", IMessageConstantsROS.SERVICE_DOCUMENT_EXPL, 2, 2);
		//
		serviceNameText = createLabelAndRefText(toolkit, composite,
				Messages.getString("IMC.ROS_SERVICE_LBL_SERVICENAME"), SWT.BORDER, 1);
		//
		Group detailGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		detailGroup.setLayout(new GridLayout(5, false));
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		detailGroup.setLayoutData(gd);
		//
		Label label = toolkit.createLabel(detailGroup, IMessageConstants.REQUIRED + Messages.getString("IMC.ROS_SERVICE_LBL_SERVICETYPE"));
		label.setForeground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_RED));
		serviceTypeCombo = new Combo(detailGroup, SWT.DROP_DOWN);
		/////
		currentList.clear();
		currentList.addAll(typeList);
		for(String item : currentList) {
			serviceTypeCombo.add(item);
		}
		/////
		serviceTypeCombo.setText("");
		serviceTypeCombo.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent e) {
				String target = serviceTypeCombo.getText();
				String[] keyList = target.split(" ");
				currentList.clear();
				for (String each : typeList) {
					boolean isHit = true;
					for(String itemKey: keyList) {
					  if (each.contains(itemKey)==false) {
						  isHit = false;
						  break;
					  }
					}
					if (isHit) {
						currentList.add(each);
					}
				}
				currentList.sort(null);
				serviceTypeCombo.removeAll();
				for(String item : currentList) {
					serviceTypeCombo.add(item);
				}
				serviceTypeCombo.setText(target);
				serviceTypeCombo.setSelection(new Point(serviceTypeCombo.getText().length(), serviceTypeCombo.getText().length()) );
			}
			public void keyPressed(KeyEvent e) { }
		});
		GridData gdcombo = new GridData(GridData.FILL_HORIZONTAL);
		gdcombo.horizontalSpan = 2;
		serviceTypeCombo.setLayoutData(gdcombo);

		Button selectButton = toolkit.createButton(detailGroup, "Select", SWT.PUSH);
		selectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		        FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
		        fileDialog.setText("Select srv file");
		        fileDialog.setFilterExtensions(new String[] { "*.srv" });
		        fileDialog.setFilterNames(new String[] { "Srv Files (*.srv)" });
		        String selectedPath = fileDialog.open();
		        if (selectedPath == null) return;
		        
	        	File srcFile = new File(selectedPath);
	        	
	    		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();
	    		IWorkspaceRoot workspaceHandle = ResourcesPlugin.getWorkspace().getRoot();
	    		IProject project = workspaceHandle.getProject(rosParam.getOutputProject());
	    		IFolder targetFolder = project.getFolder("srv");
	    		IFile destFile = targetFolder.getFile(srcFile.getName());
	    		if(destFile.exists()) {
					MessageBox message = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					message.setText("File Copy");
					message.setMessage(Messages.getString("IMC.FILE_OVERWRITE"));
					if( message.open() != SWT.YES) return;
	    		}

	    		try (FileInputStream fis = new FileInputStream(srcFile)) {
	                if (destFile.exists()) {
	                    destFile.setContents(fis, IResource.FORCE, new NullProgressMonitor());
	                } else {
	                    destFile.create(fis, IResource.NONE, new NullProgressMonitor());
	                }
	                targetFolder.refreshLocal(IResource.DEPTH_ONE, null);
	            } catch (IOException e1) {
	            } catch (CoreException e2) {
	            }
	    		
	    		updateDefaultValue();
				serviceTypeCombo.removeAll();
				currentList.clear();
				currentList.addAll(typeList);
				for(String item : currentList) {
					serviceTypeCombo.add(item);
				}
			}
		});
		
		Button reloadButton = toolkit.createButton(detailGroup, "ReLoad", SWT.PUSH);
		reloadButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
	    		updateDefaultValue();
	    		serviceTypeCombo.removeAll();
				currentList.clear();
				currentList.addAll(typeList);
				for(String item : currentList) {
					serviceTypeCombo.add(item);
				}

				preSelection = null;
				typePackageText.setText("");
				if(0 < serviceTypeCombo.getItemCount()) {
					serviceTypeCombo.select(0);
				}
			}
		});
		//
		typePackageText = createLabelAndText(toolkit, detailGroup,
						IMessageConstants.REQUIRED + Messages.getString("IMC.ROS_SERVICE_LBL_PACKAGE_TITLE"),
				 		SWT.BORDER, SWT.COLOR_RED, 2, 1);
		toolkit.createLabel(detailGroup, "");
		/////
		variableNameText = createLabelAndText(toolkit, detailGroup,
				Messages.getString("IMC.ROS_SERVICE_LBL_VARNAME"), SWT.BORDER, SWT.COLOR_BLACK, 3, 2);

		/////
		Group documentGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		documentGroup.setLayout(new GridLayout(2, false));
		documentGroup.setText("Documentation");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		documentGroup.setLayoutData(gd);
		//
		descriptionText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.ROS_TOPIC_LBL_DESCRIPTION"), SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 50;
		descriptionText.setLayoutData(gridData);
		argumentText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.ROS_SERVICE_LBL_ARGUMENT"), SWT.BORDER);
		returnText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.ROS_SERVICE_LBL_RETURN"), SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		returnText.setLayoutData(gridData);
	}

	private TableViewer createPortSection(FormToolkit toolkit, Composite parent,
			String columnLabel, final int initSel, String role) {

		final TableViewer portParamTableViewer = createTableViewer(toolkit,	parent, 70);
		portParamTableViewer.getTable().setHeaderVisible(false);

		final TableViewerColumn col = super.createColumn(portParamTableViewer, columnLabel, IRtcBuilderConstants.SINGLE_COLUMN_WIDTH);
		col.setEditingSupport(new ServiceEditingSuport(portParamTableViewer));
//		col.getColumn().setResizable(false);
		portParamTableViewer.setLabelProvider(new ServiceParamLabelProvider());
		//
		parent.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Point size = portParamTableViewer.getControl().getSize();
				ScrollBar vBar = portParamTableViewer.getTable().getVerticalBar();
				col.getColumn().setWidth(size.x- vBar.getSize().x*2);
			}
		});
		//
		Composite buttonComposite = toolkit.createComposite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.marginWidth = 1;
		buttonComposite.setLayout(gl);
		GridData gd = new GridData();
		gd.verticalAlignment = SWT.BEGINNING;
		gd.widthHint = 80;
		buttonComposite.setLayoutData(gd);

		Button addButton = toolkit.createButton(buttonComposite, "Add", SWT.PUSH);
		addButton.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				String selected = serviceTypeCombo.getText();
				updateDefaultValue();
				ServiceParam selectParam = new ServiceParam(role);
				((List) portParamTableViewer.getInput()).add(selectParam);
				portParamTableViewer.refresh();
				update();
				portParamTableViewer.setSelection(new StructuredSelection(selectParam), true);
				serviceTypeCombo.setText(selected);
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		addButton.setLayoutData(gd);
		//
		Button deleteButton = toolkit.createButton(buttonComposite, "Delete", SWT.PUSH);
		deleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectionIndex = portParamTableViewer.getTable()
						.getSelectionIndex();
				if (selectionIndex >= 0
						&& ((List) portParamTableViewer.getInput()).size() >= selectionIndex + 1) {
					((List) portParamTableViewer.getInput())
							.remove(selectionIndex);
					portParamTableViewer.refresh();
					preSelection = null;
					clearText();
					update();
				}
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		deleteButton.setLayoutData(gd);
		//
		portParamTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				setDocumentContents();
				StructuredSelection selection = (StructuredSelection)event.getSelection();
				selectParam = (ServiceParam)selection.getFirstElement();
				if( selectParam != null ) {
					StringBuffer portName = new StringBuffer(selectParam.getName());
					if(event.getSource().equals(serverTableViewer)) {
						portName.append(" (Server)");
					} else {
						portName.append(" (Client)");
					}
					serviceNameText.setText(portName.toString());

					String strType = selectParam.getType();
					int lastSlashIndex = strType.lastIndexOf('/');
					if (lastSlashIndex == -1) {
						serviceTypeCombo.setText(selectParam.getType());
						typePackageText.setText("");
					} else {
						typePackageText.setText(strType.substring(0, lastSlashIndex));
						serviceTypeCombo.setText(strType.substring(lastSlashIndex + 1));
					}

					variableNameText.setText(selectParam.getVarCallbackName());
					descriptionText.setText(StringUtil.getDisplayDocText(selectParam.getDocDescription()));
					argumentText.setText(StringUtil.getDisplayDocText(selectParam.getDocArgument()));
					returnText.setText(StringUtil.getDisplayDocText(selectParam.getDocReturn()));
					preSelection = selectParam;
				}
			}
		});

		return portParamTableViewer;
	}

	public void update() {
		if (selectParam != null) {
			String typePackage = typePackageText.getText(); 
			if(typePackage == null || typePackage.length() == 0) {
				selectParam.setType(serviceTypeCombo.getText());
			} else {
				selectParam.setType(typePackage + "/" + serviceTypeCombo.getText());
			}
			selectParam.setVarCallbackName(variableNameText.getText());
			
			selectParam.setDocDescription(StringUtil.getDocText(descriptionText.getText()));
			selectParam.setDocArgument(StringUtil.getDocText(argumentText.getText()));
			selectParam.setDocReturn(StringUtil.getDocText(returnText.getText()));
		}
		//
		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();
		((ROSBuilderEditor)editor).updateEMFPorts(
				rosParam.getTopicSubscribes(), rosParam.getTopicPublishes(),
				rosParam.getServiceServers(), rosParam.getServiceClients(),
				rosParam.getActionServers(), rosParam.getActionClients());
		((ROSBuilderEditor)editor).updateDirty();
	}

	public void updateForOutput() {
		update();
		setDocumentContents();
	}

	private void setDocumentContents() {
		if( preSelection != null ) {
			String typePackage = typePackageText.getText(); 
			if(typePackage == null || typePackage.length() == 0) {
				preSelection.setType(serviceTypeCombo.getText());
			} else {
				selectParam.setType(typePackage + "/" + serviceTypeCombo.getText());
			}
			//
			preSelection.setDocDescription(StringUtil.getDocText(descriptionText.getText()));
			preSelection.setDocArgument(StringUtil.getDocText(argumentText.getText()));
			preSelection.setDocReturn(StringUtil.getDocText(returnText.getText()));
		}
	}

	private void clearText() {
		serviceNameText.setText("");
		serviceTypeCombo.select(0);
		typePackageText.setText("");
		variableNameText.setText("");
		descriptionText.setText("");
		argumentText.setText("");
		returnText.setText("");
	}

	/**
	 * データをロードする
	 */
	public void load() {
		if (serverTableViewer == null) return;
		
		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();
		clientTableViewer.setInput(rosParam.getServiceClients());
		serverTableViewer.setInput(rosParam.getServiceServers());
		//Mac版では列の幅が最小化してしまうため，再度列幅を設定
		clientTableViewer.getTable().getColumn(0).setWidth(IRtcBuilderConstants.SINGLE_COLUMN_WIDTH);
		serverTableViewer.getTable().getColumn(0).setWidth(IRtcBuilderConstants.SINGLE_COLUMN_WIDTH);
		//
		StructuredSelection selection = (StructuredSelection) clientTableViewer
				.getSelection();
		ServiceParam outParam = (ServiceParam) selection.getFirstElement();
		selection = (StructuredSelection) serverTableViewer.getSelection();
		ServiceParam inParam = (ServiceParam) selection.getFirstElement();
		if (outParam == null && inParam == null) clearText();
		//
		((ROSBuilderEditor)editor).updateEMFPorts(
				rosParam.getTopicSubscribes(), rosParam.getTopicPublishes(),
				rosParam.getServiceServers(), rosParam.getServiceClients(),
				rosParam.getActionServers(), rosParam.getActionClients());
	}

	public String validateParam() {
		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();
		return rosParam.validateServiceInfo();
	}

	private class ServiceParamLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof ServiceParam == false) return null;
			ServiceParam param = (ServiceParam) element;
			return param.getName();
		}
	}

	private class ServiceEditingSuport extends EditingSupport {
		private ColumnViewer viewer;

		public ServiceEditingSuport(ColumnViewer viewer) {
			super(viewer);
			this.viewer = viewer;
		}

		@Override
		protected boolean canEdit(Object element) {
			return true;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return new TextCellEditor((Composite) viewer.getControl()) {
	            @Override
	            public LayoutData getLayoutData() {
	                LayoutData data = super.getLayoutData();
	                Control control = getControl();
	                if (control != null && !control.isDisposed()) {
	                    GC gc = new GC(control);
	                    int fontHeight = gc.getFontMetrics().getHeight();
	                    gc.dispose();
	                    
	                    data.minimumHeight = fontHeight + 4;
	                }
	                return data;
	            }
	        };
		}

		@Override
		protected Object getValue(Object element) {
			if (element instanceof ServiceParam == false) return null;
			ServiceParam param = (ServiceParam) element;
			return param.getName();
		}

		@Override
		protected void setValue(Object element, Object value) {
			if (element instanceof ServiceParam == false) return;
			ServiceParam param = (ServiceParam) element;

			param.setName((String) value);
			StringBuffer portName = new StringBuffer(param.getName());
			if( this.getViewer()==serverTableViewer ) {
				portName.append(" (Server)");
			} else {
				portName.append(" (Client)");
			}
			serviceNameText.setText(portName.toString());

			getViewer().update(element, null);
			update();
		}
	}
}
