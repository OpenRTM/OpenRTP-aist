package jp.go.aist.rtm.rtcbuilder.container.param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.go.aist.rtm.rtcbuilder.container.param.setting.ContainerConfig;
import jp.go.aist.rtm.rtcbuilder.container.param.setting.MappingDb;
import jp.go.aist.rtm.rtcbuilder.container.param.setting.MiddlewareDetail;
import jp.go.aist.rtm.rtcbuilder.container.param.setting.PackageProvider;
import jp.go.aist.rtm.rtcbuilder.generator.param.AbstractRecordedParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.RecordedList;

public class ContainerParam extends AbstractRecordedParam {
	private String middleware;
	private String mdlVersion;
	private String osVersion;
	private String workspace;
	private String language;
	private String configuration;
	private RecordedList<LibraryParam> libraries;
	private RecordedList<String> preSets;
	private RecordedList<RepositoryParam> repositories;
	
	private List<String> defaultLibs = new ArrayList<String>();
	private List<String> aptList = new ArrayList<String>();
	private List<String> pipList = new ArrayList<String>();
	
	public ContainerParam() {
		this.middleware = "";
		this.mdlVersion = "";
		this.osVersion = "";
		this.workspace = "";
		this.language = "";
		this.configuration = "";
		this.libraries = new RecordedList<LibraryParam>();
		this.preSets = new RecordedList<String>();
		this.repositories = new RecordedList<RepositoryParam>();
	}

	public String getMiddleware() {
		return middleware;
	}
	public void setMiddleware(String middleware) {
		checkUpdated(this.middleware, middleware);
		this.middleware = middleware;
	}

	public String getMdlVersion() {
		return mdlVersion;
	}
	public void setMdlVersion(String mdlVersion) {
		checkUpdated(this.mdlVersion, mdlVersion);
		this.mdlVersion = mdlVersion;
	}

	public String getOsVersion() {
		return osVersion;
	}
	public void setOsVersion(String osVersion) {
		checkUpdated(this.osVersion, osVersion);
		this.osVersion = osVersion;
	}

	public String getWorkspace() {
		return workspace;
	}
	public void setWorkspace(String workspace) {
		checkUpdated(this.workspace, workspace);
		this.workspace = workspace;
	}

	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		checkUpdated(this.language, language);
		this.language = language;
	}

	public String getConfiguration() {
		return configuration;
	}
	public void setConfiguration(String configuration) {
		checkUpdated(this.configuration, configuration);
		this.configuration = configuration;
	}

	public List<LibraryParam> getLibraries() {
		return libraries;
	}

	public List<String> getPreSets() {
		return preSets;
	}

	public List<RepositoryParam> getRepositories() {
		return repositories;
	}
	
	public List<String> getDefaultLibs() {
		return defaultLibs;
	}
	public List<String> getAptList() {
		return aptList;
	}
	public List<String> getPipList() {
		return pipList;
	}
	
	public void updateDefaultLibs(ContainerConfig containerConfig) {
		defaultLibs.clear();

		Map<String, MiddlewareDetail> middlewares = containerConfig.getMiddlewares();
		if(middlewares.keySet().contains(getMiddlewareName()) == false) return;
		
		MiddlewareDetail detail = middlewares.get(getMiddlewareName());
		for(String each : detail.defaultLibs) {
			defaultLibs.add(each);
		}
	}
	
	public void prepareLibraries(ContainerConfig containerConfig) {
		MappingDb mdb = containerConfig.mappingDb;
		Map<String, Map<String, PackageProvider>> libraries = mdb.libraries;
		this.aptList.clear();
		this.pipList.clear();
		
		for(String lib : this.defaultLibs) {
			if(libraries.keySet().contains(lib) == false) {
				this.aptList.add(lib);
				continue;
			}
			Map<String, PackageProvider> libDef = libraries.get(lib);
			parseLibs(libDef, "apt");
		}
		
		for(LibraryParam libParam : this.libraries) {
			String libName = libParam.getName();
			String installer = libParam.getInstaller();
			if(libraries.keySet().contains(libName.toLowerCase()) == false) {
				if(installer.equals("apt")) {
					this.aptList.add(libName);	
				} else if(installer.equals("pip")) {
					this.pipList.add(libName);	
				}
				continue;
			}
			//
			Map<String, PackageProvider> libDef = libraries.get(libName.toLowerCase());
			parseLibs(libDef, installer);
		}
	}

	private void parseLibs(Map<String, PackageProvider> libDef, String installer) {
		if(libDef.keySet().contains(getMiddlewareName().toLowerCase())) {
			PackageProvider details = libDef.get(getMiddlewareName().toLowerCase());
			setLibList(installer, details);

		} else if(libDef.keySet().contains(getOSVersion())) {
			PackageProvider details = libDef.get(getOSVersion());
			setLibList(installer, details);

		} else if(libDef.keySet().contains(this.language.toLowerCase())) {
			PackageProvider details = libDef.get(this.language.toLowerCase());
			setLibList(installer, details);

		} else if(libDef.keySet().contains("default")) {
			PackageProvider details = libDef.get("default");
			setLibList(installer, details);
		}
	}

	private void setLibList(String installer, PackageProvider details) {
		if(installer.equals("apt")) {
			if(details.apt != null && 0 < details.apt.length()) {
				String[] libs = details.apt.split(" ");
				for(String each : libs) {
					if(each.contains("${ROS_DISTRO}")) {
						String middleware = this.mdlVersion.toLowerCase();
						each = each.replace("${ROS_DISTRO}", middleware);
					}
					if(this.aptList.contains(each.trim()) == false) {
						this.aptList.add(each.trim());
					}
				}
			}
		} else if(installer.equals("pip")) {
			if(details.pip != null && 0 < details.pip.length()) {
				String[] libs = details.apt.split(" ");
				for(String each : libs) {
					if(each.contains("${ROS_DISTRO}")) {
						String middleware = this.mdlVersion.toLowerCase();
						each = each.replace("${ROS_DISTRO}", middleware);
					}
					if(this.pipList.contains(each.trim()) == false) {
						this.pipList.add(each.trim());
					}
				}
			}
		}
	}
	
	private String getMiddlewareName() {
		if(this.middleware == null || this.middleware.length() == 0) return "";

		return this.middleware.replace(" ", "");
	}
	
	private String getOSVersion() {
		if(this.osVersion == null || this.osVersion.length() == 0) return "";

		String[] elems = this.osVersion.split(" ");
		if(elems.length == 0) return "";
		return elems[0].toLowerCase();
	}
	
	@Override
	public boolean isUpdated() {
		if (super.isUpdated()) {
			return true;
		}
		if (libraries.isUpdated()) {
			return true;
		}
		if (preSets.isUpdated()) {
			return true;
		}
		if (repositories.isUpdated()) {
			return true;
		}
		return false;
	}

	@Override
	public void resetUpdated() {
		super.resetUpdated();
		libraries.resetUpdated();
		preSets.resetUpdated();
		repositories.resetUpdated();
	}
}
