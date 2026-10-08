package jp.go.aist.rtm.rtcbuilder.ros.ui.editors;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.iso.iso22166.part202.profile.SIM;
import org.openrtp.namespaces.ros.version01.RosProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.ac.meijo_u.iso22166_part202.util.ISO2ROSProfileHandler;
import jp.ac.meijo_u.iso22166_part202.util.ROS2ISOProfileHandler;
import jp.go.aist.rtm.rtcbuilder.Generator.MergeHandler;
import jp.go.aist.rtm.rtcbuilder.GuiRtcBuilder;
import jp.go.aist.rtm.rtcbuilder.IRTCBMessageConstants;
import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.RtcBuilderPlugin;
import jp.go.aist.rtm.rtcbuilder.factory.ExportCreator;
import jp.go.aist.rtm.rtcbuilder.generator.param.GeneratorParam;
import jp.go.aist.rtm.rtcbuilder.manager.GenerateManager;
import jp.go.aist.rtm.rtcbuilder.nl.Messages;
import jp.go.aist.rtm.rtcbuilder.ros.IRtcBuilderConstantsROS;
import jp.go.aist.rtm.rtcbuilder.ros.ProfileHandlerROS;
import jp.go.aist.rtm.rtcbuilder.ros.manager.CXXGenerateManager;
import jp.go.aist.rtm.rtcbuilder.ros.manager.CommonGenerateManager;
import jp.go.aist.rtm.rtcbuilder.ros.manager.ContainerGenerateManager;
import jp.go.aist.rtm.rtcbuilder.ros.param.PackageParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.ROSParam;
import jp.go.aist.rtm.rtcbuilder.ui.Perspective.LanguageProperty;
import jp.go.aist.rtm.rtcbuilder.ui.compare.CompareResultDialog;
import jp.go.aist.rtm.rtcbuilder.ui.compare.CompareTarget;
import jp.go.aist.rtm.rtcbuilder.ui.dialog.ExportDialog;
import jp.go.aist.rtm.rtcbuilder.ui.dialog.ImportDialog;
import jp.go.aist.rtm.rtcbuilder.ui.dialog.RestoreDialog;
import jp.go.aist.rtm.rtcbuilder.ui.editors.AbstractEditorFormPage;
import jp.go.aist.rtm.rtcbuilder.ui.editors.IMessageConstants;
import jp.go.aist.rtm.rtcbuilder.util.FileUtil;
import jp.go.aist.rtm.toolscommon.profiles.util.XmlHandlerROS;

/**
 * Basic Profile 設定ページ
 */
