package jp.go.aist.rtm.toolscommon.util;

import static jp.go.aist.rtm.toolscommon.model.component.Component.COMPOSITETYPE_GROUPING;
import static jp.go.aist.rtm.toolscommon.model.component.ExecutionContext.KIND_EVENT_DRIVEN;
import static jp.go.aist.rtm.toolscommon.model.component.ExecutionContext.KIND_OTHER;
import static jp.go.aist.rtm.toolscommon.model.component.ExecutionContext.KIND_PERIODIC;
import static jp.go.aist.rtm.toolscommon.model.component.ExecutionContext.KIND_UNKNOWN;
import static jp.go.aist.rtm.toolscommon.profiles.util.XmlHandler.createXMLGregorianCalendar;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.openrtp.namespaces.rts.version02.Activation;
import org.openrtp.namespaces.rts.version02.Condition;
import org.openrtp.namespaces.rts.version02.Deactivation;
import org.openrtp.namespaces.rts.version02.Finalize;
import org.openrtp.namespaces.rts.version02.Initialize;
import org.openrtp.namespaces.rts.version02.MessageSending;
import org.openrtp.namespaces.rts.version02.Resetting;
import org.openrtp.namespaces.rts.version02.Shutdown;
import org.openrtp.namespaces.rts.version02.Startup;
import org.openrtp.namespaces.rts.version02.TargetComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.go.aist.rtm.toolscommon.factory.ComponentLoader;
import jp.go.aist.rtm.toolscommon.model.component.Component;
import jp.go.aist.rtm.toolscommon.model.component.ComponentFactory;
import jp.go.aist.rtm.toolscommon.model.component.ComponentSpecification;
import jp.go.aist.rtm.toolscommon.model.component.ConfigurationSet;
import jp.go.aist.rtm.toolscommon.model.component.ConnectorProfile;
import jp.go.aist.rtm.toolscommon.model.component.ConnectorProfile.PROP;
import jp.go.aist.rtm.toolscommon.model.component.CorbaComponent;
import jp.go.aist.rtm.toolscommon.model.component.ExecutionContext;
import jp.go.aist.rtm.toolscommon.model.component.NameValue;
import jp.go.aist.rtm.toolscommon.model.component.Port;
import jp.go.aist.rtm.toolscommon.model.component.PortConnector;
import jp.go.aist.rtm.toolscommon.model.component.SystemDiagram;
import jp.go.aist.rtm.toolscommon.model.component.SystemDiagramKind;
import jp.go.aist.rtm.toolscommon.model.component.util.PortConnectorFactory;
import jp.go.aist.rtm.toolscommon.model.core.Point;
import jp.go.aist.rtm.toolscommon.model.core.Rectangle;
import jp.go.aist.rtm.toolscommon.profiles.util.XmlHandler;

/**
 * RTSプロファイルの入出力を司るクラス
 *
 */
