package jp.ac.meijo_u.iso22166_part202.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

import org.iso.iso22166.part202.profile.ArgSpec;
import org.iso.iso22166.part202.profile.Communication;
import org.iso.iso22166.part202.profile.CompilerType;
import org.iso.iso22166.part202.profile.CyberSecurity;
import org.iso.iso22166.part202.profile.DataBus;
import org.iso.iso22166.part202.profile.ExeForm;
import org.iso.iso22166.part202.profile.ExecutableForm;
import org.iso.iso22166.part202.profile.ExecutionType;
import org.iso.iso22166.part202.profile.IDnType;
import org.iso.iso22166.part202.profile.IOVariables;
import org.iso.iso22166.part202.profile.InfraType;
import org.iso.iso22166.part202.profile.Infrastructure;
import org.iso.iso22166.part202.profile.Libraries;
import org.iso.iso22166.part202.profile.Library;
import org.iso.iso22166.part202.profile.ModelCase;
import org.iso.iso22166.part202.profile.Modelling;
import org.iso.iso22166.part202.profile.ModuleID;
import org.iso.iso22166.part202.profile.NVList;
import org.iso.iso22166.part202.profile.NameValue;
import org.iso.iso22166.part202.profile.OStype;
import org.iso.iso22166.part202.profile.Properties;
import org.iso.iso22166.part202.profile.RangeString;
import org.iso.iso22166.part202.profile.SIM;
import org.iso.iso22166.part202.profile.SafeSecure;
import org.iso.iso22166.part202.profile.SafetyFunction;
import org.iso.iso22166.part202.profile.ServiceMethod;
import org.iso.iso22166.part202.profile.ServiceProfile;
import org.iso.iso22166.part202.profile.Services;
import org.iso.iso22166.part202.profile.Status;
import org.iso.iso22166.part202.profile.Variable;
import org.openrtp.namespaces.ros.version01.ActionExt;
import org.openrtp.namespaces.ros.version01.BasicInfoExt;
import org.openrtp.namespaces.ros.version01.DocAction;
import org.openrtp.namespaces.ros.version01.DocBasic;
import org.openrtp.namespaces.ros.version01.DocLifecycleCallback;
import org.openrtp.namespaces.ros.version01.DocParameter;
import org.openrtp.namespaces.ros.version01.DocService;
import org.openrtp.namespaces.ros.version01.DocTopic;
import org.openrtp.namespaces.ros.version01.LanguageExt;
import org.openrtp.namespaces.ros.version01.LifeCycleExt;
import org.openrtp.namespaces.ros.version01.LifecycleCallbackExt;
import org.openrtp.namespaces.ros.version01.ObjectFactory;
import org.openrtp.namespaces.ros.version01.ParameterExt;
import org.openrtp.namespaces.ros.version01.Property;
import org.openrtp.namespaces.ros.version01.RosProfile;
import org.openrtp.namespaces.ros.version01.ServiceExt;
import org.openrtp.namespaces.ros.version01.TargetEnvironment;
import org.openrtp.namespaces.ros.version01.Timer;
import org.openrtp.namespaces.ros.version01.TopicExt;

import jp.go.aist.rtm.toolscommon.profiles.util.XmlHandlerROS;

public class ISO2ROSProfileHandler extends ISO2RTCProfileHandler {
	public RosProfile convertIso2Ros(SIM source) {
		ObjectFactory factory = new ObjectFactory();
		
		RosProfile result = factory.createRosProfile();
		result.setVersion("0.1");
		BasicInfoExt basic = factory.createBasicInfoExt();
		result.setBasicInfo(basic);
		List<Property> basicProp = basic.getProperties();
		DocBasic docBasic = factory.createDocBasic();
		basic.setDoc(docBasic);
		
		LanguageExt lang = factory.createLanguageExt();
		result.setLanguage(lang);
		TargetEnvironment env = factory.createTargetEnvironment();
		lang.setTargets(env);
		/////
		basic.setNodeName(source.getModuleName());
		basic.setDescription(source.getDescription());
		basic.setMaintainer(source.getManufacturer());
		createISOProperty(factory, "examples", source.getExamples(), basicProp);
		createProperty(factory, "SIM_Version", "iso22166-202:2025", "", basicProp);

		convertIDnType(source, factory, basic);
		convertProperties(source, factory, result);
		convertIOVariables(source, factory, result);
		convertStatus(source, factory, basicProp);
		convertServices(source, factory, result);
		convertInfrastructure(source, factory, basicProp, env);
		convertSafeSecure(source, factory, basicProp);
		convertModelling(source, factory, basicProp);
		convertExecutableForm(source, factory, result, basicProp);
		convertNVList(source, factory, result);

		return result;
	}
	
	private void convertIDnType(SIM source, ObjectFactory factory, BasicInfoExt basic) {
		List<Property> basicProp = basic.getProperties();
		
		IDnType idnType = source.getIdnType();
		if(idnType == null) return;
		
		createISOProperty(factory, "mID", bytesListToHexString(idnType.getModuleID().getMID()), basicProp);
		createISOProperty(factory, "iID", bytesToHexString(idnType.getModuleID().getIID()), basicProp);
		
		basic.setVersion(idnType.getInformationModelVersion());
		
		if(idnType.getSwAspects() == null) return;
		
		for(ModuleID each : idnType.getSwAspects()) {
			String mId = bytesListToHexString(each.getMID());
			String iId = bytesToHexString(each.getIID());
			createISOProperty(factory, "swAspects", mId + IProfileConstants.ELEM_DELIMITOR + iId, basicProp);
		}
	}

