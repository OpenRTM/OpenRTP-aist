package jp.go.aist.rtm.rtcbuilder.ros.manager;

import static jp.go.aist.rtm.rtcbuilder.util.RTCUtil.form;

import java.io.InputStream;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.ParamBase;
import jp.go.aist.rtm.rtcbuilder.generator.GeneratedResult;
import jp.go.aist.rtm.rtcbuilder.manager.GenerateManager;
import jp.go.aist.rtm.rtcbuilder.ros.IRtcBuilderConstantsROS;
import jp.go.aist.rtm.rtcbuilder.ros.param.ROSParam;
import jp.go.aist.rtm.rtcbuilder.ros.template.TemplateHelperROS;
import jp.go.aist.rtm.rtcbuilder.template.TemplateUtil;

/**
 * 一般ファイルの出力を制御するマネージャ
 */
public class CommonGenerateManager extends GenerateManager {

	static final String TEMPLATE_PATH = "jp/go/aist/rtm/rtcbuilder/ros/template";

	protected static final String MSG_ERROR_GENERATE_FILE = "Common generation error. [{0}]";

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
		return IRtcBuilderConstants.MIDDLEWARE_ROS;
	}

	/**
	 * ファイルを出力する
	 *
	 * @param generatorParam
	 * @return 出力結果のリスト
	 */
	public List<GeneratedResult> generateTemplateCode(ParamBase param) {
		ROSParam rosParam = (ROSParam)param;
		
		Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("template", TEMPLATE_PATH);
		contextMap.put("rosParam", rosParam);
		contextMap.put("tmpltHelper", new TemplateHelperROS());
		contextMap.put("currentYear", Year.now().getValue());

		return generateTemplateCode(contextMap);
	}

	public List<GeneratedResult> generateTemplateCode(
			Map<String, Object> contextMap) {
		List<GeneratedResult> result = new ArrayList<GeneratedResult>();

		//pkg
		result.add(generateREADME(contextMap));
		result.add(generatePackageXML(contextMap));
		result.add(generateLICENSE(contextMap));
		result.add(generateConfigParams(contextMap));
		result.add(generateLaunch(contextMap));

		//pkg_interfaces
		result.add(generateInterfaceREADME(contextMap));
		result.add(generateInterfacePackageXML(contextMap));
		result.add(generateInterfaceLICENSE(contextMap));

		result.removeIf(Objects::isNull);
		
		return result;
	}

	public GeneratedResult generatePackageXML(Map<String, Object> contextMap) {
		ROSParam rosParam = (ROSParam) contextMap.get("rosParam");
		String outfile = rosParam.getPackageName() + "/package.xml";
		String infile = "common/Package.xml.vsl";
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generateREADME(Map<String, Object> contextMap) {
		ROSParam rosParam = (ROSParam) contextMap.get("rosParam");
		String outfile = rosParam.getPackageName() + "/README.md";
		String infile = "common/README.vsl";
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generateLICENSE(Map<String, Object> contextMap) {
		ROSParam rosParam = (ROSParam) contextMap.get("rosParam");
		String outfile = rosParam.getPackageName() + "/LICENSE";
		String infile = "";
		switch (rosParam.getLicense()) {
		case IRtcBuilderConstantsROS.LICENSE_APACHE:
			infile = "common/LICENSE.apache.vsl";
			break;
		case IRtcBuilderConstantsROS.LICENSE_MIT:
			infile = "common/LICENSE.MIT.vsl";
			break;
		case IRtcBuilderConstantsROS.LICENSE_BSD:
			infile = "common/LICENSE.BSD3.vsl";
			break;
		case IRtcBuilderConstantsROS.LICENSE_GPL:
			infile = "common/LICENSE.GPL3.vsl";
			break;
		case IRtcBuilderConstantsROS.LICENSE_LGPL:
			infile = "common/LICENSE.LGPL3.vsl";
			break;
		case IRtcBuilderConstantsROS.LICENSE_PROPRIETARY:
			infile = "common/LICENSE.Proprietary.vsl";
			break;
		default:
			return null;
		}
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generateConfigParams(Map<String, Object> contextMap) {
		ROSParam rosParam = (ROSParam) contextMap.get("rosParam");
		String outfile = rosParam.getPackageName() + "/config/params.yaml";
		String infile = "common/Params.yaml.vsl";
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generateLaunch(Map<String, Object> contextMap) {
		ROSParam rosParam = (ROSParam) contextMap.get("rosParam");
		String outfile = rosParam.getPackageName() + "/launch/" + rosParam.getNodeName() + ".launch.py";
		String infile = "common/Launch.py.vsl";
		return generate(infile, outfile, contextMap);
	}

	/////
	public GeneratedResult generateInterfacePackageXML(Map<String, Object> contextMap) {
		ROSParam rosParam = (ROSParam) contextMap.get("rosParam");
		String outfile = rosParam.getPackageName() + "_interfaces/package.xml";
		String infile = "common/interface_package.xml.vsl";
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generateInterfaceREADME(Map<String, Object> contextMap) {
		ROSParam rosParam = (ROSParam) contextMap.get("rosParam");
		String outfile = rosParam.getPackageName() + "_interfaces/README.md";
		String infile = "common/interface_README.vsl";
		return generate(infile, outfile, contextMap);
	}

	public GeneratedResult generateInterfaceLICENSE(Map<String, Object> contextMap) {
		ROSParam rosParam = (ROSParam) contextMap.get("rosParam");
		String outfile = rosParam.getPackageName() + "_interfaces/LICENSE";
		String infile = "";
		switch (rosParam.getLicense()) {
		case IRtcBuilderConstantsROS.LICENSE_APACHE:
			infile = "common/LICENSE.apache.vsl";
			break;
		case IRtcBuilderConstantsROS.LICENSE_MIT:
			infile = "common/LICENSE.MIT.vsl";
			break;
		case IRtcBuilderConstantsROS.LICENSE_BSD:
			infile = "common/LICENSE.BSD3.vsl";
			break;
		case IRtcBuilderConstantsROS.LICENSE_GPL:
			infile = "common/LICENSE.GPL3.vsl";
			break;
		case IRtcBuilderConstantsROS.LICENSE_LGPL:
			infile = "common/LICENSE.LGPL3.vsl";
			break;
		default:
			return null;
		}
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
