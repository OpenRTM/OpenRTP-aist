package jp.go.aist.rtm.rtcbuilder.container.param;

import jp.go.aist.rtm.rtcbuilder.generator.param.AbstractRecordedParam;

public class LibraryParam extends AbstractRecordedParam {
	private String name;
	private String installer;
	private boolean canUpdate;
	
	public LibraryParam() {
		this.name = "";
		this.installer = "apt";
		this.canUpdate = false;
		setUpdated(true);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		checkUpdated(this.name, name);
		this.name = name;
	}
	
	public String getInstaller() {
		return installer;
	}
	public void setInstaller(String installer) {
		checkUpdated(this.installer, installer);
		this.installer = installer;
	}
	
	public boolean canUpdate() {
		return canUpdate;
	}
	public void setCanUpdate(boolean canUpdate) {
		this.canUpdate = canUpdate;
	}

}