	private void convertProperties(SIM source, ObjectFactory factory, RosProfile result) {
		BasicInfoExt basic = (BasicInfoExt)result.getBasicInfo();
		LanguageExt lang = (LanguageExt)result.getLanguage();
		TargetEnvironment env = lang.getTargets();
		
		Properties properties =source.getProperties();
		if(properties == null) return;
		
		OStype osType = properties.getOsType();
		if(osType != null) {
			createISOProperty(factory, "osType_type", osType.getType(), basic.getProperties());
			if(osType.getBit() != null) {
				createISOProperty(factory, "osType_bit", osType.getBit().value(), basic.getProperties());
			}
			createISOProperty(factory, "osType_version", osType.getVersion(), basic.getProperties());
		}
		
		Libraries libs = properties.getLibs();
		if(libs!=null) {
			for(Library each : libs.getLibraries()) {
				org.openrtp.namespaces.ros.version01.Library newLib = factory.createLibrary();
				newLib.setName(each.getName());
				newLib.setVersion(each.getVersion());
				NVList nv = each.getAdditionalInfo();
				if(nv != null) {
					newLib.setOther(getTargetNVValue("other", nv.getNv()));
				}
				NVList libNv =  each.getAdditionalInfo();
				if(libNv!=null) {
					newLib.setOther(getTargetNVValue("other", libNv.getNv()));
				}
				env.getLibraries().add(newLib);
			}
		}
		
		CompilerType compiler = properties.getCompiler();
		if(compiler!=null) {
			lang.setKind(compiler.getCompilerName());

			createISOProperty(factory, "os_name", compiler.getOsName(), lang.getProperties());
			RangeString verRangeOS = compiler.getVerRangeOS();
			if(verRangeOS!=null) {
				createISOProperty(factory, "os_min", verRangeOS.getMin(), lang.getProperties());
				createISOProperty(factory, "os_min", verRangeOS.getMax(), lang.getProperties());
			}
			
			RangeString verRangeCompiler = compiler.getVerRangeCompiler();
			if(verRangeCompiler!=null) {
				createISOProperty(factory, "compiler_min", verRangeCompiler.getMin(), lang.getProperties());
				createISOProperty(factory, "compiler_max", verRangeCompiler.getMax(), lang.getProperties());
			}
			
			if(compiler.getBitnCPUarch()  != null) {
				createISOProperty(factory, "bitnCPUarch", compiler.getBitnCPUarch(), lang.getProperties());
			}
		}
		
		List<ExecutionType> exeTypes = properties.getExeType();
		if(0<exeTypes.size()) {
			for(int index=0; index<exeTypes.size(); index++) {
				ExecutionType each = exeTypes.get(index);
				String ketPre = "exeType_" + Integer.valueOf(index) + "_";
				if(each.getOpType() != null) {
					createISOProperty(factory, ketPre + "opType", each.getOpType().name(), basic.getProperties());
				}
				createISOProperty(factory, ketPre + "hardRT", Boolean.valueOf(each.isHardRT()).toString(), basic.getProperties());
				createISOProperty(factory, ketPre + "timeConstraint", Double.valueOf(each.getTimeConstraint()).toString(), basic.getProperties());
				createISOProperty(factory, ketPre + "priority", bytesToHexString(each.getPriority()), basic.getProperties());
				if(each.getInstanceType() != null) {
					createISOProperty(factory, ketPre + "instanceType", each.getInstanceType().name(), basic.getProperties());
				}
			}
		}
		
		List<org.iso.iso22166.part202.profile.Property> propList = properties.getProperty();
		if(propList != null && 0 < propList.size()) {
			for(org.iso.iso22166.part202.profile.Property each : properties.getProperty() ) {
				ParameterExt config = factory.createParameterExt();
				DocParameter docConfig = factory.createDocParameter();
				config.setDoc(docConfig);
				result.getParameters().add(config);
				
				config.setDefaultValue(each.getValue());
				config.setReadOnly(each.isImmutable());
				docConfig.setDescription(each.getDescription());
				config.setParameterName(each.getName());
				config.setDataType(each.getType());
				docConfig.setUnit(each.getUnit());
				
				NVList nvList = each.getAdditionalInfo();
				if(nvList!=null) {
					List<NameValue> nvs = nvList.getNv();
					List<String> definedList = Arrays.asList("constraint",
															 "doc_dataname", "doc_default_value", "doc_range", "doc_constraint");
					
					config.setConstraint(getTargetNVValue("constraint", nvs));

					docConfig.setDataname(getTargetNVValue("doc_dataname", nvs));
					docConfig.setDefaultValue(getTargetNVValue("doc_default_value", nvs));
					docConfig.setRange(getTargetNVValue("doc_range", nvs));
					docConfig.setConstraint(getTargetNVValue("doc_constraint", nvs));
					
					for(NameValue nv : nvList.getNv()) {
						if(checkNVName(nv.getName(), definedList)) continue;
						createISOProperty(factory, nv.getName(), nv.getValue(), config.getProperties());
					}
				}
			}
		}
	}
	
