package jp.go.aist.rtm.rtcbuilder.python.manager;

import static jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants.DEFAULT_RTM_VERSION;
import static jp.go.aist.rtm.rtcbuilder.python.IRtcBuilderConstantsPython.LANG_PYTHON;
import static jp.go.aist.rtm.rtcbuilder.python.IRtcBuilderConstantsPython.LANG_PYTHON_ARG;
import static jp.go.aist.rtm.rtcbuilder.util.RTCUtil.form;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.go.aist.rtm.rtcbuilder.fsm.StateParam;
import jp.go.aist.rtm.rtcbuilder.generator.GeneratedResult;
import jp.go.aist.rtm.rtcbuilder.generator.param.RtcParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.IdlFileParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.ServiceClassParam;
import jp.go.aist.rtm.rtcbuilder.manager.GenerateManager;
import jp.go.aist.rtm.rtcbuilder.python.ui.Perspective.PythonProperty;
import jp.go.aist.rtm.rtcbuilder.python.util.RTCUtilPy;
import jp.go.aist.rtm.rtcbuilder.template.TemplateHelper;
import jp.go.aist.rtm.rtcbuilder.template.TemplateUtil;
import jp.go.aist.rtm.rtcbuilder.ui.Perspective.LanguageProperty;
import jp.go.aist.rtm.rtcbuilder.util.RTCUtil;

/**
 * Pythonファイルの出力を制御するマネージャ
 */
public class PythonGenerateManager extends GenerateManager {

	static final String TEMPLATE_PATH = "jp/go/aist/rtm/rtcbuilder/python/template";

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
	public LanguageProperty getLanguageProperty(RtcParam rtcParam) {
		LanguageProperty langProp = null;
		if (rtcParam.isLanguageExist(LANG_PYTHON)) {
			langProp = new PythonProperty();
		}
		return langProp;
	}

	/**
	 * ファイルを出力する
	 *
	 * @param generatorParam
	 * @return 出力結果のリスト
	 */
	public List<GeneratedResult> generateTemplateCode(RtcParam rtcParam) {
		List<GeneratedResult> result = new ArrayList<GeneratedResult>();

		if (!rtcParam.isLanguageExist(LANG_PYTHON)) {
			return result;
		}

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
		for(IdlFileParam target : allIdlFileParams) {
			if(RTCUtil.checkDefault(target.getIdlPath(), rtcParam.getParent().getDataTypeParams())) continue;
			allIdlFileParamsForBuild.add(target);
		}
		for(IdlFileParam target : rtcParam.getIncludedIdlPathes()) {
			if(RTCUtil.checkDefault(target.getIdlPath(), rtcParam.getParent().getDataTypeParams())) continue;
			allIdlFileParamsForBuild.add(target);
		}

		// IDLファイル内に記述されているServiceClassParamを設定する
		for (IdlFileParam idlFileParam : allIdlFileParams) {
			for (ServiceClassParam serviceClassParam : rtcParam.getServiceClassParams()) {
				if (idlFileParam.getIdlPath().equals(serviceClassParam.getIdlPath())){
					if (!idlFileParam.getServiceClassParams().contains(serviceClassParam)){
						idlFileParam.addServiceClassParams(serviceClassParam);
					}
				}
			}
		}
		List<IdlFileParam> allFileParams = new ArrayList<IdlFileParam>();
		allFileParams.addAll(rtcParam.getProviderIdlPathes());
		allFileParams.addAll(rtcParam.getConsumerIdlPathes());
		List<String> moduleList = RTCUtilPy.checkDefaultModuile(allFileParams, rtcParam.getParent().getDataTypeParams());

		Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("template", TEMPLATE_PATH);
		contextMap.put("rtcParam", rtcParam);
		contextMap.put("tmpltHelper", new TemplateHelper());
		contextMap.put("tmpltHelperPy", new TemplateHelperPy());
		contextMap.put("pyConv", new PythonConverter());
		contextMap.put("allIdlFileParam", allIdlFileParams);
		contextMap.put("idlPathes", rtcParam.getIdlPathes());
		contextMap.put("allIdlFileParamBuild", allIdlFileParamsForBuild);
		contextMap.put("defaultModule", moduleList);

		return generateTemplateCode10(contextMap);
	}

