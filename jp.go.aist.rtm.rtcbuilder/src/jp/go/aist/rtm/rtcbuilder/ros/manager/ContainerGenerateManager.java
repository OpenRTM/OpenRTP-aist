package jp.go.aist.rtm.rtcbuilder.ros.manager;

import static jp.go.aist.rtm.rtcbuilder.util.RTCUtil.form;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.ParamBase;
import jp.go.aist.rtm.rtcbuilder.container.param.ContainerParam;
import jp.go.aist.rtm.rtcbuilder.container.param.setting.ContainerConfig;
import jp.go.aist.rtm.rtcbuilder.generator.GeneratedResult;
import jp.go.aist.rtm.rtcbuilder.manager.GenerateManager;
import jp.go.aist.rtm.rtcbuilder.ros.param.ROSParam;
import jp.go.aist.rtm.rtcbuilder.template.TemplateHelper;
import jp.go.aist.rtm.rtcbuilder.template.TemplateUtil;
import jp.go.aist.rtm.rtcbuilder.ui.preference.ContainerPreferenceManager;

/**
 * コンテナファイルの出力を制御するマネージャ
 */
public class ContainerGenerateManager extends GenerateManager {

	static final String TEMPLATE_PATH = "jp/go/aist/rtm/rtcbuilder/ros/template";

	static final String MSG_ERROR_GENERATE_FILE = "Container generation error. [{0}]";

	@Override
	public String getManagerKey() {
		return "Common";
	}

	@Override
	public String getLangArgList() {
		return null;
	}

	@Override
	public String getTargetMiddleware() {
		return IRtcBuilderConstants.MIDDLEWARE_ALL;
	}

	/**
	 * ファイルを出力する
	 *
	 * @param generatorParam
	 * @return 出力結果のリスト
	 */
	public List<GeneratedResult> generateTemplateCode(ParamBase baseParam) {
		ROSParam rosParam = (ROSParam)baseParam;
		
		if(rosParam.getContainerSettings() == null || rosParam.getContainerSettings().size() == 0) {
			return new ArrayList<GeneratedResult>();
		}

		Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("template", TEMPLATE_PATH);
		contextMap.put("rosParam", rosParam);
		contextMap.put("tmpltHelper", new TemplateHelper());
		
		ContainerConfig containerConfig;
		if(rosParam.getContainerConfig()==null) {
			String configText = ContainerPreferenceManager.getInstance().getSettings();
			ObjectMapper mapper = new ObjectMapper();
			try {
				containerConfig = mapper.readValue(configText, ContainerConfig.class);
				rosParam.setContainerConfig(containerConfig);
			} catch (Exception e) {
				return new ArrayList<GeneratedResult>();
			}
		}

		List<GeneratedResult> result = new ArrayList<GeneratedResult>();
		
		for(ContainerParam param : rosParam.getContainerSettings()) {
			param.prepareLibraries(rosParam.getContainerConfig());
			contextMap.put("containerParam", param);
			
			StringBuilder builder = new StringBuilder();
			builder.append("Dockerfile/");
			builder.append(rosParam.getNodeName()).append("__");
			
			String osInfo = param.getOsVersion();
			String[] elems = osInfo.split(" ");
			if(0<elems.length) {
				builder.append(elems[0]);
			}
			builder.append("-");
			if(1<elems.length) {
				builder.append(elems[1]);
			}
			builder.append("__");
			
			String mwName = param.getMiddleware().replace(" ", "");
			builder.append(mwName).append("-");
			builder.append(param.getMdlVersion().toLowerCase()).append("__");
			builder.append(convLanguage(param.getLanguage())).append("_");
			builder.append(param.getConfiguration());
			builder.append(".Dockerfile");
			
			result.add(generateROSContainer(contextMap, builder.toString()));
		}

		return result;
	}
	
	private String convLanguage(String source) {
		return source.replace("+", "p").toLowerCase();
	}

	public GeneratedResult generateROSContainer(Map<String, Object> contextMap, String outfile) {
		String infile = "container/ROS_Container.vsl";
		return generate(infile, outfile, contextMap);
	}

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
					new String[] { outfile }), e);
		}
	}

}
