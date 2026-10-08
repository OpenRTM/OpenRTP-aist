package jp.go.aist.rtm.rtcbuilder.ros;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.openrtp.namespaces.ros.version01.Action;
import org.openrtp.namespaces.ros.version01.ActionDoc;
import org.openrtp.namespaces.ros.version01.ActionExt;
import org.openrtp.namespaces.ros.version01.BasicInfo;
import org.openrtp.namespaces.ros.version01.BasicInfoDoc;
import org.openrtp.namespaces.ros.version01.BasicInfoExt;
import org.openrtp.namespaces.ros.version01.DocAction;
import org.openrtp.namespaces.ros.version01.DocBasic;
import org.openrtp.namespaces.ros.version01.DocLifecycleCallback;
import org.openrtp.namespaces.ros.version01.DocParameter;
import org.openrtp.namespaces.ros.version01.DocService;
import org.openrtp.namespaces.ros.version01.DocTopic;
import org.openrtp.namespaces.ros.version01.Language;
import org.openrtp.namespaces.ros.version01.LanguageExt;
import org.openrtp.namespaces.ros.version01.Library;
import org.openrtp.namespaces.ros.version01.LifeCycleExt;
import org.openrtp.namespaces.ros.version01.Lifecycle;
import org.openrtp.namespaces.ros.version01.LifecycleCallback;
import org.openrtp.namespaces.ros.version01.LifecycleCallbackExt;
import org.openrtp.namespaces.ros.version01.ObjectFactory;
import org.openrtp.namespaces.ros.version01.Parameter;
import org.openrtp.namespaces.ros.version01.ParameterDoc;
import org.openrtp.namespaces.ros.version01.ParameterExt;
import org.openrtp.namespaces.ros.version01.Property;
import org.openrtp.namespaces.ros.version01.RosProfile;
import org.openrtp.namespaces.ros.version01.Service;
import org.openrtp.namespaces.ros.version01.ServiceDoc;
import org.openrtp.namespaces.ros.version01.ServiceExt;
import org.openrtp.namespaces.ros.version01.TargetEnvironment;
import org.openrtp.namespaces.ros.version01.Timer;
import org.openrtp.namespaces.ros.version01.Topic;
import org.openrtp.namespaces.ros.version01.TopicDoc;
import org.openrtp.namespaces.ros.version01.TopicExt;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.container.param.ContainerParam;
import jp.go.aist.rtm.rtcbuilder.container.param.LibraryParam;
import jp.go.aist.rtm.rtcbuilder.container.param.RepositoryParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.GeneratorParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.ParamUtil;
import jp.go.aist.rtm.rtcbuilder.generator.param.PropertyParam;
import jp.go.aist.rtm.rtcbuilder.manager.GenerateManager;
import jp.go.aist.rtm.rtcbuilder.ros.param.ActionParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.PackageParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.ParameterParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.ROSParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.ServiceParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.TargetEnvParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.TimerParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.TopicParam;
import jp.go.aist.rtm.rtcbuilder.ros.ui.preference.ROSPreferenceManager;

public class ParamUtilROS extends ParamUtil {
	public static RosProfile initialROSXml() {
		ObjectFactory factory = new ObjectFactory();
		RosProfile profileType = factory.createRosProfile();
		profileType.setVersion(IRtcBuilderConstantsROS.SCHEMA_VERSION_ROS);

		org.openrtp.namespaces.ros.version01.BasicInfoExt basic = initBasicInfoROS(factory);
		profileType.setBasicInfo(basic);
		//
		Lifecycle lifeCycle = initLifecycle(factory);
		profileType.setLifeCycle(lifeCycle);

		return profileType;
	}
	
	private static BasicInfoExt initBasicInfoROS(ObjectFactory factory) {
		org.openrtp.namespaces.ros.version01.BasicInfoExt basic = factory.createBasicInfoExt();
		basic.setPackageName(ROSParam.DEFAULT_PACKAGE_NAME);
		basic.setNodeName(ROSParam.DEFAULT_NODE_NAME);
		basic.setDescription(ROSParam.DEFAULT_DESCRIPTION);
		basic.setVersion(ROSParam.DEFAULT_VERSION);
		basic.setCategory(ROSParam.DEFAULT_CATEGORY);

		basic.setMaintainer(ROSPreferenceManager.getMaintainerNameValue());

		org.openrtp.namespaces.ros.version01.DocBasic doc = factory.createDocBasic();
		doc.setContactAddress(ROSPreferenceManager.getMaintainerAddressValue());
		basic.setDoc(doc);

		return basic;
	}
	
