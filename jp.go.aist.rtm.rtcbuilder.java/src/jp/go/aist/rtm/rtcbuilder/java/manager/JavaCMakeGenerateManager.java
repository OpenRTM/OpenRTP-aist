package jp.go.aist.rtm.rtcbuilder.java.manager;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.generator.GeneratedResult;
import jp.go.aist.rtm.rtcbuilder.generator.param.RtcParam;
import jp.go.aist.rtm.rtcbuilder.manager.CMakeGenerateManager;
import jp.go.aist.rtm.rtcbuilder.template.TemplateUtil;
import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.*;
import static jp.go.aist.rtm.rtcbuilder.util.RTCUtil.form;
import static jp.go.aist.rtm.rtcbuilder.java.IRtcBuilderConstantsJava.LANG_JAVA;
import static jp.go.aist.rtm.rtcbuilder.java.IRtcBuilderConstantsJava.LANG_JAVA_ARG;

public class JavaCMakeGenerateManager extends CMakeGenerateManager {

	static final String TEMPLATE_PATH_JAVA = "jp/go/aist/rtm/rtcbuilder/java/template";

	public JavaCMakeGenerateManager() {
	}

	@Override
	public String getTargetVersion() {
		return DEFAULT_RTM_VERSION;
	}

	@Override
	public String getManagerKey() {
		return LANG_JAVA;
	}

	@Override
	public String getLangArgList() {
		return LANG_JAVA_ARG;
	}

	@Override
	public String getTargetMiddleware() {
		return IRtcBuilderConstants.MIDDLEWARE_RTM;
	}

	@Override
	public Map<String, Object> createContextMap(RtcParam rtcParam) {
		Map<String, Object> map = super.createContextMap(rtcParam);
		map.put("templateJava", TEMPLATE_PATH_JAVA);
		return map;
	}

	// RTM 1.0系
	@Override
	public List<GeneratedResult> generateTemplateCode10(
			Map<String, Object> contextMap) {
		List<GeneratedResult> result = super.generateTemplateCode10(contextMap);

		GeneratedResult gr;
		gr = generateModulesJavaCompile(contextMap);
		result.add(gr);

		return result;
	}

	// 1.0系 (CMake)

	@Override
	public GeneratedResult generateCMakeLists(Map<String, Object> contextMap) {
		String outfile = "CMakeLists.txt";
		String infile = "cmake/CMakeLists.txt.vsl";
		return generateJava(infile, outfile, contextMap);
	}

	public GeneratedResult generateModulesJavaCompile(
			Map<String, Object> contextMap) {
		String outfile = "cmake_modules/cmake_javacompile.cmake.in";
		String infile = "cmake/cmake_javacompile.cmake.in.vsl";
		return generateJava(infile, outfile, contextMap);
	}

	// 1.0系 (CMake/doc)
	public GeneratedResult generateDocCMakeLists(Map<String, Object> contextMap) {
		return new GeneratedResult();
	}

	@Override
	public GeneratedResult generateDocConfPy(Map<String, Object> contextMap) {
		return new GeneratedResult();
	}

	@Override
	public GeneratedResult generateDoxyfile(Map<String, Object> contextMap) {
		return new GeneratedResult();
	}

	@Override
	public GeneratedResult generateSrcCMakeLists(Map<String, Object> contextMap) {
		return new GeneratedResult();
	}

	@Override
	public GeneratedResult generateIncludeCMakeLists(Map<String, Object> contextMap) {
		return new GeneratedResult();
	}

	@Override
	public GeneratedResult generateIncModuleCMakeLists(Map<String, Object> contextMap) {
		return new GeneratedResult();
	}
	
	@Override
	public GeneratedResult generateTestIncludeCMakeLists(Map<String, Object> contextMap) {
		return new GeneratedResult();
	}
	
	@Override
	public GeneratedResult generateTestIncModuleCMakeLists(Map<String, Object> contextMap) {
		return new GeneratedResult();
	}

	// 1.0系 (CMake/cpack_resources)
	public GeneratedResult generateJava(String infile, String outfile,
			Map<String, Object> contextMap) {
		try {
			String template = TEMPLATE_PATH_JAVA + "/" + infile;
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			GeneratedResult gr = null;
			try(InputStream ins = cl.getResourceAsStream(template) ) {
				gr = TemplateUtil.createGeneratedResult(ins, contextMap, outfile);
			}
			return gr;
		} catch (Exception e) {
			throw new RuntimeException(form(MSG_ERROR_GENERATE_FILE,
					new String[] { "CMake", outfile }), e);
		}
	}

}
