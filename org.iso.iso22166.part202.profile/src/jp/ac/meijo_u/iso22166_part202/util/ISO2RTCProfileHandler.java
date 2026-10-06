package jp.ac.meijo_u.iso22166_part202.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;

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
import org.iso.iso22166.part202.profile.InstanceType;
import org.iso.iso22166.part202.profile.Libraries;
import org.iso.iso22166.part202.profile.Library;
import org.iso.iso22166.part202.profile.ModelCase;
import org.iso.iso22166.part202.profile.Modelling;
import org.iso.iso22166.part202.profile.ModuleID;
import org.iso.iso22166.part202.profile.NVList;
import org.iso.iso22166.part202.profile.NameValue;
import org.iso.iso22166.part202.profile.OStype;
import org.iso.iso22166.part202.profile.OpTypes;
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
import org.openrtp.namespaces.rtc.version02.ConstraintType;
import org.openrtp.namespaces.rtc.version03.ActionStatusDoc;
import org.openrtp.namespaces.rtc.version03.Actions;
import org.openrtp.namespaces.rtc.version03.BasicInfoExt;
import org.openrtp.namespaces.rtc.version03.ConfigurationExt;
import org.openrtp.namespaces.rtc.version03.ConfigurationSet;
import org.openrtp.namespaces.rtc.version03.DataportExt;
import org.openrtp.namespaces.rtc.version03.DocAction;
import org.openrtp.namespaces.rtc.version03.DocBasic;
import org.openrtp.namespaces.rtc.version03.DocConfiguration;
import org.openrtp.namespaces.rtc.version03.DocDataport;
import org.openrtp.namespaces.rtc.version03.DocServiceinterface;
import org.openrtp.namespaces.rtc.version03.DocServiceport;
import org.openrtp.namespaces.rtc.version03.LanguageExt;
import org.openrtp.namespaces.rtc.version03.ObjectFactory;
import org.openrtp.namespaces.rtc.version03.Position;
import org.openrtp.namespaces.rtc.version03.Property;
import org.openrtp.namespaces.rtc.version03.RtcProfile;
import org.openrtp.namespaces.rtc.version03.ServiceinterfaceExt;
import org.openrtp.namespaces.rtc.version03.ServiceportExt;
import org.openrtp.namespaces.rtc.version03.TargetEnvironment;
import org.openrtp.namespaces.rtc.version03.TransmissionMethod;

import jp.go.aist.rtm.toolscommon.profiles.util.XmlHandler;

public class ISO2RTCProfileHandler {
	public RtcProfile convertIso2Rtc(SIM source) {
		ObjectFactory factory = new ObjectFactory();
		
		RtcProfile result = factory.createRtcProfile();
		result.setVersion("0.3");
		BasicInfoExt basic = factory.createBasicInfoExt();
		result.setBasicInfo(basic);
		List<Property> basicProp = basic.getProperties();
		DocBasic docBasic = factory.createDocBasic();
		basic.setDoc(docBasic);
		
		LanguageExt lang = factory.createLanguageExt();
		result.setLanguage(lang);
		TargetEnvironment env = factory.createTargetEnvironment();
		lang.getTargets().add(env);
		/////
		basic.setName(source.getModuleName());
		basic.setDescription(source.getDescription());
		basic.setVendor(source.getManufacturer());
		createISOProperty(factory, "examples", source.getExamples(), basicProp);
		createProperty(factory, "SIM_Version", "iso22166-202:2025", "", basicProp);
		
		convertIDnType(source, factory, basic);
		convertProperties(source, factory, result);
		convertIOVariables(source, factory, result);
		convertStatus(source, factory, basicProp);
		convertServices(source, factory, result);
		convertInfrastructure(source, factory, basicProp);
		convertSafeSecure(source, factory, basicProp);
		convertModelling(source, factory, basicProp);
		convertExecutableForm(source, factory, result, basicProp);
		convertNVList(source, factory, result);
		
		StringBuilder builder = new StringBuilder();
		builder.append("RTC:");
		if(basic.getVendor() != null) {
			builder.append(basic.getVendor());
		}
		builder.append(":");
		if(basic.getCategory() != null) {
			builder.append(basic.getCategory());
		}
		builder.append(":");
		if(basic.getName() != null) {
			builder.append(basic.getName());
		}
		builder.append(":");
		if(basic.getVersion()!=null) {
			builder.append(basic.getVersion());
		}
		result.setId(builder.toString());
		
		return result;
	}

	private void convertIDnType(SIM source, ObjectFactory factory, BasicInfoExt basic) {
		List<Property> basicProp = basic.getProperties();
		
		IDnType idnType = source.getIdnType();
		if(idnType == null) return;
		
		createISOProperty(factory, "mID", bytesListToHexString(idnType.getModuleID().getMID()), basic.getProperties());
		createISOProperty(factory, "iID", bytesToHexString(idnType.getModuleID().getIID()), basic.getProperties());
		
		basic.setVersion(idnType.getInformationModelVersion());
		
		if(idnType.getSwAspects() == null) return;
		
		for(ModuleID each : idnType.getSwAspects()) {
			String mId = bytesListToHexString(each.getMID());
			String iId = bytesToHexString(each.getIID());
			createISOProperty(factory, "swAspects", mId + IProfileConstants.ELEM_DELIMITOR + iId, basicProp);
		}
	}

