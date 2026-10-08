package jp.go.aist.rtm.rtcbuilder.ros.param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.go.aist.rtm.rtcbuilder.generator.param.AbstractRecordedParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.PropertyParam;
import jp.go.aist.rtm.rtcbuilder.nl.Messages;
import jp.go.aist.rtm.rtcbuilder.ros.IRtcBuilderConstantsROS;
import jp.go.aist.rtm.rtcbuilder.util.StringUtil;

public class ActionParam extends AbstractRecordedParam implements Serializable {
	private static final long serialVersionUID = -5584413830295784662L;

	private final String DEFAULT_SERVER_NAME = "server_action";
	private final String DEFAULT_CLIENT_NAME = "client_action";
	private final String DEFAULT_CALLBACK = "action_callback";

	private String name;
	private String role;
	private String type;
	private String callbackName;
	//
	private String docDescription;
	private String docGoal;
	private String docFeedback;
	private String docResult;
	//Properties
	private List<PropertyParam> properties = new ArrayList<PropertyParam>();
	
	public ActionParam() {
		this.name = "";
		this.role = "";
		this.type = "";
		this.callbackName = "";
		//
		this.docDescription = "";
		this.docGoal = "";
		this.docFeedback = "";
		this.docResult = "";
		//
		setUpdated(false);
	}

	public ActionParam(String role) {
		this.role = role;
		this.type = "";
		this.callbackName = this.DEFAULT_CALLBACK;
		if(role.equals(IRtcBuilderConstantsROS.SPEC_ACTION_SERVER)) {
			this.name = this.DEFAULT_SERVER_NAME;
		} else if(role.equals(IRtcBuilderConstantsROS.SPEC_ACTION_CLIENT)) {
			this.name = this.DEFAULT_CLIENT_NAME;
		}
		//
		this.docDescription = "";
		this.docGoal = "";
		this.docFeedback = "";
		this.docResult = "";
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

	public String getCallbackName() {
		return callbackName;
	}
	public void setCallbackName(String callback_name) {
		checkUpdated(this.callbackName, callback_name);
		this.callbackName = callback_name;
	}

	public String getDocDescription() {
		return docDescription;
	}
	public void setDocDescription(String doc_description) {
		checkUpdated(this.docDescription, doc_description);
		this.docDescription = doc_description;
	}

	public String getDocGoal() {
		return docGoal;
	}
	public void setDocGoal(String doc_goal) {
		checkUpdated(this.docGoal, doc_goal);
		this.docGoal = doc_goal;
	}

	public String getDocFeedback() {
		return docFeedback;
	}
	public void setDocFeedback(String doc_feedback) {
		checkUpdated(this.docFeedback, doc_feedback);
		this.docFeedback = doc_feedback;
	}

	public String getDocResult() {
		return docResult;
	}
	public void setDocResult(String doc_result) {
		checkUpdated(this.docResult, doc_result);
		this.docResult = doc_result;
	}

	public boolean isDocExist() {
		if( (docGoal==null || docGoal.equals("")) &&
			(docFeedback==null || docFeedback.equals("")) &&
			(docResult==null || docResult.equals("")) )
				return false;
		return true;
	}

	public List<PropertyParam> getProperties() {
		return properties;
	}
	/////
	public String validateInfo() {
		if(this.name == null || this.name.length() == 0) {
			return Messages.getString("IMC.VALIDATE_ACTION_NAME1");
		}
		if( StringUtil.checkHyphenSpaceDotJpn(this.name) ) {
			return Messages.getString("IMC.VALIDATE_ACTION_NAME2");
		}
		if( this.name.equals("/") ) {
			return Messages.getString("IMC.VALIDATE_ACTION_NAME5");
		}
		if( this.name.endsWith("/") ) {
			return Messages.getString("IMC.VALIDATE_ACTION_NAME3");
		}
		if( this.name.contains("//") || this.name.contains("__") ) {
			return Messages.getString("IMC.VALIDATE_ACTION_NAME4");
		}
		if( !StringUtil.checkStartedWithDigitFast(this.name) ) {
			return Messages.getString("IMC.VALIDATE_ACTION_NAME6");
		}

		if(this.type == null || this.type.length() == 0) {
			return Messages.getString("IMC.VALIDATE_ACTION_TYPE");
		}
		if( StringUtil.checkInvalidFormat(this.type) ) {
			return Messages.getString("IMC.VALIDATE_ACTION_TYPE2");
		}
		if(this.type.endsWith("msg") || this.type.endsWith("action")
				 || this.type.contains(" ") || this.type.contains("･")) {
			return Messages.getString("IMC.VALIDATE_ACTION_TYPE3");
		}
		
		if(this.callbackName == null || this.callbackName.length() == 0) {
			return Messages.getString("IMC.VALIDATE_ACTION_CALLBACK1");
		}
		if( !StringUtil.checkValidIdentifier(this.callbackName) ) {
			return Messages.getString("IMC.VALIDATE_ACTION_CALLBACK2");
		}
		if( !StringUtil.checkValidIdentifierCode(this.callbackName) ) {
			return Messages.getString("IMC.VALIDATE_ACTION_CALLBACK3");
		}

		return null;
	}
	
	public void convertInfo() {
		if(this.callbackName == null || this.callbackName.length() == 0) {
			this.callbackName = this.name + "_action";
		}
	}
}