	private static Lifecycle initLifecycle(ObjectFactory factory) {
		LifeCycleExt lifeCycle = factory.createLifeCycleExt();
		//
		LifecycleCallbackExt lifeCycleCallback = null;
		ArrayList<String> docs = ROSPreferenceManager.getDocumentValue();

		{
			lifeCycleCallback = factory.createLifecycleCallbackExt();
			lifeCycleCallback.setImplemented(true);
			lifeCycle.setOnConfigure(lifeCycleCallback);
		}
		{
			lifeCycleCallback = factory.createLifecycleCallbackExt();
			lifeCycleCallback.setImplemented(true);
			lifeCycle.setOnActivate(lifeCycleCallback);
		}
		{
			lifeCycleCallback = factory.createLifecycleCallbackExt();
			lifeCycleCallback.setImplemented(true);
			lifeCycle.setOnDeactivate(lifeCycleCallback);
		}
		{
			lifeCycleCallback = factory.createLifecycleCallbackExt();
			if(0 < docs.size()) {
				lifeCycleCallback.setImplemented(Boolean.valueOf(docs.get(0)));
			} else {
				lifeCycleCallback.setImplemented(false);
			}
			lifeCycle.setOnCleanup(lifeCycleCallback);
		}
		{
			lifeCycleCallback = factory.createLifecycleCallbackExt();
			if(1 < docs.size()) {
				lifeCycleCallback.setImplemented(Boolean.valueOf(docs.get(1)));
			} else {
				lifeCycleCallback.setImplemented(false);
			}
			lifeCycle.setOnShutdown(lifeCycleCallback);
		}
		{
			lifeCycleCallback = factory.createLifecycleCallbackExt();
			if(2 < docs.size()) {
				lifeCycleCallback.setImplemented(Boolean.valueOf(docs.get(2)));
			} else {
				lifeCycleCallback.setImplemented(false);
			}
			lifeCycle.setOnError(lifeCycleCallback);
		}

		return lifeCycle;
	}
	public ROSParam convertFromROSModule(RosProfile profile, GeneratorParam generatorParam,
			List<GenerateManager> managerList) throws Exception {
		return convertFromROSModule(profile, generatorParam, managerList, false);

	}
	public ROSParam convertFromROSModule(RosProfile profile, GeneratorParam generatorParam,
			List<GenerateManager> managerList, boolean isDirect) throws Exception {
		ROSParam rosParam = new ROSParam(generatorParam);
		
		rosParam.setSchemaVersion(profile.getVersion());
		
		convertFromModuleBasic(profile, rosParam);
		convertFromModuleLifecycle(profile, rosParam);
		if( profile.getTopics() != null ) {
			createTopicParam(profile.getTopics(), rosParam);
		}
		if( profile.getServices() != null ) {
			createServiceParam(profile.getServices(), rosParam);
		}
		if( profile.getActions() != null ) {
			createActionParam(profile.getActions(), rosParam);
		}
		if( profile.getParameters() != null ) {
			createParameterParam(profile.getParameters(), rosParam);
		}
		convertFromModuleLanguage(profile, managerList, rosParam);
		//
		return rosParam;
	}
	
	private void convertFromModuleBasic(RosProfile profile, ROSParam rosParam) {
		BasicInfo basic = profile.getBasicInfo();

		rosParam.setPackageName(basic.getPackageName());
		rosParam.setNodeName(basic.getNodeName());
		rosParam.setClassName(basic.getClassName());
		rosParam.setDescription(basic.getDescription());
		rosParam.setVersion(basic.getVersion());
		rosParam.setMaintainer(basic.getMaintainer());
		rosParam.setCategory(basic.getCategory());
		
		BasicInfoDoc basicDoc = (BasicInfoDoc)profile.getBasicInfo();
		DocBasic docbasic = basicDoc.getDoc();
		if( docbasic != null ) {
			rosParam.setDocInOut(docbasic.getInout());
			rosParam.setDocAlgorithm(docbasic.getAlgorithm());
			rosParam.setDocCreator(docbasic.getCreator());
			rosParam.setDocReference(docbasic.getReference());
			rosParam.setLicense(docbasic.getLicense());
			rosParam.setContactAddress(docbasic.getContactAddress());
		}
		//Ext Basic
		if(profile.getBasicInfo() instanceof BasicInfoExt) {
			BasicInfoExt basicExt = (BasicInfoExt)profile.getBasicInfo();
			rosParam.setOutputProject(basicExt.getSaveProject());
			//Basic Properties
			for( Property prop : basicExt.getProperties() ) {
				PropertyParam propParam = new PropertyParam();
				propParam.setName(prop.getName());
				propParam.setValue(prop.getValue());
				rosParam.getProperties().add(propParam);
			}
		}
	}

