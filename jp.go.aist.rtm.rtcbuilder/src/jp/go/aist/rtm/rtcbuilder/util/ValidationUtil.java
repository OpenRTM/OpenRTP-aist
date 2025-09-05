package jp.go.aist.rtm.rtcbuilder.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import jp.go.aist.rtm.rtcbuilder.generator.param.ConfigSetParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.DataPortParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.ServicePortInterfaceParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.ServicePortParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.IdlPathParam;
import jp.go.aist.rtm.rtcbuilder.ui.editors.IMessageConstants;

public class ValidationUtil {
	
	public static String validateDataPort(DataPortParam dataport) {
		//DataPort Name
		if( dataport.getName()==null || dataport.getName().length()==0 ) {
			return IMessageConstants.DATAPORT_VALIDATE_PORTNAME1;
		}
		if( !StringUtil.checkDigitAlphabet(dataport.getName()) ) {
			return IMessageConstants.DATAPORT_VALIDATE_PORTNAME2;
		}
		//DataPort type
		if( dataport.getType()==null || dataport.getType().length()==0 ) {
			return IMessageConstants.DATAPORT_VALIDATE_PORTTYPE;
		}
		//DataPort VarName
		if( !StringUtil.checkDigitAlphabet(dataport.getVarName()) ) {
			return IMessageConstants.DATAPORT_VALIDATE_PORTVARNAME;
		}
		return null;
	}
	
	public static String validateServicePort(ServicePortParam serviceport) {
		String result = null;
		//ServicePort name
		if( serviceport.getName()==null || serviceport.getName().trim().length()==0 ) {
			result = IMessageConstants.SERVICEPORT_VALIDATE_PORTNAME1;
			return result;
		}
		if( !StringUtil.checkDigitAlphabet(serviceport.getName()) ) {
			result = IMessageConstants.SERVICEPORT_VALIDATE_PORTNAME2;
			return result;
		}
		return result;
	}
	
	public static String validateServiceInterface(ServicePortInterfaceParam ifparam, String outputProject) {
		String result = null;
		//ServiceInterface name
		if( ifparam.getName()==null || ifparam.getName().length()==0 ) {
			result = IMessageConstants.SERVICEPORT_VALIDATE_IFNAME1;
			return result;
		}
		if( !StringUtil.checkDigitAlphabet(ifparam.getName()) ) {
			result = IMessageConstants.SERVICEPORT_VALIDATE_IFNAME2;
			return result;
		}
		//ServiceInterface Instance name
//		if( ifparam.getInstanceName()==null || ifparam.getInstanceName().length()==0 ) {
//			result = IMessageConstants.SERVICEPORT_VALIDATE_INSTNAME1;
//			return result;
//		}
		if( !StringUtil.checkDigitAlphabet(ifparam.getInstanceName()) ) {
			result = IMessageConstants.SERVICEPORT_VALIDATE_INSTNAME2;
			return result;
		}
		//ServiceInterface Var name
		if( !StringUtil.checkDigitAlphabet(ifparam.getVarName()) ) {
			result = IMessageConstants.SERVICEPORT_VALIDATE_VARNAME;
			return result;
		}
		//ServiceInterface Instance type
		if( ifparam.getInterfaceType()==null || ifparam.getInterfaceType().length()==0 ) {
			result = IMessageConstants.SERVICEPORT_VALIDATE_IFTYPE1;
			return result;
		}
		if( !StringUtil.checkDigitAlphabet(ifparam.getInterfaceType()) ) {
			result = IMessageConstants.SERVICEPORT_VALIDATE_IFTYPE2;
			return result;
		}
		
		if(ifparam.getIdlFile().length()==0) {
			if(0<ifparam.getIdlDispFile().length()) {
				String dispFile = ifparam.getIdlDispFile();
				if(dispFile.startsWith("<RTM_ROOT>")) {
					String idlFile = dispFile.replace("<RTM_ROOT>\\", System.getenv("RTM_ROOT"));
					ifparam.setIdlFile(idlFile);
				} else {
					if(outputProject!=null && 0<outputProject.length()) {
						try {
							IWorkspaceRoot workspaceHandle = ResourcesPlugin.getWorkspace().getRoot();
							IProject project = workspaceHandle.getProject(outputProject);
							IFolder path = project.getFolder("idl");
							String idlFile = dispFile.replace("idl" + System.getProperty("file.separator"), System.getProperty("file.separator"));
							if(path!=null && path.exists()) {
								ifparam.setIdlFile(path.getLocation().toOSString() + idlFile);
							}
						} catch (Exception ex) {
						}
					}
				}
			}
		}
		return result;
	}

	public static String validateConfigurationSet(ConfigSetParam config) {
		String result = null;
		//Configuration name
		if( config.getName()==null || config.getName().length()==0 ) {
			result = IMessageConstants.CONFIGURATION_VALIDATE_NAME1;
			return result;
		}
		if( !StringUtil.checkDigitAlphabet(config.getName()) ) {
			result = IMessageConstants.CONFIGURATION_VALIDATE_NAME2;
			return result;
		}
		//Configuration type
		if( config.getType()==null || config.getType().length()==0 ) {
			result = IMessageConstants.CONFIGURATION_VALIDATE_TYPE;
			return result;
		}
		//Configuration default value
		if( config.getDefaultVal()==null || config.getDefaultVal().length()==0 ) {
			result = IMessageConstants.CONFIGURATION_VALIDATE_DEFVALUE;
			return result;
		}
		//
		if( config.getVarName() != null && config.getVarName().length() > 0) {
			if( !StringUtil.checkDigitAlphabet(config.getVarName()) ) {
				result = IMessageConstants.CONFIGURATION_VALIDATE_VARIABLE;
				return result;
			}
		}
		
		//制約
		//radioは列挙型のみ
		if( config.getWidget().equals("radio") ) {
			if(config.getConstraint().trim().startsWith("(") 
					&& config.getConstraint().trim().endsWith(")") ) {
				//値の重複不可
				String[] enumVal = config.getConstraint().trim().substring(1, config.getConstraint().trim().length()-1).split(",");
				List<String> arrayVal = new ArrayList<String>();
				for(String val : enumVal) {
					if(arrayVal.contains(val.trim())) {
						result = IMessageConstants.CONFIGURATION_VALIDATE_RADIO_DUPRICATE;
						return result;
					} else {
						arrayVal.add(val.trim());
					}
				}
			} else {
				result = IMessageConstants.CONFIGURATION_VALIDATE_RADIO;
				return result;
			}
		}
		//spinはint型のみ
//		if( config.getWidget().equals("spin") ) {
//			if(!( config.getType().trim().equals("int") || config.getType().trim().equals("Integer") ) ) {
//				result = IMessageConstants.CONFIGURATION_VALIDATE_SPIN;
//				return result;
//			}
//		}
//		//spin,sliderには最大値，最小値が必要
//		if( config.getWidget().equals("spin") || config.getWidget().equals("slider")) {
//		}
		return result;
	}
	
}
