package jp.go.aist.rtm.rtcbuilder.manager;

import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.LANG_CPP;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.LANG_CPP_ARG;
import static jp.go.aist.rtm.rtcbuilder.util.RTCUtil.form;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.go.aist.rtm.rtcbuilder.IRTCBMessageConstants;
import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.ParamBase;
import jp.go.aist.rtm.rtcbuilder.fsm.StateParam;
import jp.go.aist.rtm.rtcbuilder.generator.GeneratedResult;
import jp.go.aist.rtm.rtcbuilder.generator.param.RtcParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.IdlFileParam;
import jp.go.aist.rtm.rtcbuilder.template.TemplateHelper;
import jp.go.aist.rtm.rtcbuilder.template.TemplateUtil;

/**
 * CXXファイルの出力を制御するマネージャ
 */
public class CXXGenerateManager extends GenerateManager {

	static final String TEMPLATE_PATH = "jp/go/aist/rtm/rtcbuilder/template";

	static final String MSG_ERROR_GENERATE_FILE = IRTCBMessageConstants.ERROR_CODE_GENERATION;

	@Override
	public String getManagerKey() {
		return LANG_CPP;
	}

	@Override
	public String getLangArgList() {
		return LANG_CPP_ARG;
	}

	@Override
	public String getTargetMiddleware() {
		return IRtcBuilderConstants.MIDDLEWARE_RTM;
	}

	/**
	 * ファイルを出力する
	 *
	 * @param generatorParam
	 *            生成用パラメータ
	 * @return 出力結果のリスト
	 */
	public List<GeneratedResult> generateTemplateCode(ParamBase baseParam) {
		RtcParam rtcParam = (RtcParam)baseParam;
		List<GeneratedResult> result = new ArrayList<GeneratedResult>();
		if (!rtcParam.isLanguageExist(LANG_CPP)) {
			return result;
		}
		if (rtcParam.getName() == null) {
			return result;
		}

		Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("template", TEMPLATE_PATH);
		contextMap.put("rtcParam", rtcParam);
		contextMap.put("cXXConv", new CXXConverter());
		contextMap.put("tmpltHelper", new TemplateHelper());

		resetIDLServiceClass(rtcParam);

		return generateTemplateCode10(contextMap);
	}

	// RTM 1.0系
	public List<GeneratedResult> generateTemplateCode10(
			Map<String, Object> contextMap) {
		List<GeneratedResult> result = new ArrayList<GeneratedResult>();
		RtcParam rtcParam = (RtcParam) contextMap.get("rtcParam");

		boolean isStaticFSM = rtcParam.isStaticFSM();
		if(isStaticFSM) {
			StateParam stateParam = rtcParam.getFsmParam();
			stateParam.setEventParam(rtcParam);
			contextMap.put("fsmParam", stateParam);
		}
		
		result.add(generateCompSource(contextMap));
		result.add(generateRTCHeader(contextMap));
		result.add(generateRTCSource(contextMap));
		
		result.add(generateScript1604(contextMap));
		result.add(generateScript1804(contextMap));
		result.add(generateAppVeyorTemplate(contextMap));
		
		if(isStaticFSM) {
			result.add(generateFSMHeader(contextMap));
			result.add(generateFSMSource(contextMap));
		}

		for (IdlFileParam idl : rtcParam.getProviderIdlPathes()) {
			contextMap.put("idlFileParam", idl);
			result.add(generateSVCHeader(contextMap));
			result.add(generateSVCSource(contextMap));
		}
		//
		if(rtcParam.isChoreonoid()==false) {
			result.add(generateTestCompSource(contextMap));
			result.add(generateTestHeader(contextMap));
			result.add(generateTestSource(contextMap));
			for (IdlFileParam idl : rtcParam.getConsumerIdlPathes()) {
				if(idl.isDataPort()) continue;
				contextMap.put("idlFileParam", idl);
				result.add(generateTestSVCHeader(contextMap));
				result.add(generateTestSVCSource(contextMap));
			}
		}

		return result;
	}

	// 1.0系 (C++)

