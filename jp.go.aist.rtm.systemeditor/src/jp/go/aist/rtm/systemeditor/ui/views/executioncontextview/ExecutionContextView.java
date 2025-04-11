package jp.go.aist.rtm.systemeditor.ui.views.executioncontextview;

import static jp.go.aist.rtm.systemeditor.ui.util.UIUtil.COLOR_ERROR;
import static jp.go.aist.rtm.systemeditor.ui.util.UIUtil.COLOR_MODIFY;
import static jp.go.aist.rtm.systemeditor.ui.util.UIUtil.COLOR_WHITE;
import static jp.go.aist.rtm.systemeditor.ui.util.UIUtil.getColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.go.aist.rtm.systemeditor.nl.Messages;
import jp.go.aist.rtm.systemeditor.ui.dialog.SelectAttachComponentDialog;
import jp.go.aist.rtm.systemeditor.ui.editor.AbstractSystemDiagramEditor;
import jp.go.aist.rtm.systemeditor.ui.util.ComponentActionDelegate;
import jp.go.aist.rtm.toolscommon.model.component.Component;
import jp.go.aist.rtm.toolscommon.model.component.ComponentPackage;
import jp.go.aist.rtm.toolscommon.model.component.CorbaComponent;
import jp.go.aist.rtm.toolscommon.model.component.CorbaExecutionContext;
import jp.go.aist.rtm.toolscommon.model.component.ExecutionContext;
import jp.go.aist.rtm.toolscommon.model.component.SystemDiagram;
import jp.go.aist.rtm.toolscommon.model.component.SystemDiagramKind;
import jp.go.aist.rtm.toolscommon.ui.views.propertysheetview.RtcPropertySheetPage;
import jp.go.aist.rtm.toolscommon.util.AdapterUtil;

