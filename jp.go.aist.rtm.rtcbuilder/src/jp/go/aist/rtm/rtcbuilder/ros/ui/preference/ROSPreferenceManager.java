package jp.go.aist.rtm.rtcbuilder.ros.ui.preference;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import jp.go.aist.rtm.rtcbuilder.RtcBuilderPlugin;
import jp.go.aist.rtm.rtcbuilder.ros.IRtcBuilderConstantsROS;

public class ROSPreferenceManager {
	private static ROSPreferenceManager __instance = new ROSPreferenceManager();
	private static final String Separator = ":";

	public static ROSPreferenceManager getInstance() {
		return __instance;
	}

	public static final String Mantainer_Name = getClassName() + "MAINTAINER_NAME";
	public static final String Maintainer_Address = getClassName() + "MAINTAINER_ADDRESS";

	static final String Document_Value_Key = ROSPreferenceManager.class.getName() + "DOCUMENT_VALUE_KEY";
	
	public static final String DEFAULT_MAINTAINER_NAME = "Maintainer";
	public static final String DEFAULT_MAINTAINER_ADDRESS = "todo@example.com";

	public static ArrayList<String> defaultDocumentValue = new ArrayList<String>();
	protected PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public static String getMaintainerNameValue() {
		if(RtcBuilderPlugin.getDefault() == null) {
			return "";
		}
		RtcBuilderPlugin.getDefault().getPreferenceStore().setDefault(Mantainer_Name, DEFAULT_MAINTAINER_NAME);
		String resultTemp = RtcBuilderPlugin.getDefault().getPreferenceStore().getString(Mantainer_Name);
		String result;
		if (resultTemp.equals("")) { // defaultvalue
			result = "";
		} else {
			result = resultTemp;
		}
		return result;
	}
	public void setMaintainerNameValue(String defaultNameValue) {
		String oldCreatorValue = getMaintainerNameValue();
		RtcBuilderPlugin.getDefault().getPreferenceStore().setValue(Mantainer_Name, defaultNameValue);
		propertyChangeSupport.firePropertyChange(Mantainer_Name, oldCreatorValue, defaultNameValue);
	}

	public static String getMaintainerAddressValue() {
		if(RtcBuilderPlugin.getDefault() == null) {
			return "";
		}
		RtcBuilderPlugin.getDefault().getPreferenceStore().setDefault(Maintainer_Address, DEFAULT_MAINTAINER_ADDRESS);
		String resultTemp = RtcBuilderPlugin.getDefault().getPreferenceStore().getString(Maintainer_Address);
		String result;
		if (resultTemp.equals("")) { // defaultvalue
			result = "";
		} else {
			result = resultTemp;
		}
		return result;
	}
	public void setMaintainerAddressValue(String defaultNameValue) {
		String oldCreatorValue = getMaintainerAddressValue();
		RtcBuilderPlugin.getDefault().getPreferenceStore().setValue(Maintainer_Address, defaultNameValue);
		propertyChangeSupport.firePropertyChange(Maintainer_Address, oldCreatorValue, defaultNameValue);
	}

	public static ArrayList<String> getDefaultDocumentValue() {
		defaultDocumentValue = new ArrayList<String>();
		defaultDocumentValue.add("false");
		defaultDocumentValue.add("false");
		defaultDocumentValue.add("false");
		return defaultDocumentValue;
	}
	public static ArrayList<String> getDocumentValue() {
		ArrayList<String> result = new ArrayList<String>();
		if(RtcBuilderPlugin.getDefault() == null) {
			return result;
		}
		RtcBuilderPlugin.getDefault().getPreferenceStore().setDefault(Document_Value_Key, "");

		String resultTemp = RtcBuilderPlugin.getDefault().getPreferenceStore().getString(Document_Value_Key);
		if (resultTemp.equals("")) { // defaultvalue
			result = getDefaultDocumentValue();
		} else {
			result = convertString2ArrayList(resultTemp);
		}
		return result;
	}
	public void setDocumentValue(ArrayList<String> arrayDocument) {
		ArrayList<String> oldArrayDocument = getDefaultDocumentValue();
		RtcBuilderPlugin.getDefault().getPreferenceStore().setValue(Document_Value_Key, convertArrayList2String(arrayDocument));
		propertyChangeSupport.firePropertyChange(Document_Value_Key,oldArrayDocument,arrayDocument);
	}
	/////
	public static String convertArrayList2String(ArrayList<String> source) {
		StringBuffer resultTemp = new StringBuffer();
		for (int intIdx = 0; intIdx < source.size(); intIdx++) {
			resultTemp.append(source.get(intIdx));
			resultTemp.append(Separator);
		}
		String result = resultTemp.toString();
		if (result.length() == 0) return "";
		return result.substring(0,result.length()-1);
	}

	public static ArrayList<String> convertString2ArrayList(String source) {
		String[] resultTemp = source.split(Separator);
		
		ArrayList<String> result = new ArrayList<String>();
		for(int intIdx=0; intIdx < IRtcBuilderConstantsROS.ACTIVITY_CAN_EDIT_NUM; intIdx++) {
			if(intIdx < resultTemp.length) {
				result.add(resultTemp[intIdx]);
			} else {
				result.add("false");
			}
		}
		return result;
	}

	private static String getClassName() {
		return ROSPreferenceManager.class.getName();
	}
}
