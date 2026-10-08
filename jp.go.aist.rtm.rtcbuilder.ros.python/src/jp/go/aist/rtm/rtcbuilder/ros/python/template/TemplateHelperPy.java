package jp.go.aist.rtm.rtcbuilder.ros.python.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jp.go.aist.rtm.rtcbuilder.ros.param.ActionParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.ParameterParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.ROSParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.ServiceParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.TopicParam;

public class TemplateHelperPy {
	public static String getTypeName(String fullName) {
		String[] split = fullName.split("/");
		return split[split.length-1];
	}
	
	public static String getTypePackage(String fullName, ROSParam source) {
		for(String each : source.getExtMsgFiles()) {
			if(each.equals(fullName + ".msg")) {
				return source.getPackageName() + "_interfaces.msg";
			}
		}
		for(String each : source.getExtSrvFiles()) {
			if(each.equals(fullName + ".srv")) {
				return source.getPackageName() + "_interfaces.srv";
			}
		}
		for(String each : source.getExtActionFiles()) {
			if(each.equals(fullName + ".action")) {
				return source.getPackageName() + "_interfaces.action";
			}
		}
		/////
		String result = "";
		int lastSlashIndex = fullName.lastIndexOf("/");
        if (lastSlashIndex != -1) {
            String packagePart = fullName.substring(0, lastSlashIndex);
            result = packagePart.replace("/", ".");
        }
        return result;
    }
	
	public static String convTopicType(String fullName, ROSParam source) {
		for(String each : source.getExtMsgFiles()) {
			if(each.equals(fullName + ".msg")) {
				return source.getPackageName() + "_interfaces/msg/" + fullName;
			}
		}
		return fullName;
	}

	public static String convServiceType(String fullName, ROSParam source) {
		for(String each : source.getExtSrvFiles()) {
			if(each.equals(fullName + ".srv")) {
				return source.getPackageName() + "_interfaces/srv/" + fullName;
			}
		}
		return fullName;
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

	public static String getReliableType(String source) {
		if(source.toLowerCase().equals("reliable")) {
			return "RELIABLE";
		} else if(source.toLowerCase().equals("best_effort")
					|| source.toLowerCase().equals("besteffort")) {
			return "BEST_EFFORT";
		}
		return "";
	}
	
	public static String getHistoryType(String source) {
		if(source.equals("KeepLast") || source.equals("Keep Last")) {
			return "KEEP_LAST";
		} else if(source.equals("KeepAll") || source.equals("Keep All")) {
			return "KEEP_ALL";
		}
		return "";
	}
	
	public static String convDefault(ParameterParam source) {
	    if (source.getType().toLowerCase().equals("string")) {
	        return "'" + source.getDefaultValue() + "'";
	    } else if (source.getType().toLowerCase().equals("bool")) {
	    	if(source.getDefaultValue().toLowerCase().equals("true")) {
	    		return "True";
	    	} else if(source.getDefaultValue().toLowerCase().equals("false")) {
	    		return "False";
	    	}
	    }
	    return source.getDefaultValue();
	}
	
	public static String convServiceImport(ROSParam source) {
		StringBuilder builder = new StringBuilder();

		List<String> typeList = new ArrayList<String>();
		for(ServiceParam each : source.getServiceClients()) {
			if(typeList.contains(each.getType())) continue;
			typeList.add(each.getType());
		}
		for(ServiceParam each : source.getServiceServers()) {
			if(typeList.contains(each.getType())) continue;
			typeList.add(each.getType());
		}
		buildTypeInfo(builder, typeList, source);

		return builder.toString();
	}

	public static String convActionImport(ROSParam source) {
		StringBuilder builder = new StringBuilder();

		List<String> typeList = new ArrayList<String>();
		for(ActionParam each : source.getActionClients()) {
			if(typeList.contains(each.getType())) continue;
			typeList.add(each.getType());
		}
		for(ActionParam each : source.getActionServers()) {
			if(typeList.contains(each.getType())) continue;
			typeList.add(each.getType());
		}
		buildTypeInfo(builder, typeList, source);

		return builder.toString();
	}

	private static void buildTypeInfo(StringBuilder builder, List<String> typeList, ROSParam source) {
		class ServiceType {
			public String typeName;
			public List<String> elemNames = new ArrayList<String>();
		}
		List<ServiceType> candList = new ArrayList<ServiceType>();
		for(String each : typeList) {
			String typeName = getTypeName(each);
			String packageName = getTypePackage(each, source);
			Optional<ServiceType> found = candList.stream()
						                    .filter(elem -> packageName.equals(elem.typeName))
						                    .findFirst();
			if(found.isPresent()) {
				ServiceType cand = found.get();
				if(cand.elemNames.contains(typeName) == false) {
					cand.elemNames.add(typeName);
				}
			} else {
				ServiceType cand = new ServiceType();
				cand.typeName = packageName;
				cand.elemNames.add(typeName);
				candList.add(cand);
			}
		}
		for(ServiceType each : candList) {
			builder.append("from ");
			builder.append(each.typeName);
			builder.append(" import ");
			for(int index=0; index<each.elemNames.size(); index++) {
				if(0 < index) {
					builder.append(", ");
				}
				builder.append(each.elemNames.get(index));
			}
		}
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
	
	public static String convActionType(String fullName, ROSParam source) {
		for(String each : source.getExtActionFiles()) {
			if(each.equals(fullName + ".action")) {
				return source.getPackageName() + "_interfaces/action/" + fullName;
			}
		}
		return fullName;
	}
}
