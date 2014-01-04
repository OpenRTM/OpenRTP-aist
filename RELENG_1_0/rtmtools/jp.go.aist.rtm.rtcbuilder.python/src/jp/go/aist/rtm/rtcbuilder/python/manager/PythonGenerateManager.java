package jp.go.aist.rtm.rtcbuilder.python.manager;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.generator.GeneratedResult;
import jp.go.aist.rtm.rtcbuilder.generator.param.RtcParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.IdlFileParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.ServiceClassParam;
import jp.go.aist.rtm.rtcbuilder.manager.GenerateManager;
import jp.go.aist.rtm.rtcbuilder.python.IRtcBuilderConstantsPython;
import jp.go.aist.rtm.rtcbuilder.python.template.PythonConverter;
import jp.go.aist.rtm.rtcbuilder.python.template.TemplateHelperPy;
import jp.go.aist.rtm.rtcbuilder.python.ui.Perspective.PythonProperty;
import jp.go.aist.rtm.rtcbuilder.template.TemplateHelper;
import jp.go.aist.rtm.rtcbuilder.template.TemplateUtil;
import jp.go.aist.rtm.rtcbuilder.ui.Perspective.LanguageProperty;

/**
 * Python�t�@�C���̏o�͂𐧌䂷��}�l�[�W��
 */
public class PythonGenerateManager extends GenerateManager {

	@Override
	public String getTargetVersion() {
		return IRtcBuilderConstants.RTM_VERSION_100;
	}

	@Override
	public String getManagerKey() {
		return IRtcBuilderConstantsPython.LANG_PYTHON;
	}

	@Override
	public String getLangArgList() {
		return IRtcBuilderConstantsPython.LANG_PYTHON_ARG;
	}

	@Override
	public LanguageProperty getLanguageProperty(RtcParam rtcParam) {
		LanguageProperty langProp = null;
		if(rtcParam.isLanguageExist(IRtcBuilderConstantsPython.LANG_PYTHON) ) {
			langProp = new PythonProperty();
		}
		return langProp;
	}
	
	/**
	 * �t�@�C�����o�͂���
	 * 
	 * @param generatorParam
	 * @return �o�͌��ʂ̃��X�g
	 */
	public List<GeneratedResult> generateTemplateCode(RtcParam rtcParam) {
		InputStream ins = null;

		List<GeneratedResult> result = new ArrayList<GeneratedResult>();
		List<IdlFileParam> allIdlFileParams = new ArrayList<IdlFileParam>();
		List<IdlFileParam> providerIdlFileParams = new ArrayList<IdlFileParam>();

		if (rtcParam.isLanguageExist(IRtcBuilderConstantsPython.LANG_PYTHON)) {
			providerIdlFileParams = new ArrayList<IdlFileParam>(rtcParam.getProviderIdlPathes());
			allIdlFileParams = new ArrayList<IdlFileParam>(rtcParam.getProviderIdlPathes());
			allIdlFileParams.addAll(rtcParam.getConsumerIdlPathes());

			//IDL�t�@�C�����ɋL�q����Ă���ServiceClassParam��ݒ肷��
			for( IdlFileParam idlFileParam : allIdlFileParams ) {
				for (ServiceClassParam serviceClassParam : rtcParam.getServiceClassParams()) {
					if( idlFileParam.getIdlPath().equals(serviceClassParam.getIdlPath()) )
						idlFileParam.addServiceClassParams(serviceClassParam);
				}
			}
			
			Map<String, Object> contextMap = new HashMap<String, Object>();
			contextMap.put("rtcParam", rtcParam);
			contextMap.put("tmpltHelper", new TemplateHelper());
			contextMap.put("tmpltHelperPy", new TemplateHelperPy());
			contextMap.put("pyConv", new PythonConverter());
			contextMap.put("allIdlFileParam", allIdlFileParams);

			result = generatePythonSource(contextMap, result);
			result = generateCommonExtend(contextMap, result);
			if( rtcParam.getRtmVersion().equals(IRtcBuilderConstants.RTM_VERSION_100) &&
					allIdlFileParams.size()>0 ) {
				result = generateCompileExtend(contextMap, result);
			}

			contextMap = new HashMap<String, Object>();
			if( !rtcParam.getRtmVersion().equals(IRtcBuilderConstants.RTM_VERSION_100) ) {
				for (IdlFileParam idlFileParam : allIdlFileParams) {
					contextMap.put("rtcParam", rtcParam);
					contextMap.put("pyConv", new PythonConverter());
					contextMap.put("tmpltHelper", new TemplateHelper());
					contextMap.put("tmpltHelperPy", new TemplateHelperPy());
					contextMap.put("idlFileParam", idlFileParam);
					result = generateSVCIDLSource(contextMap, result);
				}
				if( allIdlFileParams.size() > 0 ) {
					contextMap = new HashMap<String, Object>();
					contextMap.put("idlFileParams", allIdlFileParams);
					contextMap.put("tmpltHelper", new TemplateHelper());
					contextMap.put("tmpltHelperPy", new TemplateHelperPy());
	
					result = generateGlobalInitSource(contextMap, result, rtcParam.getOutputProject());
					result = generateGlobalPOAInitSource(contextMap, result, rtcParam.getOutputProject());
				}
			}
			for (IdlFileParam idlFileParam : providerIdlFileParams) {
				contextMap = new HashMap<String, Object>();
				contextMap.put("rtcParam", rtcParam);
				contextMap.put("pyConv", new PythonConverter());
				contextMap.put("idlFileParam", idlFileParam);
				contextMap.put("tmpltHelper", new TemplateHelper());
				contextMap.put("tmpltHelperPy", new TemplateHelperPy());
				result = generateSVCIDLExampleSource(contextMap, result);
			}
	
			try {
				if( ins != null) ins.close();
			} catch (Exception e) {
				throw new RuntimeException(e); // system error
			}
		}

		return result;
	}
	
