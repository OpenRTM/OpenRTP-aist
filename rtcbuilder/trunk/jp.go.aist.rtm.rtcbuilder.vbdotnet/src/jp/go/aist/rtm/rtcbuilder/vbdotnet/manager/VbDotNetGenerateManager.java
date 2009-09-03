package jp.go.aist.rtm.rtcbuilder.vbdotnet.manager;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.go.aist.rtm.rtcbuilder.generator.GeneratedResult;
import jp.go.aist.rtm.rtcbuilder.generator.param.RtcParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.ServicePortInterfaceParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.ServicePortParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.IdlFileParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.ServiceClassParam;
import jp.go.aist.rtm.rtcbuilder.manager.GenerateManager;
import jp.go.aist.rtm.rtcbuilder.template.TemplateHelper;
import jp.go.aist.rtm.rtcbuilder.template.TemplateUtil;
import jp.go.aist.rtm.rtcbuilder.ui.Perspective.LanguageProperty;
import jp.go.aist.rtm.rtcbuilder.vbdotnet.IRtcBuilderConstantsVbDotNet;
import jp.go.aist.rtm.rtcbuilder.vbdotnet.template.TemplateHelperVbDotNet;
import jp.go.aist.rtm.rtcbuilder.vbdotnet.template.VbDotNetConverter;
import jp.go.aist.rtm.rtcbuilder.vbdotnet.ui.Perspective.VbDotNetProperty;

public class VbDotNetGenerateManager extends GenerateManager {

	@Override
	public String getManagerKey() {
		return IRtcBuilderConstantsVbDotNet.LANG_VBDOTNET;
	}

	@Override
	public String getLangArgList() {
		return IRtcBuilderConstantsVbDotNet.LANG_VBDOTNET_ARG;
	}
	
	@Override
	public LanguageProperty getLanguageProperty(RtcParam rtcParam) {
		LanguageProperty langProp = null;
		if(rtcParam.isLanguageExist(IRtcBuilderConstantsVbDotNet.LANG_VBDOTNET) ) {
			langProp = new VbDotNetProperty();
		}
		return langProp;
	}