	private void convertIOVariables(SIM source, ObjectFactory factory, RosProfile result) {
		IOVariables iOVariables = source.getIoVariables();
		if(iOVariables==null) return;
		
		List<Variable> varList = iOVariables.getVariable();
		if(varList != null && 0<varList.size()) {
			for(Variable each : varList) {
				String strInOut = each.getIoType().toString();
				if(strInOut.equals("IN")) {
					createTopic(factory, result, each, "Subscribe", "");
				} else if(strInOut.equals("OUT")) {
					createTopic(factory, result, each, "Publish", "");
				} else if(strInOut.equals("INOUT")) {
					createTopic(factory, result, each, "Subscribe", IProfileConstants.INOUT_SUFFIX_IN);
					createTopic(factory, result, each, "Publish", IProfileConstants.INOUT_SUFFIX_OUT);
				}
			}
		}
	}

	private void createTopic(ObjectFactory factory, RosProfile result, Variable each, String portType, String suffix) {
		TopicExt topic = factory.createTopicExt();
		DocTopic docTopic = factory.createDocTopic();
		topic.setDoc(docTopic);
		result.getTopics().add(topic);

		createISOProperty(factory, "value", each.getValue(), topic.getProperties());
		docTopic.setDescription(each.getDescription());
		topic.setTopicName(each.getName() + suffix);
		topic.setMessageType(each.getType());
		docTopic.setUnit(each.getUnit());
		topic.setTopicRole(portType);
		
		NVList nvList = each.getAdditionalInfo();
		if(nvList!=null) {
			List<NameValue> nvs = nvList.getNv();
			List<String> definedList = Arrays.asList("value", 
													 "qos_reliability_type", "qos_history_type", "qos_depth",
													 "variable_callback_name",
													 "doc_type", "doc_semantics", "doc_unit", "doc_occurrence"); 
			topic.setQoSReliabilityType(getTargetNVValue("qos_reliability_type", nvs));
			topic.setQoSHistoryType(getTargetNVValue("qos_history_type", nvs));
			topic.setQoSHistoryDepth(getTargetNVValueBigInteger("qos_depth", nvs));
			topic.setVariableCallbackName(getTargetNVValue("variable_callback_name", nvs));
			
			docTopic.setType(getTargetNVValue("doc_type", nvs));
			docTopic.setSemantics(getTargetNVValue("doc_semantics", nvs));
//			docTopic.setUnit(getTargetNVValue("doc_unit", nvs));
			docTopic.setOccurrence(getTargetNVValue("doc_occurrence", nvs));
			
			for(NameValue nv : nvList.getNv()) {
				if(checkNVName(nv.getName(), definedList)) continue;
				createISOProperty(factory, nv.getName(), nv.getValue(), topic.getProperties());
			}
		}
	}

	private void convertStatus(SIM source, ObjectFactory factory, List<Property> basicProp) {
		Status status = source.getStatus();
		if(status==null) return;
		
		if(status.getExecutionStatus() != null) {
			createISOProperty(factory, "executionStatus", status.getExecutionStatus().toString(), basicProp);
		}
		if(status.getErrorType() != null) {
			createISOProperty(factory, "errorType", status.getErrorType().toString(), basicProp);
		}
	}

	private void convertServices(SIM source, ObjectFactory factory, RosProfile result) {
		Services services = source.getServices();
		if(services==null) return;
		
		for(ServiceProfile each : services.getServiceProfile()) {
			String kind = "service";
			NVList nvList = each.getAdditionalInfo();
			if(nvList != null) {
				List<NameValue> nvs = nvList.getNv();
				kind = getTargetNVValue("kind", nvs);
			}
			
			if(kind.equals("action")) {
				createAction(factory, result, each);
			} else {
				createService(factory, result, each);
			}
		}
	}

