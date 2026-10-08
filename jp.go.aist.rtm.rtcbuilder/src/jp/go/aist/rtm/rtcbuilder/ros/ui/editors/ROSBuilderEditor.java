package jp.go.aist.rtm.rtcbuilder.ros.ui.editors;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.FileEditorInput;
import org.iso.iso22166.part202.profile.SIM;
import org.openrtp.namespaces.ros.version01.RosProfile;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.meijo_u.iso22166_part202.util.ROS2ISOProfileHandler;
import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.RtcBuilderPlugin;
import jp.go.aist.rtm.rtcbuilder.container.param.ContainerParam;
import jp.go.aist.rtm.rtcbuilder.container.param.setting.ContainerConfig;
import jp.go.aist.rtm.rtcbuilder.generator.param.GeneratorParam;
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
import jp.go.aist.rtm.rtcbuilder.ros.IRtcBuilderConstantsROS;
import jp.go.aist.rtm.rtcbuilder.ros.ParamUtilROS;
import jp.go.aist.rtm.rtcbuilder.ros.ProfileHandlerROS;
import jp.go.aist.rtm.rtcbuilder.ros.param.ActionParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.ROSParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.ServiceParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.TopicParam;
import jp.go.aist.rtm.rtcbuilder.ros.ui.preference.ROSPreferenceManager;
import jp.go.aist.rtm.rtcbuilder.ui.editors.AbstractEditorFormPage;
import jp.go.aist.rtm.rtcbuilder.ui.editors.IMessageConstants;
import jp.go.aist.rtm.rtcbuilder.ui.editors.RtcBuilderEditor;
import jp.go.aist.rtm.rtcbuilder.ui.preference.ContainerPreferenceManager;

/**
 * RtcBuilderエディタ
 */
public class ROSBuilderEditor extends RtcBuilderEditor implements IActionFilter {
	public static final String ROS_BUILDER_EDITOR_ID = ROSBuilderEditor.class
			.getName();

	public static String ROSBUILDER_NEW_EDITOR_PATH = "ROSBuilder";
	//
	public static String ECLPSE_VERSION_33 = "3.3";
	//
	private boolean isDirty;
	private String title;

	private GeneratorParam generatorParam;
	private BuildView buildview;

	private ROSBasicEditorFormPage basicFormPage;
	private LifecycleEditorFormPage lifecycleFormPage;
	private TopicEditorFormPage topicFormPage;
	private ServiceEditorFormPage serviceFormPage;
	private ActionEditorFormPage actionFormPage;
	private ParameterEditorFormPage parameterFormPage;
	private ROSDocumentEditorFormPage documentFormPage;
	private ROSXmlEditorFormPage rosXmlFormPage;
	private ROSContainerEditorFormPage containerFormPage;

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

	public ROSBuilderEditor() {
	}

	private IEditorInput load(IEditorInput input, IEditorSite site) {

		IEditorInput result = input;

		FileEditorInput fileEditorInput = ((FileEditorInput) result);
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();

		try {
			ProfileHandlerROS handler = new ProfileHandlerROS();
			generatorParam = handler.restorefromXMLFile(fileEditorInput.getPath().toOSString());
			/////
			String configText = ContainerPreferenceManager.getInstance().getSettingsROS();
			ObjectMapper mapper = new ObjectMapper();
			try {
				ContainerConfig containerSettings =  mapper.readValue(configText, ContainerConfig.class);
				for(ContainerParam each : generatorParam.getROSParam().getContainerSettings() ) {
					each.updateDefaultLibs(containerSettings);
				}
			} catch (Exception e) {
			}
			//
			if( buildview==null ) buildview = ComponentFactory.eINSTANCE.createBuildView();
			updateEMFModuleName(this.getROSParam().getNodeName());
			updateEMFPorts(this.getROSParam().getTopicSubscribes(), this.getROSParam().getTopicPublishes(),
						 	this.getROSParam().getServiceServers(), this.getROSParam().getServiceClients(),
							this.getROSParam().getActionServers(), this.getROSParam().getActionClients());
		} catch (Exception e) {
			createGeneratorParam();
		}
		String[] target = ((FileEditorInput) result).getPath().segments();
		if( target.length>1 ) {
			title = target[target.length-2];
			generatorParam.getROSParam().setOutputProject(title);
		} else {
			title = ((FileEditorInput) result).getPath().lastSegment();
			generatorParam.getROSParam().setOutputProject(title);
		}
		//
		try {
			IProject project = root.getProject(this.getROSParam().getOutputProject());
			IFolder msgDir  = project.getFolder("msg");
			if (!msgDir.exists()) {
				msgDir.create(true, true, null);
			}			
			IFolder srvDir  = project.getFolder("srv");
			if (!srvDir.exists()) {
				srvDir.create(true, true, null);
			}			
			IFolder actionDir  = project.getFolder("action");
			if (!actionDir.exists()) {
				actionDir.create(true, true, null);
			}			
		} catch (Exception e) {
			createGeneratorParam();
		}
		
		setCallback();
		//

		isDirty = false;
		firePropertyChange(IEditorPart.PROP_TITLE);

		if( basicFormPage != null )	 basicFormPage.load();
		allPagesReLoad();
		this.setInput(result);

		return result;
	}

