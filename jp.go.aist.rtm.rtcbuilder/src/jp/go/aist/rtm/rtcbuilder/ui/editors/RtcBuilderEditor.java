package jp.go.aist.rtm.rtcbuilder.ui.editors;

import static jp.go.aist.rtm.toolscommon.profiles.util.XmlHandler.createXMLGregorianCalendar;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.FileEditorInput;
import org.iso.iso22166.part202.profile.SIM;
import org.openrtp.namespaces.rtc.version03.RtcProfile;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.meijo_u.iso22166_part202.util.RTC2ISOProfileHandler;
import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.RtcBuilderPlugin;
import jp.go.aist.rtm.rtcbuilder.container.param.ContainerParam;
import jp.go.aist.rtm.rtcbuilder.container.param.setting.ContainerConfig;
import jp.go.aist.rtm.rtcbuilder.extension.AddFormPageExtension;
import jp.go.aist.rtm.rtcbuilder.extension.EditorExtension;
import jp.go.aist.rtm.rtcbuilder.fsm.ScXMLHandler;
import jp.go.aist.rtm.rtcbuilder.fsm.StateParam;
import jp.go.aist.rtm.rtcbuilder.generator.ProfileHandler;
import jp.go.aist.rtm.rtcbuilder.generator.param.DataPortParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.EventPortParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.GeneratorParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.ParamUtil;
import jp.go.aist.rtm.rtcbuilder.generator.param.RtcParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.ServicePortInterfaceParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.ServicePortParam;
import jp.go.aist.rtm.rtcbuilder.manager.GenerateManager;
import jp.go.aist.rtm.rtcbuilder.model.component.BuildView;
import jp.go.aist.rtm.rtcbuilder.model.component.Component;
import jp.go.aist.rtm.rtcbuilder.model.component.ComponentFactory;
import jp.go.aist.rtm.rtcbuilder.model.component.DataInPort;
import jp.go.aist.rtm.rtcbuilder.model.component.DataOutPort;
import jp.go.aist.rtm.rtcbuilder.model.component.InterfaceDirection;
import jp.go.aist.rtm.rtcbuilder.model.component.PortDirection;
import jp.go.aist.rtm.rtcbuilder.model.component.ServiceInterface;
import jp.go.aist.rtm.rtcbuilder.model.component.ServicePort;
import jp.go.aist.rtm.rtcbuilder.ui.preference.ComponentPreferenceManager;
import jp.go.aist.rtm.rtcbuilder.ui.preference.ContainerPreferenceManager;
import jp.go.aist.rtm.rtcbuilder.ui.preference.DocumentPreferenceManager;
import jp.go.aist.rtm.rtcbuilder.util.StringUtil;

/**
 * RtcBuilderエディタ
 */
public class RtcBuilderEditor extends FormEditor implements IActionFilter {
	public static final String RTC_BUILDER_EDITOR_ID = RtcBuilderEditor.class
			.getName();

	public static String RTCBUILDER_NEW_EDITOR_PATH = "RtcBuilder";
	//
	public static String ECLPSE_VERSION_33 = "3.3";
	//
	private boolean isDirty;
	private String title;

	private GeneratorParam generatorParam;
	private BuildView buildview;

	private BasicEditorFormPage basicFormPage;
	private DataPortEditorFormPage dataPortFormPage;
	private ServicePortEditorFormPage servicePortFormPage;
	private ConfigurationEditorFormPage configurationFormPage;
	private RtcXmlEditorFormPage rtcXmlFormPage;
	private DocumentEditorFormPage documentFormPage;
	private ActivityEditorFormPage activityFormPage;
	private FSMEditorFormPage fsmFormPage;
	private ContainerEditorFormPage containerFormPage;

	private Map<Integer, AbstractCustomFormPage> customFormPages;

	//
	private List<GenerateManager> managerList = null;

	private IPageChangedListener pageChangedListener = new IPageChangedListener(){
		public void pageChanged(PageChangedEvent event) {
			if( event!=null ){
				if( event.getSelectedPage() instanceof AbstractEditorFormPage ){
					((AbstractEditorFormPage)event.getSelectedPage()).pageSelected();
				}
			}
		}
	};

