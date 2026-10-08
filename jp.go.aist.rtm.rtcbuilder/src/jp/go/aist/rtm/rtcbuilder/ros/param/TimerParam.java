package jp.go.aist.rtm.rtcbuilder.ros.param;

import java.io.Serializable;

import jp.go.aist.rtm.rtcbuilder.generator.param.AbstractRecordedParam;
import jp.go.aist.rtm.rtcbuilder.nl.Messages;
import jp.go.aist.rtm.rtcbuilder.util.StringUtil;

public class TimerParam extends AbstractRecordedParam implements Serializable {
	private static final long serialVersionUID = -5584413630295784662L;
	
	private final String DEFAULT_NAME = "main_timer";
	private final Double DEFAULT_RATE = 1.0;
	private final String DEFAULT_CALLBACK = "timer_callback";

	private String name;
	private Double rate;
	private String callBack;
	private String description;
	
	public TimerParam() {
		this.name = this.DEFAULT_NAME;
		this.rate = this.DEFAULT_RATE;
		this.callBack = this.DEFAULT_CALLBACK;
		this.description = "";
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		checkUpdated(this.name, name);
		this.name = name;
	}

	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		checkUpdated(this.rate, rate);
		this.rate = rate;
	}

	public String getCallBack() {
		return callBack;
	}
	public void setCallBack(String callBack) {
		checkUpdated(this.callBack, callBack);
		this.callBack = callBack;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		checkUpdated(this.description, description);
		this.description = description;
	}
	/////
	public String validateInfo() {
		if(this.name == null || this.name.length() == 0) {
			return Messages.getString("IMC.VALIDATE_LICYCLE_TIMER_NAME1");
		}
//		if( !StringUtil.checkDigitSmallAlphabetUS(this.name) ) {
//			return Messages.getString("IMC.VALIDATE_LICYCLE_TIMER_NAME2");
//		}
//		if( !StringUtil.checkSmallAlphabetFirst(this.name) ) {
//			return Messages.getString("IMC.VALIDATE_LICYCLE_TIMER_NAME3");
//		}
		
		if(this.callBack == null || this.callBack.length() == 0) {
			return Messages.getString("IMC.VALIDATE_LICYCLE_CALLBACK1");
		}
//		if( !StringUtil.checkDigitSmallAlphabetUS(this.callBack) ) {
//			return Messages.getString("IMC.VALIDATE_LICYCLE_CALLBACK2");
//		}
//		if( !StringUtil.checkSmallAlphabetFirst(this.callBack) ) {
//			return Messages.getString("IMC.VALIDATE_LICYCLE_CALLBACK3");
//		}
		if( !StringUtil.checkValidIdentifier(this.callBack) ) {
			return Messages.getString("IMC.VALIDATE_LICYCLE_CALLBACK4");
		}
		
		if(this.rate < 0.0) {
			return Messages.getString("IMC.VALIDATE_LICYCLE_RATE");
		}

		return null;
	}
}
