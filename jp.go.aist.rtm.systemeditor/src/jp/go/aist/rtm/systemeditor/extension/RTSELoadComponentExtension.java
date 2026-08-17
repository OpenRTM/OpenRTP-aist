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
					ComponentSpecification module = null;
			    	RtcProfileHandler handler = new RtcProfileHandler();
			    	String targetFileName = null;
					try {
						Path path = Paths.get(this.baseFile);
				        Path directory = path.getParent();
				        
				    	URI uri = new URI(target.getPathUri());
				    	targetFileName = directory + File.separator + uri.getPath();
						module = handler.createComponent(targetFileName);

						String fileName = targetFileName.substring(targetFileName.lastIndexOf(File.separator) + 1);
						module.setAliasName(module.getInstanceNameL() + "(" + fileName + ")"); //$NON-NLS-1$ //$NON-NLS-2$
						
						File rtcFile = new File(targetFileName);
						URI pathUri = new URI("file", "localhost", rtcFile.toURI().getPath(), null );
						pathId = pathUri.toString();
						module.setPathId(pathId);
					} catch (URISyntaxException e) {
						throw new IllegalStateException("Target Component["
						+ componentid + "](" + pathId
						+ ") does not exist in RepositoryView.");
					} catch (Exception e) {
						throw new IllegalStateException("Target Component["
								+ componentid + "](" + pathId
								+ ") does not exist in RepositoryView.");
					}
					//
					RepositoryView viewer = RepositoryAccessor.eINSTANCE.getView();
			    	RepositoryViewItem rootItem = new RepositoryViewItem("root", 0); //$NON-NLS-1$
			    	rootItem.setChildren((List<RepositoryViewItem>)viewer.getViewer().getInput());
			    	
					try {
						URI uri = new URI(module.getPathId());
				        String result = uri.getPath().substring(1);
				        File pathUri = new File(result);
				 		RepositoryViewItem itemFirst = rootItem.getChild(pathUri.getParent());
				 		if( itemFirst==null ) {
				    		itemFirst = new LocalRVRootItem(pathUri.getParent());
							rootItem.addChild(itemFirst);
				 		}
						RepositoryViewFactory.buildTree(itemFirst, module, RepositoryViewLeafItem.RTC_LEAF);
						viewer.getViewer().refresh();
						spec = RepositoryAccessor.eINSTANCE.findComponent(componentid, pathId);
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
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