	private void setCallback() {
		ROSParam param  = generatorParam.getROSParam();
		param.setActionImplemented(IRtcBuilderConstantsROS.ACTIVITY_CONFIGURE, true);
		param.setActionImplemented(IRtcBuilderConstantsROS.ACTIVITY_ACTIVATE, true);
		param.setActionImplemented(IRtcBuilderConstantsROS.ACTIVITY_DEACTIVATE, true);
	}

	private void createGeneratorParam(){
		generatorParam = new GeneratorParam();
		ROSParam rosParam = new ROSParam(generatorParam);
		rosParam.setSchemaVersion(IRtcBuilderConstantsROS.SCHEMA_VERSION_ROS);
		//
		rosParam.initialize();
		rosParam.setMaintainer(ROSPreferenceManager.getMaintainerNameValue());
		rosParam.setContactAddress(ROSPreferenceManager.getMaintainerAddressValue());
		ArrayList<String> docs = ROSPreferenceManager.getDocumentValue();
		for(int index=0; index<IRtcBuilderConstantsROS.ACTIVITY_CAN_EDIT_NUM; index++) {
			rosParam.setActionImplemented(index + 3, docs.get(index));
		}
		
		rosParam.resetUpdated();
		generatorParam.setROSParam(rosParam);
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
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		try {
			IProject project = root.getProject(this.getROSParam().getOutputProject());
			IFolder idlDir  = project.getFolder("idl");
			if (idlDir.exists()) {
				idlDir.delete(true, null);
			}			
		} catch (Exception e) {
			createGeneratorParam();
		}
		
		if(managerList != null) managerList.clear();
		managerList = RtcBuilderPlugin.getDefault().getLoader().getManagerList(IRtcBuilderConstants.MIDDLEWARE_ROS);
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
			basicFormPage = new ROSBasicEditorFormPage(this);
			defaultPages[0] = basicFormPage;
			lifecycleFormPage = new LifecycleEditorFormPage(this);
			defaultPages[1] = lifecycleFormPage;
			topicFormPage = new TopicEditorFormPage(this);
			defaultPages[2] = topicFormPage;
			serviceFormPage = new ServiceEditorFormPage(this);
			defaultPages[3] = serviceFormPage;
			actionFormPage = new ActionEditorFormPage(this);
			defaultPages[4] = actionFormPage;
			parameterFormPage = new ParameterEditorFormPage(this);
			defaultPages[5] = parameterFormPage;
			documentFormPage = new ROSDocumentEditorFormPage(this);
			defaultPages[6] = documentFormPage;
			containerFormPage = new ROSContainerEditorFormPage(this);
			defaultPages[7] = containerFormPage;
			rosXmlFormPage = new ROSXmlEditorFormPage(this);
			defaultPages[8] = rosXmlFormPage;
			//
			List<List<AbstractEditorFormPage>> forms = new ArrayList<List<AbstractEditorFormPage>>();
			forms.add(new ArrayList<AbstractEditorFormPage>());
			for (AbstractEditorFormPage p : defaultPages) {
				List<AbstractEditorFormPage> list = new ArrayList<AbstractEditorFormPage>();
				list.add(p);
				forms.add(list);
			}
			//
			for (List<AbstractEditorFormPage> list : forms) {
				for (AbstractEditorFormPage p : list) {
					addPage(p);
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
		} catch (PartInitException e) {
			throw new RuntimeException(e); // system error
		}
	}

	protected void allPagesReLoad(){
		if( lifecycleFormPage != null ) lifecycleFormPage.load();
		if( topicFormPage != null ) topicFormPage.load();
		if( serviceFormPage != null ) serviceFormPage.load();
		if( actionFormPage != null ) actionFormPage.load();
		if( parameterFormPage != null ) parameterFormPage.load();
		if( rosXmlFormPage != null ) rosXmlFormPage.load();
	}

	protected void allUpdates(){
		basicFormPage.update();
		topicFormPage.updateForOutput();
		serviceFormPage.update();
		parameterFormPage.updateForOutput();
		actionFormPage.update();
		lifecycleFormPage.update();
	}

	public void updateDataTypes() {
		if( topicFormPage != null ) topicFormPage.updateDefaultValue();
	}

	public void updatePages() {
		if( lifecycleFormPage != null ) lifecycleFormPage.load();
	}

	public String validateParam() {
		String result = null;
		for (int intIdx = 0; intIdx < this.pages.size(); intIdx++) {
			AbstractEditorFormPage page = (AbstractEditorFormPage) this.pages.get(intIdx);
			if (page == null) continue;
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
		boolean isRtcXml = getCurrentPage() == rosXmlFormPage.getIndex();
		this.allUpdates();
		RtcBuilderPlugin.getDefault().setCanExit(true);

		if (isRtcXml) {
			try {
				ProfileHandlerROS handler = new ProfileHandlerROS();
				if( !handler.validateROSXml(this.getROSParam().getROSXml()) ) return;
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
				ProfileHandlerROS handler = new ProfileHandlerROS();
				if( !handler.validateROSXml(handler.convert2ROSXML(this.getGeneratorParam())) ) return;
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
	        xmlFile = this.getROSParam().getROSXml();
		} else {
			ProfileHandlerROS handler = new ProfileHandlerROS();
			xmlFile = handler.convert2ROSXML(generatorParam);
		}
		progressMonitor.worked(15);
		//

		IProject projectHandle = file.getProject();
		try {
			IFile rosxml = projectHandle.getFile(IRtcBuilderConstantsROS.DEFAULT_ROS_XML);
			if(rosxml.exists()) rosxml.delete(true, null);
			rosxml.create(new ByteArrayInputStream(xmlFile.getBytes("UTF-8")), true, null);
			//
			//ISO
			ProfileHandlerROS handler = new ProfileHandlerROS();
			RosProfile rosProfile = handler.convert2XMLProfile(this.getROSParam());
			ROS2ISOProfileHandler isoHandler = new ROS2ISOProfileHandler();
			SIM isoProfile = isoHandler.convertROS2Iso(rosProfile);
			String isoFile = isoHandler.convertToXmlIso(isoProfile);
			
			IFile isoxml = projectHandle.getFile(IRtcBuilderConstants.DEFAULT_ISO_202_XML);
			if( isoxml.exists()) isoxml.delete(true, null);
			isoxml.create(new ByteArrayInputStream(isoFile.getBytes("UTF-8")), true, null);
			/////////
			//
			setInput(new FileEditorInput(rosxml));
			this.getROSParam().setROSXml(xmlFile);
			//
			// isDirty = false;
			// firePropertyChange(IEditorPart.PROP_DIRTY);
			getROSParam().resetUpdated();
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
		if( rosXmlFormPage != null ) rosXmlFormPage.load();
		title = projectHandle.getName();
		firePropertyChange(IEditorPart.PROP_TITLE);
		progressMonitor.done();
	}

	protected void updateProfiles(String xmlFile) throws Exception {
		// RTC.xmlの内容を他のページに反映
		ProfileHandlerROS handler = new ProfileHandlerROS();
		RosProfile module = handler.restorefromXMLROS(xmlFile);
		ParamUtilROS putil = new ParamUtilROS();
		getGeneratorParam().setROSParam(putil.convertFromROSModule(module, generatorParam, managerList));
		getROSParam().setROSXml(xmlFile);
		//
		if (basicFormPage != null) basicFormPage.load();
		if (lifecycleFormPage != null) lifecycleFormPage.load();
		if (topicFormPage != null) topicFormPage.load();
		if (serviceFormPage != null) serviceFormPage.load();
		if (actionFormPage != null) actionFormPage.load();
		if (parameterFormPage != null) parameterFormPage.load();
		//
		getROSParam().resetUpdated();
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
		setDirty(getROSParam().isUpdated());
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
	 * ROSParamを取得する
	 */
	public ROSParam getROSParam() {
		return generatorParam.getROSParam();
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

	public void updateEMFPorts(List<TopicParam> subscribes, List<TopicParam> publishes,
								List<ServiceParam> servers, List<ServiceParam> clients,
								List<ActionParam> actionServers, List<ActionParam> actionClients) {
		updateEMFTopicSubscribes(subscribes);
		updateEMFTopicPublishes(publishes);
		updateEMFService(servers, clients, actionServers, actionClients);
	}

	private void updateEMFTopicSubscribes(List<TopicParam> subscribes) {
		((Component)buildview.getComponents().get(0)).clearDataInports();
		int portIndex = 0;
		for(int intIdx=0; intIdx<subscribes.size();intIdx++ ) {
			DataInPort dataInport= ComponentFactory.eINSTANCE.createDataInPort();
			dataInport.setInPort_Name(subscribes.get(intIdx).getName());
			dataInport.setIndex(portIndex);
			portIndex++;
			dataInport.setDirection(PortDirection.LEFT_LITERAL);
			((Component)buildview.getComponents().get(0)).addDataInport(dataInport);
		}
	}

	private void updateEMFTopicPublishes(List<TopicParam> publishes) {
		((Component)buildview.getComponents().get(0)).clearDataOutports();
		int portIndex = 0;
		for(int intIdx=0; intIdx<publishes.size();intIdx++ ) {
			DataOutPort dataOutport= ComponentFactory.eINSTANCE.createDataOutPort();
			dataOutport.setOutPort_Name(publishes.get(intIdx).getName());
			dataOutport.setIndex(portIndex);
			portIndex++;
			dataOutport.setDirection(PortDirection.RIGHT_LITERAL);
			((Component)buildview.getComponents().get(0)).addDataOutport(dataOutport);
		}
	}

	private void updateEMFService(List<ServiceParam> services, List<ServiceParam> clients,
									List<ActionParam> actionServers, List<ActionParam> actionClients) {
		((Component)buildview.getComponents().get(0)).clearServiceports();
		for(int intIdx=0; intIdx<services.size();intIdx++ ) {
			ServiceParam srvParam = services.get(intIdx);
			ServicePort servicePort= ComponentFactory.eINSTANCE.createServicePort();
//			servicePort.setServicePort_Name(srvParam.getName());
			servicePort.setIndex(intIdx);
			servicePort.setDirection(PortDirection.LEFT_LITERAL);
			//
			ServiceInterface serviceIF = ComponentFactory.eINSTANCE.createServiceInterface();
			serviceIF.setDirection(InterfaceDirection.PROVIDED_LITERAL);
			serviceIF.setServiceInterface_Name(srvParam.getName());
			serviceIF.setParentDirection(servicePort.getDirection());
			serviceIF.setIndex(0);
			servicePort.addServiceInterface(serviceIF);
			((Component)buildview.getComponents().get(0)).addServiceport(servicePort);
		}
		
		for(int intIdx=0; intIdx<clients.size();intIdx++ ) {
			ServiceParam srvParam = clients.get(intIdx);
			ServicePort servicePort= ComponentFactory.eINSTANCE.createServicePort();
//			servicePort.setServicePort_Name(srvParam.getName());
			servicePort.setIndex(intIdx);
			servicePort.setDirection(PortDirection.RIGHT_LITERAL);
			//
			ServiceInterface serviceIF = ComponentFactory.eINSTANCE.createServiceInterface();
			serviceIF.setDirection(InterfaceDirection.REQUIRED_LITERAL);
			serviceIF.setServiceInterface_Name(srvParam.getName());
			serviceIF.setParentDirection(servicePort.getDirection());
			serviceIF.setIndex(0);
			servicePort.addServiceInterface(serviceIF);
			((Component)buildview.getComponents().get(0)).addServiceport(servicePort);
		}
		/////
		for(int intIdx=0; intIdx<actionServers.size();intIdx++ ) {
			ActionParam actionParam = actionServers.get(intIdx);
			ServicePort servicePort= ComponentFactory.eINSTANCE.createServicePort();
//			servicePort.setServicePort_Name(srvParam.getName());
			servicePort.setIndex(intIdx);
			servicePort.setPort_Type(IRtcBuilderConstants.Type_Event);
			servicePort.setDirection(PortDirection.LEFT_LITERAL);
			//
			ServiceInterface serviceIF = ComponentFactory.eINSTANCE.createServiceInterface();
			serviceIF.setDirection(InterfaceDirection.PROVIDED_LITERAL);
			serviceIF.setServiceInterface_Name(actionParam.getName());
			serviceIF.setParentDirection(servicePort.getDirection());
			serviceIF.setIndex(0);
			servicePort.addServiceInterface(serviceIF);
			((Component)buildview.getComponents().get(0)).addServiceport(servicePort);
		}
		
		for(int intIdx=0; intIdx<actionClients.size();intIdx++ ) {
			ActionParam actionParam = actionClients.get(intIdx);
			ServicePort servicePort= ComponentFactory.eINSTANCE.createServicePort();
//			servicePort.setServicePort_Name(srvParam.getName());
			servicePort.setIndex(intIdx);
			servicePort.setPort_Type(IRtcBuilderConstants.Type_Event);
			servicePort.setDirection(PortDirection.RIGHT_LITERAL);
			//
			ServiceInterface serviceIF = ComponentFactory.eINSTANCE.createServiceInterface();
			serviceIF.setDirection(InterfaceDirection.REQUIRED_LITERAL);
			serviceIF.setServiceInterface_Name(actionParam.getName());
			serviceIF.setParentDirection(servicePort.getDirection());
			serviceIF.setIndex(0);
			servicePort.addServiceInterface(serviceIF);
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
		setEnabledInfoByLang(getROSParam().getLanguage());
	}

	@SuppressWarnings("deprecation")
	public void setEnabledInfoByLang(String langName) {
	}
}