	private void convertProperties(SIM source, ObjectFactory factory, RtcProfile result) {
		BasicInfoExt basic = (BasicInfoExt)result.getBasicInfo();
		LanguageExt lang = (LanguageExt)result.getLanguage();
		TargetEnvironment env = lang.getTargets().get(0);
		
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
				org.openrtp.namespaces.rtc.version03.Library newLib = factory.createLibrary();
				newLib.setName(each.getName());
				newLib.setVersion(each.getVersion());
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

			env.setOs(compiler.getOsName());
			RangeString verRangeOS = compiler.getVerRangeOS();
			if(verRangeOS!=null) {
				env.getOsVersions().add(verRangeOS.getMin() + IProfileConstants.ELEM_DELIMITOR + verRangeOS.getMax());
			}
			
			RangeString verRangeCompiler = compiler.getVerRangeCompiler();
			if(verRangeCompiler!=null) {
				env.setLangVersion(verRangeCompiler.getMin() + IProfileConstants.ELEM_DELIMITOR + verRangeCompiler.getMax());
			}
			
			if(compiler.getBitnCPUarch()  != null) {
				env.getCpus().add(compiler.getBitnCPUarch());
			}
			
			NVList compilerNv =  compiler.getAdditionalInfo();
			if(compilerNv!=null) {
				env.setOther(getTargetNVValue("targetOther", compilerNv.getNv()));
			}
		}
		
		List<ExecutionType> exeTypes = properties.getExeType();
		if(0<exeTypes.size()) {
			ExecutionType exeType = exeTypes.get(0);
			result.getBasicInfo().setActivityType(convertActivityType(exeType.getOpType()));
			result.getBasicInfo().setExecutionRate(exeType.getTimeConstraint());
			result.getBasicInfo().setComponentType(convertComponentType(exeType.getInstanceType()));
			createISOProperty(factory, "exeType_hardRT", Boolean.valueOf(exeType.isHardRT()).toString(), basic.getProperties());
			createISOProperty(factory, "exeType_priority", bytesToHexString(exeType.getPriority()), basic.getProperties());
			
			if(1<exeTypes.size()) {
				for(int index=1; index<exeTypes.size(); index++) {
					ExecutionType each = exeTypes.get(index);
					String ketPre = "exeType_" + Integer.valueOf(index) + "_";
					createISOProperty(factory, ketPre + "opType", each.getOpType().name(), basic.getProperties());
					createISOProperty(factory, ketPre + "hardRT", Boolean.valueOf(each.isHardRT()).toString(), basic.getProperties());
					createISOProperty(factory, ketPre + "timeConstraint", Double.valueOf(each.getTimeConstraint()).toString(), basic.getProperties());
					createISOProperty(factory, ketPre + "priority", bytesToHexString(each.getPriority()), basic.getProperties());
					createISOProperty(factory, ketPre + "instanceType", each.getInstanceType().name(), basic.getProperties());
				}
			}
		}
		
