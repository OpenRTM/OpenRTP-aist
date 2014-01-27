package jp.go.aist.rtm.rtcbuilder.ui.preference;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class CodeGeneratePreferencePage extends AbstractPreferencePage implements
		IWorkbenchPreferencePage {

	private Text moduleNameText;
	private Text moduleDescriptionText;
	private Text moduleCategoryText;
	private Text moduleVersionText;
	private Text moduleVendorText;
	private Text moduleMaxInstanceText;
	private Text moduleExecutionRateText;
	//
	private Combo typeCombo;
	private Combo activityTypeCombo;
	private Combo componentKindCombo;
	private Combo executionTypeCombo;
	//
	private Text commonPrefixText;
	private Text commonSuffixText;
	//
	private Text configurationNameText;
	private Text configurationTypeText;
	private Text configurationVarNameText;
	private Text configurationDefaultText;
	private Text configurationConstraint;
	private Text configurationUnit;
	private Text configurationPrefixText;
	private Text configurationSuffixText;
	//

	public CodeGeneratePreferencePage() {
	}

	public CodeGeneratePreferencePage(String title) {
		super(title);
	}

	public CodeGeneratePreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}

	@Override
	protected Control createContents(Composite parent) {
		GridData gd;

		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout());
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		composite.setLayoutData(gd);
		//Basic Page
		Group basicGroup = createGroup(composite, IPreferenceMessageConstants.CODE_GEN_TITLE_BASIC);
		moduleNameText = createLabelAndText(basicGroup, IPreferenceMessageConstants.CODE_GEN_LBL_COMPONENT_NAME);
		moduleNameText.setText(String.valueOf(
				ComponentPreferenceManager.getInstance().getBasic_ComponentName()));
		moduleDescriptionText = createLabelAndText(basicGroup, IPreferenceMessageConstants.CODE_GEN_LBL_DESCRIPTION);
		moduleDescriptionText.setText(String.valueOf(
				ComponentPreferenceManager.getInstance().getBasic_Description()));
		moduleVersionText = createLabelAndText(basicGroup, IPreferenceMessageConstants.CODE_GEN_LBL_VERSION);
		moduleVersionText.setText(String.valueOf(
				ComponentPreferenceManager.getInstance().getBasic_Version()));
		moduleVendorText = createLabelAndText(basicGroup, IPreferenceMessageConstants.CODE_GEN_LBL_VENDOR);
		moduleVendorText.setText(String.valueOf(
				ComponentPreferenceManager.getInstance().getBasic_VendorName()));
		moduleCategoryText = createLabelAndText(basicGroup, IPreferenceMessageConstants.CODE_GEN_LBL_CATEGORY);
		moduleCategoryText.setText(String.valueOf(
				ComponentPreferenceManager.getInstance().getBasic_Category()));
		//
		typeCombo = createLabelAndCombo(basicGroup, IPreferenceMessageConstants.CODE_GEN_LBL_COMPONENT_TYPE, IRtcBuilderConstants.COMPONENT_TYPE_ITEMS);
		String defaultSelection = ComponentPreferenceManager.getInstance().getBasic_ComponentType();
		for(int intidx=0; intidx<IRtcBuilderConstants.COMPONENT_TYPE_ITEMS.length ;intidx++ ){
			if( IRtcBuilderConstants.COMPONENT_TYPE_ITEMS[intidx].equals(defaultSelection) ) {
				typeCombo.select(intidx);
				break;
			}
		}
		activityTypeCombo = createLabelAndCombo(basicGroup, IPreferenceMessageConstants.CODE_GEN_LBL_COMPONENT_S_ACTIVITY_TYPE, IRtcBuilderConstants.ACTIVITY_TYPE_ITEMS);
		defaultSelection = ComponentPreferenceManager.getInstance().getBasic_ActivityType();
		for(int intidx=0; intidx<IRtcBuilderConstants.ACTIVITY_TYPE_ITEMS.length ;intidx++ ){
			if( IRtcBuilderConstants.ACTIVITY_TYPE_ITEMS[intidx].equals(defaultSelection) ) {
				activityTypeCombo.select(intidx);
				break;
			}
		}
		componentKindCombo = createLabelAndCombo(basicGroup, IPreferenceMessageConstants.CODE_GEN_LBL_COMPONENT_KIND, IRtcBuilderConstants.COMPONENT_KIND_ITEMS);
		defaultSelection = ComponentPreferenceManager.getInstance().getBasic_ComponentKind();
		for(int intidx=0; intidx<IRtcBuilderConstants.COMPONENT_KIND_ITEMS.length ;intidx++ ){
			if( IRtcBuilderConstants.COMPONENT_KIND_ITEMS[intidx].equals(defaultSelection) ) {
				componentKindCombo.select(intidx);
				break;
			}
		}
		//
		moduleMaxInstanceText = createLabelAndText(basicGroup, IPreferenceMessageConstants.CODE_GEN_LBL_MAX_INSTANCES);
		moduleMaxInstanceText.setText(String.valueOf(
				ComponentPreferenceManager.getInstance().getBasic_MaxInstances()));
		//
		executionTypeCombo = createLabelAndCombo(basicGroup, IPreferenceMessageConstants.CODE_GEN_LBL_EXECUTION_TYPE, IRtcBuilderConstants.EXECUTIONCONTEXT_TYPE_ITEMS);
		defaultSelection = ComponentPreferenceManager.getInstance().getBasic_ExecutionType();
		for(int intidx=0; intidx<IRtcBuilderConstants.EXECUTIONCONTEXT_TYPE_ITEMS.length ;intidx++ ){
			if( IRtcBuilderConstants.EXECUTIONCONTEXT_TYPE_ITEMS[intidx].equals(defaultSelection) ) {
				executionTypeCombo.select(intidx);
				break;
			}
		}
		//
		moduleExecutionRateText = createLabelAndText(basicGroup, IPreferenceMessageConstants.CODE_GEN_LBL_EXECUTION_RATE);
		moduleExecutionRateText.setText(String.valueOf(
				ComponentPreferenceManager.getInstance().getBasic_ExecutionRate()));
		//
		commonPrefixText = createLabelAndText(basicGroup, IPreferenceMessageConstants.PORT_LBL_PREFIX);
		commonPrefixText.setText(String.valueOf(ComponentPreferenceManager.getInstance().getBasic_Prefix()));
		//
		commonSuffixText = createLabelAndText(basicGroup, IPreferenceMessageConstants.PORT_LBL_SUFFIX);
		commonSuffixText.setText(String.valueOf(ComponentPreferenceManager.getInstance().getBasic_Suffix()));
		//
		//Configuration I/F Page
		Group configGroup = createGroup(composite, IPreferenceMessageConstants.CODE_GEN_TITLE_CONFIG);
		configurationNameText = createLabelAndText(configGroup, IPreferenceMessageConstants.CODE_GEN_LBL_NAME);
		configurationNameText.setText(String.valueOf(
				ComponentPreferenceManager.getInstance().getConfiguration_Name()));
		configurationTypeText = createLabelAndText(configGroup, IPreferenceMessageConstants.CODE_GEN_LBL_TYPE);
		configurationTypeText.setText(String.valueOf(
				ComponentPreferenceManager.getInstance().getConfiguration_Type()));
		configurationVarNameText = createLabelAndText(configGroup, IPreferenceMessageConstants.CODE_GEN_LBL_VARIABLE_NAME);
		configurationVarNameText.setText(String.valueOf(
				ComponentPreferenceManager.getInstance().getConfiguration_VarName()));
		configurationDefaultText = createLabelAndText(configGroup, IPreferenceMessageConstants.CODE_GEN_LBL_DEFAULT_VALUE);
		configurationDefaultText.setText(String.valueOf(
				ComponentPreferenceManager.getInstance().getConfiguration_Default()));
		configurationConstraint = createLabelAndText(configGroup, IPreferenceMessageConstants.CODE_GEN_LBL_CONSTRAINT);
		configurationConstraint.setText(String.valueOf(
				ComponentPreferenceManager.getInstance().getConfiguration_Constraint()));
		configurationUnit = createLabelAndText(configGroup, IPreferenceMessageConstants.CODE_GEN_LBL_UNIT);
		configurationUnit.setText(String.valueOf(
				ComponentPreferenceManager.getInstance().getConfiguration_Unit()));
		//
		//
		configurationPrefixText = createLabelAndText(configGroup, IPreferenceMessageConstants.PORT_LBL_PREFIX);
		configurationPrefixText.setText(String.valueOf(ComponentPreferenceManager.getInstance().getConfiguration_Prefix()));
		//
		configurationSuffixText = createLabelAndText(configGroup, IPreferenceMessageConstants.PORT_LBL_SUFFIX);
		configurationSuffixText.setText(String.valueOf(ComponentPreferenceManager.getInstance().getConfiguration_Suffix()));

		return composite;
	}

	@Override
	protected void performDefaults() {
		setDefault();

		super.performDefaults();
	}

	@Override
	public boolean performOk() {
		ComponentPreferenceManager.getInstance().setBasic_ComponentName(moduleNameText.getText());
		ComponentPreferenceManager.getInstance().setBasic_Description(moduleDescriptionText.getText());
		ComponentPreferenceManager.getInstance().setBasic_Category(moduleCategoryText.getText());
		ComponentPreferenceManager.getInstance().setBasic_Version(moduleVersionText.getText());
		ComponentPreferenceManager.getInstance().setBasic_VendorName(moduleVendorText.getText());
		ComponentPreferenceManager.getInstance().setBasic_ComponentType(typeCombo.getText());
		ComponentPreferenceManager.getInstance().setBasic_ActivityType(activityTypeCombo.getText());
		ComponentPreferenceManager.getInstance().setBasic_ComponentKind(componentKindCombo.getText());
		ComponentPreferenceManager.getInstance().setBasic_MaxInstances(
				Integer.valueOf(moduleMaxInstanceText.getText()).intValue());
		ComponentPreferenceManager.getInstance().setBasic_ExecutionType(executionTypeCombo.getText());
		ComponentPreferenceManager.getInstance().setBasic_ExecutionRate(
				Double.valueOf(moduleExecutionRateText.getText()).intValue());
		ComponentPreferenceManager.getInstance().setBasic_Prefix(commonPrefixText.getText());
		ComponentPreferenceManager.getInstance().setBasic_Suffix(commonSuffixText.getText());
		//
		ComponentPreferenceManager.getInstance().setConfiguration_Name(configurationNameText.getText());
		ComponentPreferenceManager.getInstance().setConfiguration_Type(configurationTypeText.getText());
		ComponentPreferenceManager.getInstance().setConfiguration_VarName(configurationVarNameText.getText());
		ComponentPreferenceManager.getInstance().setConfiguration_Default(configurationDefaultText.getText());
		ComponentPreferenceManager.getInstance().setConfiguration_Constraint(configurationConstraint.getText());
		ComponentPreferenceManager.getInstance().setConfiguration_Unit(configurationUnit.getText());
		ComponentPreferenceManager.getInstance().setConfiguration_Prefix(configurationPrefixText.getText());
		ComponentPreferenceManager.getInstance().setConfiguration_Suffix(configurationSuffixText.getText());

		ComponentPreferenceManager.getInstance().setDirtyToPreferenceStatus();

		return super.performOk();
	}

	private void setDefault() {
		moduleNameText.setText(ComponentPreferenceManager.DEFAULT_COMPONENT_NAME);
		moduleDescriptionText.setText(ComponentPreferenceManager.DEFAULT_DESCRIPTION);
		moduleCategoryText.setText(ComponentPreferenceManager.DEFAULT_CATEGORY);
		moduleVersionText.setText(ComponentPreferenceManager.DEFAULT_VERSION);
		moduleVendorText.setText(ComponentPreferenceManager.DEFAULT_VENDER);
		typeCombo.select(0);
		activityTypeCombo.select(0);
		componentKindCombo.select(0);
		moduleMaxInstanceText.setText(String.valueOf(ComponentPreferenceManager.DEFAULT_MAXINST));
		executionTypeCombo.select(0);
		moduleExecutionRateText.setText(String.valueOf(ComponentPreferenceManager.DEFAULT_EXECUTION_RATE));
		commonPrefixText.setText(String.valueOf(ComponentPreferenceManager.DEFAULT_PREFIX));
		commonSuffixText.setText(String.valueOf(ComponentPreferenceManager.DEFAULT_SUFFIX));
		//
		configurationNameText.setText(String.valueOf(ComponentPreferenceManager.DEFAULT_CONFIGURATION_NAME));
		configurationTypeText.setText(String.valueOf(ComponentPreferenceManager.DEFAULT_CONFIGURATION_TYPE));
		configurationVarNameText.setText(String.valueOf(ComponentPreferenceManager.DEFAULT_CONFIGURATION_VARNAME));
		configurationDefaultText.setText(String.valueOf(ComponentPreferenceManager.DEFAULT_CONFIGURATION_DEFAULT));
		configurationConstraint.setText(String.valueOf(ComponentPreferenceManager.DEFAULT_CONFIGURATION_CONSTRAINT));
		configurationUnit.setText(String.valueOf(ComponentPreferenceManager.DEFAULT_CONFIGURATION_UNIT));
		configurationPrefixText.setText(String.valueOf(ComponentPreferenceManager.DEFAULT_CONFIGURATION_PREFIX));
		configurationSuffixText.setText(String.valueOf(ComponentPreferenceManager.DEFAULT_CONFIGURATION_SUFFIX));
	}
	
	protected boolean validate() {
		if( moduleNameText.getText()==null || moduleNameText.getText().equals("") ) {
			return false;
		}
		//
		try {
			int parse = Integer.parseInt(moduleMaxInstanceText.getText());
			if(parse<0) return false;
		} catch (Exception e) {
			return false;
		}
		//
		try {
			double parse = Double.parseDouble(moduleExecutionRateText.getText());
			if(parse<0) return false;
		} catch (Exception e) {
			return false;
		}
		//
		return true;
	}
}