public class ExecutionContextView extends ViewPart {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionContextView.class);

	static final int EXEC_BUTTON_WIDTH = 80;
	static final int EXEC_WIDE_BUTTON_WIDTH = 120;

	static final String PROPERTY_EXECUTOIN_CONTEXT = "PROPERTY_EXECUTOIN_CONTEXT";
	static final String PROPERTY_NAME = "PROPERTY_NAME";
	static final String PROPERTY_VALUE = "PROPERTY_VALUE";

	static final String LABEL_BUTTON_APPLY = Messages.getString("Common.button.apply");
	static final String LABEL_BUTTON_START = Messages.getString("ExecutionContextView.7");
	static final String LABEL_BUTTON_STOP = Messages.getString("ExecutionContextView.8");
	static final String LABEL_BUTTON_ACTIVATE = Messages.getString("ExecutionContextView.9");
	static final String LABEL_BUTTON_DEACTIVATE = Messages.getString("ExecutionContextView.10");
	static final String LABEL_BUTTON_RESET = Messages.getString("ExecutionContextView.11");
	static final String LABEL_BUTTON_DETACH = Messages.getString("ExecutionContextView.12");
	static final String LABEL_BUTTON_ATTACH = Messages.getString("ExecutionContextView.13");

	static final String EC_PROPERTY_ID = "id";
	static final String EC_PROPERTY_KIND = "kind";
	static final String EC_PROPERTY_STATE = "state";
	static final String EC_PROPERTY_COMPONENT_STATE = "component_state";
	static final String EC_PROPERTY_OWNER = "owner";
	static final String EC_PROPERTY_PARTICIPANTS = "participants";

	static final String EC_OWNED_PREFIX = "owned";
	static final String EC_PARTICIPATE_PREFIX = "participate";

	static final String UNKNOWN = "<unknown>";

	Label componentNameLabel;

	TableViewer eclistTableViewer;
	Table eclistTable;

	Text rateText;
	TableViewer ecdetailTableViewer;
	Table ecdetailTable;

	Button applyButton;
	Button startButton;
	Button stopButton;
	Button activateButton;
	Button deactivateButton;
	Button resetButton;
	Button detachButton;
	Button attachButton;

	Component targetComponent;
	AbstractSystemDiagramEditor targetEditor;

	ECList eclist;
	String selectedECName;

	ComponentActionDelegate actionDelegate;

	public ExecutionContextView() {
		actionDelegate = new ComponentActionDelegate();
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl;
		GridData gd;

		gl = new GridLayout();
		gl.numColumns = 3;
		parent.setLayout(gl);

		Label nameLabel = new Label(parent, SWT.NONE);
		gd = new GridData();
		nameLabel.setLayoutData(gd);
		nameLabel.setText("component:");

		componentNameLabel = new Label(parent, SWT.BORDER);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		componentNameLabel.setLayoutData(gd);
		componentNameLabel.setBackground(getColor(COLOR_WHITE));

		createButtonComposite(parent);

		SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalSpan = 2;
		sashForm.setLayoutData(gd);

		createECListPart(sashForm);

		createECDetailPart(sashForm);

		sashForm.setWeights(new int[] { 30, 70 });

		setSiteSelection();
	}

	Composite createECListPart(SashForm sash) {
		GridLayout gl;
		GridData gd;

		Composite composite = new Composite(sash, SWT.FILL);
		gl = new GridLayout();
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		gl.numColumns = 1;
		composite.setLayout(gl);

		eclistTableViewer = new TableViewer(composite, SWT.FULL_SELECTION
				| SWT.SINGLE | SWT.BORDER);
		eclistTableViewer
				.setColumnProperties(new String[] { PROPERTY_EXECUTOIN_CONTEXT });
		eclistTableViewer.setContentProvider(new ArrayContentProvider());

		eclistTable = eclistTableViewer.getTable();
		eclistTable.setLinesVisible(true);
		eclistTable.setHeaderVisible(true);
		gd = new GridData();
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		eclistTable.setLayoutData(gd);

		gl = new GridLayout(1, false);
		gl.numColumns = 1;
		eclistTable.setLayout(gl);

		TableColumn col = new TableColumn(eclistTable, SWT.RIGHT);
		col.setText("Execution Context");
		col.setWidth(120);

		return composite;
	}

	Composite createECDetailPart(SashForm sash) {
		GridLayout gl;
		GridData gd;

		Composite composite = new Composite(sash, SWT.FILL);
		gl = new GridLayout();
		gl.numColumns = 2;
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		composite.setLayout(gl);

		Label rateLabel = new Label(composite, SWT.NONE);
		rateLabel.setText("rate:");

		rateText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		rateText.setLayoutData(gd);
		rateText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				rateText.setBackground(getColor(COLOR_WHITE));
				if (eclist == null || selectedECName == null) {
					return;
				}
				String s = rateText.getText();
				Double d = null;
				try {
					d = Double.parseDouble(s);
				} catch (Exception ex) {
					// void
				}
				if (d == null || d < 0.0) {
					rateText.setBackground(getColor(COLOR_ERROR));
					return;
				}
				ECData data = eclist.getData(selectedECName);
				data.setRate(d);
				if (data.isModifiedRate()) {
					rateText.setBackground(getColor(COLOR_MODIFY));
				}
			}
		});

		ecdetailTableViewer = new TableViewer(composite, SWT.FULL_SELECTION
				| SWT.SINGLE | SWT.BORDER);
		ecdetailTableViewer.setColumnProperties(new String[] { PROPERTY_NAME,
				PROPERTY_VALUE });
		ecdetailTableViewer.setContentProvider(new ArrayContentProvider());
		ecdetailTableViewer.setLabelProvider(new ECPropertyLabelProvider());

		ecdetailTable = ecdetailTableViewer.getTable();
		ecdetailTable.setLinesVisible(true);
		eclistTable.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				refreshECDetailData();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				refreshECDetailData();
			}
		});

		gd = new GridData();
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 2;
		ecdetailTable.setLayoutData(gd);

		ecdetailTable.setHeaderVisible(true);

		TableColumn col = new TableColumn(ecdetailTable, SWT.LEFT);
		col.setText("Name");
		col.setWidth(120);

		col = new TableColumn(ecdetailTable, SWT.LEFT);
		col.setText("Value");
		col.setWidth(180);

		return composite;
	}

	Composite createButtonComposite(Composite parent) {
		GridLayout gl;
		GridData gd;

		Composite composite = new Composite(parent, SWT.NONE);
		gl = new GridLayout();
		composite.setLayout(gl);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.verticalSpan = 2;
		composite.setLayoutData(gd);

		applyButton = new Button(composite, SWT.NONE);
		applyButton.setText(LABEL_BUTTON_APPLY);
		gd = new GridData();
		gd.horizontalAlignment = SWT.END;
		gd.widthHint = EXEC_WIDE_BUTTON_WIDTH;
		applyButton.setLayoutData(gd);
		applyButton.setEnabled(false);
		applyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applyData();
			}
		});

		Label line = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		line.setLayoutData(gd);

		startButton = new Button(composite, SWT.NONE);
		startButton.setText(LABEL_BUTTON_START);
		gd = new GridData();
		gd.horizontalAlignment = SWT.END;
		gd.widthHint = EXEC_WIDE_BUTTON_WIDTH;
		startButton.setLayoutData(gd);
		startButton.setEnabled(false);
		startButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ECData data = eclist.datas.get(selectedECName);

				CorbaExecutionContext ec = null;
				if (data != null && data.ec instanceof CorbaExecutionContext) {
					ec = (CorbaExecutionContext) data.ec;
				}
				if (ec != null) {
					List<ComponentActionDelegate.Command> commands = ComponentActionDelegate.commandOf_START(LOGGER, ec,
							() -> {
								buildData();
								return 1;
							});
					actionDelegate.run(commands);
				}
			}
		});

		stopButton = new Button(composite, SWT.NONE);
		stopButton.setText(LABEL_BUTTON_STOP);
		gd = new GridData();
		gd.horizontalAlignment = SWT.END;
		gd.widthHint = EXEC_WIDE_BUTTON_WIDTH;
		stopButton.setLayoutData(gd);
		stopButton.setEnabled(false);
		stopButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ECData data = eclist.datas.get(selectedECName);

				CorbaExecutionContext ec = null;
				if (data != null && data.ec instanceof CorbaExecutionContext) {
					ec = (CorbaExecutionContext) data.ec;
				}
				if (ec != null) {
					List<ComponentActionDelegate.Command> commands = ComponentActionDelegate.commandOf_STOP(LOGGER, ec,
							() -> {
								buildData();
								return 1;
							});
					actionDelegate.run(commands);
				}
			}
		});

		activateButton = new Button(composite, SWT.NONE);
		activateButton.setText(LABEL_BUTTON_ACTIVATE);
		gd = new GridData();
		gd.horizontalAlignment = SWT.END;
		gd.widthHint = EXEC_WIDE_BUTTON_WIDTH;
		activateButton.setLayoutData(gd);
		activateButton.setEnabled(false);
		activateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ECData data = eclist.datas.get(selectedECName);

				CorbaExecutionContext ec = null;
				List<CorbaComponent> comps = new ArrayList<>();
				if (data != null && data.ec instanceof CorbaExecutionContext) {
					ec = (CorbaExecutionContext) data.ec;
				}
				if (targetComponent != null && targetComponent instanceof CorbaComponent) {
					comps.add((CorbaComponent) targetComponent);
				}
				if (ec != null) {
					List<ComponentActionDelegate.Command> commands = ComponentActionDelegate.commandOf_ACTIVATE(LOGGER,
							ec, comps, () -> {
								waitSynchronize();
								buildData();
								return 1;
							});
					actionDelegate.run(commands);
				}
			}
		});

		deactivateButton = new Button(composite, SWT.NONE);
		deactivateButton.setText(LABEL_BUTTON_DEACTIVATE);
		gd = new GridData();
		gd.horizontalAlignment = SWT.END;
		gd.widthHint = EXEC_WIDE_BUTTON_WIDTH;
		deactivateButton.setLayoutData(gd);
		deactivateButton.setEnabled(false);
		deactivateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ECData data = eclist.datas.get(selectedECName);

				CorbaExecutionContext ec = null;
				List<CorbaComponent> comps = new ArrayList<>();
				if (data != null && data.ec instanceof CorbaExecutionContext) {
					ec = (CorbaExecutionContext) data.ec;
				}
				if (targetComponent != null && targetComponent instanceof CorbaComponent) {
					comps.add((CorbaComponent) targetComponent);
				}
				if (ec != null) {
					List<ComponentActionDelegate.Command> commands = ComponentActionDelegate
							.commandOf_DEACTIVATE(LOGGER, ec, comps, () -> {
								waitSynchronize();
								buildData();
								return 1;
							});
					actionDelegate.run(commands);
				}
			}
		});

		resetButton = new Button(composite, SWT.NONE);
		resetButton.setText(LABEL_BUTTON_RESET);
		gd = new GridData();
		gd.horizontalAlignment = SWT.END;
		gd.widthHint = EXEC_WIDE_BUTTON_WIDTH;
		resetButton.setLayoutData(gd);
		resetButton.setEnabled(false);
		resetButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ECData data = eclist.datas.get(selectedECName);

				CorbaExecutionContext ec = null;
				List<CorbaComponent> comps = new ArrayList<>();
				if (data != null && data.ec instanceof CorbaExecutionContext) {
					ec = (CorbaExecutionContext) data.ec;
				}
				if (targetComponent != null && targetComponent instanceof CorbaComponent) {
					comps.add((CorbaComponent) targetComponent);
				}
				if (ec != null) {
					List<ComponentActionDelegate.Command> commands = ComponentActionDelegate.commandOf_RESET(LOGGER, ec,
							comps, () -> {
								waitSynchronize();
								buildData();
								return 1;
							});
					actionDelegate.run(commands);
				}
			}
		});

		detachButton = new Button(composite, SWT.NONE);
		detachButton.setText(LABEL_BUTTON_DETACH);
		gd = new GridData();
		gd.horizontalAlignment = SWT.END;
		gd.widthHint = EXEC_WIDE_BUTTON_WIDTH;
		detachButton.setLayoutData(gd);
		detachButton.setEnabled(false);
		detachButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ECData data = eclist.datas.get(selectedECName);

				CorbaExecutionContext ec = null;
				List<CorbaComponent> comps = new ArrayList<>();
				if (data != null && data.ec instanceof CorbaExecutionContext) {
					ec = (CorbaExecutionContext) data.ec;
				}
				if (targetComponent != null && targetComponent instanceof CorbaComponent) {
					comps.add((CorbaComponent) targetComponent);
				}
				if (ec != null) {
					List<ComponentActionDelegate.Command> commands = ComponentActionDelegate.commandOf_EC_DETACH(LOGGER,
							ec, comps, () -> {
								buildData();
								return 1;
							});
					actionDelegate.run(commands);
				}
			}
		});

		attachButton = new Button(composite, SWT.NONE);
		attachButton.setText(LABEL_BUTTON_ATTACH);
		gd = new GridData();
		gd.horizontalAlignment = SWT.END;
		gd.widthHint = EXEC_WIDE_BUTTON_WIDTH;
		attachButton.setLayoutData(gd);
		attachButton.setEnabled(false);
		attachButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ECData data = eclist.datas.get(selectedECName);
				SelectAttachComponentDialog dialog = new SelectAttachComponentDialog(getSite().getShell());
				dialog.setComponents(buildAttachComponents(data.ec));
				if (dialog.open() != IDialogConstants.OK_ID) {
					return;
				}

				CorbaExecutionContext ec = null;
				List<CorbaComponent> comps = new ArrayList<>();
				if (data != null && data.ec instanceof CorbaExecutionContext) {
					ec = (CorbaExecutionContext) data.ec;
				}
				if (dialog.getSelectedComponent() != null && dialog.getSelectedComponent() instanceof CorbaComponent) {
					comps.add((CorbaComponent) dialog.getSelectedComponent());
				}
				if (ec != null) {
					List<ComponentActionDelegate.Command> commands = ComponentActionDelegate.commandOf_EC_ATTACH(LOGGER,
							ec, comps, () -> {
								buildData();
								return 1;
							});
					actionDelegate.run(commands);
				}
			}
		});

		return composite;
	}

	/** attachするコンポーネントの候補リストを作成 */
	List<Component> buildAttachComponents(ExecutionContext ec) {
		List<Component> result = new ArrayList<>();
		if (getDiagram() == null) {
			return result;
		}
		// ECとダイアグラムのオンライン/オフラインの別が不一致の場合はスキップ
		SystemDiagramKind kind = getDiagram().getKind();
		if (ec instanceof CorbaExecutionContext) {
			if (!SystemDiagramKind.ONLINE_LITERAL.equals(kind)) {
				return result;
			}
		} else {
			if (!SystemDiagramKind.OFFLINE_LITERAL.equals(kind)) {
				return result;
			}
		}
		for (Component c : getDiagram().getRegisteredComponents()) {
			if (c.isGroupingCompositeComponent()) {
				// Grouping複合RTCはECを持たないので選択不可
				continue;
			}
			if (ec.isOwner(c) || ec.containsComponent(c)) {
				// ownerのRTC、attach済みのRTCは選択不可
				continue;
			}
			result.add(c);
		}
		return result;
	}

	/** 変更を反映 */
	void applyData() {
		if (eclist == null) {
			return;
		}
		if (selectedECName == null) {
			return;
		}
		ECData data = eclist.getData(selectedECName);
		if (!data.isModifiedRate()) {
			return;
		}
		if (data.ec == null) {
			return;
		}
		data.ec.setRateR(data.rate);
		if (data.ec.getSynchronizationSupport() != null) {
			if (data.ec.eContainer() instanceof CorbaComponent) {
				CorbaComponent cc = (CorbaComponent) data.ec.eContainer();
				cc.synchronizeRemoteAttribute(ComponentPackage.eINSTANCE
						.getCorbaComponent_RTCExecutionContexts());
			}
		}

		buildData();
	}

	void waitSynchronize() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// void
		}
	}

	/** 内部モデルを構築 */
	void buildData() {
		eclist = null;
		if (targetComponent != null) {
			eclist = new ECList();
			// owned context
			for (int i = 0; i < targetComponent.getExecutionContexts().size(); i++) {
				ExecutionContext ec = targetComponent.getExecutionContexts()
						.get(i);
				String name = EC_OWNED_PREFIX + Integer.toString(i);
				buildDataAddEC(name, ec);
			}
			// participation context
			for (int i = 0; i < targetComponent.getParticipationContexts()
					.size(); i++) {
				ExecutionContext ec = targetComponent
						.getParticipationContexts().get(i);
				String name = EC_PARTICIPATE_PREFIX + Integer.toString(i);
				buildDataAddEC(name, ec);
			}
		}

		refreshData();
	}

	/** 内部モデルにECを追加 */
	void buildDataAddEC(String name, ExecutionContext ec) {
		if (eclist.ecnames.contains(name)) {
			return;
		}
		eclist.ecnames.add(name);
		//
		ECData data = eclist.getData(name);
		data.ec = ec;
		data.setRate(ec.getRateL());
		//
		List<ECData.ECProperty> props = data.properties;
		ECData.ECProperty prop;
		// context id
		String id = null;
		if (name.startsWith(EC_OWNED_PREFIX)) {
			id = targetComponent.getExecutionContextHandler().getId(ec);
		} else if (name.startsWith(EC_PARTICIPATE_PREFIX)) {
			id = targetComponent.getParticipationContextHandler().getId(ec);
		}
		if (id == null || id.isEmpty()) {
			id = UNKNOWN;
		}
		prop = new ECData.ECProperty(EC_PROPERTY_ID, id);
		props.add(prop);
		// ec kind
		prop = new ECData.ECProperty(EC_PROPERTY_KIND, ec.getKindName());
		props.add(prop);
		if (ec instanceof CorbaExecutionContext) {
			// ec state
			prop = new ECData.ECProperty(EC_PROPERTY_STATE, ec.getStateName());
			props.add(prop);
			CorbaExecutionContext cec = (CorbaExecutionContext) ec;
			String state = cec.getComponentStateName(targetComponent);
			// component state
			prop = new ECData.ECProperty(EC_PROPERTY_COMPONENT_STATE, state);
			props.add(prop);
		}
		// ec owner
		String owner = (ec.getOwner() == null) ? UNKNOWN : ec.getOwner().getInstanceNameL();
		prop = new ECData.ECProperty(EC_PROPERTY_OWNER, owner);
		props.add(prop);
		// ec participants
		String parts = "";
		if (ec.getParticipants() == null || ec.getParticipants().isEmpty()) {
			parts = "None";
		} else {
			for (Component c : ec.getParticipants()) {
				if (!parts.isEmpty()) {
					parts += ",";
				}
				parts += (c.getInstanceNameL() == null) ? "Unresolved" : c.getInstanceNameL();
			}
		}
		prop = new ECData.ECProperty(EC_PROPERTY_PARTICIPANTS, parts);
		props.add(prop);
		// properties
		for (String key : ec.getPropertyKeys()) {
			prop = new ECData.ECProperty(key, ec.getProperty(key));
			props.add(prop);
		}
	}

	/** 内部モデルから表示 */
	void refreshData() {
		refreshECListData();
		refreshECDetailData();
	}

	/** EC一覧の表示 */
	void refreshECListData() {
		eclistTableViewer.setInput(Collections.EMPTY_LIST);
		componentNameLabel.setText("");

		applyButton.setEnabled(false);
		startButton.setEnabled(false);
		stopButton.setEnabled(false);
		activateButton.setEnabled(false);
		deactivateButton.setEnabled(false);
		resetButton.setEnabled(false);

		if (eclist != null) {
			componentNameLabel.setText(targetComponent.getInstanceNameL());
			eclistTableViewer.setInput(eclist.ecnames);
			if (eclistTable.getItemCount() <= 0) {
				return;
			}
			int index = 0;
			if (eclist.ecnames.contains(selectedECName)) {
				int ii = eclist.ecnames.indexOf(selectedECName);
				if (ii != -1) {
					index = ii;
				}
			}
			eclistTable.select(index);

			applyButton.setEnabled(true);
			if (targetComponent.hasComponentAction()) {
				startButton.setEnabled(true);
				stopButton.setEnabled(true);
				activateButton.setEnabled(true);
				deactivateButton.setEnabled(true);
				resetButton.setEnabled(true);
			}
		}
	}

	/** EC詳細の表示 */
	void refreshECDetailData() {
		ecdetailTableViewer.setInput(Collections.EMPTY_LIST);
		rateText.setText("");

		detachButton.setEnabled(false);
		attachButton.setEnabled(false);

		if (eclist != null) {
			int index = eclistTable.getSelectionIndex();
			if (index != -1) {
				selectedECName = eclist.ecnames.get(index);
				//
				ECData data = eclist.getData(selectedECName);
				rateText.setText(data.rate.toString());
				//
				ecdetailTableViewer.setInput(data.properties);

				if (!targetComponent.getParticipationContexts().contains(data.ec)) {
					// 選択中のコンポーネントが participantでなければ attach可
					attachButton.setEnabled(true);
				}
				if (!targetComponent.getExecutionContexts().contains(data.ec)) {
					// 選択中のコンポーネントが ownerでなければ detach可
					detachButton.setEnabled(true);
				}
			}
		}
	}

	@Override
	public void dispose() {
		getSite().getWorkbenchWindow().getSelectionService()
				.removeSelectionListener(selectionListener);
		super.dispose();
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.equals(IPropertySheetPage.class)) {
			return new RtcPropertySheetPage();
		}
		return super.getAdapter(adapter);
	}

	public SystemDiagram getDiagram() {
		if (targetEditor == null) {
			return null;
		}
		if (targetEditor.getSystemDiagram() == null) {
			targetEditor = null;
			return null;
		}
		return targetEditor.getSystemDiagram();
	}

	/** EC一覧を表すクラス */
	static class ECList {
		List<String> ecnames = new ArrayList<String>();
		Map<String, ECData> datas = new HashMap<String, ECData>();

		ECData getData(String name) {
			ECData data = datas.get(name);
			if (data == null) {
				data = new ECData();
				datas.put(name, data);
			}
			return data;
		}
	}

	/** ECを表すクラス */
	static class ECData {
		ExecutionContext ec;
		Double rate = 0.0;
		Double originalRate;
		List<ECProperty> properties = new ArrayList<ECProperty>();

		void setRate(Double d) {
			rate = d;
			if (originalRate == null) {
				originalRate = d;
			}
		}

		boolean isModifiedRate() {
			return rate.doubleValue() != originalRate.doubleValue();
		}

		/** ECのプロパティを表すクラス */
		static class ECProperty {
			String name;
			String value;

			ECProperty(String name, String value) {
				this.name = name;
				this.value = value;
			}
		}
	}

	/** ECプロパティ一覧表示のLabelProvider */
	public class ECPropertyLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			ECData.ECProperty entry = (ECData.ECProperty) element;
			if (columnIndex == 0) {
				return entry.name;
			} else if (columnIndex == 1) {
				return entry.value;
			}
			return null;
		}
	}

	/** CORBAの同期による変更通知を受け取るアダプタ */
	AdapterImpl eAdapter = new AdapterImpl() {
		@Override
		public void notifyChanged(Notification msg) {
			if (msg.getOldValue() == this || msg.getNewValue() == this) {
				return;
			}
			boolean update = false;
			//
			if (ComponentPackage.eINSTANCE
					.getCorbaComponent_RTCExecutionContexts().equals(
							msg.getFeature())) {
				update = true;
			}
			if (ComponentPackage.eINSTANCE
					.getCorbaComponent_RTCParticipationContexts().equals(
							msg.getFeature())) {
				update = true;
			}
			//
			if (ComponentPackage.eINSTANCE.getExecutionContext_StateL().equals(
					msg.getFeature())) {
				update = true;
			}
			if (ComponentPackage.eINSTANCE
					.getCorbaExecutionContext_RtcExecutionContextProfile()
					.equals(msg.getFeature())) {
				update = true;
			}
			if (!update) {
				return;
			}
			eclistTableViewer.getControl().getDisplay().asyncExec(
					new Runnable() {
						@Override
						public void run() {
							buildData();
						}
					});
		}
	};

	ISelectionListener selectionListener = new ISelectionListener() {
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if (!(selection instanceof IStructuredSelection)) {
				return;
			}
			IStructuredSelection ss = (IStructuredSelection) selection;
			Object firstElement = ss.getFirstElement();
			Object compObj = AdapterUtil.getAdapter(firstElement,
					Component.class);
			Object ctxtObj = AdapterUtil.getAdapter(firstElement,
					ExecutionContext.class);
			Component comp = null;
			ExecutionContext ctxt = null;
			if (compObj != null) {
				comp = (Component) compObj;
				ctxt = null;
			} else if (ctxtObj != null) {
				ctxt = (ExecutionContext) ctxtObj;
				comp = (Component) ctxt.eContainer();
			}
			if (comp == null || comp == targetComponent) {
				return;
			}
			targetComponent = comp;
			ExecutionContext targetEc = ctxt;
			//
			targetComponent.eAdapters().remove(eAdapter);
			for (ExecutionContext ec : targetComponent.getExecutionContexts()) {
				ec.eAdapters().remove(eAdapter);
			}
			for (ExecutionContext ec : targetComponent
					.getParticipationContexts()) {
				ec.eAdapters().remove(eAdapter);
			}
			targetComponent.eAdapters().add(eAdapter);
			for (ExecutionContext ec : targetComponent.getExecutionContexts()) {
				ec.eAdapters().add(eAdapter);
			}
			for (ExecutionContext ec : targetComponent
					.getParticipationContexts()) {
				ec.eAdapters().add(eAdapter);
			}
			if (part instanceof AbstractSystemDiagramEditor) {
				targetEditor = (AbstractSystemDiagramEditor) part;
			}
			selectedECName = null;
			buildData();
		}
	};

	void setSiteSelection() {
		if (getSite() == null) {
			return;
		}

		actionDelegate.setActivePart(null, this);

		selectionListener.selectionChanged(null, getSite().getWorkbenchWindow()
				.getSelectionService().getSelection());

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
	}

}