public class RtsProfileHandler {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RtsProfileHandler.class);

	private boolean online;
	private org.openrtp.namespaces.rts.version02.RtsProfileExt originalProfile;
	private List<String> savedConnectors;
	private org.openrtp.namespaces.rts.version02.ObjectFactory factory;
	private SystemDiagram diagram;
	private ComponentLoader loader = new ComponentLoader();
	private String baseFile;

	static final String KEY_COMPONENT_PATH_ID = "COMPONENT_PATH_ID";
	static final String KEY_BEND_POINT = "BEND_POINT";
	static final String KEY_IOR = "IOR";

	public void setBasePath(String baseFile) {
		this.baseFile = baseFile;
	}
	/**
	 * RTSプロファイルをロードする
	 * @param targetFile	ロード対象のファイル
	 * @param kind			オンラインかオフラインかの種別
	 * @return				ロードしたシステムダイアグラム
	 * @throws Exception
	 */
	public SystemDiagram load(String targetFile, SystemDiagramKind kind)
			throws Exception {
		org.openrtp.namespaces.rts.version02.RtsProfileExt profile = load(targetFile);
		return load(profile, kind);
	}

	public org.openrtp.namespaces.rts.version02.RtsProfileExt load(String targetFile) throws Exception {
		LOGGER.debug("load: targetFile=<{}>", targetFile);
		XmlHandler handler = new XmlHandler();
		org.openrtp.namespaces.rts.version02.RtsProfileExt profile = handler.loadXmlRts(targetFile);
		return profile;
	}

	public SystemDiagram load(
			org.openrtp.namespaces.rts.version02.RtsProfileExt profile,
			SystemDiagramKind kind) throws Exception {
		diagram = ComponentFactory.eINSTANCE.createSystemDiagram();
		diagram.setProfile(profile);
		diagram.setKind(kind);
		setOnline(kind == SystemDiagramKind.ONLINE_LITERAL);
		loader.setKind(kind);
		loader.setDiagram(diagram);
		loader.setBasePath(baseFile);
		populate(diagram, profile);
		return diagram;
	}

	/**
	 * RTSプロファイルからダイアグラム内にあるコンポーネント間の接続を復元させる
	 * @param eDiagram
	 */
	public void restoreConnection(SystemDiagram eDiagram) {
		this.diagram = eDiagram;
		setOnline(eDiagram.getKind() == SystemDiagramKind.ONLINE_LITERAL);
		loader.setKind(eDiagram.getKind());
		loader.setDiagram(diagram);
		org.openrtp.namespaces.rts.version02.RtsProfileExt profile = eDiagram.getProfile();
		List<Component> eComps = eDiagram.getRegisteredComponents();
		populateDataConnector(eComps, profile.getDataPortConnectors());
		populateServiceConnector(eComps, profile.getServicePortConnectors());
	}

	/**
	 * RTSプロファイルからダイアグラム内にあるコンポーネントのコンフィグセットを復元させる
	 * @param eDiagram
	 */
	public void restoreConfigSet(SystemDiagram eDiagram) {
		for (Component eComp : eDiagram.getRegisteredComponents()) {
			org.openrtp.namespaces.rts.version02.Component component = findComponent(eComp, eDiagram.getProfile()
					.getComponents());
			populateConfigSets(eComp, component);
		}
	}

	public void restoreConfigSetbyIOR(SystemDiagram eDiagram) {
		for (Component eComp : eDiagram.getRegisteredComponents()) {
			org.openrtp.namespaces.rts.version02.Component component = findComponentByIOR(eComp, eDiagram
					.getProfile().getComponents());
			populateConfigSets(eComp, component);
		}
	}

	/**
	 * オフラインの複合コンポーネントのポートを復元させる
	 * @param eDiagram
	 */
	public void restoreCompositeComponentPort(SystemDiagram eDiagram) {
		List<org.openrtp.namespaces.rts.version02.Component> source = eDiagram.getProfile().getComponents();
		for (Component eComp : eDiagram.getRegisteredComponents()) {
			org.openrtp.namespaces.rts.version02.Component component = findComponent(eComp, source);
			populateCompositeComponentPort(eComp, component);
		}
	}

    /**
	 * ECを復元させる
	 *
	 * @param eDiagram
	 */
	public void restoreExecutionContext(SystemDiagram eDiagram) {
		List<org.openrtp.namespaces.rts.version02.Component> components = eDiagram.getProfile().getComponents();
		for (Component eComp : eDiagram.getRegisteredComponents()) {
			org.openrtp.namespaces.rts.version02.Component comp = findComponent(eComp, components);
			if (comp == null) {
				continue;
			}
			if (!online && eComp instanceof ComponentSpecification) {
				// オフラインの場合は owned ECを作成
				// 事前にリポジトリからコピーされたECはクリアし、RTSプロファイルのものを優先
				eComp.getExecutionContexts().clear();
				eComp.getExecutionContextHandler().clear();
				for (org.openrtp.namespaces.rts.version02.ExecutionContext ec : comp.getExecutionContexts()) {
					ExecutionContext eEc = ComponentFactory.eINSTANCE
							.createExecutionContext();
					if ("PERIODIC".equals(ec.getKind())) {
						eEc.setKindL(KIND_PERIODIC);
					} else if ("EVENT_DRIVEN".equals(ec.getKind())) {
						eEc.setKindL(KIND_EVENT_DRIVEN);
					} else if ("OTHER".equals(ec.getKind())) {
						eEc.setKindL(KIND_OTHER);
					} else {
						eEc.setKindL(KIND_UNKNOWN);
					}
					eEc.setRateR(ec.getRate());
					eEc.setOwner(eComp);

					if (ec instanceof org.openrtp.namespaces.rts.version02.ExecutionContextExt) {
						org.openrtp.namespaces.rts.version02.ExecutionContextExt ecExt = (org.openrtp.namespaces.rts.version02.ExecutionContextExt) ec;
						// ECのプロパティ設定
						for (org.openrtp.namespaces.rts.version02.Property prop : ecExt.getProperties()) {
							eEc.setProperty(prop.getName(), prop.getValue());
						}
					}

					eComp.getExecutionContexts().add(eEc);
				}
				eComp.getExecutionContextHandler().sync();
			} else {
				// オンラインの場合は rateを更新
				for (ExecutionContext eEc : eComp.getExecutionContexts()) {
					org.openrtp.namespaces.rts.version02.ExecutionContext ec = findExecutionContext(eEc, eComp, comp
							.getExecutionContexts());
					if (ec == null) {
						continue;
					}
					eEc.setRateR(ec.getRate());
				}
			}
			// ECのparticipantの設定
			for (ExecutionContext eEc : eComp.getExecutionContexts()) {
				org.openrtp.namespaces.rts.version02.ExecutionContext ec = findExecutionContext(eEc, eComp, comp
						.getExecutionContexts());
				if (ec == null) {
					continue;
				}
				for (org.openrtp.namespaces.rts.version02.TargetComponent tc : ec.getParticipants()) {
					Component eComp2 = findEMFComponentByTargetComponent(tc,
							eDiagram.getRegisteredComponents());
					if (eComp2 == null) {
						continue;
					}
					if (!eComp2.getParticipationContexts().contains(eEc)) {
						eEc.addComponentR(eComp2);
					}
				}
			}
		}
	}

	/**
	 * RTSプロファイルを保存する
	 *
	 * @param eDiagram
	 * @return
	 */
	public org.openrtp.namespaces.rts.version02.RtsProfileExt save(
			SystemDiagram eDiagram) {
		this.diagram = eDiagram;

		setOnline(eDiagram.getKind() == SystemDiagramKind.ONLINE_LITERAL);
		loader.setKind(eDiagram.getKind());
		originalProfile = eDiagram.getProfile();
		savedConnectors = new ArrayList<String>();

		factory = new org.openrtp.namespaces.rts.version02.ObjectFactory();
		org.openrtp.namespaces.rts.version02.RtsProfileExt profile = factory.createRtsProfileExt();
		profile.setId(eDiagram.getSystemId());
		if(eDiagram.getCreationDate() != null) {
			profile.setCreationDate(createXMLGregorianCalendar(eDiagram.getCreationDate()));
		}
		profile.setUpdateDate(createXMLGregorianCalendar(eDiagram.getUpdateDate()));
		profile.setVersion("0.2");

		populateComponents(eDiagram, profile);

		// プロパティ設定
		for (String key : eDiagram.getPropertyKeys()) {
			setProperty(key, eDiagram.getProperty(key), profile.getProperties());
		}

		populateFromProfileOnly(profile);
		return profile;
	}

	// オンラインエディタとオフラインエディタのどちらから呼び出されたかを設定する
	public void setOnline(boolean online) {
		this.online = online;
	}

	// Open時にRTSプロファイルをシステムダイアログに変換する
	public void populate(SystemDiagram eDiagram,
			org.openrtp.namespaces.rts.version02.RtsProfileExt profile) {
		eDiagram.setSystemId(profile.getId());
		if(profile.getCreationDate()!=null) {
			eDiagram.setCreationDate(profile.getCreationDate().toString());
		}
		if(profile.getUpdateDate()!=null) {
			eDiagram.setUpdateDate(profile.getUpdateDate().toString());
		}
		populate(eDiagram.getComponents(), profile);
		// ダイアグラムのプロパティ設定
		for (org.openrtp.namespaces.rts.version02.Property prop : profile.getProperties()) {
			eDiagram.setProperty(prop.getName(), prop.getValue());
		}
		// ポートはCORBAObjectまたはRTCProfileを正とするので、この段階ではまだ接続できない

		// ポート接続のベンドポイントは描画用の情報なのでダイアグラムへ格納し、モデルと分離しておく
		// データポート接続
		for (org.openrtp.namespaces.rts.version02.DataportConnector conn : profile
				.getDataPortConnectors()) {
			String connectorId = conn.getConnectorId();
			Map<Integer, Point> bp = getBendPoint(conn);
			eDiagram.getPortConnectorRoutingConstraint(connectorId).putAll(bp);
		}
		// サービスポート接続
		for (org.openrtp.namespaces.rts.version02.ServiceportConnector conn : profile
				.getServicePortConnectors()) {
			String connectorId = conn.getConnectorId();
			Map<Integer, Point> bp = getBendPoint(conn);
			eDiagram.getPortConnectorRoutingConstraint(connectorId).putAll(bp);
		}
	}

	// Save時にシステムダイアログ内に含まれるコンポーネントをRTSプロファイル内にセットする
	private void populateComponents(
			SystemDiagram eDiagram,
			org.openrtp.namespaces.rts.version02.RtsProfileExt rtsProfile) {
		List<org.openrtp.namespaces.rts.version02.Component> components = rtsProfile.getComponents();
		/////
		org.openrtp.namespaces.rts.version02.Startup startUp = factory.createStartup();
		org.openrtp.namespaces.rts.version02.Shutdown shutDown = factory.createShutdown();
		org.openrtp.namespaces.rts.version02.Activation activation = factory.createActivation();
		org.openrtp.namespaces.rts.version02.Deactivation deActivation = factory.createDeactivation();
		org.openrtp.namespaces.rts.version02.Resetting resetting = factory.createResetting();
		org.openrtp.namespaces.rts.version02.Initialize initialize = factory.createInitialize();
		org.openrtp.namespaces.rts.version02.Finalize finalize = factory.createFinalize();
		/////
		for (Component eComp : eDiagram.getRegisteredComponents()) {
			org.openrtp.namespaces.rts.version02.ComponentExt target = factory.createComponentExt();
			target.setId(eComp.getComponentId());
			target.setPathUri(eComp.getPathId());
			target.setInstanceName(eComp.getInstanceNameL());
			target.setCompositeType(eComp.getCompositeTypeL());
			target.setIsRequired(eComp.isRequired());
			/////
			if( eComp.getStartUp() !=null && 0 < eComp.getStartUp().length() ) {
				startUp.getTargets().add(createTargets(target, eComp.getStartUp()));
			}
			if( eComp.getShutDown() !=null && 0 < eComp.getShutDown().length() ) {
				shutDown.getTargets().add(createTargets(target, eComp.getShutDown()));
			}
			if( eComp.getActivation() != null && 0 < eComp.getActivation().length() ) {
				activation.getTargets().add(createTargets(target, eComp.getActivation()));
			}
			if( eComp.getDeActivation() != null && 0 < eComp.getDeActivation().length() ) {
				deActivation.getTargets().add(createTargets(target, eComp.getDeActivation()));
			}
			if( eComp.getResetting() != null && 0 < eComp.getResetting().length() ) {
				resetting.getTargets().add(createTargets(target, eComp.getResetting()));
			}
			if( eComp.getInitialize() != null && 0 < eComp.getInitialize().length() ) {
				initialize.getTargets().add(createTargets(target, eComp.getInitialize()));
			}
			if( eComp.getFinalize() != null && 0 < eComp.getFinalize().length() ) {
				finalize.getTargets().add(createTargets(target, eComp.getFinalize()));
			}
			/////
			org.openrtp.namespaces.rts.version02.Component original = findOriginalComponent(eComp);

			populateExecutionContext(eComp, target, original);
			populateComponentLocation(eComp, target);
			populateComponentProperty(eComp, target, original);
			populatePorts(eComp, target, original, rtsProfile);
			populateConfigurationSet(eComp, target);
			populateParticipants(eComp, target, original);

			populateFromProfileOnly(target, original);
			components.add(target);
		}
		//
		if( 0 < startUp.getTargets().size() ) {
			rtsProfile.setStartUp(startUp);
		}
		if( 0 < shutDown.getTargets().size()) {
			rtsProfile.setShutDown(shutDown);
		}
		if( 0 < activation.getTargets().size() ) {
			rtsProfile.setActivation(activation);
		}
		if( 0 < deActivation.getTargets().size() ) {
			rtsProfile.setDeactivation(deActivation);
		}
		if( 0 < resetting.getTargets().size() ) {
			rtsProfile.setResetting(resetting);
		}
		if( 0 < initialize.getTargets().size() ) {
			rtsProfile.setInitializing(initialize);
		}
		if( 0 < finalize.getTargets().size() ) {
			rtsProfile.setFinalizing(finalize);
		}
	}

	private org.openrtp.namespaces.rts.version02.Condition createTargets(
			org.openrtp.namespaces.rts.version02.ComponentExt target, String strSeq) {
		org.openrtp.namespaces.rts.version02.TargetExecutioncontext targetComp = factory.createTargetExecutioncontext();
		targetComp.setComponentId(target.getId());
		targetComp.setInstanceName(target.getInstanceName());
		//
		org.openrtp.namespaces.rts.version02.Condition targetCond = factory.createCondition();
		targetCond.setSequence(BigInteger.valueOf(Integer.parseInt(strSeq)));
		targetCond.setTargetComponent(targetComp);
		return targetCond;

	}

	// Save時にシステムダイアログ内に含まれるデータポートとそれらの接続をRTSプロファイル内にセットする
	// Save時にダイアグラム内に含まれるサービスポート（とその接続）の情報をRTSプロファイル内にセットする
	private void populatePorts(
			Component eComp,
			org.openrtp.namespaces.rts.version02.ComponentExt target,
			org.openrtp.namespaces.rts.version02.Component original,
			org.openrtp.namespaces.rts.version02.RtsProfileExt rtsProfile) {
		for (Port ePort : eComp.getOutports()) {
			addDataPort(ePort, target, original);
			for (ConnectorProfile eConnProf : ePort.getConnectorProfiles()) {
				addDataPortConnector(eConnProf, rtsProfile, ePort);
			}
		}
		for (Port ePort : eComp.getInports()) {
			addDataPort(ePort, target, original);
			for (ConnectorProfile eConnProf : ePort.getConnectorProfiles()) {
				addDataPortConnector(eConnProf, rtsProfile, ePort);
			}
		}
		for (Port ePort : eComp.getServiceports()) {
			addServicePort(ePort, target, original);
			for (ConnectorProfile eConnProf : ePort.getConnectorProfiles()) {
				addServicePortConnector(eConnProf, rtsProfile, ePort);
			}
		}
	}

	// データポートコネクタをRTSに追加する
	private void addDataPortConnector(
			ConnectorProfile eConnProf,
			org.openrtp.namespaces.rts.version02.RtsProfileExt rtsProfile,
			Port ePort) {
		String connectorId = eConnProf.getConnectorId();
		if(savedConnectors.contains(connectorId) ) return;
		rtsProfile.getDataPortConnectors().add(saveDataPortConnector(ePort, eConnProf));
		savedConnectors.add(connectorId);
	}

	// サービスポートコネクタをRTSに追加する
	private void addServicePortConnector(
			ConnectorProfile eConnProf,
			org.openrtp.namespaces.rts.version02.RtsProfileExt rtsProfile,
			Port ePort) {
		String connectorId = eConnProf.getConnectorId();
		if(savedConnectors.contains(connectorId) ) return;
		rtsProfile.getServicePortConnectors().add(saveServicePortConnector(ePort, eConnProf));
		savedConnectors.add(connectorId);
	}

	// Save時にシステムダイアログ内に含まれるデータポート接続をRTSプロファイル内の該当要素に変換する
	private org.openrtp.namespaces.rts.version02.DataportConnector saveDataPortConnector(
			Port ePort, ConnectorProfile eConnProf) {
		org.openrtp.namespaces.rts.version02.DataportConnectorExt connector = factory.createDataportConnectorExt();
		connector.setConnectorId(eConnProf.getConnectorId());
		connector.setName(eConnProf.getName());
		connector.setInterfaceType(eConnProf.getInterfaceType());
		connector.setDataType(eConnProf.getDataType());
		connector.setDataflowType(eConnProf.getDataflowType());
		if (eConnProf.getSubscriptionType() != null) {
			connector.setSubscriptionType(eConnProf.getSubscriptionType());
		}
		if (eConnProf.getPushRate() != null) {
			connector.setPushInterval(eConnProf.getPushRate());
		}

		// ベンドポイントの保存
		PortConnector ePortConnector = diagram.getConnectorMap().get(
				eConnProf.getConnectorId());
		if (ePortConnector != null) {
			saveBendPoint(ePortConnector.getRoutingConstraint().map(),
					connector.getProperties());
		}

		org.openrtp.namespaces.rts.version02.DataportConnector original = findOrignalDataportConnector(eConnProf
				.getConnectorId());

		if (eConnProf.getSourceString() != null) {
			connector.setSourceDataPort(createTargetPort(ePort.findPort(
					diagram, eConnProf.getSourceString()),
					original == null ? null : original.getSourceDataPort()));
		}
		if (eConnProf.getTargetString() != null) {
			connector.setTargetDataPort(createTargetPort(ePort.findPort(
					diagram, eConnProf.getTargetString()),
					original == null ? null : original.getTargetDataPort()));
		}

		if (original instanceof org.openrtp.namespaces.rts.version02.DataportConnectorExt) {
			org.openrtp.namespaces.rts.version02.DataportConnectorExt originalExt = (org.openrtp.namespaces.rts.version02.DataportConnectorExt) original;
			connector.setComment(originalExt.getComment());
			if (!originalExt.isVisible()) {
				connector.setVisible(Boolean.valueOf(originalExt.isVisible()));
			}
		}

		// 追加プロパティの設定
		if (eConnProf.getPushPolicy() != null) {
			setProperty(PROP.PUSH_POLICY, eConnProf.getPushPolicy(), connector
					.getProperties());
		}
		if (eConnProf.getSkipCount() != null) {
			setProperty(PROP.SKIP_COUNT, eConnProf.getSkipCount().toString(),
					connector.getProperties());
		}
		if (eConnProf.getTimestampPolicy() != null) {
			setProperty(PROP.TIMESTAMP_POLICY, eConnProf.getTimestampPolicy(),
					connector.getProperties());
		}
		//
		if (eConnProf.getOutportBufferLength() != null) {
			setProperty(PROP.OUTPORT_BUFF_LENGTH, eConnProf
					.getOutportBufferLength().toString(), connector
					.getProperties());
		}
		if (eConnProf.getOutportBufferFullPolicy() != null) {
			setProperty(PROP.OUTPORT_FULL_POLICY, eConnProf
					.getOutportBufferFullPolicy(), connector.getProperties());
		}
		if (eConnProf.getOutportBufferWriteTimeout() != null) {
			setProperty(PROP.OUTPORT_WRITE_TIMEOUT, eConnProf
					.getOutportBufferWriteTimeout().toString(), connector
					.getProperties());
		}
		if (eConnProf.getOutportBufferEmptyPolicy() != null) {
			setProperty(PROP.OUTPORT_EMPTY_POLICY, eConnProf
					.getOutportBufferEmptyPolicy(), connector.getProperties());
		}
		if (eConnProf.getOutportBufferReadTimeout() != null) {
			setProperty(PROP.OUTPORT_READ_TIMEOUT, eConnProf
					.getOutportBufferReadTimeout().toString(), connector
					.getProperties());
		}
		//
		if (eConnProf.getInportBufferLength() != null) {
			setProperty(PROP.INPORT_BUFF_LENGTH, eConnProf
					.getInportBufferLength().toString(), connector
					.getProperties());
		}
		if (eConnProf.getInportBufferFullPolicy() != null) {
			setProperty(PROP.INPORT_FULL_POLICY, eConnProf
					.getInportBufferFullPolicy(), connector.getProperties());
		}
		if (eConnProf.getInportBufferWriteTimeout() != null) {
			setProperty(PROP.INPORT_WRITE_TIMEOUT, eConnProf
					.getInportBufferWriteTimeout().toString(), connector
					.getProperties());
		}
		if (eConnProf.getInportBufferEmptyPolicy() != null) {
			setProperty(PROP.INPORT_EMPTY_POLICY, eConnProf
					.getInportBufferEmptyPolicy(), connector.getProperties());
		}
		if (eConnProf.getInportBufferReadTimeout() != null) {
			setProperty(PROP.INPORT_READ_TIMEOUT, eConnProf
					.getInportBufferReadTimeout().toString(), connector
					.getProperties());
		}
		//
		for (String key : eConnProf.getPropertyKeys()) {
			setProperty(key, eConnProf.getProperty(key), connector
					.getProperties());
		}

		return connector;
	}

	// Save時にシステムダイアログ内に含まれるサービスポート接続をRTSプロファイル内の該当要素に変換する
	private org.openrtp.namespaces.rts.version02.ServiceportConnector saveServicePortConnector(
			Port ePort, ConnectorProfile eConnProf) {
		org.openrtp.namespaces.rts.version02.ServiceportConnectorExt connector = factory
				.createServiceportConnectorExt();
		connector.setConnectorId(eConnProf.getConnectorId());
		connector.setName(eConnProf.getName());

		// ベンドポイントの保存
		PortConnector ePortConnector = diagram.getConnectorMap().get(
				eConnProf.getConnectorId());
		if (ePortConnector != null) {
			saveBendPoint(ePortConnector.getRoutingConstraint().map(),
					connector.getProperties());
		}

		org.openrtp.namespaces.rts.version02.ServiceportConnector original = findOrignalServiceportConnector(eConnProf
				.getConnectorId());

		if (eConnProf.getSourceString() != null) {
			connector.setSourceServicePort(createTargetPort(ePort.findPort(
					diagram, eConnProf.getSourceString()),
					original == null ? null : original.getSourceServicePort()));
		}
		if (eConnProf.getTargetString() != null) {
			connector.setTargetServicePort(createTargetPort(ePort.findPort(
					diagram, eConnProf.getTargetString()),
					original == null ? null : original.getTargetServicePort()));
		}

		if (original instanceof org.openrtp.namespaces.rts.version02.ServiceportConnectorExt) {
			if (original != null) {
				connector.setTransMethod(original.getTransMethod());
			}
			org.openrtp.namespaces.rts.version02.ServiceportConnectorExt originalExt = (org.openrtp.namespaces.rts.version02.ServiceportConnectorExt) original;
			connector.setComment(originalExt.getComment());
			if (!originalExt.isVisible()) {
				connector.setVisible(Boolean.valueOf(originalExt.isVisible()));
			}
		}

		// 追加プロパティの設定
		for (String key : eConnProf.getPropertyKeys()) {
			setProperty(key, eConnProf.getProperty(key), connector
					.getProperties());
		}

		return connector;
	}

	// ベンドポイントをRTSプロファイル内に保存する
	private void saveBendPoint(Map<Integer, Point> map,
			List<org.openrtp.namespaces.rts.version02.Property> rtsProperties) {
		if (map == null || map.isEmpty()) {
			return;
		}
		org.openrtp.namespaces.rts.version02.Property propt = factory
				.createProperty();
		propt.setName(KEY_BEND_POINT);
		propt.setValue(convertToBendPointString(map));
		rtsProperties.add(propt);
	}

	// ベンドポイントのMapを文字列表現に変換する
	private String convertToBendPointString(Map<Integer, Point> map) {
		StringBuffer buffer = new StringBuffer();
		for (Object key : map.keySet()) {
			if (buffer.length() == 0) {
				buffer.append("{");
			} else {
				buffer.append(",");
			}
			buffer.append(key).append(":").append("(");
			Point point = (Point) map.get(key);
			buffer.append(point.getX()).append(",").append(point.getY());
			buffer.append(")");
		}
		buffer.append("}");
		return buffer.toString();
	}

	// プロファイルのTargetPortを生成して返す
	private org.openrtp.namespaces.rts.version02.TargetPort createTargetPort(
			Port ePort,
			org.openrtp.namespaces.rts.version02.TargetPort original) {
		org.openrtp.namespaces.rts.version02.TargetPortExt port = factory.createTargetPortExt();
		final Component eComp = (Component) ePort.eContainer();
		port.setComponentId(eComp.getComponentId());
		port.setInstanceName(eComp.getInstanceNameL());
		port.setPortName(ePort.getNameL());

		// pathIdをプロパティにセットする
		org.openrtp.namespaces.rts.version02.Property propt = factory.createProperty();
		propt.setName(KEY_COMPONENT_PATH_ID);
		propt.setValue(eComp.getPathId());
		port.getProperties().add(propt);

		if (original instanceof org.openrtp.namespaces.rts.version02.TargetPortExt) {
			org.openrtp.namespaces.rts.version02.TargetPortExt originalPort = (org.openrtp.namespaces.rts.version02.TargetPortExt) original;
			for (org.openrtp.namespaces.rts.version02.Property property : originalPort.getProperties()) {
				if (property.getName().equals(KEY_COMPONENT_PATH_ID)) continue;
				port.getProperties().add(property);
			}
		}
		return port;
	}

	// Save時にComponentのConfigurationSetの情報をRTSプロファイルにセットする
	private void populateConfigurationSet(
			Component eComp,
			org.openrtp.namespaces.rts.version02.ComponentExt target) {
		for (ConfigurationSet eConfigSet : eComp.getConfigurationSets()) {
			org.openrtp.namespaces.rts.version02.ConfigurationSet config = factory.createConfigurationSet();
			config.setId(eConfigSet.getId());
			for (NameValue nv : eConfigSet.getConfigurationData()) {
				org.openrtp.namespaces.rts.version02.ConfigurationData data = factory.createConfigurationData();
				data.setName(nv.getName());
				data.setData(nv.getValueAsString());
				config.getConfigurationData().add(data);
			}
			target.getConfigurationSets().add(config);
			if (eConfigSet.equals(eComp.getActiveConfigurationSet())) {
				target.setActiveConfigurationSet(eConfigSet.getId());
			}
		}
	}

	// Save時に子RTCの情報をRTSプロファイルにセットする
	private void populateParticipants(
			Component eComp,
			org.openrtp.namespaces.rts.version02.ComponentExt target,
			org.openrtp.namespaces.rts.version02.Component original) {
		for (Component eChildComp : eComp.getComponents()) {
			org.openrtp.namespaces.rts.version02.Participants participants = null;
			if (original != null) {
				participants = findParticipants(eChildComp, original.getParticipants());
			}
			if (participants != null) {
				target.getParticipants().add(participants);
				continue;
			}
			org.openrtp.namespaces.rts.version02.TargetComponentExt child = factory.createTargetComponentExt();
			child.setComponentId(eChildComp.getComponentId());
			child.setInstanceName(eChildComp.getInstanceNameL());

			// pathIdをプロパティにセットする
			org.openrtp.namespaces.rts.version02.Property propt = factory.createProperty();
			propt.setName(KEY_COMPONENT_PATH_ID);
			propt.setValue(eChildComp.getPathId());
			child.getProperties().add(propt);

			participants = factory.createParticipants();
			participants.setParticipant(child);
			target.getParticipants().add(participants);
		}
	}

	// Save時にデータポートの情報をRTSプロファイルに追加する
	private void addDataPort(
			Port ePort,
			org.openrtp.namespaces.rts.version02.ComponentExt target,
			org.openrtp.namespaces.rts.version02.Component original) {
		org.openrtp.namespaces.rts.version02.DataportExt port = factory.createDataportExt();
		port.setName(ePort.getNameL());
		if (original != null) {
			org.openrtp.namespaces.rts.version02.Dataport originalPort = findOriginalPort(original.getDataPorts(), port.getName());
			if (originalPort instanceof org.openrtp.namespaces.rts.version02.DataportExt) {
				org.openrtp.namespaces.rts.version02.DataportExt source = (org.openrtp.namespaces.rts.version02.DataportExt) originalPort;
				port.setComment(source.getComment());
				if (!source.isVisible()) {
					port.setVisible(Boolean.valueOf(source.isVisible()));
				}
			}
		}
		// プロパティ設定
		for (String key : ePort.getSynchronizer().getPropertyKeys()) {
			String value = ePort.getSynchronizer().getProperty(key);
			setProperty(key, value, port.getProperties());
		}
		target.getDataPorts().add(port);
	}

	// Save時にサービスポートの情報をRTSプロファイルに追加する
	private void addServicePort(
			Port ePort,
			org.openrtp.namespaces.rts.version02.ComponentExt target,
			org.openrtp.namespaces.rts.version02.Component original) {
		org.openrtp.namespaces.rts.version02.ServiceportExt port = factory.createServiceportExt();
		port.setName(ePort.getNameL());
		if (original != null) {
			org.openrtp.namespaces.rts.version02.Serviceport originalPort = findOriginalServicePort(original.getServicePorts(), port.getName());
			if (originalPort instanceof org.openrtp.namespaces.rts.version02.ServiceportExt) {
				org.openrtp.namespaces.rts.version02.ServiceportExt source = (org.openrtp.namespaces.rts.version02.ServiceportExt) originalPort;
				port.setComment(source.getComment());
				if (!source.isVisible()) {
					port.setVisible(Boolean.valueOf(source.isVisible()));
				}
			}
		}
		// プロパティ設定
		for (String key : ePort.getSynchronizer().getPropertyKeys()) {
			String value = ePort.getSynchronizer().getProperty(key);
			setProperty(key, value, port.getProperties());
		}
		target.getServicePorts().add(port);
	}

	// Save時にコンポーネントのプロパティをセットする
	private void populateComponentProperty(
			Component eComp,
			org.openrtp.namespaces.rts.version02.ComponentExt target,
			org.openrtp.namespaces.rts.version02.Component original) {
		// プロパティ設定
		for (String key : eComp.getPropertyKeys()) {
				setProperty(key, eComp.getProperty(key), target.getProperties());
		}
		populateIOR(target.getProperties(), eComp);
	}

	// Save時にコンポーネントの位置情報をセットする
	private void populateComponentLocation(
			Component eComp,
			org.openrtp.namespaces.rts.version02.ComponentExt target) {
		target.setLocation(new org.openrtp.namespaces.rts.version02.Location());
		target.getLocation().setX(BigInteger.valueOf(eComp.getConstraint().getX()));
		target.getLocation().setY(BigInteger.valueOf(eComp.getConstraint().getY()));
		target.getLocation().setHeight(BigInteger.valueOf(eComp.getConstraint().getHeight()));
		target.getLocation().setWidth(BigInteger.valueOf(eComp.getConstraint().getWidth()));
		target.getLocation().setDirection(eComp.getOutportDirection());
	}

	// IORを保存する
	private void populateIOR(
			List<org.openrtp.namespaces.rts.version02.Property> rtsProperties,
			Component eComp) {
		if (!(eComp instanceof CorbaComponent)) return;
		RTC.RTObject corbaObjectInterface = ((CorbaComponent) eComp).getCorbaObjectInterface();
		if (corbaObjectInterface == null) return;
		rtsProperties.add(newProperty(KEY_IOR, corbaObjectInterface.toString()));
	}

	// IORを復元する
	private void populateIOR(
			Component eComp,
			List<org.openrtp.namespaces.rts.version02.Property> properties) {
		if (!(eComp instanceof CorbaComponent)) return;
		for (org.openrtp.namespaces.rts.version02.Property prop : properties) {
			if (prop.getName().equals(KEY_IOR)) {
				((CorbaComponent) eComp).setIor(prop.getValue());
			}
		}
	}

	// Save時に元のファイルにあったコンポーネントの拡張属性をセットする
	private void populateFromProfileOnly(
			org.openrtp.namespaces.rts.version02.ComponentExt target,
			org.openrtp.namespaces.rts.version02.Component original) {
		if (!(original instanceof org.openrtp.namespaces.rts.version02.ComponentExt)) return;
		org.openrtp.namespaces.rts.version02.ComponentExt source = (org.openrtp.namespaces.rts.version02.ComponentExt) original;
		target.setComment(source.getComment());
		if (!source.isVisible()) {
			target.setVisible(Boolean.valueOf(source.isVisible()));
		}
	}

	// Save時にExecutionContextの情報をRTSプロファイルにセットする
	private void populateExecutionContext(
			Component eComp,
			org.openrtp.namespaces.rts.version02.ComponentExt target,
			org.openrtp.namespaces.rts.version02.Component original) {
		for (ExecutionContext eEc : eComp.getExecutionContexts()) {
			String id = eComp.getExecutionContextHandler().getId(eEc);
			org.openrtp.namespaces.rts.version02.ExecutionContextExt ec = factory.createExecutionContextExt();
			ec.setId((id == null) ? "" : id);
			ec.setKind(eEc.getKindName());
			ec.setRate(eEc.getRateL());
			for (Component c : eEc.getParticipants()) {
				org.openrtp.namespaces.rts.version02.TargetComponentExt tc = factory.createTargetComponentExt();
				tc.setComponentId(c.getComponentId());
				tc.setInstanceName(c.getInstanceNameL());
				org.openrtp.namespaces.rts.version02.Property prop = newProperty(KEY_COMPONENT_PATH_ID, c
						.getPathId());
				tc.getProperties().add(prop);
				ec.getParticipants().add(tc);
			}
			//
			for (String key : eEc.getPropertyKeys()) {
				setProperty(key, eEc.getProperty(key), ec.getProperties());
			}
			target.getExecutionContexts().add(ec);
		}
	}

	// Save時に、元のRTSプロファイル内だけに存在し、システムエディタでは使用しない要素を書き戻す
	private void populateFromProfileOnly(org.openrtp.namespaces.rts.version02.RtsProfileExt target) {
		if (originalProfile == null) return;
		target.setAbstract(originalProfile.getAbstract());
		target.getGroups().addAll(originalProfile.getGroups());
		target.setComment(originalProfile.getComment());
		target.getVersionUpLogs().addAll(originalProfile.getVersionUpLogs());
	}

	// ベンドポイントの文字列表現からをMap表現のエントリを1つ取り出して、セットする
	private String populatePoint(Map<Integer, Point> result, String content) {
		int pos = content.indexOf(":");
		if (pos < 0) {
			if (content.indexOf(",") >= 0) {
				return content.substring(content.indexOf(",") + 1).trim();
			} else {
				return "";
			}
		}
		String key = content.substring(0, pos).trim();
		String value = content.substring(pos + 1).trim();
		if (key.indexOf(",") >= 0) {
			return content.substring(content.indexOf(",") + 1).trim();
		}
		// インデックス部の解析
		Integer keyi = null;
		try {
			keyi = new Integer(key);
		} catch (NumberFormatException e) {
			return content.substring(content.indexOf(",") + 1).trim();
		}
		// 座標部の解析
		Integer xi, yi = null;
		if (value.startsWith("(")) {
			value = value.substring(1).trim();
		}
		pos = value.indexOf(",");
		try {
			xi = new Integer(value.substring(0, pos).trim());
		} catch (NumberFormatException e) {
			return value.substring(pos + 1).trim();
		}
		value = value.substring(pos + 1).trim();
		pos = value.indexOf(")");
		try {
			yi = new Integer(value.substring(0, pos).trim());
		} catch (NumberFormatException e) {
			return value;
		}
		value = value.substring(pos + 1).trim();

		Point point = new Point();
		point.setX(xi);
		point.setY(yi);
		result.put(keyi, point);
		return value;
	}

	// RTSプロファイルからEMFコンポーネントを復元する
	private void populate(
			EList<Component> target,
			org.openrtp.namespaces.rts.version02.RtsProfile profile) {
		List<Component> eComps = new ArrayList<>();
		for (org.openrtp.namespaces.rts.version02.Component component : profile.getComponents()) {
			Component eComp = loader.create(component, profile);
			if (eComp == null) {
				continue;
			}
			eComp.setComponentId(component.getId());
			eComp.setPathId(component.getPathUri());
			eComp.setInstanceNameL(component.getInstanceName());
			populateCompositeType(eComp, component.getCompositeType());
			eComp.setRequired(component.isIsRequired());

			// portはCORBAObjectまたはRTEProfileを正とする
			// CORBA経由で取得したコンフィグセットを正とする

			if (eComp instanceof ComponentSpecification) {
				// オフラインの場合はポート名を正規化する
				Component spec = (Component) eComp;
				for (Port port : spec.getPorts()) {
					String name = ComponentUtil.createPortName(spec
							.getInstanceNameL(), port.getNameL());
					port.setNameL(name);
				}
			}

			if (online && eComp.getCompositeTypeL().equals(COMPOSITETYPE_GROUPING)) {
				// Grouping複合RTCの場合は、ConfigurationSetを復元する
				populateConfigSets(eComp, component);
			}

			if (component instanceof org.openrtp.namespaces.rts.version02.ComponentExt) {
				org.openrtp.namespaces.rts.version02.ComponentExt componentExt = (org.openrtp.namespaces.rts.version02.ComponentExt)component;
				eComp.setConstraint(toRectangle(componentExt.getLocation()));
				eComp.setOutportDirection(componentExt.getLocation().getDirection());

				// コンポーネントのプロパティ設定
				for (org.openrtp.namespaces.rts.version02.Property prop : componentExt.getProperties()) {
					if (KEY_IOR.equals(prop.getName())) {
						continue;
					}
					eComp.setProperty(prop.getName(), prop.getValue());
				}
				// ポートのプロパティ設定
				for (Port ePort : eComp.getOutports()) {
					populateDataPortProperty(ePort, componentExt.getDataPorts());
				}
				for (Port ePort : eComp.getInports()) {
					populateDataPortProperty(ePort, componentExt.getDataPorts());
				}
				for (Port ePort : eComp.getServiceports()) {
					populateServicePortProperty(ePort,
							componentExt.getServicePorts());
				}

				populateIOR(eComp, componentExt.getProperties());
			}
			eComps.add(eComp);
		}
		//
		populateOrder(eComps, profile.getStartUp());
		populateOrder(eComps, profile.getShutDown());
		populateOrder(eComps, profile.getActivation());
		populateOrder(eComps, profile.getDeactivation());
		populateOrder(eComps, profile.getResetting());
		populateOrder(eComps, profile.getInitializing());
		populateOrder(eComps, profile.getFinalizing());
		//
		for (Component eComp : eComps) {
			if (isShown(eComp, eComps, profile.getComponents()))
				target.add(eComp);
		}
	}

	private void populateOrder(List<Component> eComps, MessageSending ordering) {
		if(ordering == null || ordering.getTargets().size()==0 ) return;
		for(Condition targetCond : ordering.getTargets() ) {
			TargetComponent targetComp = targetCond.getTargetComponent();
			if(targetComp == null ) continue;
			String id = targetComp.getComponentId();
			String instName = targetComp.getInstanceName();
			Component comp = getTargetComp(id, instName, eComps);
			if(comp != null) {
				if(ordering instanceof Startup) {
					comp.setStartUp(targetCond.getSequence().toString());
				} else if(ordering instanceof Shutdown) {
					comp.setShutDown(targetCond.getSequence().toString());
				} else if(ordering instanceof Activation) {
					comp.setActivation(targetCond.getSequence().toString());
				} else if(ordering instanceof Deactivation) {
					comp.setDeActivation(targetCond.getSequence().toString());
				} else if(ordering instanceof Resetting) {
					comp.setResetting(targetCond.getSequence().toString());
				} else if(ordering instanceof Initialize) {
					comp.setInitialize(targetCond.getSequence().toString());
				} else if(ordering instanceof Finalize) {
					comp.setFinalize(targetCond.getSequence().toString());
				}
			}
		}
	}

	private Component getTargetComp(String id, String instName, List<Component> compList) {
		for(Component target : compList) {
			if(target.getComponentId().equals(id) && target.getInstanceNameL().equals(instName)) {
				return target;
			}
		}
		return null;
	}

	void populateDataPortProperty(
			Port ePort,
			List<org.openrtp.namespaces.rts.version02.Dataport> ports) {
		for (org.openrtp.namespaces.rts.version02.Dataport dp : ports) {
			if (!dp.getName().equals(ePort.getNameL())) {
				continue;
			}
			if (!(dp instanceof org.openrtp.namespaces.rts.version02.DataportExt)) {
				continue;
			}
			org.openrtp.namespaces.rts.version02.DataportExt dpExt = (org.openrtp.namespaces.rts.version02.DataportExt) dp;
			for (org.openrtp.namespaces.rts.version02.Property prop : dpExt.getProperties()) {
				ePort.getSynchronizer().setProperty(prop.getName(),
						prop.getValue());
			}
		}
	}

	void populateServicePortProperty(
			Port ePort,
			List<org.openrtp.namespaces.rts.version02.Serviceport> ports) {
		for (org.openrtp.namespaces.rts.version02.Serviceport sp : ports) {
			if (!sp.getName().equals(ePort.getNameL())) {
				continue;
			}
			if (!(sp instanceof org.openrtp.namespaces.rts.version02.ServiceportExt)) {
				continue;
			}
			org.openrtp.namespaces.rts.version02.ServiceportExt spExt = (org.openrtp.namespaces.rts.version02.ServiceportExt) sp;
			for (org.openrtp.namespaces.rts.version02.Property prop : spExt.getProperties()) {
				ePort.getSynchronizer().setProperty(prop.getName(),
						prop.getValue());
			}
		}
	}

	// ルートのシステムダイアグラムに表示されるのであれば、trueを返す
	private boolean isShown(
			Component eComp,
			List<Component> eComps,
			List<org.openrtp.namespaces.rts.version02.Component> source) {
		// targetComponentをparentComponentの子として追加する処理をここで行う
		for (int i = 0; i < eComps.size(); i++) {
			Component eParentComponent = eComps.get(i);
			org.openrtp.namespaces.rts.version02.Component component = source.get(i);
			for (org.openrtp.namespaces.rts.version02.Participants participants : component.getParticipants()) {
				org.openrtp.namespaces.rts.version02.TargetComponent tc = participants.getParticipant();
				if (eComp.getComponentId().equals(tc.getComponentId())
						&& eComp.getInstanceNameL().equals(tc.getInstanceName())) {
					// pathIdもチェックする
					if (equalsPathId(eComp, tc)) {
						eParentComponent.getComponents().add(eComp);
						return false;
					}
				}
			}
		}
		return true;
	}

	// RtsプロファイルのLocationをRectangleに変換する
	private Rectangle toRectangle(org.openrtp.namespaces.rts.version02.Location location) {
		Rectangle result = new Rectangle();
		result.setX(location.getX().intValue());
		result.setY(location.getY().intValue());
		result.setWidth(location.getWidth().intValue());
		result.setHeight(location.getHeight().intValue());
		return result;
	}

	// 複合コンポーネントのポートを復元させる（オフライン）
	private void populateCompositeComponentPort(
			Component eComp,
			org.openrtp.namespaces.rts.version02.Component component) {
		if (!eComp.isCompositeComponent()) return;
		if (!online || eComp.getCompositeTypeL().equals(COMPOSITETYPE_GROUPING)) {
			List<Component> emptyList = Collections.emptyList();
			eComp.addComponentsR(emptyList);
		}
	}

	// データポートの接続を復元させる
	private void populateDataConnector(
			List<Component> eComps,
			List<org.openrtp.namespaces.rts.version02.DataportConnector> dataPortConnectors) {
		for (org.openrtp.namespaces.rts.version02.DataportConnector connBase : dataPortConnectors) {
			ConnectorProfile conn = ComponentFactory.eINSTANCE.createConnectorProfile();
			conn.setConnectorId(connBase.getConnectorId());
			conn.setName(connBase.getName());
			conn.setInterfaceType(connBase.getInterfaceType());
			conn.setDataType(connBase.getDataType());
			conn.setDataflowType(connBase.getDataflowType());
			if(connBase.getSubscriptionType()!=null) conn.setSubscriptionType(connBase.getSubscriptionType());
			if(connBase.getPushInterval()!=null) conn.setPushRate(connBase.getPushInterval());

			// 追加プロパティの設定
			if (connBase instanceof org.openrtp.namespaces.rts.version02.DataportConnectorExt) {
				org.openrtp.namespaces.rts.version02.DataportConnectorExt connExt = (org.openrtp.namespaces.rts.version02.DataportConnectorExt) connBase;
				for (org.openrtp.namespaces.rts.version02.Property p : connExt.getProperties()) {
					String name = p.getName();
					String value = p.getValue();
					if (PROP.PUSH_POLICY.equals(name)) {
						conn.setPushPolicy(value);
					} else if (PROP.SKIP_COUNT.equals(name)) {
						try {
							int i = Integer.parseInt(value);
							conn.setSkipCount(i);
						} catch (Exception e) {
							// void
						}
					} else if (PROP.TIMESTAMP_POLICY.equals(name)) {
						conn.setTimestampPolicy(value);
					}
					//
					else if (PROP.OUTPORT_BUFF_LENGTH.equals(name)) {
						try {
							int i = Integer.parseInt(value);
							conn.setOutportBufferLength(i);
						} catch (Exception e) {
							// void
						}
					} else if (PROP.OUTPORT_FULL_POLICY.equals(name)) {
						conn.setOutportBufferFullPolicy(value);
					} else if (PROP.OUTPORT_WRITE_TIMEOUT.equals(name)) {
						try {
							double d = Double.parseDouble(value);
							conn.setOutportBufferWriteTimeout(d);
						} catch (Exception e) {
							// void
						}
					} else if (PROP.OUTPORT_EMPTY_POLICY.equals(name)) {
						conn.setOutportBufferEmptyPolicy(value);
					} else if (PROP.OUTPORT_READ_TIMEOUT.equals(name)) {
						try {
							double d = Double.parseDouble(value);
							conn.setOutportBufferReadTimeout(d);
						} catch (Exception e) {
							// void
						}
					}
					//
					else if (PROP.INPORT_BUFF_LENGTH.equals(name)) {
						try {
							int i = Integer.parseInt(value);
							conn.setInportBufferLength(i);
						} catch (Exception e) {
							// void
						}
					} else if (PROP.INPORT_FULL_POLICY.equals(name)) {
						conn.setInportBufferFullPolicy(value);
					} else if (PROP.INPORT_WRITE_TIMEOUT.equals(name)) {
						try {
							double d = Double.parseDouble(value);
							conn.setInportBufferWriteTimeout(d);
						} catch (Exception e) {
							// void
						}
					} else if (PROP.INPORT_EMPTY_POLICY.equals(name)) {
						conn.setInportBufferEmptyPolicy(value);
					} else if (PROP.INPORT_READ_TIMEOUT.equals(name)) {
						try {
							double d = Double.parseDouble(value);
							conn.setInportBufferReadTimeout(d);
						} catch (Exception e) {
							// void
						}
					} else {
						if (isIOR(value) || KEY_BEND_POINT.equals(name)) {
							continue;
						}
						conn.setProperty(name, value);
					}
				}
			}

			connectPorts(conn, eComps, connBase.getTargetDataPort(),
					connBase.getSourceDataPort());
		}
	}

	// RTSプロファイルのサービスポート接続から接続を復元する
	private void populateServiceConnector(
			List<Component> eComp,
			List<org.openrtp.namespaces.rts.version02.ServiceportConnector> servicePortConnectors) {
		for (org.openrtp.namespaces.rts.version02.ServiceportConnector connBase : servicePortConnectors) {
			ConnectorProfile eConnProf = ComponentFactory.eINSTANCE.createConnectorProfile();
			eConnProf.setConnectorId(connBase.getConnectorId());
			eConnProf.setName(connBase.getName());

			// 追加プロパティの設定
			if (connBase instanceof org.openrtp.namespaces.rts.version02.ServiceportConnectorExt) {
				org.openrtp.namespaces.rts.version02.ServiceportConnectorExt connExt = (org.openrtp.namespaces.rts.version02.ServiceportConnectorExt) connBase;
				for (org.openrtp.namespaces.rts.version02.Property p : connExt.getProperties()) {
					if (isIOR(p.getValue())
							|| KEY_BEND_POINT.equals(p.getName())) {
						continue;
					}
					eConnProf.setProperty(p.getName(), p.getValue());
				}
			}

			connectPorts(eConnProf, eComp, connBase.getTargetServicePort(),
					connBase.getSourceServicePort());
		}
	}

	private boolean isIOR(String value) {
		return (value != null && value.startsWith("IOR:"));
	}

	// ポート間の接続を復元する
	private void connectPorts(
			ConnectorProfile eConnProf,
			List<Component> eComps,
			org.openrtp.namespaces.rts.version02.TargetPort target,
			org.openrtp.namespaces.rts.version02.TargetPort source) {
		Component eComp = null;
		Port eSource = null;
		Port eTarget = null;
		if (source != null) {
			eComp = findEMFComponentByTargetComponent(source, eComps);
			if (eComp != null) {
				eSource = findEMFPortByTargetPort(source, eComp.getPorts());
			}
		}
		if (target != null) {
			eComp = findEMFComponentByTargetComponent(target, eComps);
			if (eComp != null) {
				eTarget = findEMFPortByTargetPort(target, eComp.getPorts());
			}
		}
		// すでにポート間が接続済みの場合は重複接続しない
		String eSourceId = eSource.getOriginalPortString();
		String eTargetId = eTarget.getOriginalPortString();
		for (ConnectorProfile prof : eSource.getConnectorProfiles()) {
			String sname = prof.getSourceString();
			String tname = prof.getTargetString();
			LOGGER.trace(
					"connectPorts: verify existing connection: sourceId=<{}> targetId=<{}> prof.sourceId=<{}> prof.targetId=<{}>",
					eSourceId, eTargetId, sname, tname);
			if (eSourceId.equals(sname) && eTargetId.equals(tname)) {
				LOGGER.info(
						"connectPorts: already connected ports: source=<{}> target=<{}> sourceId=<{}> targetId=<{}>",
						eSource.getNameL(), eTarget.getNameL(), eSourceId,
						eTargetId);
				return;
			}
		}
		PortConnector eConnector = PortConnectorFactory.createPortConnector(
				eSource, eTarget);
		eConnector.setSource(eSource);
		eConnector.setTarget(eTarget);
		eConnector.setConnectorProfile(eConnProf);
		if (!online) {
			diagram.getConnectorMap().put(eConnProf.getConnectorId(),
					eConnector);
		}
		eConnector.createConnectorR();
	}

	// RTSプロファイルからコンポジット種別を復元する
	private void populateCompositeType(
			Component eComp,
			String compositeType) {
		if (compositeType.equals("None")) return;
		eComp.setCategoryL("composite." + compositeType);
	}

	// RTSプロファイルからコンフィグセットを復元する
	private void populateConfigSets(
			Component eComp,
			org.openrtp.namespaces.rts.version02.Component component) {
		if (component == null) {
			return;
		}
		eComp.getConfigurationSets().clear();
		String activeId = component.getActiveConfigurationSet();
		for (org.openrtp.namespaces.rts.version02.ConfigurationSet configSet : component.getConfigurationSets()) {
			ConfigurationSet eConfigSet = ComponentFactory.eINSTANCE.createConfigurationSet();
			if (configSet.getId().equals(activeId)) eComp.setActiveConfigurationSet(eConfigSet);
			eConfigSet.setId(configSet.getId());
			for (org.openrtp.namespaces.rts.version02.ConfigurationData configData : configSet.getConfigurationData()) {
				NameValue nv = ComponentFactory.eINSTANCE.createNameValue();
				nv.setName(configData.getName());
				nv.setValue(configData.getData());
				eConfigSet.getConfigurationData().add(nv);
			}
			eComp.getConfigurationSets().add(eConfigSet);
		}
	}

	// ベンドポイントをプロパティから復元する
	private Map<Integer, Point> getBendPoint(
			org.openrtp.namespaces.rts.version02.DataportConnector connBase) {
		if (!(connBase instanceof org.openrtp.namespaces.rts.version02.DataportConnectorExt)) {
			return null;
		}
		org.openrtp.namespaces.rts.version02.DataportConnectorExt connBaseExt = (org.openrtp.namespaces.rts.version02.DataportConnectorExt) connBase;
		return getBendPoint(connBaseExt.getProperties());
	}

	// ベンドポイントをプロパティから復元する
	private Map<Integer, Point> getBendPoint(
			org.openrtp.namespaces.rts.version02.ServiceportConnector connBase) {
		if (!(connBase instanceof org.openrtp.namespaces.rts.version02.ServiceportConnectorExt)) {
			return null;
		}
		org.openrtp.namespaces.rts.version02.ServiceportConnectorExt connBaseExt = (org.openrtp.namespaces.rts.version02.ServiceportConnectorExt) connBase;
		return getBendPoint(connBaseExt.getProperties());
	}

	// ベンドポイントをプロパティから復元する
	private Map<Integer, Point> getBendPoint(
			List<org.openrtp.namespaces.rts.version02.Property> properties) {
		String bendPointString = findProperyValue(KEY_BEND_POINT, properties);
		return convertFromBendPointString(bendPointString);
	}

	// ベンドポイントの文字列表現をMap表現に変換する
	public Map<Integer, Point> convertFromBendPointString(String bendPoint) {
		if (StringUtils.isBlank(bendPoint)) {
			return new HashMap<Integer, Point>();
		}
		String content = bendPoint.trim();
		if (content.startsWith("{")) {
			content = content.substring(1).trim(); // "{"除去
		}
		if (content.endsWith("}")) {
			content = content.substring(0, content.length() - 1).trim(); // "}"除去
		}
		Map<Integer, Point> result = new HashMap<Integer, Point>();
		while (content.length() > 0) {
			content = populatePoint(result, content);
		}
		return result;
	}

	/** キーと値を指定してプロパティリストを更新(キーに一致する要素があれば値を上書き) */
	void setProperty(String name, String value,
			List<org.openrtp.namespaces.rts.version02.Property> properties) {
		org.openrtp.namespaces.rts.version02.Property prop = findProperty(name,
				properties);
		if (prop != null) {
			prop.setValue(value);
		} else {
			prop = newProperty(name, value);
			properties.add(prop);
		}
	}

	/** キーと値を指定してプロパティ要素を作成 */
	org.openrtp.namespaces.rts.version02.Property newProperty(String name,
			String value) {
		org.openrtp.namespaces.rts.version02.Property prop = factory
				.createProperty();
		prop.setName(name);
		prop.setValue(value);
		return prop;
	}

	/** TargetComponentがPathIdを持ち、EMFコンポーネントのPathIdが等しい場合はtrue */
	private boolean equalsPathId(
			Component eComp,
			org.openrtp.namespaces.rts.version02.TargetComponent tc) {
		String pathId = getPathId(tc);
		if (pathId == null) {
			return true;
		}
		return pathId.equals(eComp.getPathId());
	}

	/** TargetComponent、もしくはTargetPortのプロパティからPathIdを取り出す */
	private String getPathId(org.openrtp.namespaces.rts.version02.TargetComponent tc) {
		if (tc instanceof org.openrtp.namespaces.rts.version02.TargetPortExt) {
			org.openrtp.namespaces.rts.version02.TargetPortExt saved = (org.openrtp.namespaces.rts.version02.TargetPortExt) tc;
			return findProperyValue(KEY_COMPONENT_PATH_ID, saved.getProperties());
		}
		if (tc instanceof org.openrtp.namespaces.rts.version02.TargetComponentExt) {
			org.openrtp.namespaces.rts.version02.TargetComponentExt saved = (org.openrtp.namespaces.rts.version02.TargetComponentExt) tc;
			return findProperyValue(KEY_COMPONENT_PATH_ID, saved.getProperties());
		}
		return null;
	}

	/** Open時に読み込んだRTSプロファイルから該当するデータポート接続を見つけ出す */
	private org.openrtp.namespaces.rts.version02.DataportConnector findOrignalDataportConnector(String connectorId) {
		if (originalProfile == null) {
			return null;
		}
		for (org.openrtp.namespaces.rts.version02.DataportConnector temp : originalProfile.getDataPortConnectors()) {
			if (temp.getConnectorId().equals(connectorId)) {
				return temp;
			}
		}
		return null;
	}

	/** Open時に読み込んだRTSプロファイルから該当するサービスポート接続を見つけ出す */
	private org.openrtp.namespaces.rts.version02.ServiceportConnector findOrignalServiceportConnector(
			String connectorId) {
		if (originalProfile == null) {
			return null;
		}
		for (org.openrtp.namespaces.rts.version02.ServiceportConnector temp : originalProfile
				.getServicePortConnectors()) {
			if (temp.getConnectorId().equals(connectorId)) {
				return temp;
			}
		}
		return null;
	}

	/** OpenしたRTSプロファイルに存在したDataport要素を探し出す */
	private org.openrtp.namespaces.rts.version02.Dataport findOriginalPort(List<org.openrtp.namespaces.rts.version02.Dataport> ports, String name) {
		for (org.openrtp.namespaces.rts.version02.Dataport port : ports) {
			if (port.getName().equals(name)) {
				return port;
			}
		}
		return null;
	}

	/** OpenしたRTSプロファイルに存在したServiceport要素を探し出す */
	private org.openrtp.namespaces.rts.version02.Serviceport findOriginalServicePort(List<org.openrtp.namespaces.rts.version02.Serviceport> ports, String name) {
		for (org.openrtp.namespaces.rts.version02.Serviceport port : ports) {
			if (port.getName().equals(name)) {
				return port;
			}
		}
		return null;
	}

	/** OpenしたRTSプロファイルに存在したComponent要素を探し出す */
	private org.openrtp.namespaces.rts.version02.Component findOriginalComponent(
			Component eComp) {
		if (originalProfile == null) {
			return null;
		}
		return findComponent(eComp, originalProfile.getComponents());
	}

	/** EMFコンポーネントに合致するRTSコンポーネントを探し出す */
	public static org.openrtp.namespaces.rts.version02.Component findComponent(
			Component eComp,
			List<org.openrtp.namespaces.rts.version02.Component> components) {
		for (org.openrtp.namespaces.rts.version02.Component component : components) {
			if(eComp instanceof ComponentSpecification) {
				if (component.getId().equals(eComp.getComponentId())
						&& component.getInstanceName().equals(
								eComp.getInstanceNameL())
						&& component.getPathUri().equals(eComp.getPathId())) {
					return component;
				}
			} else {
				if (component.getId().equals(eComp.getComponentId())
						&& component.getInstanceName().equals(
								eComp.getInstanceNameL())) {
					return component;
				}
			}
		}
		return null;
	}

	/** EMFコンポーネントのIORに合致するRTSコンポーネントを探し出す */
	public static org.openrtp.namespaces.rts.version02.Component findComponentByIOR(
			Component eComp,
			List<org.openrtp.namespaces.rts.version02.Component> components) {
		if (!(eComp instanceof CorbaComponent)) {
			return null;
		}
		CorbaComponent eCorbaComp = (CorbaComponent) eComp;
		for (org.openrtp.namespaces.rts.version02.Component c : components) {
			if (!(c instanceof org.openrtp.namespaces.rts.version02.ComponentExt)) {
				continue;
			}
			org.openrtp.namespaces.rts.version02.ComponentExt cx = (org.openrtp.namespaces.rts.version02.ComponentExt) c;
			String compIor = findProperyValue(KEY_IOR, cx.getProperties());
			if (compIor == null) {
				continue;
			}
			if (compIor.equals(eCorbaComp.getIor())) {
				return cx;
			}
		}
		return null;
	}

	/** EMFコンポーネントに合致するParticipants要素を探し出す */
	private org.openrtp.namespaces.rts.version02.Participants findParticipants(
			Component eComp,
			List<org.openrtp.namespaces.rts.version02.Participants> participants) {
		for (org.openrtp.namespaces.rts.version02.Participants participant : participants) {
			org.openrtp.namespaces.rts.version02.TargetComponent tc = participant.getParticipant();
			if (tc.getComponentId().equals(eComp.getComponentId())
					&& tc.getInstanceName().equals(eComp.getInstanceNameL())) {
				// pathIdもチェックする
				if (equalsPathId(eComp, tc)) {
					return participant;
				}
			}
		}
		return null;
	}

	/** EMFコンポーネントのECに合致するECを探し出す */
	public static org.openrtp.namespaces.rts.version02.ExecutionContext findExecutionContext(
			ExecutionContext eEc,
			Component eComp,
			List<org.openrtp.namespaces.rts.version02.ExecutionContext> contexts) {
		String id = eComp.getExecutionContextHandler().getId(eEc);
		if (id == null) {
			return null;
		}
		for (org.openrtp.namespaces.rts.version02.ExecutionContext ec : contexts) {
			if (id.equals(ec.getId()) && eEc.getKindName().equals(ec.getKind())) {
				return ec;
			}
		}
		return null;
	}

	/** TargetComponentに合致するEMFコンポーネントを探し出す */
	private Component findEMFComponentByTargetComponent(
			org.openrtp.namespaces.rts.version02.TargetComponent tc,
			List<Component> eComps) {
		for (Component eComp : eComps) {
			if (eComp.getComponentId().equals(tc.getComponentId())
					&& eComp.getInstanceNameL().equals(tc.getInstanceName())) {
				// pathIdもチェックする
				if(this.online) {
					if (equalsPathId(eComp, tc)) {
						return eComp;
					}
				} else {
					return eComp;
				}
			}
		}
		return null;
	}

	/** TargetPortに合致するEMFポートを探し出す */
	private Port findEMFPortByTargetPort(
			org.openrtp.namespaces.rts.version02.TargetPort tp,
			List<Port> ePorts) {
		String name = tp.getPortName();
		if (name.split("\\.").length < 2
				&& !name.startsWith(tp.getInstanceName())) {
			// オフラインの場合はポート名を正規化する
			name = ComponentUtil.createPortName(tp.getInstanceName(),
					tp.getPortName());
		}
		for (Port ePort : ePorts) {
			if (name.equals(ePort.getNameL())) {
				return ePort;
			}
		}
		return null;
	}

	/** RTSプロファイルのプロパティのリストから、指定したキーのプロパティを探し出す */
	static org.openrtp.namespaces.rts.version02.Property findProperty(
			String name,
			List<org.openrtp.namespaces.rts.version02.Property> properties) {
		if (name == null) {
			return null;
		}
		for (org.openrtp.namespaces.rts.version02.Property p : properties) {
			if (name.equals(p.getName())) {
				return p;
			}
		}
		return null;
	}

	/** RTSプロファイルのプロパティのリストから、指定したキーの値を探し出す */
	static String findProperyValue(String name,
			List<org.openrtp.namespaces.rts.version02.Property> properties) {
		org.openrtp.namespaces.rts.version02.Property p = findProperty(name,
				properties);
		return (p != null) ? p.getValue() : null;
	}

}