	public RtcBuilderEditor() {
	}

	private IEditorInput load(IEditorInput input, IEditorSite site) {

		IEditorInput result = input;

		FileEditorInput fileEditorInput = ((FileEditorInput) result);
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		try {
			ProfileHandler handler = new ProfileHandler();
			generatorParam = handler.restorefromXMLFile(fileEditorInput.getPath().toOSString());
			/////
			String configText = ContainerPreferenceManager.getInstance().getSettings();
			ObjectMapper mapper = new ObjectMapper();
			try {
				ContainerConfig containerSettings =  mapper.readValue(configText, ContainerConfig.class);
				for(ContainerParam each : generatorParam.getRtcParam().getContainerSettings() ) {
					each.updateDefaultLibs(containerSettings);
				}
			} catch (Exception e) {
			}
			//
			String targetFile = this.getRtcParam().getName() + "FSM.scxml";
			IProject project = root.getProject(this.getRtcParam().getOutputProject());
			IFile fsmFile  = project.getFile(targetFile);
			if(fsmFile.exists()) {
				targetFile = fsmFile.getLocation().toOSString();
				ScXMLHandler scHandler = new ScXMLHandler();
				StringBuffer buffer = new StringBuffer();
				StateParam rootState = scHandler.parseSCXML(targetFile, buffer);
				if(rootState!=null) {
					this.getRtcParam().setFsmParam(rootState);
					this.getRtcParam().setFsmContents(buffer.toString());
					this.getRtcParam().parseEvent();
				}
			}
			//
			if( buildview==null ) buildview = ComponentFactory.eINSTANCE.createBuildView();
			updateEMFModuleName(this.getRtcParam().getName());
			updateEMFDataPorts(
					this.getRtcParam().getInports(), this.getRtcParam().getOutports(),
					this.getRtcParam().getEventports(),this.getRtcParam().getServicePorts());
		} catch (Exception e) {
			createGeneratorParam();
		}
		String[] target = ((FileEditorInput) result).getPath().segments();
		if( target.length>1 ) {
			title = target[target.length-2];
			generatorParam.getRtcParam().setOutputProject(title);
		} else {
			title = ((FileEditorInput) result).getPath().lastSegment();
			generatorParam.getRtcParam().setOutputProject(title);
		}
		//
		try {
			IProject project = root.getProject(this.getRtcParam().getOutputProject());
			IFolder idlDir  = project.getFolder("idl");
			if (!idlDir.exists()) {
				idlDir.create(true, true, null);
			}			
		} catch (Exception e) {
			createGeneratorParam();
		}
		
		//on_initializeは常にON
		setOnInitialize();
		//

		isDirty = false;
		firePropertyChange(IEditorPart.PROP_TITLE);

		if( basicFormPage != null )	 basicFormPage.load();
		allPagesReLoad();
		this.setInput(result);

		return result;
	}

	private void setOnInitialize() {
		RtcParam param  = generatorParam.getRtcParam();
		param.setActionImplemented(IRtcBuilderConstants.ACTIVITY_INITIALIZE, true);
	}

	public void loadNewData(RtcParam param) {
		this.generatorParam.setRtcParam(param);

		title = "RtcBuilder";
		if( buildview==null ) buildview = ComponentFactory.eINSTANCE.createBuildView();
		updateEMFModuleName(this.getRtcParam().getName());
		updateEMFDataPorts(this.getRtcParam().getInports(), this.getRtcParam().getOutports(),
				this.getRtcParam().getEventports(), this.getRtcParam().getServicePorts());
		//
		if( basicFormPage != null )	 basicFormPage.load();
		allPagesReLoad();

		updateDirty();
	}

