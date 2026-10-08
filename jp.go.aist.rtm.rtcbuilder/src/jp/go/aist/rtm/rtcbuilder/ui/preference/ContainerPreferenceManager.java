package jp.go.aist.rtm.rtcbuilder.ui.preference;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;

import jp.go.aist.rtm.rtcbuilder.RtcBuilderPlugin;

public class ContainerPreferenceManager {
	private static ContainerPreferenceManager __instance = new ContainerPreferenceManager();
	protected PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);

	/**
	 * コンストラクタ
	 * 
	 * @return シングルトン
	 */
	public static ContainerPreferenceManager getInstance() {
		return __instance;
	}

	/**
	 * コンテナ設定 キー
	 */
	public static final String Generate_Container_Settings = ContainerPreferenceManager.class.getName()
			+ "GENERATE_CONTAINER_SETTINGS";
	public static final String Generate_Container_Settings_ROS = ContainerPreferenceManager.class.getName()
			+ "GENERATE_CONTAINER_SETTINGS_ROS";

	public String getSettings() {
		RtcBuilderPlugin.getDefault().getPreferenceStore().setDefault(Generate_Container_Settings, "");
		
		String DEFAULT_SETTINGS = "";
		try {
			Bundle bundle = RtcBuilderPlugin.getDefault().getBundle();
			URL fileURL = FileLocator.find(bundle, new Path("conf/rtcbuilder_config.json"), null);
	        if (fileURL != null) {
	        	try (InputStream is = FileLocator.openStream(bundle, new Path("conf/rtcbuilder_config.json"), false)) {
		        	try (Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name())) {
		                scanner.useDelimiter("\\A");
		                DEFAULT_SETTINGS = scanner.hasNext() ? scanner.next() : "";
		        	}
	        	}
	        }
		} catch (Exception e) {
		}
		/////
		String resultTemp = RtcBuilderPlugin.getDefault().getPreferenceStore().getString(Generate_Container_Settings);
		String result;
		if (resultTemp.equals("") ) { // defaultvalue
			result = DEFAULT_SETTINGS;
		} else {
			result = resultTemp;
		}
		return result;
	}
	public void setSettings(String defaultSettings) {
		String oldSettings = getSettings();
		RtcBuilderPlugin.getDefault().getPreferenceStore().setValue(Generate_Container_Settings, defaultSettings);
		propertyChangeSupport.firePropertyChange(Generate_Container_Settings, oldSettings, defaultSettings);
	}

	public String getSettingsROS() {
		RtcBuilderPlugin.getDefault().getPreferenceStore().setDefault(Generate_Container_Settings_ROS, "");
		
		String DEFAULT_SETTINGS = "";
		try {
			Bundle bundle = RtcBuilderPlugin.getDefault().getBundle();
			URL fileURL = FileLocator.find(bundle, new Path("conf/rosbuilder_config.json"), null);
	        if (fileURL != null) {
	        	try (InputStream is = FileLocator.openStream(bundle, new Path("conf/rosbuilder_config.json"), false)) {
		        	try (Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name())) {
		                scanner.useDelimiter("\\A");
		                DEFAULT_SETTINGS = scanner.hasNext() ? scanner.next() : "";
		        	}
	        	}
	        }
		} catch (Exception e) {
		}
		/////
		String resultTemp = RtcBuilderPlugin.getDefault().getPreferenceStore().getString(Generate_Container_Settings_ROS);
		String result;
		if (resultTemp.equals("") ) { // defaultvalue
			result = DEFAULT_SETTINGS;
		} else {
			result = resultTemp;
		}
		return result;
	}
	public void setSettingsROS(String defaultSettings) {
		String oldSettings = getSettings();
		RtcBuilderPlugin.getDefault().getPreferenceStore().setValue(Generate_Container_Settings_ROS, defaultSettings);
		propertyChangeSupport.firePropertyChange(Generate_Container_Settings_ROS, oldSettings, defaultSettings);
	}

	/**
	 * @see PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	/**
	 * @see PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
}
