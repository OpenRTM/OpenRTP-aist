package jp.go.aist.rtm.rtcbuilder.ros.template;

import java.util.ArrayList;
import java.util.List;

import jp.go.aist.rtm.rtcbuilder.ros.param.ActionParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.PackageParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.ParameterParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.ROSParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.ServiceParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.TopicParam;

public class TemplateHelperROS {
	public static String getMessageBasename(String fullName) {
		String[] split = fullName.split("/");
		return split[0];
	}
	
	public static String slash2Cc(String fullName) {
		return fullName.replaceAll("/", "::");
	}

	public static String convTopicType(String fullName, ROSParam source) {
		for(String each : source.getExtMsgFiles()) {
			if(each.equals(fullName + ".msg")) {
				return source.getPackageName() + "_interfaces/msg/" + fullName;
			}
		}
		return fullName;
	}

	public static String convTopicType4Headder(String fullName, ROSParam source) {
		for(String each : source.getExtMsgFiles()) {
			if(each.equals(fullName + ".msg")) {
				return source.getPackageName() + "_interfaces/msg/" + fullName.replaceAll("(?<!^)(?=[A-Z])", "_").toLowerCase();
			}
		}
		///
		String[] elems = fullName.split("/");
		elems[elems.length - 1] = elems[elems.length - 1].replaceAll("(?<!^)(?=[A-Z])", "_").toLowerCase(); 
		return String.join("/", elems);
	}

	public static String convServiceType(String fullName, ROSParam source) {
		for(String each : source.getExtSrvFiles()) {
			if(each.equals(fullName + ".srv")) {
				return source.getPackageName() + "_interfaces/srv/" + fullName;
			}
		}
		return fullName;
	}

	public static String convServiceType4Headder(String fullName, ROSParam source) {
		for(String each : source.getExtSrvFiles()) {
			if(each.equals(fullName + ".srv")) {
				return source.getPackageName() + "_interfaces/srv/" + fullName.replaceAll("(?<!^)(?=[A-Z])", "_").toLowerCase();
			}
		}
		///
		String[] elems = fullName.split("/");
		elems[elems.length - 1] = elems[elems.length - 1].replaceAll("(?<!^)(?=[A-Z])", "_").toLowerCase(); 
		return String.join("/", elems);
	}

	public static String convActionType(String fullName, ROSParam source) {
		for(String each : source.getExtActionFiles()) {
			if(each.equals(fullName + ".action")) {
				return source.getPackageName() + "_interfaces/action/" + fullName;
			}
		}
		return fullName;
	}

	public static String convActionType4Headder(String fullName, ROSParam source) {
		for(String each : source.getExtActionFiles()) {
			if(each.equals(fullName + ".action")) {
				return source.getPackageName() + "_interfaces/action/" + fullName.replaceAll("(?<!^)(?=[A-Z])", "_").toLowerCase();
			}
		}
		
		if(fullName.contains("/action/")) {
			String[] elems = fullName.split("/");
			elems[elems.length - 1] = elems[elems.length - 1].replaceAll("(?<!^)(?=[A-Z])", "_").toLowerCase(); 
			return String.join("/", elems);
		}
		return fullName.toLowerCase();
	}

