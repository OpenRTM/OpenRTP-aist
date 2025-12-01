package jp.go.aist.rtm.rtcbuilder.generator.param;

import static jp.go.aist.rtm.toolscommon.profiles.util.XmlHandler.createXMLGregorianCalendar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.fsm.EventParam;
import jp.go.aist.rtm.rtcbuilder.fsm.StateParam;
import jp.go.aist.rtm.rtcbuilder.fsm.TransitionParam;
import jp.go.aist.rtm.rtcbuilder.generator.ProfileHandler;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.IdlFileParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.IdlPathParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.ServiceClassParam;

/**
 * RTCを表すクラス
 */
public class RtcParam extends AbstractRecordedParam implements Serializable {

	private static final long serialVersionUID = -1249129059979166069L;

	private GeneratorParam parent;

	private String schemaVersion;
	//基本
	private String abstractDesc;
	private String name;
	private String category;
	private String creationDate;
	private String description;
	private String version;
	private String vender;
	private String componentType;
	private String activityType;
	private String componentKind = "DataFlowComponent";
	private int maxInstance;
	private String updateDate;
	private RecordedList<String> versionUpLog = new RecordedList<String>();
	private String executionType;
	private double executionRate;
	private String rtcType;
	private String currentVULog;
	//
	private RecordedList<DataPortParam> inports = new RecordedList<DataPortParam>();
	private RecordedList<DataPortParam> outports = new RecordedList<DataPortParam>();
	private RecordedList<EventPortParam> eventports = new RecordedList<EventPortParam>();
	//
	private RecordedList<ServicePortParam> serviceports = new RecordedList<ServicePortParam>();
	//
	private RecordedList<ServiceClassParam> serviceClassParams = new RecordedList<ServiceClassParam>();
	//コンフィギュレーション
	private RecordedList<ConfigSetParam> configParams = new RecordedList<ConfigSetParam>();
	//言語・環境
	private RecordedList<String> langList = new RecordedList<String>();
	private RecordedList<String> langArgList = new RecordedList<String>();
	private RecordedList<String> libraryPath = new RecordedList<String>();
	private String architecture = new String();
	private RecordedList<TargetEnvParam> targetEnvs = new RecordedList<TargetEnvParam>();
	//RTC.xml
	private String rtcxml;
	//ドキュメント
	private String doc_description;
	private String doc_in_out;
	private String doc_algorithm;
	//
	private RecordedList<ActionsParam> actions;
	//FSM
	private StateParam fsmParam;
	private String fsmContents;
	//
	private String doc_creator;
	private String doc_license;
	private String doc_reference;
	//Properties
	private RecordedList<PropertyParam> properties = new RecordedList<PropertyParam>();
	//
	private String outputProject = null;

	private Map extentionData = new HashMap();

	private List<IdlFileParam> providerIdlPathes = new ArrayList<IdlFileParam>();
	private List<IdlFileParam> consumerIdlPathes = new ArrayList<IdlFileParam>();
	private List<String> originalProviderIdls = new ArrayList<String>();
	private List<String> originalConsumerIdls = new ArrayList<String>();

	private List<String> includedIdls = new ArrayList<String>();
	private List<IdlPathParam> idlPathes = new ArrayList<IdlPathParam>();

	private RecordedList<String> privateAttributes = new RecordedList<String>();
	private RecordedList<String> protectedAttributes = new RecordedList<String>();
	private RecordedList<String> publicAttributes = new RecordedList<String>();
	private RecordedList<String> privateOperations = new RecordedList<String>();
	private RecordedList<String> protectedOperations = new RecordedList<String>();
	private RecordedList<String> publicOperations = new RecordedList<String>();
	//
	private String[] detailContent = new String[IRtcBuilderConstants.ACTIVITY_DUMMY];
	private String privateOpeSource;
	private String protectedOpeSource;
	private String publicOpeSource;
	//Prefix,Suffix
	private String commonPrefix;
	private String commonSuffix;
	private String dataPortPrefix;
	private String dataPortSuffix;
	private String servicePortPrefix;
	private String servicePortSuffix;
	private String serviceIFPrefix;
	private String serviceIFSuffix;
	private String configurationPrefix;
	private String configurationSuffix;
	private String eventPortPrefix;
	private String eventPortSuffix;