public class ROSBasicEditorFormPage extends AbstractEditorFormPage {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ROSBasicEditorFormPage.class);

	private Text packageText;
	private Text nodeText;
	private Text classText;
	private Text descriptionText;
	private Text versionText;
	private Text maintainerText;
	private Combo categoryCombo;

	private Combo licenseCombo;
	private Text contactText;
	
	private TableViewer packageListViewer;
	private Button addPackageButton;	
	private Button deletePackageButton;
	private List<PackageParam> packageList = new ArrayList<PackageParam>();

	private List<GenerateManager> managerList = null;
	private Group LangGroup;
	private Button cppRadio;
	private List<Button> buttonList = new ArrayList<Button>();
	
	private Button generateButton;
	private Button codeRestoreButton;
	private Button backupClearButton;

	private Button profileLoadButton;
	private Button profileSaveButton;

	private Button profileGenerateButton;

	private Composite generateSection;
	private Composite codeRestoreSection;
	private Composite isoProfileSection;
	private Composite profileSection;
	
	/**
	 * コンストラクタ
	 *
	 * @param editor
	 *            親のエディタ
	 */
	public ROSBasicEditorFormPage(ROSBuilderEditor editor) {
		super(editor, "id", Messages.getString("IMC.BASIC_SECTION"));
	}

	/**
	 * {@inheritDoc}
	 */
	protected void createFormContent(final IManagedForm managedForm) {
		ScrolledForm form = super.createBase(managedForm, Messages.getString("IMC.ROS_BASIC_NODE_TITLE"));
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
		createSpecificSection(toolkit, form);
		createPackageSection(toolkit, form);
		
		createLanguageSection(toolkit, form);
		createGenerateSection(toolkit, form);
		createCodeRestoreSection(toolkit, form);
		createIsoProfileSection(toolkit, form);
		createExportImportSection(toolkit, form);
		//
		managerList = RtcBuilderPlugin.getDefault().getLoader()
				.getManagerList(IRtcBuilderConstants.MIDDLEWARE_ROS);
		if (managerList != null) {
			for (String key : RtcBuilderPlugin.getDefault().getLoader()
					.getManagerKeyList(IRtcBuilderConstants.MIDDLEWARE_ROS)) {
				Button extRadio = createLanguageRadioButton(toolkit, LangGroup, key);
				extRadio.addSelectionListener(createLanguageRadioListner());
				buttonList.add(extRadio);
			}
		}
		//
		// 言語・環境ページより先にこのページが表示された場合、ここで言語を判断する
		editor.setEnabledInfoByLang();

		load();
	}

	private void switchPerspective() {
		ROSParam rosParam = editor.getGeneratorParam().getROSParam();
		//Pluginの存在確認
		LanguageProperty langProp = LanguageProperty.checkPlugin(rosParam);
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
		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();
		rosParam.convertInfo();
		load();
		return rosParam.validateBasicInfo();
	}

	private void createModuleSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createSectionBaseWithLabel(toolkit, form,
				Messages.getString("IMC.ROS_BASIC_NODE_TITLE"), Messages.getString("IMC.ROS_BASIC_NODE_EXPL"), 3);
		//
		packageText = createLabelAndText(toolkit, composite,
				IMessageConstants.REQUIRED + Messages.getString("IMC.ROS_BASIC_LBL_PACKAGENAME"), SWT.NONE, SWT.COLOR_RED, 2);
		nodeText = createLabelAndText(toolkit, composite,
				IMessageConstants.REQUIRED + Messages.getString("IMC.ROS_BASIC_LBL_NODENAME"), SWT.NONE, SWT.COLOR_RED, 2);
		classText = createLabelAndText(toolkit, composite,
				Messages.getString("IMC.ROS_BASIC_LBL_CLASSNAME"), SWT.NONE, SWT.COLOR_BLACK, 2);
		descriptionText = createLabelAndText(toolkit, composite, Messages.getString("IMC.BASIC_LBL_DESCRIPTION"), SWT.NONE, SWT.COLOR_BLACK, 2);
		versionText = createLabelAndText(toolkit, composite,
				IMessageConstants.REQUIRED + Messages.getString("IMC.BASIC_LBL_VERSION"), SWT.NONE, SWT.COLOR_RED, 2);
		maintainerText = createLabelAndText(toolkit, composite,
				IMessageConstants.REQUIRED + Messages.getString("IMC.ROS_BASIC_LBL_MAINTAINER"), SWT.NONE, SWT.COLOR_RED, 2);

		String[] categoryList = { "", "Robot", "Sensor", "Actuator", "Controller", "Planning", 
				 "Localization", "Mapping", "Navigation", "Perception", "Communication",
				 "Simulation", "Tool", "Other" };
		categoryCombo = createCombo(toolkit, composite,
				IMessageConstants.REQUIRED + Messages.getString("IMC.BASIC_LBL_CATEGORY"), categoryList, SWT.COLOR_RED, 2);
	}

	private void createHintSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createHintSectionBase(toolkit, form, 7);
		//
		createHintLabel(Messages.getString("IMC.ROS_BASIC_LBL_PACKAGENAME"), IMessageConstantsROS.BASIC_HINT_PACKCGENAME_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_BASIC_LBL_NODENAME"), IMessageConstantsROS.BASIC_HINT_NODENAME_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_BASIC_LBL_CLASSNAME"), IMessageConstantsROS.BASIC_HINT_CLASSNAME_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.BASIC_HINT_DESCRIPTION_TITLE"), IMessageConstantsROS.BASIC_HINT_DESCRIPTION_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.BASIC_HINT_VERSION_TITLE"), Messages.getString("IMC.ROS_BASIC_HINT_VERSION_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_BASIC_LBL_MAINTAINER"), Messages.getString("IMC.ROS_BASIC_HINT_MAINTAINER_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.BASIC_HINT_CATEGORY_TITLE"), IMessageConstantsROS.BASIC_HINT_CATEGORY_DESC, toolkit, composite);
		//
		createHintSpace(toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_BASIC_LBL_LICENSE"), IMessageConstantsROS.BASIC_HINT_LICENSE_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_BASIC_LBL_MAINTAINER_EMAIL"), Messages.getString("IMC.ROS_BASIC_HINT_MAINTAINER_MAIL_DESC"), toolkit, composite);
		createHintSpace(toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_PACKAGE_TITLE"), IMessageConstantsROS.BASIC_HINT_DEPENDENCY_DESC, toolkit, composite);
		//
		createHintSpace(toolkit, composite);
		createHintLabel(IMessageConstants.LANGUAGE_HINT_LANG_TITLE, IMessageConstantsROS.BASIC_HINT_LANGUAGE_DESC, toolkit, composite);
		//
		createHintSpace(toolkit, composite);
		createHintLabel(Messages.getString("IMC.BASIC_HINT_GENERATE_TITLE"), IMessageConstantsROS.BASIC_HINT_CODE_GEN_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.BASIC_HINT_CODE_RESTORE_TITLE"), Messages.getString("IMC.BASIC_HINT_CODE_RESTORE_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.BASIC_HINT_ISO_PROFILE_TITLE"), Messages.getString("IMC.BASIC_HINT_ISO_PROFILE_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.BASIC_HINT_IMPORT_TITLE"), Messages.getString("IMC.ROS_BASIC_HINT_IMPORT_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.BASIC_HINT_EXPORT_TITLE"), Messages.getString("IMC.ROS_BASIC_HINT_EXPORT_DESC"), toolkit, composite);
	}

	private void createSpecificSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createSectionBaseWithLabel(toolkit, form,
				Messages.getString("IMC.ROS_SPECIFIC_TITLE"), Messages.getString("IMC.ROS_SPECIFIC_EXPL"), 3);

		String[] licenseList = { IRtcBuilderConstantsROS.LICENSE_APACHE,
								 IRtcBuilderConstantsROS.LICENSE_MIT,
								 IRtcBuilderConstantsROS.LICENSE_BSD,
								 IRtcBuilderConstantsROS.LICENSE_GPL,
								 IRtcBuilderConstantsROS.LICENSE_LGPL,
								 IRtcBuilderConstantsROS.LICENSE_PROPRIETARY };
		licenseCombo = createCombo(toolkit, composite,
				Messages.getString("IMC.ROS_BASIC_LBL_LICENSE"), licenseList, SWT.COLOR_BLACK, 2);
		contactText = createLabelAndText(toolkit, composite,
				Messages.getString("IMC.ROS_BASIC_LBL_MAINTAINER_EMAIL"), SWT.NONE, SWT.COLOR_BLACK, 2);
	}
	
	private void createPackageSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createSectionBaseWithLabel(toolkit, form,
				Messages.getString("IMC.ROS_PACKAGE_TITLE"), Messages.getString("IMC.ROS_PACKAGE_EXPL"), 3);

		packageListViewer = new TableViewer(composite, SWT.FULL_SELECTION
				| SWT.SINGLE | SWT.BORDER);
		packageListViewer.setContentProvider(new ArrayContentProvider());
		Table packageListTable = packageListViewer.getTable();
		packageListTable.setLinesVisible(true);
		packageListTable.setHeaderVisible(true);
		GridData gd = new GridData();
		gd.heightHint = 150;
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		packageListTable.setLayoutData(gd);
		
		TableViewerColumn nameColumn = createColumn(packageListViewer, "Package Name", 200);
		nameColumn.setEditingSupport(new PackageNameCellModifier(packageListViewer, 0));
		TableViewerColumn versionColumn = createColumn(packageListViewer, "Version", 100);
		versionColumn.setEditingSupport(new PackageNameCellModifier(packageListViewer, 1));
		TableViewerColumn otherColumn = createColumn(packageListViewer, "Other", 150);
		otherColumn.setEditingSupport(new PackageNameCellModifier(packageListViewer, 2));

		packageListViewer.setLabelProvider(new PackageLabelProvider());
		packageListViewer.setInput(packageList);
		
		Composite libraryComposite = new Composite(composite, SWT.NONE);
		GridLayout gl = new GridLayout(1, false);
		gd = new GridData(GridData.FILL_VERTICAL);
		libraryComposite.setLayout(gl);
		libraryComposite.setLayoutData(gd);

		addPackageButton = new Button(libraryComposite, SWT.PUSH);
		addPackageButton.setText(Messages.getString("IPreferenceMessageConstants.CONFIG_BTN_ADD"));
		gd = new GridData();
		gd.widthHint = EXEC_BUTTON_WIDTH;
		addPackageButton.setLayoutData(gd);
		addPackageButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PackageParam elem = new PackageParam();
				elem.setName("new_package");
				packageList.add(elem);
				packageListViewer.refresh();
				update();
			}
		});
		
		deletePackageButton = new Button(libraryComposite, SWT.PUSH);
		deletePackageButton.setText(Messages.getString("IPreferenceMessageConstants.CONFIG_BTN_DELETE"));
		gd = new GridData();
		gd.widthHint = EXEC_BUTTON_WIDTH;
		deletePackageButton.setLayoutData(gd);
		deletePackageButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectionIndex = packageListViewer.getTable().getSelectionIndex();
				if (0 <= selectionIndex
						&& selectionIndex + 1 <= ((List) packageListViewer.getInput()).size()) {
					packageList.remove(selectionIndex);
					packageListViewer.refresh();
					update();
				}
			}
		});
	}

	private void createLanguageSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createSectionBaseWithLabel(toolkit, form,
				IMessageConstants.LANGUAGE_LANG_TITLE, IMessageConstants.LANGUAGE_LANG_EXPL, 2);
		//
		LangGroup = new Group(composite, SWT.NONE);
		LangGroup.setLayout(new GridLayout(5, false));
		GridData gd = new GridData();
		LangGroup.setLayoutData(gd);
		//
		cppRadio = createLanguageRadioButton(toolkit, LangGroup, "C++");
		cppRadio.addSelectionListener(createLanguageRadioListner());
	}
	
	private void createGenerateSection(FormToolkit toolkit, ScrolledForm form) {
		generateSection = createSectionBaseWithLabel(toolkit, form,
				Messages.getString("IMC.BASIC_GENERATE_TITLE"), Messages.getString("IMC.BASIC_GENERATE_EXPL"), 3);
		createGenerateButton(toolkit);
	}

	private void createGenerateButton(FormToolkit toolkit) {
		generateButton = toolkit.createButton(generateSection,
				Messages.getString("IMC.BASIC_BTN_GENERATE"), SWT.NONE);
		generateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				((ROSBuilderEditor)editor).allUpdates();
				String validateRtcParam = ((ROSBuilderEditor)editor).validateParam();
				if (validateRtcParam != null) {
					MessageDialog.openError(getSite().getShell(), "Error", validateRtcParam);
					return;
				}
				ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();
				List<String> warnings = rosParam.validateWarnings();
				if(0<warnings.size()) {
					StringBuilder builder = new StringBuilder();
					for(String each : warnings) {
						if(0 < builder.length()) {
							builder.append(System.getProperty("line.separator"));
						}
						builder.append(each);
					}
					builder.append(Messages.getString("IMC.VALIDATE_CAUTION_NAME_DUPLICATE"));
        			if (!MessageDialog.openQuestion(getSite().getShell(),
        											"Caution",
        											builder.toString()) )
        				return;
				}
				
				//対象プロジェクトの確認
				IProject project = checkTargetProject(((ROSBuilderEditor)editor).getROSParam().getOutputProject(), true);
				if( project==null) return;
				try {
					project.refreshLocal(IResource.DEPTH_INFINITE, null);
				} catch (CoreException e1) {
					throw new RuntimeException(IRTCBMessageConstants.ERROR_GENERATE_FAILED);
				}
				//
				rosParam.convertInfo();
				File dirMsg = new File(project.getLocation().toOSString() + File.separator + "msg");
				rosParam.getExtMsgFiles().clear();
				for(File each : FileUtil.listAllFiles(dirMsg)) {
					rosParam.getExtMsgFiles().add(each.getName());
				}
				File dirSrv = new File(project.getLocation().toOSString() + File.separator + "srv");
				rosParam.getExtSrvFiles().clear();
				for(File each : FileUtil.listAllFiles(dirSrv)) {
					rosParam.getExtSrvFiles().add(each.getName());
				}
				File dirAction = new File(project.getLocation().toOSString() + File.separator + "action");
				rosParam.getExtActionFiles().clear();
				for(File each : FileUtil.listAllFiles(dirAction)) {
					rosParam.getExtActionFiles().add(each.getName());
				}
				//
				GuiRtcBuilder rtcBuilder = new GuiRtcBuilder();
				rtcBuilder.clearGenerateManager();
				
				List<GenerateManager> managerList = RtcBuilderPlugin
						.getDefault().getLoader().getManagerList(IRtcBuilderConstants.MIDDLEWARE_ROS);
				if (managerList == null || rosParam.getLangList().contains(IRtcBuilderConstants.LANG_CPP)) {
					rtcBuilder.addGenerateManager(new CommonGenerateManager());
					rtcBuilder.addGenerateManager(new CXXGenerateManager());
				} else {
					for (GenerateManager manager : managerList) {
						if(rosParam.getLangList().contains(manager.getManagerKey())) {
							rtcBuilder.addGenerateManager(manager);
						}
					}
				}
				rtcBuilder.addGenerateManager(new ContainerGenerateManager());
				
				GeneratorParam generatorParam = editor.getGeneratorParam();
				String genTime = DATE_FORMAT.format(new GregorianCalendar().getTime());
				if (rtcBuilder.doGenerateWriteROS(generatorParam, true, genTime)) {
					LanguageProperty langProp = LanguageProperty.checkPlugin(((ROSBuilderEditor)editor).getROSParam());
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
					saveROSProfile(project, genTime);
					switchPerspective();
					((ROSBuilderEditor)editor).getROSParam().resetUpdated();
	        		editor.updateDirty();
					//
					try {
						project.refreshLocal(IResource.DEPTH_INFINITE, null);
					} catch (CoreException e1) {
						throw new RuntimeException(IRTCBMessageConstants.ERROR_GENERATE_FAILED);
					}
				}
			}

			// Profileを保存
			private void saveROSProfile(IProject project, String genTime) {
				ProfileHandlerROS handler = new ProfileHandlerROS();
				try {
					ExportCreator export = new ExportCreator();
					export.preExport(editor);
					//
					String strXml = handler.convert2ROSXML(editor.getGeneratorParam());

					IFile orgROSxml = project.getFile(IRtcBuilderConstantsROS.DEFAULT_ROS_XML);
					if (orgROSxml.exists()) {
						IFile renameFile = project.getFile(IRtcBuilderConstantsROS.DEFAULT_ROS_XML + genTime);
						orgROSxml.move(renameFile.getFullPath(), true, null);
						FileUtil.removeBackupFiles(project.getLocation().toOSString(), IRtcBuilderConstantsROS.DEFAULT_ROS_XML);
					}
					IFile saveROSxml = project.getFile(IRtcBuilderConstantsROS.DEFAULT_ROS_XML);
					saveROSxml.create(new ByteArrayInputStream(strXml.getBytes("UTF-8")), true, null);

					//ISO
					RosProfile rosProfile = handler.convert2XMLProfile(((ROSBuilderEditor)editor).getROSParam());
					ROS2ISOProfileHandler isoHandler = new ROS2ISOProfileHandler();
					SIM isoProfile = isoHandler.convertROS2Iso(rosProfile);
					String strIsoXml = isoHandler.convertToXmlIso(isoProfile);
//					
					IFile orgIsoxml = project.getFile(IRtcBuilderConstants.DEFAULT_ISO_202_XML);
					if (orgIsoxml.exists()) {
						IFile renameIsoFile = project.getFile(IRtcBuilderConstants.DEFAULT_ISO_202_XML + genTime);
						orgIsoxml.move(renameIsoFile.getFullPath(), true, null);
						FileUtil.removeBackupFiles(project.getLocation().toOSString(), IRtcBuilderConstants.DEFAULT_ISO_202_XML);
					}
					IFile saveIsoxml = project.getFile(IRtcBuilderConstants.DEFAULT_ISO_202_XML);
					saveIsoxml.create(new ByteArrayInputStream(strIsoXml.getBytes("UTF-8")), true, null);
					//
					((ROSBuilderEditor)editor).getROSParam().resetUpdated();
					editor.updateDirty();
				} catch (Exception e) {
					LOGGER.error("Fail to save rtc-profile", e);
				}
			}
		});
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
				LanguageProperty langProp = LanguageProperty.checkPlugin(((ROSBuilderEditor)editor).getROSParam());
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

	private void createIsoProfileSection(FormToolkit toolkit, ScrolledForm form) {
		isoProfileSection = createSectionBaseWithLabel(toolkit, form,
				Messages.getString("IMC.BASIC_ISO_PROFILE_TITLE"), Messages.getString("IMC.BASIC_ISO_PROFILE_EXPL"), 2);
		profileGenerateButton = toolkit.createButton(isoProfileSection,
				Messages.getString("IMC.BASIC_BTN_ISO_PROFILE_GENERATE"), SWT.NONE);
		profileGenerateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				((ROSBuilderEditor)editor).allUpdates();
				String validateROSParam = ((ROSBuilderEditor)editor).validateParam();
				if (validateROSParam != null) {
					MessageDialog.openError(getSite().getShell(), "Error", validateROSParam);
					return;
				}
				IProject project = checkTargetProject(((ROSBuilderEditor)editor).getROSParam().getOutputProject(), true);
				if( project==null) return;

				ProfileHandlerROS handler = new ProfileHandlerROS();
				try {
					RosProfile rosProfile = handler.convert2XMLProfile(((ROSBuilderEditor)editor).getROSParam());
					ROS2ISOProfileHandler isoHandler = new ROS2ISOProfileHandler();
					SIM isoProfile = isoHandler.convertROS2Iso(rosProfile);
					String strIsoXml = isoHandler.convertToXmlIso(isoProfile);
	
					IFile orgIsoxml = project.getFile(IRtcBuilderConstants.DEFAULT_ISO_202_XML);
					if (orgIsoxml.exists()) {
						String genTime = DATE_FORMAT.format(new GregorianCalendar().getTime());
						IFile renameIsoFile = project.getFile(IRtcBuilderConstants.DEFAULT_ISO_202_XML + genTime);
						orgIsoxml.move(renameIsoFile.getFullPath(), true, null);
						FileUtil.removeBackupFiles(project.getLocation().toOSString(), IRtcBuilderConstants.DEFAULT_ISO_202_XML);
					}
					IFile saveIsoxml = project.getFile(IRtcBuilderConstants.DEFAULT_ISO_202_XML);
					saveIsoxml.create(new ByteArrayInputStream(strIsoXml.getBytes("UTF-8")), true, null);
				} catch (Exception ex) {
					LOGGER.error("Fail to save iso-profile", ex);
					MessageDialog.openWarning(getSite().getShell(),
	    					Messages.getString("IMC.BASIC_ISO_PROFILE_TITLE"),
							Messages.getString("IMC.BASIC_ISO_EXPORT_ERROR"));
					return;
				}
				MessageDialog.openInformation(getSite().getShell(),
    					Messages.getString("IMC.BASIC_ISO_PROFILE_TITLE"),
						Messages.getString("IMC.BASIC_ISO_EXPORT_DONE"));
			}
		});
	}

	private void createCodeRestoreSection(FormToolkit toolkit, ScrolledForm form) {
		codeRestoreSection = createSectionBaseWithLabel(toolkit, form,
				Messages.getString("IMC.BASIC_CODE_RESTORE_TITLE"), Messages.getString("IMC.BASIC_CODE_RESTORE_EXPL"), 3);
		createCodeRestoreButton(toolkit);
		backupClearButton = toolkit.createButton(codeRestoreSection,
				Messages.getString("IMC.BASIC_BTN_BACKUP_CLEAR"), SWT.NONE);
		backupClearButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
    			if (!MessageDialog.openQuestion(getSite().getShell(),
    					Messages.getString("IMC.BASIC_BTN_BACKUP_CLEAR"),
    					Messages.getString("IMC.BASIC_BACKUP_CLEAR_MSG")) )
    				return;
				
				IWorkspaceRoot workspaceHandle = ResourcesPlugin.getWorkspace().getRoot();
				String targetProject = ((ROSBuilderEditor)editor).getROSParam().getOutputProject();
				IProject project = workspaceHandle.getProject(targetProject);
				File dir = new File(project.getLocation().toOSString());
				List<File> files = FileUtil.listAllFiles(dir);
				for(File target : files) {
					String name = target.getName();
					if(name.length() < 14) continue;
					String last14 = name.substring(name.length() - 14);
					if(last14.matches("\\d{14}")) {
						target.delete();
					}
				}
				MessageDialog.openInformation(getSite().getShell(),
    					Messages.getString("IMC.BASIC_BTN_BACKUP_CLEAR"),
						Messages.getString("IMC.BASIC_BACKUP_CLEAR_DONE_MSG"));
			}
		});
	}
	
	private void createCodeRestoreButton(FormToolkit toolkit) {
		codeRestoreButton = toolkit.createButton(codeRestoreSection,
				Messages.getString("IMC.BASIC_BTN_CODE_RESTORE"), SWT.NONE);
		codeRestoreButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				RestoreDialog dialog = new RestoreDialog(getSite().getShell());
				dialog.setTargetProject(((ROSBuilderEditor)editor).getROSParam().getOutputProject());
				dialog.setTargetFile(IRtcBuilderConstantsROS.DEFAULT_ROS_XML);
				int ret = dialog.open();
				if(ret != IDialogConstants.OK_ID) return;
				
				String targetTimeStamp = dialog.getTimeStamp();
				IWorkspaceRoot workspaceHandle = ResourcesPlugin.getWorkspace().getRoot();
				IProject project = workspaceHandle.getProject(((ROSBuilderEditor)editor).getROSParam().getOutputProject());
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
				((ROSBuilderEditor)editor).allUpdates();
				String validateRtcParam = ((ROSBuilderEditor)editor).validateParam();
				if (validateRtcParam != null) {
					MessageDialog.openError(getSite().getShell(), "Error", validateRtcParam);
					return;
				}
				///////////
				boolean outputRtc = false;
				String selectedFileNameRtc = "";
				boolean outputIso = false;
				String selectedFileNameIso = "";
        		ExportCreator export = new ExportCreator();
        		if(!export.canCreateProfileName(editor)) {
    				ExportDialog dialog = new ExportDialog(getSite().getShell());
    				dialog.setTargetKind("ROSProfile");
    				int ret = dialog.open();
    				if(ret != IDialogConstants.OK_ID) return;

    				outputRtc = dialog.outputRtc();
    				outputIso = dialog.outputIso();
    				selectedFileNameRtc = dialog.getRtcFileName();
    				selectedFileNameIso = dialog.getIsoFileName();
        		} else {
        			outputRtc = true;
        			selectedFileNameRtc = export.createProfileName(editor);
        		}

				if(outputIso) {
	        		try {
	            		ProfileHandlerROS handlerTemp = new ProfileHandlerROS();
						RosProfile profile = handlerTemp.convert2XMLProfile(editor.getGeneratorParam().getROSParam());
	
						ROS2ISOProfileHandler handler202 = new ROS2ISOProfileHandler();
						SIM result = handler202.convertROS2Iso(profile);
						handler202.saveXmlIso(result, selectedFileNameIso);
					} catch (Exception e3) {
						e3.printStackTrace();
					}
				}

		        if (outputRtc) {
		        	try {
		        		export.preExport(editor);

	            		ProfileHandlerROS handler = new ProfileHandlerROS();
	            		try {
	        				handler.validateROSXml(handler.convert2ROSXML(editor.getGeneratorParam()));
	        			} catch (JAXBException ex) {
	            			if (!MessageDialog.openQuestion(getSite().getShell(),ex.getMessage(),
	            					IMessageConstants.PROFILE_VALIDATE_ERROR_MESSAGE + System.getProperty("line.separator") + ex.getCause().toString()) )
	            				return ;// 「いいえ」のときは保存しない
	            		}// 通常のExceptionは外側でcatchする
	        			handler.storeToXML(selectedFileNameRtc, editor.getGeneratorParam());
	        			
		        		export.postExport(selectedFileNameRtc, editor);
		        		((ROSBuilderEditor)editor).getROSParam().resetUpdated();
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
				ImportDialog dialog = new ImportDialog(getSite().getShell());
				dialog.setTargetKind("ROSProfile");
				int ret = dialog.open();
				if(ret != IDialogConstants.OK_ID) return;
				
				String targetKind = dialog.getSelectedKind();
				String selectedFileName = dialog.getSelectedFile();

				if (targetKind.equals("RTC")) {
		        	try {
		        		String origProject = ((ROSBuilderEditor)editor).getROSParam().getOutputProject();
		        		ProfileHandlerROS handler = new ProfileHandlerROS();
						GeneratorParam genParam = handler.restorefromXMLFile(selectedFileName);
						editor.setGeneratorParam(genParam);
						((ROSBuilderEditor)editor).getROSParam().setOutputProject(origProject);
					} catch (Exception e1) {
						MessageDialog.openError(getSite().getShell(), "Error",
								Messages.getString("IMC.BASIC_IMPORT_ERROR"));
						return;
		        	}
				} else {
		        	try {
		        		String origProject = ((ROSBuilderEditor)editor).getROSParam().getOutputProject();
		        		ISO2ROSProfileHandler isoHandler = new ISO2ROSProfileHandler();
		        		ProfileHandlerROS handler = new ProfileHandlerROS();
		        		
	        			SIM profile = isoHandler.restoreFromFileIso(selectedFileName);
	        			RosProfile rosProfile = isoHandler.convertIso2Ros(profile);
	        			XmlHandlerROS xmlHandler = new XmlHandlerROS();
	        			String xmlFile = xmlHandler.convertToXmlROS(rosProfile);
	        			
						GeneratorParam genParam = handler.restorefromROSProfile(rosProfile);
						editor.setGeneratorParam(genParam);
						((ROSBuilderEditor)editor).getROSParam().setROSXml(xmlFile);

						((ROSBuilderEditor)editor).getROSParam().setOutputProject(origProject);
					} catch (Exception e1) {
						MessageDialog.openError(getSite().getShell(), "Error",
								Messages.getString("IMC.BASIC_IMPORT_ERROR"));
						return;
					}
	        		
		        }
				MessageDialog.openInformation(getSite().getShell(), "Finish",
						Messages.getString("IMC.BASIC_IMPORT_DONE"));
				//
				((ROSBuilderEditor)editor).allPagesReLoad();
				ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();
				((ROSBuilderEditor)editor).updateEMFPorts(
						rosParam.getTopicSubscribes(), rosParam.getTopicPublishes(),
						rosParam.getServiceServers(), rosParam.getServiceClients(),
						rosParam.getActionServers(), rosParam.getActionClients());
				((ROSBuilderEditor)editor).setEnabledInfoByLang();
				extractDataTypes();
				load();
				//
				editor.updateDirty();
			}
		});
	}

	protected void update() {
		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();

		rosParam.setPackageName(getText(packageText.getText()));
		rosParam.setNodeName(getText(nodeText.getText()));
		rosParam.setClassName(getText(classText.getText()));
		rosParam.setDescription(getText(descriptionText.getText()));
		rosParam.setVersion(getText(versionText.getText()));
		rosParam.setMaintainer(getText(maintainerText.getText()));
		rosParam.setCategory(getText(categoryCombo.getText()));

		rosParam.setLicense(getText(licenseCombo.getText()));
		rosParam.setContactAddress(getText(contactText.getText()));
		// 以下、cppRadioが有効な場合のみ実行する
		// →この画面が表示される前にこの処理が呼ばれた場合は、なにもしない
		// ∵rtcParamが画面に反映される前にクリアしてしまうとまずいため
		List<String> langList = new ArrayList<String>();

		if (cppRadio.getSelection()) {
			langList.add(IRtcBuilderConstants.LANG_CPP);
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
					break;
				}
				break;
			}
		}
		if (!rosParam.getLangList().equals(langList)) {
			rosParam.getLangList().clear();
			rosParam.getLangList().addAll(langList);
		}

		editor.updateEMFModuleName(getText(nodeText.getText()));
		editor.updateDirty();
	}

	/**
	 * データをロードする
	 */
	public void load() {
		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();

		packageText.setText(getValue(rosParam.getPackageName()));
		nodeText.setText(getValue(rosParam.getNodeName()));
		classText.setText(getValue(rosParam.getClassName()));
		descriptionText.setText(getValue(rosParam.getDescription()));
		versionText.setText(getValue(rosParam.getVersion()));
		maintainerText.setText(getValue(rosParam.getMaintainer()));
		categoryCombo.setText(getValue(rosParam.getCategory()));

		licenseCombo.setText(getValue(rosParam.getLicense()));
		contactText.setText(getValue(rosParam.getContactAddress()));
		//
		if(packageListViewer != null) {
			packageList = rosParam.getTargetEnv().getLibraries();
			packageListViewer.setInput(packageList);
		}
		//
		editor.updateEMFModuleName(rosParam.getNodeName());
		//
		if (rosParam.getLangList().contains(IRtcBuilderConstants.LANG_CPP)
				|| rosParam.getLangList().contains(
						IRtcBuilderConstants.LANG_CPPWIN)) {
			cppRadio.setSelection(true);
		} else {
			// rtcParam.getLangList()に含まれない場合は選択解除
			cppRadio.setSelection(false);
		}
		if (buttonList != null) {
			for (Button chkButton : buttonList) {
				if (rosParam.getLangList().contains(chkButton.getText().trim())) {
					chkButton.setSelection(true);
				} else {
					// rtcParam.getLangList()に含まれない場合は選択解除
					chkButton.setSelection(false);
				}
			}
		}
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
	//////////
	private class PackageLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof PackageParam == false) return null;
			PackageParam elem = (PackageParam) element;
			if (columnIndex == 0) {
				return elem.getName();
			} else if (columnIndex == 1) {
				return elem.getVersion();
			} else if (columnIndex == 2) {
				return elem.getOther();
			} else {
				return "";
			}
		}
	}
	private class PackageNameCellModifier extends EditingSupport {
		private CellEditor editor;
		int column;

		public PackageNameCellModifier(ColumnViewer viewer, int column) {
			super(viewer);
			this.column = column;
			editor = new TextCellEditor(((TableViewer) viewer).getTable());
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
			if (element instanceof PackageParam == false) return null;
			PackageParam elem = (PackageParam) element;
			String label = null;
			if(this.column == 0) {
				label = elem.getName();
			} else if(this.column == 1) {
				label = elem.getVersion();
			} else if(this.column == 2) {
				label = elem.getOther();
			}
			return label;
		}
		@Override
		protected void setValue(Object element, Object value) {
			if (element instanceof PackageParam == false) return;
			
			String modified = (String) value;
			boolean exists = packageList.stream()
					.anyMatch(item -> modified.equals(item.getName()));
			if(exists) return;

			PackageParam elem = (PackageParam) element;
			if(this.column == 0) {
				elem.setName((String) value);
			} else if(this.column == 1) {
				elem.setVersion((String) value);
			} else if(this.column == 2) {
				elem.setOther((String) value);
			}
			getViewer().update(element, null);
			update();
		}
	}
}
