package jp.ac.meijo_u.iso22166_part202.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.iso.iso22166.part202.profile.ArgSpec;
import org.iso.iso22166.part202.profile.CompilerType;
import org.iso.iso22166.part202.profile.ExeStatus;
import org.iso.iso22166.part202.profile.ExecutionType;
import org.iso.iso22166.part202.profile.IDnType;
import org.iso.iso22166.part202.profile.IOVariables;
import org.iso.iso22166.part202.profile.InOutType;
import org.iso.iso22166.part202.profile.InfraType;
import org.iso.iso22166.part202.profile.Infrastructure;
import org.iso.iso22166.part202.profile.Libraries;
import org.iso.iso22166.part202.profile.MOType;
import org.iso.iso22166.part202.profile.ModuleID;
import org.iso.iso22166.part202.profile.NVList;
import org.iso.iso22166.part202.profile.NoBit;
import org.iso.iso22166.part202.profile.OStype;
import org.iso.iso22166.part202.profile.ObjectFactory;
import org.iso.iso22166.part202.profile.PhysicalVirtual_0020;
import org.iso.iso22166.part202.profile.Properties;
import org.iso.iso22166.part202.profile.RangeString;
import org.iso.iso22166.part202.profile.ReqProvType;
import org.iso.iso22166.part202.profile.SIM;
import org.iso.iso22166.part202.profile.ServiceMethod;
import org.iso.iso22166.part202.profile.ServiceProfile;
import org.iso.iso22166.part202.profile.Services;
import org.iso.iso22166.part202.profile.Status;
import org.iso.iso22166.part202.profile.Variable;
import org.openrtp.namespaces.ros.version01.Action;
import org.openrtp.namespaces.ros.version01.ActionExt;
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
import org.openrtp.namespaces.ros.version01.LifecycleCallbackDoc;
import org.openrtp.namespaces.ros.version01.LifecycleCallbackExt;
import org.openrtp.namespaces.ros.version01.Parameter;
import org.openrtp.namespaces.ros.version01.ParameterExt;
import org.openrtp.namespaces.ros.version01.Property;
import org.openrtp.namespaces.ros.version01.RosProfile;
import org.openrtp.namespaces.ros.version01.Service;
import org.openrtp.namespaces.ros.version01.ServiceExt;
import org.openrtp.namespaces.ros.version01.TargetEnvironment;
import org.openrtp.namespaces.ros.version01.Timer;
import org.openrtp.namespaces.ros.version01.Topic;
import org.openrtp.namespaces.ros.version01.TopicExt;