	private void createService(ObjectFactory factory, RosProfile result, ServiceProfile each) {
		ServiceExt service = factory.createServiceExt();
		DocService docService = factory.createDocService();
		service.setDoc(docService);
		result.getServices().add(service);
		
		service.setServiceName(each.getId());
		
		List<Property> serviceProp = service.getProperties();
		createISOProperty(factory, "ifURL", each.getIfURL(), serviceProp);
		if(each.getPvType() != null) {
			createISOProperty(factory, "pvType", each.getPvType().toString(), serviceProp);
		}
		if(each.getMoType() != null) {
			createISOProperty(factory, "moType", each.getMoType().toString(), serviceProp);
		}
		

		NVList nvList = each.getAdditionalInfo();
		if(nvList!=null) {
			List<NameValue> nvs = nvList.getNv();
			List<String> definedList = Arrays.asList("service_role", "service_type", "variable_callback_name",
													 "doc_description", "doc_argument", "doc_return"); 
			service.setServiceRole(getTargetNVValue("service_role", nvs));
			service.setServiceType(getTargetNVValue("service_type", nvs));
			service.setVariableCallbackName(getTargetNVValue("variable_callback_name", nvs));
			
			docService.setDescription(getTargetNVValue("doc_description", nvs));
			docService.setArgument(getTargetNVValue("doc_argument", nvs));
			docService.setReturn(getTargetNVValue("doc_return", nvs));
			
			for(NameValue nv : nvList.getNv()) {
				if(checkNVName(nv.getName(), definedList)) continue;
				createISOProperty(factory, nv.getName(), nv.getValue(), serviceProp);
			}
		}
		
		for(int index=0;index<each.getMethodList().size(); index++) {
			ServiceMethod method = each.getMethodList().get(index);
			String strIndex = Integer.valueOf(index + 1).toString();
			String methodPre = "method_" + strIndex;
			createISOProperty(factory, methodPre + "_name", method.getMethodName(), serviceProp);
			createISOProperty(factory, methodPre + "_retType", method.getRetType(), serviceProp);
			createISOProperty(factory, methodPre + "_moType", method.getMoType().toString(), serviceProp);
			createISOProperty(factory, methodPre + "_reqProvType", method.getReqProvType().toString(), serviceProp);

			NVList nvListMethod = method.getAdditionalInfo();
			if(nvListMethod!=null) {
				for(NameValue nv : nvListMethod.getNv()) {
					createISOProperty(factory, methodPre + "_add_" + nv.getName(), nv.getValue(), serviceProp);
				}
			}
			
			for(int idxArg=0;idxArg<method.getArgType().size(); idxArg++) {
				ArgSpec arg = method.getArgType().get(idxArg);
				createISOProperty(factory, methodPre + "_valueName", arg.getValueName(), serviceProp);
				String argPre = methodPre + "_" + arg.getValueName();
				createISOProperty(factory, argPre + "_type", arg.getType(), serviceProp);
				createISOProperty(factory, argPre + "_inout", arg.getInout().toString(), serviceProp);

				NVList nvListArg = arg.getAdditionalInfo();
				if(nvListArg!=null) {
					for(NameValue nv : nvListArg.getNv()) {
						createISOProperty(factory, argPre + "_add_" + nv.getName(), nv.getValue(), serviceProp);
					}
				}
			}
		}
	}
	
	private void createAction(ObjectFactory factory, RosProfile result, ServiceProfile each) {
		ActionExt action = factory.createActionExt();
		DocAction docAction = factory.createDocAction();
		action.setDoc(docAction);
		result.getActions().add(action);
		
		action.setActionName(each.getId());

		NVList nvList = each.getAdditionalInfo();
		if(nvList!=null) {
			List<NameValue> nvs = nvList.getNv();
			List<String> definedList = Arrays.asList("action_role", "action_type", "callback_base",
													 "doc_description", "doc_goal", "doc_feedback", "doc_result"); 
			action.setActionRole(getTargetNVValue("action_role", nvs));
			action.setActionType(getTargetNVValue("action_type", nvs));
			action.setCallbackBase(getTargetNVValue("callback_base", nvs));
			
			docAction.setDescription(getTargetNVValue("doc_description", nvs));
			docAction.setGoal(getTargetNVValue("doc_goal", nvs));
			docAction.setFeedback(getTargetNVValue("doc_feedback", nvs));
			docAction.setResult(getTargetNVValue("doc_result", nvs));
			
			for(NameValue nv : nvList.getNv()) {
				if(checkNVName(nv.getName(), definedList)) continue;
				createISOProperty(factory, nv.getName(), nv.getValue(), action.getProperties());
			}
		}
	}

	private void convertInfrastructure(SIM source, ObjectFactory factory, List<Property> basicProp, TargetEnvironment env) {
		Infrastructure infra = source.getInfra();
		if(infra== null) return;
		
		NVList nvList = infra.getAdditionalInfo();
		if(nvList!=null) {
			for(NameValue nv : nvList.getNv()) {
				createISOProperty(factory, "infra_add_" + nv.getName(), nv.getValue(), basicProp);
			}
		}
		
		for(InfraType each : infra.getDatabase()) {
			String name = each.getName();
			String min = "";
			String max = "";
			if(each.getVersion() != null) {
				min = each.getVersion().getMin();
				max = each.getVersion().getMax();
			}
			createISOProperty(factory, "infra_database", name + IProfileConstants.ELEM_DELIMITOR + min + IProfileConstants.ELEM_DELIMITOR + max, basicProp);
		}
		
		String commsPre = "infra_comms_";
		for(int index=0; index<infra.getComms().size(); index++) {
			Communication comm = infra.getComms().get(index);
			String strIndex = Integer.valueOf(index + 1).toString();
			for(InfraType each : comm.getMostTopProtocol()) {
				String name = each.getName();
				String min = "";
				String max = "";
				if(each.getVersion() != null) {
					min = each.getVersion().getMin();
					max = each.getVersion().getMax();
				}
				createISOProperty(factory,
								commsPre + "mostTop_" + strIndex,
								name + IProfileConstants.ELEM_DELIMITOR + min + IProfileConstants.ELEM_DELIMITOR + max,
								basicProp);
			}
			DataBus underlayingProtocol = comm.getUnderlyingProtocol();
			if(underlayingProtocol!=null) {
				String underlayingPre = commsPre + "underlaying_" + strIndex;

				createISOProperty(factory, underlayingPre + "_connectionType", underlayingProtocol.getConnectionType(), basicProp);
				createISOProperty(factory, underlayingPre + "_typePhyMac", underlayingProtocol.getTypePhyMac(), basicProp);
				
				String typeNetTrans = String.join(",", underlayingProtocol.getTypeNetTrans());
				createISOProperty(factory, underlayingPre + "_typeNetTrans", typeNetTrans, basicProp);
				
				String typeApp = String.join(",", underlayingProtocol.getTypeApp());
				createISOProperty(factory, underlayingPre + "_typeApp", typeApp, basicProp);

				createISOProperty(factory, underlayingPre + "_speed", Double.valueOf(underlayingProtocol.getSpeed()).toString(), basicProp);
				
				NVList nvListCom = underlayingProtocol.getAdditionalInfo();
				if(nvListCom!=null) {
					for(NameValue nv : nvListCom.getNv()) {
						createISOProperty(factory, underlayingPre + "_add_" + nv.getName(), nv.getValue(), basicProp);
					}
				}
			}
		}
		
		for(InfraType each : infra.getMiddleware()) {
			String name = each.getName();
			String min = "";
			String max = "";
			if(each.getVersion() != null) {
				min = each.getVersion().getMin();
				max = each.getVersion().getMax();
			}
			if(name.equals("ROS") && min.equals("2") && max.equals("2")) {
				env.setRosVersion("2");
			} else {
				createISOProperty(factory, "infra_middleware", name + IProfileConstants.ELEM_DELIMITOR + min + IProfileConstants.ELEM_DELIMITOR + max, basicProp);
			}
		}
	}