	private void convertFromModuleLifecycle(RosProfile profile, ROSParam rosParam) {
		Lifecycle lifecycle = profile.getLifeCycle();
		
		convertFromModuleLifecycleCallback(IRtcBuilderConstantsROS.ACTIVITY_CONFIGURE,
				lifecycle.getOnConfigure(), rosParam);
		convertFromModuleLifecycleCallback(IRtcBuilderConstantsROS.ACTIVITY_ACTIVATE,
				lifecycle.getOnActivate(), rosParam);
		convertFromModuleLifecycleCallback(IRtcBuilderConstantsROS.ACTIVITY_DEACTIVATE,
				lifecycle.getOnDeactivate(), rosParam);
		convertFromModuleLifecycleCallback(IRtcBuilderConstantsROS.ACTIVITY_CLEANUP,
				lifecycle.getOnCleanup(), rosParam);
		convertFromModuleLifecycleCallback(IRtcBuilderConstantsROS.ACTIVITY_SHUTDOWN,
				lifecycle.getOnShutdown(), rosParam);
		convertFromModuleLifecycleCallback(IRtcBuilderConstantsROS.ACTIVITY_ERROR,
				lifecycle.getOnError(), rosParam);
		
		if(lifecycle instanceof LifeCycleExt) {
			LifeCycleExt lifecycleExt = (LifeCycleExt)lifecycle;
			rosParam.getTimers().clear();
			for(Timer each : lifecycleExt.getTimers()) {
				TimerParam elem = new TimerParam();
				elem.setName(each.getTimerName());
				elem.setRate(each.getRate());
				elem.setCallBack(each.getCallBack());
				elem.setDescription(each.getDescription());
				rosParam.getTimers().add(elem);
			}
		}
	}
	
	private void convertFromModuleLifecycleCallback(int callbackId, LifecycleCallback callback, ROSParam rosParam) {
		if(callback != null) {
			rosParam.setActionImplemented(callbackId, callback.isImplemented());
			if(callback instanceof LifecycleCallbackExt) {
				LifecycleCallbackExt callbackExt = (LifecycleCallbackExt)callback;
				DocLifecycleCallback callbackDoc = callbackExt.getDoc();
				if(callbackDoc != null) {
					rosParam.setDocActionOverView(callbackId, callbackDoc.getDescription());
					rosParam.setDocActionPreCondition(callbackId, callbackDoc.getPreCondition());
					rosParam.setDocActionPostCondition(callbackId, callbackDoc.getPostCondition());
				}
			}
		}
	}
	
	private void createTopicParam(List<Topic> topics, ROSParam rosParam) throws Exception {
		List<TopicParam> subscribeList = new ArrayList<TopicParam>();
		List<TopicParam> publishList = new ArrayList<TopicParam>();
		
		for( Topic topic : topics ) {
			TopicParam topicp = new TopicParam();
			topicp.setRole(topic.getTopicRole());
			topicp.setName(topic.getTopicName());
			topicp.setMessageType(topic.getMessageType());
			topicp.setReliabilityType(topic.getQoSReliabilityType());
			topicp.setHistoryType(topic.getQoSHistoryType());
			topicp.setDepth(Integer.valueOf(topic.getQoSHistoryDepth().toString()));
			topicp.setVarCallbackName(topic.getVariableCallbackName());

			if(topic instanceof TopicDoc) {
				TopicDoc topicDoc = (TopicDoc)topic;
				DocTopic docTopic = topicDoc.getDoc();
				if( docTopic!=null ) {
					topicp.setDocDescription(docTopic.getDescription());
					topicp.setDocType(docTopic.getType());
					topicp.setDocSemantics(docTopic.getSemantics());
					topicp.setDocUnit(docTopic.getUnit());
					topicp.setDocOccurrence(docTopic.getOccurrence());
				}
			}
			if(topic instanceof TopicExt) {
				TopicExt topicExt = (TopicExt)topic;
				//Properties
				for( Property prop : topicExt.getProperties() ) {
					PropertyParam propParam = new PropertyParam();
					propParam.setName(prop.getName());
					propParam.setValue(prop.getValue());
					topicp.getProperties().add(propParam);
				}
			}
			//
			if(topic.getTopicRole().equals(IRtcBuilderConstantsROS.SPEC_TOPIC_SUBSCRIBE) )
				subscribeList.add(topicp);
			else
				publishList.add(topicp);
		}
		rosParam.getTopicSubscribes().clear();
		rosParam.getTopicSubscribes().addAll(subscribeList);
		rosParam.getTopicPublishes().clear();
		rosParam.getTopicPublishes().addAll(publishList);
	}
	
