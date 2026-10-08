package jp.go.aist.rtm.rtcbuilder.ui.editors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.preference.IPreferenceStore;
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
import org.eclipse.jface.viewers.CellEditor.LayoutData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.RtcBuilderPlugin;
import jp.go.aist.rtm.rtcbuilder.generator.param.ConfigSetParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.RtcParam;
import jp.go.aist.rtm.rtcbuilder.nl.Messages;
import jp.go.aist.rtm.rtcbuilder.ui.preference.ComponentPreferenceManager;
import jp.go.aist.rtm.rtcbuilder.util.StringUtil;
import jp.go.aist.rtm.rtcbuilder.util.ValidationUtil;

/**
 * Configページ
 */
public class ConfigurationEditorFormPage extends AbstractEditorFormPage {
	private TableViewer configurationSetTableViewer;
	private Button configurationSetAddButton;
	private Button configurationSetDeleteButton;
	//
	private Text parametertNameDetailText;
	private Combo typeCombo;
	private Text defaultValueText;
	private Text variableNameText;
	private Text unitConfigText;
	private Text constraintConfigText;
	private Combo widgetCombo;
	private Text stepText;
	//
	private Text datanameText;
	private Text defaultValText;
	private Text descriptionText;
	private Text unitText;
	private Text rangeText;
	private Text constraintText;
	//
	private ConfigSetParam preSelection;
	private ConfigSetParam selectParam;
	//
	private String defaultConfigName;
	private String defaultConfigType;
	private String defaultConfigVarName;
	private String defaultConfigDefault;
	private String defaultConfigConstraint;
	private String defaultConfigUnit;
	private String[] defaultTypeList = {"short", "int", "long", "float", "double", "string"};

	/**
	 * コンストラクタ
	 *
	 * @param editor
	 *            親のエディタ
	 */
	public ConfigurationEditorFormPage(RtcBuilderEditor editor) {
		super(editor, "id", Messages.getString("IMC.CONFIGURATION_SECTION"));
		//
		preSelection = null;
		updateDefaultValue();
	}

	private void updateDefaultValue() {
		IPreferenceStore store = RtcBuilderPlugin.getDefault().getPreferenceStore();
		defaultConfigName = ComponentPreferenceManager.getInstance().getConfiguration_Name();
		defaultConfigType = store.getString(ComponentPreferenceManager.Generate_Configuration_Type);
		defaultConfigVarName = store.getString(ComponentPreferenceManager.Generate_Configuration_VarName);
		defaultConfigDefault = store.getString(ComponentPreferenceManager.Generate_Configuration_Default);
		defaultConfigConstraint = store.getString(ComponentPreferenceManager.Generate_Configuration_Constraint);
		defaultConfigUnit = store.getString(ComponentPreferenceManager.Generate_Configuration_Unit);
		//
//		defaultTypeList = super.extractDataTypes();
	}

	/**
	 * {@inheritDoc}
	 */
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = super.createBase(managedForm, Messages.getString("IMC.CONFIGURATION_SECTION_TITLE"));
		FormToolkit toolkit = managedForm.getToolkit();
		//
		configurationSetTableViewer = createConfigurationSetSection(toolkit, form);
		createHintSection(toolkit, form);
		createDetailSection(toolkit, form);
		// 言語・環境ページより先にこのページが表示された場合、ここで言語を判断する
		editor.setEnabledInfoByLang();