	/**
	 * �t�@�C�����o�͂���
	 * 
	 * @param generatorParam	�����p�p�����[�^
	 * @return �o�͌��ʂ̃��X�g
	 */
	@Override
	public List<GeneratedResult> generateTemplateCode(RtcParam rtcParam) {
		InputStream ins = null;
		List<GeneratedResult> result = new ArrayList<GeneratedResult>();

		if (rtcParam.isLanguageExist(IRtcBuilderConstantsVbDotNet.LANG_VBDOTNET) && rtcParam.getName() != null) {
			Map<String, Object> contextMap = new HashMap<String, Object>();
			contextMap.put("rtcParam", rtcParam);
			contextMap.put("tmpltHelper", new TemplateHelper());
			contextMap.put("tmpltHelperVb", new TemplateHelperVbDotNet());
			contextMap.put("vbConv", new VbDotNetConverter());

			result = generateRTCSource(contextMap, result);
			result = generateAppConfig(contextMap, result);
			result = generateProject(contextMap, result);
			result = generateUserProject(contextMap, result);
			result = generateGenFile(contextMap, result);
			result = generateProgramFile(contextMap, result);
			result = generateAssemblyInfoFile(contextMap, result, rtcParam.getOutputProject());

			//IDL�t�@�C�����ɋL�q����Ă���ServiceClassParam��ݒ肷��
			for( IdlFileParam idlFileParam : rtcParam.getProviderIdlPathes() ) {
				for (ServiceClassParam serviceClassParam : rtcParam.getServiceClassParams()) {
					if( idlFileParam.getIdlPath().equals(serviceClassParam.getIdlPath()) )
						idlFileParam.addServiceClassParams(serviceClassParam);
				}
			}

			//Provider�ɎQ�Ƃ���Ă���ServiceClassParam���쐬����
			Set<ServiceClassParam> providerRefenencedServiceClassParam = new HashSet<ServiceClassParam>();
			for( ServicePortParam servicePort : rtcParam.getServicePorts() ) {
				for( ServicePortInterfaceParam serviceInterface : servicePort.getServicePortInterfaces() ) {
					if( serviceInterface.getDirection().equals(ServicePortInterfaceParam.INTERFACE_DIRECTION_PROVIDED) ) {
						ServiceClassParam find = null;
						for (ServiceClassParam serviceClassParam : rtcParam.getServiceClassParams()) {
							if (serviceInterface.getInterfaceType().equals(
									serviceClassParam.getName())) {
								find = serviceClassParam;
								break;
							}
						}
						if (find != null) {
							providerRefenencedServiceClassParam.add(find);
						}
					}
				}
			}

			for (IdlFileParam idlFileParm : rtcParam.getProviderIdlPathes()) {
				contextMap = new HashMap<String, Object>();
				contextMap.put("rtcParam", rtcParam);
				contextMap.put("idlFileParam", idlFileParm);
				contextMap.put("tmpltHelper", new TemplateHelper());
				contextMap.put("tmpltHelperVb", new TemplateHelperVbDotNet());
				contextMap.put("vbConv", new VbDotNetConverter());
				
				result = generateSVCSource(contextMap, result);
				result = generateSVCExtend(contextMap, result);
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
	 * RTC�̃\�[�X�E�t�@�C���𐶐�����
	 * 
	 * @param result	�����p�p�����[�^
	 * @return �o�͌��ʂ̃��X�g
	 */
	protected List<GeneratedResult> generateRTCSource(Map<String, Object> contextMap, List<GeneratedResult> result) {
		InputStream ins = null;

		ins = VbDotNetGenerateManager.class.getClassLoader()	
			.getResourceAsStream("jp/go/aist/rtm/rtcbuilder/vbdotnet/template/VbDotNet_RTC_Source.template");
		result.add(TemplateUtil.createGeneratedResult(ins, contextMap, 
				((RtcParam)contextMap.get("rtcParam")).getName() + ".vb"));

		try {
			if( ins != null) ins.close();
		} catch (Exception e) {
			throw new RuntimeException(e); // system error
		}

		return result;
	}
	
	/**
	 * App.config�t�@�C���𐶐�����
	 * 
	 * @param result	�����p�p�����[�^
	 * @return �o�͌��ʂ̃��X�g
	 */
	protected List<GeneratedResult> generateAppConfig(Map<String, Object> contextMap, List<GeneratedResult> result) {
		InputStream ins = null;

		ins = VbDotNetGenerateManager.class.getClassLoader()	
			.getResourceAsStream("jp/go/aist/rtm/rtcbuilder/vbdotnet/template/App_Config.template");
		result.add(TemplateUtil.createGeneratedResult(ins, contextMap, "App.config"));

		try {
			if( ins != null) ins.close();
		} catch (Exception e) {
			throw new RuntimeException(e); // system error
		}

		return result;
	}
	
	/**
	 * �v���W�F�N�g�t�@�C���𐶐�����
	 * 
	 * @param result	�����p�p�����[�^
	 * @return �o�͌��ʂ̃��X�g
	 */
	protected List<GeneratedResult> generateProject(Map<String, Object> contextMap, List<GeneratedResult> result) {
		InputStream ins = null;

		ins = VbDotNetGenerateManager.class.getClassLoader()	
			.getResourceAsStream("jp/go/aist/rtm/rtcbuilder/vbdotnet/template/VbDotNet_Project.template");
		result.add(TemplateUtil.createGeneratedResult(ins, contextMap, 
				((RtcParam)contextMap.get("rtcParam")).getName() + ".vbproj"));

		try {
			if( ins != null) ins.close();
		} catch (Exception e) {
			throw new RuntimeException(e); // system error
		}

		return result;
	}
	
	/**
	 * User�v���W�F�N�g�t�@�C���𐶐�����
	 * 
	 * @param result	�����p�p�����[�^
	 * @return �o�͌��ʂ̃��X�g
	 */
	protected List<GeneratedResult> generateUserProject(Map<String, Object> contextMap, List<GeneratedResult> result) {
		InputStream ins = null;

		ins = VbDotNetGenerateManager.class.getClassLoader()	
			.getResourceAsStream("jp/go/aist/rtm/rtcbuilder/vbdotnet/template/VbDotNet_Project_User.template");
		result.add(TemplateUtil.createGeneratedResult(ins, contextMap, 
				((RtcParam)contextMap.get("rtcParam")).getName() + ".vbproj.user"));

		try {
			if( ins != null) ins.close();
		} catch (Exception e) {
			throw new RuntimeException(e); // system error
		}

		return result;
	}
	
	/**
	 * Gen�t�@�C���𐶐�����
	 * 
	 * @param result	�����p�p�����[�^
	 * @return �o�͌��ʂ̃��X�g
	 */
	protected List<GeneratedResult> generateGenFile(Map<String, Object> contextMap, List<GeneratedResult> result) {
		InputStream ins = null;

		ins = VbDotNetGenerateManager.class.getClassLoader()	
			.getResourceAsStream("jp/go/aist/rtm/rtcbuilder/vbdotnet/template/VbDotNet_Gen.template");
		result.add(TemplateUtil.createGeneratedResult(ins, contextMap, "gen.xml"));

		try {
			if( ins != null) ins.close();
		} catch (Exception e) {
			throw new RuntimeException(e); // system error
		}

		return result;
	}

	/**
	 * Program�t�@�C���𐶐�����
	 * 
	 * @param result	�����p�p�����[�^
	 * @return �o�͌��ʂ̃��X�g
	 */
	protected List<GeneratedResult> generateProgramFile(Map<String, Object> contextMap, List<GeneratedResult> result) {
		InputStream ins = null;

		ins = VbDotNetGenerateManager.class.getClassLoader()	
			.getResourceAsStream("jp/go/aist/rtm/rtcbuilder/vbdotnet/template/VbDotNet_Program.template");
		result.add(TemplateUtil.createGeneratedResult(ins, contextMap, "Program.vb"));

		try {
			if( ins != null) ins.close();
		} catch (Exception e) {
			throw new RuntimeException(e); // system error
		}

		return result;
	}

	/**
	 * AssemblyInfo�t�@�C���𐶐�����
	 * 
	 * @param result	�����p�p�����[�^
	 * @return �o�͌��ʂ̃��X�g
	 */
	protected List<GeneratedResult> generateAssemblyInfoFile(Map<String, Object> contextMap, List<GeneratedResult> result, String outDir) {
		InputStream ins = null;

		ins = VbDotNetGenerateManager.class.getClassLoader()	
			.getResourceAsStream("jp/go/aist/rtm/rtcbuilder/vbdotnet/template/VbDotNet_Assemble_Info.template");
		File targetDirectory = new File(outDir + File.separator + "My Project");
		if( !targetDirectory.isDirectory() ) {
			targetDirectory.mkdir();
        }
		result.add(TemplateUtil.createGeneratedResult(ins, contextMap, File.separator + "My Project" + File.separator + "AssemblyInfo.vb"));

		try {
			if( ins != null) ins.close();
		} catch (Exception e) {
			throw new RuntimeException(e); // system error
		}

		return result;
	}

	/**
	 * RTCImpl�̃\�[�X�E�t�@�C���𐶐�����
	 * 
	 * @param rtcParam	�����p�p�����[�^
	 * @param result	�������ʊi�[��
	 * @return �o�͌��ʂ̃��X�g
	 */
	protected List<GeneratedResult> generateSVCSource(Map<String, Object> contextMap, List<GeneratedResult> result) {
		InputStream ins = null;

		for(ServiceClassParam serviceClass : ((IdlFileParam)contextMap.get("idlFileParam")).getServiceClassParams()) {
			ins = VbDotNetGenerateManager.class.getClassLoader()	
					.getResourceAsStream("jp/go/aist/rtm/rtcbuilder/vbdotnet/template/VbDotNet_SVC_Source.template");
			contextMap.put("serviceClassParam", serviceClass);
			result.add(TemplateUtil.createGeneratedResult(ins, contextMap, 
									TemplateHelper.getBasename(serviceClass.getName()) + "Impl.vb"));
	
			try {
				if( ins != null) ins.close();
			} catch (Exception e) {
				throw new RuntimeException(e); // system error
			}
		}

		return result;
	}
	protected List<GeneratedResult> generateSVCExtend(Map contextMap, List<GeneratedResult> result) {
		return result;		
	}

}