package jp.go.aist.rtm.rtcbuilder.ros.python.manager;

import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DEFAULT_RTM_VERSION;
import static jp.go.aist.rtm.rtcbuilder.ros.python.IRtcBuilderConstantsPython.LANG_PYTHON;
import static jp.go.aist.rtm.rtcbuilder.ros.python.IRtcBuilderConstantsPython.LANG_PYTHON_ARG;
import static jp.go.aist.rtm.rtcbuilder.util.RTCUtil.form;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.generator.GeneratedResult;
import jp.go.aist.rtm.rtcbuilder.ros.manager.CommonGenerateManager;
import jp.go.aist.rtm.rtcbuilder.ros.param.ROSParam;
import jp.go.aist.rtm.rtcbuilder.template.TemplateUtil;

public class PythonCommonGenerateManager extends CommonGenerateManager {

	static final String TEMPLATE_PATH_PYTHON = "jp/go/aist/rtm/rtcbuilder/ros/python/template";

	public PythonCommonGenerateManager() {
	}

	@Override
	public String getTargetVersion() {
		return DEFAULT_RTM_VERSION;
	}

	@Override
	public String getManagerKey() {
		return LANG_PYTHON;
	}

	@Override
	public String getLangArgList() {
		return LANG_PYTHON_ARG;
	}

	@Override
	public String getTargetMiddleware() {
		return IRtcBuilderConstants.MIDDLEWARE_ROS;
	}

	@Override
	public List<GeneratedResult> generateTemplateCode(Map<String, Object> contextMap) {
		List<GeneratedResult> result = super.generateTemplateCode(contextMap);

		result.add(generateResource(contextMap));
		result.add(generateInterfaceCMakeLists(contextMap));

		return result;
	}

	@Override
	public GeneratedResult generateREADME(Map<String, Object> contextMap) {
		ROSParam rosParam = (ROSParam) contextMap.get("rosParam");
		String outfile = rosParam.getPackageName() + "/README.md";
		String infile = "common/README.vsl";
		return generatePython(infile, outfile, contextMap);
	}

	@Override
	public GeneratedResult generatePackageXML(Map<String, Object> contextMap) {
		ROSParam rosParam = (ROSParam) contextMap.get("rosParam");
		String outfile = rosParam.getPackageName() + "/package.xml";
		String infile = "common/package.xml.vsl";
		return generatePython(infile, outfile, contextMap);
	}

	public GeneratedResult generateResource(Map<String, Object> contextMap) {
		ROSParam rosParam = (ROSParam) contextMap.get("rosParam");
		String outfile = rosParam.getPackageName() + "/resource/" + rosParam.getPackageName();
		String infile = "common/resource.vsl";
		return generatePython(infile, outfile, contextMap);
	}
	
	@Override
	public GeneratedResult generateInterfacePackageXML(Map<String, Object> contextMap) {
		ROSParam rosParam = (ROSParam) contextMap.get("rosParam");
		String outfile = rosParam.getPackageName() + "_interfaces/package.xml";
		String infile = "common/interface_package.xml.vsl";
		return generatePython(infile, outfile, contextMap);
	}
	
	public GeneratedResult generateInterfaceCMakeLists(Map<String, Object> contextMap) {
		ROSParam rosParam = (ROSParam) contextMap.get("rosParam");
		String outfile = rosParam.getPackageName() + "_interfaces/CMakeLists.txt";
		String infile = "";
		infile = "common/interface_CMakeLists.txt.vsl";
		return generatePython(infile, outfile, contextMap);
	}
	/////
	public GeneratedResult generatePython(String infile, String outfile,
			Map<String, Object> contextMap) {
		try {
			String template = TEMPLATE_PATH_PYTHON + "/" + infile;
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			GeneratedResult gr = null;
			try (InputStream ins = cl.getResourceAsStream(template)) {
				gr = TemplateUtil.createGeneratedResult(ins, contextMap, outfile);
			}
			return gr;
		} catch (Exception e) {
			throw new RuntimeException(form(MSG_ERROR_GENERATE_FILE,
					new String[] { "CMake", outfile }), e);
		}
	}

}