public class ROS2ISOProfileHandler extends RTC2ISOProfileHandler {
	public SIM convertROS2Iso(RosProfile source) {
		ObjectFactory factory = new ObjectFactory();

		SIM result = factory.createSIM();
		IDnType idnType = factory.createIDnType();
		result.setIdnType(idnType);
		ModuleID moduleId = factory.createModuleID();
		idnType.setModuleID(moduleId);
		
		IOVariables iovar = factory.createIOVariables();
		result.setIoVariables(iovar);
		
		Services services = factory.createServices();
		result.setServices(services);
		
		Properties properties = factory.createProperties();
		result.setProperties(properties);	
		
		Status status = factory.createStatus();
		result.setStatus(status);
		
		Infrastructure infra = factory.createInfrastructure();
		result.setInfra(infra);
		
		NVList simNv = factory.createNVList();
		result.setAdditionalInfo(simNv);

		createISONameValue(factory, "profileVersion", source.getVersion(), simNv);
		createNameValue(factory, "SIM_Version", "iso22166-202:2025", "", simNv);
		
		//BasicInfo
		BasicInfoExt basicProfile = (BasicInfoExt)source.getBasicInfo();
		result.setModuleName(basicProfile.getNodeName());
		result.setDescription(basicProfile.getDescription());
		result.setManufacturer(basicProfile.getMaintainer());
		
		idnType.setInformationModelVersion(basicProfile.getVersion());

		createISONameValue(factory, "package_name", basicProfile.getPackageName(), simNv);
		createISONameValue(factory, "class_name", basicProfile.getClassName(), simNv);
		createISONameValue(factory, "category", basicProfile.getCategory(), simNv);

		DocBasic basicDoc = basicProfile.getDoc();
		if(basicDoc != null) {
			createISONameValue(factory, "doc_algorithm", basicDoc.getAlgorithm(), simNv);
			createISONameValue(factory, "doc_inout", basicDoc.getInout(), simNv);
			createISONameValue(factory, "doc_creator", basicDoc.getCreator(), simNv);
			createISONameValue(factory, "doc_license", basicDoc.getLicense(), simNv);
			createISONameValue(factory, "doc_reference", basicDoc.getReference(), simNv);
			createISONameValue(factory, "contact_address", basicDoc.getContactAddress(), simNv);
		}
		
		createISONameValue(factory, "ext_save_project", basicProfile.getSaveProject(), simNv);

		OStype osType = factory.createOStype();
		properties.setOsType(osType);

		ExecutionType exeType = factory.createExecutionType();
		properties.getExeType().add(exeType); 

		List<Property> propList = basicProfile.getProperties();
		propList.sort(Comparator.comparing(Property::getName));
		parseBasicProperties(factory, result, propList);
		//////////
		List<String> skipNames = new ArrayList<String>(); 
		for(Topic dtopic : source.getTopics()) {
			TopicExt topic = (TopicExt)dtopic;
			String topicName = topic.getTopicName();
			if(skipNames.contains(topicName)) continue;

			Variable var = factory.createVariable();
			iovar.getVariable().add(var);
			NVList iovarNv = factory.createNVList();

			boolean isInOut = false;
			String oppBaseName = null;
			if(topicName.endsWith(IProfileConstants.INOUT_SUFFIX_IN)
					|| topicName.endsWith(IProfileConstants.INOUT_SUFFIX_OUT)) {
				String oppPortNameChk = null;
				Optional<Topic> oppPortOpt = null;
				TopicExt targetTopic = null;
				if(topicName.endsWith(IProfileConstants.INOUT_SUFFIX_IN)) {
					oppBaseName = topicName.substring(0, topicName.length() - IProfileConstants.INOUT_SUFFIX_IN.length());
					String oppPortName = oppBaseName + IProfileConstants.INOUT_SUFFIX_OUT;
					oppPortOpt = source.getTopics().stream()
					         .filter(u -> u.getTopicName().equals(oppPortName))
					         .findFirst();
					oppPortNameChk = oppPortName;
				} else if(topicName.endsWith(IProfileConstants.INOUT_SUFFIX_OUT)) {
					oppBaseName = topicName.substring(0, topicName.length() - IProfileConstants.INOUT_SUFFIX_OUT.length());
					String oppPortName = oppBaseName + IProfileConstants.INOUT_SUFFIX_IN;
					oppPortOpt = source.getTopics().stream()
					         .filter(u -> u.getTopicName().equals(oppPortName))
					         .findFirst();
					oppPortNameChk = oppPortName;
				}
				Topic oppPort = oppPortOpt.orElse(null);
				if(oppPort != null) {
					targetTopic = (TopicExt)oppPort;
				}
				if(targetTopic != null) {
					if(compareTopic(topic, targetTopic)) {
						skipNames.add(oppPortNameChk);
						isInOut = true;
					}
				}
			}

			var.setType(topic.getMessageType());
			if(isInOut) {
				var.setName(oppBaseName);
				var.setIoType(InOutType.INOUT);
			} else {
				var.setName(topic.getTopicName());
				String portType = topic.getTopicRole();
				if(portType.toLowerCase().contains("subscribe")) {
					var.setIoType(InOutType.IN);
				} else if(portType.toLowerCase().contains("publish")) {
					var.setIoType(InOutType.OUT);
				}
			}
			createISONameValue(factory, "qos_reliability_type", topic.getQoSReliabilityType(), iovarNv);
			createISONameValue(factory, "qos_history_type", topic.getQoSHistoryType(), iovarNv);
			if(topic.getQoSHistoryDepth()!=null) {
				createISONameValue(factory, "qos_depth", topic.getQoSHistoryDepth().toString(), iovarNv);
			}
			createISONameValue(factory, "variable_callback_name", topic.getVariableCallbackName(), iovarNv);
			
			DocTopic topicDoc = topic.getDoc();
			if(topicDoc != null) {
				var.setDescription(topicDoc.getDescription());
				var.setUnit(topicDoc.getUnit());
				createISONameValue(factory, "doc_type", topicDoc.getType(), iovarNv);
				createISONameValue(factory, "doc_semantics", topicDoc.getSemantics(), iovarNv);
				createISONameValue(factory, "doc_occurrence", topicDoc.getOccurrence(), iovarNv);
			}

			for(Property prop : topic.getProperties()) {
				String key = prop.getName();
				if(equalsKey(key, "value")) {
					var.setValue(prop.getValue());
				} else {
					createISONameValue(factory, prop.getName(), prop.getValue(), iovarNv);
				}
			}
			if(0<iovarNv.getNv().size()) {
				var.setAdditionalInfo(iovarNv);
			}
		}
		//////////
		services.setNoOfBasicService(BigInteger.valueOf(source.getServices().size() + source.getActions().size()));
		services.setNoOfOptionalService(BigInteger.valueOf(0));
		for(Service dsrv : source.getServices()) {
			ServiceExt srv = (ServiceExt)dsrv;  
			ServiceProfile prof = factory.createServiceProfile();
			services.getServiceProfile().add(prof);
			NVList serviceNv = factory.createNVList();
			
			prof.setId(srv.getServiceName());
			createISONameValue(factory, "service_role", srv.getServiceRole(), serviceNv);
			createISONameValue(factory, "service_type", srv.getServiceType(), serviceNv);
			createISONameValue(factory, "variable_callback_name", srv.getVariableCallbackName(), serviceNv);
			createISONameValue(factory, "kind", "service", serviceNv);
			
			DocService doc = srv.getDoc();
			if(doc != null) {
				createISONameValue(factory, "doc_description", doc.getDescription(), serviceNv);
				createISONameValue(factory, "doc_argument", doc.getArgument(), serviceNv);
				createISONameValue(factory, "doc_return", doc.getReturn(), serviceNv);
			}
			
			for(Property prop : srv.getProperties()) {
				String key = prop.getName();
				
				if(equalsKey(key, "ifurl")) {
					prof.setIfURL(prop.getValue());
				} else if(equalsKey(key, "pvtype")) {
					prof.setPvType(PhysicalVirtual_0020.valueOf(prop.getValue()));
				} else if(equalsKey(key, "motype")) {
					prof.setMoType(MOType.valueOf(prop.getValue()));
				} else {
					if(startsWithKey(prop.getName(), "method_")) continue;
					createISONameValue(factory, prop.getName(), prop.getValue(), serviceNv);
				}
			}
			if(0 < serviceNv.getNv().size()) {
				prof.setAdditionalInfo(serviceNv);
			}
			/////
			List<org.openrtp.namespaces.rtc.version03.Property> propListConv = convProperty(srv.getProperties());
			List<org.openrtp.namespaces.rtc.version03.Property> methodProps = getTargetStartProperty(propListConv, "method_");
			for(org.openrtp.namespaces.rtc.version03.Property each : methodProps) {
				String eachKey = each.getName();
				eachKey = eachKey.replace(IProfileConstants.ISO_PREFIX, "");

				String[] elems = eachKey.split("_");
				if(elems.length < 2) continue;
				String methodNoStr = elems[1];
				ServiceMethod method;
				String methodPre = "method_" + methodNoStr;
				if(eachKey.equals(methodPre + "_name") == false) continue;
				try {
					int methodNo = Integer.parseInt(methodNoStr);
					if(prof.getMethodList().size() < methodNo) {
						method = factory.createServiceMethod();
						prof.getMethodList().add(method);
					} else {
						method = prof.getMethodList().get(methodNo-1); 
					}
					method.setMethodName(getTargetPropertyValue(methodProps, methodPre + "_name"));
					method.setRetType(getTargetPropertyValue(methodProps, methodPre + "_retType"));
					method.setMoType(MOType.fromValue(getTargetPropertyValue(methodProps, methodPre + "_moType")));
					String rpType = getTargetPropertyValue(methodProps, methodPre + "_reqProvType");
					if(0 < rpType.length()) {
						method.setReqProvType(ReqProvType.fromValue(rpType));
					}
					
					List<org.openrtp.namespaces.rtc.version03.Property> methodPropList = getTargetStartProperty(methodProps, methodPre + "_add_");
					NVList mcNv = factory.createNVList();
					for(org.openrtp.namespaces.rtc.version03.Property eachP : methodPropList) {
						String orgKey = eachP.getName().replace("method_" + methodNoStr + "_add_", "");
						createISONameValue(factory, orgKey, eachP.getValue(), mcNv);
					}
					if(0 < mcNv.getNv().size()) {
						method.setAdditionalInfo(mcNv);
					}

				} catch (NumberFormatException ex) {
					continue;
				}

				List<org.openrtp.namespaces.rtc.version03.Property> argProps = getTargetStartProperty(methodProps, methodPre + "_valueName");
				for(org.openrtp.namespaces.rtc.version03.Property eachArg : argProps) {
					ArgSpec arg = factory.createArgSpec();
					method.getArgType().add(arg);
					arg.setValueName(eachArg.getValue());
					arg.setType(getTargetPropertyValue(methodProps, methodPre + "_" + eachArg.getValue() + "_type"));
					String argInout = getTargetPropertyValue(methodProps, methodPre + "_" + eachArg.getValue() + "_inout");
					if(0 < argInout.length()) {
						arg.setInout(InOutType.fromValue(argInout));
					}
					List<org.openrtp.namespaces.rtc.version03.Property> argPropList = getTargetStartProperty(methodProps, methodPre + "_" + eachArg.getValue() + "_add_");
					NVList mcNv = factory.createNVList();
					for(org.openrtp.namespaces.rtc.version03.Property eachP : argPropList) {
						String orgKey = eachP.getName().replace(methodPre + "_" + eachArg.getValue() + "_add_", "");
						createISONameValue(factory, orgKey, eachP.getValue(), mcNv);
					}
					if(0 < mcNv.getNv().size()) {
						arg.setAdditionalInfo(mcNv);
					}
				}

			}

		}
		/////
		for(Action act : source.getActions()) {
			ActionExt action = (ActionExt)act;
			ServiceProfile prof = factory.createServiceProfile();
			services.getServiceProfile().add(prof);
			NVList serviceNv = factory.createNVList();
			
			prof.setId(action.getActionName());
			createISONameValue(factory, "action_role", action.getActionRole(), serviceNv);
			createISONameValue(factory, "action_type", action.getActionType(), serviceNv);
			createISONameValue(factory, "callback_base", action.getCallbackBase(), serviceNv);
			createISONameValue(factory, "kind", "action", serviceNv);
			
			DocAction doc = action.getDoc();
			if(doc != null) {
				createISONameValue(factory, "doc_description", doc.getDescription(), serviceNv);
				createISONameValue(factory, "doc_goal", doc.getGoal(), serviceNv);
				createISONameValue(factory, "doc_feedback", doc.getFeedback(), serviceNv);
				createISONameValue(factory, "doc_result", doc.getResult(), serviceNv);
			}
			for(Property prop : action.getProperties()) {
				createISONameValue(factory, prop.getName(), prop.getValue(), serviceNv);
			}
			if(0 < serviceNv.getNv().size()) {
				prof.setAdditionalInfo(serviceNv);
			}
		}
		//////////
		for(Parameter dparam : source.getParameters()) {
			ParameterExt param = (ParameterExt)dparam;
			org.iso.iso22166.part202.profile.Property prop = factory.createProperty();
			properties.getProperty().add(prop);
			NVList propNv = factory.createNVList();
			
			prop.setName(param.getParameterName());
			prop.setType(param.getDataType());
			prop.setValue(param.getDefaultValue());
			prop.setImmutable(param.isReadOnly());
			createISONameValue(factory, "constraint", param.getConstraint(), propNv);
			
			DocParameter doc = param.getDoc();
			if(doc != null) {
				prop.setUnit(doc.getUnit());
				prop.setDescription(doc.getDescription());
				createISONameValue(factory, "doc_dataname", doc.getDataname(), propNv);
				createISONameValue(factory, "doc_default_value", doc.getDefaultValue(), propNv);
				createISONameValue(factory, "doc_range", doc.getRange(), propNv);
				createISONameValue(factory, "doc_constraint", doc.getConstraint(), propNv);
			}
			for(Property confprop : param.getProperties()) {
				String key = confprop.getName();
				String value = confprop.getValue();
				
				if(equalsKey(key, "immutable")) {
					prop.setImmutable(Boolean.valueOf(value));
				} else {
					createISONameValue(factory, key, value, propNv);
				}
			}
			if(0 < propNv.getNv().size()) {
				prop.setAdditionalInfo(propNv);
			}
		}
		//////////
		LanguageExt lang = (LanguageExt)source.getLanguage();
		if(lang != null) {
			CompilerType compiler = factory.createCompilerType();
			properties.setCompiler(compiler);
			compiler.setCompilerName(lang.getKind());
			/////
			List<org.openrtp.namespaces.rtc.version03.Property> propListConv = convProperty(lang.getProperties());
			compiler.setOsName(getTargetPropertyValue(propListConv, "os_name"));
			String osMin = getTargetPropertyValue(propListConv, "os_min");
			String osMax = getTargetPropertyValue(propListConv, "os_max");
			if((osMin != null && 0 < osMin.length()) || (osMax != null && 0 < osMax.length())) {
				RangeString osRange = factory.createRangeString();
				osRange.setMin(osMin);
				osRange.setMax(osMax);
				compiler.setVerRangeOS(osRange);
			}
			osMin = getTargetPropertyValue(propListConv, "compiler_min");
			osMax = getTargetPropertyValue(propListConv, "compiler_max");
			if((osMin != null && 0 < osMin.length()) || (osMax != null && 0 < osMax.length())) {
				RangeString range = factory.createRangeString();
				range.setMin(osMin);
				range.setMax(osMax);
				compiler.setVerRangeCompiler(range);
			}

			compiler.setBitnCPUarch(getTargetPropertyValue(propListConv, "bitnCPUarch"));
			/////
			InfraType infraType = factory.createInfraType();
			infraType.setName("ROS");
			infra.getMiddleware().add(infraType);
			
			LanguageExt langext = (LanguageExt)lang;
			TargetEnvironment env = langext.getTargets();
			if(env != null) {
				RangeString range = factory.createRangeString();
				range.setMin(env.getRosVersion());
				range.setMax(env.getRosVersion());
				infraType.setVersion(range);
				
				if(0<env.getLibraries().size()) {
					Libraries libs = factory.createLibraries();
					properties.setLibs(libs);
					for(Library each : env.getLibraries() ) {
						org.iso.iso22166.part202.profile.Library lib = factory.createLibrary();
						lib.setName(each.getName());
						lib.setVersion(each.getVersion());
						if(each.getOther() != null && 0 < each.getOther().length()) {
							NVList libNv = factory.createNVList();
							lib.setAdditionalInfo(libNv);
							createISONameValue(factory, "other", each.getOther(), libNv);
						}
						libs.getLibraries().add(lib);
					}
				}
			}
		}
		//////////
		Lifecycle lifecycle = source.getLifeCycle();
		if(lifecycle != null) {
			createLifecycleCallback(factory, lifecycle.getOnConfigure(), "on_configure", simNv);
			createLifecycleCallback(factory, lifecycle.getOnActivate(), "on_activate", simNv);
			createLifecycleCallback(factory, lifecycle.getOnDeactivate(), "on_deactivate", simNv);
			createLifecycleCallback(factory, lifecycle.getOnCleanup(), "on_cleanup", simNv);
			createLifecycleCallback(factory, lifecycle.getOnShutdown(), "on_shutdown", simNv);
			createLifecycleCallback(factory, lifecycle.getOnError(), "on_error", simNv);
			
			LifeCycleExt lifecycleExt = (LifeCycleExt)lifecycle;
			if(lifecycleExt != null) {
				for(Property prop : lifecycleExt.getProperties()) {
					createISONameValue(factory, "life_cycle_" + prop.getName(), prop.getValue(), simNv);
				}
				
				for(int index=0; index<lifecycleExt.getTimers().size(); index++) {
					Timer each = lifecycleExt.getTimers().get(index);
					String key_pre = "timer_" + Integer.valueOf(index).toString();
					createISONameValue(factory, key_pre + "_name", each.getTimerName(), simNv);
					createISONameValue(factory, key_pre + "_rate", Double.valueOf(each.getRate()).toString(), simNv);
					createISONameValue(factory, key_pre + "_callback", each.getCallBack(), simNv);
					createISONameValue(factory, key_pre + "_description", each.getDescription(), simNv);
				}
			}
		}

		return result;
	}
	