	private void createServiceParam(List<Service> services, ROSParam rosParam) {
		List<ServiceParam> serviceList = new ArrayList<ServiceParam>();
		List<ServiceParam> clientList = new ArrayList<ServiceParam>();

		for( Service service : services ) {
			ServiceParam servicep = new ServiceParam();
			servicep.setRole(service.getServiceRole());
			servicep.setName(service.getServiceName());
			servicep.setType(service.getServiceType());
			servicep.setVarCallbackName(service.getVariableCallbackName());
			if(service instanceof ServiceDoc) {
				ServiceDoc serviceDoc = (ServiceDoc)service;
				DocService doc = serviceDoc.getDoc();
				if( doc != null ) {
					servicep.setDocDescription(doc.getDescription());
					servicep.setDocArgument(doc.getArgument());
					servicep.setDocReturn(doc.getReturn());
				}
			}
			if(service instanceof ServiceExt) {
				ServiceExt serviceExt = (ServiceExt)service;
				//Properties
				for( Property prop : serviceExt.getProperties() ) {
					PropertyParam propParam = new PropertyParam();
					propParam.setName(prop.getName());
					propParam.setValue(prop.getValue());
					servicep.getProperties().add(propParam);
				}
			}
			//
			if(service.getServiceRole().equals(IRtcBuilderConstantsROS.SPEC_SERVICE_SERVER) )
				serviceList.add(servicep);
			else
				clientList.add(servicep);
		}
		rosParam.getServiceServers().clear();
		rosParam.getServiceServers().addAll(serviceList);
		rosParam.getServiceClients().clear();
		rosParam.getServiceClients().addAll(clientList);
	}

	private void createActionParam(List<Action> actions, ROSParam rosParam) {
		List<ActionParam> serviceList = new ArrayList<ActionParam>();
		List<ActionParam> clientList = new ArrayList<ActionParam>();

		for( Action action : actions ) {
			ActionParam actionp = new ActionParam();
			actionp.setRole(action.getActionRole());
			actionp.setName(action.getActionName());
			actionp.setType(action.getActionType());
			actionp.setCallbackName(action.getCallbackBase());
			if(action instanceof ActionDoc) {
				ActionDoc actionDoc = (ActionDoc)action;
				DocAction doc = actionDoc.getDoc();
				if( doc != null ) {
					actionp.setDocDescription(doc.getDescription());
					actionp.setDocGoal(doc.getGoal());
					actionp.setDocFeedback(doc.getFeedback());
					actionp.setDocResult(doc.getResult());
				}
			}
			if(action instanceof ActionExt) {
				ActionExt actionExt = (ActionExt)action;
				//Properties
				for( Property prop : actionExt.getProperties() ) {
					PropertyParam propParam = new PropertyParam();
					propParam.setName(prop.getName());
					propParam.setValue(prop.getValue());
					actionp.getProperties().add(propParam);
				}
			}
			//
			if(action.getActionRole().equals(IRtcBuilderConstantsROS.SPEC_ACTION_SERVER) )
				serviceList.add(actionp);
			else
				clientList.add(actionp);
		}
		rosParam.getActionServers().clear();
		rosParam.getActionServers().addAll(serviceList);
		rosParam.getActionClients().clear();
		rosParam.getActionClients().addAll(clientList);
	}

	private void createParameterParam(List<Parameter> params, ROSParam rosParam) {
		for( Parameter param : params ) {
			ParameterParam paramp = new ParameterParam();
			paramp.setName(param.getParameterName());
			paramp.setType(param.getDataType());
			paramp.setDefaultValue(param.getDefaultValue());
			paramp.setReadOnly(param.isReadOnly());
			
			String constraint = param.getConstraint();
			String[] elems = constraint.split("\\|");
			if(0<elems.length) {
				double val = 0.0;
				val = Double.parseDouble(elems[0]);
				paramp.setMin(val);
			}
			if(1<elems.length) {
				double val = 0.0;
				val = Double.parseDouble(elems[1]);
				paramp.setMax(val);
			}
			if(2<elems.length) {
				double val = 0.0;
				val = Double.parseDouble(elems[2]);
				paramp.setStep(val);
			}
			
			if( param instanceof ParameterDoc ) {
				ParameterDoc paramDoc = (ParameterDoc)param;
				DocParameter docParam = paramDoc.getDoc();
				if( docParam!=null ) {
					paramp.setDocDescription(docParam.getDescription());
					paramp.setDocDataname(docParam.getDataname());
					paramp.setDocDefault(docParam.getDefaultValue());
					paramp.setDocUnit(docParam.getUnit());
					paramp.setDocRange(docParam.getRange());
					paramp.setDocConstraint(docParam.getConstraint());
				}
			}
			//Properties
			if( param instanceof ParameterExt ) {
				ParameterExt paramExt = (ParameterExt)param;
				for( Property prop : paramExt.getProperties() ) {
					PropertyParam propParam = new PropertyParam();
					propParam.setName(prop.getName());
					propParam.setValue(prop.getValue());
					paramp.getProperties().add(propParam);
				}
			}
			rosParam.getParameters().add(paramp);
		}
	}