	private void convertSafeSecure(SIM source, ObjectFactory factory, List<Property> basicProp) {
		SafeSecure safeSecure = source.getSafeSecure();
		if(safeSecure!=null) {
			createISOProperty(factory, "safeSecure_overallValidSafetyLevelType", safeSecure.getOverallValidSafetyLevelType().toString(), basicProp);
			createISOProperty(factory, "safeSecure_overallSafetyLevelPL", safeSecure.getOverallSafetyLevelPL().toString(), basicProp);
			createISOProperty(factory, "safeSecure_overallSafetyLevelSIL", safeSecure.getOverallSafetyLevelSIL().toString(), basicProp);
			createISOProperty(factory, "safeSecure_overallPhySecurityLevel", safeSecure.getOverallPhySecurityLevel(), basicProp);
			createISOProperty(factory, "safeSecure_overallCybSecurityLevel", safeSecure.getOverallCybSecurityLevel(), basicProp);
			
			List<SafetyFunction> inSafetyLevel = safeSecure.getInSafetyLevel();
			for(int index=0;index<inSafetyLevel.size(); index++) {
				SafetyFunction each = inSafetyLevel.get(index);
				String strIndex = Integer.valueOf(index + 1).toString();
				String inSafetyLevelPre = "safeSecure_inSafetyLevel_" + strIndex;

				createISOProperty(factory, inSafetyLevelPre + "_safetyFunctionType", each.getSafetyFunctionType().toString(), basicProp);
				createISOProperty(factory, inSafetyLevelPre + "_validSafetyLevelType", each.getValidSafetyLevelType().toString(), basicProp);
				createISOProperty(factory, inSafetyLevelPre + "_eachSafetyLevelPL", each.getEachSafetyLevelPL().toString(), basicProp);
				createISOProperty(factory, inSafetyLevelPre + "_eachSafetyLevelSIL", each.getEachSafetyLevelSIL().toString(), basicProp);
			}

			List<CyberSecurity> inCybSecurityLevel = safeSecure.getInCybSecurityLevel();
			for(int index=0;index<inCybSecurityLevel.size(); index++) {
				CyberSecurity each = inCybSecurityLevel.get(index);
				String strIndex = Integer.valueOf(index + 1).toString();
				String inCybSecurityLevelPre = "safeSecure_inCybSecurityLevel_" + strIndex;

				createISOProperty(factory, inCybSecurityLevelPre + "_securityType", each.getSecurityType().toString(), basicProp);
				createISOProperty(factory, inCybSecurityLevelPre + "_eachSecurityLevel", each.getEachSecurityLevel().toString(), basicProp);
			}

			NVList nvList = safeSecure.getAdditionalInfo();
			if(nvList!=null) {
				for(NameValue nv : nvList.getNv()) {
					createISOProperty(factory,
							"safeSecure_add_" + nv.getName(), nv.getValue(), basicProp);
				}
			}
		}
	}

