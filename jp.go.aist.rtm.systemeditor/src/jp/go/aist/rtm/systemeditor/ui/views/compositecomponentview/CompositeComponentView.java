package jp.go.aist.rtm.systemeditor.ui.views.compositecomponentview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.go.aist.rtm.systemeditor.RTSystemEditorPlugin;
import jp.go.aist.rtm.systemeditor.nl.Messages;
import jp.go.aist.rtm.systemeditor.ui.editor.AbstractSystemDiagramEditor;
import jp.go.aist.rtm.systemeditor.ui.util.ComponentUtil;
import jp.go.aist.rtm.toolscommon.model.component.Component;
import jp.go.aist.rtm.toolscommon.model.component.ConfigurationSet;
import jp.go.aist.rtm.toolscommon.model.component.NameValue;
import jp.go.aist.rtm.toolscommon.model.component.Port;
import jp.go.aist.rtm.toolscommon.model.component.SystemDiagram;
import jp.go.aist.rtm.toolscommon.model.component.util.ComponentCommonUtil;
import jp.go.aist.rtm.toolscommon.ui.views.propertysheetview.RtcPropertySheetPage;
import jp.go.aist.rtm.toolscommon.util.AdapterUtil;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;

/**
 * 複合コンポーネントビュー
 *
 */
public class CompositeComponentView extends ViewPart {

	private static final int EXEC_BUTTON_WIDTH = 70;
	private static final int EXEC_WIDE_BUTTON_WIDTH = 100;

	private static final String COLOR_WHITE = "COLOR_WHITE";

	private static final String COLOR_TARGET = "COLOR_TARGET";

	private static final String COLOR_MODIFY = "COLOR_MODIFY";

	private static final String COLOR_REQUIRED = "COLOR_REQUIRED";

	private static final String PROPERTY_PORT_CHECK = "PROPERTY_PORT_CHECK";

	private static final String PROPERTY_COMPONENT_NAME = "PROPERTY_COMPONENT_NAME";

	private static final String PROPERTY_PORT_NAME = "PROPERTY_PORT_NAME";

	private static final String COLUMN_CHECK_LABEL = "";

	private static final String BUTTON_APPLY_LABEL = Messages.getString("Common.button.apply"); // Apply

	private static final String BUTTON_CANCEL_LABEL = Messages.getString("Common.button.cancel"); // Cancel

	private static final String CONFIRM_APPLY_TITLE = Messages.getString("Common.dialog.confirm_title"); // Confirm

	private static final String CONFIRM_APPLY_MESSAGE = Messages.getString("CompositeComponentView.confirm.apply_message"); // Apply changes?

	private Component targetComponent;

	private Port targetPort;

	private List<PortEntry> portList;

	private List<String> requiredExportedPorts;

	private Composite composite;

	private TableViewer portTableViewer;

	private Table portTable;

	private Button applyButton;

	private Button cancelButton;

	private Text nameText;

	private Text typeText;

	private static ColorRegistry colorRegistry = null;

	public CompositeComponentView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		if (colorRegistry == null) {
			colorRegistry = new ColorRegistry();
			colorRegistry.put(COLOR_WHITE, new RGB(255, 255, 255));
			colorRegistry.put(COLOR_TARGET, new RGB(255, 255, 128));
			colorRegistry.put(COLOR_MODIFY, new RGB(255, 192, 192));
			colorRegistry.put(COLOR_REQUIRED, new RGB(192, 192, 192));
		}

		GridLayout gl;
		GridData gd;

		gl = new GridLayout();
		gl.numColumns = 5;
		composite = new Composite(parent, SWT.FILL);
		composite.setLayout(gl);

		final Label nameLabel = new Label(composite, SWT.NONE);
		gd = new GridData();
		gd.horizontalSpan = 0;
		gd.verticalSpan = 0;
		nameLabel.setLayoutData(gd);
		nameLabel.setText("component:");

		nameText = new Text(composite, SWT.BORDER);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 0;
		gd.verticalSpan = 0;
		nameText.setLayoutData(gd);
		nameText.setEditable(false);
		nameText.setBackground(colorRegistry.get(COLOR_WHITE));

