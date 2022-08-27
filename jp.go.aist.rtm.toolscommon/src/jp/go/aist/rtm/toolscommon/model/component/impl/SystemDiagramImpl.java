/**
 * <copyright>
 * </copyright>
 *
 * $Id: SystemDiagramImpl.java,v 1.16 2008/03/31 04:25:06 yamashita Exp $
 */
package jp.go.aist.rtm.toolscommon.model.component.impl;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertySource;
import org.openrtp.namespaces.rts.version02.RtsProfileExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.go.aist.rtm.toolscommon.manager.ToolsCommonPreferenceManager;
import jp.go.aist.rtm.toolscommon.model.component.Component;
import jp.go.aist.rtm.toolscommon.model.component.ComponentPackage;
import jp.go.aist.rtm.toolscommon.model.component.CorbaStatusObserver;
import jp.go.aist.rtm.toolscommon.model.component.IPropertyMap;
import jp.go.aist.rtm.toolscommon.model.component.PortConnector;
import jp.go.aist.rtm.toolscommon.model.component.SystemDiagram;
import jp.go.aist.rtm.toolscommon.model.component.SystemDiagramKind;
import jp.go.aist.rtm.toolscommon.model.component.util.CorbaObserverHandler;
import jp.go.aist.rtm.toolscommon.model.component.util.PropertyMap;
import jp.go.aist.rtm.toolscommon.model.core.Point;
import jp.go.aist.rtm.toolscommon.model.core.impl.ModelElementImpl;
import jp.go.aist.rtm.toolscommon.nl.Messages;
import jp.go.aist.rtm.toolscommon.synchronizationframework.RefreshThread;
import jp.go.aist.rtm.toolscommon.synchronizationframework.SynchronizationSupport;
import jp.go.aist.rtm.toolscommon.ui.propertysource.SystemDiagramPropertySource;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>System Diagram</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link jp.go.aist.rtm.toolscommon.model.component.impl.SystemDiagramImpl#getComponents <em>Components</em>}</li>
 *   <li>{@link jp.go.aist.rtm.toolscommon.model.component.impl.SystemDiagramImpl#getKind <em>Kind</em>}</li>
 *   <li>{@link jp.go.aist.rtm.toolscommon.model.component.impl.SystemDiagramImpl#isConnectorProcessing <em>Connector Processing</em>}</li>
 *   <li>{@link jp.go.aist.rtm.toolscommon.model.component.impl.SystemDiagramImpl#getSystemId <em>System Id</em>}</li>
 *   <li>{@link jp.go.aist.rtm.toolscommon.model.component.impl.SystemDiagramImpl#getCreationDate <em>Creation Date</em>}</li>
 *   <li>{@link jp.go.aist.rtm.toolscommon.model.component.impl.SystemDiagramImpl#getUpdateDate <em>Update Date</em>}</li>
 *   <li>{@link jp.go.aist.rtm.toolscommon.model.component.impl.SystemDiagramImpl#getParentSystemDiagram <em>Parent System Diagram</em>}</li>
 *   <li>{@link jp.go.aist.rtm.toolscommon.model.component.impl.SystemDiagramImpl#getCompositeComponent <em>Composite Component</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SystemDiagramImpl extends ModelElementImpl implements SystemDiagram {

	private static final Logger LOGGER = LoggerFactory.getLogger(SystemDiagramImpl.class);

	/**
	 * The cached value of the '{@link #getComponents() <em>Components</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComponents()
	 * @generated
	 * @ordered
	 */
	protected EList<Component> components;

	/**
	 * The default value of the '{@link #getKind() <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected static final SystemDiagramKind KIND_EDEFAULT = SystemDiagramKind.ONLINE_LITERAL;

	/**
	 * The cached value of the '{@link #getKind() <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected SystemDiagramKind kind = KIND_EDEFAULT;

	protected IPropertyMap properties;
	
	private String currentIP = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected SystemDiagramImpl() {
		super();
		this.properties = new PropertyMap();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ComponentPackage.Literals.SYSTEM_DIAGRAM;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Component> getComponents() {
		if (components == null) {
			components = new EObjectContainmentEList<Component>(Component.class, this, ComponentPackage.SYSTEM_DIAGRAM__COMPONENTS);
		}
		return components;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SystemDiagramKind getKind() {
		return kind;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKind(SystemDiagramKind newKind) {
		SystemDiagramKind oldKind = kind;
		kind = newKind == null ? KIND_EDEFAULT : newKind;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SYSTEM_DIAGRAM__KIND, oldKind, kind));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isConnectorProcessing() {
		return connectorProcessing;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConnectorProcessing(boolean newConnectorProcessing) {
		boolean oldConnectorProcessing = connectorProcessing;
		connectorProcessing = newConnectorProcessing;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SYSTEM_DIAGRAM__CONNECTOR_PROCESSING, oldConnectorProcessing, connectorProcessing));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSystemId() {
		return systemId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSystemId(String newSystemId) {
		String oldSystemId = systemId;
		systemId = newSystemId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SYSTEM_DIAGRAM__SYSTEM_ID, oldSystemId, systemId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCreationDate() {
		return creationDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCreationDate(String newCreationDate) {
		String oldCreationDate = creationDate;
		creationDate = newCreationDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SYSTEM_DIAGRAM__CREATION_DATE, oldCreationDate, creationDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUpdateDate() {
		return updateDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUpdateDate(String newUpdateDate) {
		String oldUpdateDate = updateDate;
		updateDate = newUpdateDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SYSTEM_DIAGRAM__UPDATE_DATE, oldUpdateDate, updateDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SystemDiagram getParentSystemDiagram() {
		if (parentSystemDiagram != null && parentSystemDiagram.eIsProxy()) {
			InternalEObject oldParentSystemDiagram = (InternalEObject)parentSystemDiagram;
			parentSystemDiagram = (SystemDiagram)eResolveProxy(oldParentSystemDiagram);
			if (parentSystemDiagram != oldParentSystemDiagram) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ComponentPackage.SYSTEM_DIAGRAM__PARENT_SYSTEM_DIAGRAM, oldParentSystemDiagram, parentSystemDiagram));
			}
		}
		return parentSystemDiagram;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SystemDiagram basicGetParentSystemDiagram() {
		return parentSystemDiagram;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParentSystemDiagram(SystemDiagram newParentSystemDiagram) {
		SystemDiagram oldParentSystemDiagram = parentSystemDiagram;
		parentSystemDiagram = newParentSystemDiagram;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SYSTEM_DIAGRAM__PARENT_SYSTEM_DIAGRAM, oldParentSystemDiagram, parentSystemDiagram));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Component getCompositeComponent() {
		if (compositeComponent != null && compositeComponent.eIsProxy()) {
			InternalEObject oldCompositeComponent = (InternalEObject)compositeComponent;
			compositeComponent = (Component)eResolveProxy(oldCompositeComponent);
			if (compositeComponent != oldCompositeComponent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ComponentPackage.SYSTEM_DIAGRAM__COMPOSITE_COMPONENT, oldCompositeComponent, compositeComponent));
			}
		}
		return compositeComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Component basicGetCompositeComponent() {
		return compositeComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCompositeComponent(Component newCompositeComponent) {
		Component oldCompositeComponent = compositeComponent;
		compositeComponent = newCompositeComponent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SYSTEM_DIAGRAM__COMPOSITE_COMPONENT, oldCompositeComponent, compositeComponent));
	}

	/**
	 * The default value of the '{@link #isConnectorProcessing() <em>Connector Processing</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isConnectorProcessing()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CONNECTOR_PROCESSING_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isConnectorProcessing() <em>Connector Processing</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isConnectorProcessing()
	 * @generated
	 * @ordered
	 */
	protected boolean connectorProcessing = CONNECTOR_PROCESSING_EDEFAULT;

	/**
	 * The default value of the '{@link #getSystemId() <em>System Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSystemId()
	 * @generated
	 * @ordered
	 */
	protected static final String SYSTEM_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSystemId() <em>System Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSystemId()
	 * @generated
	 * @ordered
	 */
	protected String systemId = SYSTEM_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getCreationDate() <em>Creation Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCreationDate()
	 * @generated
	 * @ordered
	 */
	protected static final String CREATION_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCreationDate() <em>Creation Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCreationDate()
	 * @generated
	 * @ordered
	 */
	protected String creationDate = CREATION_DATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getUpdateDate() <em>Update Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpdateDate()
	 * @generated
	 * @ordered
	 */
	protected static final String UPDATE_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUpdateDate() <em>Update Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpdateDate()
	 * @generated
	 * @ordered
	 */
	protected String updateDate = UPDATE_DATE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getParentSystemDiagram() <em>Parent System Diagram</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParentSystemDiagram()
	 * @generated
	 * @ordered
	 */
	protected SystemDiagram parentSystemDiagram;

	/**
	 * The cached value of the '{@link #getCompositeComponent() <em>Composite Component</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCompositeComponent()
	 * @generated
	 * @ordered
	 */
	protected Component compositeComponent;

	RefreshThread syncRemoteThread;
	RefreshThread syncLocalThread;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	@Override
	public synchronized void setSynchronizeInterval(long milliSecond) {
		if (!SystemDiagramKind.ONLINE_LITERAL.equals(getKind())) {
			return;
		}
		// CORBAからの同期
		if (syncRemoteThread == null) {
			syncRemoteThread = new RefreshThread(milliSecond) {
				@Override
				protected void executeCommand() {
					synchronizeRemote();
				}
			};
			syncRemoteThread.setDaemon(true);
			syncRemoteThread.start();
		} else {
			syncRemoteThread.setSynchronizeInterval(milliSecond);
		}
		// モデルへの同期
		if (syncLocalThread == null) {
			syncLocalThread = new RefreshThread(1000) {
				@Override
				protected void executeCommand() {
					synchronizeLocal();
				}
			};
			syncLocalThread.setDaemon(true);
			syncLocalThread.start();
		}
	}

	protected synchronized List<Component> getUnmodifiedComponents() {
		return new ArrayList<Component>(getComponents());
	}

	@Override
	public synchronized void removeComponent(Component component) {
		CorbaObserverHandler.eINSTANCE.detachStatusObserver(component);
		for (Component comp : component.getComponents()) {
			CorbaObserverHandler.eINSTANCE.detachStatusObserver(comp);
		}
		//
		getComponents().remove(component);
	}

	@Override
	public synchronized void removeComponents(List<Component> components) {
		for (Component c : components) {
			removeComponent(c);
		}
	}

	@Override
	public synchronized void addComponent(Component component) {
		addComponent(-1, component);
	}

	@Override
	public synchronized void addComponent(int pos, Component component) {
		if (!ToolsCommonPreferenceManager.getInstance().isSTATUS_OBSERVER_ATTACH_ENABLE()) {
			LOGGER.debug("addComponent: global observer attachment is off.");
		} else {
			CorbaObserverHandler.eINSTANCE.attachStatusObserver(component);
			for (Component comp : component.getComponents()) {
				CorbaObserverHandler.eINSTANCE.attachStatusObserver(comp);
			}
		}
		//
		if (pos == -1) {
			getComponents().add(component);
		} else {
			getComponents().add(pos, component);
		}
	}

	@Override
	public synchronized void addComponents(List<Component> components) {
		for (Component c : components) {
			addComponent(-1, c);
		}
	}

	@Override
	public synchronized void clearComponents() {
		for (Component c : getUnmodifiedComponents()) {
			removeComponent(c);
		}
	}

	@Override
	public SystemDiagram getRootDiagram() {
		if (getParentSystemDiagram() == null) return this;
		return getParentSystemDiagram().getRootDiagram();
	}

	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);

	private RtsProfileExt profile;
	/**
	 * <!-- begin-user-doc -->
	 * コンポーネンツ変更の通知を行うリスナを登録する
	 * @param listener
	 * 
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	/**
	 * <!-- begin-user-doc -->
	 * コンポーネンツ変更の通知を行うリスナを取得する
	 * @param listener
	 * 
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	private PropertyChangeSupport getPropertyChangeSupport() {
		return propertyChangeSupport;
	}

	/**
	 * <!-- begin-user-doc -->
	 * コンポーネンツ変更の通知を行うリスナを削除する
	 * @param listener
	 * 
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String removeProperty(String key) {
		return properties.removeProperty(key);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public EList<String> getPropertyKeys() {
		return properties.getPropertyKeys();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public IPropertyMap getPropertyMap() {
		return properties;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ComponentPackage.SYSTEM_DIAGRAM__COMPONENTS:
				return ((InternalEList<?>)getComponents()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ComponentPackage.SYSTEM_DIAGRAM__COMPONENTS:
				return getComponents();
			case ComponentPackage.SYSTEM_DIAGRAM__KIND:
				return getKind();
			case ComponentPackage.SYSTEM_DIAGRAM__CONNECTOR_PROCESSING:
				return isConnectorProcessing();
			case ComponentPackage.SYSTEM_DIAGRAM__SYSTEM_ID:
				return getSystemId();
			case ComponentPackage.SYSTEM_DIAGRAM__CREATION_DATE:
				return getCreationDate();
			case ComponentPackage.SYSTEM_DIAGRAM__UPDATE_DATE:
				return getUpdateDate();
			case ComponentPackage.SYSTEM_DIAGRAM__PARENT_SYSTEM_DIAGRAM:
				if (resolve) return getParentSystemDiagram();
				return basicGetParentSystemDiagram();
			case ComponentPackage.SYSTEM_DIAGRAM__COMPOSITE_COMPONENT:
				if (resolve) return getCompositeComponent();
				return basicGetCompositeComponent();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ComponentPackage.SYSTEM_DIAGRAM__COMPONENTS:
				getComponents().clear();
				getComponents().addAll((Collection<? extends Component>)newValue);
				return;
			case ComponentPackage.SYSTEM_DIAGRAM__KIND:
				setKind((SystemDiagramKind)newValue);
				return;
			case ComponentPackage.SYSTEM_DIAGRAM__CONNECTOR_PROCESSING:
				setConnectorProcessing((Boolean)newValue);
				return;
			case ComponentPackage.SYSTEM_DIAGRAM__SYSTEM_ID:
				setSystemId((String)newValue);
				return;
			case ComponentPackage.SYSTEM_DIAGRAM__CREATION_DATE:
				setCreationDate((String)newValue);
				return;
			case ComponentPackage.SYSTEM_DIAGRAM__UPDATE_DATE:
				setUpdateDate((String)newValue);
				return;
			case ComponentPackage.SYSTEM_DIAGRAM__PARENT_SYSTEM_DIAGRAM:
				setParentSystemDiagram((SystemDiagram)newValue);
				return;
			case ComponentPackage.SYSTEM_DIAGRAM__COMPOSITE_COMPONENT:
				setCompositeComponent((Component)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ComponentPackage.SYSTEM_DIAGRAM__COMPONENTS:
				getComponents().clear();
				return;
			case ComponentPackage.SYSTEM_DIAGRAM__KIND:
				setKind(KIND_EDEFAULT);
				return;
			case ComponentPackage.SYSTEM_DIAGRAM__CONNECTOR_PROCESSING:
				setConnectorProcessing(CONNECTOR_PROCESSING_EDEFAULT);
				return;
			case ComponentPackage.SYSTEM_DIAGRAM__SYSTEM_ID:
				setSystemId(SYSTEM_ID_EDEFAULT);
				return;
			case ComponentPackage.SYSTEM_DIAGRAM__CREATION_DATE:
				setCreationDate(CREATION_DATE_EDEFAULT);
				return;
			case ComponentPackage.SYSTEM_DIAGRAM__UPDATE_DATE:
				setUpdateDate(UPDATE_DATE_EDEFAULT);
				return;
			case ComponentPackage.SYSTEM_DIAGRAM__PARENT_SYSTEM_DIAGRAM:
				setParentSystemDiagram((SystemDiagram)null);
				return;
			case ComponentPackage.SYSTEM_DIAGRAM__COMPOSITE_COMPONENT:
				setCompositeComponent((Component)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ComponentPackage.SYSTEM_DIAGRAM__COMPONENTS:
				return components != null && !components.isEmpty();
			case ComponentPackage.SYSTEM_DIAGRAM__KIND:
				return kind != KIND_EDEFAULT;
			case ComponentPackage.SYSTEM_DIAGRAM__CONNECTOR_PROCESSING:
				return connectorProcessing != CONNECTOR_PROCESSING_EDEFAULT;
			case ComponentPackage.SYSTEM_DIAGRAM__SYSTEM_ID:
				return SYSTEM_ID_EDEFAULT == null ? systemId != null : !SYSTEM_ID_EDEFAULT.equals(systemId);
			case ComponentPackage.SYSTEM_DIAGRAM__CREATION_DATE:
				return CREATION_DATE_EDEFAULT == null ? creationDate != null : !CREATION_DATE_EDEFAULT.equals(creationDate);
			case ComponentPackage.SYSTEM_DIAGRAM__UPDATE_DATE:
				return UPDATE_DATE_EDEFAULT == null ? updateDate != null : !UPDATE_DATE_EDEFAULT.equals(updateDate);
			case ComponentPackage.SYSTEM_DIAGRAM__PARENT_SYSTEM_DIAGRAM:
				return parentSystemDiagram != null;
			case ComponentPackage.SYSTEM_DIAGRAM__COMPOSITE_COMPONENT:
				return compositeComponent != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (kind: ");
		result.append(kind);
		result.append(", ConnectorProcessing: ");
		result.append(connectorProcessing);
		result.append(", systemId: ");
		result.append(systemId);
		result.append(", creationDate: ");
		result.append(creationDate);
		result.append(", updateDate: ");
		result.append(updateDate);
		result.append(')');
		return result.toString();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public java.lang.Object getAdapter(Class adapter) {
		java.lang.Object result = null;
		if (IPropertySource.class.equals(adapter)) {
			result = new SystemDiagramPropertySource(this);
		}
		if (result == null) {
			result = super.getAdapter(adapter);
		}
		return result;
	}

	@Override
	public RtsProfileExt getProfile() {
		return profile;
	}

	@Override
	public void setProfile(RtsProfileExt profile) {
		this.profile = profile;
	}

	private Map<String, PortConnector> connectorMap = new HashMap<>();

	@Override
	public Map<String, PortConnector> getConnectorMap() {
		return connectorMap;
	}

	private Map<String, Map<Integer, Point>> portConnectorRoutingConstraintMap = new HashMap<>();

	@Override
	public Map<Integer, Point> getPortConnectorRoutingConstraint(
			String connectorId) {
		Map<Integer, Point> ret = this.portConnectorRoutingConstraintMap
				.get(connectorId);
		if (ret == null) {
			ret = new TreeMap<>();
			this.portConnectorRoutingConstraintMap.put(connectorId, ret);
		}
		return ret;
	}

	@Override
	public void dispose() {
		if (syncLocalThread != null) {
			syncLocalThread.setSynchronizeInterval(-1);
			syncLocalThread = null;
		}
		if (syncRemoteThread != null) {
			syncRemoteThread.setSynchronizeInterval(-1);
			syncRemoteThread = null;
		}
	}

	private MessageDialog msgDialog = null;
	
	void synchronizeRemote() {
		if (getParentSystemDiagram() == null) {
			try {
				InetAddress addr = InetAddress.getLocalHost();
				String address = addr.getHostAddress();
				if(currentIP==null) {
					currentIP = address;
				} else {
					if(currentIP.equals(address)==false) {
						String oldAddress = currentIP;
						currentIP = address;
						if( 0 < PlatformUI.getWorkbench().getWorkbenchWindowCount() ) {
							Shell shell = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getShell();
							shell.getDisplay().asyncExec(new Runnable() {
							    public void run() {
							    	if(msgDialog != null) {
							    		msgDialog.close();
							    	}
							    	String title = Messages.getString("IPCaution.title");
							    	String message = Messages.getString("IPCaution.message01") + " (" + oldAddress + " -> " + currentIP + ")" + System.lineSeparator()
				            							+ Messages.getString("IPCaution.message02");
							    	String[] buttonLabels = new String[] { "OK" };
							    	msgDialog = new MessageDialog(shell, title, null, message, MessageDialog.ERROR, buttonLabels, 0);
							    	msgDialog.open();
							    }
							});
						}
					}
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}	
			
			for (Component component : getUnmodifiedComponents()) {
				if (component instanceof CorbaComponentImpl) {
					CorbaComponentImpl corbaComp = (CorbaComponentImpl) component;
					CorbaStatusObserver obs = corbaComp.getStatusObserver();
					if (obs != null) {
						continue;
					}
				}
				//
				SynchronizationSupport support = component.getSynchronizationSupport();
				if (support == null) {
					continue;
				}
				support.synchronizeRemote();
			}
		}
	}

	void synchronizeLocal() {
		// // リモートと同期を取る
		for (Component component : getUnmodifiedComponents()) {
			if (component instanceof CorbaComponentImpl) {
				CorbaComponentImpl corbaComp = (CorbaComponentImpl) component;
				CorbaStatusObserver obs = corbaComp.getStatusObserver();
				if (obs != null) {
					// 状態通知オブザーバが登録されている場合の同期
					if (obs.isTimeOut()) {
						// H.Bがタイムアウトしていたらダイアグラムから削除
						if (!SynchronizationSupport.ping(corbaComp.getCorbaObjectInterface())) {
							removeComponent(corbaComp);
						}
						continue;
					}
				}
			}
			//
			SynchronizationSupport support = component.getSynchronizationSupport();
			if (support == null) {
				continue;
			}
			if (support.failSynchronizeRemote()) {
				LOGGER.error("Fail to sync remote, so remove component in diagram. comp=<{}>", component);
				removeComponent(component);
			}
			support.synchronizeLocal();
		}
		//
		try {
			closeIfExit();
		} catch (Exception e) {
			// void
		}
	}

	private void closeIfExit() {
		if (getParentSystemDiagram() == null) {
			return;
		}
		SystemDiagram rootDiagram = getRootDiagram();

		synchronized (rootDiagram) {
			List<Component> registeredComponents = rootDiagram
					.getRegisteredComponents();
			List<Component> exits = new ArrayList<Component>();
			for (Component c : getComponents()) {
				if (!isExist(registeredComponents, c)) {
					exits.add(c);
				}
			}
			// 当ダイアグラム中のコンポーネントが存在しない(Exit)ときは削除
			for (Component c : exits) {
				getComponents().remove(c);
			}
			// 親コンポーネントが存在しない(Exit)場合はエディタを閉じる
			if (!isExist(registeredComponents, getCompositeComponent())) {
				getPropertyChangeSupport().firePropertyChange(
						"SYSTEM_DIAGRAM_COMPONENTS", getCompositeComponent(),
						null);
			}
		}
	}

	private boolean isExist(List<Component> registeredComponents,
			Component component) {
		for (Component element : registeredComponents) {
			if (element == component) {
				return true;
			}
		}
		return false;
	}

	@Override
	public synchronized boolean synchronizeManually() {
		if (!SystemDiagramKind.ONLINE_LITERAL.equals(getKind())) {
			return false;
		}
		if (syncRemoteThread != null && syncRemoteThread.isRunning()) {
			return false;
		}
		synchronizeLocal();
		return true;
	}

	@Override
	public List<Component> getRegisteredComponents() {
		List<Component> unmodifiedComponents = getUnmodifiedComponents();
		List<Component> result = new ArrayList<Component>(unmodifiedComponents);
		for (Component c : unmodifiedComponents) {
			result.addAll(c.getAllComponents());
		}
		return result;
	}

} // SystemDiagramImpl