	private void createLifecycleCallback(ObjectFactory factory, LifecycleCallback cb, String name, NVList nvs) {
		if(cb != null) {
			createISONameValue(factory, name, cb.getImplemented(), nvs);
			LifecycleCallbackDoc cbDoc = (LifecycleCallbackDoc)cb;
			DocLifecycleCallback doccb = cbDoc.getDoc();
			if(doccb != null) {
				createISONameValue(factory, name + "_doc_description", doccb.getDescription(), nvs);
				createISONameValue(factory, name + "_doc_pre_condition", doccb.getPreCondition(), nvs);
				createISONameValue(factory, name + "_doc_post_condition", doccb.getPostCondition(), nvs);
			}
			
			LifecycleCallbackExt cbExt = (LifecycleCallbackExt)cb;
			if(cbExt != null) {
				for(Property prop : cbExt.getProperties()) {
					createISONameValue(factory, name + "_" + prop.getName(), prop.getValue(), nvs);
				}
			}
		}
	}

	private boolean compareTopic(TopicExt port01, TopicExt port02) {
		if(port01.getMessageType().equals(port02.getMessageType())==false) return false;
		if(port01.getQoSReliabilityType().equals(port02.getQoSReliabilityType())==false) return false;
		if(port01.getQoSHistoryType().equals(port02.getQoSHistoryType())==false) return false;
		if(port01.getQoSHistoryDepth().equals(port02.getQoSHistoryDepth())==false) return false;
		if(port01.getVariableCallbackName().equals(port02.getVariableCallbackName())==false) return false;
		
		DocTopic doc01 = port01.getDoc();
		DocTopic doc02 = port02.getDoc();
		if(doc01.getDescription().equals(doc02.getDescription())==false) return false;
		if(doc01.getType().equals(doc02.getType())==false) return false;
		if(doc01.getSemantics().equals(doc02.getSemantics())==false) return false;
		if(doc01.getUnit().equals(doc02.getUnit())==false) return false;
		if(doc01.getOccurrence().equals(doc02.getOccurrence())==false) return false;

		List<Property> props01 = port01.getProperties();
		List<Property> props02 = port02.getProperties();
		if(props01.size() != props02.size()) return false;
		for(int index=0; index<props01.size(); index++) {
			Property prop01 = props01.get(index);
			Property prop02 = props02.get(index);
			if(prop01.getName().equals(prop02.getName())==false) return false;
			if(prop01.getValue().equals(prop02.getValue())==false) return false;
		}
		return true;
	}