	//
	private String rtm_version = IRtcBuilderConstants.DEFAULT_RTM_VERSION;
	private String rtm_java_version = IRtcBuilderConstants.DEFAULT_RTM_VERSION;
	private boolean test_version = false;

	private boolean isChoreonoid = false;
	private List<IdlPathParam> idlSearchPathList = new ArrayList<IdlPathParam>();

	public RtcParam(GeneratorParam parent) {
		this(parent, false);
	}

	public RtcParam(GeneratorParam parent, boolean isTest) {
		this.parent = parent;
		//
		if (!isTest) {
			String dateTime = createXMLGregorianCalendar(new Date()).toString();
			ProfileHandler handler = new ProfileHandler();
			rtcxml = handler.createInitialRtcXml(dateTime);
			this.creationDate = dateTime;
			this.updateDate = dateTime;
		}
		//
		actions = new RecordedList<ActionsParam>();
		for (int intidx = IRtcBuilderConstants.ACTIVITY_INITIALIZE; intidx < IRtcBuilderConstants.ACTIVITY_DUMMY; intidx++) {
			actions.add(new ActionsParam());
		}
		//
		setUpdated(false);
	}

	public List<String> getPrivateOperations() {
		return privateOperations;
	}
	public List<String> getProtectedOperations() {
		return protectedOperations;
	}
	public List<String> getPublicOperations() {
		return publicOperations;
	}

	public List<String> getPrivateAttributes() {
		return privateAttributes;
	}
	public List<String> getProtectedAttributes() {
		return protectedAttributes;
	}
	public List<String> getPublicAttributes() {
		return publicAttributes;
	}

