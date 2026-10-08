/**
 * <copyright>
 * </copyright>
 *
 * $Id: ServicePort.java,v 1.1 2007/12/25 05:43:00 tsakamoto Exp $
 */
package jp.go.aist.rtm.rtcbuilder.model.component;

import java.util.List;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Service Port</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link jp.go.aist.rtm.rtcbuilder.model.component.ServicePort#getServicePort_Name <em>Service Port Name</em>}</li>
 *   <li>{@link jp.go.aist.rtm.rtcbuilder.model.component.ServicePort#getServiceInterfaces <em>Service Interfaces</em>}</li>
 * </ul>
 * </p>
 *
 * @see jp.go.aist.rtm.rtcbuilder.model.component.ComponentPackage#getServicePort()
 * @model
 * @generated
 */
public interface ServicePort extends PortBase {
	/**
	 * Returns the value of the '<em><b>Service Port Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Service Port Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Service Port Name</em>' attribute.
	 * @see #setServicePort_Name(String)
	 * @see jp.go.aist.rtm.rtcbuilder.model.component.ComponentPackage#getServicePort_ServicePort_Name()
	 * @model
	 * @generated
	 */
	String getServicePort_Name();

	/**
	 * Sets the value of the '{@link jp.go.aist.rtm.rtcbuilder.model.component.ServicePort#getServicePort_Name <em>Service Port Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Service Port Name</em>' attribute.
	 * @see #getServicePort_Name()
	 * @generated
	 */
	void setServicePort_Name(String value);

	/**
	 * Returns the value of the '<em><b>Service Interfaces</b></em>' reference list.
	 * The list contents are of type {@link jp.go.aist.rtm.rtcbuilder.model.component.ServiceInterface}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Service Interfaces</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Service Interfaces</em>' reference list.
	 * @see jp.go.aist.rtm.rtcbuilder.model.component.ComponentPackage#getServicePort_ServiceInterfaces()
	 * @model type="jp.go.aist.rtm.rtcbuilder.model.component.ServiceInterface"
	 * @generated NOT
	 */
	List getServiceInterfaces();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void addServiceInterface(ServiceInterface serviceinterface);

	int getPort_Type();
	void setPort_Type(int port_Type) ;

} // ServicePort