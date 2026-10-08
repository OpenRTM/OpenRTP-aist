package jp.go.aist.rtm.rtcbuilder.ros.param;

import java.io.Serializable;

import jp.go.aist.rtm.rtcbuilder.generator.param.AbstractRecordedParam;

public class PackageParam extends AbstractRecordedParam implements Serializable {
	private static final long serialVersionUID = -2156457756959722868L;
	
	private String name;
	private String version;
	private String other;
	
	public PackageParam() {
		this.name = "";
		this.version = "";
		this.other = "";
		setUpdated(true);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		checkUpdated(this.name, name);
		this.name = name;
	}

	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		checkUpdated(this.version, version);
		this.version = version;
	}
	
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		checkUpdated(this.other, other);
		this.other = other;
	}
}
