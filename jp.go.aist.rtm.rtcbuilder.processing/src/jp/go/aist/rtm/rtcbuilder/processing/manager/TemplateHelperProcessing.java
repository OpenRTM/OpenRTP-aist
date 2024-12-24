package jp.go.aist.rtm.rtcbuilder.processing.manager;

import java.util.ArrayList;
import java.util.List;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.fsm.EventParam;
import jp.go.aist.rtm.rtcbuilder.fsm.StateParam;
import jp.go.aist.rtm.rtcbuilder.fsm.TransitionParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.RtcParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.IdlFileParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.ServiceClassParam;
import jp.go.aist.rtm.rtcbuilder.processing.IRtcBuilderConstantsProcessing;
import jp.go.aist.rtm.rtcbuilder.util.RTCUtil;
import jp.go.aist.rtm.rtcbuilder.util.StringUtil;

/**
 * テンプレートを出力する際に使用されるヘルパー
 */
public class TemplateHelperProcessing {

	//
	public String convertDescDocJava(String source) {
		return StringUtil.splitString(source, IRtcBuilderConstants.DOC_DEFAULT_WIDTH, 
				IRtcBuilderConstantsProcessing.DOC_DESC_PREFIX_PROCESSING, IRtcBuilderConstantsProcessing.DOC_DESC_OFFSET_PROCESSING);
	}
	public String convertPreDocJava(String source) {
		return StringUtil.splitString(source, IRtcBuilderConstants.DOC_DEFAULT_WIDTH, 
				IRtcBuilderConstantsProcessing.DOC_DESC_PREFIX_PROCESSING, IRtcBuilderConstantsProcessing.DOC_PRE_OFFSET_PROCESSING);
	}
	public String convertPostDocJava(String source) {
		return StringUtil.splitString(source, IRtcBuilderConstants.DOC_DEFAULT_WIDTH, 
				IRtcBuilderConstantsProcessing.DOC_DESC_PREFIX_PROCESSING, IRtcBuilderConstantsProcessing.DOC_POST_OFFSET_PROCESSING);
	}
	public String convertUnitDocJava(String source) {
		return StringUtil.splitString(source, IRtcBuilderConstants.DOC_DEFAULT_WIDTH, 
				IRtcBuilderConstantsProcessing.DOC_UNIT_PREFIX_PROCESSING, IRtcBuilderConstantsProcessing.DOC_UNIT_OFFSET_PROCESSING);
	}
	public String convertRangeDocJava(String source) {
		return StringUtil.splitString(source, IRtcBuilderConstants.DOC_DEFAULT_WIDTH, 
				IRtcBuilderConstantsProcessing.DOC_RANGE_PREFIX_PROCESSING, IRtcBuilderConstantsProcessing.DOC_RANGE_OFFSET_PROCESSING);
	}
	public String convertConstraintDocJava(String source) {
		return StringUtil.splitString(source, IRtcBuilderConstants.DOC_DEFAULT_WIDTH, 
				IRtcBuilderConstantsProcessing.DOC_CONSTRAINT_PREFIX_PROCESSING, IRtcBuilderConstantsProcessing.DOC_CONSTRAINT_OFFSET_PROCESSING);
	}
	public String convertTypeDocJava(String source) {
		return StringUtil.splitString(source, IRtcBuilderConstants.DOC_DEFAULT_WIDTH, 
				IRtcBuilderConstantsProcessing.DOC_UNIT_PREFIX_PROCESSING, IRtcBuilderConstantsProcessing.DOC_UNIT_OFFSET_PROCESSING);
	}
	public String convertNumberDocJava(String source) {
		return StringUtil.splitString(source, IRtcBuilderConstants.DOC_DEFAULT_WIDTH, 
				IRtcBuilderConstantsProcessing.DOC_NUMBER_PREFIX_PROCESSING, IRtcBuilderConstantsProcessing.DOC_NUMBER_OFFSET_PROCESSING);
	}
	public String convertSemanticsDocJava(String source) {
		return StringUtil.splitString(source, IRtcBuilderConstants.DOC_DEFAULT_WIDTH, 
				IRtcBuilderConstantsProcessing.DOC_SEMANTICS_PREFIX_PROCESSING, IRtcBuilderConstantsProcessing.DOC_SEMANTICS_OFFSET_PROCESSING);
	}
	public String convertFrequencyDocJava(String source) {
		return StringUtil.splitString(source, IRtcBuilderConstants.DOC_DEFAULT_WIDTH, 
				IRtcBuilderConstantsProcessing.DOC_SEMANTICS_PREFIX_PROCESSING, IRtcBuilderConstantsProcessing.DOC_SEMANTICS_OFFSET_PROCESSING);
	}
	public String convertCycleDocJava(String source) {
		return StringUtil.splitString(source, IRtcBuilderConstants.DOC_DEFAULT_WIDTH, 
				IRtcBuilderConstantsProcessing.DOC_CYCLE_PREFIX_PROCESSING, IRtcBuilderConstantsProcessing.DOC_CYCLE_OFFSET_PROCESSING);
	}
	public String convertInterfaceDocJava(String source) {
		return StringUtil.splitString(source, IRtcBuilderConstants.DOC_DEFAULT_WIDTH, 
				IRtcBuilderConstantsProcessing.DOC_INTERFACE_PREFIX_PROCESSING, IRtcBuilderConstantsProcessing.DOC_INTERFACE_OFFSET_PROCESSING);
	}
	public String convertInterfaceDetailDocJava(String source) {
		return StringUtil.splitString(source, IRtcBuilderConstants.DOC_DEFAULT_WIDTH, 
				IRtcBuilderConstantsProcessing.DOC_INTERFACE_DETAIL_PREFIX_PROCESSING, IRtcBuilderConstantsProcessing.DOC_INTERFACE_DETAIL_OFFSET_PROCESSING);
	}
	//
	public String getServiceName(RtcParam param) {
		List<ServiceClassParam> services = param.getServiceClassParams(); 
		if(services != null && 0 < services.size()) {
			ServiceClassParam target = services.get(0);
			if(target.getModule().contains("::")) {
				return target.getModule().replace("::", "");
			}
		}
		return getUserDefinedModule(param);
	}
	