	private void createGeneratorParam(){
		generatorParam = new GeneratorParam();
		RtcParam rtcParam = new RtcParam(generatorParam);
		rtcParam.setSchemaVersion(IRtcBuilderConstants.SCHEMA_VERSION);
		//
		rtcParam.setName(ComponentPreferenceManager.getInstance().getBasic_ComponentName());
		rtcParam.setDescription(ComponentPreferenceManager.getInstance().getBasic_Description());
		rtcParam.setCategory(ComponentPreferenceManager.getInstance().getBasic_Category());
		rtcParam.setVersion(ComponentPreferenceManager.getInstance().getBasic_Version());
		rtcParam.setVender(ComponentPreferenceManager.getInstance().getBasic_VendorName());
		rtcParam.setComponentType(ComponentPreferenceManager.getInstance().getBasic_ComponentType());
		rtcParam.setActivityType(ComponentPreferenceManager.getInstance().getBasic_ActivityType());
		rtcParam.setComponentKind("DataFlowComponent");
		rtcParam.setMaxInstance(ComponentPreferenceManager.getInstance().getBasic_MaxInstances());
		rtcParam.setExecutionType(ComponentPreferenceManager.getInstance().getBasic_ExecutionType());
		rtcParam.setExecutionRate(ComponentPreferenceManager.getInstance().getBasic_ExecutionRate());
		//
		ArrayList<String> docs = DocumentPreferenceManager.getDocumentValue();
		for( int intidx=IRtcBuilderConstants.ACTIVITY_INITIALIZE; intidx<IRtcBuilderConstants.ACTIVITY_DUMMY; intidx++) {
			rtcParam.setActionImplemented(intidx, docs.get(intidx));
		}
		//
		rtcParam.setDocLicense(DocumentPreferenceManager.getLicenseValue());
		rtcParam.setDocCreator(DocumentPreferenceManager.getCreatorValue());
		//
		rtcParam.resetUpdated();
		generatorParam.setRtcParam(rtcParam);
		buildview = ComponentFactory.eINSTANCE.createBuildView();
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		IEditorInput newInput = load(input, site);
		super.init(site, newInput);
		managerList = RtcBuilderPlugin.getDefault().getLoader().getManagerList(IRtcBuilderConstants.MIDDLEWARE_RTM);
		// ページ切り替え時のイベントを管理
		addPageChangedListener(pageChangedListener);
	}

	/**
	 * {@inheritDoc}
	 */
	protected FormToolkit createToolkit(Display display) {
		return new FormToolkit(getSite().getShell().getDisplay());
	}

	@Override
	protected void addPages() {
		try {
			AbstractEditorFormPage[] defaultPages = new AbstractEditorFormPage[9];
			//
			basicFormPage = new BasicEditorFormPage(this);
			defaultPages[0] = basicFormPage;
			activityFormPage = new ActivityEditorFormPage(this);
			defaultPages[1] = activityFormPage;
			fsmFormPage = new FSMEditorFormPage(this);
			defaultPages[2] = fsmFormPage;
			dataPortFormPage = new DataPortEditorFormPage(this);
			dataPortFormPage.setDefaultTypeList(fsmFormPage.getDefaultTypeList());
			defaultPages[3] = dataPortFormPage;
			servicePortFormPage = new ServicePortEditorFormPage(this);
			defaultPages[4] = servicePortFormPage;
			configurationFormPage = new ConfigurationEditorFormPage(this);
			defaultPages[5] = configurationFormPage;
			documentFormPage = new DocumentEditorFormPage(this);
			defaultPages[6] = documentFormPage;
			containerFormPage = new ContainerEditorFormPage(this);
			defaultPages[7] = containerFormPage;
			rtcXmlFormPage = new RtcXmlEditorFormPage(this);
			defaultPages[8] = rtcXmlFormPage;
			//
			List<List<AbstractEditorFormPage>> forms = new ArrayList<List<AbstractEditorFormPage>>();
			forms.add(new ArrayList<AbstractEditorFormPage>());
			for (AbstractEditorFormPage p : defaultPages) {
				List<AbstractEditorFormPage> list = new ArrayList<AbstractEditorFormPage>();
				list.add(p);
				forms.add(list);
			}
			List<AbstractEditorFormPage> last = forms.get(forms.size() - 1);
			//
			List extList = RtcBuilderPlugin.getDefault()
					.getAddFormPageExtensionLoader().getList();
			if (extList != null) {
				for (Object o : extList) {
					AddFormPageExtension ext = (AddFormPageExtension) o;
					Map<Integer, AbstractCustomFormPage> map = ext
							.getCustomPages(this);
					for (Integer i : map.keySet()) {
						if (i < 0 || i >= defaultPages.length) {
							last.add(map.get(i));
						} else {
							forms.get(i).add(map.get(i));
						}
					}
				}
			}
			//
			this.customFormPages = new HashMap<Integer, AbstractCustomFormPage>();
			for (List<AbstractEditorFormPage> list : forms) {
				for (AbstractEditorFormPage p : list) {
					int index = addPage(p);
					if (p instanceof AbstractCustomFormPage) {
						this.customFormPages.put(index,
								(AbstractCustomFormPage) p);
					}
				}
			}
			// nullページが挿入されるので削除しておく (Eclipseのバグ？)
			if (this.pages.contains(null)) {
				int nullIndex = -1;
				for (int i = 0; i < this.pages.size(); i++) {
					if (this.pages.get(i) == null) {
						nullIndex = i;
						break;
					}
				}
				if (nullIndex >= 0) {
					this.pages.remove(nullIndex);
				}
			}
			//
			for (AbstractCustomFormPage p : customFormPages.values()) {
				p.setDefaultEnableInfo();
			}
		} catch (PartInitException e) {
			throw new RuntimeException(e); // system error
		}
	}

