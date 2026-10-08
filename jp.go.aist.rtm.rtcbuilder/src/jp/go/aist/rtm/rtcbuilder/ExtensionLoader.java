package jp.go.aist.rtm.rtcbuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.go.aist.rtm.rtcbuilder.manager.GenerateManager;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtensionLoader {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ExtensionLoader.class);

	// 拡張ポイントID
	public static final String EXTENSION_POINT_ID = "jp.go.aist.rtm.rtcbuilder.generateManager";
	// Manager
	Map<String, List<GenerateManager>> managerList = new HashMap<String, List<GenerateManager>>(); 
	// ManagerKey
	Map<String, List<String>> managerKeyList = new HashMap<String, List<String>>();

	List<GenerateManager> allManager = new ArrayList<GenerateManager>();

	/**
	 * 拡張オブジェクトをロードしてリストに格納する。
	 * @throws CoreException 
	 */
	public void loadExtensions() throws CoreException {
		allManager.clear();
		// 拡張ポイントの取得
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint( EXTENSION_POINT_ID );		
		if( point == null ) return;		
		
		// 拡張宣言のロード
		IExtension[] extensions = point.getExtensions();
		for( int index = 0; index < extensions.length; index++ ) {
			// 拡張宣言（extensionタグ）ごとに、下位のタグを処理する
			IConfigurationElement[] cfgElems = extensions[index].getConfigurationElements();
			for(int intext = 0; intext < cfgElems.length; intext++) {
				IConfigurationElement cfgElem = cfgElems[intext];
				
				if ( "manager".equals( cfgElem.getName() ) ) {
					processManager( cfgElem );
				}
			}
		}
		//
		if(0<allManager.size()) {
			for (String key : managerList.keySet()) {
				List<GenerateManager> managers = managerList.get(key);
				List<String> keys = managerKeyList.get(key);
				for(GenerateManager each : allManager) {
					managers.add(each);
					if (!keys.contains(each.getManagerKey())) {
						keys.add(each.getManagerKey());
					}
				}
			}
		}
	}

	protected void processManager(IConfigurationElement cfgElem)
			throws CoreException {
		try {
			if (cfgElem.isValid()) {
				Object obj = cfgElem.createExecutableExtension("managerclass");
				if (obj instanceof GenerateManager) {
					GenerateManager manager = (GenerateManager) obj;
					String targetMdl = manager.getTargetMiddleware(); 
					if(targetMdl.equals(IRtcBuilderConstants.MIDDLEWARE_ALL)) {
						allManager.add(manager);
					} else {
						List<GenerateManager> managers;
						List<String> keys;
						if(managerList.containsKey(targetMdl)) {
							managers = managerList.get(targetMdl);
							keys = managerKeyList.get(targetMdl);
						} else {
							managers = new ArrayList<GenerateManager>();
							managerList.put(targetMdl, managers);
							
							keys = new ArrayList<String>();
							managerKeyList.put(targetMdl, keys);
						}
						managers.add(manager);
						if (!keys.contains(manager.getManagerKey())) {
							keys.add(manager.getManagerKey());
						}
					}
				}
			}
		} catch (CoreException e) {
			LOGGER.error("Fail to create extension. ext=managerclass", e);
			throw e;
		}
	}

	public List<GenerateManager> getManagerList(String key) {
		return managerList.get(key);
	}

	public List<String> getManagerKeyList(String key) {
		return managerKeyList.get(key);
	}

}
