package jp.go.aist.rtm.rtcbuilder.ui.editors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.go.aist.rtm.rtcbuilder.Generator.MergeHandler;
import jp.go.aist.rtm.rtcbuilder.GuiRtcBuilder;
import jp.go.aist.rtm.rtcbuilder.IRTCBMessageConstants;
import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.RtcBuilderPlugin;
import jp.go.aist.rtm.rtcbuilder.extension.ImportExtension;
import jp.go.aist.rtm.rtcbuilder.factory.ExportCreator;
import jp.go.aist.rtm.rtcbuilder.fsm.ScXMLHandler;
import jp.go.aist.rtm.rtcbuilder.fsm.StateParam;
import jp.go.aist.rtm.rtcbuilder.generator.ProfileHandler;
import jp.go.aist.rtm.rtcbuilder.generator.param.GeneratorParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.PropertyParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.RtcParam;
import jp.go.aist.rtm.rtcbuilder.manager.GenerateManager;
import jp.go.aist.rtm.rtcbuilder.nl.Messages;
import jp.go.aist.rtm.rtcbuilder.ui.Perspective.LanguageProperty;
import jp.go.aist.rtm.rtcbuilder.ui.compare.CompareResultDialog;
import jp.go.aist.rtm.rtcbuilder.ui.compare.CompareTarget;
import jp.go.aist.rtm.rtcbuilder.ui.dialog.RestoreDialog;
import jp.go.aist.rtm.rtcbuilder.ui.preference.ComponentPreferenceManager;
import jp.go.aist.rtm.rtcbuilder.ui.preference.DocumentPreferenceManager;
import jp.go.aist.rtm.rtcbuilder.util.FileUtil;
import jp.go.aist.rtm.rtcbuilder.util.RTCUtil;
import jp.go.aist.rtm.rtcbuilder.util.StringUtil;

/**
 * Basic Profile 設定ページ
 */
