package jp.go.aist.rtm.rtcbuilder.template;

import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DEFAULT_SVC_IMPL_SUFFIX;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DEFAULT_SVC_SKEL_SUFFIX;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DEFAULT_SVC_STUB_SUFFIX;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_AUTHOR_OFFSET;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_CONSTRAINT_OFFSET;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_CONSTRAINT_PREFIX;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_CYCLE_OFFSET;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_CYCLE_PREFIX;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_DEFAULT_OFFSET;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_DEFAULT_PREFIX;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_DEFAULT_WIDTH;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_DESC_OFFSET;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_DESC_PREFIX;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_INTERFACE_DETAIL_OFFSET;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_INTERFACE_DETAIL_PREFIX;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_INTERFACE_OFFSET;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_INTERFACE_PREFIX;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_NUMBER_OFFSET;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_NUMBER_PREFIX;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_POST_OFFSET;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_PRE_OFFSET;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_RANGE_OFFSET;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_RANGE_PREFIX;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_SEMANTICS_OFFSET;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_SEMANTICS_PREFIX;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_UNIT_OFFSET;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DOC_UNIT_PREFIX;
import static jp.go.aist.rtm.rtcbuilder.util.StringUtil.splitString;

import java.io.File;
import java.util.List;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.fsm.EventParam;
import jp.go.aist.rtm.rtcbuilder.fsm.StateParam;
import jp.go.aist.rtm.rtcbuilder.fsm.TransitionParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.ConfigSetParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.DataPortParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.EventPortParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.PropertyParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.RtcParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.ServicePortParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.IdlFileParam;
import jp.go.aist.rtm.rtcbuilder.util.RTCUtil;

/**
 * テンプレートを出力する際に使用されるヘルパー
 */
public class TemplateHelper {

	/**
	 * ベース名を取得する
	 * 
	 * @param fullName
	 * @return
	 */
	public static String getBasename(String fullName) {
		String[] split = fullName.split("::");
		return split[split.length - 1];
	}

	/*
	 *  Appebd By I.Hara 2015/06
	 **/
	public static String getBasename2(String fullName) {
		return fullName.replaceAll("::", "_");
	}
	/**
	 * ファイル名を取得する
	 * 
	 * @param fullPath
	 * @return
	 */
	public static String getFileName(String fullPath) {
		if (fullPath == null)
			return "";
		File file = new File(fullPath);
		return file.getName();
	}

	/**
	 * 拡張子無しファイル名を取得する
	 * 
	 * @param fullPath
	 * @return
	 */
	public static String getFilenameNoExt(String fullPath) {
		String fileName = getFileName(fullPath);
		if (fileName == null)
			return "";
		int index = fileName.lastIndexOf('.');
		if (index > 0 && index < fileName.length() - 1) {
			return fileName.substring(0, index);
		}
		return "";
	}

	public static String getIDLFilesforIDLCMake(RtcParam source) {
		StringBuilder builder = new StringBuilder();
		
		for(IdlFileParam target : source.getProviderIdlPathes() ) {
			if(RTCUtil.checkDefault(target.getIdlPath(), source.getParent().getDataTypeParams())) continue;
			builder.append("${CMAKE_CURRENT_SOURCE_DIR}/");
			builder.append(getFilenameNoExt(target.getIdlFile()));
			builder.append(".idl ");
		}
		for(IdlFileParam target : source.getConsumerIdlPathes() ) {
			if(RTCUtil.checkDefault(target.getIdlPath(), source.getParent().getDataTypeParams())) continue;
			builder.append("${CMAKE_CURRENT_SOURCE_DIR}/");
			builder.append(getFilenameNoExt(target.getIdlFile()));
			builder.append(".idl ");
		}
		for(IdlFileParam target : source.getIncludedIdlPathes() ) {
			if(RTCUtil.checkDefault(target.getIdlPath(), source.getParent().getDataTypeParams())) continue;
			builder.append("${CMAKE_CURRENT_SOURCE_DIR}/");
			builder.append(getFilenameNoExt(target.getIdlFile()));
			builder.append(".idl ");
		}
		return builder.toString();
	}
	