		final Label typeLabel = new Label(composite, SWT.NONE);
		gd = new GridData();
		gd.horizontalSpan = 0;
		gd.verticalSpan = 0;
		typeLabel.setLayoutData(gd);
		typeLabel.setText("type:");

		typeText = new Text(composite, SWT.BORDER);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 0;
		gd.verticalSpan = 0;
		typeText.setLayoutData(gd);
		typeText.setEditable(false);
		typeText.setBackground(colorRegistry.get(COLOR_WHITE));
	
		final Composite listComposite = new Composite(composite, SWT.FILL);
		gl = new GridLayout();
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		gl.numColumns = 3;
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalSpan = 4;
		listComposite.setLayout(gl);
		listComposite.setLayoutData(gd);

		portTableViewer = new TableViewer(listComposite, SWT.FULL_SELECTION
				| SWT.SINGLE | SWT.BORDER);
		portTableViewer.setContentProvider(new ArrayContentProvider());
		portTableViewer.setContentProvider(new ArrayContentProvider());
		portTableViewer.setColumnProperties(new String[] { PROPERTY_PORT_CHECK,
				PROPERTY_COMPONENT_NAME, PROPERTY_PORT_NAME });
		portTableViewer.setLabelProvider(new PortEntryLabelProvider());
		portTableViewer.setCellModifier(new PortTableCellModifier(
				portTableViewer));
		portTableViewer
				.setCellEditors(new CellEditor[] {
						new CheckboxCellEditor(portTableViewer.getTable()),
						null, null });

		portTable = portTableViewer.getTable();
		gl = new GridLayout(1, false);
		gl.numColumns = 3;
		gd = new GridData();
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 2;
		portTable.setLayout(gl);
		portTable.setLayoutData(gd);
		portTable.setLinesVisible(true);
		portTable.setHeaderVisible(true);

		final TableColumn checkCol = new TableColumn(portTable, SWT.NONE);
		checkCol.setText(COLUMN_CHECK_LABEL);
		checkCol.setWidth(40);
		final TableColumn compCol = new TableColumn(portTable, SWT.NONE);
		compCol.setText("component");
		compCol.setWidth(200);
		final TableColumn portCol = new TableColumn(portTable, SWT.NONE);
		portCol.setText("port");
		portCol.setWidth(200);
	
		final Composite execButtonComposite = new Composite(composite, SWT.NONE);
		gl = new GridLayout();
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.FILL;
		execButtonComposite.setLayout(gl);
		execButtonComposite.setLayoutData(gd);