	/**
	 * Python�R�[�h�𐶐�����
	 * 
	 * @param contextMap	�����p�p�����[�^
	 * @param result	�������ʊi�[��
	 * @return �o�͌��ʂ̃��X�g
	 */
	protected List<GeneratedResult> generatePythonSource(Map<String, Object> contextMap, List<GeneratedResult> result) {
		InputStream ins = null;
		String tmpltPath = null;
		
		RtcParam param = (RtcParam)contextMap.get("rtcParam");
		if( param.getRtmVersion().equals(IRtcBuilderConstants.RTM_VERSION_100) ) {
			tmpltPath = "jp/go/aist/rtm/rtcbuilder/python/template/_100/Py_src.template";
		} else {
			tmpltPath = "jp/go/aist/rtm/rtcbuilder/python/template//Py_src.template";
		}

		ins = PythonGenerateManager.class.getClassLoader().getResourceAsStream(tmpltPath);
		result.add(TemplateUtil.createGeneratedResult(ins, contextMap, param.getName() + ".py"));

		try {
			if( ins != null) ins.close();
		} catch (Exception e) {
			throw new RuntimeException(e); // system error
		}

		return result;
	}
	
	/**
	 * IDL�ɑΉ�����Service�t�@�C���𐶐�����
	 * 
	 * @param contextMap	�����p�����[�^
	 * @param result	�������ʊi�[��
	 * @return �o�͌��ʂ̃��X�g
	 */
	protected List<GeneratedResult> generateSVCIDLSource(Map<String, Object> contextMap, List<GeneratedResult> result) {
		InputStream ins = null;

		ins = PythonGenerateManager.class.getClassLoader()	
				.getResourceAsStream("jp/go/aist/rtm/rtcbuilder/python/template/Py_SVC_IDL_src.template");
		result.add(TemplateUtil.createGeneratedResult(ins, contextMap, 
				TemplateHelper.getFilenameNoExt(((IdlFileParam)contextMap.get("idlFileParam")).getIdlPath()) + "_idl.py"));

		try {
			if( ins != null) ins.close();
		} catch (Exception e) {
			throw new RuntimeException(e); // system error
		}

		return result;
	}
	
