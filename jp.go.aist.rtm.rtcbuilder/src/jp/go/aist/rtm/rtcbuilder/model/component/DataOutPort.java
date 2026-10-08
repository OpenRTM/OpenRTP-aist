/**
 * <copyright>
 * </copyright>
 *
 * $Id: DataOutPort.java,v 1.1 2007/12/25 05:43:00 tsakamoto Exp $
 */
package jp.go.aist.rtm.rtcbuilder.model.component;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Out Port</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link jp.go.aist.rtm.rtcbuilder.model.component.DataOutPort#getOutPort_Name <em>Out Port Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see jp.go.aist.rtm.rtcbuilder.model.component.ComponentPackage#getDataOutPort()
 * @model
 * @generated
 */
public interface DataOutPort extends PortBase {
	/**
	 * Returns the value of the '<em><b>Out Port Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Out Port Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Out Port Name</em>' attribute.
	 * @see #setOutPort_Name(String)
	 * @see jp.go.aist.rtm.rtcbuilder.model.component.ComponentPackage#getDataOutPort_OutPort_Name()
	 * @model
	 * @generated
	 */
	String getOutPort_Name();

	/**
	 * Sets the value of the '{@link jp.go.aist.rtm.rtcbuilder.model.component.DataOutPort#getOutPort_Name <em>Out Port Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Out Port Name</em>' attribute.
	 * @see #getOutPort_Name()
	 * @generated
	 */
	void setOutPort_Name(String value);
	
	int getPort_Type();
	void setPort_Type(int port_Type) ;

} // DataOutPort