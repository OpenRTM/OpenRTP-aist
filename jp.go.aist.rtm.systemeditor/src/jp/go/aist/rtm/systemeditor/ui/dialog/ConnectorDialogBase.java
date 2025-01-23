package jp.go.aist.rtm.systemeditor.ui.dialog;

import static jp.go.aist.rtm.systemeditor.nl.Messages.getString;

import java.util.ArrayList;
import java.util.List;

import jp.go.aist.rtm.systemeditor.nl.Messages;
import jp.go.aist.rtm.toolscommon.corba.CorbaUtil;
import jp.go.aist.rtm.toolscommon.model.component.Component;
import jp.go.aist.rtm.toolscommon.model.component.Port;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

public class ConnectorDialogBase extends TitleAreaDialog {

	static final int PROPERTY_NAME = 0;
	static final int PROPERTY_VALUE = 1;
	static final int EXEC_BUTTON_WIDTH = 70;
	static protected final String MSG_ERROR_PROPERTY_DUPLICATE = Messages.getString("ConnectorCreaterDialogBase.0");
	
	private List<AdditionalEntry> additional;
	
	public ConnectorDialogBase(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.CENTER | SWT.RESIZE);
		additional = new ArrayList<AdditionalEntry>();
	}

	protected TableViewer createAdditionalTableViewer(Composite parent) {
		Composite baseComposite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout(2, false);
		gl.numColumns = 2;
		GridData gd = new GridData();
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 2;
		gd.minimumHeight = 200;
		baseComposite.setLayout(gl);
		baseComposite.setLayoutData(gd);
		
		final TableViewer additionalTableViewer = new TableViewer(baseComposite, SWT.FULL_SELECTION | SWT.SINGLE | SWT.BORDER);
		additionalTableViewer.setContentProvider(new ArrayContentProvider());
		Table additionalTable = additionalTableViewer.getTable();
		
		gl = new GridLayout(1, false);
		gl.numColumns = 1;
		gd = new GridData();
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		gd.heightHint = 120;
		additionalTable.setLayout(gl);
		additionalTable.setLayoutData(gd);
		additionalTable.setLinesVisible(true);
		additionalTable.setHeaderVisible(true);
		
		TableViewerColumn col = null;
		col = createColumn(additionalTableViewer, "Name", 200);
		col.setEditingSupport(new AdditionalTableEdittingSupport(
				additionalTableViewer, PROPERTY_NAME));
		col = createColumn(additionalTableViewer, "Value", 200);
		col.setEditingSupport(new AdditionalTableEdittingSupport(
				additionalTableViewer, PROPERTY_VALUE));

		additionalTableViewer
				.setLabelProvider(new AdditionalEntryLabelProvider());
		additionalTableViewer.setInput(additional);
		
		Composite buttonComposite = new Composite(baseComposite, SWT.NONE);
		gl = new GridLayout();
		gd = new GridData(GridData.FILL_VERTICAL);
		buttonComposite.setLayout(gl);
		buttonComposite.setLayoutData(gd);

		Button addButton = new Button(buttonComposite, SWT.TOP);
		addButton.setText(getString("Common.button.add"));
		gd = new GridData();
		gd.widthHint = EXEC_BUTTON_WIDTH;
		addButton.setLayoutData(gd);
		addButton.setEnabled(true);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AdditionalEntry entry = new AdditionalEntry();
				additional.add(entry);
				additionalTableViewer.refresh();
			}
		});

		Button deleteButton = new Button(buttonComposite, SWT.TOP);
		deleteButton.setText(getString("Common.button.delete"));
		gd = new GridData();
		gd.widthHint = EXEC_BUTTON_WIDTH;
		deleteButton.setLayoutData(gd);
		deleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectionIndex = additionalTableViewer.getTable().getSelectionIndex();
				if (selectionIndex >= 0
						&& ((List<?>) additionalTableViewer.getInput()).size() >= selectionIndex + 1) {
						((List<?>) additionalTableViewer.getInput()).remove(selectionIndex);
				additionalTableViewer.refresh();
				}
			}
		});
		
		return additionalTableViewer;
	}
	
	protected TableViewerColumn createColumn(TableViewer tv, String title, int width) {
		TableViewerColumn col = new TableViewerColumn(tv, SWT.NONE);
		col.getColumn().setText(title);
		col.getColumn().setWidth(width);
		col.getColumn().setResizable(true);
		col.getColumn().setMoveable(false);
		return col;
	}
	
	protected boolean checkProperties(List<AdditionalEntry> additional) {
		//重複チェック
		List<String> listAdd = new ArrayList<String>();
		for(AdditionalEntry target : additional) {
			if(listAdd.contains(target.getName().trim())==false) {
				listAdd.add(target.getName().trim());
			} else {
				setMessage(MSG_ERROR_PROPERTY_DUPLICATE,
						IMessageProvider.ERROR);
				return false;
			}
		}
		return true;
	}
	
	protected String getPortInfo(Port source, boolean withPort) {
		StringBuilder builder = new StringBuilder();
		
		String ip = getPortAddress(source, withPort);
		if(ip.length() == 0) return "";
		
		builder.append("(");
		builder.append(source.getNameL());
		builder.append(":");
		builder.append(ip);
		builder.append(")");
		
		return builder.toString();
	}
	
	protected String getPortAddress(Port source, boolean witPort) {
		Component component = (Component) source.eContainer();
		Object first = component.getSynchronizationSupport().getRemoteObjects()[0];
		if(first instanceof org.omg.CORBA.Object) {
			org.omg.CORBA.Object corbaObj = (org.omg.CORBA.Object)first;
			CorbaUtil.IORInfo info = CorbaUtil.getIORInfo(corbaObj);
			if( 0<info.taggedProfiles.size() ) {
				if(witPort) {
					return info.taggedProfiles.get(0).host + " : " + info.taggedProfiles.get(0).port;
				} else {
					return info.taggedProfiles.get(0).host;
				}
			}
		}
		return "";
	}

	public class AdditionalEntry {
		private String name;
		private String value;
		
		public AdditionalEntry() {
			name = "NewName";
			value = "NewValue";
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
	
	protected class AdditionalEntryLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if( (element instanceof AdditionalEntry) == false ) return "";
			AdditionalEntry entry = (AdditionalEntry) element;
			if (columnIndex == PROPERTY_NAME) {
				return entry.getName();
			} else if (columnIndex == PROPERTY_VALUE) {
				return entry.getValue();
			}
			return null;
		}
	}

	protected class AdditionalTableEdittingSupport extends EditingSupport {
		CellEditor editor;
		int column;

		public AdditionalTableEdittingSupport(ColumnViewer viewer, int column) {
			super(viewer);

			// Create the correct editor based on the column index
			this.column = column;
			switch (this.column) {
			case PROPERTY_NAME:
			case PROPERTY_VALUE:
				editor = new TextCellEditor(((TableViewer) viewer).getTable());
				break;
			default:
				break;
			}
		}

		@Override
		protected boolean canEdit(Object element) {
			return true;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return editor;
		}

		@Override
		protected Object getValue(Object element) {
			AdditionalEntry entry = (AdditionalEntry) element;
			//
			String label = null;
			if (column == PROPERTY_NAME) {
				label = entry.getName();
			} else if (column == PROPERTY_VALUE) {
				label = entry.getValue();
			}
			if (label == null) {
				return null;
			}
			return label;
		}

		@Override
		protected void setValue(Object element, Object value) {
			if (!(element instanceof AdditionalEntry)) {
				return;
			}
			AdditionalEntry entry = (AdditionalEntry) element;
			//
			if (column == PROPERTY_NAME) {
				entry.setName( (String) value);
			} else if (column == PROPERTY_VALUE) {
				entry.setValue( (String) value);
			}
			getViewer().update(element, null);
		}
	}
}
