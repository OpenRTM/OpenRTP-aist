/**
 * <copyright>
 * </copyright>
 *
 * $Id: ServicePortImpl.java,v 1.1 2007/12/25 05:43:03 tsakamoto Exp $
 */
package jp.go.aist.rtm.rtcbuilder.model.component.impl;

import java.util.Collection;

import jp.go.aist.rtm.rtcbuilder.model.component.ComponentPackage;
import jp.go.aist.rtm.rtcbuilder.model.component.ServiceInterface;
import jp.go.aist.rtm.rtcbuilder.model.component.ServicePort;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Service Port</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link jp.go.aist.rtm.rtcbuilder.model.component.impl.ServicePortImpl#getServicePort_Name <em>Service Port Name</em>}</li>
 *   <li>{@link jp.go.aist.rtm.rtcbuilder.model.component.impl.ServicePortImpl#getServiceInterfaces <em>Service Interfaces</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ServicePortImpl extends PortBaseImpl implements ServicePort {
	/**
	 * The default value of the '{@link #getServicePort_Name() <em>Service Port Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServicePort_Name()
	 * @generated
	 * @ordered
	 */
	protected static final String SERVICE_PORT_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getServicePort_Name() <em>Service Port Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServicePort_Name()
	 * @generated
	 * @ordered
	 */
	protected String servicePort_Name = SERVICE_PORT_NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getServiceInterfaces() <em>Service Interfaces</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServiceInterfaces()
	 * @generated
	 * @ordered
	 */
	protected EList serviceInterfaces = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ServicePortImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return ComponentPackage.Literals.SERVICE_PORT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getServicePort_Name() {
		return servicePort_Name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setServicePort_Name(String newServicePort_Name) {
		String oldServicePort_Name = servicePort_Name;
		servicePort_Name = newServicePort_Name;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SERVICE_PORT__SERVICE_PORT_NAME, oldServicePort_Name, servicePort_Name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getServiceInterfaces() {
		if (serviceInterfaces == null) {
			serviceInterfaces = new EObjectContainmentEList(ServiceInterface.class, this, ComponentPackage.SERVICE_PORT__SERVICE_INTERFACES);
		}
		return serviceInterfaces;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void addServiceInterface(ServiceInterface serviceinterface) {
		getServiceInterfaces().add(serviceinterface);
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SERVICE_PORT__SERVICE_INTERFACES, serviceinterface, null));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ComponentPackage.SERVICE_PORT__SERVICE_INTERFACES:
				return ((InternalEList)getServiceInterfaces()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ComponentPackage.SERVICE_PORT__SERVICE_PORT_NAME:
				return getServicePort_Name();
			case ComponentPackage.SERVICE_PORT__SERVICE_INTERFACES:
				return getServiceInterfaces();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ComponentPackage.SERVICE_PORT__SERVICE_PORT_NAME:
				setServicePort_Name((String)newValue);
				return;
			case ComponentPackage.SERVICE_PORT__SERVICE_INTERFACES:
				getServiceInterfaces().clear();
				getServiceInterfaces().addAll((Collection)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
			case ComponentPackage.SERVICE_PORT__SERVICE_PORT_NAME:
				setServicePort_Name(SERVICE_PORT_NAME_EDEFAULT);
				return;
			case ComponentPackage.SERVICE_PORT__SERVICE_INTERFACES:
				getServiceInterfaces().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ComponentPackage.SERVICE_PORT__SERVICE_PORT_NAME:
				return SERVICE_PORT_NAME_EDEFAULT == null ? servicePort_Name != null : !SERVICE_PORT_NAME_EDEFAULT.equals(servicePort_Name);
			case ComponentPackage.SERVICE_PORT__SERVICE_INTERFACES:
				return serviceInterfaces != null && !serviceInterfaces.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (ServicePort_Name: ");
		result.append(servicePort_Name);
		result.append(')');
		return result.toString();
	}

	protected int port_Type = 0;

	public int getPort_Type() {
		return port_Type;
	}

	public void setPort_Type(int port_Type) {
		this.port_Type = port_Type;
	}

} //ServicePortImpl