	private List<org.openrtp.namespaces.rtc.version03.Property> convProperty(List<Property> propListRos) {
		List<org.openrtp.namespaces.rtc.version03.Property> propList = new ArrayList<org.openrtp.namespaces.rtc.version03.Property>();
		org.openrtp.namespaces.rtc.version03.ObjectFactory factory = new org.openrtp.namespaces.rtc.version03.ObjectFactory();

		for(Property prop : propListRos) {
			org.openrtp.namespaces.rtc.version03.Property conv = factory.createProperty();
			conv.setName(prop.getName());
			conv.setValue(prop.getValue());
			propList.add(conv);
		}
		
		return propList;
	}
	
	private void parseBasicProperties(ObjectFactory factory, SIM result, List<Property> propListRos) {
		List<org.openrtp.namespaces.rtc.version03.Property> propList = convProperty(propListRos);
		
		Status status = result.getStatus();
		NVList simNv = result.getAdditionalInfo();
		
		result.setExamples(getTargetPropertyValue(propList, "examples"));

		List<org.openrtp.namespaces.rtc.version03.Property> exeTypeList = getTargetStartProperty(propList, "exeType_");
		if(0<exeTypeList.size()) {
			buildExeType(factory, result, exeTypeList);
		}
		List<org.openrtp.namespaces.rtc.version03.Property> infraList = getTargetStartProperty(propList, "infra_");
		if(0<infraList.size()) {
			buildInfrastructure(factory, result, infraList);
		}
		List<org.openrtp.namespaces.rtc.version03.Property> safesList = getTargetStartProperty(propList, "safesecure_");
		if(0<safesList.size()) {
			buildSafeSecure(factory, result, safesList);
		}
		List<org.openrtp.namespaces.rtc.version03.Property> modelList = getTargetStartProperty(propList, "modelling_");
		if(0<modelList.size()) {
			buildModelling(factory, result, modelList);
		}
		List<org.openrtp.namespaces.rtc.version03.Property> exeflList = getTargetStartProperty(propList, "exeForm_");
		if(0<exeflList.size()) {
			buildExeForm(factory, result, exeflList);
		}
		
		for(org.openrtp.namespaces.rtc.version03.Property prop : propList) {
			String key = prop.getName();
			String value = prop.getValue();
			
			if(value==null || value.length() == 0) continue;

			if(equalsKey(key, "examples")
					|| equalsKey(key, "profileVersion")
					|| key.equals("SIM_Version")
					|| startsWithKey(key, "exeType_")
					|| startsWithKey(key, "infra_")
					|| startsWithKey(key, "safesecure_")
					|| startsWithKey(key, "modelling_")
					|| startsWithKey(key, "exeform_")) continue;
				
			if(equalsKey(key, "swaspects")){
				if(value.contains(IProfileConstants.ELEM_DELIMITOR) == false) continue;
				String[] elems = value.split(IProfileConstants.ELEM_DELIMITOR);
				if(0 < elems.length) {
					ModuleID mid = factory.createModuleID();
					result.getIdnType().getSwAspects().add(mid);
					mid.getMID().add(hexStringToBytes(elems[0]));
					if(1 < elems.length) {
						mid.setIID(hexStringToBytes(elems[1]));
					}
				}

			} else if(equalsKey(key, "mID")){
				result.getIdnType().getModuleID().getMID().add(hexStringToBytes(value));
			} else if(equalsKey(key, "iID")){
				result.getIdnType().getModuleID().setIID(hexStringToBytes(value));

			} else if(equalsKey(key, "osType_type")){
				result.getProperties().getOsType().setType(value);
			} else if(equalsKey(key, "osType_bit")){
				result.getProperties().getOsType().setBit(NoBit.fromValue(value));
			} else if(equalsKey(key, "osType_version")){
				result.getProperties().getOsType().setVersion(value);

			} else if(equalsKey(key, "executionstatus")){
				status.setExecutionStatus(ExeStatus.valueOf(value));
			} else if(equalsKey(key, "errortype")){
				BigInteger val = new BigInteger(value);
				status.setErrorType(val);

			} else {
				createISONameValue(factory, key, value, simNv);
			}
		}
		if(simNv.getNv().size() == 0) {
			result.setAdditionalInfo(null);
		}
	}
}