	public static boolean notExtSrv(String fullName, ROSParam source) {
		for(String each : source.getExtSrvFiles()) {
			if(each.equals(fullName + ".srv")) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean notExtAction(String fullName, ROSParam source) {
		for(String each : source.getExtActionFiles()) {
			if(each.equals(fullName + ".action")) {
				return false;
			}
		}
		return true;
	}

	public static int toMilliSec(Double rate) {
		return (int) Math.round(rate * 1000);
	}

	public static List<String> getServicePackage(ROSParam source) {
		List<String> result = new ArrayList<String>();
		List<String> existed = getTopicTypes(source);
		
		for(ServiceParam each : source.getServiceClients()) {
			String type = each.getType();
			if(notExtSrv(type, source) == false) continue;
			String[] elems = type.split("/");
			if(elems == null || elems.length == 0) continue;
			
			if(existed.contains(elems[0])) continue;
			if(result.contains(elems[0])) continue;
			result.add(elems[0]);
		}
		for(ServiceParam each : source.getServiceServers()) {
			String type = each.getType();
			if(notExtSrv(type, source) == false) continue;
			String[] elems = type.split("/");
			if(elems == null || elems.length == 0) continue;
			
			if(existed.contains(elems[0])) continue;
			if(result.contains(elems[0])) continue;
			result.add(elems[0]);
		}
		
		return result;
	}
	public static List<String> getActionPackage(ROSParam source) {
		List<String> result = new ArrayList<String>();
		List<String> existed = getTopicTypes(source);
		
		for(ActionParam each : source.getActionClients()) {
			String type = each.getType();
			if(notExtAction(type, source) == false) {
				String typeName = source.getPackageName() + "_interfaces";
				if(existed.contains(typeName)) continue;
				if(result.contains(typeName)) continue;
				result.add(typeName);
				continue;
			}
			String[] elems = type.split("/");
			if(elems == null || elems.length == 0) continue;
			
			if(existed.contains(elems[0])) continue;
			if(result.contains(elems[0])) continue;
			
			result.add(elems[0]);
		}
		for(ActionParam each : source.getActionServers()) {
			String type = each.getType();
			if(notExtAction(type, source) == false) {
				String typeName = source.getPackageName() + "_interfaces";
				if(existed.contains(typeName)) continue;
				if(result.contains(typeName)) continue;
				result.add(typeName);
				continue;
			}
			String[] elems = type.split("/");
			if(elems == null || elems.length == 0) continue;
			
			if(existed.contains(elems[0])) continue;
			if(result.contains(elems[0])) continue;
			
			result.add(elems[0]);
		}
		
		return result;
	}
	
	public static String capitalize(String str) {
	    if (str == null || str.isEmpty()) {
	        return str;
	    }
	    String[] words = str.split("_");
	    StringBuilder sb = new StringBuilder();

	    for (String word : words) {
	        if (!word.isEmpty()) {
	            sb.append(Character.toUpperCase(word.charAt(0)));
	            sb.append(word.substring(1).toLowerCase());
	        }
	    }

	    return sb.toString();
	}

	public static String convZero(Double source) {
	    if (source == null) {
	        return "-";
	    }
	    return source.toString();
	}
	
	public static String quoteDefault(ParameterParam source) {
	    if (source.getType().toLowerCase().equals("string")) {
	        return "\"" + source.getDefaultValue() + "\"";
	    }
	    return source.getDefaultValue();
	}
	
	public static String quoteDefaultWithType(ParameterParam source) {
	    if (source.getType().toLowerCase().equals("string")) {
	        return "std::string(\"" + source.getDefaultValue() + "\")";
	    }
	    return source.getDefaultValue();
	}
	
	public static String convType(String source) {
	    if (source.equals("string")) {
	        return "std::string";
	    }
	    return source;
	}
	
	public static List<String> getIfTypes(ROSParam source) {
		List<String> result = new ArrayList<String>();
		
		boolean addPkgIf = false;
		
		for(TopicParam param : source.getTopicPublishes()) {
			String type = getMessageBasename(param.getMessageType());
			if(result.contains(type)) continue;
			if(source.getExtMsgFiles().contains(type + ".msg")) {
				addPkgIf = true;
			} else {
				result.add(type);
			}
		}
		for(TopicParam param : source.getTopicSubscribes()) {
			String type = getMessageBasename(param.getMessageType());
			if(result.contains(type)) continue;
			if(source.getExtMsgFiles().contains(type + ".msg")) {
				addPkgIf = true;
			} else {
				result.add(type);
			}
		}
		
		for(ServiceParam param : source.getServiceClients()) {
			String type = getMessageBasename(param.getType());
			if(result.contains(type)) continue;
			if(source.getExtSrvFiles().contains(type + ".srv")) {
				addPkgIf = true;
			} else {
				result.add(type);
			}
		}
		for(ServiceParam param : source.getServiceServers()) {
			String type = getMessageBasename(param.getType());
			if(result.contains(type)) continue;
			if(source.getExtSrvFiles().contains(type + ".srv")) {
				addPkgIf = true;
			} else {
				result.add(type);
			}
		}

		for(ActionParam param : source.getActionClients()) {
			String type = getMessageBasename(param.getType());
			if(result.contains(type)) continue;
			if(source.getExtActionFiles().contains(type + ".action")) {
				addPkgIf = true;
			} else {
				result.add(type);
			}
		}

		for(ActionParam param : source.getActionServers()) {
			String type = getMessageBasename(param.getType());
			if(result.contains(type)) continue;
			if(source.getExtActionFiles().contains(type + ".action")) {
				addPkgIf = true;
			} else {
				result.add(type);
			}
		}
		
		if(addPkgIf) {
			String pkgName = source.getPackageName() + "_interfaces";
			result.add(0, pkgName);
		}
		
		return result;
	}
	
	public static boolean checkCustomIF(ROSParam source) {
		for(TopicParam param : source.getTopicPublishes()) {
			String type = getMessageBasename(param.getMessageType());
			if(source.getExtMsgFiles().contains(type + ".msg")) {
				return true;
			}
		}
		for(TopicParam param : source.getTopicSubscribes()) {
			String type = getMessageBasename(param.getMessageType());
			if(source.getExtMsgFiles().contains(type + ".msg")) {
				return true;
			}
		}
		
		for(ServiceParam param : source.getServiceClients()) {
			String type = getMessageBasename(param.getType());
			if(source.getExtSrvFiles().contains(type + ".srv")) {
				return true;
			}
		}
		for(ServiceParam param : source.getServiceServers()) {
			String type = getMessageBasename(param.getType());
			if(source.getExtSrvFiles().contains(type + ".srv")) {
				return true;
			}
		}

		for(ActionParam param : source.getActionClients()) {
			String type = getMessageBasename(param.getType());
			if(source.getExtActionFiles().contains(type + ".action")) {
				return true;
			}
		}

		for(ActionParam param : source.getActionServers()) {
			String type = getMessageBasename(param.getType());
			if(source.getExtActionFiles().contains(type + ".action")) {
				return true;
			}
		}
		
		return false;
	}

	public static boolean hasDefinedIF(ROSParam source) {
		if(0 < source.getTargetEnv().getLibraries().size() ) return true;
		
		return false;
	}
	
	public static List<String> getDefinedIf(ROSParam source) {
		List<String> result = new ArrayList<String>();

		if(checkCustomIF(source)) {
			result.add(source.getPackageName() + "_interfaces");
		}
		if(0 < source.getTargetEnv().getLibraries().size() ) {
			for(PackageParam each : source.getTargetEnv().getLibraries()) {
				if(result.contains(each.getName())) continue;
				result.add(each.getName());
			}
		}

		return result;
	}
	public static List<String> getTopicTypes(ROSParam source) {
		List<String> result = new ArrayList<String>();

		boolean addPkgIf = false;

		for(TopicParam param : source.getTopicPublishes()) {
			String type = getMessageBasename(param.getMessageType());
			if(result.contains(type)) continue;
			if(source.getExtMsgFiles().contains(type + ".msg")) {
				addPkgIf = true;
			} else {
				result.add(type);
			}
		}
		for(TopicParam param : source.getTopicSubscribes()) {
			String type = getMessageBasename(param.getMessageType());
			if(result.contains(type)) continue;
			if(source.getExtMsgFiles().contains(type + ".msg")) {
				addPkgIf = true;
			} else {
				result.add(type);
			}
		}
		
		if(addPkgIf) {
			String pkgName = source.getPackageName() + "_interfaces";
			result.add(pkgName);
		}

		return result;
	}
	
	public static List<String> getTopicTypesSubscribe(ROSParam source) {
		List<String> result = new ArrayList<String>();
		List<String> existed = new ArrayList<String>();

		for(TopicParam param : source.getTopicPublishes()) {
			existed.add(param.getMessageType());
		}
		for(TopicParam param : source.getTopicSubscribes()) {
			String type = param.getMessageType();
			if(existed.contains(type)) continue;
			if(result.contains(type)) continue;
			result.add(type);
		}
		
		return result;
	}
	
	public static List<String> getServiceTypes(ROSParam source) {
		List<String> result = new ArrayList<String>();
		List<String> exsisted = getTopicTypes(source);
		
		for(ServiceParam param : source.getServiceClients()) {
			String type = getMessageBasename(param.getType());
			if(exsisted.contains(type)) continue;
			if(result.contains(type)) continue;
			if(source.getExtSrvFiles().contains(type + ".srv") == false) {
				result.add(type);
			}
		}
		for(ServiceParam param : source.getServiceServers()) {
			String type = getMessageBasename(param.getType());
			if(exsisted.contains(type)) continue;
			if(result.contains(type)) continue;
			if(source.getExtSrvFiles().contains(type + ".srv") == false) {
				result.add(type);
			}
		}
		
		return result;
	}
	
	public static List<String> getActionTypes(ROSParam source) {
		List<String> result = new ArrayList<String>();
		List<String> exsisted = getTopicTypes(source);
		exsisted.addAll(getServiceTypes(source));
		
		for(ActionParam param : source.getActionClients()) {
			String type = getMessageBasename(param.getType());
			String typeName = "";
			
			if(source.getExtActionFiles().contains(type + ".action")) {
				typeName = source.getPackageName() + "_interfaces";
			} else {
				typeName = type;
			}
			if(exsisted.contains(typeName)) continue;
			if(result.contains(typeName)) continue;
			result.add(typeName);
		}
		
		for(ActionParam param : source.getActionServers()) {
			String type = getMessageBasename(param.getType());
			String typeName = "";

			if(source.getExtActionFiles().contains(type + ".action")) {
				typeName = source.getPackageName() + "_interfaces";
			} else {
				typeName = type;
			}
			if(exsisted.contains(typeName)) continue;
			if(result.contains(typeName)) continue;
			result.add(typeName);
		}
		
		return result;
	}
	
	public static String convReliabilityType(String source) {
		if(source.toLowerCase().equals("reliable")) {
			return "reliable";
		}
		if(source.toLowerCase().equals("besteffort") || source.toLowerCase().equals("best effort")) {
			return "best_effort";
		}
		return "";
	}
}
