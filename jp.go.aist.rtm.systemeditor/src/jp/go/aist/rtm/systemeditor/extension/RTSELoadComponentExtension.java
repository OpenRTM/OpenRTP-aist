package jp.go.aist.rtm.systemeditor.extension;

import static jp.go.aist.rtm.toolscommon.model.component.Component.COMPOSITETYPE_NONE;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import jp.go.aist.rtm.repositoryView.RepositoryAccessor;
import jp.go.aist.rtm.repositoryView.model.LocalRVRootItem;
import jp.go.aist.rtm.repositoryView.model.RepositoryViewFactory;
import jp.go.aist.rtm.repositoryView.model.RepositoryViewItem;
import jp.go.aist.rtm.repositoryView.model.RepositoryViewLeafItem;
import jp.go.aist.rtm.repositoryView.ui.views.RepositoryView;
import jp.go.aist.rtm.toolscommon.extension.LoadComponentExtension;
import jp.go.aist.rtm.toolscommon.model.component.Component;
import jp.go.aist.rtm.toolscommon.model.component.ComponentFactory;
import jp.go.aist.rtm.toolscommon.model.component.ComponentSpecification;
import jp.go.aist.rtm.toolscommon.profiles.util.IDUtil;
import jp.go.aist.rtm.toolscommon.util.RtcProfileHandler;

public class RTSELoadComponentExtension extends LoadComponentExtension {

	public RTSELoadComponentExtension() {
	}
	
	@Override
	public boolean canCreate() {
		return true;
	}

	@Override
	public Component create() {
		if (isOnline()) {
			return createDefaultAsOnline();
		} else {
			if (COMPOSITETYPE_NONE.equals(target.getCompositeType())) {
				String componentid = target.getId();
				String pathId = target.getPathUri();
				Component spec = RepositoryAccessor.eINSTANCE.findComponent(
						componentid, pathId);
				if (spec == null) {
					//リポジトリビューに登録されていない場合には，Profileを読み込み
//					throw new IllegalStateException("Target Component["
//							+ componentid + "](" + pathId
//							+ ") does not exist in RepositoryView.");
					ComponentSpecification module = null;
			    	RtcProfileHandler handler = new RtcProfileHandler();
			    	String targetFileName = null;
					try {
						Path path = Paths.get(this.baseFile);
				        Path directory = path.getParent();
				        
				    	URI uri = new URI(target.getPathUri());
				    	targetFileName = directory + File.separator + uri.getPath();
						module = handler.createComponent(targetFileName);
					} catch (URISyntaxException e) {
						throw new IllegalStateException("Target Component["
						+ componentid + "](" + pathId
						+ ") does not exist in RepositoryView.");
					} catch (Exception e) {
						throw new IllegalStateException("Target Component["
								+ componentid + "](" + pathId
								+ ") does not exist in RepositoryView.");
					}
					String fileName = targetFileName.substring(targetFileName.lastIndexOf(File.separator) + 1);
					module.setAliasName(module.getInstanceNameL() + "(" + fileName + ")"); //$NON-NLS-1$ //$NON-NLS-2$
					module.setPathId(target.getPathUri());
					//
					RepositoryView viewer = RepositoryAccessor.eINSTANCE.getView();
			    	RepositoryViewItem rootItem = new RepositoryViewItem("root", 0); //$NON-NLS-1$
			    	rootItem.setChildren((List<RepositoryViewItem>)viewer.getViewer().getInput());

			 		RepositoryViewItem itemFirst = rootItem.getChild(module.getPathId());
			 		if( itemFirst==null ) {
			    		itemFirst = new LocalRVRootItem(module.getPathId());
						rootItem.addChild(itemFirst);
			 		}
					RepositoryViewFactory.buildTree(itemFirst, module, RepositoryViewLeafItem.RTC_LEAF);
					viewer.getViewer().refresh();
					spec = RepositoryAccessor.eINSTANCE.findComponent(componentid, pathId);
				}
				return spec.copy();
			}
			Component spec = ComponentFactory.eINSTANCE
					.createComponentSpecification();
			IDUtil.RTCId id = IDUtil.parseRTCId(target.getId());
			if (id != null) {
				spec.setVenderL(id.vendor);
				spec.setCategoryL(id.category);
				spec.setTypeNameL(id.name);
				spec.setVersionL(id.version);
			}
			return spec;
		}
	}

}
