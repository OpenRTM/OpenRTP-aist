package jp.go.aist.rtm.systemeditor.ui.views.configurationview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.IDialogConstants;
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
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.go.aist.rtm.systemeditor.RTSystemEditorPlugin;
import jp.go.aist.rtm.systemeditor.nl.Messages;
import jp.go.aist.rtm.systemeditor.ui.dialog.ConfigurationDialog;
import jp.go.aist.rtm.systemeditor.ui.editor.AbstractSystemDiagramEditor;
import jp.go.aist.rtm.systemeditor.ui.util.ComponentUtil;
import jp.go.aist.rtm.systemeditor.ui.views.configurationview.configurationwrapper.ComponentConfigurationWrapper;
import jp.go.aist.rtm.systemeditor.ui.views.configurationview.configurationwrapper.ConfigurationSetConfigurationWrapper;
import jp.go.aist.rtm.systemeditor.ui.views.configurationview.configurationwrapper.NamedValueConfigurationWrapper;
import jp.go.aist.rtm.systemeditor.ui.views.configurationview.configurationwrapper.Secretable;
import jp.go.aist.rtm.toolscommon.model.component.Component;
import jp.go.aist.rtm.toolscommon.model.component.ComponentFactory;
import jp.go.aist.rtm.toolscommon.model.component.ComponentPackage;
import jp.go.aist.rtm.toolscommon.model.component.ConfigurationSet;
import jp.go.aist.rtm.toolscommon.model.component.CorbaComponent;
import jp.go.aist.rtm.toolscommon.model.component.SystemDiagram;
import jp.go.aist.rtm.toolscommon.ui.views.propertysheetview.RtcPropertySheetPage;
import jp.go.aist.rtm.toolscommon.util.AdapterUtil;
import jp.go.aist.rtm.toolscommon.util.SDOUtil;

/**
 * ConfigurationViewを定義するクラス
 * <p>
 * Applyボタン押下までは、修正の適用は保留され、変更された場所は色がついて表示される。
 * NameValueの値の編集ができるのはStringクラスのみであり、それ以外のオブジェクトが含まれていた場合には、編集することはできない（削除は可能）
 */
