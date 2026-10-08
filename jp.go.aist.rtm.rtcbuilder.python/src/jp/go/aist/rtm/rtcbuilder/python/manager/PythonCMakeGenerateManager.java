package jp.go.aist.rtm.rtcbuilder.python.manager;

import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DEFAULT_RTM_VERSION;
import static jp.go.aist.rtm.rtcbuilder.python.IRtcBuilderConstantsPython.LANG_PYTHON;
import static jp.go.aist.rtm.rtcbuilder.python.IRtcBuilderConstantsPython.LANG_PYTHON_ARG;
import static jp.go.aist.rtm.rtcbuilder.util.RTCUtil.form;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.generator.GeneratedResult;
import jp.go.aist.rtm.rtcbuilder.generator.param.RtcParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.IdlFileParam;
import jp.go.aist.rtm.rtcbuilder.manager.CMakeGenerateManager;
import jp.go.aist.rtm.rtcbuilder.template.TemplateUtil;
import jp.go.aist.rtm.rtcbuilder.util.RTCUtil;

public class PythonCMakeGenerateManager extends CMakeGenerateManager {

	static final String TEMPLATE_PATH_PYTHON = "jp/go/aist/rtm/rtcbuilder/python/template";

	public PythonCMakeGenerateManager() {
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
		return IRtcBuilderConstants.MIDDLEWARE_RTM;
	}

	@Override
	public Map<String, Object> createContextMap(RtcParam rtcParam) {
		Map<String, Object> map = super.createContextMap(rtcParam);
		map.put("tmpltHelperPy", new TemplateHelperPy());
		map.put("templatePython", TEMPLATE_PATH_PYTHON);
		
		List<IdlFileParam> allIdlFileParams = new ArrayList<IdlFileParam>();
		for(IdlFileParam target : rtcParam.getProviderIdlPathes()) {
			if(RTCUtil.checkDefault(target.getIdlPath(), rtcParam.getParent().getDataTypeParams())) continue;
			allIdlFileParams.add(target);
		}
		for(IdlFileParam target : rtcParam.getConsumerIdlPathes()) {
			if(RTCUtil.checkDefault(target.getIdlPath(), rtcParam.getParent().getDataTypeParams())) continue;
			allIdlFileParams.add(target);
		}
		List<IdlFileParam> allIdlFileParamsForBuild = new ArrayList<IdlFileParam>();
		allIdlFileParamsForBuild.addAll(allIdlFileParams);
		for(IdlFileParam target : rtcParam.getIncludedIdlPathes()) {
			if(RTCUtil.checkDefault(target.getIdlPath(), rtcParam.getParent().getDataTypeParams())) continue;
			allIdlFileParamsForBuild.add(target);
		}
		map.put("allIdlFileParamBuild", allIdlFileParamsForBuild);
		
		return map;
	}

	// RTM 1.0系
	@Override
	public List<GeneratedResult> generateTemplateCode10(
			Map<String, Object> contextMap) {
		List<GeneratedResult> result = super.generateTemplateCode10(contextMap);

		RtcParam rtcParam = (RtcParam) contextMap.get("rtcParam");
		
		boolean isExist = false;
		for(IdlFileParam target : rtcParam.getProviderIdlPathes()) {
			if(RTCUtil.checkDefault(target.getIdlPath(), rtcParam.getParent().getDataTypeParams())) continue;
			isExist = true;
			break;
		}
		if(isExist == false) {
			for(IdlFileParam target : rtcParam.getConsumerIdlPathes()) {
				if(RTCUtil.checkDefault(target.getIdlPath(), rtcParam.getParent().getDataTypeParams())) continue;
				isExist = true;
				break;
			}
		}

		if(isExist) {
			result.add(generatePostinstIin(contextMap));
			result.add(generatePrermIn(contextMap));
			result.add(generateCMakeWixPatchXmlIn(contextMap));
		}

		return result;
	}

	// 1.0系 (CMake)

	@Override
	public GeneratedResult generateCMakeLists(Map<String, Object> contextMap) {
		String outfile = "CMakeLists.txt";
		String infile = "cmake/CMakeLists.txt.vsl";
		GeneratedResult result = generatePython(infile, outfile, contextMap);
		result.setNotBom(true);
		return result;
	}

	@Override
	public GeneratedResult generateIdlCMakeLists(Map<String, Object> contextMap) {
		String outfile = "idl/CMakeLists.txt";
		String infile = "cmake/idl/IdlCMakeLists.txt.vsl";
		GeneratedResult result = generatePython(infile, outfile, contextMap);
		result.setNotBom(true);
		return result;
	}
	
	@Override
	public GeneratedResult generateTestCMakeLists(Map<String, Object> contextMap) {
		String outfile = "test/CMakeLists.txt";
		String infile = "cmake/test/CMakeLists.txt.vsl";
		GeneratedResult result = generatePython(infile, outfile, contextMap);
		result.setNotBom(true);
		return result;
	}
	
	public GeneratedResult generateTestIncludeCMakeLists(Map<String, Object> contextMap) {
		return null;
	}
	
	public GeneratedResult generateTestIncModuleCMakeLists(Map<String, Object> contextMap) {
		return null;
	}
	public GeneratedResult generateTestSrcCMakeLists(Map<String, Object> contextMap) {
		return null;
	}
	
	// 1.0系 (CMake/cpack_resources)
	public GeneratedResult generatePostinstIin(Map<String, Object> contextMap) {
		String outfile = "postinst.in";
		String infile = "cmake/postinst.in.vsl";
		return generatePython(infile, outfile, contextMap);
	}

	public GeneratedResult generatePrermIn(Map<String, Object> contextMap) {
		String outfile = "prerm.in";
		String infile = "cmake/prerm.in.vsl";
		return generatePython(infile, outfile, contextMap);
	}

	public GeneratedResult generateCMakeWixPatchXmlIn(Map<String, Object> contextMap) {
		String outfile = "cmake/wix_patch.xml.in";
		String infile = "cmake/wix_patch.xml.in.vsl";
		return generatePython(infile, outfile, contextMap);
	}

	@Override
	public GeneratedResult generateCmakeCPackOption(Map<String, Object> contextMap) {
		String outfile = "cmake/cpack_options.cmake.in";
		String infile = "cmake/cpack_options_cmake.in.vsl";
		GeneratedResult result = generatePython(infile, outfile, contextMap);
		result.setNotBom(true);
		return result;
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
