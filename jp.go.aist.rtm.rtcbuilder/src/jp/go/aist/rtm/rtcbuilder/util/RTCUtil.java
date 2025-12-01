package jp.go.aist.rtm.rtcbuilder.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.graphics.RGB;

import jp.go.aist.rtm.rtcbuilder.generator.param.DataTypeParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.RtcParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.IdlPathParam;

public class RTCUtil {

	public static String form(String fm, String[] args) {
		String result = fm;
		for (int index = 0; index < args.length; index++) {
			String p = "{" + index + "}";
			int j = result.indexOf(p);
			if (j == -1) {
				continue;
			}
			String head = result.substring(0, j);
			String tail = result.substring(j + p.length());
			result = head + args[index] + tail;
		}
		return result;
	}

	public static boolean isBlank(String s) {
		if (s == null || s.isEmpty()) {
			return true;
		}
		return false;
	}

	public static List<String> split(String list, String delimit) {
		List<String> result = new ArrayList<String>();
		for (String s : list.split(delimit)) {
			result.add(s);
		}
		return result;
	}

	public static String join(List<String> list, String delimit) {
		String result = "";
		for (String s : list) {
			if (!result.isEmpty()) {
				result += delimit;
			}
			result += s;
		}
		return result;
	}

	public static void getIDLPathes(RtcParam target) {
		target.getIdlSearchPathList().clear();
		List<String> added = new ArrayList<String>();

		String FS = System.getProperty("file.separator");

		String defaultPath = System.getenv("RTM_ROOT");
		if (defaultPath != null) {
			if(!defaultPath.endsWith(FS)) {
				defaultPath += FS;
			}
			String fullPath = defaultPath + "rtm" + FS + "idl";
			String dispPath = "<RTM_ROOT>" + FS + "rtm" + FS + "idl";
			target.getIdlSearchPathList().add(new IdlPathParam(fullPath, dispPath, true));
			added.add(defaultPath + "rtm" + FS + "idl");
		}
		//
		if(target!=null && target.getOutputProject()!=null && 0<target.getOutputProject().length()) {
			try {
				IWorkspaceRoot workspaceHandle = ResourcesPlugin.getWorkspace().getRoot();
				IProject project = workspaceHandle.getProject(target.getOutputProject());
				IFolder path = project.getFolder("idl");
				if(path!=null && path.exists()) {
					if(added.contains("idl")==false) {
						target.getIdlSearchPathList().add(new IdlPathParam("idl", "idl", false));
						added.add("idl");
					}
				}
			} catch (Exception ex) {
			}
		}
	}

	public static boolean checkDefault(String target, List<DataTypeParam> typeList) {
		for(DataTypeParam type : typeList) {
			if(type.isDefault()) {
				if(target.trim().equals(type.getFullPath().trim())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Component Colorのキー
	 */
	public static final String COLOR_COMPONENT = RTCUtil.class.getName()
			+ "COMPONENT_COLOR";
	/**
	 * DataPort Colorのキー
	 */
	public static final String COLOR_DATAPORT = RTCUtil.class.getName()
			+ "DATA_PORT_COLOR";

	public static final String COLOR_EVENTPORT = RTCUtil.class.getName()
			+ "EVENT_PORT_COLOR";
	/**
	 * ServicePort Colorのキー
	 */
	public static final String COLOR_SERVICEPORT = RTCUtil.class.getName()
			+ "SERVICE_PORT_COLOR";

	/**
	 * Service I/F Colorのキー
	 */
	public static final String COLOR_SERVICEIF = RTCUtil.class.getName()
			+ "SERVICE_IF_COLOR";
	/**
	 * デフォルトの色を管理するマップ
	 */
	public static final Map<String, RGB> defaultRGBMap = new HashMap<String, RGB>();
	static {
		defaultRGBMap.put(COLOR_COMPONENT, new RGB(136, 190, 240));
		defaultRGBMap.put(COLOR_DATAPORT, new RGB(55, 97, 217));
		defaultRGBMap.put(COLOR_EVENTPORT, new RGB(255, 153, 0));
		defaultRGBMap.put(COLOR_SERVICEPORT, new RGB(192, 192, 192));
		defaultRGBMap.put(COLOR_SERVICEIF, new RGB(101, 136, 22));
	}

}