		load();
	}

	private void createDetailSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createSectionBaseWithLabel(toolkit, form,
				"Detail", Messages.getString("IMC.CONFIGURATION_DETAIL_EXPL"), 2);

		parametertNameDetailText = createLabelAndText(toolkit, composite,
				Messages.getString("IMC.CONFIGURATION_LBL_PARAMNAME"), SWT.BORDER);
		parametertNameDetailText.setEditable(false);
		parametertNameDetailText.setBackground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		//
		Group detailGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		detailGroup.setLayout(new GridLayout(2, false));
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		detailGroup.setLayoutData(gd);
		//
		typeCombo = createEditableCombo(toolkit, detailGroup,
				IMessageConstants.REQUIRED + Messages.getString("IMC.CONFIGURATION_TBLLBL_TYPE"), "", defaultTypeList, SWT.COLOR_RED);
		defaultValueText = createLabelAndText(toolkit, detailGroup,
				IMessageConstants.REQUIRED + Messages.getString("IMC.CONFIGURATION_TBLLBL_DEFAULTVAL"), SWT.BORDER, SWT.COLOR_RED);
		variableNameText = createLabelAndText(toolkit, detailGroup,
				Messages.getString("IMC.CONFIGURATION_LBL_VARNAME"), SWT.BORDER);
		unitConfigText = createLabelAndText(toolkit, detailGroup,
				Messages.getString("IMC.CONFIGURATION_LBL_UNIT"), SWT.BORDER);
		constraintConfigText = createLabelAndText(toolkit, detailGroup,
				Messages.getString("IMC.CONFIGURATION_LBL_CONSTRAINT"), SWT.BORDER);
		String[] widgetItems = {"text", "slider", "spin", "radio", "checkbox", "ordered_list" };
		widgetCombo = createLabelAndCombo(toolkit, detailGroup,
				Messages.getString("IMC.CONFIGURATION_LBL_WIDGET"),
				widgetItems);
		stepText = createLabelAndText(toolkit, detailGroup,
				Messages.getString("IMC.CONFIGURATION_LBL_STEP"), SWT.BORDER);

		widgetCombo.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if( ((Combo)e.widget).getText().trim().equals(IRtcBuilderConstants.CONFIG_WIDGET_SLIDER) ||
						((Combo)e.widget).getText().trim().equals(IRtcBuilderConstants.CONFIG_WIDGET_SPIN) ) {
					stepText.setEnabled(true);
				} else {
					stepText.setEnabled(false);
				}
			}
		});
		/////
		Group documentGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		documentGroup.setLayout(new GridLayout(2, false));
		documentGroup.setText("Documentation");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		documentGroup.setLayoutData(gd);
		//
		datanameText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.CONFIGURATION_LBL_DATANAME"), SWT.BORDER);
		defaultValText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.CONFIGURATION_LBL_DEFAULT"), SWT.BORDER);
		descriptionText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.CONFIGURATION_LBL_DESCRIPTION"), SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 50;
		descriptionText.setLayoutData(gridData);
		unitText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.CONFIGURATION_LBL_UNIT"), SWT.BORDER);
		rangeText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.CONFIGURATION_LBL_RANGE"), SWT.BORDER);
		constraintText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.CONFIGURATION_LBL_CONSTRAINT"), SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		constraintText.setLayoutData(gridData);
	}

	private TableViewer createConfigurationSetSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createSectionBaseWithLabel(toolkit, form,
				Messages.getString("IMC.CONFIGURATION_SET_TITLE"), Messages.getString("IMC.CONFIGURATION_SET_EXPL"), 3);
		//
		createLabel(toolkit, composite,
				IMessageConstants.REQUIRED + Messages.getString("IMC.CONFIGURATION_TBLLBL_NAME"),
				3,
				getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_RED));
		//
		final TableViewer configSetTableViewer = createTableViewer(toolkit,	composite);
		configSetTableViewer.getTable().setHeaderVisible(false);

		final TableViewerColumn col = super.createColumn(configSetTableViewer, "",
				IRtcBuilderConstants.SINGLE_COLUMN_WIDTH);
		col.setEditingSupport(new ConfigSetCellModifier(configSetTableViewer));
