package jp.go.aist.rtm.rtcbuilder.ros.param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.go.aist.rtm.rtcbuilder.generator.param.AbstractRecordedParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.PropertyParam;
import jp.go.aist.rtm.rtcbuilder.nl.Messages;
import jp.go.aist.rtm.rtcbuilder.ros.IRtcBuilderConstantsROS;
import jp.go.aist.rtm.rtcbuilder.util.StringUtil;

public class TopicParam extends AbstractRecordedParam implements Serializable {
	private static final long serialVersionUID = 533482869407934299L;

	private final String DEFAULT_SUBSCRIBE_NAME = "sub_topic";
	private final String DEFAULT_PUBLISH_NAME = "pub_topic";
	private final String DEFAULT_RELIABILITY = "Reliable";
	private final String DEFAULT_HISTORY = "KeepLast";
	private final Integer DEFAULT_DEPTH = 10;
	private final String DEFAULT_SUBSCRIBE_CALLBACK = "subscribe_callback";
	private final String DEFAULT_PUBLISH_VARIABLE = "publish_variable";

	private String name;
	private String role;
	private String messageType;

	private String reliabilityType;
	private String historyType;
	private Integer depth;

	private String varCallbackName;
	//
	private String docDescription;
	private String docType;
	private String docSemantics;
	private String docUnit;
	private String docOccurrence;
	//Properties
	private List<PropertyParam> properties = new ArrayList<PropertyParam>();

	public TopicParam() {
		this.name = "";
		this.role = "";
		this.messageType = "";
		this.reliabilityType = "";
		this.historyType = "";
		this.depth = 0;
		this.varCallbackName = "";
		//
		this.docDescription = "";
		this.docType = "";
		this.docSemantics = "";
		this.docUnit = "";
		this.docOccurrence = "";
		//
		setUpdated(false);
	}

	public TopicParam(String role) {
		this.role = role;

		this.messageType = "";
		this.reliabilityType = this.DEFAULT_RELIABILITY;
		this.historyType = this.DEFAULT_HISTORY;
		this.depth = this.DEFAULT_DEPTH;
		
		if(role.equals(IRtcBuilderConstantsROS.SPEC_TOPIC_SUBSCRIBE)) {
			this.name = this.DEFAULT_SUBSCRIBE_NAME;
			this.varCallbackName = this.DEFAULT_SUBSCRIBE_CALLBACK;
		} else if(role.equals(IRtcBuilderConstantsROS.SPEC_TOPIC_PUBLISH)) {
			this.name = this.DEFAULT_PUBLISH_NAME;
			this.varCallbackName = this.DEFAULT_PUBLISH_VARIABLE;
		}
		//
		this.docDescription = "";
		this.docType = "";
		this.docSemantics = "";
		this.docUnit = "";
		this.docOccurrence = "";
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

	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String message_type) {
		checkUpdated(this.messageType, message_type);
		this.messageType = message_type;
	}

	public String getReliabilityType() {
		return reliabilityType;
	}
	public void setReliabilityType(String reliability_type) {
		checkUpdated(this.reliabilityType, reliability_type);
		this.reliabilityType = reliability_type;
	}

	public String getHistoryType() {
		return historyType;
	}
	public void setHistoryType(String history_type) {
		checkUpdated(this.historyType, history_type);
		this.historyType = history_type;
	}

	public Integer getDepth() {
		return depth;
	}
	public void setDepth(Integer depth) {
		checkUpdated(this.depth, depth);
		this.depth = depth;
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

	public String getDocType() {
		return docType;
	}
	public void setDocType(String doc_type) {
		checkUpdated(this.docType, doc_type);
		this.docType = doc_type;
	}

	public String getDocSemantics() {
		return docSemantics;
	}
	public void setDocSemantics(String doc_semantics) {
		checkUpdated(this.docSemantics, doc_semantics);
		this.docSemantics = doc_semantics;
	}

	public String getDocUnit() {
		return docUnit;
	}
	public void setDocUnit(String doc_unit) {
		checkUpdated(this.docUnit, doc_unit);
		this.docUnit = doc_unit;
	}

	public String getDocOccurrence() {
		return docOccurrence;
	}
	public void setDocOccurrence(String doc_occurrence) {
		checkUpdated(this.docOccurrence, doc_occurrence);
		this.docOccurrence = doc_occurrence;
	}

	public boolean isDocExist() {
		if( (docType==null || docType.equals("")) &&
			(docSemantics==null || docSemantics.equals("")) &&
			(docUnit==null || docUnit.equals("")) &&
			(docOccurrence==null || docOccurrence.equals("")) )
				return false;
		return true;
	}

	public List<PropertyParam> getProperties() {
		return properties;
	}
	/////
	public String validateInfo() {
		if(this.name == null || this.name.length() == 0) {
			return Messages.getString("IMC.VALIDATE_TOPIC_NAME1");
		}
		if( StringUtil.checkHyphenSpaceDotJpn(this.name) ) {
			return Messages.getString("IMC.VALIDATE_TOPIC_NAME2");
		}
		if( this.name.equals("/") ) {
			return Messages.getString("IMC.VALIDATE_TOPIC_NAME5");
		}
		if( this.name.endsWith("/") ) {
			return Messages.getString("IMC.VALIDATE_TOPIC_NAME3");
		}
		if( this.name.contains("//") || this.name.contains("__") ) {
			return Messages.getString("IMC.VALIDATE_TOPIC_NAME4");
		}
		if( !StringUtil.checkStartedWithDigitFast(this.name) ) {
			return Messages.getString("IMC.VALIDATE_TOPIC_NAME6");
		}
		if( StringUtil.checkInvalidTilde(this.name) ) {
			return Messages.getString("IMC.VALIDATE_TOPIC_NAME7");
		}
		
		if(this.messageType == null || this.messageType.length() == 0) {
			return Messages.getString("IMC.VALIDATE_TOPIC_TYPE1");
		}
		if( StringUtil.checkInvalidFormat(this.messageType) ) {
			return Messages.getString("IMC.VALIDATE_TOPIC_TYPE2");
		}
		if(this.messageType.endsWith("srv") || this.messageType.endsWith("action")
				 || this.messageType.contains(" ") || this.messageType.contains("･")) {
			return Messages.getString("IMC.VALIDATE_TOPIC_TYPE3");
		}
		
		if(this.historyType !=null ) {
			if(this.historyType.equals("KeepLast")) {
				if(this.depth < 1) {
					return Messages.getString("IMC.VALIDATE_TOPIC_DEPTH");
				}
			}
		}

		if(this.varCallbackName == null || this.varCallbackName.length() == 0) {
			return Messages.getString("IMC.VALIDATE_TOPIC_CALLBACK1");
		}
		if( !StringUtil.checkValidIdentifier(this.varCallbackName) ) {
			return Messages.getString("IMC.VALIDATE_TOPIC_CALLBACK2");
		}
		if( !StringUtil.checkValidIdentifierCode(this.varCallbackName) ) {
			return Messages.getString("IMC.VALIDATE_TOPIC_CALLBACK3");
		}

		return null;
	}
	
	public void convertInfo() {
		if(this.varCallbackName == null || this.varCallbackName.length() == 0) {
			if(role.equals(IRtcBuilderConstantsROS.SPEC_TOPIC_SUBSCRIBE)) {
				this.varCallbackName = this.name + "_callback";
			} else if(role.equals(IRtcBuilderConstantsROS.SPEC_TOPIC_PUBLISH)) {
				this.varCallbackName = this.name + "_publisher";
			}
		}
	}
}
