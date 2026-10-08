package jp.go.aist.rtm.rtcbuilder.ros.python.manager;

import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DEFAULT_RTM_VERSION;
import static jp.go.aist.rtm.rtcbuilder.ros.python.IRtcBuilderConstantsPython.LANG_PYTHON;
import static jp.go.aist.rtm.rtcbuilder.ros.python.IRtcBuilderConstantsPython.LANG_PYTHON_ARG;
import static jp.go.aist.rtm.rtcbuilder.util.RTCUtil.form;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.ParamBase;
import jp.go.aist.rtm.rtcbuilder.generator.GeneratedResult;
import jp.go.aist.rtm.rtcbuilder.manager.GenerateManager;
import jp.go.aist.rtm.rtcbuilder.ros.param.ROSParam;
import jp.go.aist.rtm.rtcbuilder.ros.python.template.TemplateHelperPy;
import jp.go.aist.rtm.rtcbuilder.ros.python.ui.Perspective.PythonProperty;
import jp.go.aist.rtm.rtcbuilder.template.TemplateUtil;
import jp.go.aist.rtm.rtcbuilder.ui.Perspective.LanguageProperty;

public class PythonGenerateManager extends GenerateManager {

	static final String TEMPLATE_PATH = "jp/go/aist/rtm/rtcbuilder/ros/python/template";

	static final String MSG_ERROR_GENERATE_FILE = "Python generation error. [{0}]";

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
	public LanguageProperty getLanguageProperty(ParamBase rtcParam) {
		LanguageProperty langProp = null;
		if (rtcParam.isLanguageExist(LANG_PYTHON)) {
			langProp = new PythonProperty();
		}
		return langProp;
	}

	/**
	 * 繝輔ぃ繧､繝ｫ繧貞�ｺ蜉帙☆繧�
	 *
	 * @param generatorParam
	 * @return 蜃ｺ蜉帷ｵ先棡縺ｮ繝ｪ繧ｹ繝�
	 */
	public List<GeneratedResult> generateTemplateCode(ParamBase baseParam) {
		ROSParam rosParam = (ROSParam)baseParam;
		List<GeneratedResult> result = new ArrayList<GeneratedResult>();

		if (!rosParam.isLanguageExist(LANG_PYTHON)) {
			return result;
		}

		Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("template", TEMPLATE_PATH);
		contextMap.put("rosParam", rosParam);
		contextMap.put("helperPy", new TemplateHelperPy());

		return generateTemplateCode(contextMap);
	}

	@SuppressWarnings("unchecked")
	public List<GeneratedResult> generateTemplateCode(Map<String, Object> contextMap) {
		List<GeneratedResult> result = new ArrayList<GeneratedResult>();

		result.add(generateSetupCfg(contextMap));
		result.add(generateSetupPy(contextMap));

		result.add(generateInitPy(contextMap));
		result.add(generateSource(contextMap));
		
		return result;
	}

	public GeneratedResult generateSetupCfg(Map<String, Object> contextMap) {
		ROSParam rosParam = (ROSParam) contextMap.get("rosParam");
		String outfile = rosParam.getPackageName() + "/setup.cfg";
		String infile = "python/setup.cfg.vsl";
		GeneratedResult result = generatePython(infile, outfile, contextMap);
		result.setNotBom(true);
		return result;
	}

	public GeneratedResult generateSetupPy(Map<String, Object> contextMap) {
		ROSParam rosParam = (ROSParam) contextMap.get("rosParam");
		String outfile = rosParam.getPackageName() + "/setup.py";
		String infile = "python/setup.py.vsl";
		return generatePython(infile, outfile, contextMap);
	}

	public GeneratedResult generateInitPy(Map<String, Object> contextMap) {
		ROSParam rosParam = (ROSParam) contextMap.get("rosParam");
		String outfile = rosParam.getPackageName() + "/" + rosParam.getPackageName() + "/__init__.py";
		String infile = "python/init.py.vsl";
		return generatePython(infile, outfile, contextMap);
	}

	public GeneratedResult generateSource(Map<String, Object> contextMap) {
		ROSParam rosParam = (ROSParam) contextMap.get("rosParam");
		String outfile = rosParam.getPackageName() + "/" + rosParam.getPackageName() + "/" + rosParam.getNodeName() + ".py";
		String infile = "python/Py_ROS.py.vsl";
		return generatePython(infile, outfile, contextMap);
	}
	//////////
	public GeneratedResult generatePython(String infile, String outfile,
			Map<String, Object> contextMap) {
		try {
			String template = TEMPLATE_PATH + "/" + infile;
			GeneratedResult gr = null;
			try (InputStream ins = getClass().getClassLoader().getResourceAsStream(template) ) {
				gr = TemplateUtil.createGeneratedResult(ins, contextMap, outfile);
			}
			return gr;
		} catch (Exception e) {
			throw new RuntimeException(form(MSG_ERROR_GENERATE_FILE,
					new String[] { outfile }), e);
		}
	}

}