	private void convertModelling(SIM source, ObjectFactory factory, List<Property> basicProp) {
		Modelling modelling = source.getModelling();
		if(modelling!=null) {
			List<ModelCase> simulationModel = modelling.getSimulationModel();
			for(int index=0;index<simulationModel.size(); index++) {
				ModelCase each = simulationModel.get(index);
				String strIndex = Integer.valueOf(index + 1).toString();
				String modellingPre = "modelling_" + strIndex;

				createISOProperty(factory, modellingPre + "_simulator", each.getSimulator(), basicProp);
				String mdfs = String.join(",", each.getMdf());
				createISOProperty(factory, modellingPre + "_mdf", mdfs, basicProp);
				
				for(String eachLib :each.getLibraries() ) {
					createISOProperty(factory, modellingPre + "_lib", eachLib, basicProp);
				}
				
				List<ExeForm> dynamicSW = each.getDynamicSW();
				for(int idxDyn=0;idxDyn<dynamicSW.size(); idxDyn++) {
					ExeForm eachExe = dynamicSW.get(idxDyn);
					String strIdxExe = Integer.valueOf(idxDyn + 1).toString();
					String dynamicSWPre = modellingPre + "_dynamicSW_" + strIdxExe;

					createISOProperty(factory, dynamicSWPre + "_exeFileURL", eachExe.getExeFileURL(), basicProp);
					createISOProperty(factory, dynamicSWPre + "_shellCmd", eachExe.getShellCmd(), basicProp);
					
					List<org.iso.iso22166.part202.profile.Property> exePros = eachExe.getProperties();
					for(int idxProp=0; idxProp<exePros.size();idxProp++) {
						org.iso.iso22166.part202.profile.Property eachProp = exePros.get(idxProp);
						String strIdxProp = Integer.valueOf(idxProp + 1).toString();
						String propertyPre = dynamicSWPre + "_property_" + strIdxProp;

						createISOProperty(factory, propertyPre + "_value", eachProp.getValue(), basicProp);
						createISOProperty(factory, propertyPre + "_immutable", Boolean.valueOf(eachProp.isImmutable()).toString(), basicProp);
						createISOProperty(factory, propertyPre + "_description", eachProp.getDescription(), basicProp);
						createISOProperty(factory, propertyPre + "_name", eachProp.getName(), basicProp);
						createISOProperty(factory, propertyPre + "_type", eachProp.getType(), basicProp);
						createISOProperty(factory, propertyPre + "_unit", eachProp.getUnit(), basicProp);
					}
					
					NVList nvList = eachExe.getAdditionalInfo();
					if(nvList!=null) {
						for(NameValue nv : nvList.getNv()) {
							createISOProperty(factory, dynamicSWPre + "_add_" + nv.getName(), nv.getValue(), basicProp);
						}
					}
				}
				NVList nvListMod = each.getAdditionalInfo();
				if(nvListMod!=null) {
					for(NameValue nv : nvListMod.getNv()) {
						createISOProperty(factory, modellingPre + "_add_" + nv.getName(), nv.getValue(), basicProp);
					}
				}
			}
		}
	}

	private void convertExecutableForm(SIM source, ObjectFactory factory, RosProfile result, List<Property> basicProp) {
		ExecutableForm exeForms = source.getExeForm();
		if(exeForms!=null) {
			for(String each : exeForms.getLibraryURL()) {
				createISOProperty(factory, "exeForm_LibraryURL", each, basicProp);
			}
			List<ExeForm> exeForm = exeForms.getExeForm();
			for(int index=0;index<exeForm.size(); index++) {
				ExeForm eachExe = exeForm.get(index);
				
				String strIndex = Integer.valueOf(index + 1).toString();
				String exeFormPre = "exeForm_exeForm_" + strIndex;

				createISOProperty(factory, exeFormPre + "_exeFileURL", eachExe.getExeFileURL(), basicProp);
				createISOProperty(factory, exeFormPre + "_shellCmd", eachExe.getShellCmd(), basicProp);
				
				List<org.iso.iso22166.part202.profile.Property> exePros = eachExe.getProperties();
				for(int idxProp=0; idxProp<exePros.size();idxProp++) {
					org.iso.iso22166.part202.profile.Property eachProp = exePros.get(idxProp);
					String strIdxProp = Integer.valueOf(idxProp + 1).toString();
					String propertyPre = exeFormPre + "_property_" + strIdxProp;

					createISOProperty(factory, propertyPre + "_value", eachProp.getValue(), basicProp);
					createISOProperty(factory, propertyPre + "_immutable", Boolean.valueOf(eachProp.isImmutable()).toString(), basicProp);
					createISOProperty(factory, propertyPre + "_description", eachProp.getDescription(), basicProp);
					createISOProperty(factory, propertyPre + "_name", eachProp.getName(), basicProp);
					createISOProperty(factory, propertyPre + "_type", eachProp.getType(), basicProp);
					createISOProperty(factory, propertyPre + "_unit", eachProp.getUnit(), basicProp);
				}
				NVList nvList = eachExe.getAdditionalInfo();
				if(nvList!=null) {
					for(NameValue nv : nvList.getNv()) {
						createISOProperty(factory, exeFormPre + "_add_" + nv.getName(), nv.getValue(), basicProp);
					}
				}
			}
		}
	}