	/**
	 * IDL�ɑΉ�����ServiceSample�t�@�C���𐶐�����
	 * 
	 * @param contextMap	�������p�����[�^
	 * @param result	�������ʊi�[��
	 * @return �o�͌��ʂ̃��X�g
	 */
	protected List<GeneratedResult> generateSVCIDLExampleSource(Map<String, Object> contextMap, List<GeneratedResult> result) {
		InputStream ins = null;

		RtcParam param = (RtcParam)contextMap.get("rtcParam");
		if( param.getRtmVersion().equals(IRtcBuilderConstants.RTM_VERSION_100) ) {
			ins = PythonGenerateManager.class.getClassLoader()	
						.getResourceAsStream("jp/go/aist/rtm/rtcbuilder/python/template/_100/Py_SVC_IDL_Example_src.template");
		} else {
			ins = PythonGenerateManager.class.getClassLoader()	
						.getResourceAsStream("jp/go/aist/rtm/rtcbuilder/python/template/Py_SVC_IDL_Example_src.template");
		}
		result.add(TemplateUtil.createGeneratedResult(ins, contextMap, 
				((IdlFileParam)contextMap.get("idlFileParam")).getIdlFileNoExt() + "_idl_example.py"));
		try {
			if( ins != null) ins.close();
		} catch (Exception e) {
			throw new RuntimeException(e); // system error
		}

		return result;
	}
	
	/**
	 * Global IDL�pinit�t�@�C���𐶐�����
	 * 
	 * @param idlFileParm	�����Ώۂ�IDL�t�@�C���p�����[�^
	 * @param contextMap	���������
	 * @param result	�������ʊi�[��
	 * @return �o�͌��ʂ̃��X�g
	 */
	protected List<GeneratedResult> generateGlobalInitSource(Map<String, Object> contextMap, List<GeneratedResult> result, String outDir) {
		InputStream ins = null;

		ins = PythonGenerateManager.class.getClassLoader()	
				.getResourceAsStream("jp/go/aist/rtm/rtcbuilder/python/template/Py_Global_Init_src.template");
		File targetDirectory = new File(outDir + File.separator + "_GlobalIDL");
		if( !targetDirectory.isDirectory() ) {
			targetDirectory.mkdir();
        }
		result.add(TemplateUtil.createGeneratedResult(ins, contextMap, File.separator + "_GlobalIDL" + File.separator + "__init__.py"));

		try {
			if( ins != null) ins.close();
		} catch (Exception e) {
			throw new RuntimeException(e); // system error
		}

		return result;
	}
	
	/**
	 * Global IDL POA�pinit�t�@�C���𐶐�����
	 * 
	 * @param idlFileParm	�����Ώۂ�IDL�t�@�C���p�����[�^
	 * @param contextMap	���������
	 * @param result	�������ʊi�[��
	 * @return �o�͌��ʂ̃��X�g
	 */
	protected List<GeneratedResult> generateGlobalPOAInitSource(Map<String, Object> contextMap, List<GeneratedResult> result, String outDir) {
		InputStream ins = null;

		ins = PythonGenerateManager.class.getClassLoader()	
				.getResourceAsStream("jp/go/aist/rtm/rtcbuilder/python/template/Py_Global_POA_Init_src.template");
		File targetDirectory = new File(outDir + File.separator + "_GlobalIDL__POA");
		if( !targetDirectory.isDirectory() ) {
			targetDirectory.mkdir();
        }
		result.add(TemplateUtil.createGeneratedResult(ins, contextMap, File.separator + "_GlobalIDL__POA" + File.separator + "__init__.py"));

		try {
			if( ins != null) ins.close();
		} catch (Exception e) {
			throw new RuntimeException(e); // system error
		}

		return result;
	}
	
	protected List<GeneratedResult> generateCompileExtend(Map<String, Object> contextMap, List<GeneratedResult> result) {
		InputStream ins = null;

		ins = PythonGenerateManager.class.getClassLoader()	
				.getResourceAsStream("jp/go/aist/rtm/rtcbuilder/python/template/_100/IDL_Compile_bat.template");
		result.add(TemplateUtil.createGeneratedResult(ins, contextMap, "idlcompile.bat"));
		//
		ins = PythonGenerateManager.class.getClassLoader()	
				.getResourceAsStream("jp/go/aist/rtm/rtcbuilder/python/template/_100/IDL_Compile_sh.template");
		result.add(TemplateUtil.createGeneratedResult(ins, contextMap, "idlcompile.sh"));

		try {
			if( ins != null) ins.close();
		} catch (Exception e) {
			throw new RuntimeException(e); // system error
		}

		return result;
	}
	
	protected List<GeneratedResult> generateCommonExtend(Map<String, Object> contextMap, List<GeneratedResult> result) {
		return result;		
	}
}