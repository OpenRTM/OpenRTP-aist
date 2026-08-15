package jp.go.aist.rtm.toolscommon.factory;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;

import jp.go.aist.rtm.toolscommon.ToolsCommonPlugin;
import jp.go.aist.rtm.toolscommon.extension.LoadComponentExtension;
import jp.go.aist.rtm.toolscommon.model.component.Component;
import jp.go.aist.rtm.toolscommon.model.component.SystemDiagram;
import jp.go.aist.rtm.toolscommon.model.component.SystemDiagramKind;

public class ComponentLoader {

	static final String EXTENTION_POINT_NAME = "loadcomponent";
	static List<LoadComponentExtension> loaders;

	SystemDiagram diagram;
	SystemDiagramKind kind;
	private String baseFile;

	public ComponentLoader() {
	}

	public void setDiagram(SystemDiagram diagram) {
		this.diagram = diagram;
	}

	public void setKind(SystemDiagramKind kind) {
		this.kind = kind;
	}
	public SystemDiagramKind getKind() {
		return this.kind;
	}

	public void setBasePath(String baseFile) {
		this.baseFile = baseFile;
	}

	/**
	 * RTSプロファイルのコンポーネント定義より、EMFコンポーネントを生成します。
	 * 
	 * @param target
	 *            生成対象のコンポーネント定義
	 * @param profile
	 *            生成対象のプロファイル
	 * @return コンポーネント定義から生成された EMFコンポーネント
	 */
	public Component create(
			org.openrtp.namespaces.rts.version02.Component target,
			org.openrtp.namespaces.rts.version02.RtsProfile profile) {
		if (loaders == null) {
			buildLoader();
		}
		LoadComponentExtension loader = null;
		for (LoadComponentExtension ext : loaders) {
			ext.setKind(kind);
			ext.setDiagram(diagram);
			ext.setTarget(target);
			ext.setProfile(profile);
			ext.setBasePath(baseFile);
			if (!ext.canCreate()) {
				continue;
			}
			loader = ext;
			break;
		}
		return loader.create();
	}

	static void buildLoader() {
		loaders = new ArrayList<LoadComponentExtension>();
		//
		String ns = ToolsCommonPlugin.class.getPackage().getName();
		if (Platform.getExtensionRegistry() != null) {
			IExtension[] extensions = Platform.getExtensionRegistry()
					.getExtensionPoint(ns, EXTENTION_POINT_NAME)
					.getExtensions();
			for (IExtension ex : extensions) {
				for (IConfigurationElement ce : ex.getConfigurationElements()) {
					Object obj;
					try {
						obj = ce.createExecutableExtension("extensionclass");
						if (obj instanceof LoadComponentExtension) {
							loaders.add((LoadComponentExtension) obj);
						}
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		if (!loaders.isEmpty()) {
			return;
		}
		// デフォルトのファクトリ
		LoadComponentExtension ext = createDefaultExtension();
		loaders.add(ext);
	}

	public static List<LoadComponentExtension> getExtensions() {
		return loaders;
	}

	public static LoadComponentExtension createDefaultExtension() {
		return new LoadComponentExtension() {
			@Override
			public boolean canCreate() {
				return true;
			}

			@Override
			public Component create() {
				if (isOnline()) {
					return createDefaultAsOnline();
				} else {
					return createDefaultAsOffline();
				}
			}
		};
	}

}