	private void convertFromModuleLanguage(RosProfile profile, List<GenerateManager> managerList, ROSParam rosParam) {
		Language language = profile.getLanguage();
		if (language != null) {
			String langKind = language.getKind();
			if (isCxx(langKind)) {
				rosParam.getLangList().clear();
				rosParam.getLangList().add(IRtcBuilderConstants.LANG_CPP);
			} else {
				if (managerList != null) {
					for (GenerateManager manager : managerList) {
						manager.convertProfile(profile);
						language = profile.getLanguage();
						langKind = language.getKind();
						if (langKind.trim().equals(manager.getManagerKey())) {
							rosParam.getLangList().clear();
							rosParam.getLangList().add(manager.getManagerKey());
							rosParam.getLangArgList().clear();
							rosParam.getLangArgList().add(manager.getLangArgList());
							break;
						}
					}
				}
			}
			if( language instanceof LanguageExt ) {
				LanguageExt langExt = (LanguageExt)language;
				TargetEnvironment target = langExt.getTargets();
				TargetEnvParam env = new TargetEnvParam();
				env.setRosVersion(target.getRosVersion());
				//
				for( Library lib : target.getLibraries()) {
					PackageParam libParam = new PackageParam();
					libParam.setName(lib.getName());
					libParam.setVersion(lib.getVersion());
					libParam.setOther(lib.getOther());
					env.getLibraries().add(libParam);
				}
				rosParam.setTargetEnv(env);
				//////
				String strKey = IRtcBuilderConstants.CONTAINER_PREFIX + "middleware_";
				List<Property> middlewares = langExt.getProperties().stream()
						.filter(p -> p.getName().toLowerCase().startsWith(strKey))
						.collect(Collectors.toList());
				for(Property each : middlewares) {
					String[] elems = each.getName().split("_");
					String strIdx = elems[elems.length-1];
					
					ContainerParam param = new ContainerParam();
					param.setMiddleware(each.getValue());
					param.setMdlVersion(getTargetPropertyValue(langExt.getProperties(), "mdl_version_"+ strIdx));
					param.setOsVersion(getTargetPropertyValue(langExt.getProperties(), "os_version_"+ strIdx));
					param.setWorkspace(getTargetPropertyValue(langExt.getProperties(), "workspace_"+ strIdx));
					param.setLanguage(getTargetPropertyValue(langExt.getProperties(), "language_"+ strIdx));
					param.setConfiguration(getTargetPropertyValue(langExt.getProperties(), "configration_"+ strIdx));
					
					List<Property> libs = getTargetProperty(langExt.getProperties(), IRtcBuilderConstants.CONTAINER_PREFIX + "lib_"+ strIdx);
					for(Property lib : libs) {
						String[] libElems = lib.getValue().split("\\|");
						LibraryParam libParam = new LibraryParam();
						if(0 < libElems.length) {
							libParam.setName(libElems[0]);
						}
						if(1 < libElems.length) {
							libParam.setInstaller(libElems[1]);
						}
						if(2 < libElems.length) {
							libParam.setCanUpdate(Boolean.valueOf(libElems[2]).booleanValue());
						}
						param.getLibraries().add(libParam);
					}
					
					List<Property> repos = getTargetProperty(langExt.getProperties(), IRtcBuilderConstants.CONTAINER_PREFIX + "giturl_"+ strIdx);
					for(Property repo : repos) {
						String[] repoElems = repo.getValue().split("\\|");
						RepositoryParam repoParam = new RepositoryParam();
						if(0 < repoElems.length) {
							repoParam.setURL(repoElems[0]);
						}
						if(1 < repoElems.length) {
							repoParam.setBranch(repoElems[1]);
						}
						param.getRepositories().add(repoParam);
					}
					
					List<Property> preSets = getTargetProperty(langExt.getProperties(), IRtcBuilderConstants.CONTAINER_PREFIX + "category_"+ strIdx);
					for(Property preSet : preSets) {
						param.getPreSets().add(preSet.getValue());
					}

					rosParam.getContainerSettings().add(param);
				}
			}
		}
	}
	private String getTargetPropertyValue(List<Property> propList, String key) {
		String elemKey = IRtcBuilderConstants.CONTAINER_PREFIX + key;
		List<Property> filtered = getTargetProperty(propList, elemKey);
		if(filtered.size() == 1) return filtered.get(0).getValue();
		return "";
	}
	
	private List<Property> getTargetProperty(List<Property> propList, String key) {
		List<Property> filtered = propList.stream()
									.filter(p -> p.getName().toLowerCase().equals(key))
									.collect(Collectors.toList());
		return filtered;
	}
	//////////
	public RosProfile convertToROSModule(GeneratorParam generatorParam, List<GenerateManager> managerList) throws Exception {
		ROSParam rosParam = generatorParam.getROSParam();
		return convertToROSModule(rosParam, managerList);
	}