	public static String getIDLFiles(RtcParam source) {
		StringBuilder builder = new StringBuilder();
		
		for(IdlFileParam target : source.getProviderIdlPathes() ) {
			if(RTCUtil.checkDefault(target.getIdlPath(), source.getParent().getDataTypeParams())) continue;
			builder.append(getFilenameNoExt(target.getIdlFile()));
			builder.append(".idl ");
		}
		for(IdlFileParam target : source.getConsumerIdlPathes() ) {
			if(RTCUtil.checkDefault(target.getIdlPath(), source.getParent().getDataTypeParams())) continue;
			builder.append(getFilenameNoExt(target.getIdlFile()));
			builder.append(".idl ");
		}
		for(IdlFileParam target : source.getIncludedIdlPathes() ) {
			if(RTCUtil.checkDefault(target.getIdlPath(), source.getParent().getDataTypeParams())) continue;
			builder.append(getFilenameNoExt(target.getIdlFile()));
			builder.append(".idl ");
		}
		return builder.toString();
	}
	
	public static String toSvcImpl(String fullPath) {
		String name = getFilenameNoExt(fullPath);
		if (name.isEmpty()) {
			return name;
		}
		return name + getServiceImplSuffix();
	}

	public static String toSvcSkel(String fullPath) {
		String name = getFilenameNoExt(fullPath);
		if (name.isEmpty()) {
			return name;
		}
		return name + getServiceSkelSuffix();
	}

	public static String toSvcStub(String fullPath) {
		String name = getFilenameNoExt(fullPath);
		if (name.isEmpty()) {
			return name;
		}
		return name + getServiceStubSuffix();
	}

	public String convFormat(String source) {
		return source.replace("::", ".");
	}

	public boolean isModule(String source) {
		return source.contains("::");
	}

	public boolean isCpp(RtcParam source) {
		return source.getLangList().contains(IRtcBuilderConstants.LANG_CPP);
	}
	
	public static String getServiceImplSuffix() {
		return DEFAULT_SVC_IMPL_SUFFIX;
	}

	public static String getServiceSkelSuffix() {
		return DEFAULT_SVC_SKEL_SUFFIX;
	}

	public static String getServiceStubSuffix() {
		return DEFAULT_SVC_STUB_SUFFIX;
	}

	public String convertDoc(String source) {
		return splitString(source, DOC_DEFAULT_WIDTH, DOC_DEFAULT_PREFIX,
				DOC_DEFAULT_OFFSET);
	}

	public String convertDescDoc(String source) {
		return splitString(source, DOC_DEFAULT_WIDTH, DOC_DESC_PREFIX,
				DOC_DESC_OFFSET);
	}

	public String convertPreDoc(String source) {
		return splitString(source, DOC_DEFAULT_WIDTH, DOC_DESC_PREFIX,
				DOC_PRE_OFFSET);
	}

	public String convertPostDoc(String source) {
		return splitString(source, DOC_DEFAULT_WIDTH, DOC_DESC_PREFIX,
				DOC_POST_OFFSET);
	}

	public String convertUnitDoc(String source) {
		return splitString(source, DOC_DEFAULT_WIDTH, DOC_UNIT_PREFIX,
				DOC_UNIT_OFFSET);
	}

	public String convertRangeDoc(String source) {
		return splitString(source, DOC_DEFAULT_WIDTH, DOC_RANGE_PREFIX,
				DOC_RANGE_OFFSET);
	}

	public String convertConstraintDoc(String source) {
		return splitString(source, DOC_DEFAULT_WIDTH, DOC_CONSTRAINT_PREFIX,
				DOC_CONSTRAINT_OFFSET);
	}

	public String convertTypeDoc(String source) {
		return splitString(source, DOC_DEFAULT_WIDTH, DOC_UNIT_PREFIX,
				DOC_UNIT_OFFSET);
	}

	public String convertNumberDoc(String source) {
		return splitString(source, DOC_DEFAULT_WIDTH, DOC_NUMBER_PREFIX,
				DOC_NUMBER_OFFSET);
	}

	public String convertSemanticsDoc(String source) {
		return splitString(source, DOC_DEFAULT_WIDTH, DOC_SEMANTICS_PREFIX,
				DOC_SEMANTICS_OFFSET);
	}

	public String convertFrequencyDoc(String source) {
		return splitString(source, DOC_DEFAULT_WIDTH, DOC_SEMANTICS_PREFIX,
				DOC_SEMANTICS_OFFSET);
	}

