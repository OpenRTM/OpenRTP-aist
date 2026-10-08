package jp.go.aist.rtm.rtcbuilder.ros.python.ui.Perspective;

import java.util.ArrayList;
import java.util.List;

import jp.go.aist.rtm.rtcbuilder.ui.Perspective.LanguageProperty;

public class PythonProperty extends LanguageProperty {
	private String PerspectiveId = "org.python.pydev.ui.PythonPerspective";
	private String PerspectiveName = "Python";
	private String PluginId = "org.python.pydev";

	public String getPerspectiveId() {
		return PerspectiveId;
	}

	public String getPerspectiveName() {
		return PerspectiveName;
	}

	public String getPluginId() {
		return PluginId;
	}

	@Override
	public List<String> getNatures() {
		List<String> natures = new ArrayList<String>();
		natures.add("org.python.pydev.pythonNature");
		return natures;
	}
}