//		col.getColumn().setResizable(false);
		configSetTableViewer.setLabelProvider(new ConfigSetLabelProvider());
		composite.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Point size = configSetTableViewer.getControl().getSize();
				ScrollBar vBar = configSetTableViewer.getTable().getVerticalBar();
				col.getColumn().setWidth(size.x- vBar.getSize().x*2);
			}
		});
		//
		Composite buttonComposite = toolkit.createComposite(composite, SWT.NONE);
		GridLayout gl = new GridLayout();
		buttonComposite.setLayout(gl);
		gl.marginWidth = 1;
		GridData gd = new GridData();
		gd.verticalAlignment = SWT.BEGINNING;
		gd.widthHint = 80;
		buttonComposite.setLayoutData(gd);

		configurationSetAddButton = createConfigAddButton(toolkit,
				configSetTableViewer, buttonComposite);
		configurationSetDeleteButton = createConfigDeleteButton(toolkit,
				configSetTableViewer, buttonComposite);
		//
		configSetTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				setDocumentContents();
				StructuredSelection selection = (StructuredSelection)event.getSelection();
				selectParam = (ConfigSetParam)selection.getFirstElement();
				if( selectParam != null ) {
					parametertNameDetailText.setText(selectParam.getName());
					updateDefaultTypeList(selectParam.getType());
					typeCombo.setText(selectParam.getType());
					defaultValueText.setText(selectParam.getDefaultVal());
					variableNameText.setText(StringUtil.getDisplayDocText(selectParam.getVarName()));
					unitConfigText.setText(StringUtil.getDisplayDocText(selectParam.getUnit()));
					constraintConfigText.setText(StringUtil.getDisplayDocText(selectParam.getConstraint()));
					widgetCombo.setText(StringUtil.getDisplayDocText(selectParam.getWidget()));
					stepText.setText(StringUtil.getDisplayDocText(selectParam.getStep()));
					//
//					parametertNameText.setText(selectParam.getName());
					datanameText.setText(StringUtil.getDisplayDocText(selectParam.getDocDataName()));
					defaultValText.setText(StringUtil.getDisplayDocText(selectParam.getDocDefaultVal()));
					descriptionText.setText(StringUtil.getDisplayDocText(selectParam.getDocDescription()));
					unitText.setText(StringUtil.getDisplayDocText(selectParam.getDocUnit()));
					rangeText.setText(StringUtil.getDisplayDocText(selectParam.getDocRange()));
					constraintText.setText(StringUtil.getDisplayDocText(selectParam.getDocConstraint()));
					preSelection = selectParam;
				}
			}
		});
		return configSetTableViewer;
	}

	private Button createConfigDeleteButton(FormToolkit toolkit, final TableViewer configSetTableViewer, Composite buttonComposite) {
		Button deleteButton = toolkit.createButton(buttonComposite, "Delete", SWT.PUSH);
		deleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectionIndex = configSetTableViewer.getTable()
						.getSelectionIndex();
				if (selectionIndex >= 0
						&& ((List) configSetTableViewer.getInput()).size() >= selectionIndex + 1) {
					((List) configSetTableViewer.getInput())
							.remove(selectionIndex);
					configSetTableViewer.refresh();
					preSelection = null;
					clearText();
					update();
				}
			}
		});
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		deleteButton.setLayoutData(gd);
		return deleteButton;
	}

	private Button createConfigAddButton(FormToolkit toolkit, final TableViewer configSetTableViewer, Composite buttonComposite) {
		Button addButton = toolkit.createButton(buttonComposite, "Add", SWT.PUSH);
		addButton.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				String selected = typeCombo.getText();
				updateDefaultValue();
				String confNum = Integer.valueOf(((List) configSetTableViewer.getInput()).size()).toString();
				ConfigSetParam selectParam = new ConfigSetParam(defaultConfigName + confNum, defaultConfigType, defaultConfigVarName, defaultConfigDefault, defaultConfigConstraint, defaultConfigUnit);
				((List) configSetTableViewer.getInput()).add(selectParam);
				configSetTableViewer.refresh();
				update();
				configSetTableViewer.setSelection(new StructuredSelection(selectParam), true);
				typeCombo.setText(selected);
			}
		});
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		addButton.setLayoutData(gd);
		return addButton;
	}

	private void createHintSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createHintSectionBase(toolkit, form, 4);
		//
		createHintLabel(Messages.getString("IMC.CONFIGURATION_HINT_COFIGPARAM_TITLE"), IMessageConstants.CONFIGURATION_HINT_COFIGPARAM_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.CONFIGURATION_HINT_PARAMNAME_TITLE"), IMessageConstants.CONFIGURATION_HINT_PARAMNAME_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.CONFIGURATION_HINT_PARAMTYPE_TITLE"), IMessageConstants.CONFIGURATION_HINT_PARAMTYPE_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.CONFIGURATION_HINT_DEFAULT_TITLE"), IMessageConstants.CONFIGURATION_HINT_DEFAULT_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.CONFIGURATION_HINT_VARNAME_TITLE"), IMessageConstants.CONFIGURATION_HINT_VARNAME_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.CONFIGURATION_HINT_UNIT_TITLE"), Messages.getString("IMC.CONFIGURATION_HINT_UNIT_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.CONFIGURATION_HINT_CONSTRAINT_TITLE"), IMessageConstants.CONFIGURATION_HINT_CONSTRAINT_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.CONFIGURATION_HINT_WIDGET_TITLE"), IMessageConstants.CONFIGURATION_HINT_WIDGET_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.CONFIGURATION_HINT_STEP_TITLE"), IMessageConstants.CONFIGURATION_HINT_STEP_DESC, toolkit, composite);
		createHintSpace(toolkit, composite);
		createHintLabel(Messages.getString("IMC.HINT_DOCUMENT_TITLE"), "", toolkit, composite);
		createHintLabel(Messages.getString("IMC.CONFIGURATION_LBL_DATANAME"), Messages.getString("IMC.CONFIGURATION_HINT_DOC_NAME_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.CONFIGURATION_LBL_DEFAULT"), Messages.getString("IMC.CONFIGURATION_HINT_DOC_DEFAULT_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.CONFIGURATION_LBL_DESCRIPTION"), Messages.getString("IMC.CONFIGURATION_HINT_DOC_DESCRIPTION_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.CONFIGURATION_LBL_UNIT"), Messages.getString("IMC.CONFIGURATION_HINT_DOC_UNIT_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.CONFIGURATION_LBL_RANGE"), IMessageConstants.CONFIGURATION_HINT_DOC_RANGE, toolkit, composite);
		createHintLabel(Messages.getString("IMC.CONFIGURATION_LBL_CONSTRAINT"), IMessageConstants.CONFIGURATION_HINT_DOC_CONSTRAINT, toolkit, composite);
	}

	public void updateForOutput() {
		setDocumentContents();
	}

	private void setDocumentContents() {
		setContents(preSelection);
	}

	public void update() {
		setContents(selectParam);
		editor.updateDirty();
	}

	private void setContents(ConfigSetParam target) {
		if( target != null ) {
			target.setType(typeCombo.getText());
			target.setDefaultVal(StringUtil.getDocText(defaultValueText.getText()));
			target.setVarName(StringUtil.getDocText(variableNameText.getText()));
			target.setUnit(StringUtil.getDocText(unitConfigText.getText()));
			target.setConstraint(StringUtil.getDocText(constraintConfigText.getText()));
			target.setWidget(StringUtil.getDocText(widgetCombo.getText()));
			target.setStep(StringUtil.getDocText(stepText.getText()));
			//
			target.setDocDataName(StringUtil.getDocText(datanameText.getText()));
			target.setDocDefaultVal(StringUtil.getDocText(defaultValText.getText()));
			target.setDocDescription(StringUtil.getDocText(descriptionText.getText()));
			target.setDocUnit(StringUtil.getDocText(unitText.getText()));
			target.setDocRange(StringUtil.getDocText(rangeText.getText()));
			target.setDocConstraint(StringUtil.getDocText(constraintText.getText()));
		}
	}

	private void clearText() {
		//
		parametertNameDetailText.setText("");
		typeCombo.select(0);
		defaultValueText.setText("");
		variableNameText.setText("");
		unitConfigText.setText("");
		constraintConfigText.setText("");
		widgetCombo.select(0);
		stepText.setText("");
		//
		datanameText.setText("");
		defaultValText.setText("");
		descriptionText.setText("");
		unitText.setText("");
		rangeText.setText("");
		constraintText.setText("");
	}

	/**
	 * データをロードする
	 */
	public void load() {
		if (configurationSetTableViewer == null) return;
		//
		RtcParam rtcParam = editor.getRtcParam();
		configurationSetTableViewer.setInput(rtcParam.getConfigParams());
		//Mac版では列の幅が最小化してしまうため，再度列幅を設定
		configurationSetTableViewer.getTable().getColumn(0).setWidth(IRtcBuilderConstants.SINGLE_COLUMN_WIDTH);
		//
		StructuredSelection selection = (StructuredSelection) configurationSetTableViewer
				.getSelection();
		ConfigSetParam param = (ConfigSetParam) selection.getFirstElement();
		if (param == null) 	clearText();
	}

	public String validateParam() {
		String result = null;

		RtcParam rtcParam = editor.getRtcParam();
		Set<String> checkSet = new HashSet<String>();
		Set<String> checkVarSet = new HashSet<String>();

		for(ConfigSetParam config : rtcParam.getConfigParams()) {
			result = ValidationUtil.validateConfigurationSet(config);
			if( result!=null ) return result;
			//重複
			if( checkSet.contains(config.getName()) ) {
				result = IMessageConstants.CONFIGURATION_VALIDATE_DUPLICATE;
				return result;
			}
			checkSet.add(config.getName());
			//変数重複
			if( checkVarSet.contains(config.getTmplVarName()) ) {
				result = IMessageConstants.CONFIGURATION_VALIDATE_VAR_DUPLICATE;
				return result;
			}
			checkVarSet.add(config.getTmplVarName());
		}
		return null;
	}

	private class ConfigSetLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof ConfigSetParam == false) return null;

			ConfigSetParam configSetParam = (ConfigSetParam) element;

			String result = null;
			if (columnIndex == 0) {
				result = configSetParam.getName();
			} else if (columnIndex == 1) {
				result = configSetParam.getType();
			} else if (columnIndex == 2) {
				result = configSetParam.getDefaultVal();
			}

			return result;
		}
	}

	private int updateDefaultTypeList(String newValue){
		int index = searchIndex(defaultTypeList, newValue);
		if( index == defaultTypeList.length ){
			// その値がプルダウン選択肢にない場合、選択肢にそれを追加する
			String[] newDefaultTypeList = new String[defaultTypeList.length+1];
			for( int i=0; i<defaultTypeList.length; i++ ){
				newDefaultTypeList[i] = defaultTypeList[i];
			}
			if(newValue.trim().length()==0) {
				newDefaultTypeList[defaultTypeList.length] = newValue;
			} else {
				newDefaultTypeList[defaultTypeList.length-1] = newValue;
				newDefaultTypeList[defaultTypeList.length] = "";
			}

			defaultTypeList = newDefaultTypeList;

			typeCombo.setItems(defaultTypeList);
		}
		return index;
	}

	private int searchIndex(String[] sources, String target) {
		for(int intIdx=0;intIdx<sources.length;intIdx++) {
			if( target.equals(sources[intIdx]) )
				return intIdx;
		}
		return sources.length;
	}

	private class ConfigSetCellModifier extends EditingSupport {
		private ColumnViewer viewer;

		public ConfigSetCellModifier(ColumnViewer viewer) {
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
			if (element instanceof ConfigSetParam == false) return null;
			ConfigSetParam configSetParam = (ConfigSetParam) element;

			return configSetParam.getName();
		}

		@Override
		protected void setValue(Object element, Object value) {
			if (element instanceof ConfigSetParam == false) return;
			ConfigSetParam configSetParam = (ConfigSetParam) element;

			configSetParam.setName((String) value);
			parametertNameDetailText.setText(configSetParam.getName());

			getViewer().update(element, null);
			update();
		}
	}

	/**
	 * Configurationフォーム内の要素の有効/無効を設定します。
	 * <ul>
	 * <li>config.configSet.table : ConfigurationSetセクションのテーブル</li>
	 * <li>config.configSet.addButton : ConfigurationSetセクションの Addボタン</li>
	 * <li>config.configSet.deleteButton : ConfigurationSetセクションの Deleteボタン</li>
	 * <li>config.configParam.table : ConfigurationParameterセクションのテーブル</li>
	 * <li>config.configParam.addButton : ConfigurationParameterセクションの Addボタン</li>
	 * <li>config.configParam.deleteButton : ConfigurationParameterセクションの
	 * Deleteボタン</li>
	 * </ul>
	 */
	public void setEnabledInfo(WidgetInfo widgetInfo, boolean enabled) {
		if (widgetInfo.matchSection("configSet")) {
			if (configurationSetTableViewer != null) {
				if (widgetInfo.matchWidget("table")) {
					setViewerEnabled(configurationSetTableViewer, enabled);
				}
				if (widgetInfo.matchWidget("addButton")) {
					setButtonEnabled(configurationSetAddButton, enabled);
				}
				if (widgetInfo.matchWidget("deleteButton")) {
					setButtonEnabled(configurationSetDeleteButton, enabled);
				}
			}
		}
	}
}
