package jp.go.aist.rtm.rtcbuilder.ros.param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.go.aist.rtm.rtcbuilder.generator.param.AbstractRecordedParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.PropertyParam;
import jp.go.aist.rtm.rtcbuilder.nl.Messages;
import jp.go.aist.rtm.rtcbuilder.ros.IRtcBuilderConstantsROS;
import jp.go.aist.rtm.rtcbuilder.util.StringUtil;

public class ServiceParam extends AbstractRecordedParam implements Serializable {
	private static final long serialVersionUID = -5584413830295784662L;
	
	private final String DEFAULT_SERVER_NAME = "server_service";
	private final String DEFAULT_CLIENT_NAME = "client_service";
	private final String DEFAULT_SERVER_CALLBACK = "service_callback";
	private final String DEFAULT_CLIENT_VARIABLE = "client_variable";

	private String name;
	private String role;
	private String type;
	private String varCallbackName;
	//
	private String docDescription;
	private String docArgument;
	private String docReturn;
	//Properties
	private List<PropertyParam> properties = new ArrayList<PropertyParam>();

	public ServiceParam() {
		this.name = "";
		this.role = "";
		this.type = "";
		this.varCallbackName = "";
		//
		this.docDescription = "";
		this.docArgument = "";
		this.docReturn = "";
		//
		setUpdated(false);
	}

	public ServiceParam(String role) {
		this.role = role;
		this.type = "";
		
		if(role.equals(IRtcBuilderConstantsROS.SPEC_SERVICE_SERVER)) {
			this.name = this.DEFAULT_SERVER_NAME;
			this.varCallbackName = this.DEFAULT_SERVER_CALLBACK;
		} else if(role.equals(IRtcBuilderConstantsROS.SPEC_SERVICE_CLIENT)) {
			this.name = this.DEFAULT_CLIENT_NAME;
			this.varCallbackName = this.DEFAULT_CLIENT_VARIABLE;
		}
		//
		this.docDescription = "";
		this.docArgument = "";
		this.docReturn = "";
		//
		setUpdated(false);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		checkUpdated(this.name, name);
		this.name = name;
	}

	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		checkUpdated(this.role, role);
		this.role = role;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		checkUpdated(this.type, type);
		this.type = type;
	}

	public String getVarCallbackName() {
		return varCallbackName;
	}
	public void setVarCallbackName(String var_callback_name) {
		checkUpdated(this.varCallbackName, var_callback_name);
		this.varCallbackName = var_callback_name;
	}

	public String getDocDescription() {
		return docDescription;
	}
	public void setDocDescription(String doc_description) {
		checkUpdated(this.docDescription, doc_description);
		this.docDescription = doc_description;
	}

	public String getDocArgument() {
		return docArgument;
	}
	public void setDocArgument(String doc_argument) {
		checkUpdated(this.docArgument, doc_argument);
		this.docArgument = doc_argument;
	}

	public String getDocReturn() {
		return docReturn;
	}

	public void setDocReturn(String doc_return) {
		checkUpdated(this.docReturn, doc_return);
		this.docReturn = doc_return;
	}

	public boolean isDocExist() {
		if( (docArgument==null || docArgument.equals("")) &&
			(docReturn==null || docReturn.equals("")) )
				return false;
		return true;
	}

	public List<PropertyParam> getProperties() {
		return properties;
	}
	/////
	public String validateInfo() {
		if(this.name == null || this.name.length() == 0) {
			return Messages.getString("IMC.VALIDATE_SERVICE_NAME1");
		}
		if( StringUtil.checkHyphenSpaceDotJpn(this.name) ) {
			return Messages.getString("IMC.VALIDATE_SERVICE_NAME2");
		}
		if( this.name.equals("/") ) {
			return Messages.getString("IMC.VALIDATE_SERVICE_NAME5");
		}
		if( this.name.endsWith("/") ) {
			return Messages.getString("IMC.VALIDATE_SERVICE_NAME3");
		}
		if( this.name.contains("//") || this.name.contains("__") ) {
			return Messages.getString("IMC.VALIDATE_SERVICE_NAME4");
		}
		if( !StringUtil.checkStartedWithDigitFast(this.name) ) {
			return Messages.getString("IMC.VALIDATE_SERVICE_NAME6");
		}
		
		if( this.type==null || this.type.length()==0 ) {
			return Messages.getString("IMC.VALIDATE_SERVICE_TYPE1");
		}
		if( StringUtil.checkInvalidFormat(this.type) ) {
			return Messages.getString("IMC.VALIDATE_SERVICE_TYPE2");
		}
		if(this.type.endsWith("msg") || this.type.endsWith("action")
				 || this.type.contains(" ") || this.type.contains("･")) {
			return Messages.getString("IMC.VALIDATE_SERVICE_TYPE3");
		}

		if(this.varCallbackName == null || this.varCallbackName.length() == 0) {
			return Messages.getString("IMC.VALIDATE_SERVICE_CALLBACK1");
		}
		if( !StringUtil.checkValidIdentifier(this.varCallbackName) ) {
			return Messages.getString("IMC.VALIDATE_SERVICE_CALLBACK2");
		}
		if( !StringUtil.checkValidIdentifierCode(this.varCallbackName) ) {
			return Messages.getString("IMC.VALIDATE_TOPIC_CALLBACK3");
		}

		return null;
	}
	
	public void convertInfo() {
		if(this.varCallbackName == null || this.varCallbackName.length() == 0) {
			if(role.equals(IRtcBuilderConstantsROS.SPEC_SERVICE_SERVER)) {
				this.varCallbackName = this.name;
			} else if(role.equals(IRtcBuilderConstantsROS.SPEC_SERVICE_CLIENT)) {
				this.varCallbackName = this.name + "_client";
			}
		}
	}
}