	private void customPagesOperation(String command) {
		if (customFormPages == null)
			return;

		for (Integer i : customFormPages.keySet()) {
			AbstractCustomFormPage customPage = customFormPages.get(i);
			if (customPage == null) {
				continue;
			}
			String key = customPage.getManagerKey();
			if (command.equals("load")) {
				customPage.load();
			} else if (command.equals("update")) {
				// 言語選択に一致しないページは画面→RtcParamへ反映しない
				if (!StringUtil.matchKey(getRtcParam().getLangList(), key)) {
					continue;
				}
				customPage.update();
			}
		}
	}

	protected void allPagesReLoad(){
		if( dataPortFormPage != null ) dataPortFormPage.load();
		if( servicePortFormPage != null ) servicePortFormPage.load();
		if( configurationFormPage != null ) configurationFormPage.load();
		if( rtcXmlFormPage != null ) rtcXmlFormPage.load();
		if( documentFormPage != null ) documentFormPage.load();
		if( activityFormPage != null ) activityFormPage.load();
		if( fsmFormPage != null ) fsmFormPage.load();
		//
		customPagesOperation("load");
	}

	protected void allUpdates(){
		basicFormPage.update();
		dataPortFormPage.updateForOutput();
		servicePortFormPage.update();
		configurationFormPage.updateForOutput();
		documentFormPage.update();
		activityFormPage.update();
		fsmFormPage.update();
		//
		customPagesOperation("update");
	}

	protected void addDefaultComboValue(){
		basicFormPage.addDefaultComboValue();
	}

	public void updateDataTypes() {
		if( dataPortFormPage != null ) dataPortFormPage.updateDefaultValue();
	}

	public void updatePages() {
		if( activityFormPage != null ) activityFormPage.load();
	}