	public RosProfile convertToROSModule(ROSParam target, List<GenerateManager> managerList) throws Exception {
		org.openrtp.namespaces.ros.version01.ObjectFactory factory = new org.openrtp.namespaces.ros.version01.ObjectFactory();
		RosProfile profile = factory.createRosProfile();
		profile.setVersion(target.getSchemaVersion());
		convertToModuleBasicROS(target, factory, profile);
		convertToModuleLifecycle(target, factory, profile);

		for( TopicParam topicp : target.getTopicSubscribes() ) {
			profile.getTopics().add(createTopic(topicp, IRtcBuilderConstantsROS.SPEC_TOPIC_SUBSCRIBE));
		}
		for( TopicParam topicp : target.getTopicPublishes() ) {
			profile.getTopics().add(createTopic(topicp, IRtcBuilderConstantsROS.SPEC_TOPIC_PUBLISH));
		}
		
		for( ServiceParam servicep : target.getServiceServers() ) {
			profile.getServices().add(createService(servicep, IRtcBuilderConstantsROS.SPEC_SERVICE_SERVER));
		}
		for( ServiceParam servicep : target.getServiceClients() ) {
			profile.getServices().add(createService(servicep, IRtcBuilderConstantsROS.SPEC_SERVICE_CLIENT));
		}

		for( ActionParam actionp : target.getActionServers() ) {
			profile.getActions().add(createAction(actionp, IRtcBuilderConstantsROS.SPEC_ACTION_SERVER));
		}
		for( ActionParam actionp : target.getActionClients() ) {
			profile.getActions().add(createAction(actionp, IRtcBuilderConstantsROS.SPEC_ACTION_CLIENT));
		}
		
		for(ParameterParam paramp : target.getParameters()) {
			profile.getParameters().add(createParameter(paramp));
		}
		convertToModuleLanguage(managerList, target, factory, profile);
		
		LanguageExt lang = (LanguageExt)profile.getLanguage();
		for(int index=0; index<target.getContainerSettings().size(); index++) {
			ContainerParam param = target.getContainerSettings().get(index);
			
			addContainerInfo(factory, "middleware_" + index, param.getMiddleware(), lang);
			addContainerInfo(factory, "mdl_version_" + index, param.getMdlVersion(), lang);
			addContainerInfo(factory, "os_version_" + index, param.getOsVersion(), lang);
			addContainerInfo(factory, "workspace_" + index, param.getWorkspace(), lang);
			addContainerInfo(factory, "language_" + index, param.getLanguage(), lang);
			addContainerInfo(factory, "configration_" + index, param.getConfiguration(), lang);
			for(LibraryParam lib : param.getLibraries() ) {
				String strVal = lib.getName() + "|" + lib.getInstaller() + "|" + lib.canUpdate();
				addContainerInfo(factory, "lib_" + index, strVal, lang);
				
			}
			for(RepositoryParam rep : param.getRepositories() ) {
				String strVal = rep.getURL() + "|" + rep.getBranch();
				addContainerInfo(factory, "giturl_" + index, strVal, lang);
				
			}
			for(String each : param.getPreSets()) {
				addContainerInfo(factory, "category_" + index, each, lang);
			}
		}

		return profile;
	}
	
	private void addContainerInfo(org.openrtp.namespaces.ros.version01.ObjectFactory factory,
									String key, String value, LanguageExt lang) {
		String strKey = IRtcBuilderConstants.CONTAINER_PREFIX + key;
		Property prop = factory.createProperty();
		prop.setName(strKey);
		prop.setValue(value);
		lang.getProperties().add(prop);
	}
	
	private void convertToModuleBasicROS(ROSParam param, org.openrtp.namespaces.ros.version01.ObjectFactory factory, RosProfile profile) {
		org.openrtp.namespaces.ros.version01.BasicInfoExt basic = factory.createBasicInfoExt();
		basic.setPackageName(param.getPackageName());
		basic.setNodeName(param.getNodeName());
		basic.setClassName(param.getClassName());
		basic.setDescription(param.getDescription());
		basic.setVersion(param.getVersion());
		basic.setMaintainer(param.getMaintainer());
		basic.setCategory(param.getCategory());
		//Doc Basic
		org.openrtp.namespaces.ros.version01.DocBasic docbasic = factory.createDocBasic();
		docbasic.setInout(param.getDocInOut());
		docbasic.setAlgorithm(param.getDocAlgorithm());
		docbasic.setCreator(param.getDocCreator());
		docbasic.setLicense(param.getLicense());
		docbasic.setContactAddress(param.getContactAddress());
		docbasic.setReference(param.getDocReference());
		basic.setDoc(docbasic);
		//Ext Basic
		basic.setSaveProject(param.getOutputProject());
		//Properties
		for( PropertyParam prop : param.getProperties() ) {
			org.openrtp.namespaces.ros.version01.Property basicProp = factory.createProperty();
			basicProp.setName(prop.getName());
			basicProp.setValue(prop.getValue());
			basic.getProperties().add(basicProp);
		}
		profile.setBasicInfo(basic);
	}