public class BasicEditorFormPage extends AbstractEditorFormPage {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BasicEditorFormPage.class);

	/**
	 * 生成を行ったCategoryの情報を保存するワークスペース永続文字列へのキー
	 */
	private static final String CATEGORY_INDEX_KEY = BasicEditorFormPage.class.getName() + ".category.name";
	private final String CATEGORY_COMPOSITE =  "composite.";
	private final String KIND_CHOREONOID =  "BodyIoRTC";

	private Text nameText;
	private Combo categoryCombo;
	private Text descriptionText;
	private Text versionText;
	private Text venderText;
	private Combo activityTypeCombo;
	private Combo typeCombo;
	private Text maxInstanceText;
	private Combo executionTypeCombo;
	private Text executionRateText;
	private Text abstractText;
	private Button choreonoidBtn;

	private Text rtcTypeText;

	private List<GenerateManager> managerList = null;
	private Group LangGroup;
	private Button cppRadio;
	private List<Button> buttonList = new ArrayList<Button>();
	
	private Button generateButton;
	private Button codeRestoreButton;

	private Button profileLoadButton;
	private Button profileSaveButton;

	private Composite generateSection;
	private Composite codeRestoreSection;
	private Composite outputProjectSection;
	private Composite profileSection;
	
	private String[] defaultCategory = { "Actuator", "Animation", "Autonomous Decentralized System", "Chatbot", "Computer Algebra System",
			 "Controller", "Converter", "Database", "Educational Tool", "Game",
			 "ImageProcessiong", "Machine Learning", "Manipulator", "Manufacturing System", "Master-Slave",
			 "Mobile Robot", "Motion Capture", "Navigation", "Nonholonomic System", "Office Suite",
			 "Robot", "Sample", "Script Engine", "Sensor", "Simulator",
			 "Speech Processing", "Streaming", "Test", "Transport System", "User Interface",
			 "Vehicle", "Web Application Server"};

	/**
	 * コンストラクタ
	 *
	 * @param editor
	 *            親のエディタ
	 */
	public BasicEditorFormPage(RtcBuilderEditor editor) {
		super(editor, "id", Messages.getString("IMC.BASIC_SECTION"));
	}

	/**
	 * {@inheritDoc}
	 */
	protected void createFormContent(final IManagedForm managedForm) {
		ScrolledForm form = super.createBase(managedForm, Messages.getString("IMC.BASIC_COMPONENT_TITLE"));
		FormToolkit toolkit = managedForm.getToolkit();
		createModuleSection(toolkit, form);
		//
		getSite().setSelectionProvider(new ISelectionProvider() {
			public void addSelectionChangedListener(
				ISelectionChangedListener listener) {
			}
			public ISelection getSelection() {
				return new StructuredSelection(buildview);
			}
			public void removeSelectionChangedListener(
				ISelectionChangedListener listener) {
			}
			public void setSelection(ISelection selection) {
			}
		});
		//
		createHintSection(toolkit, form);
		createLanguageSection(toolkit, form);
		createGenerateSection(toolkit, form);
		createCodeRestoreSection(toolkit, form);
		createExportImportSection(toolkit, form);
		//
		managerList = RtcBuilderPlugin.getDefault().getLoader()
				.getManagerList();
		if (managerList != null) {
			for (String key : RtcBuilderPlugin.getDefault().getLoader()
					.getManagerKeyList()) {
				Button extRadio = createRadioCheckButton(toolkit, LangGroup,
						key, SWT.RADIO);
				extRadio.addSelectionListener(createLanguageRadioListner());
				buttonList.add(extRadio);
			}
		}
		//
		// 言語・環境ページより先にこのページが表示された場合、ここで言語を判断する
		editor.setEnabledInfoByLang();

		load();
	}

	private String getFileExtension(String filename){
		int index = filename.lastIndexOf(".");
		if( index > -1 ) return filename.substring(index + 1);
		return "";
	}

	private void switchPerspective() {

		RtcParam rtcParam = editor.getGeneratorParam().getRtcParam();
		//Pluginの存在確認
		LanguageProperty langProp = LanguageProperty.checkPlugin(rtcParam);
		String currentPerspectiveId = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
            							.getActivePage().getPerspective().getId();
		if( langProp != null && !langProp.getPerspectiveId().equals(currentPerspectiveId) ) {
			MessageBox message = new MessageBox(getSite().getShell(),
					SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			message.setText(Messages.getString("IMC.BASIC_PERSPECTIVE_TEXT"));
			message.setMessage(Messages.getString("IMC.BASIC_PERSPECTIVE_MSG1")
					+ langProp.getPerspectiveName()
					+ Messages.getString("IMC.BASIC_PERSPECTIVE_MSG2"));
			if( message.open() == SWT.YES) {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().setPerspective(
						PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId(
								langProp.getPerspectiveId()));
				}
		}
	}

	/**
	 * バリデートを行う。エラーがない場合にはnullを返し、エラーがある場合にはメッセージを返す。
	 *
	 * @return
	 */
	public String validateParam() {
		String result = null;
		//Module Name
		if (result == null && nameText.getText().length() == 0) {
			result = Messages.getString("IMC.BASIC_VALIDATE_NAME1");
		}
		if( !StringUtil.checkDigitAlphabet(nameText.getText()) ) {
			result = IMessageConstants.BASIC_VALIDATE_NAME2;
		}
		//Module category
		if (result == null && categoryCombo.getText().length() == 0) {
			result = IMessageConstants.BASIC_VALIDATE_CATEGORY;
		}
		//Number of maximum instance
		String maxText = maxInstanceText.getText();
		if( maxText != null ) {
			Integer parseInt = null;
			try {
				parseInt = new Integer(Integer.parseInt(maxInstanceText.getText()));
			} catch (Exception e) {
				// void
			}
			if (parseInt == null) {
				result = Messages.getString("IMC.BASIC_VALIDATE_MAXINST1");
			}
			if (parseInt != null && parseInt.intValue() < 0) {
				result = Messages.getString("IMC.BASIC_VALIDATE_MAXINST2");
			}
		}
		//Execution Rate
		Double parseDbl = null;
		try {
			parseDbl = new Double(Double.parseDouble(executionRateText.getText()));
		} catch (Exception e) {
			// void
		}
		if (parseDbl == null) {
			result = Messages.getString("IMC.BASIC_VALIDATE_ECRATE1");
		}
		if (parseDbl != null && parseDbl.intValue() < 0) {
			result = Messages.getString("IMC.BASIC_VALIDATE_ECRATE2");
		}
		//Composite Component
		if( categoryCombo.getText().startsWith(CATEGORY_COMPOSITE) &&
				(editor.getRtcParam().getInports().size()>0 || editor.getRtcParam().getOutports().size()>0 ||
				 editor.getRtcParam().getServicePorts().size()>0) ) {
			result = "Cannot add any Ports to Composite Component.";
		}
		//
		//Language
		RtcParam rtcParam = editor.getRtcParam();

		if( rtcParam.getLangList()==null || rtcParam.getLangList().size()==0 ) {
			result = IMessageConstants.LANGUAGE_SELECTION_CAUTION;
		}

		if(rtcParam.isChoreonoid() && rtcParam.getLangList().contains(IRtcBuilderConstants.LANG_CPP)==false ) {
			result = IMessageConstants.LANGUAGE_CHOREONOID_CAUTION;
		}

		return result;
	}

	private void createModuleSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createSectionBaseWithLabel(toolkit, form,
				Messages.getString("IMC.BASIC_COMPONENT_TITLE"), Messages.getString("IMC.BASIC_COMPONENT_EXPL"), 3);
		//
		nameText = createLabelAndText(toolkit, composite,
				IMessageConstants.REQUIRED + Messages.getString("IMC.BASIC_LBL_MODULENAME"), SWT.NONE, SWT.COLOR_RED, 2);
		descriptionText = createLabelAndText(toolkit, composite, Messages.getString("IMC.BASIC_LBL_DESCRIPTION"), SWT.NONE, SWT.COLOR_BLACK, 2);
		versionText = createLabelAndText(toolkit, composite,
				IMessageConstants.REQUIRED + Messages.getString("IMC.BASIC_LBL_VERSION"), SWT.NONE, SWT.COLOR_RED, 2);
		venderText = createLabelAndText(toolkit, composite,
				IMessageConstants.REQUIRED + Messages.getString("IMC.BASIC_LBL_VENDOR"), SWT.NONE, SWT.COLOR_RED, 2);
		categoryCombo = createEditableCombo(toolkit, composite,
				IMessageConstants.REQUIRED + Messages.getString("IMC.BASIC_LBL_CATEGORY"),
				CATEGORY_INDEX_KEY, defaultCategory, SWT.COLOR_RED, 2);
		typeCombo = createLabelAndCombo(toolkit, composite,
				Messages.getString("IMC.BASIC_LBL_COMPONENT_TYPE"),
				IRtcBuilderConstants.COMPONENT_TYPE_ITEMS, SWT.COLOR_BLACK, 2);
		activityTypeCombo = createLabelAndCombo(toolkit, composite,
				Messages.getString("IMC.BASIC_LBL_ACTIVITY_TYPE"),
				IRtcBuilderConstants.ACTIVITY_TYPE_ITEMS, SWT.COLOR_BLACK, 2);
		//
		maxInstanceText = createLabelAndText(toolkit, composite,
				Messages.getString("IMC.BASIC_LBL_MAX_INSTANCES"),
				SWT.NONE, SWT.COLOR_BLACK, 2);
		executionTypeCombo = createLabelAndCombo(toolkit, composite,
				Messages.getString("IMC.BASIC_LBL_EXECUTION_TYPE"),
				IRtcBuilderConstants.EXECUTIONCONTEXT_TYPE_ITEMS, SWT.COLOR_BLACK, 2);
		executionRateText = createLabelAndText(toolkit,	composite,
				Messages.getString("IMC.BASIC_LBL_EXECUTION_RATE"),
				SWT.NONE, SWT.COLOR_BLACK, 2);
		abstractText = createLabelAndText(toolkit, composite,
				Messages.getString("IMC.BASIC_LBL_ABSTRACT"),
				SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 50;
		gridData.widthHint = 100;
		gridData.horizontalSpan = 2;
		abstractText.setLayoutData(gridData);
		rtcTypeText = createLabelAndText(toolkit, composite,
				Messages.getString("IMC.BASIC_LBL_RTCTYPE"),
				SWT.NONE, SWT.COLOR_BLACK, 2);
	}

	private void createHintSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createHintSectionBase(toolkit, form, 5);
		//
		createHintLabel(Messages.getString("IMC.BASIC_HINT_MODULENAME_TITLE"), IMessageConstants.BASIC_HINT_MODULENAME_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.BASIC_HINT_DESCRIPTION_TITLE"), IMessageConstants.BASIC_HINT_DESCRIPTION_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.BASIC_HINT_VERSION_TITLE"), IMessageConstants.BASIC_HINT_VERSION_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.BASIC_HINT_VENDOR_TITLE"), IMessageConstants.BASIC_HINT_VENDOR_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.BASIC_HINT_CATEGORY_TITLE"), IMessageConstants.BASIC_HINT_CATEGORY_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.BASIC_HINT_COMPTYPE_TITLE"), IMessageConstants.BASIC_HINT_COMPTYPE_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.BASIC_HINT_ACTIVITYTYPE_TITLE"), IMessageConstants.BASIC_HINT_ACTIVITYTYPE_DESC, toolkit, composite);
		createHintLabel(IMessageConstants.BASIC_HINT_MAXINST_TITLE, IMessageConstants.BASIC_HINT_MAXINST_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.BASIC_HINT_EXECUTIONTYPE_TITLE"), Messages.getString("IMC.BASIC_HINT_EXECUTIONTYPE_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.BASIC_HINT_EXECUTIONRATE_TITLE"), IMessageConstants.BASIC_HINT_EXECUTIONRATE_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.BASIC_HINT_ABSTRACT_TITLE"), Messages.getString("IMC.BASIC_HINT_ABSTRACT_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.BASIC_HINT_RTCTYPE_TITLE"), IMessageConstants.BASIC_HINT_RTCTYPE_DESC, toolkit, composite);
		//
		createHintSpace(toolkit, composite);
		createHintLabel(IMessageConstants.LANGUAGE_HINT_LANG_TITLE, IMessageConstants.LANGUAGE_HINT_LANG_DESC, toolkit, composite);
		//
		createHintSpace(toolkit, composite);
		createHintSpace(toolkit, composite);
		createHintSpace(toolkit, composite);
		createHintLabel(Messages.getString("IMC.BASIC_HINT_GENERATE_TITLE"), Messages.getString("IMC.BASIC_HINT_GENERATE_DESC"), toolkit, composite);
		//
		createHintSpace(toolkit, composite);
		createHintSpace(toolkit, composite);
		createHintSpace(toolkit, composite);
		//
		createHintLabel(Messages.getString("IMC.BASIC_HINT_CODE_RESTORE_TITLE"), Messages.getString("IMC.BASIC_HINT_CODE_RESTORE_DESC"), toolkit, composite);
		//
		createHintSpace(toolkit, composite);
		createHintSpace(toolkit, composite);
		createHintSpace(toolkit, composite);
		//
		createHintLabel(Messages.getString("IMC.BASIC_HINT_IMPORT_TITLE"), Messages.getString("IMC.BASIC_HINT_IMPORT_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.BASIC_HINT_EXPORT_TITLE"), Messages.getString("IMC.BASIC_HINT_EXPORT_DESC"), toolkit, composite);
	}

	private void createLanguageSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createSectionBaseWithLabel(toolkit, form,
				IMessageConstants.LANGUAGE_LANG_TITLE, IMessageConstants.LANGUAGE_LANG_EXPL, 2);
		//
		LangGroup = new Group(composite, SWT.NONE);
		LangGroup.setLayout(new GridLayout(1, false));
		GridData gd = new GridData();
		LangGroup.setLayoutData(gd);
		//
		cppRadio = createRadioCheckButton(toolkit, LangGroup, "C++", SWT.RADIO);
		cppRadio.addSelectionListener(createLanguageRadioListner());
	}
	
	private void createGenerateSection(FormToolkit toolkit, ScrolledForm form) {
		generateSection = createSectionBaseWithLabel(toolkit, form,
				Messages.getString("IMC.BASIC_GENERATE_TITLE"), Messages.getString("IMC.BASIC_GENERATE_EXPL"), 3);
		createGenerateButton(toolkit);
		//
		Label sep = toolkit.createLabel(generateSection, "          ");
		choreonoidBtn = createRadioCheckButton(toolkit, generateSection,
				Messages.getString("IMC.BASIC_BTN_CHOREONOID"), SWT.CHECK);
	}

	private void createGenerateButton(FormToolkit toolkit) {
		generateButton = toolkit.createButton(generateSection,
				Messages.getString("IMC.BASIC_BTN_GENERATE"), SWT.NONE);
		generateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				editor.allUpdates();
				String validateRtcParam = editor.validateParam();
				if (validateRtcParam != null) {
					MessageDialog.openError(getSite().getShell(), "Error", validateRtcParam);
					return;
				}
				//動的FSMの場合
				boolean isDynamicFSM = false;
				RtcParam rtcParam = editor.getRtcParam();
				PropertyParam fsm = rtcParam.getProperty(IRtcBuilderConstants.PROP_TYPE_FSM);
				if(fsm!=null) {
					if(Boolean.valueOf(fsm.getValue())) {
						PropertyParam fsmType = rtcParam.getProperty(IRtcBuilderConstants.PROP_TYPE_FSMTYTPE);
						if(fsmType!=null && fsmType.getValue().equals(IRtcBuilderConstants.FSMTYTPE_DYNAMIC)) {
							isDynamicFSM = true;
						}
					}
				}
				//対象プロジェクトの確認
				IProject project = checkTargetProject(editor.getRtcParam().getOutputProject(), true);
				if( project==null) return;
				// 裏からファイルを削除されている可能性があるため、
				// プロジェクトとファイルシステムの同期を取る
				try {
					project.refreshLocal(IResource.DEPTH_INFINITE, null);
				} catch (CoreException e1) {
					throw new RuntimeException(IRTCBMessageConstants.ERROR_GENERATE_FAILED);
				}
				//
				editor.addDefaultComboValue();
				GuiRtcBuilder rtcBuilder = new GuiRtcBuilder();
				List<GenerateManager> managerList = RtcBuilderPlugin
						.getDefault().getLoader().getManagerList();
				if (managerList != null) {
					for (GenerateManager manager : managerList) {
						rtcBuilder.addGenerateManager(manager);
					}
				}
				GeneratorParam generatorParam = editor.getGeneratorParam();
				//TODO 複数コンポーネント対応版とする場合には複数設定
				generatorParam.getRtcParam().getServiceClassParams().clear();
				setPrefixSuffix(generatorParam.getRtcParam());
				RTCUtil.getIDLPathes(editor.getRtcParam());
				String genTime = DATE_FORMAT.format(new GregorianCalendar().getTime());
				if (rtcBuilder.doGenerateWrite(generatorParam, editor.getRtcParam().getIdlSearchPathList(), !isDynamicFSM, genTime)) {
					LanguageProperty langProp = LanguageProperty.checkPlugin(editor.getRtcParam());
					if(langProp != null) {
						try {
							IProjectDescription description = project.getDescription();
							String[] ids = description.getNatureIds();
							String[] newIds = new String[ids.length + langProp.getNatures().size()];
							System.arraycopy(ids, 0, newIds, 0, ids.length);
							for( int intIdx=0; intIdx<langProp.getNatures().size(); intIdx++ ) {
								newIds[ids.length+intIdx] = langProp.getNatures().get(intIdx);
							}
							description.setNatureIds(newIds);
							project.setDescription(description, null);
						} catch (CoreException e1) {
							LOGGER.error(
									"Fail to get/set description for project",
									e1);
						}
					}
					//
					saveRtcProfile(project, genTime);
					switchPerspective();
	        		editor.getRtcParam().resetUpdated();
	        		editor.updateDirty();
					//
					try {
						project.refreshLocal(IResource.DEPTH_INFINITE, null);
					} catch (CoreException e1) {
						throw new RuntimeException(IRTCBMessageConstants.ERROR_GENERATE_FAILED);
					}
				}
        		//
				if(isDynamicFSM) {
					generateDynamicFSM();
					return;
				}
			}

			private void generateDynamicFSM() {
				RtcParam rtcParam = editor.getRtcParam();
				StateParam stateParam = rtcParam.getFsmParam();
				
				List<RtcParam> stateList = new ArrayList<RtcParam>();
				
				RtcParam stateRtc = createDefaultRTC(stateParam);
				stateList.add(stateRtc);
				for(StateParam subState : stateParam.getAllStateList()) {
					RtcParam subRtc = createDefaultRTC(subState);
					stateList.add(subRtc);
				}
				//
				editor.addDefaultComboValue();
				GuiRtcBuilder rtcBuilder = new GuiRtcBuilder();
				List<GenerateManager> managerList = RtcBuilderPlugin.getDefault().getLoader().getManagerList();
				if (managerList != null) {
					for (GenerateManager manager : managerList) {
						rtcBuilder.addGenerateManager(manager);
					}
				}
				GeneratorParam generatorParam = editor.getGeneratorParam();
				RtcParam orgRtc = generatorParam.getRtcParam();
				
				for(RtcParam targetFsm : stateList) {
					IProject project = checkTargetProject(targetFsm.getOutputProject(), false);
					if( project==null) continue;
					try {
						project.refreshLocal(IResource.DEPTH_INFINITE, null);
					} catch (CoreException e1) {
						throw new RuntimeException(IRTCBMessageConstants.ERROR_GENERATE_FAILED);
					}
					//
					targetFsm.getServiceClassParams().clear();
					setPrefixSuffix(targetFsm);
					generatorParam.setRtcParam(targetFsm);
					//
					RTCUtil.getIDLPathes(editor.getRtcParam());
					String genTime = DATE_FORMAT.format(new GregorianCalendar().getTime());
					if (rtcBuilder.doGenerateWrite(generatorParam, editor.getRtcParam().getIdlSearchPathList(), false, genTime)) {
						LanguageProperty langProp = LanguageProperty.checkPlugin(editor.getRtcParam());
						if(langProp != null) {
							try {
								IProjectDescription description = project.getDescription();
								String[] ids = description.getNatureIds();
								String[] newIds = new String[ids.length + langProp.getNatures().size()];
								System.arraycopy(ids, 0, newIds, 0, ids.length);
								for( int intIdx=0; intIdx<langProp.getNatures().size(); intIdx++ ) {
									newIds[ids.length+intIdx] = langProp.getNatures().get(intIdx);
								}
								description.setNatureIds(newIds);
								project.setDescription(description, null);
							} catch (CoreException e1) {
								LOGGER.error(
										"Fail to get/set description for project",
										e1);
							}
						}
					}
					saveRtcProfile(project, genTime);
				}
        		editor.getRtcParam().resetUpdated();
        		editor.updateDirty();
				generatorParam.setRtcParam(orgRtc);
				MessageDialog.openInformation(getSite().getShell(), "Information", "Generate success.");
			}
			
			private RtcParam createDefaultRTC(StateParam stateParam) {
				List<String> langList = new ArrayList<String>();
				List<String> langArgList = new ArrayList<String>();
				String rtmVersion = IRtcBuilderConstants.DEFAULT_RTM_VERSION;
				langList.add(IRtcBuilderConstants.LANG_CPP);
				langArgList.add(IRtcBuilderConstants.LANG_CPP_ARG);
				
				RtcParam targetRtc = new RtcParam(null, false);
				targetRtc.setName(stateParam.getName());
				targetRtc.setSchemaVersion(IRtcBuilderConstants.SCHEMA_VERSION);
				targetRtc.setDescription(ComponentPreferenceManager.getInstance().getBasic_Description());
				targetRtc.setCategory(ComponentPreferenceManager.getInstance().getBasic_Category());
				targetRtc.setVersion(ComponentPreferenceManager.getInstance().getBasic_Version());
				targetRtc.setVender(ComponentPreferenceManager.getInstance().getBasic_VendorName());
				targetRtc.setComponentType(ComponentPreferenceManager.getInstance().getBasic_ComponentType());
				targetRtc.setActivityType(ComponentPreferenceManager.getInstance().getBasic_ActivityType());
				targetRtc.setComponentKind("DataFlowComponent");
				targetRtc.setMaxInstance(ComponentPreferenceManager.getInstance().getBasic_MaxInstances());
				targetRtc.setExecutionType(ComponentPreferenceManager.getInstance().getBasic_ExecutionType());
				targetRtc.setExecutionRate(ComponentPreferenceManager.getInstance().getBasic_ExecutionRate());
				ArrayList<String> docs = DocumentPreferenceManager.getDocumentValue();
				for( int intidx=IRtcBuilderConstants.ACTIVITY_INITIALIZE; intidx<IRtcBuilderConstants.ACTIVITY_DUMMY; intidx++) {
					targetRtc.setActionImplemented(intidx, docs.get(intidx));
				}
				targetRtc.setDocLicense(DocumentPreferenceManager.getLicenseValue());
				targetRtc.setDocCreator(DocumentPreferenceManager.getCreatorValue());
				//
				targetRtc.setOutputProject(stateParam.getName());
				targetRtc.getLangList().addAll(langList);
				targetRtc.getLangArgList().addAll(langArgList);
				targetRtc.setRtmVersion(rtmVersion);
				return targetRtc;
			}
			// Profileを保存
			private void saveRtcProfile(IProject project, String genTime) {
				ProfileHandler handler = new ProfileHandler();
				try {
					ExportCreator export = new ExportCreator();
					export.preExport(editor);
					//
					////FSM
					if(editor.getRtcParam().getFsmParam()!=null) {
						String fsmName = editor.getRtcParam().getName() + "FSM.scxml";
						IFile fsmFile  = project.getFile(fsmName);
						if(editor.getRtcParam().getFsmContents().trim().length()==0) {
							try {
								fsmFile.delete(true, null);
							} catch (CoreException e) {
								e.printStackTrace();
							}
						} else {
							if(fsmFile.exists()==false) {
								try {
									fsmFile.create(null, true, null);
								} catch (CoreException e) {
									e.printStackTrace();
								}
							}
							String strPath = fsmFile.getLocation().toOSString();
							String xmlSplit[] = editor.getRtcParam().getFsmContents().split("\n");
							try {
								BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(strPath), "UTF-8"));
								for (String s : xmlSplit) {
									writer.write(s);
									writer.newLine();
								}
								writer.close();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
					//
					String strXml = handler.convert2XML(editor.getGeneratorParam());

					IFile orgRtcxml = project.getFile(IRtcBuilderConstants.DEFAULT_RTC_XML);
					if (orgRtcxml.exists()) {
						IFile renameFile = project.getFile(IRtcBuilderConstants.DEFAULT_RTC_XML + genTime);
						orgRtcxml.move(renameFile.getFullPath(), true, null);
						//バックアップ最大数以上のファイルは削除
						FileUtil.removeBackupFiles(project.getLocation().toOSString(), IRtcBuilderConstants.DEFAULT_RTC_XML);
					}
					IFile saveRtcxml = project.getFile(IRtcBuilderConstants.DEFAULT_RTC_XML);
					saveRtcxml.create(new ByteArrayInputStream(strXml.getBytes("UTF-8")), true, null);
					//
					editor.getRtcParam().resetUpdated();
					editor.updateDirty();
				} catch (Exception e) {
					LOGGER.error("Fail to save rtc-profile", e);
				}
			}

			private IProject checkTargetProject(String targetProject, boolean isConfirmNew) {
				if( targetProject==null || "".equals(targetProject) ){
					MessageDialog.openError(getSite().getShell(), "Error", IRTCBMessageConstants.VALIDATE_ERROR_OUTPUTPROJECT);
					return null;
				}
				IWorkspaceRoot workspaceHandle = ResourcesPlugin.getWorkspace().getRoot();
				IProject project = workspaceHandle.getProject(targetProject);
				if(!project.exists()) {
					if(isConfirmNew) {
						IWorkbench workbench = PlatformUI.getWorkbench();
						IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
						Shell shell = window.getShell();
						MessageBox message = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						message.setText(IRTCBMessageConstants.CONFIRM_PROJECT_GENERATE_TITLE);
						message.setMessage(IRTCBMessageConstants.CONFIRM_PROJECT_GENERATE);
						if( message.open() != SWT.YES) return null;
					}
					try {
						project.create(null);
						project.open(null);
						LanguageProperty langProp = LanguageProperty.checkPlugin(editor.getRtcParam());
						if(langProp != null) {
							IProjectDescription description = project.getDescription();
							String[] ids = description.getNatureIds();
							String[] newIds = new String[ids.length + langProp.getNatures().size()];
							System.arraycopy(ids, 0, newIds, 0, ids.length);
							for( int intIdx=0; intIdx<langProp.getNatures().size(); intIdx++ ) {
								newIds[ids.length+intIdx] = langProp.getNatures().get(intIdx);
							}
							description.setNatureIds(newIds);
							project.setDescription(description, null);
						}
					} catch (CoreException ex) {
						throw new RuntimeException(IRTCBMessageConstants.ERROR_GENERATE_FAILED);
					}
				}
				return project;
			}
		});
	}

	private void setPrefixSuffix(RtcParam param) {
		IPreferenceStore store = RtcBuilderPlugin.getDefault().getPreferenceStore();
		param.setCommonPrefix(ComponentPreferenceManager.getInstance().getBasic_Prefix());
		param.setCommonSuffix(store.getString(ComponentPreferenceManager.Generate_Basic_Suffix));
		param.setConfigurationPrefix(store.getString(ComponentPreferenceManager.Generate_Configuration_Prefix));
		param.setConfigurationSuffix(store.getString(ComponentPreferenceManager.Generate_Configuration_Suffix));
		//
		param.setDataPortPrefix(store.getString(ComponentPreferenceManager.Generate_DataPort_Prefix));
		param.setDataPortSuffix(store.getString(ComponentPreferenceManager.Generate_DataPort_Suffix));
		param.setServicePortPrefix(store.getString(ComponentPreferenceManager.Generate_ServicePort_Prefix));
		param.setServicePortSuffix(store.getString(ComponentPreferenceManager.Generate_ServicePort_Suffix));
		param.setServiceIFPrefix(store.getString(ComponentPreferenceManager.Generate_ServiceIF_Prefix));
		param.setServiceIFSuffix(store.getString(ComponentPreferenceManager.Generate_ServiceIF_Suffix));
		param.setEventPortPrefix(store.getString(ComponentPreferenceManager.Generate_EventPort_Prefix));
		param.setEventPortSuffix(store.getString(ComponentPreferenceManager.Generate_EventPort_Suffix));
	}

	private ImportExtension getTargetImportExtension() {
		List list = RtcBuilderPlugin.getDefault().getImportExtensionLoader().getList();
		if( list != null ) {
			String targetLang = editor.getRtcParam().getLanguage();
			for( Iterator iter = list.iterator(); iter.hasNext(); ) {
				ImportExtension extension = (ImportExtension) iter.next();
				if( extension.getManagerKey().equals(targetLang) ){
					return extension;
				}
			}
		}
		return null;
	}

	private void createCodeRestoreSection(FormToolkit toolkit, ScrolledForm form) {
		codeRestoreSection = createSectionBaseWithLabel(toolkit, form,
				Messages.getString("IMC.BASIC_CODE_RESTORE_TITLE"), Messages.getString("IMC.BASIC_CODE_RESTORE_EXPL"), 2);
		createCodeRestoreButton(toolkit);
	}
	private void createCodeRestoreButton(FormToolkit toolkit) {
		codeRestoreButton = toolkit.createButton(codeRestoreSection,
				Messages.getString("IMC.BASIC_BTN_CODE_RESTORE"), SWT.NONE);
		codeRestoreButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				RestoreDialog dialog = new RestoreDialog(getSite().getShell());
				dialog.setTargetProject(editor.getRtcParam().getOutputProject());
				int ret = dialog.open();
				if(ret != IDialogConstants.OK_ID) return;
				
				String targetTimeStamp = dialog.getTimeStamp();
				IWorkspaceRoot workspaceHandle = ResourcesPlugin.getWorkspace().getRoot();
				IProject project = workspaceHandle.getProject(editor.getRtcParam().getOutputProject());
				File dir = new File(project.getLocation().toOSString());
				List<File> targetList = parseDirectory(dir, targetTimeStamp);
				boolean isRestore = false;
				String currentTime = DATE_FORMAT.format(new GregorianCalendar().getTime());
				for(File each : targetList) {
					String currentFileStr = each.getPath().substring(0, each.getPath().length() - 14);
					File currentFile = new File(currentFileStr);
					if(currentFile.exists() == false) continue;
					
					String currentContents = readFileContents(currentFile);
					String targetContents = readFileContents(each);
					if(currentContents.equals(targetContents)) continue;
					
					CompareTarget cmpTarget = new CompareTarget();
					cmpTarget.setTargetName(currentFile.getPath());
					cmpTarget.setOriginalSrc(currentContents);
					cmpTarget.setGenerateSrc(targetContents);
					cmpTarget.setCanMerge(false);
					
					CompareResultDialog cmpDialog = new CompareResultDialog(getSite().getShell(),
																			cmpTarget,
																			CompareResultDialog.MODE_RESTORE,
																			"Restore Target");
					int cmpRet = cmpDialog.open();
					if(cmpRet == MergeHandler.PROCESS_GENERATE_ID) {
						isRestore = true;
						//TODO ファイルのバックアップ，コピー
						File renameFileBk = new File(currentFile.getPath());
						File renameFileCur = new File(currentFile.getPath() + currentTime);
						currentFile.renameTo(renameFileCur);
						each.renameTo(renameFileBk);
					}
				}
				if(isRestore) {
					//RTC.xmlのバックアップ
					IFile orgRtcxml = project.getFile(IRtcBuilderConstants.DEFAULT_RTC_XML);
					IFile bkRtcxml = project.getFile(IRtcBuilderConstants.DEFAULT_RTC_XML + targetTimeStamp);

					IFile renameFileBk = project.getFile(IRtcBuilderConstants.DEFAULT_RTC_XML);
					IFile renameFileCur = project.getFile(IRtcBuilderConstants.DEFAULT_RTC_XML + currentTime);
					
					try {
						orgRtcxml.move(renameFileCur.getFullPath(), true, null);
						bkRtcxml.move(renameFileBk.getFullPath(), true, null);
					} catch (CoreException e1) {
					}
					
					MessageDialog.openInformation(getSite().getShell(), "Information", "Restore success.");
				}
				try {
					project.refreshLocal(IResource.DEPTH_INFINITE, null);
				} catch (CoreException e1) {
				}
			}
		});
	}
	
	private String readFileContents(File target) {
		StringBuilder builder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(target))) {
			String text;
			while ((text = br.readLine()) != null) {
				builder.append(text).append(System.getProperty("line.separator"));
			}
		} catch (IOException e) {
		}		
		return builder.toString();
	}
	
	private List<File> parseDirectory(File targetDir, String timeStamp) {
		List<File> result = new ArrayList<File>();

		File[] files = targetDir.listFiles();
		for(File target : files) {
			if(target.isDirectory()) {
				result.addAll(parseDirectory(target, timeStamp));
			} else {
				if(target.getName().endsWith(timeStamp)) {
					result.add(target);
				}
			}
		}

		return result;
	}

	private void createExportImportSection(FormToolkit toolkit, ScrolledForm form) {
		profileSection = createSectionBaseWithLabel(toolkit, form,
				Messages.getString("IMC.BASIC_EXPORT_IMPORT_TITLE"), Messages.getString("IMC.BASIC_EXPORT_IMPORT_EXPL"), 2);
		createProfileLoadButton(toolkit);
		createProfileSaveButton(toolkit);
	}

	private void createProfileSaveButton(FormToolkit toolkit) {
		profileSaveButton = toolkit.createButton(profileSection,
				Messages.getString("IMC.BASIC_BTN_EXPORT"), SWT.NONE);
		profileSaveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				editor.allUpdates();
				String validateRtcParam = editor.validateParam();
				if (validateRtcParam != null) {
					MessageDialog.openError(getSite().getShell(), "Error", validateRtcParam);
					return;
				}

				String selectedFileName;
        		ExportCreator export = new ExportCreator();
        		if(!export.canCreateProfileName(editor)) {
        			FileDialog dialog = new FileDialog(getSite().getShell(),SWT.SAVE);
    		        dialog.setText(Messages.getString("IMC.BASIC_BTN_EXPORT"));
    				dialog.setFilterNames(new String[] {IMessageConstants.FILETYPE_XML,IMessageConstants.FILETYPE_YAML});
    				dialog.setFilterExtensions(new String[] { "*.xml","*.yaml" });
    				selectedFileName = dialog.open();
        		} else {
        			selectedFileName = export.createProfileName(editor);
        		}

		        if (selectedFileName != null) {
		        	try {
		        		export.preExport(editor);

		            	if (getFileExtension(selectedFileName).equals(IRtcBuilderConstants.YAML_EXTENSION)) {
		            		ProfileHandler handler = new ProfileHandler();
		            		handler.createYaml(selectedFileName, editor.getGeneratorParam());
		            	} else {
		            		ProfileHandler handler = new ProfileHandler();
		            		try {
		        				handler.validateXml(handler.convert2XML(editor.getGeneratorParam()));
		        			} catch (JAXBException ex) {
		            			if (!MessageDialog.openQuestion(getSite().getShell(),ex.getMessage(),
		            					IMessageConstants.PROFILE_VALIDATE_ERROR_MESSAGE + System.getProperty("line.separator") + ex.getCause().toString()) )
		            				return ;// 「いいえ」のときは保存しない
		            		}// 通常のExceptionは外側でcatchする
		        			handler.storeToXML(selectedFileName, editor.getGeneratorParam());
		            	}
		            	//FSM
		        		PropertyParam fsm = editor.getRtcParam().getProperty(IRtcBuilderConstants.PROP_TYPE_FSM);
		        		if(fsm!=null) {
			        		if(Boolean.valueOf(fsm.getValue())) {
			    				String cmpName = editor.getRtcParam().getName() + "FSM.scxml";
			    				IWorkspace workspace = ResourcesPlugin.getWorkspace();
			    				IWorkspaceRoot root = workspace.getRoot();
			    				IProject project = root.getProject(editor.getRtcParam().getOutputProject());
			    				String fsmFile  = project.getFile(cmpName).getLocation().toOSString();
			        			if(fsmFile!=null) {
			        				String dirName = new File(selectedFileName).getParent();
			        				String targetFile = dirName + File.separator + cmpName;
			        				Path inputPath = FileSystems.getDefault().getPath(fsmFile);
			        				Path outputPath = FileSystems.getDefault().getPath(targetFile);			        				
			        				Files.copy(inputPath, outputPath);
			        			}
			        		}
		        		}
		        		export.postExport(selectedFileName, editor);
		        		editor.getRtcParam().resetUpdated();
		        		editor.updateDirty();

					} catch (Exception e1) {
						String msg = e1.getMessage();
						if (msg == null || msg.equals("")) {
							msg = Messages.getString("IMC.BASIC_EXPORT_ERROR");
						}
						MessageDialog.openError(getSite().getShell(), "Error", msg);
						return;
					}
					MessageDialog.openInformation(getSite().getShell(), "Finish",
							Messages.getString("IMC.BASIC_EXPORT_DONE"));
		        }
			}
		});
	}

	private void createProfileLoadButton(FormToolkit toolkit) {
		profileLoadButton = toolkit.createButton(profileSection,
				Messages.getString("IMC.BASIC_BTN_IMPORT"), SWT.NONE);
		profileLoadButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
					ImportExtension extension = getTargetImportExtension();
				FileDialog dialog = new FileDialog(getSite().getShell(),SWT.OPEN);
		        dialog.setText(Messages.getString("IMC.BASIC_BTN_IMPORT"));

				String[] names = extension == null ? new String[] { IMessageConstants.FILETYPE_XML,IMessageConstants.FILETYPE_YAML }
				  					: extension.getFileDialogFilterNames();
				String[] exts = extension == null	 ? new String[] { "*.xml","*.yaml" }
				 					: extension.getFileDialogFilterExtensions();
				dialog.setFilterNames(names);
				dialog.setFilterExtensions(exts);

				String selectedFileName = dialog.open();
		        if (selectedFileName != null) {
		        	if(extension == null) {
			        	try {
			        		String origProject = editor.getRtcParam().getOutputProject();
			        		ProfileHandler handler = new ProfileHandler();
				        	if (getFileExtension(selectedFileName).equals(IRtcBuilderConstants.YAML_EXTENSION)) {
				        		GeneratorParam genParam = handler.readYaml(selectedFileName);
								String xmlFile = handler.convert2XML(genParam);
								editor.setGeneratorParam(genParam);
								editor.getRtcParam().setRtcXml(xmlFile);
				        	} else {
								GeneratorParam genParam = handler.restorefromXMLFile(selectedFileName);
								editor.setGeneratorParam(genParam);
							}
							editor.getRtcParam().setOutputProject(origProject);
						} catch (Exception e1) {
							MessageDialog.openError(getSite().getShell(), "Error",
									Messages.getString("IMC.BASIC_IMPORT_ERROR"));
							return;
						}
		        	} else {
			        	try {
		        			extension.setImportData(selectedFileName,editor);
						} catch (Exception e1) {
							String msg = e1.getMessage();
							if (msg == null || msg.equals("")) {
								msg = Messages.getString("IMC.BASIC_IMPORT_ERROR");
							}
							MessageDialog.openError(getSite().getShell(), "Error", msg);
							return;
						}
		        	}
		        	//FSM
	        		PropertyParam fsm = editor.getRtcParam().getProperty(IRtcBuilderConstants.PROP_TYPE_FSM);
	        		if(fsm!=null) {
		        		if(Boolean.valueOf(fsm.getValue())) {
		    				FileDialog dialogFSM = new FileDialog(getSite().getShell(),SWT.OPEN);
		    				dialogFSM.setText("FSM Import");
		    		        
		    				String[] namesFSM = new String[] { Messages.getString("IMC.FILE_SCXML"), Messages.getString("IMC.FILE_XML") };
		    				String[] extsFSM = new String[] { "*.scxml","*.xml" };
		    				dialogFSM.setFilterNames(namesFSM);
		    				dialogFSM.setFilterExtensions(extsFSM);
		    				String selectedFileNameFSM = dialogFSM.open();
		    				if(selectedFileNameFSM == null) {
		    					fsm.setValue("false");
		    					editor.getRtcParam().deleteFSMPort();
		    					
		    				} else {
			    				String cmpName = editor.getRtcParam().getName() + "FSM.scxml";
			    				IWorkspace workspace = ResourcesPlugin.getWorkspace();
			    				IWorkspaceRoot root = workspace.getRoot();
			    				IProject project = root.getProject(editor.getRtcParam().getOutputProject());
			    				String fsmFile  = project.getLocation().toOSString() + File.separator + cmpName;
		        				try {
			        				Path inputPath = FileSystems.getDefault().getPath(selectedFileNameFSM);
			        				Path outputPath = FileSystems.getDefault().getPath(fsmFile);			        				
									Files.copy(inputPath, outputPath);
									
									project.refreshLocal(IResource.DEPTH_INFINITE, null);
									//インポートしたファイルの読み込み
									ScXMLHandler scHandler = new ScXMLHandler();
									StringBuffer buffer = new StringBuffer();
									StateParam rootState = scHandler.parseSCXML(outputPath.toAbsolutePath().toString(), buffer);
									if(rootState!=null) {
										editor.getRtcParam().setFsmParam(rootState);
										editor.getRtcParam().setFsmContents(buffer.toString());
										editor.getRtcParam().parseEvent();
									}
								} catch (IOException e1) {
									e1.printStackTrace();
								} catch (CoreException e1) {
									e1.printStackTrace();
								}
		    				}
		        		}
	        		}
	        		
					MessageDialog.openInformation(getSite().getShell(), "Finish",
							Messages.getString("IMC.BASIC_IMPORT_DONE"));
					//
					editor.allPagesReLoad();
					editor.updateEMFModuleName(editor.getRtcParam().getName());
					editor.updateEMFDataPorts(
							editor.getRtcParam().getInports(), editor.getRtcParam().getOutports(),
							editor.getRtcParam().getEventports(), editor.getRtcParam().getServicePorts());
					editor.setEnabledInfoByLang();
					extractDataTypes();
					load();
					//
//					editor.getRtcParam().resetUpdated();
					editor.updateDirty();
		        }
			}
		});
	}

	protected void update() {
		RtcParam rtcParam = editor.getRtcParam();

		rtcParam.setName(getText(nameText.getText()));
		rtcParam.setDescription(getText(descriptionText.getText()));
		rtcParam.setVersion(getText(versionText.getText()));
		rtcParam.setVender(getText(venderText.getText()));
		rtcParam.setCategory(getText(categoryCombo.getText()));
		rtcParam.setActivityType(getText(activityTypeCombo.getText()));
		rtcParam.setComponentType(getText(typeCombo.getText()));
		if(choreonoidBtn.getSelection()) {
			rtcParam.setComponentKind(KIND_CHOREONOID);
		} else {
			rtcParam.setComponentKind("DataFlowComponent");
		}
		rtcParam.setAbstract(getText(abstractText.getText()));
		rtcParam.setRtcType(getText(rtcTypeText.getText()));

		rtcParam.setChoreonoid(choreonoidBtn.getSelection());

		try {
			int maxInstance = Integer.parseInt(getText(maxInstanceText
					.getText()));
			rtcParam.setMaxInstance(maxInstance);
		} catch (Exception e) {
		}

		rtcParam.setExecutionType(getText(executionTypeCombo.getText()));
		try {
			double exec_rate = Double.parseDouble(getText(executionRateText
					.getText()));
			rtcParam.setExecutionRate(exec_rate);
		} catch (Exception e) {
		}
		// 以下、cppRadioが有効な場合のみ実行する
		// →この画面が表示される前にこの処理が呼ばれた場合は、なにもしない
		// ∵rtcParamが画面に反映される前にクリアしてしまうとまずいため
		List<String> langList = new ArrayList<String>();
		List<String> langArgList = new ArrayList<String>();
		String rtmVersion = null;

		if (cppRadio.getSelection()) {
			langList.add(IRtcBuilderConstants.LANG_CPP);
			langArgList.add(IRtcBuilderConstants.LANG_CPP_ARG);
			rtmVersion = IRtcBuilderConstants.DEFAULT_RTM_VERSION;
		}
		if (buttonList != null) {
			for (Button extButton : buttonList) {
				if (!extButton.getSelection()) {
					continue;
				}
				for (GenerateManager manager : managerList) {
					if (!extButton.getText().trim().equals(
							manager.getManagerKey())) {
						continue;
					}
					langList.add(manager.getManagerKey());
					langArgList.add(manager.getManagerKey());
					rtmVersion = manager.getTargetVersion();
					break;
				}
				break;
			}
		}
		if (!rtcParam.getLangList().equals(langList)) {
			rtcParam.getLangList().clear();
			rtcParam.getLangList().addAll(langList);
			rtcParam.getLangArgList().clear();
			rtcParam.getLangArgList().addAll(langArgList);
			rtcParam.setRtmVersion(rtmVersion);
		}

		editor.updateEMFModuleName(getText(nameText.getText()));
		editor.updateDirty();
	}

	/**
	 * データをロードする
	 */
	public void load() {
		RtcParam rtcParam = editor.getRtcParam();

		nameText.setText(getValue(rtcParam.getName()));
		descriptionText.setText(getValue(rtcParam.getDescription()));
		versionText.setText(getValue(rtcParam.getVersion()));
		venderText.setText(getValue(rtcParam.getVender()));
		categoryCombo.setText(getValue(rtcParam.getCategory()));
		if( rtcParam.getComponentKind().contains(KIND_CHOREONOID) ) {
			choreonoidBtn.setSelection(true);
		}
		activityTypeCombo.setText(getValue(rtcParam.getActivityType()));
		typeCombo.setText(getValue(rtcParam.getComponentType()));
		maxInstanceText.setText(getValue(String.valueOf(rtcParam.getMaxInstance())));
		executionTypeCombo.setText(getValue(rtcParam.getExecutionType()));
		executionRateText.setText(getValue(String.valueOf(rtcParam.getExecutionRate())));
		abstractText.setText(getValue(rtcParam.getAbstract()));
		rtcTypeText.setText(getValue(rtcParam.getRtcType()));
		//
		editor.updateEMFModuleName(rtcParam.getName());
		//
		if (rtcParam.getLangList().contains(IRtcBuilderConstants.LANG_CPP)
				|| rtcParam.getLangList().contains(
						IRtcBuilderConstants.LANG_CPPWIN)) {
			cppRadio.setSelection(true);
		} else {
			// rtcParam.getLangList()に含まれない場合は選択解除
			cppRadio.setSelection(false);
		}
		if (buttonList != null) {
			for (Button chkButton : buttonList) {
				if (rtcParam.getLangList().contains(chkButton.getText().trim())) {
					chkButton.setSelection(true);
				} else {
					// rtcParam.getLangList()に含まれない場合は選択解除
					chkButton.setSelection(false);
				}
			}
		}
	}

	/**
	 * 入力したカテゴリを永続情報に設定する
	 */
	protected void addDefaultComboValue(){
		String value = categoryCombo.getText(); // local
		List<String> defaultList = Arrays.asList(defaultCategory);
		if(defaultList.contains(value)) return;
		
		String storedString = RtcBuilderPlugin.getDefault().getPreferenceStore().getString(CATEGORY_INDEX_KEY);
		String[] savedValues = storedString.split(",");
		LinkedList<String> storedList = new LinkedList<String>(Arrays.asList(savedValues));
		
		StringBuilder newString = new StringBuilder();
		if (storedList.contains(value)) {
			storedList.remove(storedList.indexOf(value));
			
			newString.append(value);
			for(int index=0; index<storedList.size(); index++) {
				newString.append(",").append(storedList.get(index));
			}
		} else {
			if ("".equals(storedString)) {
				newString.append(value);
			} else {
				newString.append(value).append(",").append(storedString);
			}
		}
		RtcBuilderPlugin.getDefault().getPreferenceStore().setValue(CATEGORY_INDEX_KEY, newString.toString());
		categoryCombo.add(value);
	}

	@Override
	protected Text createLabelAndText(FormToolkit toolkit, Composite composite, String labelString) {
		Text text = super.createLabelAndText(toolkit, composite, labelString);
		GridData gd = (GridData)text.getLayoutData();
		gd.widthHint = 100;
		return text;
	}

	@Override
	protected Combo createEditableCombo(FormToolkit toolkit, Composite composite, String labelString, String key, String[] defaultValue, int color, int hspan) {
		Combo combo = super.createEditableCombo(toolkit, composite, labelString, key, defaultValue, color, hspan);
		GridData gd = (GridData)combo.getLayoutData();
		gd.widthHint = 100;
		return combo;
	}

	/**
	 * BasicInfoフォーム内の要素の有効/無効を設定します。
	 * <ul>
	 * <li>basic.info.moduleName : BasicInfoセクションの Module name</li>
	 * <li>basic.info.moduleDescription : BasicInfoセクションの Module description</li>
	 * <li>basic.info.moduleVersion : BasicInfoセクションの Module version</li>
	 * <li>basic.info.moduleVendor : BasicInfoセクションの Module vendor</li>
	 * <li>basic.info.moduleCategory : BasicInfoセクションの Module category</li>
	 * <li>basic.info.componentType : BasicInfoセクションの Component type</li>
	 * <li>basic.info.activityType : BasicInfoセクションの Component's activity type</li>
	 * <li>basic.info.dataFlow : BasicInfoセクションの Component kind の DataFlow</li>
	 * <li>basic.info.fsm : BasicInfoセクションの Component kind の FSM</li>
	 * <li>basic.info.multiMode : BasicInfoセクションの Component kind の MultiMode</li>
	 * <li>basic.info.maxInstances : BasicInfoセクションの maximum instances</li>
	 * <li>basic.info.executionType : BasicInfoセクションの Execution type</li>
	 * <li>basic.info.executionRate : BasicInfoセクションの Execution rate</li>
	 * <li>basic.info.abstract : BasicInfoセクションの Abstract</li>
	 * <li>basic.info.rtcType : BasicInfoセクションの RTC type</li>
	 * <li>basic.outputProject.* : OutputProjectセクション全体</li>
	 * <li>basic.generate.code : Generateセクションのコード生成ボタン</li>
	 * <li>basic.generate.package : Generateセクションのパッケージ化ボタン</li>
	 * <li>basic.profile.import : Profileセクションのインポートボタン</li>
	 * <li>basic.profile.export : Profileセクションのエクスポートボタン</li>
	 * </ul>
	 */
	public void setEnabledInfo(WidgetInfo widgetInfo, boolean enabled) {
		if (widgetInfo.matchSection("info")) {
			if (nameText != null) {
				if (widgetInfo.matchWidget("moduleName"))        setControlEnabled(nameText, enabled);
				if (widgetInfo.matchWidget("moduleDescription")) setControlEnabled(descriptionText, enabled);
				if (widgetInfo.matchWidget("moduleVersion"))     setControlEnabled(versionText, enabled);
				if (widgetInfo.matchWidget("moduleVendor"))      setControlEnabled(venderText, enabled);
				if (widgetInfo.matchWidget("moduleCategory"))    setControlEnabled(categoryCombo, enabled);
				if (widgetInfo.matchWidget("componentType"))     setControlEnabled(typeCombo, enabled);
				if (widgetInfo.matchWidget("activityType"))      setControlEnabled(activityTypeCombo, enabled);
				if (widgetInfo.matchWidget("maxInstances"))      setControlEnabled(maxInstanceText, enabled);
				if (widgetInfo.matchWidget("executionType"))     setControlEnabled(executionTypeCombo, enabled);
				if (widgetInfo.matchWidget("executionRate"))     setControlEnabled(executionRateText, enabled);
				if (widgetInfo.matchWidget("abstract"))          setControlEnabled(abstractText, enabled);
				if (widgetInfo.matchWidget("rtcType"))           setControlEnabled(rtcTypeText, enabled);
			}
		}
		if (widgetInfo.matchSection("outputProject")) {
			if (outputProjectSection != null) {
				outputProjectSection.setEnabled(enabled);
				setEnableBackground(outputProjectSection, getBackgroundByEnabled(enabled));
			}
		}
		if (widgetInfo.matchSection("generate")) {
			if (generateSection != null) {
				if (widgetInfo.matchWidget("code"))    setButtonEnabled(generateButton, enabled);
				boolean genEnable = false;
				if (generateButton.getEnabled()) {
					genEnable = true;
				}
				generateSection.setEnabled(genEnable);
				setEnableBackground(generateSection, getBackgroundByEnabled(genEnable));
			}
		}
		if (widgetInfo.matchSection("profile")) {
			if (profileSection != null) {
				if (widgetInfo.matchWidget("import")) setButtonEnabled(profileLoadButton, enabled);
				if (widgetInfo.matchWidget("export")) setButtonEnabled(profileSaveButton, enabled);
				boolean profEnable = false;
				if (profileLoadButton.getEnabled() || profileSaveButton.getEnabled()) {
					profEnable = true;
				}
				profileSection.setEnabled(profEnable);
				setEnableBackground(profileSection,	getBackgroundByEnabled(profEnable));
			}
		}
	}
	
	private org.eclipse.swt.events.SelectionAdapter createLanguageRadioListner(){
		return new org.eclipse.swt.events.SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				// eventからボタン名称を取得
				String btnName = ((Button)e.widget).getText();
				// 選択言語による活性状態の制御
				editor.setEnabledInfoByLang(btnName);
			}
		};
	}
}
