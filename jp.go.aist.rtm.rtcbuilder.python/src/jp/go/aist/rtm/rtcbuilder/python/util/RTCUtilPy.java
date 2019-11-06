package jp.go.aist.rtm.rtcbuilder.python.util;

import java.util.ArrayList;
import java.util.List;

import jp.go.aist.rtm.rtcbuilder.generator.param.DataTypeParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.IdlFileParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.ServiceClassParam;
import jp.go.aist.rtm.rtcbuilder.util.RTCUtil;

public class RTCUtilPy {
	public static List<String> checkDefaultModuile(List<IdlFileParam> targetFiles, List<DataTypeParam> typeList) {
		List<String> result = new ArrayList<String>();
		List<String> check = new ArrayList<String>();
		check.add("RTC");
		check.add("OpenRTM_aist");

		for(IdlFileParam target : targetFiles) {
			if(RTCUtil.checkDefault(target.getIdlPath(), typeList) == false) continue;
			if(target.isDataPort()) {
				String targetType = "";
				for(String targetTypes : target.getTargetType()) {
					if( targetTypes.contains("::") ) {
						String[] types = targetTypes.split("::");
						/////
						targetType = types[0];
						if(check.contains(targetType)==false) {
							check.add(targetType);
							result.add(targetType);
						}
					}
				}
			} else {
				String targetType = "";
				for(ServiceClassParam targetTypes : target.getServiceClassParams()) {
					targetType = targetTypes.getModule();
					targetType = targetType.replace("::", "");
					if(check.contains(targetType)==false) {
						check.add(targetType);
						result.add(targetType);
					}
				}
			}
		}
		return result;
	}
}