	private void convertToModuleLifecycle(ROSParam param, ObjectFactory factory, RosProfile profile) {
		LifeCycleExt lifecycle = factory.createLifeCycleExt();
		lifecycle.setOnConfigure(createLifecycleCallback(IRtcBuilderConstantsROS.ACTIVITY_CONFIGURE, factory, param));
		lifecycle.setOnActivate(createLifecycleCallback(IRtcBuilderConstantsROS.ACTIVITY_ACTIVATE, factory, param));
		lifecycle.setOnDeactivate(createLifecycleCallback(IRtcBuilderConstantsROS.ACTIVITY_DEACTIVATE, factory, param));
		lifecycle.setOnCleanup(createLifecycleCallback(IRtcBuilderConstantsROS.ACTIVITY_CLEANUP, factory, param));
		lifecycle.setOnShutdown(createLifecycleCallback(IRtcBuilderConstantsROS.ACTIVITY_SHUTDOWN, factory, param));
		lifecycle.setOnError(createLifecycleCallback(IRtcBuilderConstantsROS.ACTIVITY_ERROR, factory, param));
		
		for(TimerParam each : param.getTimers()) {
			Timer timer = factory.createTimer();
			timer.setTimerName(each.getName());
			timer.setRate(each.getRate());
			timer.setCallBack(each.getCallBack());
			timer.setDescription(each.getDescription());
			lifecycle.getTimers().add(timer);
		}
		
		profile.setLifeCycle(lifecycle);
	}
	
	private LifecycleCallbackExt createLifecycleCallback(int actionId, ObjectFactory factory, ROSParam param) {
		LifecycleCallbackExt status = factory.createLifecycleCallbackExt();
		DocLifecycleCallback docCallback = factory.createDocLifecycleCallback();
		//
		docCallback.setDescription(param.getDocActionOverView(actionId));
		docCallback.setPreCondition(param.getDocActionPreCondition(actionId));
		docCallback.setPostCondition(param.getDocActionPostCondition(actionId));
		if( checkNotNull(docCallback.getDescription()) ||
			 checkNotNull(docCallback.getPreCondition()) ||
			 checkNotNull(docCallback.getPostCondition()) ) {
				status.setDoc(docCallback);
		}
		//
		status.setImplemented(param.getActionImplemented(actionId));
		//
		return status;
	}
	
	private TopicExt createTopic(TopicParam topicp, String topicType) throws Exception {
		ObjectFactory factory = new ObjectFactory();
		TopicExt topic = factory.createTopicExt();
		topic.setTopicRole(topicType);
		topic.setTopicName(topicp.getName());
		topic.setMessageType(topicp.getMessageType());
		topic.setQoSReliabilityType(topicp.getReliabilityType());
		topic.setQoSHistoryType(topicp.getHistoryType());
		topic.setQoSHistoryDepth(BigInteger.valueOf(topicp.getDepth()));
		topic.setVariableCallbackName(topicp.getVarCallbackName());
		//
		DocTopic doctopic = factory.createDocTopic();
		doctopic.setDescription(topicp.getDocDescription());
		doctopic.setType(topicp.getDocType());
		doctopic.setSemantics(topicp.getDocSemantics());
		doctopic.setUnit(topicp.getDocUnit());
		doctopic.setOccurrence(topicp.getDocOccurrence());
		if( checkNotNull(doctopic.getDescription()) ||
			 checkNotNull(doctopic.getType()) ||
			 checkNotNull(doctopic.getSemantics()) ||
			 checkNotNull(doctopic.getUnit()) ||
			 checkNotNull(doctopic.getOccurrence()) ) {
			topic.setDoc(doctopic);
		}
		//
		//Properties
		for( PropertyParam prop : topicp.getProperties() ) {
			Property dpProp = factory.createProperty();
			dpProp.setName(prop.getName());
			dpProp.setValue(prop.getValue());
			topic.getProperties().add(dpProp);
		}
		//
		return topic;
	}

