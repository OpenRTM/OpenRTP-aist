package jp.go.aist.rtm.rtcbuilder.ui.Perspective;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Platform;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.ParamBase;
import jp.go.aist.rtm.rtcbuilder.RtcBuilderPlugin;
import jp.go.aist.rtm.rtcbuilder.manager.GenerateManager;

public abstract class LanguageProperty {
	public abstract String getPerspectiveId();
	public abstract String getPerspectiveName();
	public abstract String getPluginId();
	public abstract List<String> getNatures();
	
	public static LanguageProperty getLanguageProperty(ParamBase rtcParam) {
		LanguageProperty langProp = null;
		if(rtcParam.isLanguageExist(IRtcBuilderConstants.LANG_CPP) ||
				rtcParam.isLanguageExist(IRtcBuilderConstants.LANG_CPPWIN)) {
			langProp = new CppProperty();
		}
		if(langProp==null) {
			List<GenerateManager> managerList = RtcBuilderPlugin.getDefault().getLoader().getManagerList(IRtcBuilderConstants.MIDDLEWARE_RTM);
			if( managerList != null ) {
				for( Iterator<GenerateManager> iter = managerList.iterator(); iter.hasNext(); ) {
					GenerateManager manager = iter.next();
					langProp = manager.getLanguageProperty(rtcParam);
					if(langProp!=null) break;
				}
			}
		}
		return langProp;
	}
	
	public static LanguageProperty checkPlugin(ParamBase rtcParam) {
		LanguageProperty langProp = getLanguageProperty(rtcParam);
		//Pluginの存在確認
		if( langProp != null ) {
			String[] plugins = Platform.getExtensionRegistry().getNamespaces();
			List<String> pluginMap = Arrays.asList(plugins);
			for(String target : pluginMap) {
				if(target.startsWith(langProp.getPluginId())) {
					return langProp;
				}
			}
		}
		return null;
	}
}