	public String getSchemaVersion() {
		return schemaVersion;
	}
	//
	public void setSchemaVersion(String version) {
		checkUpdated(this.schemaVersion, version);
		this.schemaVersion = version;
	}
	//基本
	public String getAbstract() {
		return abstractDesc;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public List<String> getVersionUpLog() {
		return versionUpLog;
	}
	public String getName() {
		return name;
	}
	public String getCategory() {
		return category;
	}
	public String getDescription() {
		return description;
	}
	public String getVersion() {
		return version;
	}
	public String getVender() {
		return vender;
	}
	public String getComponentType() {
		return componentType;
	}
	public String getActivityType() {
		return activityType;
	}
	public String getComponentKind() {
		return componentKind;
	}
	public int getMaxInstance() {
		return maxInstance;
	}
	public String getExecutionType() {
		return executionType;
	}
	public double getExecutionRate() {
		return executionRate;
	}
	public boolean IsExecutionRateSet() {
		return executionRate>0.0;
	}
	public String getRtcType() {
		return rtcType;
	}
	public String getCurrentVersionUpLog() {
		return currentVULog;
	}
	//
	public void setAbstract(String abst) {
		checkUpdated(this.abstractDesc, abst);
		this.abstractDesc = abst;
	}
	public void setCreationDate(String date) {
		checkUpdated(this.creationDate, date);
		this.creationDate = date;
	}
	public void setUpdateDate(String date) {
		checkUpdated(this.updateDate, date);
		this.updateDate = date;
	}
	public void setName(String name) {
		checkUpdated(this.name, name);
		this.name = name;
	}
	public void setCategory(String category) {
		checkUpdated(this.category, category);
		this.category = category;
	}
	public void setDescription(String desc) {
		checkUpdated(this.description, desc);
		this.description = desc;
	}
	public void setVersion(String version) {
		checkUpdated(this.version, version);
		this.version = version;
	}
	public void setVender(String vender) {
		checkUpdated(this.vender, vender);
		this.vender = vender;
	}
	public void setComponentType(String comp_type) {
		checkUpdated(this.componentType, comp_type);
		this.componentType = comp_type;
	}
	public void setActivityType(String act_type) {
		checkUpdated(this.activityType, act_type);
		this.activityType = act_type;
	}
	public void setComponentKind(String comp_kind) {
		checkUpdated(this.componentKind, comp_kind);
		this.componentKind = comp_kind;
	}
	public void setMaxInstance(int maxInst) {
		checkUpdated(new Integer(this.maxInstance), new Integer(maxInst));
		this.maxInstance = maxInst;
	}
	public void setExecutionType(String exec_type) {
		checkUpdated(this.executionType, exec_type);
		this.executionType = exec_type;
	}
	public void setExecutionRate(double exec_rate) {
		checkUpdated(new Double(this.executionRate), new Double(exec_rate));
		this.executionRate = exec_rate;
	}
	public void setRtcType(String rtc_type) {
		checkUpdated(this.rtcType, rtc_type);
		this.rtcType = rtc_type;
	}
	public void setCurrentVersionUpLog(String cvulog) {
		if (cvulog != null && this.currentVULog == null) {
			this.currentVULog = "";
		}
		checkUpdated(this.currentVULog, cvulog);
		this.currentVULog = cvulog;
	}
	//
	public List<DataPortParam> getInports() {
		return inports;
	}
	
	public List<DataPortParam> getOutports() {
		return outports;
	}
	public List<EventPortParam> getEventports() {
		return eventports;
	}
	
	public EventPortParam getEventport() {
		if(eventports == null || eventports.size()==0) return null;
		return eventports.get(0); 
	}
	//
	public List<ServicePortParam> getServicePorts() {
		return serviceports;
	}

	public List<ServiceClassParam> getServiceClassParams() {
		return serviceClassParams;
	}

	//
	public List<ConfigSetParam> getConfigParams() {
		return configParams;
	}
	//RTC.xml
	public String getRtcXml() {
		return rtcxml;
	}
	public void setRtcXml(String rtcXml) {
		checkUpdated(this.rtcxml, rtcXml);
		this.rtcxml = rtcXml;
	}
	//
	public String getLanguage() {
		return getLangageListString(langList);
	}
	public String getLanguageArg() {
		return getLangageListArgString();
	}
	public List<String> getLangList() {
		return langList;
	}
	public List<String> getLangArgList() {
		return langArgList;
	}
	public List<String> getLibraryPathes() {
		return libraryPath;
	}
	public String getArchitecture() {
		return this.architecture;
	}
	public List<TargetEnvParam> getTargetEnvs() {
		return this.targetEnvs;
	}
	//
	public void setLanguage(String lang) {
		if (lang != null) {
			getLangList().clear();
			getLangList().addAll(Arrays.asList(lang.split(",")));
		}
	}
	public void setLanguageArg(String lang) {
		if (lang != null) {
			getLangArgList().clear();
			getLangArgList().addAll(Arrays.asList(lang.split(",")));
		}
	}

	public void setArchitecture(String arch) {
		checkUpdated(this.architecture, arch);
		this.architecture = arch;
	}
	//
	public boolean isLanguageExist(String language) {
		boolean result = false;
		for (String str : getLangList()) {
			if (language.equalsIgnoreCase(str)) {
				result = true;
				break;
			}
		}
		return result;
	}

	//
	public boolean isDocExist() {
		if( (doc_description==null || doc_description.equals("")) &&
			(doc_in_out==null || doc_in_out.equals("")) &&
			(doc_algorithm==null || doc_algorithm.equals("")) &&
			(doc_creator==null || doc_creator.equals("")) &&
			(doc_license==null || doc_license.equals("")) &&
			(doc_reference==null || doc_reference.equals("")) )
				return false;
		return true;
	}
	public String getDocDescription() {
		if(doc_description==null) doc_description = "";
		return doc_description;
	}
	public String getDocInOut() {
		if(doc_in_out==null) doc_in_out = "";
		return doc_in_out;
	}
	public String getDocAlgorithm() {
		if(doc_algorithm==null) doc_algorithm = "";
		return doc_algorithm;
	}
	//
	public void setDocDescription(String description) {
		checkUpdated(this.doc_description, description);
		this.doc_description = description;
	}
	public void setDocInOut(String inout) {
		checkUpdated(this.doc_in_out, inout);
		this.doc_in_out = inout;
	}
	public void setDocAlgorithm(String algorithm) {
		checkUpdated(this.doc_algorithm, algorithm);
		this.doc_algorithm = algorithm;
	}
	//Actions
	public boolean isActionsExist(int actionsId) {
		if( actions == null )
			return false;
		if( actions.get(actionsId) == null )
			return false;
		if( actions.get(actionsId).getOverView() == null ||
				actions.get(actionsId).getPreCondition() == null ||
				actions.get(actionsId).getPostCondition() == null )
					return false;
		if( actions.get(actionsId).getOverView().equals("") &&
				actions.get(actionsId).getPreCondition().equals("") &&
				actions.get(actionsId).getPostCondition().equals("") )
					return false;
		return true;
	}
	public boolean IsNotImplemented(int actionId) {
		return !actions.get(actionId).getImplemented();
	}
	public String IsComment(int actionId) {
		if(actions.get(actionId).getImplemented()) {
			return "";
		} else {
			return "//";
		}
	}
	public boolean getActionImplemented(int actionId) {
		return actions.get(actionId).getImplemented();
	}
	public String getDocActionOverView(int actionId) {
		return actions.get(actionId).getOverView();
	}
	public String getDocActionPreCondition(int actionId) {
		return actions.get(actionId).getPreCondition();
	}
	public String getDocActionPostCondition(int actionId) {
		return actions.get(actionId).getPostCondition();
	}
	//
	public void setActionImplemented(int actionId, boolean implemented) {
		actions.get(actionId).setImplemaented(implemented);
	}
	public void setActionImplemented(int actionId, String implemented) {
		actions.get(actionId).setImplemaented(implemented);
	}
	public void setDocActionOverView(int actionId, String overview) {
		actions.get(actionId).setOverview(overview);
	}
	public void setDocActionPreCondition(int actionId, String precond) {
		actions.get(actionId).setPreCondition(precond);
	}
	public void setDocActionPostCondition(int actionId, String postcond) {
		actions.get(actionId).setPostCondition(postcond);
	}
	public void setAction(int actionId, boolean implemented, String overview, String precond, String postcond) {
		actions.get(actionId).setImplemaented(implemented);
		actions.get(actionId).setOverview(overview);
		actions.get(actionId).setPreCondition(precond);
		actions.get(actionId).setPostCondition(postcond);
	}
	//
	public String getDocCreator() {
		if(doc_creator==null) doc_creator = "";
		return doc_creator;
	}
	public String getDocLicense() {
		if(doc_license==null) doc_license = "";
		return doc_license;
	}
	public String getDocReference() {
		if(doc_reference==null) doc_reference = "";
		return doc_reference;
	}
	//
	public void setDocCreator(String creator) {
		checkUpdated(this.doc_creator, creator);
		this.doc_creator = creator;
	}
	public void setDocLicense(String license) {
		checkUpdated(this.doc_license, license);
		this.doc_license = license;
	}
	public void setDocReference(String reference) {
		checkUpdated(this.doc_reference, reference);
		this.doc_reference = reference;
	}

	public String getLangageListArgString() {
		StringBuffer result = new StringBuffer();
		for (Iterator iter = langArgList.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			if ("".equals(result.toString()) == false) {
				result.append(",");
			}
			result.append(element);
		}
		return result.toString();
	}

	public static String getLangageListString(List langList) {
		StringBuffer result = new StringBuffer();
		for (Iterator iter = langList.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			if ("".equals(result.toString()) == false) {
				result.append(",");
			}
			result.append(element);
		}
		return result.toString();
	}
	//
	public String getOutputProject() {
		return outputProject;
	}

	public void setOutputProject(String outputDirectory) {
		checkUpdated(this.outputProject, outputDirectory);
		this.outputProject = outputDirectory;
	}

	public Map getExtentionData() {
		return extentionData;
	}

	public List<PropertyParam> getProperties() {
		return properties;
	}

	public GeneratorParam getParent() {
		return parent;
	}

	public List<IdlFileParam> getProviderIdlPathes() {
		return providerIdlPathes;
	}

	public List<IdlFileParam> getConsumerIdlPathes() {
		return consumerIdlPathes;
	}
	
	public List<IdlFileParam> getConsumerIdlPathesAdded() {
		List<IdlFileParam> result = new ArrayList<IdlFileParam>();
		for(IdlFileParam target : consumerIdlPathes) {
			if(target.checkDefault()) continue;
			result.add(target);
		}
		return result;
	}
	
	public boolean checkWithoutDefaultPathes() {
		if(0<providerIdlPathes.size()) {
			for(IdlFileParam e : providerIdlPathes) {
				if(e.checkDefault()==false) return true;
			}
		}
		if(0<consumerIdlPathes.size() ) {
			for(IdlFileParam e : consumerIdlPathes) {
				if(e.checkDefault()==false) return true;
			}
		}
		return false;
	}

	public List<String> getOriginalProviderIdls() {
		return originalProviderIdls;
	}
	public List<String> getOriginalConsumerIdls() {
		return originalConsumerIdls;
	}

	public List<String> getIncludedIdls() {
		return includedIdls;
	}
	public List<IdlFileParam> getIncludedIdlPathes() {
		List<IdlFileParam> result = new ArrayList<IdlFileParam>();
		for (String s : includedIdls) {
			result.add(new IdlFileParam(s, this));
		}
		return result;
	}
	public List<IdlPathParam> getIdlPathes() {
		return idlPathes;
	}
	//
	public String getCommonPrefix() {
		if(commonPrefix==null) commonPrefix = "m_";
		return commonPrefix;
	}
	public void setCommonPrefix(String commonPrefix) {
		this.commonPrefix = commonPrefix;
	}

	public String getCommonSuffix() {
		if(commonSuffix==null) commonSuffix = "";
		return commonSuffix;
	}
	public void setCommonSuffix(String commonSuffix) {
		this.commonSuffix = commonSuffix;
	}

	public String getDataPortPrefix() {
		if(dataPortPrefix==null) dataPortPrefix = "";
		return dataPortPrefix;
	}
	public void setDataPortPrefix(String dataPortPrefix) {
		this.dataPortPrefix = dataPortPrefix;
	}

	public String getDataPortSuffix() {
		if(dataPortSuffix==null) dataPortSuffix = "";
		return dataPortSuffix;
	}
	public void setDataPortSuffix(String dataPortSuffix) {
		this.dataPortSuffix = dataPortSuffix;
	}

	public String getServicePortPrefix() {
		if(servicePortPrefix==null) servicePortPrefix = "";
		return servicePortPrefix;
	}
	public void setServicePortPrefix(String servicePortPrefix) {
		this.servicePortPrefix = servicePortPrefix;
	}

	public String getServicePortSuffix() {
		if(servicePortSuffix==null) servicePortSuffix = "";
		return servicePortSuffix;
	}
	public void setServicePortSuffix(String servicePortSuffix) {
		this.servicePortSuffix = servicePortSuffix;
	}

	public String getServiceIFPrefix() {
		if(serviceIFPrefix==null) serviceIFPrefix = "";
		return serviceIFPrefix;
	}
	public void setServiceIFPrefix(String serviceIFPrefix) {
		this.serviceIFPrefix = serviceIFPrefix;
	}

	public String getServiceIFSuffix() {
		if(serviceIFSuffix==null) serviceIFSuffix = "";
		return serviceIFSuffix;
	}
	public void setServiceIFSuffix(String serviceIFSuffix) {
		this.serviceIFSuffix = serviceIFSuffix;
	}

	public String getConfigurationPrefix() {
		if(configurationPrefix==null) configurationPrefix = "";
		return configurationPrefix;
	}
	public void setConfigurationPrefix(String configurationPrefix) {
		this.configurationPrefix = configurationPrefix;
	}

	public String getConfigurationSuffix() {
		if(configurationSuffix==null) configurationSuffix = "";
		return configurationSuffix;
	}
	public void setConfigurationSuffix(String configurationSuffix) {
		this.configurationSuffix = configurationSuffix;
	}

	public String getEventPortPrefix() {
		if(eventPortPrefix==null) eventPortPrefix = "";
		return eventPortPrefix;
	}

	public void setEventPortPrefix(String eventPortPrefix) {
		this.eventPortPrefix = eventPortPrefix;
	}

	public String getEventPortSuffix() {
		if(eventPortSuffix==null) eventPortSuffix = "";
		return eventPortSuffix;
	}

	public void setEventPortSuffix(String eventPortSuffix) {
		this.eventPortSuffix = eventPortSuffix;
	}

	public boolean isChoreonoid() {
		return isChoreonoid;
	}
	public void setChoreonoid(boolean value) {
		isChoreonoid = value;
	}
	
	public List<IdlPathParam> getIdlSearchPathList() {
		return idlSearchPathList;
	}
	//
	public void checkAndSetParameter() {
		List<String> providerIdlStrings = new ArrayList<String>();
		List<String> consumerIdlStrings = new ArrayList<String>();
		List<String> idlPathes = new ArrayList<String>();
		List<IdlFileParam> providerIdlParams = new ArrayList<IdlFileParam>();
		List<IdlFileParam> consumerIdlParams = new ArrayList<IdlFileParam>();
		List<String> originalConsumerIdlPathList = new ArrayList<String>();

		List<ServicePortInterfaceParam> serviceIFs = getServiceInterfaceList();

		//IDLパス，IDLサーチパスの確認
		for( ServicePortInterfaceParam serviceInterface : serviceIFs ) {
			if( serviceInterface.getDirection().equals(ServicePortInterfaceParam.INTERFACE_DIRECTION_PROVIDED)) {
				if( !providerIdlStrings.contains(serviceInterface.getIdlFullPath()) ) {
					idlPathes.add(serviceInterface.getIdlFullPath().trim());
					providerIdlStrings.add(serviceInterface.getIdlFullPath());
					providerIdlParams.add(new IdlFileParam(serviceInterface.getIdlFullPath(),this));
				}
			}
		}
		for( ServicePortInterfaceParam serviceInterface : serviceIFs ) {
			if( serviceInterface.getDirection().equals(ServicePortInterfaceParam.INTERFACE_DIRECTION_REQUIRED)) {
				originalConsumerIdlPathList.add(serviceInterface.getIdlFullPath());
				if( !idlPathes.contains(serviceInterface.getIdlFullPath().trim()) ) {
					idlPathes.add(serviceInterface.getIdlFullPath().trim());
					consumerIdlStrings.add(serviceInterface.getIdlFullPath());
					consumerIdlParams.add(new IdlFileParam(serviceInterface.getIdlFullPath(),this));
				}
			}
		}
		/////
		for( DataPortParam target : getInports() ) {
			List<String> localIdlPathes = new ArrayList<String>();
			checkAndAddIDLPath(target.getType(), localIdlPathes, consumerIdlStrings, consumerIdlParams);
			if(0<localIdlPathes.size()) {
				idlPathes.addAll(localIdlPathes);
				target.setIdlFile(localIdlPathes.get(0));
			}
		}
		for( DataPortParam target : outports ) {
			if(checkExistIDL(providerIdlParams, consumerIdlParams, target)) continue;
			
			List<String> localIdlPathes = new ArrayList<String>();
			checkAndAddIDLPath(target.getType(), localIdlPathes, consumerIdlStrings, consumerIdlParams);
			if(0<localIdlPathes.size()) {
				idlPathes.addAll(localIdlPathes);
				target.setIdlFile(localIdlPathes.get(0));
			}
		}
		for( ConfigSetParam target : configParams ) {
			List<String> localIdlPathes = new ArrayList<String>();
			checkAndAddIDLPath(target.getType(), localIdlPathes, consumerIdlStrings, consumerIdlParams);
			if(0<localIdlPathes.size()) {
				idlPathes.addAll(localIdlPathes);
				target.setIdlFile(localIdlPathes.get(0));
			}
		}
		/////
		for( ServicePortInterfaceParam serviceInterface : serviceIFs ) {
			for( IdlFileParam idlParam : providerIdlParams ) {
				if( serviceInterface.getIdlFullPath().trim().equals(idlParam.getIdlPath().trim()) ) {
					for(IdlPathParam path : this.idlSearchPathList) {
						if(path.isDefault()) continue;
						if(idlParam.getIdlSearchPathes().contains(path.getPath())) continue;
						idlParam.getIdlSearchPathes().add(path.getPath());
					}
					break;
				}
			}
			for( IdlFileParam idlParam : consumerIdlParams ) {
				if( serviceInterface.getIdlFullPath().trim().equals(idlParam.getIdlPath().trim()) ) {
					for(IdlPathParam path : this.idlSearchPathList) {
						if(path.isDefault()) continue;
						if(idlParam.getIdlSearchPathes().contains(path.getPath())) continue;
						idlParam.getIdlSearchPathes().add(path.getPath());
					}
					break;
				}
			}
		}
		//
		getProviderIdlPathes().clear();
		getProviderIdlPathes().addAll(providerIdlParams);
		getConsumerIdlPathes().clear();
		getConsumerIdlPathes().addAll(consumerIdlParams);
		getOriginalProviderIdls().clear();
		getOriginalProviderIdls().addAll(providerIdlStrings);
		getOriginalConsumerIdls().clear();
		getOriginalConsumerIdls().addAll(originalConsumerIdlPathList);

		//クラスパスの重複削除
		ArrayList<String> libraries = new ArrayList<String>();
		for (String library : this.getLibraryPathes()) {
			if (!libraries.contains(library)) {
				libraries.add(library);
			}
		}
		getLibraryPathes().clear();
		getLibraryPathes().addAll(libraries);
	}

	private boolean checkExistIDL(List<IdlFileParam> providerIdlParams, List<IdlFileParam> consumerIdlParams,
			DataPortParam target) {
		boolean isExist = false;
		for(IdlFileParam param : consumerIdlParams) {
			if(param.getIdlFile().equals(target.getIdlFile())) {
				isExist = true;
				break;
			}
		}
		for(IdlFileParam param : providerIdlParams) {
			if(param.getIdlFile().equals(target.getIdlFile())) {
				isExist = true;
				break;
			}
		}
		return isExist;
	}

	private void checkAndAddIDLPath(String targetType, List<String> idlPathes,
			List<String> consumerIdlStrings, List<IdlFileParam> consumerIdlParams) {
		for(DataTypeParam dataTypes : this.parent.getDataTypeParams()) {
			if( dataTypes.getDefinedTypes().contains(targetType) ) {
				String targetIDL = dataTypes.getFullPath();
				if( !idlPathes.contains(targetIDL.trim()) ) {
					if( targetIDL!=null) {
						idlPathes.add(targetIDL.trim());
						consumerIdlStrings.add(targetIDL);
					}
				}
				boolean isHit = false;
				for(IdlFileParam file : consumerIdlParams) {
					if( file.getIdlPath().equals(targetIDL) ) {
						if(file.getTargetType().contains(targetType)==false) {
							file.getTargetType().add(targetType);
						}
						isHit = true;
						break;
					}
				}
				if(isHit==false) {
					IdlFileParam target = new IdlFileParam(targetIDL,this);
					target.setDataPort(true);
					target.getTargetType().add(targetType);
					consumerIdlParams.add(target);
				}

				break;
			}
		}
	}

	private List<ServicePortInterfaceParam> getServiceInterfaceList() {
		List<ServicePortInterfaceParam> result = new ArrayList<ServicePortInterfaceParam>();
		for( ServicePortParam servicePort : this.getServicePorts() ) {
			result.addAll(servicePort.getServicePortInterfaces());
		}
		return result;
	}
	//
	public String getRtmVersion() {
		return this.rtm_version;
	}
	public void setRtmVersion(String version) {
		this.rtm_version = version;
	}

	public String getRtmJavaVersion() {
		return rtm_java_version;
	}
	public void setRtmJavaVersion(String rtm_java_version) {
		this.rtm_java_version = rtm_java_version;
	}

	public boolean getIsTest() {
		return this.test_version;
	}
	public void setIsTest(boolean isTest) {
		this.test_version = isTest;
	}
	public boolean checkConfig() {
		if( 0<configParams.size() || 0.0<executionRate ) {
			return true;
		}
		return false;
	}

	public void setDetailContent(int index, String target) {
		detailContent[index] = target;
	}
	public String getDetailContent(int index) {
		return detailContent[index];
	}

	public String getPrivateOpeSource() {
		return privateOpeSource;
	}
	public void setPrivateOpeSource(String privateOpeSource) {
		this.privateOpeSource = privateOpeSource;
	}

	public String getProtectedOpeSource() {
		return protectedOpeSource;
	}
	public void setProtectedOpeSource(String protectedOpeSource) {
		this.protectedOpeSource = protectedOpeSource;
	}

	public String getPublicOpeSource() {
		return publicOpeSource;
	}
	public void setPublicOpeSource(String publicOpeSource) {
		this.publicOpeSource = publicOpeSource;
	}

	public StateParam getFsmParam() {
		return fsmParam;
	}

	public void setFsmParam(StateParam fsmParam) {
		this.fsmParam = fsmParam;
	}
	
	public void parseEvent() {
		if(this.fsmParam == null ) return;
		
		List<EventParam> eventList = getEventport().getEvents();
		
		for(TransitionParam trans : fsmParam.getAllTransList()) {
			boolean isExist = false;
			for(EventParam eventp : eventList) {
				if(eventp.getName().equals(trans.getEvent())
						&& eventp.getCondition().equals(trans.getCondition())
						&& eventp.getSource().equals(trans.getSource())
						&& eventp.getTarget().equals(trans.getTarget()) ) {
					trans.setEventParam(eventp);
					isExist = true;
					break;
				}
			}
			if(isExist == false) {
				EventParam eventp = new EventParam();
				eventp.setName(trans.getEvent());
				eventp.setCondition(trans.getCondition());
				eventp.setSource(trans.getSource());
				eventp.setTarget(trans.getTarget());
				trans.setEventParam(eventp);
				eventList.add(eventp);
			}
		}
	}
	
	public String getFsmContents() {
		return fsmContents;
	}
	public void setFsmContents(String fsmContents) {
		checkUpdated(this.fsmContents, fsmContents);
		this.fsmContents = fsmContents;
	}

	@Override
	public boolean isUpdated() {
		if (super.isUpdated()) {
			return true;
		}
		if (this.langList.isUpdated() || this.langArgList.isUpdated()) {
			return true;
		}
		if (this.inports.isUpdated() || this.outports.isUpdated()) {
			return true;
		}
		if (this.eventports.isUpdated()) {
			return true;
		}
		if (this.serviceports.isUpdated()) {
			return true;
		}
		if (this.configParams.isUpdated()) {
			return true;
		}
		if (this.actions.isUpdated()) {
			return true;
		}
		if (this.targetEnvs.isUpdated()) {
			return true;
		}
		return false;
	}

	@Override
	public void resetUpdated() {
		super.resetUpdated();
		//
		this.langList.resetUpdated();
		this.langArgList.resetUpdated();
		//
		this.inports.resetUpdated();
		this.outports.resetUpdated();
		this.eventports.resetUpdated();
		//
		this.serviceports.resetUpdated();
		//
		this.configParams.resetUpdated();
		//
		this.actions.resetUpdated();
		//
		this.targetEnvs.resetUpdated();
	}
	/////
	public void addFSMPort() {
		if(0<eventports.size()) return;
		eventports.add(new EventPortParam("FSMEvent", "FSMEvent", 0));
	}
	
	public void deleteFSMPort() {
		eventports.clear();
	}
	
	public PropertyParam getProperty(String target) {
		PropertyParam result = null;
		for(PropertyParam param : properties) {
			if( param.getName().equals(target)) {
				result = param;
				break;
			}
		}
		return result;
	}
	
	public void setProperty(String target, String value) {
		PropertyParam prop = null;
		for(PropertyParam param : properties) {
			if( param.getName().equals(target)) {
				prop = param;
				break;
			}
		}
		if(prop==null) {
			prop = new PropertyParam();
			prop.setName(target);
			getProperties().add(prop);
		}
		prop.setValue(value);
	}
	
	public boolean isStaticFSM() {
		PropertyParam fsm = getProperty(IRtcBuilderConstants.PROP_TYPE_FSM);
		if(fsm!=null) {
			if(Boolean.valueOf(fsm.getValue())) {
				PropertyParam fsmType = getProperty(IRtcBuilderConstants.PROP_TYPE_FSMTYTPE);
				if(fsmType.getValue().equals(IRtcBuilderConstants.FSMTYTPE_STATIC)) {
					return true;
				}
			}
		}
		return false;
	}
}
