package jp.go.aist.rtm.rtcbuilder.ros.manager;

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
import jp.go.aist.rtm.rtcbuilder.generator.GeneratedResult;
import jp.go.aist.rtm.rtcbuilder.manager.GenerateManager;
import jp.go.aist.rtm.rtcbuilder.ros.param.ROSParam;
import jp.go.aist.rtm.rtcbuilder.ros.template.TemplateHelperROS;
import jp.go.aist.rtm.rtcbuilder.template.TemplateUtil;

/**
 * CXXファイルの出力を制御するマネージャ
 */
public class CXXGenerateManager extends GenerateManager {

	static final String TEMPLATE_PATH = "jp/go/aist/rtm/rtcbuilder/ros/template";

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
		return IRtcBuilderConstants.MIDDLEWARE_ROS;
	}

	/**
	 * ファイルを出力する
	 *
	 * @param generatorParam
	 *            生成用パラメータ
	 * @return 出力結果のリスト
	 */
	public List<GeneratedResult> generateTemplateCode(ParamBase param) {
		ROSParam rosParam = (ROSParam)param;
		List<GeneratedResult> result = new ArrayList<GeneratedResult>();
		if (!rosParam.isLanguageExist(LANG_CPP)) {
			return result;
		}
		if (rosParam.getNodeName() == null) {
			return result;
		}

		Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("template", TEMPLATE_PATH);
		contextMap.put("rosParam", rosParam);
		contextMap.put("tmpltHelper", new TemplateHelperROS());

		return generateTemplateCode(contextMap);
	}

	public List<GeneratedResult> generateTemplateCode(
			Map<String, Object> contextMap) {
		List<GeneratedResult> result = new ArrayList<GeneratedResult>();

		result.add(generateCMakeLists(contextMap));
		result.add(generateSourceMain(contextMap));
		result.add(generateHeader(contextMap));
		result.add(generateSource(contextMap));

		result.add(generateInterfaceCMakeLists(contextMap));

		return result;
	}
	
	public GeneratedResult generateCMakeLists(Map<String, Object> contextMap) {
		ROSParam rosParam = (ROSParam) contextMap.get("rosParam");
		String outfile = rosParam.getPackageName() + "/CMakeLists.txt";
		String infile = "";
		infile = "cpp/CMakeLists.txt.vsl";
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generateSourceMain(Map<String, Object> contextMap) {
		ROSParam rosParam = (ROSParam) contextMap.get("rosParam");
		String outfile = rosParam.getPackageName() + "/src/main.cpp";
		String infile = "cpp/CXX_Main.cpp.vsl";
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generateHeader(Map<String, Object> contextMap) {
		ROSParam rosParam = (ROSParam) contextMap.get("rosParam");
		String outfile = rosParam.getPackageName() + "/include/" + rosParam.getPackageName() + "/" + rosParam.getNodeName() + ".hpp";
		String infile = "cpp/CXX_ROS.hpp.vsl";
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generateSource(Map<String, Object> contextMap) {
		ROSParam rosParam = (ROSParam) contextMap.get("rosParam");
		String outfile = rosParam.getPackageName() + "/src/" + rosParam.getNodeName() + ".cpp";
		String infile = "cpp/CXX_ROS.cpp.vsl";
		return generate(infile, outfile, contextMap);
	}
	
	public GeneratedResult generateInterfaceCMakeLists(Map<String, Object> contextMap) {
		ROSParam rosParam = (ROSParam) contextMap.get("rosParam");
		String outfile = rosParam.getPackageName() + "_interfaces/CMakeLists.txt";
		String infile = "";
		infile = "cpp/interface_CMakeLists.txt.vsl";
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