	public GeneratedResult generateCompSource(Map<String, Object> contextMap) {
		RtcParam rtcParam = (RtcParam) contextMap.get("rtcParam");
		String outfile = null;
		outfile = "src/" + rtcParam.getName() + "Comp.cpp";
		String infile = "cpp/CXX_Comp.cpp.vsl";
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generateRTCHeader(Map<String, Object> contextMap) {
		RtcParam rtcParam = (RtcParam) contextMap.get("rtcParam");
		String outfile = null;
		outfile = "include/" + rtcParam.getName() + "/" + rtcParam.getName() + ".h";
		String infile = "";
		if(rtcParam.isChoreonoid()) {
			infile = "choreonoid/CXX_RTC.h.vsl";
		} else {
			infile = "cpp/CXX_RTC.h.vsl";
		}
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generateRTCSource(Map<String, Object> contextMap) {
		RtcParam rtcParam = (RtcParam) contextMap.get("rtcParam");
		String outfile = null;
		outfile = "src/" + rtcParam.getName() + ".cpp";
		String infile = "";
		if(rtcParam.isChoreonoid()) {
			infile = "choreonoid/CXX_RTC.cpp.vsl";
		} else {
			infile = "cpp/CXX_RTC.cpp.vsl";
		}
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generateSVCHeader(Map<String, Object> contextMap) {
		RtcParam rtcParam = (RtcParam) contextMap.get("rtcParam");
		IdlFileParam idlParam = (IdlFileParam) contextMap.get("idlFileParam");
		String outfile = null;
		outfile = "include/" + rtcParam.getName() + "/"
				+ TemplateHelper.getBasename(idlParam.getIdlFileNoExt())
				+ TemplateHelper.getServiceImplSuffix() + ".h";
		String infile = "cpp/CXX_SVC.h.vsl";
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generateSVCSource(Map<String, Object> contextMap) {
		IdlFileParam idlParam = (IdlFileParam) contextMap.get("idlFileParam");
		String outfile = null;
		outfile = "src/" + TemplateHelper.getBasename(idlParam.getIdlFileNoExt())
					+ TemplateHelper.getServiceImplSuffix() + ".cpp";
		String infile = "cpp/CXX_SVC.cpp.vsl";
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generateScript1604(Map<String, Object> contextMap) {
		String outfile = "scripts/ubuntu_1604/Dockerfile";
		String infile = "cpp/scripts/Dockerfile_ubuntu_1604.vsl";
		return generate(infile, outfile, contextMap);
	}
	
	public GeneratedResult generateScript1804(Map<String, Object> contextMap) {
		String outfile = "scripts/ubuntu_1804/Dockerfile";
		String infile = "cpp/scripts/Dockerfile_ubuntu_1804.vsl";
		return generate(infile, outfile, contextMap);
	}
	
	public GeneratedResult generateFSMHeader(Map<String, Object> contextMap) {
		RtcParam rtcParam = (RtcParam) contextMap.get("rtcParam");
		String outfile = null;
		outfile = "include/" + rtcParam.getName() + "/" + rtcParam.getName() + "FSM.h";
		String infile = "fsm/CXX_FSM.h.vsl";
		return generate(infile, outfile, contextMap);
	}
	
	public GeneratedResult generateFSMSource(Map<String, Object> contextMap) {
		RtcParam rtcParam = (RtcParam) contextMap.get("rtcParam");
		String outfile = null;
		outfile = "src/" + rtcParam.getName() + "FSM.cpp";
		String infile = "fsm/CXX_FSM.cpp.vsl";
		return generate(infile, outfile, contextMap);
	}
	
	public GeneratedResult generateFSMTestHeader(Map<String, Object> contextMap) {
		RtcParam rtcParam = (RtcParam) contextMap.get("rtcParam");
		String outfile = null;
		outfile = "test/include/" + rtcParam.getName() + "Test/" + rtcParam.getName() + "FSMTest.h";
		String infile = "fsm/CXX_FSM_Test_RTC.h.vsl";
		return generate(infile, outfile, contextMap);
	}
	
	public GeneratedResult generateFSMTestSource(Map<String, Object> contextMap) {
		RtcParam rtcParam = (RtcParam) contextMap.get("rtcParam");
		String outfile = null;
		outfile = "test/src/" + rtcParam.getName() + "FSMTest.cpp";
		String infile = "fsm/CXX_FSM_Test_RTC.cpp.vsl";
		return generate(infile, outfile, contextMap);
	}
	/////
	public GeneratedResult generateTestCompSource(Map<String, Object> contextMap) {
		RtcParam rtcParam = (RtcParam) contextMap.get("rtcParam");
		String outfile = null;
		outfile = "test/src/" + rtcParam.getName() + "TestComp.cpp";
		String infile = "cpp/test/CXX_Test_Comp.cpp.vsl";
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generateTestHeader(Map<String, Object> contextMap) {
		RtcParam rtcParam = (RtcParam) contextMap.get("rtcParam");
		String outfile = null;
		outfile = "test/include/" + rtcParam.getName() + "Test/" + rtcParam.getName() + "Test.h";
		String infile = "cpp/test/CXX_Test_RTC.h.vsl";
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generateTestSource(Map<String, Object> contextMap) {
		RtcParam rtcParam = (RtcParam) contextMap.get("rtcParam");
		String outfile = null;
		outfile = "test/src/" + rtcParam.getName() + "Test.cpp";
		String infile = "cpp/test/CXX_Test_RTC.cpp.vsl";
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generateTestSVCHeader(Map<String, Object> contextMap) {
		RtcParam rtcParam = (RtcParam) contextMap.get("rtcParam");
		IdlFileParam idlParam = (IdlFileParam) contextMap.get("idlFileParam");
		String outfile = null;
		outfile = "test/include/" + rtcParam.getName() + "Test/"
				+ TemplateHelper.getBasename(idlParam.getIdlFileNoExt())
				+ TemplateHelper.getServiceImplSuffix() + ".h";
		String infile = "cpp/test/CXX_Test_SVC.h.vsl";
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generateTestSVCSource(Map<String, Object> contextMap) {
		IdlFileParam idlParam = (IdlFileParam) contextMap.get("idlFileParam");
		String outfile = null;
		outfile = "test/src/" + TemplateHelper.getBasename(idlParam.getIdlFileNoExt())
					+ TemplateHelper.getServiceImplSuffix() + ".cpp";
		String infile = "cpp/test/CXX_Test_SVC.cpp.vsl";
		return generate(infile, outfile, contextMap);
	}
	
	public GeneratedResult generateAppVeyorTemplate(Map<String, Object> contextMap) {
		String outfile = ".appveyor.yml";
		String infile = "cpp/appveyor.vsl";
		return generate(infile, outfile, contextMap);
	}
	/////
	public GeneratedResult generate(String infile, String outfile,
			Map<String, Object> contextMap) {
		try {
			String template = TEMPLATE_PATH + "/" + infile;
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			GeneratedResult gr = null;
			try( InputStream ins = cl.getResourceAsStream(template) ) {
				gr = TemplateUtil.createGeneratedResult(ins, contextMap, outfile);
			}
			return gr;
		} catch (Exception e) {
			throw new RuntimeException(form(MSG_ERROR_GENERATE_FILE,
					new String[] { "C++", outfile }), e);
		}
	}

}
