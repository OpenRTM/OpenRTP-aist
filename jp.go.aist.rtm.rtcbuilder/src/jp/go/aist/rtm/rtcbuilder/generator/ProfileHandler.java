package jp.go.aist.rtm.rtcbuilder.generator;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.openrtp.namespaces.rtc.version03.RtcProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.go.aist.rtm.rtcbuilder.IRTCBMessageConstants;
import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.RtcBuilderPlugin;
import jp.go.aist.rtm.rtcbuilder.generator.param.GeneratorParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.ParamUtil;
import jp.go.aist.rtm.rtcbuilder.generator.param.RtcParam;
import jp.go.aist.rtm.rtcbuilder.manager.GenerateManager;
import jp.go.aist.rtm.toolscommon.profiles.util.XmlHandler;
import jp.go.aist.rtm.toolscommon.profiles.util.YamlHandler;

public class ProfileHandler {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProfileHandler.class);

	private List<GenerateManager> managerList = null;
	private boolean isDirect = false;

	public ProfileHandler() {
		super();
		managerList = RtcBuilderPlugin.getDefault().getLoader().getManagerList(IRtcBuilderConstants.MIDDLEWARE_RTM);
	}

	public ProfileHandler(boolean source) {
		super();
		isDirect = source;
	}

	public void addManager(GenerateManager target) {
		if( managerList==null ) {
			managerList = new ArrayList<GenerateManager>();
		}
		managerList.add(target);
	}

	public boolean validateXml(String targetString) throws Exception {
		XmlHandler handler = new XmlHandler();
		handler.validateXmlRtcBySchema(targetString);
		return true;
	}

	public RtcProfile restorefromXML(String targetXML) throws Exception {
		XmlHandler handler = new XmlHandler();
		return handler.restoreFromXmlRtc(targetXML);
	}

	public GeneratorParam restorefromXMLFile(String filePath) throws Exception {
		return restorefromXMLFile(filePath, false);
	}
	public GeneratorParam restorefromXMLFile(String filePath, boolean isDirect) throws Exception {
		GeneratorParam generatorParam = null;
		try {
			StringBuffer tmp_sb = new StringBuffer();
			try( BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8")) ) {
				String tmp_str = null;
			    while((tmp_str = br.readLine()) != null){
			    	tmp_sb.append(tmp_str + "\r\n");
			    }
			}
		    XmlHandler handler = new XmlHandler();
		    RtcProfile profile = handler.restoreFromXmlRtc(tmp_sb.toString());

			generatorParam = new GeneratorParam();
			ParamUtil putil = new ParamUtil();
			RtcParam rtcParam = putil.convertFromModule(profile, generatorParam, managerList, isDirect);
		    rtcParam.setRtcXml(tmp_sb.toString());
			generatorParam.setRtcParam(rtcParam);
		} catch (FileNotFoundException e) {
			throw new Exception(IRTCBMessageConstants.ERROR_PROFILE_RESTORE, e);
		} catch (IOException e) {
			throw new Exception(IRTCBMessageConstants.ERROR_PROFILE_RESTORE, e);
		}
		return generatorParam;
	}

	public GeneratorParam restorefromRtcProfile(RtcProfile profile) throws Exception {
		GeneratorParam generatorParam = null;
		try {
			generatorParam = new GeneratorParam();
			ParamUtil putil = new ParamUtil();
			RtcParam rtcParam = putil.convertFromModule(profile, generatorParam, managerList, isDirect);
			generatorParam.setRtcParam(rtcParam);
		} catch (FileNotFoundException e) {
			throw new Exception(IRTCBMessageConstants.ERROR_PROFILE_RESTORE, e);
		} catch (IOException e) {
			throw new Exception(IRTCBMessageConstants.ERROR_PROFILE_RESTORE, e);
		}
		return generatorParam;
	}

	public String convert2XML(GeneratorParam generatorParam) throws Exception {
	    String xmlFile = "";
	    ParamUtil putil = new ParamUtil();
		RtcProfile profile = putil.convertToModule(generatorParam, managerList);
		XmlHandler handler = new XmlHandler();
		xmlFile = handler.convertToXmlRtc(profile);
		return xmlFile;
	}

	public String convert2XML(RtcParam target) throws Exception {
	    String xmlFile = "";
	    ParamUtil putil = new ParamUtil();
		RtcProfile profile = putil.convertToModule(target, managerList);
		XmlHandler handler = new XmlHandler();
		xmlFile = handler.convertToXmlRtc(profile);
		return xmlFile;
	}

	public RtcProfile convert2XMLProfile(RtcParam target) throws Exception {
	    ParamUtil putil = new ParamUtil();
		RtcProfile profile = putil.convertToModule(target, managerList);
		return profile;
	}

	public 	String createInitialRtcXml(String creationDate) {
		String result = "";
		RtcProfile profile = ParamUtil.initialXml(creationDate);
		XmlHandler handler = new XmlHandler();
		try {
			result = handler.convertToXmlRtc(profile);
		} catch (Exception e) {
			LOGGER.error("Fail to convert rtc-profile", e);
		}
		return result;
	}

	public void storeToXML(String filePath, GeneratorParam generatorParam) throws Exception {
	    ParamUtil putil = new ParamUtil();
		RtcProfile profile = putil.convertToModule(generatorParam, managerList);
		XmlHandler handler = new XmlHandler();

		String xmlString = handler.convertToXmlRtc(profile);
		try( BufferedWriter outputFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8")) ) {
			String lineSeparator = System.getProperty( "line.separator" );
			if( lineSeparator==null || lineSeparator.equals("") ) lineSeparator = "\n";
			String splitStr[] = xmlString.split(lineSeparator);
			for(int intIdx=0;intIdx<splitStr.length;intIdx++) {
				outputFile.write(splitStr[intIdx]);
				outputFile.newLine();
			}
		}
	}

	//YAML
	public void createYaml(String targetFile, GeneratorParam targetRtc) throws Exception {
	    ParamUtil putil = new ParamUtil();
		RtcProfile profile = putil.convertToModule(targetRtc, managerList);
		YamlHandler handler = new YamlHandler();
		String yamlText = handler.convertToYamlRtc(profile);
		String lineSeparator = System.getProperty( "line.separator" );
		if( lineSeparator==null || lineSeparator.equals("") ) lineSeparator = "\n";
		String[] yamlSplt = yamlText.split(lineSeparator);
		if( yamlSplt.length > 0 ) {
			try( BufferedWriter outputFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8")) ) {
				for(int intIdx=0;intIdx<yamlSplt.length;intIdx++) {
					outputFile.write(yamlSplt[intIdx]);
					outputFile.newLine();
				}
			}
		}
	}
	public GeneratorParam readYaml(String targetFile) throws Exception {
		BufferedInputStream inputFile = null;
		inputFile = new BufferedInputStream(new FileInputStream(targetFile));
		YamlHandler handler = new YamlHandler();
		RtcProfile profile = handler.restoreFromYamlRtc(inputFile);
		GeneratorParam generatorParam = new GeneratorParam();
		ParamUtil util = new ParamUtil();
		RtcParam rtcParam = util.convertFromModule(profile, generatorParam, managerList);
		generatorParam.setRtcParam(rtcParam);
		return generatorParam;

	}
}