	public String convertCycleDoc(String source) {
		return splitString(source, DOC_DEFAULT_WIDTH, DOC_CYCLE_PREFIX,
				DOC_CYCLE_OFFSET);
	}

	public String convertInterfaceDoc(String source) {
		return splitString(source, DOC_DEFAULT_WIDTH, DOC_INTERFACE_PREFIX,
				DOC_INTERFACE_OFFSET);
	}

	public String convertInterfaceDetailDoc(String source) {
		return splitString(source, DOC_DEFAULT_WIDTH,
				DOC_INTERFACE_DETAIL_PREFIX, DOC_INTERFACE_DETAIL_OFFSET);
	}

	public String convertAuthorDoc(String source) {
		return splitString(source, DOC_DEFAULT_WIDTH, DOC_DEFAULT_PREFIX,
				DOC_AUTHOR_OFFSET);
	}

	public static String toLower(String s) {
		return (s == null) ? "" : s.toLowerCase();
	}

	public static String getVerMajor(String ver) {
		if (ver == null || ver.isEmpty()) {
			return "";
		}
		String[] vers = ver.split("\\.");
		return (vers.length > 0) ? vers[0] : "0";
	}

	public static String getVerMinor(String ver) {
		if (ver == null || ver.isEmpty()) {
			return "";
		}
		String[] vers = ver.split("\\.");
		return (vers.length > 1) ? vers[1] : "0";
	}

	public static String getVerPatch(String ver) {
		if (ver == null || ver.isEmpty()) {
			return "";
		}
		String[] vers = ver.split("\\.");
		return (vers.length > 2) ? vers[2] : "0";
	}
	
	public static String convFormatted(String source, int len) {
		int clen = source.length();
		StringBuilder builder = new StringBuilder();
		builder.append(source);
		for(int index=0;index<(len-clen);index++) {
			builder.append(" ");
		}
		return builder.toString();
	}
	
	public boolean checkNotWidget(RtcParam param) {
		for(ConfigSetParam target : param.getConfigParams()) {
			if( target.getWidget()!=null && 0<target.getWidget().length() ) {
				return false;
			}
		}
		return true;
	}
	
	public boolean checkNotConstraint(RtcParam param) {
		for(ConfigSetParam target : param.getConfigParams()) {
			if( target.getConstraint()!=null && 0<target.getConstraint().length() ) {
				return false;
			}
		}
		return true;
	}
	
	public boolean checkDetailContent(int index, RtcParam param) {
		if(param.getDetailContent(index)==null || param.getDetailContent(index).length()==0)
			return false;
		return true;
	}
	
	public boolean checkContents(String target) {
		if( target==null || target.length()==0 )
			return false;
		return true;
	}
	
	public String getHistory(StateParam param) {
		if(param.getHistory()==2) {
			return "    DEEPHISTORY()";
		} else if(param.getHistory()==1) {
			return "    HISTORY()";
		}
		return "  ";
	}
	
	public boolean checkFSM(RtcParam param) {
		PropertyParam fsm = param.getProperty(IRtcBuilderConstants.PROP_TYPE_FSM);
		if(fsm==null) return false;
		
		if(Boolean.valueOf(fsm.getValue())) {
			PropertyParam fsmType = param.getProperty(IRtcBuilderConstants.PROP_TYPE_FSMTYTPE);
			if(fsmType==null) return false;
			String strType = fsmType.getValue();
			if(strType.equals(IRtcBuilderConstants.FSMTYTPE_STATIC)) return true;
		}
		return false;
	}
	
	public boolean existFSMEvent(StateParam source) {
		if(source==null) return false;
		for(TransitionParam each : source.getAllTransList()) {
			if(0< each.getEvent().trim().length()) return true;
		}
		return false;
	}
	
	public String getFSMType(RtcParam param) {
		PropertyParam fsmType = param.getProperty(IRtcBuilderConstants.PROP_TYPE_FSMTYTPE);
		if(fsmType!=null) {
			String strType = fsmType.getValue();
			if(strType.equals(IRtcBuilderConstants.FSMTYTPE_STATIC)) {
				return "StaticFSM";
			} else if(strType.equals(IRtcBuilderConstants.FSMTYTPE_DYNAMIC)) {
				return "DynamicFSM";
			}
		}
		return "";
	}
	