	private void convertNVList(SIM source, ObjectFactory factory, RosProfile result) {
		if(source.getAdditionalInfo() == null) return;
		
		BasicInfoExt basic = (BasicInfoExt)result.getBasicInfo();
		DocBasic docBasic = basic.getDoc();
		List<String> definedList = Arrays.asList("SIM_Version", "profileVersion",
												 "package_name", "class_name", "category", 
												 "doc_algorithm", "doc_inout", "doc_creator", "doc_license", "doc_reference",
												 "contact_address",
												 "ext_save_project",
												 "on_configure", "on_configure_doc_description", "on_configure_doc_pre_condition", "on_configure_doc_post_condition",
												 "on_activate", "on_activate_doc_description", "on_activate_doc_pre_condition", "on_activate_doc_post_condition",
												 "on_deactivate", "on_deactivate_doc_description", "on_deactivate_doc_pre_condition", "on_deactivate_doc_post_condition",
												 "on_cleanup", "on_cleanup_doc_description", "on_cleanup_doc_pre_condition", "on_cleanup_doc_post_condition",
												 "on_shutdown", "on_shutdown_doc_description", "on_shutdown_doc_pre_condition", "on_shutdown_doc_post_condition",
												 "on_error", "on_error_doc_description", "on_error_doc_pre_condition", "on_error_doc_post_condition"); 
		List<String> definedPrefixList = Arrays.asList("life_cycle", "timer"); 
		
		List<NameValue> nvList = source.getAdditionalInfo().getNv();
		result.setVersion(getTargetNVValue("profileVersion", nvList));
		
		basic.setPackageName(getTargetNVValue("package_name", nvList));
		basic.setClassName(getTargetNVValue("class_name", nvList));
		basic.setCategory(getTargetNVValue("category", nvList));

		docBasic.setAlgorithm(getTargetNVValue("doc_algorithm", nvList));
		docBasic.setInout(getTargetNVValue("doc_inout", nvList));
		docBasic.setCreator(getTargetNVValue("doc_creator", nvList));
		docBasic.setLicense(getTargetNVValue("doc_license", nvList));
		docBasic.setReference(getTargetNVValue("doc_reference", nvList));
		docBasic.setContactAddress(getTargetNVValue("contact_address", nvList));
		
		basic.setSaveProject(getTargetNVValue("ext_save_project", nvList));
		/////
		LifeCycleExt lifecycle = factory.createLifeCycleExt();
		result.setLifeCycle(lifecycle);
		List<NameValue> lifeCycles = getTargetStartNV("life_cycle", nvList);
		for(NameValue nv : lifeCycles) {
			if(checkNVName(nv.getName(), definedList, definedPrefixList)) continue;
			createISOProperty(factory, nv.getName(), nv.getValue(), lifecycle.getProperties());
		}
		
		List<NameValue> onConfigures = getTargetStartNV("on_configure", nvList);
		if(0 < onConfigures.size()) {
			LifecycleCallbackExt callback = factory.createLifecycleCallbackExt();
			lifecycle.setOnConfigure(callback);
			DocLifecycleCallback doc = factory.createDocLifecycleCallback();
			callback.setDoc(doc);
			
			callback.setImplementedbln(getTargetNVValueBoolean("on_configure", nvList));
			doc.setDescription(getTargetNVValue("on_configure_doc_description", nvList));
			doc.setPreCondition(getTargetNVValue("on_configure_doc_pre_condition", nvList));
			doc.setPostCondition(getTargetNVValue("on_configure_doc_post_condition", nvList));
			
			for(NameValue nv : onConfigures) {
				if(checkNVName(nv.getName(), definedList, definedPrefixList)) continue;
				createISOProperty(factory, nv.getName(), nv.getValue(), callback.getProperties());
			}
		}
		//
		List<NameValue> onActivates = getTargetStartNV("on_activate", nvList);
		if(0 < onActivates.size()) {
			LifecycleCallbackExt callback = factory.createLifecycleCallbackExt();
			lifecycle.setOnActivate(callback);
			DocLifecycleCallback doc = factory.createDocLifecycleCallback();
			callback.setDoc(doc);
			
			callback.setImplementedbln(getTargetNVValueBoolean("on_activate", nvList));
			doc.setDescription(getTargetNVValue("on_activate_doc_description", nvList));
			doc.setPreCondition(getTargetNVValue("on_activate_doc_pre_condition", nvList));
			doc.setPostCondition(getTargetNVValue("on_activate_doc_post_condition", nvList));

			for(NameValue nv : onActivates) {
				if(checkNVName(nv.getName(), definedList, definedPrefixList)) continue;
				createISOProperty(factory, nv.getName(), nv.getValue(), callback.getProperties());
			}
		}
		//
		List<NameValue> onDeactivates = getTargetStartNV("on_deactivate", nvList);
		if(0 < onDeactivates.size()) {
			LifecycleCallbackExt callback = factory.createLifecycleCallbackExt();
			lifecycle.setOnDeactivate(callback);
			DocLifecycleCallback doc = factory.createDocLifecycleCallback();
			callback.setDoc(doc);
			
			callback.setImplementedbln(getTargetNVValueBoolean("on_deactivate", nvList));
			doc.setDescription(getTargetNVValue("on_deactivate_doc_description", nvList));
			doc.setPreCondition(getTargetNVValue("on_deactivate_doc_pre_condition", nvList));
			doc.setPostCondition(getTargetNVValue("on_deactivate_doc_post_condition", nvList));

			for(NameValue nv : onDeactivates) {
				if(checkNVName(nv.getName(), definedList, definedPrefixList)) continue;
				createISOProperty(factory, nv.getName(), nv.getValue(), callback.getProperties());
			}
		}
		//
		List<NameValue> onCleanUps = getTargetStartNV("on_cleanup", nvList);
		if(0 < onCleanUps.size()) {
			LifecycleCallbackExt callback = factory.createLifecycleCallbackExt();
			lifecycle.setOnCleanup(callback);
			DocLifecycleCallback doc = factory.createDocLifecycleCallback();
			callback.setDoc(doc);
			
			callback.setImplementedbln(getTargetNVValueBoolean("on_cleanup", nvList));
			doc.setDescription(getTargetNVValue("on_cleanup_doc_description", nvList));
			doc.setPreCondition(getTargetNVValue("on_cleanup_doc_pre_condition", nvList));
			doc.setPostCondition(getTargetNVValue("on_cleanup_doc_post_condition", nvList));

			for(NameValue nv : onCleanUps) {
				if(checkNVName(nv.getName(), definedList, definedPrefixList)) continue;
				createISOProperty(factory, nv.getName(), nv.getValue(), callback.getProperties());
			}
		}
		//
		List<NameValue> onShutDowns = getTargetStartNV("on_shutdown", nvList);
		if(0 < onShutDowns.size()) {
			LifecycleCallbackExt callback = factory.createLifecycleCallbackExt();
			lifecycle.setOnShutdown(callback);
			DocLifecycleCallback doc = factory.createDocLifecycleCallback();
			callback.setDoc(doc);
			
			callback.setImplementedbln(getTargetNVValueBoolean("on_shutdown", nvList));
			doc.setDescription(getTargetNVValue("on_shutdown_doc_description", nvList));
			doc.setPreCondition(getTargetNVValue("on_shutdown_doc_pre_condition", nvList));
			doc.setPostCondition(getTargetNVValue("on_shutdown_doc_post_condition", nvList));

			for(NameValue nv : onShutDowns) {
				if(checkNVName(nv.getName(), definedList, definedPrefixList)) continue;
				createISOProperty(factory, nv.getName(), nv.getValue(), callback.getProperties());
			}
		}
		//
		List<NameValue> onErrors = getTargetStartNV("on_error", nvList);
		if(0 < onErrors.size()) {
			LifecycleCallbackExt callback = factory.createLifecycleCallbackExt();
			lifecycle.setOnError(callback);
			DocLifecycleCallback doc = factory.createDocLifecycleCallback();
			callback.setDoc(doc);
			
			callback.setImplementedbln(getTargetNVValueBoolean("on_error", nvList));
			doc.setDescription(getTargetNVValue("on_error_doc_description", nvList));
			doc.setPreCondition(getTargetNVValue("on_error_doc_pre_condition", nvList));
			doc.setPostCondition(getTargetNVValue("on_error_doc_post_condition", nvList));

			for(NameValue nv : onErrors) {
				if(checkNVName(nv.getName(), definedList, definedPrefixList)) continue;
				createISOProperty(factory, nv.getName(), nv.getValue(), callback.getProperties());
			}
		}
		//
		List<NameValue> timers = getTargetStartNV("timer", nvList);
		for(int index=0; index<timers.size(); index++) {
			String strIndex = Integer.valueOf(index).toString();
			String timerPre = "timer_" + strIndex;
			String value = getTargetNVValue(timerPre + "_rate", nvList);
			if(value == null || value.length() == 0) break;
			
			Timer timer = factory.createTimer();
			timer.setTimerName(getTargetNVValue(timerPre + "_name", nvList));
			timer.setRate(getTargetNVValueDouble(timerPre + "_rate", nvList));
			timer.setCallBack(getTargetNVValue(timerPre + "_callback", nvList));
			timer.setDescription(getTargetNVValue(timerPre + "_description", nvList));
			lifecycle.getTimers().add(timer);
		}
		
		//
		for(NameValue nv : nvList) {
			if(checkNVName(nv.getName(), definedList, definedPrefixList)) continue;
			createISOProperty(factory, nv.getName(), nv.getValue(), basic.getProperties());
		}
	}