	public String getServiceNamePath(RtcParam param) {
		String result = getServiceName(param);
		if(0 < result.length()) {
			return "/" + result;
		}
		return "";
	}
	
	public String getUserDefinedModule(RtcParam param) {
		IdlFileParam targetIDL = null;
		for(IdlFileParam target : param.getProviderIdlPathes()) {
			if(RTCUtil.checkDefault(target.getIdlPath(), param.getParent().getDataTypeParams())) continue;
			targetIDL = target;
			break;
		}
		if(targetIDL == null) {
			for(IdlFileParam target : param.getConsumerIdlPathes()) {
				if(RTCUtil.checkDefault(target.getIdlPath(), param.getParent().getDataTypeParams())) continue;
				targetIDL = target;
				break;
			}
		}
		if(targetIDL == null || targetIDL.getTargetType().size() == 0) {
			return "";
		}
		
		String targetType = targetIDL.getTargetType().get(0);
		String[] elems = targetType.split("::");
		return elems[0];
	}

	public boolean notNullRTMRoot() {
		String defaultPath = System.getenv("RTM_ROOT");
		if( defaultPath==null ) return false;
		return true;
	}
	
	public String getHistoryImport(StateParam param) {
		if(param.getHistory()==2) {
			return "import jp.go.aist.rtm.RTC.jfsm.DeepHistory;";
		} else if(param.getHistory()==1) {
			return "import jp.go.aist.rtm.RTC.jfsm.History;";
		}
		return "  ";
	}
	
	public String getHistory(StateParam param) {
		if(param.getHistory()==2) {
			return "@DeepHistory";
		} else if(param.getHistory()==1) {
			return "@History";
		}
		return "  ";
	}
	
	public List<String> getInEventList(StateParam parent, StateParam param) {
		List<String> result = new ArrayList<String>();
		
		for(TransitionParam trans : parent.getAllTransList()) {
			if(trans.getTarget().equals(param.getName())) {
				if(trans.getEvent()!=null && 0<trans.getEvent().length()) {
					result.add(trans.getEvent());
				}
			}
		}
		
		return result;
	}
	
	public List<String> getEventDataTypes(StateParam param) {
		ProcessingConverter converter = new ProcessingConverter();
		List<String> result = new ArrayList<String>();
		
		for(TransitionParam trans : param.getAllTransList()) {
			if(trans.existDataType()==false) continue;
			String dataType = converter.getDataportPackageName(trans.getDataType());
			if(result.contains(dataType)) continue;
			result.add(dataType);
		}
		
		return result;
	}
	
	public List<String> getEventDataTypes4Test(StateParam param) {
		List<String> result = getEventDataTypes(param);
		if(result.contains("import RTC.TimedLong;")==false) {
			result.add("import RTC.TimedLong;");
		}
		return result;
	}
	
	public String getInitialValue(TransitionParam trans) {
		String result = "0";
		
		EventParam param = trans.getEventParam();
		if(param!=null) {
			if(param.getDataType().contains("String")) {
				result = "\"0\"";
			}
		}
		
		return result;
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
			builder.append("    setState(new State(").append(targetTans.getTarget()).append(".class));");
			return builder.toString();
		}
		return "";
	}
}