	public String getTopFSMName(RtcParam param) {
		StateParam state = param.getFsmParam();
		if(state==null) return "";
		return state.getName();
	}
	
	public String getInitialState(RtcParam param) {
		StateParam state = param.getFsmParam();
		String initial = state.getInitialState();
		String startNode = "";
		for(TransitionParam each : state.getAllTransList()) {
			if(each.getSource().equals(initial)) {
				startNode = each.getTarget();
				break;
			}
		}

		return startNode;
	}
	
	public String checkTransition(StateParam state) {
		TransitionParam targetTans = null;
		for(TransitionParam trans : state.getTransList()) {
			if(trans.getEvent().trim().length()==0) {
				targetTans = trans;
			}
		}
		if(targetTans!=null) {
			StringBuilder builder = new StringBuilder();
			builder.append("  setState<").append(targetTans.getTarget()).append(">();");
			return builder.toString();
		}
		return "";
	}
	
	public String getConnectorString(RtcParam param) {
		StringBuilder builder = new StringBuilder();
		
		for(DataPortParam port : param.getInports() ) {
			if(port.getType().length()==0) continue;
			if(0<builder.length()) builder.append(",");
            builder.append("${PROJECT_NAME}0.").append(port.getName());
            builder.append("?port=${PROJECT_NAME}Test0.").append(port.getName());
		}
		for(DataPortParam port : param.getOutports() ) {
			if(port.getType().length()==0) continue;
			if(0<builder.length()) builder.append(",");
            builder.append("${PROJECT_NAME}0.").append(port.getName());
            builder.append("?port=${PROJECT_NAME}Test0.").append(port.getName());
		}
		//
		for(ServicePortParam port : param.getServicePorts() ) {
			if(0<builder.length()) builder.append(",");
            builder.append("${PROJECT_NAME}0.").append(port.getName());
            builder.append("?port=${PROJECT_NAME}Test0.").append(port.getName());
		}
		
		EventPortParam evPort = param.getEventport();
		if(evPort!=null) {
			for(EventParam event : evPort.getEvents() ) {
				if(event.getName()==null || event.getName().length()==0) continue;
				if(0<builder.length()) builder.append(",");
				
				builder.append("${PROJECT_NAME}0.").append(evPort.getName());
				
				builder.append("?port=${PROJECT_NAME}Test0.").append(event.getName());
				builder.append("&fsm_event_name=").append(event.getName());
			}
		}
		
		return builder.toString();
	}
	
	public String getFSMNodeInfo(StateParam param) {
		String br = System.getProperty("line.separator");		
		StringBuilder builder = new StringBuilder();
		
		for(StateParam state : param.getAllStateList()) {
			if(state.isInitial()) {
				builder.append("  <tr>").append(br);
				builder.append("    <td>").append(state.getName()).append("</td>").append(br);
				builder.append("    <td colspan=\"2\">Initial State</td>").append(br);
				builder.append("  </tr>").append(br);
			} else if(state.isFinal()) {
				builder.append("  <tr>").append(br);
				builder.append("    <td>").append(state.getName()).append("</td>").append(br);
				builder.append("    <td colspan=\"2\">Final State</td>").append(br);
				builder.append("  </tr>").append(br);
			} else {
				List<TransitionParam> transList = state.getTransList();
				if(transList.size()==0) {
					builder.append("  <tr>").append(br);
					builder.append("    <td rowspan=\"").append(transList.size()).append("\">").append(state.getName()).append("</td>").append(br);
					builder.append("    <td></td>").append(br);
					builder.append("    <td></td>").append(br);
					builder.append("  </tr>").append(br);
				} else {
					builder.append("  <tr>").append(br);
					builder.append("    <td rowspan=\"").append(transList.size()).append("\">").append(state.getName()).append("</td>").append(br);
					builder.append("    <td>").append(transList.get(0).getEvent()).append("</td>").append(br);
					builder.append("    <td>").append(transList.get(0).getTarget()).append("</td>").append(br);
					builder.append("  </tr>").append(br);
					if( 1<state.getTransList().size() ) {
						for(int index=1; index<state.getTransList().size(); index++) {
							builder.append("  <tr>").append(br);
							builder.append("    <td>").append(transList.get(index).getEvent()).append("</td>").append(br);
							builder.append("    <td>").append(transList.get(index).getTarget()).append("</td>").append(br);
							builder.append("  </tr>").append(br);
						}
					}
				}
			}
		}
		return builder.toString();
	}