public class ConfigurationView extends ViewPart {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ConfigurationView.class);

	/** 編集ボタン押下時に呼び出されるリスナー */
	public static class EditSelectionAdapter implements SelectionListener {

		private ConfigurationView configurationView;

		public EditSelectionAdapter(ConfigurationView configurationView) {
			this.configurationView = configurationView;
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			ConfigurationDialog dialog = new ConfigurationDialog(
					configurationView);
			if (dialog.open() == IDialogConstants.OK_ID) {
				// ConfigurationDialog内でapplyを実行する 2009.11.16
				// if (dialog.isApply()) {
				// applyConfiguration();
				// }
				configurationView.refreshData();
			}
		}

	}

	private static final String PROPERTY_ACTIVE_CONFIGSET = "PROPERTY_ACTIVE_CONFIGSET";
	private static final String PROPERTY_CONFIG_SET = "PROPERTY_CONFIG_SET";
	private static final String PROPERTY_KEY = "PROPERTY_KEY";
	private static final String PROPERTY_VALUE = "PROPERTY_VALUE";

	private static final String MODIFY_COLOR = "MODIFY_COLOR";
	private static final String CANT_MODIFY_COLOR = "CANT_MODIFY_COLOR";
	private static final String WHITE_COLOR = "WHITE_COLOR";

	private static final int BUTTON_WIDTH = 70;
	private static final int BUTTON_WIDE_WIDTH = 100;

	private static final String LABEL_BUTTON_EDIT = Messages.getString("Common.button.edit");
	private static final String LABEL_BUTTON_APPLY = Messages.getString("Common.button.apply");
	private static final String LABEL_BUTTON_CANCEL = Messages.getString("Common.button.cancel");

	private static final String LABEL_BUTTON_ADD = Messages.getString("Common.button.add");
	private static final String LABEL_BUTTON_DEL = Messages.getString("Common.button.delete");
	private static final String LABEL_BUTTON_COPY = Messages.getString("ConfigurationView.23");
	private static final String LABEL_BUTTON_DETAIL = Messages.getString("ConfigurationView.48");
	private static final String LABEL_TOOLTIP_DETAIL = Messages.getString("ConfigurationView.47");

	private static final String LABEL_BUTTON_NV_ADD = Messages.getString("Common.button.add");
	private static final String LABEL_BUTTON_NV_DEL = Messages.getString("Common.button.delete");
	private static final String LABEL_BUTTON_NV_SORT = Messages.getString("ConfigurationView.49");
	private static final String LABEL_TOOLTIP_NV_SORT = Messages.getString("ConfigurationView.50");
	private static final String LABEL_BUTTON_NV_DETAIL = Messages.getString("ConfigurationView.48");
	private static final String LABEL_TOOLTIP_NV_DETAIL = Messages.getString("ConfigurationView.47");

	private static final String MSG_ERROR = Messages.getString("Common.dialog.error_title");
	private static final String MSG_UPDATE_FAILURE = Messages.getString("ConfigurationView.13");

	private static final String MSG_CONFIRM = Messages.getString("Common.dialog.confirm_title");

	private static final String MSG_WARNING = Messages.getString("ConfigurationView.39");
	private static final String MSG_NAME_ALREADY_EXIST = Messages.getString("ConfigurationView.40");
	private static final String MSG_KEY_ALREADY_EXIST = Messages.getString("ConfigurationView.43");

	private Component targetComponent;
	private ComponentConfigurationWrapper copiedComponent;
	private ComponentConfigurationWrapper originalComponent;

	private Table leftTable;
	private TableViewer leftTableViewer;
	private DetailTableViewerFilter leftTableViewerFilter;

	private Table rightTable;
	private TableViewer rightTableViewer;
	private DetailTableViewerFilter rightTableViewerFilter;

	private Label componentNameLabel;
	private Label configrationSetNameLabel;

	private Button addConfigurationSetButton;
	private Button copyConfigurationSetButton;
	private Button deleteConfigurationSetButton;
	private Button detailConfigurationSetCheckButton;

	private Button sortCheckButton;

	private Button addNamedValueButton;
	private Button deleteNamedValueButton;
	private Button detailNamedValueCheckButton;

	private Button editButton;
	private Button applyButton;
	private Button cancelButton;

	private static ColorRegistry colorRegistry = null;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createPartControl(Composite parent) {
		if (colorRegistry == null) {
			colorRegistry = new ColorRegistry();
			colorRegistry.put(MODIFY_COLOR, new RGB(255, 192, 192));
			colorRegistry.put(CANT_MODIFY_COLOR, new RGB(198, 198, 198));
			colorRegistry.put(WHITE_COLOR, new RGB(255, 255, 255));
		}

		GridLayout gl;
		GridData gd;

		gl = new GridLayout();
		gl.numColumns = 2;
		parent.setLayout(gl);

		SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL);
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.FILL;
		sashForm.setLayoutData(gd);

		createLeftControl(sashForm);

		createRightControl(sashForm);

		sashForm.setWeights(new int[] { 30, 70 });

		Composite executionButtonComposite = new Composite(parent, SWT.NONE);
		gl = new GridLayout();
		executionButtonComposite.setLayout(gl);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.FILL;
		executionButtonComposite.setLayoutData(gd);

		editButton = new Button(executionButtonComposite, SWT.NONE);
		editButton.setText(LABEL_BUTTON_EDIT);
		gd = new GridData();
		gd.horizontalAlignment = SWT.END;
		gd.widthHint = BUTTON_WIDE_WIDTH;
		editButton.setLayoutData(gd);
		editButton.setEnabled(false);
		editButton.addSelectionListener(new EditSelectionAdapter(this));

		applyButton = new Button(executionButtonComposite, SWT.TOP);
		applyButton.setText(LABEL_BUTTON_APPLY);
		gd = new GridData();
		gd.widthHint = BUTTON_WIDE_WIDTH;
		applyButton.setLayoutData(gd);
		applyButton.setEnabled(false);
		applyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applyConfiguration(true);
			}
		});

		cancelButton = new Button(executionButtonComposite, SWT.TOP);
		cancelButton.setText(LABEL_BUTTON_CANCEL);
		gd = new GridData();
		gd.widthHint = BUTTON_WIDE_WIDTH;
		cancelButton.setLayoutData(gd);
		cancelButton.setEnabled(false);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				buildData();
			}
		});

		setSiteSelection();
	}

	/**
	 * Configurationの変更を反映します。
	 */
	public void applyConfiguration(boolean checkConstraint) {
		int selectionIndex = leftTable.getSelectionIndex();

		List<ConfigurationSet> newConfigurationSetList = createNewConfigurationSetList(copiedComponent);
		List<ConfigurationSet> originalConfigurationSetList = createNewConfigurationSetList(originalComponent);
		
		if(checkConstraint) {
			List<String> validateErrors = new ArrayList<String>();
			for(ConfigurationSetConfigurationWrapper cs : this.copiedComponent.getConfigurationSetList()) {
				if(cs.isSecret()) continue;
				for (NamedValueConfigurationWrapper nv : cs.getNamedValueList()) {
					if(nv.isValueModified() == false) continue;
					if(nv.isLoadedWidgetValue() == false) {
						nv.loadWidgetValue();
					}
					List<String> result = nv.checkConstraints(cs.getId(), false);
					validateErrors.addAll(result);
				}
			}
			if(0 < validateErrors.size()) {
				StringBuilder builder = new StringBuilder();
				builder.append(Messages.getString("ConfigurationView.condition_error.1")).append(System.getProperty("line.separator"));
				builder.append(Messages.getString("ConfigurationView.condition_error.2")).append(System.getProperty("line.separator"));
				builder.append(System.getProperty("line.separator"));
				for(String each : validateErrors) {
					builder.append(each).append(System.getProperty("line.separator"));
				}
				boolean ret = MessageDialog.openConfirm(getSite().getShell(),
														Messages.getString("Common.dialog.confirm_title"),
														builder.toString());
				if(ret == false) return;
			}
		}
		
		int activeConfigurationIndex = copiedComponent
				.getConfigurationSetList().indexOf(
						copiedComponent.getActiveConfigSet());

		ConfigurationSet newActiveConfigurationSet = null;
		if (activeConfigurationIndex != -1) {
			newActiveConfigurationSet = newConfigurationSetList
					.get(activeConfigurationIndex);
		}

		boolean result = targetComponent.updateConfigurationSetListR(
				newConfigurationSetList, newActiveConfigurationSet,
				originalConfigurationSetList);
		if (result == false) {
			MessageDialog.openError(getSite().getShell(), MSG_ERROR,
					MSG_UPDATE_FAILURE);
		} else {
			setDirty();
		}
		if (targetComponent instanceof CorbaComponent) {
			CorbaComponent c = (CorbaComponent) targetComponent;
			c.synchronizeRemoteAttribute(ComponentPackage.eINSTANCE
					.getComponent_ConfigurationSets());
			c.synchronizeRemoteAttribute(ComponentPackage.eINSTANCE
					.getComponent_ActiveConfigurationSet());
			c.synchronizeLocalAttribute(null);
		}
		buildData();

		leftTable.setSelection(selectionIndex);
		refreshRightData();
	}

	private void setDirty() {
		final EObject container = targetComponent.eContainer();
		if (!(container instanceof SystemDiagram))
			return;
		SystemDiagram diagram = (SystemDiagram) container;
		AbstractSystemDiagramEditor editor = ComponentUtil.findEditor(diagram
				.getRootDiagram());
		if (editor == null)
			return;
		editor.setDirty();
	}

	/**
	 * アクティブなコンフィグレーションを切り替えたか
	 */
	private boolean isActiveConfigurationSetChanged() {
		if (copiedComponent.getActiveConfigSet() == null
				|| copiedComponent.getActiveConfigSet().getConfigurationSet() == null) {
			return targetComponent.getActiveConfigurationSet() != null;
		}
		if (targetComponent.getActiveConfigurationSet() == null) {
			return true;
		}
		if (!copiedComponent.getActiveConfigSet().getConfigurationSet().getId()
				.equals(targetComponent.getActiveConfigurationSet().getId())) {
			return true;
		}
		return false;
	}

	/**
	 * アクティブなコンフィグレーションを修正したかどうか
	 */
	private boolean isActiveConfigurationSetModified() {
		if (isActiveConfigurationSetChanged()) {
			return true;
		}
		if (copiedComponent.getActiveConfigSet().getNamedValueList().size() != targetComponent
				.getActiveConfigurationSet().getConfigurationData().size()) {
			return true;
		}
		for (NamedValueConfigurationWrapper namedValue : copiedComponent
				.getActiveConfigSet().getNamedValueList()) {
			if (namedValue.isKeyModified() || namedValue.isValueModified()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return ConfiguratoinSetの詳細表示が有効な場合はtrue
	 */
	public boolean isCheckedDetailConfigurationSet() {
		return this.detailConfigurationSetCheckButton.getSelection();
	}

	/**
	 * @return NamedValueの詳細表示が有効な場合はtrue
	 */
	public boolean isCheckedDetailNamedValue() {
		return this.detailNamedValueCheckButton.getSelection();
	}

	/**
	 * 編集後の新しいConfigurationSetを作成する。
	 * <p>
	 *
	 * @param copiedComponent
	 * @return
	 */
	private List<ConfigurationSet> createNewConfigurationSetList(
			ComponentConfigurationWrapper copiedComponent) {
		ArrayList<ConfigurationSet> result = new ArrayList<ConfigurationSet>();
		for (ConfigurationSetConfigurationWrapper configset : copiedComponent
				.getConfigurationSetList()) {
			ConfigurationSet configurationSet = ComponentFactory.eINSTANCE
					.createConfigurationSet();
			for (NamedValueConfigurationWrapper namedValue : configset
					.getNamedValueList()) {
				configurationSet.getConfigurationData().add(
						SDOUtil.createNameValue(namedValue.getKey(),
								namedValue.getValue()));
			}

			configurationSet.setId(configset.getId());

			result.add(configurationSet);
		}

		return result;
	}

	private void createLeftControl(SashForm sashForm) {
		GridLayout gl;
		GridData gd;

		final Composite composite = new Composite(sashForm, SWT.FILL);
		gl = new GridLayout();
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		gl.numColumns = 1;
		composite.setLayout(gl);

		Composite componentNameComposite = new Composite(composite, SWT.NONE);
		gl = new GridLayout();
		gl.numColumns = 2;
		gl.marginHeight = 2;
		componentNameComposite.setLayout(gl);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 0;
		gd.verticalSpan = 0;
		componentNameComposite.setLayoutData(gd);

		Label componentNameLabelConst = new Label(componentNameComposite,
				SWT.NONE);
		gd = new GridData();
		gd.horizontalSpan = 0;
		gd.verticalSpan = 0;
		componentNameLabelConst.setLayoutData(gd);
		componentNameLabelConst.setText("ComponentName:");

		componentNameLabel = new Label(componentNameComposite, SWT.BORDER);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 0;
		gd.verticalSpan = 0;
		componentNameLabel.setLayoutData(gd);
		componentNameLabel.setBackground(colorRegistry.get(WHITE_COLOR));

		leftTableViewer = new TableViewer(composite, SWT.FULL_SELECTION
				| SWT.SINGLE | SWT.BORDER);
		leftTableViewer.setColumnProperties(new String[] {
				PROPERTY_ACTIVE_CONFIGSET, PROPERTY_CONFIG_SET });
		leftTableViewer.setContentProvider(new ArrayContentProvider());
		leftTableViewer.setLabelProvider(new ConfigSetLabelProvider());
		leftTableViewer.setCellModifier(new LeftTableCellModifier(
				leftTableViewer));

		leftTableViewer.setCellEditors(new CellEditor[] {
				new CheckboxCellEditor(leftTableViewer.getTable()),
				new TextCellEditor(leftTableViewer.getTable()) });
		this.leftTableViewerFilter = new DetailTableViewerFilter();
		this.leftTableViewer.addFilter(this.leftTableViewerFilter);

		leftTable = leftTableViewer.getTable();
		leftTable.setLinesVisible(true);
		leftTable.setHeaderVisible(true);
		gd = new GridData();
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 0;
		gd.verticalSpan = 0;
		leftTable.setLayoutData(gd);

		gl = new GridLayout(1, false);
		gl.numColumns = 1;
		leftTable.setLayout(gl);

		final TableColumn activeCol = new TableColumn(leftTable, SWT.RIGHT);
		activeCol.setText("active");
		activeCol.setWidth(50);

		final TableColumn configCol = new TableColumn(leftTable, SWT.LEFT);
		configCol.setText("config");
		activeCol.setWidth(50);

		ControlAdapter controlAdapter = new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				composite.getParent().forceFocus();
				int width = composite.getClientArea().width - 2
						* leftTable.getBorderWidth() - activeCol.getWidth();
				configCol.setWidth(Math.max(10, width));
			}
		};

		activeCol.addControlListener(controlAdapter);
		composite.addControlListener(controlAdapter);

		Composite buttonCompsite = new Composite(composite, SWT.BOTTOM);
		gl = new GridLayout();
		gl.numColumns = 4;
		buttonCompsite.setLayout(gl);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		buttonCompsite.setLayoutData(gd);

		// Copyボタンの追加 2008.12.17
		copyConfigurationSetButton = new Button(buttonCompsite, SWT.NONE);
		copyConfigurationSetButton.setText(LABEL_BUTTON_COPY);
		copyConfigurationSetButton.setEnabled(false);
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.END;
		gd.widthHint = BUTTON_WIDTH;
		copyConfigurationSetButton.setLayoutData(gd);
		copyConfigurationSetButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ConfigurationSetConfigurationWrapper currentConfugurationSet = getSelectedConfigurationSet();
				if (currentConfugurationSet == null) {
					return;
				}

				ConfigurationSetConfigurationWrapper csw = new ConfigurationSetConfigurationWrapper(null, null);
				csw.setId(createDefaultConfigurationSetName(currentConfugurationSet.getId())); // modified_name

				copiedComponent.addConfigurationSet(csw);

				// 選択されているコンフィグセットの設定値をそのままコピー
				List<NamedValueConfigurationWrapper> nvlist = csw.getNamedValueList();
				for (NamedValueConfigurationWrapper oldNavedValue : currentConfugurationSet.getNamedValueList()) {
					nvlist.add(oldNavedValue.clone());
				}
				Collections.sort(nvlist);

				refreshLeftData();
				leftTable.forceFocus();
				leftTable.setSelection(leftTable.getItemCount() - 1);
				updateDeleteConfigurationSetButtonEnable();
				refreshRightData();
			}
		});

		addConfigurationSetButton = new Button(buttonCompsite, SWT.NONE);
		addConfigurationSetButton.setText(LABEL_BUTTON_ADD);
		addConfigurationSetButton.setEnabled(false);
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.END;
		gd.widthHint = BUTTON_WIDTH;
		addConfigurationSetButton.setLayoutData(gd);
		addConfigurationSetButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ConfigurationSetConfigurationWrapper csw = new ConfigurationSetConfigurationWrapper(
						null, null);
				csw.setId(createDefaultConfigurationSetName("configSet"));

				copiedComponent.addConfigurationSet(csw);

				if (copiedComponent.getConfigurationSetList().size() == 1) { // コンフィグレーションセットが追加した１つしかない場合には、それをアクティブにする。
					copiedComponent.setActiveConfigSet(csw);
				}

				// デフォルトのwidgetと制約条件からNVList作成
				List<NamedValueConfigurationWrapper> nvlist = csw
						.getNamedValueList();
				for (String key : copiedComponent.getDefaultNameSet()) {
					NamedValueConfigurationWrapper nvw = new NamedValueConfigurationWrapper(
							key, "value");
					String type = copiedComponent.getWidgetSetting().get(key);
					String cond = copiedComponent.getDefaultConditionSetting()
							.get(key);
					nvw.setWidgetAndCondition(type, cond);
					nvlist.add(nvw);
				}
				Collections.sort(nvlist);

				refreshLeftData();
				leftTable.forceFocus();
				leftTable.setSelection(leftTable.getItemCount() - 1);
				updateDeleteConfigurationSetButtonEnable();
				refreshRightData();
			}
		});

		deleteConfigurationSetButton = new Button(buttonCompsite, SWT.NONE);
		deleteConfigurationSetButton.setText(LABEL_BUTTON_DEL);
		gd = new GridData();
		gd.horizontalAlignment = SWT.END;
		gd.widthHint = BUTTON_WIDTH;
		deleteConfigurationSetButton.setLayoutData(gd);
		deleteConfigurationSetButton.setEnabled(false);
		deleteConfigurationSetButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ConfigurationSetConfigurationWrapper currentConfugurationSet = getSelectedConfigurationSet();
				if (currentConfugurationSet == null) {
					return;
				}

				int selectionIndex = copiedComponent.getConfigurationSetList().indexOf(currentConfugurationSet);
				int activeConfigurationIndex = copiedComponent.getConfigurationSetList()
						.indexOf(copiedComponent.getActiveConfigSet());

				copiedComponent.removeConfigurationSet(currentConfugurationSet);

				int count = copiedComponent.getConfigurationSetList().size();
				int newIndex = (selectionIndex < count) ? selectionIndex : count - 1;

				if (selectionIndex == activeConfigurationIndex) {
					ConfigurationSetConfigurationWrapper configurationSetConfigurationWrapper = copiedComponent
							.getConfigurationSetList().get(newIndex);
					if (configurationSetConfigurationWrapper != null) {
						copiedComponent.setActiveConfigSet(configurationSetConfigurationWrapper);
					}
				}

				refreshLeftData();
				leftTableViewer.refresh(); // ActiveなConfigurationSetを削除する場合に必要

				if (leftTable.getItemCount() >= 1) {
					leftTable.forceFocus();
					leftTable.setSelection(newIndex);
					updateDeleteConfigurationSetButtonEnable();
				}

				refreshRightData();
			}
		});

		this.detailConfigurationSetCheckButton = new Button(buttonCompsite, SWT.BOTTOM | SWT.CHECK);
		this.detailConfigurationSetCheckButton.setEnabled(false);
		this.detailConfigurationSetCheckButton.setToolTipText(LABEL_TOOLTIP_DETAIL);
		gd = new GridData();
		gd.horizontalAlignment = SWT.END;
		this.detailConfigurationSetCheckButton.setLayoutData(gd);
		this.detailConfigurationSetCheckButton.setText(LABEL_BUTTON_DETAIL);
		this.detailConfigurationSetCheckButton.setSelection(false);
		this.detailConfigurationSetCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshLeftData();
			}
		});

		leftTableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				updateDeleteConfigurationSetButtonEnable();
			}
		});
	}

	private void createRightControl(SashForm sashForm) {
		GridLayout gl;
		GridData gd;

		final Composite composite = new Composite(sashForm, SWT.FILL);
		gl = new GridLayout();
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		composite.setLayout(gl);

		Composite configurationNameComposite = new Composite(composite,
				SWT.NONE);
		gl = new GridLayout();
		gl.numColumns = 2;
		gl.marginHeight = 2;
		configurationNameComposite.setLayout(gl);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 0;
		gd.verticalSpan = 0;
		configurationNameComposite.setLayoutData(gd);

		Label configurationNameLabelConst = new Label(
				configurationNameComposite, SWT.NONE);
		gd = new GridData();
		gd.horizontalSpan = 0;
		gd.verticalSpan = 0;
		configurationNameLabelConst.setLayoutData(gd);
		configurationNameLabelConst.setText("ConfigurationSet:");

		configrationSetNameLabel = new Label(configurationNameComposite,
				SWT.BORDER);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 0;
		gd.verticalSpan = 0;
		configrationSetNameLabel.setLayoutData(gd);
		configrationSetNameLabel.setBackground(colorRegistry.get(WHITE_COLOR));

		rightTableViewer = new TableViewer(composite, SWT.FULL_SELECTION
				| SWT.SINGLE | SWT.BORDER);
		rightTableViewer.setColumnProperties(new String[] { PROPERTY_KEY,
				PROPERTY_VALUE });
		rightTableViewer.setContentProvider(new ArrayContentProvider());
		rightTableViewer.setLabelProvider(new MapEntryLabelProvider());
		rightTableViewer.setCellModifier(new RightTableCellModifier(
				rightTableViewer));
		rightTableViewer.setCellEditors(new CellEditor[] {
				new TextCellEditor(rightTableViewer.getTable()),
				new TextCellEditor(rightTableViewer.getTable()) });
		this.rightTableViewerFilter = new DetailTableViewerFilter();
		this.rightTableViewer.addFilter(this.rightTableViewerFilter);

		rightTable = rightTableViewer.getTable();
		rightTable.setLinesVisible(true);
		leftTable.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshRightData();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				refreshRightData();
			}
		});

		gd = new GridData();
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 0;
		gd.verticalSpan = 0;
		rightTable.setLayoutData(gd);

		rightTable.setHeaderVisible(true);

		final TableColumn keyCol = new TableColumn(rightTable, SWT.LEFT);
		keyCol.setText("name");
		keyCol.setWidth(150);

		final TableColumn valueCol = new TableColumn(rightTable, SWT.LEFT);
		valueCol.setText("value");
		valueCol.setWidth(300);

		ControlAdapter controlAdapter = new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				composite.getParent().forceFocus();
				int width = composite.getClientArea().width - 2
						* rightTable.getBorderWidth() - keyCol.getWidth();
				valueCol.setWidth(Math.max(10, width));
			}
		};

		keyCol.addControlListener(controlAdapter);
		composite.addControlListener(controlAdapter);

		Composite buttonCompsite = new Composite(composite, SWT.BOTTOM);
		gl = new GridLayout();
		gl.numColumns = 4;
		buttonCompsite.setLayout(gl);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		buttonCompsite.setLayoutData(gd);

		addNamedValueButton = new Button(buttonCompsite, SWT.NONE);
		addNamedValueButton.setText(LABEL_BUTTON_NV_ADD);
		addNamedValueButton.setEnabled(false);
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.END;
		gd.widthHint = BUTTON_WIDTH;
		addNamedValueButton.setLayoutData(gd);
		addNamedValueButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ConfigurationSetConfigurationWrapper currentConfugurationSet = getSelectedConfigurationSet();
				if (currentConfugurationSet == null) {
					return;
				}

				NamedValueConfigurationWrapper namedValueConfigurationWrapper = new NamedValueConfigurationWrapper(null,
						null);
				namedValueConfigurationWrapper.setKey(createDefaultNamedValueKey("name")); // modified_key
				namedValueConfigurationWrapper.setValue("value");
				currentConfugurationSet.addNamedValue(namedValueConfigurationWrapper);

				refreshRightData();

				rightTable.forceFocus();
				rightTable.setSelection(rightTable.getItemCount() - 1);

				updateDeleteNamedValueButtonEnable();
			}
		});

		deleteNamedValueButton = new Button(buttonCompsite, SWT.NONE);
		deleteNamedValueButton.setText(LABEL_BUTTON_NV_DEL);
		gd = new GridData();
		gd.horizontalAlignment = SWT.END;
		gd.widthHint = BUTTON_WIDTH;
		deleteNamedValueButton.setLayoutData(gd);
		deleteNamedValueButton.setEnabled(false);
		deleteNamedValueButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ConfigurationSetConfigurationWrapper currentConfugurationSet = getSelectedConfigurationSet();
				if (currentConfugurationSet == null) {
					return;
				}
				NamedValueConfigurationWrapper currentNamedValueConfiguration = getSelectedNamedValue();
				if (currentNamedValueConfiguration == null) {
					return;
				}

				int selectionIndex = currentConfugurationSet.getNamedValueList()
						.indexOf(currentNamedValueConfiguration);

				currentConfugurationSet.removeNamedValue(currentNamedValueConfiguration);

				int count = currentConfugurationSet.getNamedValueList().size();
				int newIndex = (selectionIndex < count) ? selectionIndex : count - 1;

				refreshRightData();

				if (rightTable.getItemCount() >= 1) {
					rightTable.forceFocus();
					rightTable.setSelection(newIndex);
					updateDeleteNamedValueButtonEnable();
				}
			}
		});

		this.sortCheckButton = new Button(buttonCompsite, SWT.BOTTOM | SWT.CHECK);
		this.sortCheckButton.setEnabled(false);
		this.sortCheckButton.setToolTipText(LABEL_TOOLTIP_NV_SORT);
		gd = new GridData();
		gd.horizontalAlignment = SWT.END;
		this.sortCheckButton.setLayoutData(gd);
		this.sortCheckButton.setText(LABEL_BUTTON_NV_SORT);
		this.sortCheckButton.setSelection(false);
		this.sortCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				buildData();
				refreshRightData();
			}
		});

		this.detailNamedValueCheckButton = new Button(buttonCompsite, SWT.BOTTOM | SWT.CHECK);
		this.detailNamedValueCheckButton.setEnabled(false);
		this.detailNamedValueCheckButton.setToolTipText(LABEL_TOOLTIP_NV_DETAIL);
		gd = new GridData();
		gd.horizontalAlignment = SWT.END;
		this.detailNamedValueCheckButton.setLayoutData(gd);
		this.detailNamedValueCheckButton.setText(LABEL_BUTTON_NV_DETAIL);
		this.detailNamedValueCheckButton.setSelection(false);
		this.detailNamedValueCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshRightData();
			}
		});

		rightTableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				updateDeleteNamedValueButtonEnable();
			}
		});
	}

	// 左テーブルで選択中の ConfigurationSet を取得します。未選択の場合はnull
	private ConfigurationSetConfigurationWrapper getSelectedConfigurationSet() {
		if (this.leftTableViewer.getSelection() instanceof StructuredSelection) {
			ConfigurationSetConfigurationWrapper ret = (ConfigurationSetConfigurationWrapper) ((StructuredSelection) this.leftTableViewer
					.getSelection()).getFirstElement();
			return ret;
		}
		return null;
	}

	// 右テーブルで選択中の NamedValue を取得します。未選択の場合はnull
	private NamedValueConfigurationWrapper getSelectedNamedValue() {
		if (this.rightTableViewer.getSelection() instanceof StructuredSelection) {
			NamedValueConfigurationWrapper ret = (NamedValueConfigurationWrapper) ((StructuredSelection) this.rightTableViewer
					.getSelection()).getFirstElement();
			return ret;
		}
		return null;
	}

	private String createDefaultNamedValueKey(String preString) {
		ConfigurationSetConfigurationWrapper currentConfugurationSet = getSelectedConfigurationSet();
		int number = 1;
		for (;; number++) {
			boolean isExist = false;
			for (NamedValueConfigurationWrapper current : currentConfugurationSet.getNamedValueList()) {
				if ((preString + "_" + number).equals(current.getKey())) {
					isExist = true;
					break;
				}
			}
			if (!isExist) {
				break;
			}
		}
		return (preString + "_" + number);
	}

	private String createDefaultConfigurationSetName(String preString) {
		int number = 1;
		for (;; number++) {
			boolean isExist = false;
			for (ConfigurationSetConfigurationWrapper current : copiedComponent.getConfigurationSetList()) {
				if ((preString + "_" + number).equals(current.getId())) {
					isExist = true;
					break;
				}
			}
			if (!isExist) {
				break;
			}
		}
		return preString + "_" + number;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.equals(IPropertySheetPage.class)) {
			return new RtcPropertySheetPage();
		}

		return super.getAdapter(adapter);
	}

	/** CORBAの同期による変更通知を受け取るアダプタ */
	AdapterImpl eAdapter = new AdapterImpl() {
		@Override
		public void notifyChanged(Notification msg) {
			if (msg.getOldValue() == this || msg.getNewValue() == this) {
				return;
			}
			boolean update = false;
			if (ComponentPackage.eINSTANCE.getComponent_ConfigurationSets()
					.equals(msg.getFeature())) {
				update = true;
			}
			if (ComponentPackage.eINSTANCE
					.getComponent_ActiveConfigurationSet().equals(
							msg.getFeature())) {
				update = true;
			}
			if (!update) {
				return;
			}
			leftTableViewer.getControl().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					buildData();
				}
			});
		}
	};

	/**
	 * 選択を監視するリスナ
	 */
	private ISelectionListener selectionListener = new ISelectionListener() {
		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if (targetComponent != null) {
				targetComponent.eAdapters().remove(eAdapter);
			}
			targetComponent = null;
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection sSelection = (IStructuredSelection) selection;
				Object selectedComponent = AdapterUtil.getAdapter(
						sSelection.getFirstElement(), Component.class);
				if (selectedComponent != null) {
					targetComponent = (Component) selectedComponent;
					LOGGER.trace(
							"selectionChanged: target component is selected. comp=<{}>",
							targetComponent);
					targetComponent.synchronizeManually();
					targetComponent.eAdapters().add(eAdapter);
				}
			}
			buildData();
		}
	};

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
	}

	private void buildData() {
		copiedComponent = null;
		if (targetComponent != null) {
			copiedComponent = createConfigurationWrapper(targetComponent);
			originalComponent = createConfigurationWrapper(targetComponent);
		}
		refreshData();
	}

	private void refreshData() {
		this.editButton.setEnabled(false);
		this.applyButton.setEnabled(false);
		this.cancelButton.setEnabled(false);

		refreshLeftData();

		if (this.copiedComponent != null) {
			this.leftTable.setSelection(
					this.copiedComponent.getConfigurationSetList().indexOf(this.copiedComponent.getActiveConfigSet()));
		}

		refreshRightData();
		if (this.copiedComponent != null) {
			this.editButton.setEnabled(true);
			this.applyButton.setEnabled(true);
			this.cancelButton.setEnabled(true);
		}
	}

	private void refreshLeftData() {
		this.leftTableViewer.setInput(Collections.EMPTY_LIST);
		this.componentNameLabel.setText("");
		this.addConfigurationSetButton.setEnabled(false);
		this.detailConfigurationSetCheckButton.setEnabled(false);

		if (this.copiedComponent != null) {
			this.addConfigurationSetButton.setEnabled(true);
			this.detailConfigurationSetCheckButton.setEnabled(true);
			this.componentNameLabel.setText(this.targetComponent.getInstanceNameL());

			this.leftTableViewerFilter.setDetail(this.detailConfigurationSetCheckButton.getSelection());
			this.leftTableViewer.setInput(this.copiedComponent.getConfigurationSetList());

			if (this.leftTable.getItemCount() > 0) {
				this.leftTable.select(0);
			}
		}

		updateDeleteConfigurationSetButtonEnable();
	}

	// ConfigSetのDeleteボタンとCopyボタンのenable属性は同じ 2008.12.17
	private void updateDeleteConfigurationSetButtonEnable() {
		boolean deleteConfigurationSetEnabled = (getSelectedConfigurationSet() != null);

		deleteConfigurationSetButton.setEnabled(deleteConfigurationSetEnabled);
		copyConfigurationSetButton.setEnabled(deleteConfigurationSetEnabled);
	}

	private void refreshRightData() {
		this.rightTableViewer.setInput(Collections.EMPTY_LIST);
		this.configrationSetNameLabel.setText("");
		this.addNamedValueButton.setEnabled(false);
		this.sortCheckButton.setEnabled(false);
		this.detailNamedValueCheckButton.setEnabled(false);

		if (this.copiedComponent != null) {
			ConfigurationSetConfigurationWrapper currentConfugurationSet = getSelectedConfigurationSet();
			if (currentConfugurationSet != null) {
				this.configrationSetNameLabel.setText(currentConfugurationSet.getId());

				this.rightTableViewerFilter.setDetail(this.detailNamedValueCheckButton.getSelection());
				this.rightTableViewer.setInput(currentConfugurationSet.getNamedValueList());

				if (!(this.targetComponent instanceof CorbaComponent)) {
					this.addNamedValueButton.setEnabled(true);
				}
				this.sortCheckButton.setEnabled(true);
				this.detailNamedValueCheckButton.setEnabled(true);
			}
		}
		updateDeleteNamedValueButtonEnable();
	}

	private void updateDeleteNamedValueButtonEnable() {
		if (rightTable.getSelectionIndex() != -1) {
			if (!(targetComponent instanceof CorbaComponent)) {
				deleteNamedValueButton.setEnabled(true);
			}
		} else {
			deleteNamedValueButton.setEnabled(false);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		getSite().getWorkbenchWindow().getSelectionService()
				.removeSelectionListener(selectionListener);
		super.dispose();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFocus() {
	}

	/**
	 * ConfigurationViewの内部モデルであるComponentConfigurationWrapperを作成する
	 */
	public ComponentConfigurationWrapper createConfigurationWrapper(
			Component target) {
		return ComponentConfigurationWrapper.create(target, sortCheckButton.getSelection());
	}

	private void setSiteSelection() {
		if (getSite() == null) {
			return;
		}

		getSite().getWorkbenchWindow().getSelectionService()
				.addSelectionListener(selectionListener);

		getSite().setSelectionProvider(new ISelectionProvider() {
			@Override
			public void addSelectionChangedListener(
					ISelectionChangedListener listener) {
			}

			@Override
			public ISelection getSelection() {
				StructuredSelection result = null;
				if (targetComponent != null) {
					result = new StructuredSelection(targetComponent);
				}

				return result;
			}

			@Override
			public void removeSelectionChangedListener(
					ISelectionChangedListener listener) {
			}

			@Override
			public void setSelection(ISelection selection) {
			}
		});

		selectionListener.selectionChanged(null, getSite().getWorkbenchWindow()
				.getSelectionService().getSelection());
	}

	/**
	 * 左テーブルのCellModifierクラス
	 */
	public class LeftTableCellModifier implements ICellModifier {
		private TableViewer viewer;

		public LeftTableCellModifier(TableViewer viewer) {
			this.viewer = viewer;
		}

		@Override
		public boolean canModify(Object element, String property) {
			ConfigurationSetConfigurationWrapper configurationSet = null;
			if (element instanceof ConfigurationSetConfigurationWrapper) {
				configurationSet = (ConfigurationSetConfigurationWrapper) element;
			}
			if (PROPERTY_ACTIVE_CONFIGSET.equals(property)) {
				if (configurationSet.isSecret()) {
					return false;
				}
			}
			return true;
		}

		@Override
		public Object getValue(Object element, String property) {
			Object result = null;
			if (PROPERTY_ACTIVE_CONFIGSET.equals(property)) {
				result = Boolean.TRUE;
			} else if (PROPERTY_CONFIG_SET.equals(property)) {
				result = ((ConfigurationSetConfigurationWrapper) element).getId();
			}
			return result;
		}

		@Override
		public void modify(Object element, String property, Object value) {
			ConfigurationSetConfigurationWrapper configurationSet = null;
			if (element instanceof Item) {
				configurationSet = ((ConfigurationSetConfigurationWrapper) ((Item) element).getData());
			}
			if (configurationSet == null) {
				return;
			}

			if (PROPERTY_ACTIVE_CONFIGSET.equals(property)) {
				copiedComponent.setActiveConfigSet(configurationSet);
				viewer.refresh();
			} else if (PROPERTY_CONFIG_SET.equals(property)) {
				boolean isDuplicate = false;
				for (ConfigurationSetConfigurationWrapper current : copiedComponent.getConfigurationSetList()) {
					if (configurationSet != current && ((String) value).equals(current.getId())) {
						isDuplicate = true;
						break;
					}
				}
				String newConfigurationSetName = (String) value;
				if (isDuplicate) {
					MessageDialog.openWarning(viewer.getControl().getShell(), MSG_WARNING, MSG_NAME_ALREADY_EXIST);
					newConfigurationSetName = createDefaultConfigurationSetName((String) value);
				}
				configurationSet.setId(newConfigurationSetName);
				viewer.update(configurationSet, null);
			}
		}

	}

	/**
	 * 右テーブルのCellModifierクラス
	 */
	public class RightTableCellModifier implements ICellModifier {
		private TableViewer viewer;

		public RightTableCellModifier(TableViewer viewer) {
			this.viewer = viewer;
		}

		@Override
		public boolean canModify(Object element, String property) {

			if (PROPERTY_KEY.equals(property)) {
				return true;
			} else if (PROPERTY_VALUE.equals(property)) {
				NamedValueConfigurationWrapper item = (NamedValueConfigurationWrapper) element;
				return item.canModify();
			}

			return false;
		}

		@Override
		public Object getValue(Object element, String property) {
			NamedValueConfigurationWrapper item = (NamedValueConfigurationWrapper) element;

			if (PROPERTY_KEY.equals(property)) {
				return item.getKey();
			} else if (PROPERTY_VALUE.equals(property)) {
				return item.getValueAsString();
			}

			return null;
		}

		@Override
		public void modify(Object element, String property, Object value) {
			if (element instanceof TableItem == false) {
				return;
			}
			NamedValueConfigurationWrapper item = ((NamedValueConfigurationWrapper) ((TableItem) element).getData());
			ConfigurationSetConfigurationWrapper currentConfugurationSet = getSelectedConfigurationSet();
			if (item == null || currentConfugurationSet == null) {
				return;
			}

			if (PROPERTY_KEY.equals(property)) {
				boolean isDuplicate = false;
				for (NamedValueConfigurationWrapper current : currentConfugurationSet.getNamedValueList()) {
					if (item != current && ((String) value).equals(current.getKey())) {
						isDuplicate = true;
						break;
					}
				}
				String newKey = (String) value;
				if (isDuplicate) {
					MessageDialog.openWarning(viewer.getControl().getShell(), MSG_WARNING, MSG_KEY_ALREADY_EXIST);
					newKey = createDefaultNamedValueKey((String) value);
				}
				item.setKey(newKey);
			} else if (PROPERTY_VALUE.equals(property)) {
				item.setValue((String) value);
			}

			viewer.update(item, null);
		}
	}

	/**
	 * LabelProviderクラス
	 */
	public class ConfigSetLabelProvider extends LabelProvider implements
			ITableLabelProvider, ITableColorProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			Image result = null;
			if (columnIndex == 0) {
				ConfigurationSetConfigurationWrapper item = (ConfigurationSetConfigurationWrapper) element;
				if (item == copiedComponent.getActiveConfigSet()) {
					result = RTSystemEditorPlugin
							.getCachedImage("icons/Radiobutton_Checked.png");
				} else {
					result = RTSystemEditorPlugin
							.getCachedImage("icons/Radiobutton_Unchecked.png");
				}
			}

			return result;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			ConfigurationSetConfigurationWrapper item = (ConfigurationSetConfigurationWrapper) element;

			String result = null;
			if (columnIndex == 1) {
				result = getModiedLabelString(item.isNameModified())
						+ item.getId();
			}

			return result;
		}

		@Override
		public Color getBackground(Object element, int columnIndex) {
			ConfigurationSetConfigurationWrapper configurationSetConfigurationWrapper = (ConfigurationSetConfigurationWrapper) element;

			boolean isModify = false;
			if (columnIndex == 0) {
				if (isActiveConfigurationSetChanged()) {
					if (targetComponent.getActiveConfigurationSet() == configurationSetConfigurationWrapper
							.getConfigurationSet()
							|| copiedComponent.getActiveConfigSet() == configurationSetConfigurationWrapper) {
						isModify = true;
					}
				}
			} else if (columnIndex == 1) {
				isModify = configurationSetConfigurationWrapper
						.isNameModified();
			}

			Color color = null;
			if (isModify) {
				color = colorRegistry.get(MODIFY_COLOR);
			}

			return color;
		}

		@Override
		public Color getForeground(Object element, int columnIndex) {
			return null;
		}
	}

	/**
	 * LabelProviderクラス
	 */
	public class MapEntryLabelProvider extends LabelProvider implements
			ITableLabelProvider, ITableColorProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			NamedValueConfigurationWrapper item = (NamedValueConfigurationWrapper) element;

			if (columnIndex == 0) {
				return getModiedLabelString(item.isKeyModified())
						+ item.getKey();
			} else if (columnIndex == 1) {
				return item.getValueAsString();
			}

			return null;
		}

		@Override
		public Color getForeground(Object element, int columnIndex) {
			return null;
		}

		@Override
		public Color getBackground(Object element, int columnIndex) {
			NamedValueConfigurationWrapper namedValueConfigurationWrapper = (NamedValueConfigurationWrapper) element;
			if (columnIndex == 0
					&& namedValueConfigurationWrapper.isKeyModified()
					|| columnIndex == 1
					&& namedValueConfigurationWrapper.isValueModified()) {
				return colorRegistry.get(MODIFY_COLOR);
			}

			if (columnIndex == 1 && !namedValueConfigurationWrapper.canModify()) {
				return colorRegistry.get(CANT_MODIFY_COLOR);
			}

			return null;
		}
	}

	/**
	 * ConfigurationSet、およびNamedValueの一覧テーブルの表示フィルタ<br>
	 * 「__」で始まる設定値は、詳細モードのときのみ表示します。
	 */
	private static class DetailTableViewerFilter extends ViewerFilter {

		private boolean isDetail;

		public DetailTableViewerFilter() {
			this.isDetail = false;
		}

		/**
		 * 詳細モードを設定します。
		 *
		 * @param isDetail
		 *            詳細モードの場合はtrue
		 */
		public void setDetail(boolean isDetail) {
			this.isDetail = isDetail;
		}

		/**
		 * 詳細モード、かつNameValueのキーが「__」で始まっていない場合はtrue
		 */
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (!(element instanceof Secretable)) {
				return false;
			}
			Secretable sc = (Secretable) element;
			return (this.isDetail || !sc.isSecret());
		}

	}

	private String getModiedLabelString(boolean bool) {
		String result = "";
		if (bool) {
			result = "*";
		}
		return result;
	}

	/** 編集用のコンフィグを返す */
	public ComponentConfigurationWrapper getComponentConfig() {
		return copiedComponent;
	}
}