		List<org.iso.iso22166.part202.profile.Property> propList = properties.getProperty();
		if(propList != null && 0 < propList.size()) {
			ConfigurationSet configSet = factory.createConfigurationSet();
			result.setConfigurationSet(configSet);
			for(org.iso.iso22166.part202.profile.Property each : properties.getProperty() ) {
				ConfigurationExt config = factory.createConfigurationExt();
				DocConfiguration docConfig = factory.createDocConfiguration();
				config.setDoc(docConfig);
				configSet.getConfiguration().add(config);
				
				config.setDefaultValue(each.getValue());
				createISOProperty(factory, "immutable", Boolean.valueOf(each.isImmutable()).toString(), config.getProperties());
				docConfig.setDescription(each.getDescription());
				config.setName(each.getName());
				config.setType(each.getType());
				config.setUnit(each.getUnit());
				
				NVList nvList = each.getAdditionalInfo();
				if(nvList!=null) {
					List<NameValue> nvs = nvList.getNv();
					List<String> definedList = Arrays.asList("constraint",
															 "docDataname", "docDefaultValue", "docUnit", "docRange", "docConstraint",
															 "comment", "variableName");
					
					String constraintStr = getTargetNVValue("constraint", nvs);
					try {
						ConstraintType constraint = XmlHandler.convertToXmlConstraint(constraintStr);
						config.setConstraint(constraint);
					} catch (Exception e) {
					}
					docConfig.setDataname(getTargetNVValue("docDataname", nvs));
					docConfig.setDefaultValue(getTargetNVValue("docDefaultValue", nvs));
					docConfig.setUnit(getTargetNVValue("docUnit", nvs));
					docConfig.setRange(getTargetNVValue("docRange", nvs));
					docConfig.setConstraint(getTargetNVValue("docConstraint", nvs));
					
					config.setComment(getTargetNVValue("comment", nvs));
					config.setVariableName(getTargetNVValue("variableName", nvs));

					for(NameValue nv : nvList.getNv()) {
						if(checkNVName(nv.getName(), definedList)) continue;
						createISOProperty(factory, nv.getName(), nv.getValue(), config.getProperties());
					}
				}
			}
		}
	}
	
	private void convertIOVariables(SIM source, ObjectFactory factory, RtcProfile result) {
		IOVariables iOVariables = source.getIoVariables();
		if(iOVariables==null) return;
		
		List<Variable> varList = iOVariables.getVariable();
		if(varList != null && 0<varList.size()) {
			for(Variable each : varList) {
				String strInOut = each.getIoType().toString();
				if(strInOut.equals("IN")) {
					createDataPort(factory, result, each, "DataInPort", "");
				} else if(strInOut.equals("OUT")) {
					createDataPort(factory, result, each, "DataOutPort", "");
				} else if(strInOut.equals("INOUT")) {
					createDataPort(factory, result, each, "DataInPort", IProfileConstants.INOUT_SUFFIX_IN);
					createDataPort(factory, result, each, "DataOutPort", IProfileConstants.INOUT_SUFFIX_OUT);
				}
			}
		}
	}

	private void createDataPort(ObjectFactory factory, RtcProfile result, Variable each, String portType, String suffix) {
		DataportExt dataPort = factory.createDataportExt();
		DocDataport docPort = factory.createDocDataport();
		dataPort.setDoc(docPort);
		result.getDataPorts().add(dataPort);

		createISOProperty(factory, "value", each.getValue(), dataPort.getProperties());
		docPort.setDescription(each.getDescription());
		dataPort.setName(each.getName() + suffix);
		dataPort.setType(each.getType());
		dataPort.setUnit(each.getUnit());
		dataPort.setPortType(portType);
		
		NVList nvList = each.getAdditionalInfo();
		if(nvList!=null) {
			List<NameValue> nvs = nvList.getNv();
			List<String> definedList = Arrays.asList("idlFile", "interfaceType", "dataflowType", "subscriptionType",
													 "type", "number", "semantics", "unit",
													 "occurrence", "operation",
													 "comment", "variableName", "position"); 
			dataPort.setIdlFile(getTargetNVValue("idlFile", nvs));
			dataPort.setInterfaceType(getTargetNVValue("interfaceType", nvs));
			dataPort.setDataflowType(getTargetNVValue("dataflowType", nvs));
			dataPort.setSubscriptionType(getTargetNVValue("subscriptionType", nvs));
			
			docPort.setType(getTargetNVValue("type", nvs));
			docPort.setNumber(getTargetNVValue("number", nvs));
			docPort.setSemantics(getTargetNVValue("semantics", nvs));
			docPort.setUnit(getTargetNVValue("unit", nvs));
			docPort.setOccerrence(getTargetNVValue("occurrence", nvs));
			docPort.setOperation(getTargetNVValue("operation", nvs));
			
			dataPort.setComment(getTargetNVValue("comment", nvs));
			dataPort.setVariableName(getTargetNVValue("variableName", nvs));
			try {
				dataPort.setPosition(Position.fromValue(getTargetNVValue("position", nvs)));
			} catch (Exception e) {
			}

			for(NameValue nv : nvList.getNv()) {
				if(checkNVName(nv.getName(), definedList)) continue;
				createISOProperty(factory, nv.getName(), nv.getValue(), dataPort.getProperties());
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
	
	private void convertServices(SIM source, ObjectFactory factory, RtcProfile result) {
		Services services = source.getServices();
		if(services==null) return;
		
		for(ServiceProfile each : services.getServiceProfile()) {
			ServiceportExt servicePort = factory.createServiceportExt();
			result.getServicePorts().add(servicePort);
			DocServiceport docPort = factory.createDocServiceport();
			servicePort.setDoc(docPort);
			
			servicePort.setName(each.getId());
			createISOProperty(factory, "ifURL", each.getIfURL(), servicePort.getProperties());
			if(each.getPvType() != null) {
				createISOProperty(factory, "pvType", each.getPvType().toString(), servicePort.getProperties());
			}
			if(each.getMoType() != null) {
				createISOProperty(factory, "moType", each.getMoType().toString(), servicePort.getProperties());
			}
			
			
			NVList nvList = each.getAdditionalInfo();
			if(nvList!=null) {
				List<NameValue> nvs = nvList.getNv();
				List<String> definedList = Arrays.asList("description", "ifdescription",
														 "comment", "position",
														 "kind"); 

				docPort.setDescription(getTargetNVValue("description", nvs));
				docPort.setIfdescription(getTargetNVValue("ifdescription", nvs));
				
				servicePort.setComment(getTargetNVValue("comment", nvs));
				try {
					servicePort.setPosition(Position.fromValue(getTargetNVValue("position", nvs)));
				} catch (Exception e) {
				}

				List<NameValue> kinds = getTargetNV("kind", nvs);
				for(NameValue elem : kinds) {
					TransmissionMethod tm = factory.createTransmissionMethod();
					tm.setKind(elem.getValue());
					servicePort.getTransMethods().add(tm);
				}
				
				for(NameValue nv : nvList.getNv()) {
					if(checkNVName(nv.getName(), definedList)) continue;
					createISOProperty(factory, nv.getName(), nv.getValue(), servicePort.getProperties());
				}
			}
			
			for(ServiceMethod eachIf : each.getMethodList()) {
				ServiceinterfaceExt serviceIf = factory.createServiceinterfaceExt();
				servicePort.getServiceInterface().add(serviceIf);
				DocServiceinterface docIf = factory.createDocServiceinterface();
				serviceIf.setDoc(docIf);
				
				serviceIf.setName(eachIf.getMethodName());
				serviceIf.setType(eachIf.getRetType());
				if(eachIf.getMoType() != null) {
					createISOProperty(factory, "moType", eachIf.getMoType().toString(), serviceIf.getProperties());
				}
				serviceIf.setDirection(eachIf.getReqProvType().toString());
				
				NVList nvListIf = eachIf.getAdditionalInfo();
				if(nvListIf!=null) {
					List<NameValue> nvs = nvListIf.getNv();
					List<String> definedList = Arrays.asList("instanceName", "idlFile", "path",
															 "description", "docArgument", "docReturn", "docException", "docPreCondition",
															 "docPostCondition",
															 "comment", "variableName"); 

					serviceIf.setInstanceName(getTargetNVValue("instanceName", nvs));
					String fileName = getTargetNVValue("idlFile", nvs);
					String pathName = getTargetNVValue("path", nvs);
					serviceIf.setIdlFile(pathName + System.getProperty("file.separator") + fileName);
					
					docIf.setDescription(getTargetNVValue("description", nvs));
					docIf.setDocArgument(getTargetNVValue("docArgument", nvs));
					docIf.setDocReturn(getTargetNVValue("docReturn", nvs));
					docIf.setDocException(getTargetNVValue("docException", nvs));
					docIf.setDocPreCondition(getTargetNVValue("docPreCondition", nvs));
					docIf.setDocPostCondition(getTargetNVValue("docPostCondition", nvs));

					serviceIf.setComment(getTargetNVValue("comment", nvs));
					serviceIf.setVariableName(getTargetNVValue("variableName", nvs));

					for(NameValue nv : nvListIf.getNv()) {
						if(checkNVName(nv.getName(), definedList)) continue;
						createISOProperty(factory, nv.getName(), nv.getValue(), serviceIf.getProperties());
					}
				}
				
				for(ArgSpec eachArg : eachIf.getArgType()) {
					String argName = eachArg.getValueName();
					createISOProperty(factory, "argType_valueName", argName, serviceIf.getProperties());
					createISOProperty(factory, "argType_" + argName + "_type", eachArg.getType(), serviceIf.getProperties());
					if(eachArg.getInout() != null) {
						createISOProperty(factory, "argType_" + argName + "_inout", eachArg.getInout().toString(), serviceIf.getProperties());
					}

					NVList nvListArg = eachArg.getAdditionalInfo();
					if(nvListArg!=null) {
						for(NameValue nv : nvListArg.getNv()) {
							createISOProperty(factory, 
											"argType_" + argName + "_add_" + nv.getName(),
											nv.getValue(),
											serviceIf.getProperties());
						}
					}
				}
			}
		}
	}

	private void convertInfrastructure(SIM source, ObjectFactory factory, List<Property> basicProp) {
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
			createISOProperty(factory, "infra_middleware", name + IProfileConstants.ELEM_DELIMITOR + min + IProfileConstants.ELEM_DELIMITOR + max, basicProp);
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
	
	private void convertExecutableForm(SIM source, ObjectFactory factory, RtcProfile result, List<Property> basicProp) {
		ExecutableForm exeForms = source.getExeForm();
		if(exeForms!=null) {
			for(String each : exeForms.getLibraryURL()) {
				createISOProperty(factory, "exeForm_LibraryURL", each, basicProp);
			}
			List<ExeForm> exeForm = exeForms.getExeForm();
			int containerNum = 0;
			LanguageExt lang = (LanguageExt)result.getLanguage();
			lang.getTargets().clear();
			for(int index=0;index<exeForm.size(); index++) {
				ExeForm eachExe = exeForm.get(index);
				
				if(eachExe.getExeFileURL().startsWith(IProfileConstants.CONTAINER_PREFIX)) {
					List<NameValue> nv = eachExe.getAdditionalInfo().getNv();
					String middleware = getTargetNVValueRaw(IProfileConstants.CONTAINER_PREFIX + "Middleware", nv);
					String mdlVersion  = getTargetNVValueRaw(IProfileConstants.CONTAINER_PREFIX + "MiddlewareVersion", nv);
					String osVersion  = getTargetNVValueRaw(IProfileConstants.CONTAINER_PREFIX + "TargetOSVersion", nv);
					String workSpace  = getTargetNVValueRaw(IProfileConstants.CONTAINER_PREFIX + "Workspace", nv);
					String language  = getTargetNVValueRaw(IProfileConstants.CONTAINER_PREFIX + "Language", nv);
					String config  = getTargetNVValueRaw(IProfileConstants.CONTAINER_PREFIX + "Configuration", nv);
					
					TargetEnvironment env = factory.createTargetEnvironment();
					lang.getTargets().add(env);
					containerNum++;
					
					env.setOs(middleware);
					env.setCpuOther(mdlVersion);
					env.getOsVersions().add(osVersion);
					env.setOther(workSpace);
					env.setLangVersion(language);
					env.getCpus().add(config);
					
					List<NameValue> libraries = getTargetStartNVRaw(IProfileConstants.CONTAINER_PREFIX + "Libraries", nv);
					for(NameValue each : libraries) {
						createProperty(factory, IProfileConstants.CONTAINER_PREFIX + "lib_" + containerNum, each.getValue(), "", lang.getProperties());
					}
					
					List<NameValue> repositories = getTargetStartNVRaw(IProfileConstants.CONTAINER_PREFIX + "GitURL", nv);
					for(NameValue each : repositories) {
						String val = each.getValue();
						String[] elems = val.split(" ");
						if(elems.length < 2) continue;
						org.openrtp.namespaces.rtc.version03.Library lib = factory.createLibrary();
						lib.setName(elems[0]);
						lib.setVersion(elems[1]);
						env.getLibraries().add(lib);
					}

					List<NameValue> preSets = getTargetStartNVRaw(IProfileConstants.CONTAINER_PREFIX + "category", nv);
					for(NameValue each : preSets) {
						createProperty(factory, IProfileConstants.CONTAINER_PREFIX + "category_" + containerNum, each.getValue(), "", lang.getProperties());
					}
					
				} else {
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
	}
	
	private void convertNVList(SIM source, ObjectFactory factory, RtcProfile result) {
		if(source.getAdditionalInfo() == null) return;
		
		BasicInfoExt basic = (BasicInfoExt)result.getBasicInfo();
		DocBasic docBasic = basic.getDoc();
		List<String> definedList = Arrays.asList("SIM_Version", "profileVersion",
												 "rtcType", "category", "maxInstances", "executionType",
												 "abstract", "creationDate", "updateDate", "componentKind",
												 "docAlgorithm", "docDescription", "docInout", "docCreator", "docLicense",
												 "docReference",
												 "extComment", "extSaveProject", "extVersionUpLog",
												 "onInitialize", "onInitializeDocDescription", "onInitializeDocPreCondition", "onInitializeDocPostCondition",
												 "onFinalize", "onFinalizeDocDescription", "onFinalizeDocPreCondition", "onFinalizeDocPostCondition",
												 "onStartup", "onStartupDocDescription", "onStartupDocPreCondition", "onStartupDocPostCondition",
												 "onShutdown", "onShutdownDocDescription", "onShutdownDocPreCondition", "onShutdownDocPostCondition",
												 "onActivated", "onActivatedDocDescription", "onActivatedDocPreCondition", "onActivatedDocPostCondition",
												 "onDeactivated", "onDeactivatedDocDescription", "onDeactivatedDocPreCondition", "onDeactivatedDocPostCondition",
												 "onAborting", "onAbortingDocDescription", "onAbortingDocPreCondition", "onAbortingDocPostCondition",
												 "onError", "onErrorDocDescription", "onErrorDocPreCondition", "onErrorDocPostCondition",
												 "onReset", "onResetDocDescription", "onResetDocPreCondition", "onResetDocPostCondition",
												 "onExecute", "onExecuteDocDescription", "onExecuteDocPreCondition", "onExecuteDocPostCondition",
												 "onStateUpdate", "onStateUpdateDocDescription", "onStateUpdateDocPreCondition", "onStateUpdateDocPostCondition",
												 "onRateChanged", "onRateChangedDocDescription", "onRateChangedDocPreCondition", "onRateChangedDocPostCondition",
												 "onAction", "onActionDocDescription", "onActionDocPreCondition", "onActionDocPostCondition",
												 "onModeChanged", "onModeChangedDocDescription", "onModeChangedDocPreCondition", "onModeChangedDocPostCondition"); 
		
		List<NameValue> nvList = source.getAdditionalInfo().getNv();
		result.setVersion(getTargetNVValue("profileVersion", nvList));
		
		basic.setRtcType(getTargetNVValue("rtcType", nvList));
		basic.setCategory(getTargetNVValue("category", nvList));
		basic.setExecutionType(getTargetNVValue("executionType", nvList));
		basic.setComponentKind(getTargetNVValue("componentKind", nvList));
		basic.setMaxInstances(getTargetNVValueBigInteger("maxInstances", nvList));
		basic.setAbstract(getTargetNVValue("abstract", nvList));
		basic.setHardwareProfile(getTargetNVValue("hardwareProfile", nvList));
		basic.setCreationDate(getTargetNVValueCalendar("creationDate", nvList));
		basic.setUpdateDate(getTargetNVValueCalendar("updateDate", nvList));
		
		docBasic.setAlgorithm(getTargetNVValue("docAlgorithm", nvList));
		docBasic.setDescription(getTargetNVValue("docDescription", nvList));
		docBasic.setInout(getTargetNVValue("docInout", nvList));
		docBasic.setCreator(getTargetNVValue("docCreator", nvList));
		docBasic.setLicense(getTargetNVValue("docLicense", nvList));
		docBasic.setReference(getTargetNVValue("docReference", nvList));
		
		basic.setComment(getTargetNVValue("extComment", nvList));
		basic.setSaveProject(getTargetNVValue("extSaveProject", nvList));
		basic.setVersionUpLogs(getTargetNVValueList("extVersionUpLog", nvList));
		/////
		Actions actions = factory.createActions();
		result.setActions(actions);
		
		List<NameValue> onInitializes = getTargetStartNV("onInitialize", nvList);
		if(0 < onInitializes.size()) {
			ActionStatusDoc onInitialize = factory.createActionStatusDoc();
			actions.setOnInitialize(onInitialize);
			DocAction docInitialize = factory.createDocAction();
			onInitialize.setDoc(docInitialize);
			
			onInitialize.setImplementedbln(getTargetNVValueBoolean("onInitialize", nvList));
			docInitialize.setDescription(getTargetNVValue("onInitializeDocDescription", nvList));
			docInitialize.setPreCondition(getTargetNVValue("onInitializeDocPreCondition", nvList));
			docInitialize.setPostCondition(getTargetNVValue("onInitializeDocPostCondition", nvList));
		}
		//
		List<NameValue> onFinalizes = getTargetStartNV("onFinalize", nvList);
		if(0 < onFinalizes.size()) {
			ActionStatusDoc onFinalize = factory.createActionStatusDoc();
			actions.setOnFinalize(onFinalize);
			DocAction docFinalize = factory.createDocAction();
			onFinalize.setDoc(docFinalize);
			
			onFinalize.setImplementedbln(getTargetNVValueBoolean("onFinalize", nvList));
			docFinalize.setDescription(getTargetNVValue("onFinalizeDocDescription", nvList));
			docFinalize.setPreCondition(getTargetNVValue("onFinalizeDocPreCondition", nvList));
			docFinalize.setPostCondition(getTargetNVValue("onFinalizeDocPostCondition", nvList));
		}
		//
		List<NameValue> onStartups = getTargetStartNV("onStartup", nvList);
		if(0 < onStartups.size()) {
			ActionStatusDoc onStartup = factory.createActionStatusDoc();
			actions.setOnStartup(onStartup);
			DocAction docStartup = factory.createDocAction();
			onStartup.setDoc(docStartup);
			
			onStartup.setImplementedbln(getTargetNVValueBoolean("onStartup", nvList));
			docStartup.setDescription(getTargetNVValue("onStartupDocDescription", nvList));
			docStartup.setPreCondition(getTargetNVValue("onStartupDocPreCondition", nvList));
			docStartup.setPostCondition(getTargetNVValue("onStartupDocPostCondition", nvList));
		}
		//
		List<NameValue> onShutdowns = getTargetStartNV("onShutdown", nvList);
		if(0 < onShutdowns.size()) {
			ActionStatusDoc onShutdown = factory.createActionStatusDoc();
			actions.setOnShutdown(onShutdown);
			DocAction docShutdown = factory.createDocAction();
			onShutdown.setDoc(docShutdown);
			
			onShutdown.setImplementedbln(getTargetNVValueBoolean("onShutdown", nvList));
			docShutdown.setDescription(getTargetNVValue("onShutdownDocDescription", nvList));
			docShutdown.setPreCondition(getTargetNVValue("onShutdownDocPreCondition", nvList));
			docShutdown.setPostCondition(getTargetNVValue("onShutdownDocPostCondition", nvList));
		}
		//
		List<NameValue> onActivateds = getTargetStartNV("onActivated", nvList);
		if(0 < onActivateds.size()) {
			ActionStatusDoc onActivated = factory.createActionStatusDoc();
			actions.setOnActivated(onActivated);
			DocAction docActivated = factory.createDocAction();
			onActivated.setDoc(docActivated);
			
			onActivated.setImplementedbln(getTargetNVValueBoolean("onActivated", nvList));
			docActivated.setDescription(getTargetNVValue("onActivatedDocDescription", nvList));
			docActivated.setPreCondition(getTargetNVValue("onActivatedDocPreCondition", nvList));
			docActivated.setPostCondition(getTargetNVValue("onActivatedDocPostCondition", nvList));
		}
		//
		List<NameValue> onDeactivateds = getTargetStartNV("onDeactivated", nvList);
		if(0 < onDeactivateds.size()) {
			ActionStatusDoc onDeactivated = factory.createActionStatusDoc();
			actions.setOnDeactivated(onDeactivated);
			DocAction docDeactivated = factory.createDocAction();
			onDeactivated.setDoc(docDeactivated);
			
			onDeactivated.setImplementedbln(getTargetNVValueBoolean("onDeactivated", nvList));
			docDeactivated.setDescription(getTargetNVValue("onDeactivatedDocDescription", nvList));
			docDeactivated.setPreCondition(getTargetNVValue("onDeactivatedDocPreCondition", nvList));
			docDeactivated.setPostCondition(getTargetNVValue("onDeactivatedDocPostCondition", nvList));
		}
		//
		List<NameValue> onAbortings = getTargetStartNV("onAborting", nvList);
		if(0 < onAbortings.size()) {
			ActionStatusDoc onAborting = factory.createActionStatusDoc();
			actions.setOnAborting(onAborting);
			DocAction docAborting = factory.createDocAction();
			onAborting.setDoc(docAborting);
			
			onAborting.setImplementedbln(getTargetNVValueBoolean("onAborting", nvList));
			docAborting.setDescription(getTargetNVValue("onAbortingDocDescription", nvList));
			docAborting.setPreCondition(getTargetNVValue("onAbortingDocPreCondition", nvList));
			docAborting.setPostCondition(getTargetNVValue("onAbortingDocPostCondition", nvList));
		}
		//
		List<NameValue> onErrors = getTargetStartNV("onError", nvList);
		if(0 < onErrors.size()) {
			ActionStatusDoc onError = factory.createActionStatusDoc();
			actions.setOnError(onError);
			DocAction docError = factory.createDocAction();
			onError.setDoc(docError);
			
			onError.setImplementedbln(getTargetNVValueBoolean("onError", nvList));
			docError.setDescription(getTargetNVValue("onErrorDocDescription", nvList));
			docError.setPreCondition(getTargetNVValue("onErrorDocPreCondition", nvList));
			docError.setPostCondition(getTargetNVValue("onErrorDocPostCondition", nvList));
		}
		//
		List<NameValue> onResets = getTargetStartNV("onReset", nvList);
		if(0 < onResets.size()) {
			ActionStatusDoc onReset = factory.createActionStatusDoc();
			actions.setOnReset(onReset);
			DocAction docReset = factory.createDocAction();
			onReset.setDoc(docReset);
			
			onReset.setImplementedbln(getTargetNVValueBoolean("onReset", nvList));
			docReset.setDescription(getTargetNVValue("onResetDocDescription", nvList));
			docReset.setPreCondition(getTargetNVValue("onResetDocPreCondition", nvList));
			docReset.setPostCondition(getTargetNVValue("onResetDocPostCondition", nvList));
		}
		//
		List<NameValue> onExecutes = getTargetStartNV("onStateUpdate", nvList);
		if(0 < onExecutes.size()) {
			ActionStatusDoc onExecute = factory.createActionStatusDoc();
			actions.setOnExecute(onExecute);
			DocAction docExecute = factory.createDocAction();
			onExecute.setDoc(docExecute);
			
			onExecute.setImplementedbln(getTargetNVValueBoolean("onExecute", nvList));
			docExecute.setDescription(getTargetNVValue("onExecuteDocDescription", nvList));
			docExecute.setPreCondition(getTargetNVValue("onExecuteDocPreCondition", nvList));
			docExecute.setPostCondition(getTargetNVValue("onExecuteDocPostCondition", nvList));
		}
		//
		List<NameValue> onStateUpdates = getTargetStartNV("onStateUpdate", nvList);
		if(0 < onStateUpdates.size()) {
			ActionStatusDoc onStateUpdate = factory.createActionStatusDoc();
			actions.setOnStateUpdate(onStateUpdate);
			DocAction docStateUpdate = factory.createDocAction();
			onStateUpdate.setDoc(docStateUpdate);
			
			onStateUpdate.setImplementedbln(getTargetNVValueBoolean("onStateUpdate", nvList));
			docStateUpdate.setDescription(getTargetNVValue("onStateUpdateDocDescription", nvList));
			docStateUpdate.setPreCondition(getTargetNVValue("onStateUpdateDocPreCondition", nvList));
			docStateUpdate.setPostCondition(getTargetNVValue("onStateUpdateDocPostCondition", nvList));
		}
		//
		List<NameValue> onRateChangeds = getTargetStartNV("onRateChanged", nvList);
		if(0 < onRateChangeds.size()) {
			ActionStatusDoc onRateChanged = factory.createActionStatusDoc();
			actions.setOnRateChanged(onRateChanged);
			DocAction docRateChanged = factory.createDocAction();
			onRateChanged.setDoc(docRateChanged);
			
			onRateChanged.setImplementedbln(getTargetNVValueBoolean("onRateChanged", nvList));
			docRateChanged.setDescription(getTargetNVValue("onRateChangedDocDescription", nvList));
			docRateChanged.setPreCondition(getTargetNVValue("onRateChangedDocPreCondition", nvList));
			docRateChanged.setPostCondition(getTargetNVValue("onRateChangedDocPostCondition", nvList));
		}
		//
		List<NameValue> onActions = getTargetStartNV("onAction", nvList);
		if(0 < onActions.size()) {
			ActionStatusDoc onAction = factory.createActionStatusDoc();
			actions.setOnAction(onAction);
			DocAction docAction = factory.createDocAction();
			onAction.setDoc(docAction);
			
			onAction.setImplementedbln(getTargetNVValueBoolean("onAction", nvList));
			docAction.setDescription(getTargetNVValue("onActionDocDescription", nvList));
			docAction.setPreCondition(getTargetNVValue("onActionDocPreCondition", nvList));
			docAction.setPostCondition(getTargetNVValue("onActionDocPostCondition", nvList));
		}
		//
		List<NameValue> onModeChangeds = getTargetStartNV("onModeChanged", nvList);
		if(0 < onModeChangeds.size()) {
			ActionStatusDoc onModeChanged = factory.createActionStatusDoc();
			actions.setOnModeChanged(onModeChanged);
			DocAction docModeChanged = factory.createDocAction();
			onModeChanged.setDoc(docModeChanged);
			
			onModeChanged.setImplementedbln(getTargetNVValueBoolean("onModeChanged", nvList));
			docModeChanged.setDescription(getTargetNVValue("onModeChangedDocDescription", nvList));
			docModeChanged.setPreCondition(getTargetNVValue("onModeChangedDocPreCondition", nvList));
			docModeChanged.setPostCondition(getTargetNVValue("onModeChangedDocPostCondition", nvList));
		}

		
		for(NameValue nv : nvList) {
			if(checkNVName(nv.getName(), definedList)) continue;
			createISOProperty(factory, nv.getName(), nv.getValue(), basic.getProperties());
		}
	}

	//////////
	private String convertActivityType(OpTypes source) {
		if(source == OpTypes.PERIODIC) {
			return "PERIODIC";
		} else if(source == OpTypes.EVENTDRIVEN) {
			return "EVENTDRIVEN";
		} else if(source == OpTypes.NONRT) {
			return "SPORADIC";
		}
		return "";
	}

	private String convertComponentType(InstanceType source) {
		if(source == InstanceType.SINGLETON) {
			return "STATIC";
		} else if(source == InstanceType.MULTITON_STATIC) {
			return "UNIQUE";
		} else if(source == InstanceType.MULTITON_COMM) {
			return "COMMUTATIVE";
		}
		return "";
	}
	
	private boolean checkNVName(String name, List<String> definedList) {
		if(name.startsWith(IProfileConstants.ISO_PREFIX)) {
			if(definedList.contains(name.substring(IProfileConstants.ISO_PREFIX.length()))) return true;
		} else {
			if(definedList.contains(name)) return true;
		}
		return false;
	}

	private List<String> getTargetNVValueList(String key, List<NameValue> nvList) {
		String temp = getTargetNVValue(key, nvList);
		try {
			List<String> result = Arrays.asList(temp.split(","));
			return result;
		} catch (Exception ex){
			return null;
		}
	}
	
	private XMLGregorianCalendar getTargetNVValueCalendar(String key, List<NameValue> nvList) {
		String temp = getTargetNVValue(key, nvList);
		try {
			XMLGregorianCalendar val = XmlHandler.createXMLGregorianCalendar(temp);
			return val;
		} catch (Exception ex){
			return null;
		}
	}

	private BigInteger getTargetNVValueBigInteger(String key, List<NameValue> nvList) {
		String temp = getTargetNVValue(key, nvList);
		try {
			BigInteger val = new BigInteger(temp);
			return val;
		} catch (Exception ex){
			return null;
		}
	}
	
	private Boolean getTargetNVValueBoolean(String key, List<NameValue> nvList) {
		String temp = getTargetNVValue(key, nvList);
		try {
			Boolean val = Boolean.valueOf(temp);
			return val;
		} catch (Exception ex){
			return null;
		}
	}

	private String getTargetNVValue(String key, List<NameValue> nvList) {
		List<NameValue> filtered = nvList.stream()
									.filter(p -> p.getName().equals(IProfileConstants.ISO_PREFIX + key))
									.collect(Collectors.toList());
		if(filtered != null && 0 < filtered.size() ) {
			return filtered.get(0).getValue();
		}
		return "";
	}

	private String getTargetNVValueRaw(String key, List<NameValue> nvList) {
		List<NameValue> filtered = nvList.stream()
									.filter(p -> p.getName().equals(key))
									.collect(Collectors.toList());
		if(filtered != null && 0 < filtered.size() ) {
			return filtered.get(0).getValue();
		}
		return "";
	}

	private List<NameValue> getTargetNV(String key, List<NameValue> nvList) {
		List<NameValue> filtered = nvList.stream()
									.filter(p -> p.getName().equals(IProfileConstants.ISO_PREFIX + key))
									.collect(Collectors.toList());
		return filtered;
	}

	private List<NameValue> getTargetStartNV(String key, List<NameValue> nvList) {
		List<NameValue> filtered = nvList.stream()
									.filter(p -> p.getName().startsWith(IProfileConstants.ISO_PREFIX + key))
									.collect(Collectors.toList());
		return filtered;
	}
	
	private List<NameValue> getTargetStartNVRaw(String key, List<NameValue> nvList) {
		List<NameValue> filtered = nvList.stream()
									.filter(p -> p.getName().startsWith(key))
									.collect(Collectors.toList());
		return filtered;
	}

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
	
	private String bytesToHexString(byte[] bytes) {
	    if (bytes == null) return null;

	    StringBuilder sb = new StringBuilder(bytes.length * 2);
	    for (byte b : bytes) {
	        sb.append(String.format("%02x", b & 0xFF));
	    }
	    return sb.toString();
	}

	private String bytesListToHexString(List<byte[]> list) {
	    if (list == null) return null;

	    StringBuilder sb = new StringBuilder();

	    for (byte[] bytes : list) {
	        if (bytes == null) continue;
	        for (byte b : bytes) {
	            sb.append(String.format("%02x", b & 0xFF));
	        }
	    }
	    return sb.toString();
	}
	//////////
	public boolean saveXmlRtc(RtcProfile rtcProfile, String targetFile) throws Exception {
		XmlHandler handlerRtc = new XmlHandler();
		
		String xmlString = handlerRtc.convertToXmlRtc(rtcProfile);

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

	public String convertToXmlRtc(SIM profile) throws Exception {
		String xmlString = "";
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance("org.openrtp.namespaces.rtc.version03");
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper",
					new NamespacePrefixMapperImpl("http://www.openrtp.org/namespaces/rtc"));
			StringWriter xmlFileWriter = new StringWriter();
			marshaller.marshal(profile, xmlFileWriter);
			xmlString = xmlFileWriter.toString();
		} catch (JAXBException exception) {
			throw new Exception("XML Transformation Failed.", exception);
		}
		return xmlString;
	}
	
	public SIM restoreFromFileIso(String targetXML) throws Exception {
		String splitter = "\n";
	    StringBuilder stbRet = new StringBuilder();
	    try (BufferedReader br = new BufferedReader(
	            new InputStreamReader(new FileInputStream(targetXML), StandardCharsets.UTF_8))) {

	        String str;
	        while ((str = br.readLine()) != null) {
	            stbRet.append(str).append(splitter);
	        }

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    String strXml = stbRet.toString();
	    return restoreFromXmlIso(strXml);
	}
	
	public SIM restoreFromXmlIso(String targetXML) throws Exception {
		SIM result = null;
	    StringReader xmlReader = new StringReader(targetXML);
		JAXBContext jc = JAXBContext.newInstance(
			        		org.iso.iso22166.part202.profile.ObjectFactory.class
			    			);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		unmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
	    Object profile = ((JAXBElement<?>)unmarshaller.unmarshal(xmlReader)).getValue();
	    //
    	result = (SIM)profile;

	    return result;
	}


}