	private ServiceExt createService(ServiceParam servicep, String serviceType) {
		ObjectFactory factory = new ObjectFactory();
		ServiceExt serviceport = factory.createServiceExt();
		serviceport.setServiceRole(serviceType);
		serviceport.setServiceName(servicep.getName());
		serviceport.setServiceType(servicep.getType());
		serviceport.setVariableCallbackName(servicep.getVarCallbackName());
		//
		DocService docservice = factory.createDocService();
		docservice.setDescription(servicep.getDocDescription());
		docservice.setArgument(servicep.getDocArgument());
		docservice.setReturn(servicep.getDocReturn());
		if( checkNotNull(servicep.getDocDescription())
				|| checkNotNull(servicep.getDocArgument())
				|| checkNotNull(servicep.getDocReturn()) ) {
				 serviceport.setDoc(docservice);
		}
		//Properties
		for( PropertyParam prop : servicep.getProperties() ) {
			Property srvProp = factory.createProperty();
			srvProp.setName(prop.getName());
			srvProp.setValue(prop.getValue());
			serviceport.getProperties().add(srvProp);
		}
		return serviceport;
	}

	private ActionExt createAction(ActionParam actionp, String actionType) {
		ObjectFactory factory = new ObjectFactory();
		ActionExt actionport = factory.createActionExt();
		actionport.setActionRole(actionType);
		actionport.setActionName(actionp.getName());
		actionport.setActionType(actionp.getType());
		actionport.setCallbackBase(actionp.getCallbackName());
		//
		DocAction docaction = factory.createDocAction();
		docaction.setDescription(actionp.getDocDescription());
		docaction.setGoal(actionp.getDocGoal());
		docaction.setFeedback(actionp.getDocFeedback());
		docaction.setResult(actionp.getDocResult());
		if( checkNotNull(actionp.getDocDescription())
				|| checkNotNull(actionp.getDocGoal())
				|| checkNotNull(actionp.getDocFeedback())
				|| checkNotNull(actionp.getDocResult())) {
			actionport.setDoc(docaction);
		}
		//Properties
		for( PropertyParam prop : actionp.getProperties() ) {
			Property srvProp = factory.createProperty();
			srvProp.setName(prop.getName());
			srvProp.setValue(prop.getValue());
			actionport.getProperties().add(srvProp);
		}
		return actionport;
	}

	private ParameterExt createParameter(ParameterParam paramp) {
		ObjectFactory factory = new ObjectFactory();
		ParameterExt param = factory.createParameterExt();
		param.setParameterName(paramp.getName());
		param.setDataType(paramp.getType());
		param.setDefaultValue(paramp.getDefaultValue());
		param.setReadOnly(paramp.isReadOnly());
		StringBuilder builder = new StringBuilder();
		builder.append(paramp.getMin()).append("|");
		builder.append(paramp.getMax()).append("|");
		builder.append(paramp.getStep());
		param.setConstraint(builder.toString());
		//
		for(PropertyParam propp : paramp.getProperties() ) {
			if( propp.getValue()!=null && propp.getValue().length()>0 ) {
				Property prop = factory.createProperty();
				prop.setName(propp.getName());
				prop.setValue(propp.getValue());
				param.getProperties().add(prop);
			}
		}
		//
		DocParameter docParam = factory.createDocParameter();
		docParam.setDescription(paramp.getDocDescription());
		docParam.setDataname(paramp.getDocDataname());
		docParam.setDefaultValue(paramp.getDocDefault());
		docParam.setUnit(paramp.getDocUnit());
		docParam.setRange(paramp.getDocRange());
		docParam.setConstraint(paramp.getDocConstraint());
		if( checkNotNull(paramp.getDocDataname()) ||
			 checkNotNull(paramp.getDocDefault()) ||
			 checkNotNull(paramp.getDocDescription()) ||
			 checkNotNull(paramp.getDocUnit()) ||
			 checkNotNull(paramp.getDocRange()) ||
			 checkNotNull(paramp.getDocConstraint()) ) {
			param.setDoc(docParam);
		}
		return param;
	}

	private void convertToModuleLanguage(List<GenerateManager> managerList, ROSParam param, ObjectFactory factory, RosProfile profile) {
		LanguageExt language = factory.createLanguageExt();
		for( String languagep : param.getLangList() ) {
			if(languagep.equals(IRtcBuilderConstants.LANG_CPP)) {
				language.setKind(IRtcBuilderConstants.LANG_CPP);
			} else {
				if( managerList != null ) {
					for( Iterator<GenerateManager> iter = managerList.iterator(); iter.hasNext(); ) {
						GenerateManager manager = iter.next();
						if( languagep.trim().equals(manager.getManagerKey())) {
							language.setKind(manager.getManagerKey());
							break;
						}
					}
				}
			}
		}
		//
		if( param.getTargetEnv() != null ) {
			TargetEnvParam target = param.getTargetEnv(); 
			TargetEnvironment env = factory.createTargetEnvironment();
			env.setRosVersion(target.getRosVersion());
			//
			for( PackageParam library : target.getLibraries() ) {
				Library lib = factory.createLibrary();
				lib.setName(library.getName());
				lib.setVersion(library.getVersion());
				lib.setOther(library.getOther());
				env.getLibraries().add(lib);
			}
			language.setTargets(env);
		}
		profile.setLanguage(language);
	}

}
