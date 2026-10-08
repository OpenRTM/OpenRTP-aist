package jp.go.aist.rtm.rtcbuilder.ros;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import org.openrtp.namespaces.ros.version01.RosProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.go.aist.rtm.rtcbuilder.IRTCBMessageConstants;
import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.RtcBuilderPlugin;
import jp.go.aist.rtm.rtcbuilder.generator.param.GeneratorParam;
import jp.go.aist.rtm.rtcbuilder.manager.GenerateManager;
import jp.go.aist.rtm.rtcbuilder.ros.param.ROSParam;
import jp.go.aist.rtm.rtcbuilder.ros.ui.editors.IMessageConstantsROS;
import jp.go.aist.rtm.toolscommon.profiles.util.XmlHandlerROS;

public class ProfileHandlerROS {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProfileHandlerROS.class);	

	private List<GenerateManager> managerList = null;
	private boolean isDirect = false;

	public ProfileHandlerROS() {
		super();
		managerList = RtcBuilderPlugin.getDefault().getLoader().getManagerList(IRtcBuilderConstants.MIDDLEWARE_ROS);
	}

	public 	String createInitialROSXml() {
		String result = "";
		RosProfile profile = ParamUtilROS.initialROSXml();
		XmlHandlerROS handler = new XmlHandlerROS();
		try {
			result = handler.convertToXmlROS(profile);
		} catch (Exception e) {
			LOGGER.error("Fail to convert rtc-profile", e);
		}
		return result;
	}
	
	public GeneratorParam restorefromROSProfile(RosProfile profile) throws Exception {
		GeneratorParam generatorParam = null;
		try {
			generatorParam = new GeneratorParam();
			ParamUtilROS putil = new ParamUtilROS();
			ROSParam rosParam = putil.convertFromROSModule(profile, generatorParam, managerList, isDirect);
			generatorParam.setROSParam(rosParam);
		} catch (FileNotFoundException e) {
			throw new Exception(IMessageConstantsROS.PROFIE_LOAD_ERROR, e);
		} catch (IOException e) {
			throw new Exception(IMessageConstantsROS.PROFIE_LOAD_ERROR, e);
		}
		return generatorParam;
	}

	public String convert2ROSXML(GeneratorParam generatorParam) throws Exception {
	    String xmlFile = "";
	    ParamUtilROS putil = new ParamUtilROS();
		RosProfile profile = putil.convertToROSModule(generatorParam, managerList);
		XmlHandlerROS handler = new XmlHandlerROS();
		xmlFile = handler.convertToXmlROS(profile);
		return xmlFile;
	}

	public String convert2ROSXML(ROSParam target) throws Exception {
	    String xmlFile = "";
	    ParamUtilROS putil = new ParamUtilROS();
		RosProfile profile = putil.convertToROSModule(target, managerList);
		XmlHandlerROS handler = new XmlHandlerROS();
		xmlFile = handler.convertToXmlROS(profile);
		return xmlFile;
	}

	public RosProfile convert2XMLProfile(ROSParam target) throws Exception {
	    ParamUtilROS putil = new ParamUtilROS();
	    RosProfile profile = putil.convertToROSModule(target, managerList);
		return profile;
	}

	public boolean validateROSXml(String targetString) throws Exception {
		XmlHandlerROS handler = new XmlHandlerROS();
		handler.validateXmlROSBySchema(targetString);
		return true;
	}
	
	public RosProfile restorefromXMLROS(String targetXML) throws Exception {
		XmlHandlerROS handler = new XmlHandlerROS();
		return handler.restoreFromXmlROS(targetXML);
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
		    XmlHandlerROS handler = new XmlHandlerROS();
		    RosProfile profile = handler.restoreFromXmlROS(tmp_sb.toString());

			generatorParam = new GeneratorParam();
			ParamUtilROS putil = new ParamUtilROS();
			ROSParam rosParam = putil.convertFromROSModule(profile, generatorParam, managerList);
			rosParam.setROSXml(tmp_sb.toString());
			generatorParam.setROSParam(rosParam);
		} catch (FileNotFoundException e) {
			throw new Exception(IRTCBMessageConstants.ERROR_PROFILE_RESTORE, e);
		} catch (IOException e) {
			throw new Exception(IRTCBMessageConstants.ERROR_PROFILE_RESTORE, e);
		}
		return generatorParam;
	}
	
	public void storeToXML(String filePath, GeneratorParam generatorParam) throws Exception {
	    ParamUtilROS putil = new ParamUtilROS();
		RosProfile profile = putil.convertToROSModule(generatorParam, managerList);
		XmlHandlerROS handler = new XmlHandlerROS();

		String xmlString = handler.convertToXmlROS(profile);
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
}
