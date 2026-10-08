package jp.go.aist.rtm.rtcbuilder;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import jp.go.aist.rtm.rtcbuilder.generator.param.AbstractRecordedParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.RecordedList;

public class ParamBase extends AbstractRecordedParam {
	protected RecordedList<String> langList = new RecordedList<String>();
	protected RecordedList<String> langArgList = new RecordedList<String>();

	public String getLanguage() {
		return getLangageListString(langList);
	}
	public String getLanguageArg() {
		return getLangageListArgString();
	}
	public List<String> getLangList() {
		return langList;
	}
	public List<String> getLangArgList() {
		return langArgList;
	}

	public void setLanguage(String lang) {
		if (lang != null) {
			getLangList().clear();
			getLangList().addAll(Arrays.asList(lang.split(",")));
		}
	}
	public void setLanguageArg(String lang) {
		if (lang != null) {
			getLangArgList().clear();
			getLangArgList().addAll(Arrays.asList(lang.split(",")));
		}
	}

	public boolean isLanguageExist(String language) {
		boolean result = false;
		for (String str : getLangList()) {
			if (language.equalsIgnoreCase(str)) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	public String getLangageListArgString() {
		StringBuffer result = new StringBuffer();
		for (Iterator iter = langArgList.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			if ("".equals(result.toString()) == false) {
				result.append(",");
			}
			result.append(element);
		}
		return result.toString();
	}

	public static String getLangageListString(List langList) {
		StringBuffer result = new StringBuffer();
		for (Iterator iter = langList.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			if ("".equals(result.toString()) == false) {
				result.append(",");
			}
			result.append(element);
		}
		return result.toString();
	}

}