		applyButton = new Button(execButtonComposite, SWT.TOP);
		applyButton.setText(BUTTON_APPLY_LABEL);
		gd = new GridData();
		gd.widthHint = EXEC_WIDE_BUTTON_WIDTH;
		applyButton.setLayoutData(gd);
		applyButton.setEnabled(false);
		applyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applyData();
			}
		});

		cancelButton = new Button(execButtonComposite, SWT.TOP);
		cancelButton.setText(BUTTON_CANCEL_LABEL);
		gd = new GridData();
		gd.widthHint = EXEC_WIDE_BUTTON_WIDTH;
		cancelButton.setLayoutData(gd);
		cancelButton.setEnabled(false);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				buildData();
			}
		});

		setSiteSelection();
	}

	@Override
	public void dispose() {
		getSite().getWorkbenchWindow().getSelectionService()
				.removeSelectionListener(selectionListener);
		super.dispose();
	}

	@Override
	public void setFocus() {
	}

	@SuppressWarnings("unchecked")
	private void applyData() {
		boolean isModified = false;
		for (PortEntry entry : this.portList) {
			if (entry.isModified) {
				isModified = true;
				break;
			}
		}
		if (!isModified) {
			return;
		}

		if (getViewSite() != null) {
			boolean isOk = MessageDialog.openConfirm(getViewSite().getShell(),
					CONFIRM_APPLY_TITLE, CONFIRM_APPLY_MESSAGE);
			if (isOk == false) {
				return;
			}
		}

		String comma = "";
		String exports = "";
		for (String s : this.requiredExportedPorts) {
			exports += comma;
			comma = ",";
			exports += s;
		}
		for (PortEntry entry : this.portList) {
			if (!entry.isRequired()
					&& entry.checked) {
				exports += comma;
				comma = ",";
				exports += entry.toConfigString();
			}
		}

		ConfigurationSet cso = this.targetComponent.getActiveConfigurationSet();
		ConfigurationSet csc = ComponentUtil.cloneConfigurationSet(cso);
		if (csc != null && csc.getConfigurationData() != null) {
			for (Object o : csc.getConfigurationData()) {
				NameValue nv = (NameValue) o;
				if (!nv.getName().equals("exported_ports")) {
					continue;
				}
				nv.setValue(exports);
			}
		}
		this.targetComponent.updateConfigurationSetR(csc, true);
		if (targetComponent.inOnlineSystemDiagram()) {
			waitSynchronize();
			targetComponent.getSynchronizationSupport().synchronizeLocal();
		} else {
			// オフラインの場合はexported_portsから公開ポートを設定
			targetComponent.addComponentsR(new ArrayList());
		}

		buildData();

		refreshDiagram();
	}

	private void refreshDiagram() {
		EObject container = targetComponent.eContainer();
		if (!(container instanceof SystemDiagram)) return;
		AbstractSystemDiagramEditor editor = ComponentUtil.findEditor((SystemDiagram) container);
		if (editor == null) return;
		editor.refresh();
	}

	private void buildData() {
		applyButton.setEnabled(false);
		cancelButton.setEnabled(false);
		refreshData();
		if (this.targetComponent != null) {
			applyButton.setEnabled(true);
			cancelButton.setEnabled(true);
		}
	}

	@SuppressWarnings("unchecked")
	private void refreshData() {
		nameText.setText("");
		typeText.setText("");
		portTableViewer.setInput(Collections.EMPTY_LIST);
		if (portList == null) {
			portList = new ArrayList<PortEntry>();
		}
		portList.clear();
		if (requiredExportedPorts == null) {
			requiredExportedPorts = new ArrayList<String>();
		}
		requiredExportedPorts.clear();

		if (this.targetComponent != null) {
			nameText.setText(this.targetComponent.getInstanceNameL());
			typeText.setText(this.targetComponent.getCompositeTypeL());

			List<String> exports = targetComponent.getExportedPorts();
			requiredExportedPorts = ComponentCommonUtil
					.getRequiredExportedPorts(this.targetComponent
							.getComponents());
			for (Object o1 : this.targetComponent.getComponents()) {
				if (!(o1 instanceof Component)) {
					continue;
				}
				Component ac = (Component) o1;
				for (Object o2 : ac.getPorts()) {
					if (!(o2 instanceof Port)) {
						continue;
					}
					Port port = (Port) o2;
					PortEntry entry = new PortEntry();
					entry.componentName = ac.getInstanceNameL();
					entry.portName = port.getNameL();
					if (exports.contains(entry.toConfigString())) {
						entry.checked = true;
					}
					entry.isTarget = this.isTargetPort(port);
					portList.add(entry);
				}
			}
			portTableViewer.setInput(portList);
		}
	}

	private boolean isTargetPort(Port port) {
		if (this.targetPort == null) {
			return false;
		}
		if (this.targetPort.getOriginalPortString().equals(
				port.getOriginalPortString())) {
			return true;
		}
		return false;
	}

	void waitSynchronize() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// void
		}
	}

	/** ポート一覧のエントリ */
	public class PortEntry {
		boolean checked = false;

		String componentName = "";

		String portName = "";

		boolean isTarget = false;

		boolean isModified = false;
		
		boolean isRequired() {
			return requiredExportedPorts.contains(toConfigString());
		}

		public String toConfigString() {
			return this.portName;
		}
	}

	/** ポート一覧表示のLabelProvider */
	public class PortEntryLabelProvider extends LabelProvider implements
			ITableLabelProvider, ITableColorProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			Image result = null;
			PortEntry entry = (PortEntry) element;
			if (columnIndex == 0) {
				if (entry.checked) {
					result = RTSystemEditorPlugin
							.getCachedImage("icons/checkbox_checked.png");
				} else {
					result = RTSystemEditorPlugin
							.getCachedImage("icons/checkbox_unchecked.png");
				}
			}
			return result;
		}

		public String getColumnText(Object element, int columnIndex) {
			PortEntry entry = (PortEntry) element;
			if (columnIndex == 1) {
				return entry.componentName;
			} else if (columnIndex == 2) {
				return entry.portName;
			}
			return null;
		}

		public Color getBackground(Object element, int columnIndex) {
			PortEntry entry = (PortEntry) element;
			if (entry.isTarget) {
				return colorRegistry.get(COLOR_TARGET);
			}
			if (entry.isModified) {
				return colorRegistry.get(COLOR_MODIFY);
			}
			if (entry.isRequired()) {
				return colorRegistry.get(COLOR_REQUIRED);
			}
			return null;
		}

		public Color getForeground(Object element, int columnIndex) {
			return null;
		}
	}

	/** ポート一覧のCellModifier */
	public class PortTableCellModifier implements ICellModifier {
		private TableViewer viewer;

		public PortTableCellModifier(TableViewer viewer) {
			this.viewer = viewer;
		}

		public boolean canModify(Object element, String property) {
			return true;
		}

		public Object getValue(Object element, String property) {
			Object result = null;
			PortEntry entry = (PortEntry) element;
			if (PROPERTY_PORT_CHECK.equals(property)) {
				result = Boolean.valueOf(entry.checked);
			} else if (PROPERTY_COMPONENT_NAME.equals(property)) {
				result = entry.componentName;
			} else if (PROPERTY_PORT_NAME.equals(property)) {
				result = entry.portName;
			}
			return result;
		}

		public void modify(Object element, String property, Object value) {
			if (element instanceof Item) {
				element = ((Item) element).getData();
			}
			PortEntry entry = (PortEntry) element;
			if (PROPERTY_PORT_CHECK.equals(property)) {
				if (entry.isRequired()) {
					entry.checked = true;
				} else {
					boolean newChecked = ((Boolean) value).booleanValue();
					if (entry.checked != newChecked) {
						entry.isModified = true;
					}
					entry.checked = newChecked;
				}
			} else if (PROPERTY_COMPONENT_NAME.equals(property)) {
				entry.componentName = (String) value;
			} else if (PROPERTY_PORT_NAME.equals(property)) {
				entry.portName = (String) value;
			}
			viewer.update(element, null);
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.equals(IPropertySheetPage.class)) {
			return new RtcPropertySheetPage();
		}
		return super.getAdapter(adapter);
	}

	private ISelectionListener selectionListener = new ISelectionListener() {
		@SuppressWarnings("unchecked")
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			targetComponent = null;
			targetPort = null;
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection ss = (IStructuredSelection) selection;
				Object firstElement = ss.getFirstElement();
				Class[] classes = new Class[] { Component.class,
						Port.class };
				Object adapter = null;
				for (Class klass : classes) {
					adapter = AdapterUtil.getAdapter(firstElement, klass);
					if (adapter != null) {
						break;
					}
				}
				if (adapter != null) {
					Component ac = null;
					Port port = null;
					if (adapter instanceof Component) {
						// コンポーネント選択時
						ac = (Component) adapter;
					} else if (adapter instanceof Port) {
						// ポート選択時は、eContainerからコンポーネントを取得
						port = (Port) adapter;
						if (port.eContainer() != null
								&& port.eContainer() instanceof Component) {
							ac = (Component) port.eContainer();
						}
					}
					if (ac != null) {
						ac.synchronizeManually();
						if (ac.isCompositeComponent()) {
							targetComponent = ac;
							targetPort = port;
						}
					}
				}
			}
			buildData();
		}
	};

	private void setSiteSelection() {
		if (getSite() == null) {
			return;
		}

		// NameServiceViewの選択監視リスナを登録
		getSite().getWorkbenchWindow().getSelectionService()
				.addSelectionListener(selectionListener);

		// SelectionProviderを登録(プロパティ・ビュー連携)
		getSite().setSelectionProvider(new ISelectionProvider() {
			public void addSelectionChangedListener(
					ISelectionChangedListener listener) {
			}

			public ISelection getSelection() {
				StructuredSelection result = null;
				if (targetComponent != null) {
					result = new StructuredSelection(targetComponent);
				}
				return result;
			}

			public void removeSelectionChangedListener(
					ISelectionChangedListener listener) {
			}

			public void setSelection(ISelection selection) {
			}
		});

		selectionListener.selectionChanged(null, getSite().getWorkbenchWindow()
				.getSelectionService().getSelection());
	}
}