	// RTM 1.0系
	@SuppressWarnings("unchecked")
	public List<GeneratedResult> generateTemplateCode10(
			Map<String, Object> contextMap) {
		List<GeneratedResult> result = new ArrayList<GeneratedResult>();
		RtcParam rtcParam = (RtcParam) contextMap.get("rtcParam");
		List<IdlFileParam> allIdlFileParams = (List<IdlFileParam>) contextMap
				.get("allIdlFileParam");
		boolean isStaticFSM = rtcParam.isStaticFSM();
		if(isStaticFSM) {
			StateParam stateParam = rtcParam.getFsmParam();
			stateParam.setEventParam(rtcParam);
			contextMap.put("fsmParam", stateParam);
		}

		result.add(generatePythonSource(contextMap));

		if(isStaticFSM) {
			result.add(generatePythonFSM(contextMap));
		}

		result.add(generateScript1604(contextMap));
		result.add(generateScript1804(contextMap));
		result.add(generateAppVeyorTemplate(contextMap));

		if ( 0<allIdlFileParams.size() ) {
			result.add(generateIDLCompileBat(contextMap));
			result.add(generateIDLCompileSh(contextMap));
			result.add(generateDeleteBat(contextMap));
		}

		for (IdlFileParam idlFileParam : rtcParam.getProviderIdlPathes()) {
			contextMap.put("idlFileParam", idlFileParam);
			result.add(generateSVCIDLExampleSource(contextMap));
		}
		//////////
		result.add(generatePythonTestSource(contextMap));
		for (IdlFileParam idlFileParam : rtcParam.getConsumerIdlPathes()) {
			if(idlFileParam.isDataPort()) continue;
			if(RTCUtil.checkDefault(idlFileParam.getIdlPath(), rtcParam.getParent().getDataTypeParams())) continue;
			contextMap.put("idlFileParam", idlFileParam);
			result.add(generateTestSVCIDLExampleSource(contextMap));
		}

		return result;
	}

	// 1.0系 (Python)

	public GeneratedResult generatePythonSource(Map<String, Object> contextMap) {
		RtcParam rtcParam = (RtcParam) contextMap.get("rtcParam");
		String outfile = rtcParam.getName() + ".py";
		String infile = "python/Py_RTC.py.vsl";
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generateSVCIDLExampleSource(
			Map<String, Object> contextMap) {
		IdlFileParam idlParam = (IdlFileParam) contextMap.get("idlFileParam");
		String outfile = idlParam.getIdlFileNoExt() + "_idl_example.py";
		String infile = "python/Py_SVC_idl_example.py.vsl";
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generatePythonFSM(Map<String, Object> contextMap) {
		RtcParam rtcParam = (RtcParam) contextMap.get("rtcParam");
		String outfile = null;
		outfile = rtcParam.getName()  + "FSM.py";
		String infile = "python/fsm/Py_FSM.py.vsl";
		return generate(infile, outfile, contextMap);
	}

	// 1.0系 (ビルド環境)

	public GeneratedResult generateIDLCompileBat(Map<String, Object> contextMap) {
		String outfile = "idlcompile.bat";
		String infile = "python/idlcompile.bat.vsl";
		GeneratedResult result = generate(infile, outfile, contextMap);
		result.setEncode("Shift_JIS");
		return result;
	}

	public GeneratedResult generateIDLCompileSh(Map<String, Object> contextMap) {
		String outfile = "idlcompile.sh";
		String infile = "python/idlcompile.sh.vsl";
		GeneratedResult result = generate(infile, outfile, contextMap);
		result.setCode(result.getCode().replaceAll("\r\n", "\n"));
		result.setEncode("EUC_JP");
		return result;
	}

	public GeneratedResult generateDeleteBat(Map<String, Object> contextMap) {
		String outfile = "delete.bat";
		String infile = "python/delete.bat.vsl";
		GeneratedResult result = generate(infile, outfile, contextMap);
		result.setEncode("Shift_JIS");
		return result;
	}
	//////////
	public GeneratedResult generatePythonTestSource(Map<String, Object> contextMap) {
		RtcParam rtcParam = (RtcParam) contextMap.get("rtcParam");
		String outfile = "test/" + rtcParam.getName() + "Test.py";
		String infile = "python/test/Py_Test_RTC.py.vsl";
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generateTestSVCIDLExampleSource(
			Map<String, Object> contextMap) {
		IdlFileParam idlParam = (IdlFileParam) contextMap.get("idlFileParam");
		String outfile = "test/" + idlParam.getIdlFileNoExt() + "_idl_example.py";
		String infile = "python/Py_SVC_idl_example.py.vsl";
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generateScript1604(Map<String, Object> contextMap) {
		String outfile = "scripts/ubuntu_1604/Dockerfile";
		String infile = "python/scripts/Dockerfile_ubuntu_1604.vsl";
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generateScript1804(Map<String, Object> contextMap) {
		String outfile = "scripts/ubuntu_1804/Dockerfile";
		String infile = "python/scripts/Dockerfile_ubuntu_1804.vsl";
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generateAppVeyorTemplate(Map<String, Object> contextMap) {
		String outfile = ".appveyor.yml";
		String infile = "python/appveyor.vsl";
		return generate(infile, outfile, contextMap);
	}
	//////////
	public GeneratedResult generate(String infile, String outfile,
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
