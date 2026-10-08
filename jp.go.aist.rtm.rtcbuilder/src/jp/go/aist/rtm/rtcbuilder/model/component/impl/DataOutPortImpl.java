/**
 * <copyright>
 * </copyright>
 *
 * $Id: DataOutPortImpl.java,v 1.1 2007/12/25 05:43:03 tsakamoto Exp $
 */
package jp.go.aist.rtm.rtcbuilder.model.component.impl;

import jp.go.aist.rtm.rtcbuilder.model.component.ComponentPackage;
import jp.go.aist.rtm.rtcbuilder.model.component.DataOutPort;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Data Out Port</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link jp.go.aist.rtm.rtcbuilder.model.component.impl.DataOutPortImpl#getOutPort_Name <em>Out Port Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DataOutPortImpl extends PortBaseImpl implements DataOutPort {
	/**
	 * The default value of the '{@link #getOutPort_Name() <em>Out Port Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutPort_Name()
	 * @generated
	 * @ordered
	 */
	protected static final String OUT_PORT_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOutPort_Name() <em>Out Port Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutPort_Name()
	 * @generated
	 * @ordered
	 */
	protected String outPort_Name = OUT_PORT_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DataOutPortImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return ComponentPackage.Literals.DATA_OUT_PORT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOutPort_Name() {
		return outPort_Name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOutPort_Name(String newOutPort_Name) {
		String oldOutPort_Name = outPort_Name;
		outPort_Name = newOutPort_Name;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.DATA_OUT_PORT__OUT_PORT_NAME, oldOutPort_Name, outPort_Name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ComponentPackage.DATA_OUT_PORT__OUT_PORT_NAME:
				return getOutPort_Name();
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
			case ComponentPackage.DATA_OUT_PORT__OUT_PORT_NAME:
				setOutPort_Name((String)newValue);
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
			case ComponentPackage.DATA_OUT_PORT__OUT_PORT_NAME:
				setOutPort_Name(OUT_PORT_NAME_EDEFAULT);
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
			case ComponentPackage.DATA_OUT_PORT__OUT_PORT_NAME:
				return OUT_PORT_NAME_EDEFAULT == null ? outPort_Name != null : !OUT_PORT_NAME_EDEFAULT.equals(outPort_Name);
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
		result.append(" (OutPort_Name: ");
		result.append(outPort_Name);
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

} //DataOutPortImpl