	public String validateParam() {
		String result = null;
		for (int intIdx = 0; intIdx < this.pages.size(); intIdx++) {
			AbstractEditorFormPage page = (AbstractEditorFormPage) this.pages.get(intIdx);
			if (page == null) continue;
			if (page instanceof AbstractCustomFormPage) {
				String key = ((AbstractCustomFormPage) page).getManagerKey();
				if (!StringUtil.matchKey(getRtcParam().getLangList(), key)) continue;
			}
			result = page.validateParam();
			if (result != null) {
				this.setActivePage(intIdx);
				return result;
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public void doSave(IProgressMonitor monitor) {
		boolean isRtcXml = getCurrentPage() == rtcXmlFormPage.getIndex();
		this.allUpdates();
		RtcBuilderPlugin.getDefault().setCanExit(true);

		if (isRtcXml) {
			try {
				ProfileHandler handler = new ProfileHandler();
				if( !handler.validateXml(this.getRtcParam().getRtcXml()) ) return;
			} catch (Exception e) {
				String errMessage = null;
				if( e.getCause()==null ) {
					errMessage = e.getMessage();
				} else {
					errMessage = e.getCause().toString();
				}
				MessageDialog.openError(getSite().getShell(), "XML Save Error", errMessage);
				//例外発生時には処理中断
				RtcBuilderPlugin.getDefault().setCanExit(false);
				return;
			}
		}else{
			// RTC.xmlページではないとき
			try {
				ProfileHandler handler = new ProfileHandler();
				if( !handler.validateXml(handler.convert2XML(this.getGeneratorParam())) ) return;
			} catch (JAXBException ex) {
				boolean result = MessageDialog.openQuestion(
						getSite().getShell(),
						ex.getMessage(),
    					IMessageConstants.PROFILE_VALIDATE_ERROR_MESSAGE + System.getProperty("line.separator") + ex.getCause().toString()
    				);
    			if( !result ) {
    				RtcBuilderPlugin.getDefault().setCanExit(false);
    				return;// 「いいえ」のときは保存しない
    			}
			} catch (Exception e) {
				MessageDialog.openError(getSite().getShell(), "XML Save Error", e.getMessage());
				//例外発生時には処理中断
				RtcBuilderPlugin.getDefault().setCanExit(false);
				return;
			}
		}

		IFile file = ((IFileEditorInput) getEditorInput()).getFile();

		try {
			save(file, monitor, isRtcXml);
		} catch (CoreException e) {
			ErrorDialog.openError(getSite().getShell(), "Error During Save",
					"The current model could not be saved.", e.getStatus());
		} catch (Exception e) {
			MessageDialog.openError(getSite().getShell(), "Error During Save",
					"The current model could not be saved.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void doSaveAs() {
	}

	/**
	 * {@inheritDoc}
	 * @throws Exception
	 */
	private void save(IFile file, IProgressMonitor progressMonitor, boolean blnRtcXml)
			throws Exception {

		if (null == progressMonitor) progressMonitor = new NullProgressMonitor();

		progressMonitor.beginTask("Saving ", 2);
		String xmlFile = "";
		if( blnRtcXml ) {
	        xmlFile = this.getRtcParam().getRtcXml();
		} else {
			String dateTime = createXMLGregorianCalendar(new Date()).toString();
			generatorParam.getRtcParam().setUpdateDate(dateTime);
			ProfileHandler handler = new ProfileHandler();
			xmlFile = handler.convert2XML(generatorParam);
		}
		progressMonitor.worked(15);
		//

		IProject projectHandle = file.getProject();
		try {
			IFile rtcxml = projectHandle.getFile(IRtcBuilderConstants.DEFAULT_RTC_XML);
			if( rtcxml.exists()) rtcxml.delete(true, null);
			rtcxml.create(new ByteArrayInputStream(xmlFile.getBytes("UTF-8")), true, null);
			//
			////FSM
			if(this.getRtcParam().getFsmParam()!=null) {
				String fsmName = this.getRtcParam().getName() + "FSM.scxml";
				IFile fsmFile  = projectHandle.getFile(fsmName);
				if(this.getRtcParam().getFsmContents().trim().length()==0) {
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
					String xmlSplit[] = this.getRtcParam().getFsmContents().split(System.lineSeparator());
					try {
						BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(strPath), "UTF-8"));
						for (String s : xmlSplit) {
							if(s.length()==0) continue;
							writer.write(s);
							writer.newLine();
						}
						writer.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				String dummyName = ".Dummy.scxml";
				IFile dummyFile  = projectHandle.getFile(dummyName);
				if(dummyFile.exists()) {
					try {
						dummyFile.delete(true, null);
					} catch (CoreException ex) {
						ex.printStackTrace();
					}
				}
			}
			//ISO
			ProfileHandler handler = new ProfileHandler();
			RtcProfile rtcProfile = handler.convert2XMLProfile(this.getRtcParam());
			RTC2ISOProfileHandler isoHandler = new RTC2ISOProfileHandler();
			SIM isoProfile = isoHandler.convertRtc2Iso(rtcProfile);
			String isoFile = isoHandler.convertToXmlIso(isoProfile);
			
			IFile isoxml = projectHandle.getFile(IRtcBuilderConstants.DEFAULT_ISO_202_XML);
			if( isoxml.exists()) isoxml.delete(true, null);
			isoxml.create(new ByteArrayInputStream(isoFile.getBytes("UTF-8")), true, null);
			/////////
			//
			setInput(new FileEditorInput(rtcxml));
			this.getRtcParam().setRtcXml(xmlFile);
			//
			// isDirty = false;
			// firePropertyChange(IEditorPart.PROP_DIRTY);
			getRtcParam().resetUpdated();
			updateDirty();
		} catch (UnsupportedEncodingException e) {
			IStatus status = new Status(IStatus.ERROR, RtcBuilderPlugin
						.getDefault().getClass().getName(), 0,
						"Error writing file.", e);
			progressMonitor.done();
			throw new CoreException(status);
		} catch (NullPointerException ex) {
			MessageDialog.openError(getSite().getShell(), "Error",
					"Error writing file.");
			progressMonitor.done();
			throw new CoreException(null);
		}
		//
		if( blnRtcXml ) {
			updateProfiles(xmlFile);
		}
		//
		if( rtcXmlFormPage != null ) rtcXmlFormPage.load();
		addDefaultComboValue();
		title = projectHandle.getName();
		firePropertyChange(IEditorPart.PROP_TITLE);
		progressMonitor.done();
	}

	protected void updateProfiles(String xmlFile) throws Exception {
		// RTC.xmlの内容を他のページに反映
		ProfileHandler handler = new ProfileHandler();
		RtcProfile module = handler.restorefromXML(xmlFile);
		ParamUtil putil = new ParamUtil();
		getGeneratorParam().setRtcParam(putil.convertFromModule(module, generatorParam, managerList));
		getRtcParam().setRtcXml(xmlFile);
		//
		if (basicFormPage != null) basicFormPage.load();
		if (dataPortFormPage != null) dataPortFormPage.load();
		if (servicePortFormPage != null) servicePortFormPage.load();
		if (configurationFormPage != null) configurationFormPage.load();
		if (documentFormPage != null) documentFormPage.load();
		if (activityFormPage != null) activityFormPage.load();
		if (fsmFormPage != null) fsmFormPage.load();
		//
		customPagesOperation("load");
		//
		getRtcParam().resetUpdated();
		updateDirty();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void firePropertyChange(int propertyId) {
		super.firePropertyChange(propertyId);
	}

	/**
	 * エディタをダーティにする。
	 */
	public void updateDirty() {
		setDirty(getRtcParam().isUpdated());
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * GeneratorParamを取得する
	 */
	public GeneratorParam getGeneratorParam() {
		return generatorParam;
	}
	public void setGeneratorParam(GeneratorParam genparam) {
		this.generatorParam = genparam;
	}

	/**
	 * RtcParamを取得する
	 */
	public RtcParam getRtcParam() {
		return generatorParam.getRtcParam();
	}

	/**
	 * EMF modelを取得する
	 */
	public BuildView getEMFmodel() {
		return buildview;
	}

	public void updateEMFModuleName(String name) {
		((Component)buildview.getComponents().get(0)).setComponent_Name(name);
	}

	public void updateEMFDataPorts(
			List<DataPortParam> dataInPorts, List<DataPortParam> dataOutPorts,
			List<EventPortParam> eventPorts, List<ServicePortParam> servicePorts) {
		updateEMFDataInPorts(eventPorts, dataInPorts);
		updateEMFDataOutPorts(dataOutPorts);
		updateEMFServiceOutPorts(servicePorts);
	}

	private void updateEMFDataInPorts(List<EventPortParam> eventPorts, List<DataPortParam> dataInPorts) {
		((Component)buildview.getComponents().get(0)).clearDataInports();
		int portIndex = 0;
		for(int intIdx=0; intIdx<dataInPorts.size();intIdx++ ) {
			DataInPort dataInport= ComponentFactory.eINSTANCE.createDataInPort();
			dataInport.setInPort_Name(dataInPorts.get(intIdx).getName());
			dataInport.setIndex(portIndex);
			portIndex++;
			dataInport.setDirection(PortDirection.get(dataInPorts.get(intIdx).getPositionByIndex()));
			((Component)buildview.getComponents().get(0)).addDataInport(dataInport);
		}
		//
		if(0<eventPorts.size() ) {
			DataInPort dataInport= ComponentFactory.eINSTANCE.createDataInPort();
			dataInport.setInPort_Name(eventPorts.get(0).getName());
			dataInport.setIndex(portIndex);
			dataInport.setPort_Type(IRtcBuilderConstants.Type_Event);
			portIndex++;
			dataInport.setDirection(PortDirection.get(eventPorts.get(0).getPositionByIndex()));
			((Component)buildview.getComponents().get(0)).addDataInport(dataInport);
			
		}
	}

	private void updateEMFDataOutPorts(List<DataPortParam> dataOutPorts) {
		((Component)buildview.getComponents().get(0)).clearDataOutports();
		for(int intIdx=0; intIdx<dataOutPorts.size();intIdx++ ) {
			DataOutPort dataOutport= ComponentFactory.eINSTANCE.createDataOutPort();
			dataOutport.setOutPort_Name(dataOutPorts.get(intIdx).getName());
			dataOutport.setIndex(intIdx);
			dataOutport.setDirection(PortDirection.get(dataOutPorts.get(intIdx).getPositionByIndex()));
			((Component)buildview.getComponents().get(0)).addDataOutport(dataOutport);
		}
	}

	private void updateEMFServiceOutPorts(List<ServicePortParam> servicePorts) {
		((Component)buildview.getComponents().get(0)).clearServiceports();
		for(int intIdx=0; intIdx<servicePorts.size();intIdx++ ) {
			ServicePortParam srvParam = servicePorts.get(intIdx);
			ServicePort servicePort= ComponentFactory.eINSTANCE.createServicePort();
			servicePort.setServicePort_Name(srvParam.getName());
			servicePort.setIndex(intIdx);
			servicePort.setDirection(PortDirection.get(srvParam.getPositionByIndex()));
			for(int idxIf=0;idxIf<srvParam.getServicePortInterfaces().size();idxIf++) {
				ServicePortInterfaceParam srvIfParam = srvParam.getServicePortInterfaces().get(idxIf);
				ServiceInterface serviceIF = ComponentFactory.eINSTANCE.createServiceInterface();
				serviceIF.setServiceInterface_Name(srvIfParam.getName());
				serviceIF.setDirection(InterfaceDirection.get(srvIfParam.getDirection()));
				serviceIF.setParentDirection(servicePort.getDirection());
				serviceIF.setIndex(idxIf);
				servicePort.addServiceInterface(serviceIF);
			}
			((Component)buildview.getComponents().get(0)).addServiceport(servicePort);
		}
	}

	public boolean testAttribute(Object target, String name, String value) {
		boolean result = false;
		if ("dirty".equals(name)) {
			if (isDirty()) {
				result = "true".equalsIgnoreCase(value);
			} else {
				result = "false".equalsIgnoreCase(value);
			}
		}
		return result;
	}

	@SuppressWarnings("unused")
	private String getEclipseVersion() {
		return System.getProperty("osgi.framework.version");
	}

	public void setEnabledInfoByLang() {
		setEnabledInfoByLang(getRtcParam().getLanguage());
	}

	@SuppressWarnings("deprecation")
	public void setEnabledInfoByLang(String langName) {
		EditorExtension ext = RtcBuilderPlugin.getDefault()
				.getEditorExtensionLoader().findByLang(langName);
		if (ext == null) {
			// 全表示(C++含む)
			setEnabledInfo(new ArrayList<String>());
		} else {
			if (!ext.getDisableFormWidgets().isEmpty()) {
				setEnabledInfo(ext.getDisableFormWidgets());
			} else {
				// 廃止予定
				setEnabledInfoObsolete(ext.getInapplicables());
			}
		}
		// 拡張しているFormPage
		for (AbstractCustomFormPage customPage : customFormPages.values()) {
			customPage.setEnableInfo(langName);
		}
	}

	void setEnabledInfo(List<String> infos) {
		AbstractEditorFormPage.WidgetInfo widgetInfo;
		widgetInfo = createWidgetInfo("basic.*.*");
		basicFormPage.setEnabledInfo(widgetInfo, true);
		widgetInfo = createWidgetInfo("activity.*.*");
		activityFormPage.setEnabledInfo(widgetInfo, true);
		widgetInfo = createWidgetInfo("fsm.*.*");
		fsmFormPage.setEnabledInfo(widgetInfo, true);
		widgetInfo = createWidgetInfo("dataport.*.*");
		dataPortFormPage.setEnabledInfo(widgetInfo, true);
		widgetInfo = createWidgetInfo("serviceport.*.*");
		servicePortFormPage.setEnabledInfo(widgetInfo, true);
		widgetInfo = createWidgetInfo("config.*.*");
		configurationFormPage.setEnabledInfo(widgetInfo, true);
		for (String info : infos) {
			widgetInfo = createWidgetInfo(info);
			if (widgetInfo == null) {
				continue;
			}
			if (widgetInfo.matchForm("basic")) {
				basicFormPage.setEnabledInfo(widgetInfo, false);
			} else if (widgetInfo.matchForm("activity")) {
				activityFormPage.setEnabledInfo(widgetInfo, false);
			} else if (widgetInfo.matchForm("fsm")) {
				fsmFormPage.setEnabledInfo(widgetInfo, false);
			} else if (widgetInfo.matchForm("dataport")) {
				dataPortFormPage.setEnabledInfo(widgetInfo, false);
			} else if (widgetInfo.matchForm("serviceport")) {
				servicePortFormPage.setEnabledInfo(widgetInfo, false);
			} else if (widgetInfo.matchForm("config")) {
				configurationFormPage.setEnabledInfo(widgetInfo, false);
			}
		}
	}

	/** 古い方式によるフォームの活性化設定 */
	@Deprecated
	void setEnabledInfoObsolete(List<String> infos) {
		AbstractEditorFormPage.WidgetInfo widgetInfo;
		//
		if (infos.contains(EditorExtension.RTC_PROFILE_PARAMETERS_INAPPLICABLE)) {
			widgetInfo = createWidgetInfo("config.configParam.*");
			configurationFormPage.setEnabledInfo(widgetInfo, false);
		} else {
			widgetInfo = createWidgetInfo("config.*.*");
			configurationFormPage.setEnabledInfo(widgetInfo, true);
		}
		//
		if (infos
				.contains(EditorExtension.RTC_PROFILE_SERVICE_PORTS_INAPPLICABLE)) {
			widgetInfo = createWidgetInfo("serviceport.servicePort.*");
			servicePortFormPage.setEnabledInfo(widgetInfo, false);
		} else {
			widgetInfo = createWidgetInfo("serviceport.*.*");
			servicePortFormPage.setEnabledInfo(widgetInfo, true);
		}
		//
		if (infos.contains(EditorExtension.RTC_PROFILE_DATA_PORTS_INAPPLICABLE)) {
			widgetInfo = createWidgetInfo("dataport.inPort.*");
			dataPortFormPage.setEnabledInfo(widgetInfo, false);
			widgetInfo = createWidgetInfo("dataport.outPort.*");
			dataPortFormPage.setEnabledInfo(widgetInfo, false);
		} else {
			widgetInfo = createWidgetInfo("dataport.*.*");
			dataPortFormPage.setEnabledInfo(widgetInfo, true);
		}
		//
		if (infos
				.contains(EditorExtension.GENERATE_BUTTON_SECTION_INAPPLICABLE)) {
			widgetInfo = createWidgetInfo("basic.outputProject.*");
			basicFormPage.setEnabledInfo(widgetInfo, false);
			widgetInfo = createWidgetInfo("basic.generate.*");
			basicFormPage.setEnabledInfo(widgetInfo, false);
		} else {
			widgetInfo = createWidgetInfo("basic.*.*");
			basicFormPage.setEnabledInfo(widgetInfo, true);
		}
	}

	@Deprecated
	public void setEnabledInfoByLangFromRtcParam() {
		setEnabledInfoByLang(getRtcParam().getLanguage());
		// FormPage
		for (AbstractCustomFormPage customPage : customFormPages.values()) {
			customPage.setEnableInfo(getRtcParam().getLanguage());
		}
	}

	AbstractEditorFormPage.WidgetInfo createWidgetInfo(String widgetName) {
		if (widgetName == null) {
			return null;
		}
		String[] ss = widgetName.split("\\.");
		if (ss.length != 3) {
			return null;
		}
		return new AbstractEditorFormPage.WidgetInfo(ss[0], ss[1], ss[2]);
	}
}
