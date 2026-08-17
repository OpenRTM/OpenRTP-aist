package jp.go.aist.rtm.toolscommon.extension;

import static jp.go.aist.rtm.toolscommon.model.component.Component.COMPOSITETYPE_GROUPING;
import static jp.go.aist.rtm.toolscommon.model.component.Component.COMPOSITETYPE_NONE;
import static jp.go.aist.rtm.toolscommon.model.component.ExecutionContext.KIND_PERIODIC;
import static jp.go.aist.rtm.toolscommon.model.component.ExecutionContext.KIND_EVENT_DRIVEN;
import static jp.go.aist.rtm.toolscommon.model.component.ExecutionContext.KIND_OTHER;
import static jp.go.aist.rtm.toolscommon.model.component.ExecutionContext.KIND_UNKNOWN;

import java.util.ArrayList;
import java.util.List;

import org.openrtp.namespaces.rts.version02.ConfigurationData;
import org.openrtp.namespaces.rts.version02.ConfigurationSet;
import org.openrtp.namespaces.rts.version02.Dataport;
import org.openrtp.namespaces.rts.version02.DataportConnector;
import org.openrtp.namespaces.rts.version02.DataportExt;
import org.openrtp.namespaces.rts.version02.ExecutionContext;
import org.openrtp.namespaces.rts.version02.ExecutionContextExt;
import org.openrtp.namespaces.rts.version02.Property;
import org.openrtp.namespaces.rts.version02.Serviceport;
import org.openrtp.namespaces.rts.version02.ServiceportExt;
import org.openrtp.namespaces.rts.version02.TargetPort;

import jp.go.aist.rtm.toolscommon.model.component.Component;
import jp.go.aist.rtm.toolscommon.model.component.ComponentFactory;
import jp.go.aist.rtm.toolscommon.model.component.Port;
import jp.go.aist.rtm.toolscommon.model.component.SystemDiagram;
import jp.go.aist.rtm.toolscommon.model.component.SystemDiagramKind;
import jp.go.aist.rtm.toolscommon.profiles.util.IDUtil;

public abstract class LoadComponentExtension {

	protected SystemDiagram diagram;
	protected SystemDiagramKind kind;
	protected String baseFile;

	protected org.openrtp.namespaces.rts.version02.Component target;
	protected org.openrtp.namespaces.rts.version02.RtsProfile profile;

	public abstract boolean canCreate();

	public abstract Component create();

	public void setDiagram(SystemDiagram diagram) {
		this.diagram = diagram;
	}

	public void setKind(SystemDiagramKind kind) {
		this.kind = kind;
	}

	public void setTarget(org.openrtp.namespaces.rts.version02.Component target) {
		this.target = target;
	}

	public void setProfile(
			org.openrtp.namespaces.rts.version02.RtsProfile profile) {
		this.profile = profile;
	}

	public void setBasePath(String baseFile) {
		this.baseFile = baseFile;
	}

	public boolean isOnline() {
		return (kind == SystemDiagramKind.ONLINE_LITERAL);
	}

	public Component createDefaultAsOnline() {
		if (!COMPOSITETYPE_GROUPING.equals(target.getCompositeType())) {
			return ComponentFactory.eINSTANCE.createCorbaComponent();
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

	public Component createDefaultAsOffline() {
		Component spec = ComponentFactory.eINSTANCE
				.createComponentSpecification();
		IDUtil.RTCId id = IDUtil.parseRTCId(target.getId());
		if (id != null) {
			spec.setVenderL(id.vendor);
			spec.setCategoryL(id.category);
			spec.setTypeNameL(id.name);
			spec.setVersionL(id.version);
		}
		spec.setComponentId(target.getId());
		spec.setInstanceNameL(target.getInstanceName());

		if (!COMPOSITETYPE_NONE.equals(target.getCompositeType())) {
			return spec;
		}

		// コンフィグレーション
		jp.go.aist.rtm.toolscommon.model.component.ConfigurationSet cs;
		for (ConfigurationSet c : target.getConfigurationSets()) {
			cs = ComponentFactory.eINSTANCE.createConfigurationSet();
			cs.setId(c.getId());
			jp.go.aist.rtm.toolscommon.model.component.NameValue nv;
			for (ConfigurationData d : c.getConfigurationData()) {
				nv = ComponentFactory.eINSTANCE.createNameValue();
				nv.setName(d.getName());
				nv.setValue(d.getData());
				cs.getConfigurationData().add(nv);
			}
			if (target.getActiveConfigurationSet().equals(c.getId())) {
				spec.setActiveConfigurationSet(cs);
			}
			spec.getConfigurationSets().add(cs);
		}
		// 実行コンテキスト
		jp.go.aist.rtm.toolscommon.model.component.ExecutionContext ec;
		for (ExecutionContext e : target.getExecutionContexts()) {
			ExecutionContextExt ecExt = (ExecutionContextExt) e;
			ec = ComponentFactory.eINSTANCE.createExecutionContext();
			if ("PERIODIC".equals(ecExt.getKind())) {
				ec.setKindL(KIND_PERIODIC);
			} else if ("EVENT_DRIVEN".equals(ecExt.getKind())) {
				ec.setKindL(KIND_EVENT_DRIVEN);
			} else if ("OTHER".equals(ecExt.getKind())) {
				ec.setKindL(KIND_OTHER);
			} else {
				ec.setKindL(KIND_UNKNOWN);
			}
			ec.setRateL(ecExt.getRate());
			for (Property prop : ecExt.getProperties()) {
				ec.setProperty(prop.getName(), prop.getValue());
			}
			ec.setOwner(spec);
			spec.getExecutionContextHandler().setContext(ecExt.getId(), ec);
		}
		// 入力ポート名抽出
		List<String> inports = new ArrayList<String>();
		for (DataportConnector conn : profile.getDataPortConnectors()) {
			TargetPort tp = conn.getTargetDataPort();
			if (spec.getComponentId().equals(tp.getComponentId())) {
				inports.add(tp.getPortName());
			}
		}
		// データポート
		Port port = null;
		for (Dataport dp : target.getDataPorts()) {
			DataportExt dpExt = (DataportExt) dp;
			// RTSプロファイルのみでは完全な入出力判定は不可
			if (inports.contains(dpExt.getName())) {
				port = ComponentFactory.eINSTANCE.createInPort();
			} else {
				port = ComponentFactory.eINSTANCE.createOutPort();
			}
			port.setSynchronizer(ComponentFactory.eINSTANCE
					.createPortSynchronizer());
			port.setNameL(dpExt.getName());
			for (Property p : dpExt.getProperties()) {
				port.getSynchronizer().setProperty(p.getName(), p.getValue());
			}
			spec.getPorts().add(port);
		}
		// サービスポート
		for (Serviceport sp : target.getServicePorts()) {
			ServiceportExt spExt = (ServiceportExt) sp;
			port = ComponentFactory.eINSTANCE.createServicePort();
			port.setSynchronizer(ComponentFactory.eINSTANCE
					.createPortSynchronizer());
			port.setNameL(spExt.getName());
			for (Property p : spExt.getProperties()) {
				port.getSynchronizer().setProperty(p.getName(), p.getValue());
			}
			spec.getPorts().add(port);
		}

		return spec;
	}

}
