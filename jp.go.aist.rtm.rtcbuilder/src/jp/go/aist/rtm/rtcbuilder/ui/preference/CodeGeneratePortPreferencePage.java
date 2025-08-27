package jp.go.aist.rtm.rtcbuilder.ui.preference;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import jp.go.aist.rtm.rtcbuilder.RtcBuilderPlugin;
import jp.go.aist.rtm.rtcbuilder.nl.Messages;

public class CodeGeneratePortPreferencePage extends AbstarctFieldEditorPreferencePage implements
	IWorkbenchPreferencePage {
	
	public CodeGeneratePortPreferencePage(){
		setPreferenceStore(RtcBuilderPlugin.getDefault().getPreferenceStore());
	}
	@Override
	public void init(IWorkbench workbench) {
		IPreferenceStore store = RtcBuilderPlugin.getDefault().getPreferenceStore();
		storeDataPortInitialSetting(store);
		storeServicePortInitialSetting(store);
		storeServiceInterfaceInitialSetting(store);
		storeEventPortInitialSetting(store);
	}
	
	@Override
	protected void createFieldEditors() {
		Composite composite = new Composite(getFieldEditorParent(), SWT.NULL);
		composite.setLayout(new GridLayout());
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		composite.setLayoutData(gd);
		//
		createDataPortPart(composite);
		createServicePortPart(composite);
		createServiceInterfacePart(composite);
		createEventPortPart(composite);
	}
	
	private void createEventPortPart(Composite composite) {
		Composite eventportGroup = createGroup(composite, IPreferenceMessageConstants.PORT_TITLE_EVENT_PORT);
		DigitAlphabetStringFieldEditor eventPortNameEditor = 
			new DigitAlphabetStringFieldEditor(ComponentPreferenceManager.Generate_EventPort_VarName,
					Messages.getString("IMC.EVENTPORT_LBL_VARNAME"), eventportGroup);
		addField(eventPortNameEditor);
		StringFieldEditor eventPortPrefixEditor = 
			new StringFieldEditor(ComponentPreferenceManager.Generate_EventPort_Prefix,
				IPreferenceMessageConstants.PORT_LBL_PREFIX, eventportGroup);
		addField(eventPortPrefixEditor);
		StringFieldEditor eventPortSuffixEditor = 
			new StringFieldEditor(ComponentPreferenceManager.Generate_EventPort_Suffix,
				IPreferenceMessageConstants.PORT_LBL_SUFFIX, eventportGroup);
		addField(eventPortSuffixEditor);
	}
	private void createServiceInterfacePart(Composite composite) {
		Composite serviceIFGroup = createGroup(composite,
				Messages.getString("IPreferenceMessageConstants.PORT_TITLE_SERVICE_INTERFACE"));
		DigitAlphabetStringFieldEditor serviceIFNameEditor = 
			new DigitAlphabetStringFieldEditor(ComponentPreferenceManager.Generate_ServiceIF_Name,
					Messages.getString("IMC.SERVICEPORT_LBL_IFNAME"), serviceIFGroup);
		addField(serviceIFNameEditor);
		DigitAlphabetStringFieldEditor serviceIFInstanceNameEditor = 
			new DigitAlphabetStringFieldEditor(ComponentPreferenceManager.Generate_ServiceIF_InstanceName,
					Messages.getString("IMC.SERVICEPORT_LBL_IFINSTNAME"), serviceIFGroup);
		addField(serviceIFInstanceNameEditor);
		DigitAlphabetStringFieldEditor serviceIFVarNameEditor = 
			new DigitAlphabetStringFieldEditor(ComponentPreferenceManager.Generate_ServiceIF_VarName,
					Messages.getString("IMC.SERVICEPORT_LBL_IFVARNAME"), serviceIFGroup);
		addField(serviceIFVarNameEditor);
		StringFieldEditor serviceIFPrefixEditor = 
			new StringFieldEditor(ComponentPreferenceManager.Generate_ServiceIF_Prefix,
				IPreferenceMessageConstants.PORT_LBL_PREFIX, serviceIFGroup);
		addField(serviceIFPrefixEditor);
		StringFieldEditor serviceIFSuffixEditor = 
			new StringFieldEditor(ComponentPreferenceManager.Generate_ServiceIF_Suffix,
				IPreferenceMessageConstants.PORT_LBL_SUFFIX, serviceIFGroup);
		addField(serviceIFSuffixEditor);
	}
	private void createServicePortPart(Composite composite) {
		Composite serviceportGroup = createGroup(composite, 
				Messages.getString("IPreferenceMessageConstants.PORT_TITLE_SERVICE_PORT"));
		DigitAlphabetStringFieldEditor servicePortNameEditor = 
			new DigitAlphabetStringFieldEditor(ComponentPreferenceManager.Generate_ServicePort_Name,
					Messages.getString("IMC.SERVICEPORT_LBL_PORTNAME"), serviceportGroup);
		addField(servicePortNameEditor);
		StringFieldEditor servicePortPrefixEditor = 
			new StringFieldEditor(ComponentPreferenceManager.Generate_ServicePort_Prefix,
				IPreferenceMessageConstants.PORT_LBL_PREFIX, serviceportGroup);
		addField(servicePortPrefixEditor);
		StringFieldEditor servicePortSuffixEditor = 
			new StringFieldEditor(ComponentPreferenceManager.Generate_ServicePort_Suffix,
				IPreferenceMessageConstants.PORT_LBL_SUFFIX, serviceportGroup);
		addField(servicePortSuffixEditor);
	}
	private void createDataPortPart(Composite composite) {
		Composite dataportGroup = createGroup(composite, 
				Messages.getString("IPreferenceMessageConstants.PORT_TITLE_DATA_PORT"));
		DigitAlphabetStringFieldEditor dataPortNameEditor = 
			new DigitAlphabetStringFieldEditor(ComponentPreferenceManager.Generate_DataPort_Name,
					Messages.getString("IMC.DATAPORT_LBL_PORTNAME"), dataportGroup);
		addField(dataPortNameEditor);
		StringFieldEditor dataPortTypeEditor = 
			new StringFieldEditor(ComponentPreferenceManager.Generate_DataPort_Type,
					Messages.getString("IMC.DATAPORT_LBL_PORTTYPE"), dataportGroup);
		addField(dataPortTypeEditor);
		DigitAlphabetStringFieldEditor dataPortVarNameEditor = 
			new DigitAlphabetStringFieldEditor(ComponentPreferenceManager.Generate_DataPort_VarName,
					Messages.getString("IMC.DATAPORT_TBLLBL_VARNAME"), dataportGroup);
		addField(dataPortVarNameEditor);
		StringFieldEditor dataPortPrefixEditor = 
			new StringFieldEditor(ComponentPreferenceManager.Generate_DataPort_Prefix,
				IPreferenceMessageConstants.PORT_LBL_PREFIX, dataportGroup);
		addField(dataPortPrefixEditor);
		StringFieldEditor dataPortSuffixEditor = 
			new StringFieldEditor(ComponentPreferenceManager.Generate_DataPort_Suffix,
				IPreferenceMessageConstants.PORT_LBL_SUFFIX, dataportGroup);
		addField(dataPortSuffixEditor);
	}
	
	private void storeEventPortInitialSetting(IPreferenceStore store) {
		store.setDefault(ComponentPreferenceManager.Generate_EventPort_VarName, ComponentPreferenceManager.DEFAULT_EVENTPORT_VARNAME);
		store.setDefault(ComponentPreferenceManager.Generate_EventPort_Prefix, ComponentPreferenceManager.DEFAULT_EVENTPORT_PREFIX);
		store.setDefault(ComponentPreferenceManager.Generate_EventPort_Suffix, ComponentPreferenceManager.DEFAULT_EVENTPORT_SUFFIX);
	}
	private void storeServiceInterfaceInitialSetting(IPreferenceStore store) {
		store.setDefault(ComponentPreferenceManager.Generate_ServiceIF_Name, ComponentPreferenceManager.DEFAULT_SERVICEIF_NAME);
		store.setDefault(ComponentPreferenceManager.Generate_ServiceIF_InstanceName, ComponentPreferenceManager.DEFAULT_SERVICEIF_INSTANCENAME);
		store.setDefault(ComponentPreferenceManager.Generate_ServiceIF_VarName, ComponentPreferenceManager.DEFAULT_SERVICEIF_VARNAME);
		store.setDefault(ComponentPreferenceManager.Generate_ServiceIF_Prefix, ComponentPreferenceManager.DEFAULT_SERVICEIF_PREFIX);
		store.setDefault(ComponentPreferenceManager.Generate_ServiceIF_Suffix, ComponentPreferenceManager.DEFAULT_SERVICEIF_SUFFIX);
	}
	private void storeServicePortInitialSetting(IPreferenceStore store) {
		store.setDefault(ComponentPreferenceManager.Generate_ServicePort_Name, ComponentPreferenceManager.DEFAULT_SERVICEPORT_NAME);
		store.setDefault(ComponentPreferenceManager.Generate_ServicePort_Prefix, ComponentPreferenceManager.DEFAULT_SERVICEPORT_PREFIX);
		store.setDefault(ComponentPreferenceManager.Generate_ServicePort_Suffix, ComponentPreferenceManager.DEFAULT_SERVICEPORT_SUFFIX);
	}
	private void storeDataPortInitialSetting(IPreferenceStore store) {
		store.setDefault(ComponentPreferenceManager.Generate_DataPort_Name, ComponentPreferenceManager.DEFAULT_DATAPORT_NAME);
		store.setDefault(ComponentPreferenceManager.Generate_DataPort_Type, ComponentPreferenceManager.DEFAULT_DATAPORT_TYPE);
		store.setDefault(ComponentPreferenceManager.Generate_DataPort_VarName, ComponentPreferenceManager.DEFAULT_DATAPORT_VARNAME);
		store.setDefault(ComponentPreferenceManager.Generate_DataPort_Prefix, ComponentPreferenceManager.DEFAULT_DATAPORT_PREFIX);
		store.setDefault(ComponentPreferenceManager.Generate_DataPort_Suffix, ComponentPreferenceManager.DEFAULT_DATAPORT_SUFFIX);
	}
}
