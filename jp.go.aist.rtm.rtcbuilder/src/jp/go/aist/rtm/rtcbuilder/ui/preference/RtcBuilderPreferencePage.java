package jp.go.aist.rtm.rtcbuilder.ui.preference;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import jp.go.aist.rtm.rtcbuilder.RtcBuilderPlugin;
import jp.go.aist.rtm.rtcbuilder.nl.Messages;
import jp.go.aist.rtm.rtcbuilder.ui.editors.IMessageConstants;

public class RtcBuilderPreferencePage extends AbstarctFieldEditorPreferencePage implements
		IWorkbenchPreferencePage {
	public RtcBuilderPreferencePage(){
		setPreferenceStore(RtcBuilderPlugin.getDefault().getPreferenceStore());
	}
	
	@Override
	public void init(IWorkbench workbench) {
		IPreferenceStore store = RtcBuilderPlugin.getDefault().getPreferenceStore();
		storeBackupInitialSetting(store);
	}

	@Override
	protected void createFieldEditors() {
		Composite composite = new Composite(getFieldEditorParent(), SWT.NULL);
		composite.setLayout(new GridLayout());
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		composite.setLayoutData(gd);
		createBackupParts(composite);
		createCodeGenerateParts(composite);
	}
	
	private void createBackupParts(Composite composite) {
		Composite backupGroup = createGroup(composite, 
				Messages.getString("IPreferenceMessageConstants.CODE_GEN_TITLE_BACKUP"));
		IntegerFieldEditor moduleMaxInstanceTextEditor = new IntegerFieldEditor(ComponentPreferenceManager.Generate_Backup_Num,
				IMessageConstants.BACKUP_FILE_NUM, backupGroup);
		addField(moduleMaxInstanceTextEditor);
	}

	private void createCodeGenerateParts(Composite composite) {
		Composite codeGenGroup = createGroup(composite, 
				Messages.getString("IPreferenceMessageConstants.CODE_GEN_TITLE_GENERATED_CAUTION"));
		BooleanFieldEditor generatedBooleanEditor = new BooleanFieldEditor(ComponentPreferenceManager.Not_Generated_Caution,
				Messages.getString("IPreferenceMessageConstants.CODE_GEN_GENERATED_CAUTION"),
				codeGenGroup);
		addField(generatedBooleanEditor);
	}

	private void storeBackupInitialSetting(IPreferenceStore store) {
		store.setDefault(ComponentPreferenceManager.Generate_Backup_Num, ComponentPreferenceManager.DEFAULT_BACKUP_NUM);
		store.setDefault(ComponentPreferenceManager.Not_Generated_Caution, ComponentPreferenceManager.DEFAULT_NOT_GENERATED_CAUTION);
	}
}