	//////////
	private void createISOProperty(ObjectFactory factory, String name, String value, List<Property> propList) {
		createProperty(factory, name, value, IProfileConstants.ISO_PREFIX, propList);
	}
	
	private void createProperty(ObjectFactory factory, String name, String value, String prefix, List<Property> propList) {
		if(value==null || value.length() == 0) return;
		
		Property prop = factory.createProperty();
		if(name.startsWith(prefix)) {
			prop.setName(name.substring(prefix.length()));
		} else {
			prop.setName(prefix + name);
		}
		prop.setValue(value);
		propList.add(prop);
	}
	//////////
	public boolean saveXmlRos(RosProfile rosProfile, String targetFile) throws Exception {
		XmlHandlerROS handler = new XmlHandlerROS();
		
		String xmlString = handler.convertToXmlROS(rosProfile);

		String lineSeparator = System.getProperty( "line.separator" );
		if( lineSeparator==null || lineSeparator.equals("") ) lineSeparator = "\n";
		String xmlSplit[] = xmlString.split(lineSeparator);

		try(BufferedWriter outputFile = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8")) ) {
			for(int intIdx=0;intIdx<xmlSplit.length;intIdx++) {
				outputFile.write(xmlSplit[intIdx]);
				outputFile.newLine();
			}
		}
		return true;
	}
	

}