	public String getFSMEventInfo(StateParam param) {
		String br = System.getProperty("line.separator");		
		StringBuilder builder = new StringBuilder();
		
		for(TransitionParam tranParam : param.getAllTransList()) {
			if(tranParam.getEventParam()==null) continue;
			builder.append("##### ").append(tranParam.getEventParam().getName()).append(br);
			builder.append(br);
			builder.append(tranParam.getEventParam().getDoc_description()).append(br);
			builder.append(br);
			builder.append("<table>").append(br);
			builder.append("  <tr>").append(br);
			builder.append("    <td>Source State</td>").append(br);
			builder.append("    <td colspan=\"2\">").append(tranParam.getEventParam().getSource()).append("</td>").append(br);
			builder.append("  </tr>").append(br);
			
			builder.append("  <tr>").append(br);
			builder.append("    <td>Target State</td>").append(br);
			builder.append("    <td colspan=\"2\">").append(tranParam.getEventParam().getTarget()).append("</td>").append(br);
			builder.append("  </tr>").append(br);
			
			builder.append("  <tr>").append(br);
			builder.append("    <td>DataType</td>").append(br);
			builder.append("    <td>").append(tranParam.getEventParam().getDataType()).append("</td>").append(br);
			builder.append("    <td>").append(tranParam.getEventParam().getDoc_type()).append("</td>").append(br);
			builder.append("  </tr>").append(br);
			
			builder.append("  <tr>").append(br);
			builder.append("    <td>Number of Data</td>").append(br);
			builder.append("    <td colspan=\"2\">").append(tranParam.getEventParam().getDoc_num()).append("</td>").append(br);
			builder.append("  </tr>").append(br);
			
			builder.append("  <tr>").append(br);
			builder.append("    <td>Unit</td>").append(br);
			builder.append("    <td colspan=\"2\">").append(tranParam.getEventParam().getDoc_unit()).append("</td>").append(br);
			builder.append("  </tr>").append(br);
			
			builder.append("  <tr>").append(br);
			builder.append("    <td>Operational frecency Period</td>").append(br);
			builder.append("    <td colspan=\"2\">").append(tranParam.getEventParam().getDoc_operation()).append("</td>").append(br);
			builder.append("  </tr>").append(br);
			
			builder.append("</table>").append(br);
			builder.append(br);
			builder.append(tranParam.getEventParam().getDoc_semantics()).append(br);
			builder.append(br);
		}
		
		return builder.toString();
	}
	public boolean checkUserDefined(RtcParam rtcParam) {
		boolean result = false;
		for(IdlFileParam target : rtcParam.getProviderIdlPathes()) {
			if(RTCUtil.checkDefault(target.getIdlPath(), rtcParam.getParent().getDataTypeParams())) continue;
			result = true;
			break;
		}
		if(result == false) {
			for(IdlFileParam target : rtcParam.getConsumerIdlPathes()) {
				if(RTCUtil.checkDefault(target.getIdlPath(), rtcParam.getParent().getDataTypeParams())) continue;
				result = true;
				break;
			}
		}
		return result;
	}
	
	public int convLanguage(String source) {
		if(source.toLowerCase().equals("c++")) {
			return 1;
		} else if(source.toLowerCase().equals("python")) {
			return 2;
		}
		return 0;
	}
	
	public int convConfiguration(String source) {
		if(source.toLowerCase().equals("min")) {
			return 1;
		} else if(source.toLowerCase().equals("max") || source.toLowerCase().equals("full")) {
			return 2;
		}
		return 0;
	}
	
	public String convOSVersion(String source) {
		String result = source;
		if(result.contains("(")) {
			result = result.substring(0, result.indexOf("(") - 1);
		}
		result = result.replace(" ", "");
		return result.toLowerCase();
	}
	
	public String getRepositoryName(String source) {
		String result = "";
		
		int lastSlashIdx = source.lastIndexOf("/");
        int gitIdx = source.lastIndexOf(".git");
        
        if (lastSlashIdx != -1 && gitIdx != -1) {
        	result = source.substring(lastSlashIdx + 1, gitIdx);
        }		
        
        return result;
	}
}
