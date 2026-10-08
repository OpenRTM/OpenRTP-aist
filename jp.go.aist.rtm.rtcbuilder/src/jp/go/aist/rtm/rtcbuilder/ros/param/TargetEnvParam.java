package jp.go.aist.rtm.rtcbuilder.ros.param;

import java.io.Serializable;

import jp.go.aist.rtm.rtcbuilder.generator.param.AbstractRecordedParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.RecordedList;

public class TargetEnvParam extends AbstractRecordedParam implements Serializable {
	private static final long serialVersionUID = 3332439570605262921L;
	
	private String rosVersion;
	private RecordedList<PackageParam> libraries = new RecordedList<PackageParam>();

	public TargetEnvParam() {
		this.rosVersion = "";
		this.libraries.clear();
	}

	public String getRosVersion() {
		return rosVersion;
	}
	public void setRosVersion(String rosVersion) {
		checkUpdated(this.rosVersion, rosVersion);
		this.rosVersion = rosVersion;
	}

	public RecordedList<PackageParam> getLibraries() {
		return libraries;
	}
	@Override
	public boolean isUpdated() {
		if (super.isUpdated()) {
			return true;
		}
		if (this.libraries.isUpdated()) {
			return true;
		}
		return false;
	}

	@Override
	public void resetUpdated() {
		super.resetUpdated();
		//
		this.libraries.resetUpdated();
	}
}
