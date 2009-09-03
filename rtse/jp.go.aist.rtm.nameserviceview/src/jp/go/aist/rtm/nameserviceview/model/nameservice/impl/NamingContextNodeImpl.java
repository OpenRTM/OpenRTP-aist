/**
 * <copyright>
 * </copyright>
 *
 * $Id: NamingContextNodeImpl.java,v 1.5 2008/01/28 09:44:51 yamashita Exp $
 */
package jp.go.aist.rtm.nameserviceview.model.nameservice.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.go.aist.rtm.nameserviceview.manager.ManagerPackage;
import jp.go.aist.rtm.nameserviceview.manager.NameServerContext;
import jp.go.aist.rtm.nameserviceview.model.nameservice.CorbaNode;
import jp.go.aist.rtm.nameserviceview.model.nameservice.NameServiceReference;
import jp.go.aist.rtm.nameserviceview.model.nameservice.NameservicePackage;
import jp.go.aist.rtm.nameserviceview.model.nameservice.NamingContextNode;
import jp.go.aist.rtm.toolscommon.corba.CorbaUtil;
import jp.go.aist.rtm.toolscommon.model.core.CorePackage;
import jp.go.aist.rtm.toolscommon.synchronizationframework.LocalObject;
import jp.go.aist.rtm.toolscommon.synchronizationframework.SynchronizationManager;
import jp.go.aist.rtm.toolscommon.synchronizationframework.mapping.AttributeMapping;
import jp.go.aist.rtm.toolscommon.synchronizationframework.mapping.ClassMapping;
import jp.go.aist.rtm.toolscommon.synchronizationframework.mapping.ConstructorParamMapping;
import jp.go.aist.rtm.toolscommon.synchronizationframework.mapping.ManyReferenceMapping;
import jp.go.aist.rtm.toolscommon.synchronizationframework.mapping.MappingRule;
import jp.go.aist.rtm.toolscommon.synchronizationframework.mapping.ReferenceMapping;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.omg.CosNaming.Binding;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.CosNaming.NamingContextPackage.AlreadyBound;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Naming Context Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link jp.go.aist.rtm.nameserviceview.model.nameservice.impl.NamingContextNodeImpl#getNameServerName <em>Name Server Name</em>}</li>
 *   <li>{@link jp.go.aist.rtm.nameserviceview.model.nameservice.impl.NamingContextNodeImpl#isZombie <em>Zombie</em>}</li>
 *   <li>{@link jp.go.aist.rtm.nameserviceview.model.nameservice.impl.NamingContextNodeImpl#getKind <em>Kind</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NamingContextNodeImpl extends CorbaNodeImpl implements
		NamingContextNode {
	/**
	 * The default value of the '{@link #getNameServerName() <em>Name Server Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNameServerName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_SERVER_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNameServerName() <em>Name Server Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNameServerName()
	 * @generated
	 * @ordered
	 */
	protected String nameServerName = NAME_SERVER_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #isZombie() <em>Zombie</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isZombie()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ZOMBIE_EDEFAULT = false;

	/**
	 * The default value of the '{@link #getKind() <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected static final String KIND_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getKind() <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected String kind = KIND_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public NamingContextNodeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NameservicePackage.Literals.NAMING_CONTEXT_NODE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getNameServerName() {
		if (nameServerName == null && getNameServiceReference() != null)
			return getNameServiceReference().getNameServerName();
		return nameServerName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNameServerName(String newNameServerName) {
		String oldNameServerName = nameServerName;
		nameServerName = newNameServerName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NameservicePackage.NAMING_CONTEXT_NODE__NAME_SERVER_NAME, oldNameServerName, nameServerName));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isZombie() {
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getKind() {
		return kind;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKind(String newKind) {
		String oldKind = kind;
		kind = newKind;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NameservicePackage.NAMING_CONTEXT_NODE__KIND, oldKind, kind));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case NameservicePackage.NAMING_CONTEXT_NODE__NAME_SERVER_NAME:
				return getNameServerName();
			case NameservicePackage.NAMING_CONTEXT_NODE__ZOMBIE:
				return isZombie() ? Boolean.TRUE : Boolean.FALSE;
			case NameservicePackage.NAMING_CONTEXT_NODE__KIND:
				return getKind();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case NameservicePackage.NAMING_CONTEXT_NODE__NAME_SERVER_NAME:
				setNameServerName((String)newValue);
				return;
			case NameservicePackage.NAMING_CONTEXT_NODE__KIND:
				setKind((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case NameservicePackage.NAMING_CONTEXT_NODE__NAME_SERVER_NAME:
				setNameServerName(NAME_SERVER_NAME_EDEFAULT);
				return;
			case NameservicePackage.NAMING_CONTEXT_NODE__KIND:
				setKind(KIND_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case NameservicePackage.NAMING_CONTEXT_NODE__NAME_SERVER_NAME:
				return NAME_SERVER_NAME_EDEFAULT == null ? nameServerName != null : !NAME_SERVER_NAME_EDEFAULT.equals(nameServerName);
			case NameservicePackage.NAMING_CONTEXT_NODE__ZOMBIE:
				return isZombie() != ZOMBIE_EDEFAULT;
			case NameservicePackage.NAMING_CONTEXT_NODE__KIND:
				return KIND_EDEFAULT == null ? kind != null : !KIND_EDEFAULT.equals(kind);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class baseClass) {
		if (baseClass == NameServerContext.class) {
			switch (derivedFeatureID) {
				case NameservicePackage.NAMING_CONTEXT_NODE__NAME_SERVER_NAME: return ManagerPackage.NAME_SERVER_CONTEXT__NAME_SERVER_NAME;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class baseClass) {
		if (baseClass == NameServerContext.class) {
			switch (baseFeatureID) {
				case ManagerPackage.NAME_SERVER_CONTEXT__NAME_SERVER_NAME: return NameservicePackage.NAMING_CONTEXT_NODE__NAME_SERVER_NAME;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (nameServerName: ");
		result.append(nameServerName);
		result.append(", kind: ");
		result.append(kind);
		result.append(')');
		return result.toString();
	}

	@SuppressWarnings("unused")
	private Binding createLastBinding(Binding binding) {
		Binding result = new Binding();
		result.binding_type = binding.binding_type;
		result.binding_name = new NameComponent[] { binding.binding_name[binding.binding_name.length - 1] };

		return result;
	}

	public static final MappingRule MAPPING_RULE = new MappingRule(
			null,
			new ClassMapping(
					NamingContextNodeImpl.class,
					new ConstructorParamMapping[] { new ConstructorParamMapping(
							NamingContext.class, CorePackage.eINSTANCE
									.getCorbaWrapperObject_CorbaObject()) }) {
				@Override
				public LocalObject createLocalObject(LocalObject parent,
						Object[] remoteObjects, java.lang.Object link) {
					NamingContextNode result = (NamingContextNode)super.createLocalObject(parent,
							remoteObjects, link);
					Binding binding = (Binding) link;
					result.setNameServiceReference(((CorbaNode) parent)
									.getNameServiceReference()
									.createMergedNameServiceReference(
											binding));
					NameComponent[] nameComponent = binding.binding_name;
					result.setKind(nameComponent[nameComponent.length-1].kind);
					return result;
				}
			}, new AttributeMapping[] {},
			new ReferenceMapping[] { new ManyReferenceMapping(
					ManagerPackage.eINSTANCE.getNode_Nodes(),
					true) {
				private NamingContext context;
				private LocalObject localObject;
				
				@SuppressWarnings("unchecked")
				@Override
				public List getNewRemoteLinkList(Object[] remoteObjects) {
					context = (NamingContext) remoteObjects[0];
					List<Binding> bindingList = CorbaUtil.getBindingList(context);

					return bindingList;
				}

				public boolean isLinkEquals(java.lang.Object o1,
						java.lang.Object o2) {
					Binding o12 = (Binding) o1;
					Binding o22 = (Binding) o2;
					boolean result = CorbaNodeImpl.COMARATOR.compare(o12,o22) == 0;
					if (!result) return false;
					try {
						org.omg.CORBA.Object newObj = context.resolve(o12.binding_name);
						CorbaNode oldLocal = (CorbaNode) getLocalObjectByRemoteLink(localObject, o12);
						return newObj._is_equivalent(oldLocal.getCorbaObject());
					} catch (Exception e) {
						return true;
					}
				}

				@Override
				protected void dubugPrint(LocalObject localObject) {
				}

				@Override
				public Object[] getRemoteObjectByRemoteLink(
						LocalObject localObject, Object[] remoteObjects,
						java.lang.Object link) {
					try {
						NamingContext namingContextExt = (NamingContext) remoteObjects[0];
						NameComponent[] bindingName = ((Binding) link).binding_name;
						org.omg.CORBA.Object resolve = namingContextExt.resolve(bindingName);
						return new Object[] { resolve };
					} catch (NotFound e) {
						throw new RuntimeException(); // systemError
					} catch (CannotProceed e) {
						throw new RuntimeException(); // systemError
					} catch (InvalidName e) {
						throw new RuntimeException(); // systemError
					}
				}

				@SuppressWarnings("unchecked")
				@Override
				public List getOldRemoteLinkList(LocalObject localObject) {
					this.localObject = localObject;
					List<Binding> result = new ArrayList<Binding>();
					for (Iterator iter = ((EList) localObject
							.eGet(getLocalFeature())).iterator(); iter
							.hasNext();) {
						CorbaNode bindNode = (CorbaNode) iter.next();

						Binding binding = bindNode.getNameServiceReference()
								.getBinding();

						Binding lastBinding = new Binding();
						lastBinding.binding_name = new NameComponent[] { binding.binding_name[binding.binding_name.length - 1] };
						lastBinding.binding_type = binding.binding_type;

						result.add(lastBinding);
					}

					return result;
				}

				@SuppressWarnings("unchecked")
				@Override
				public LocalObject getLocalObjectByRemoteLink(
						LocalObject parent, Object link) {
					for (Iterator iter = ((EList) parent
							.eGet(getLocalFeature())).iterator(); iter
							.hasNext();) {
						CorbaNode bindNode = (CorbaNode) iter.next();

						Binding binding = bindNode.getNameServiceReference()
								.getBinding();

						Binding lastBinding = new Binding();
						lastBinding.binding_name = new NameComponent[] { binding.binding_name[binding.binding_name.length - 1] };
						lastBinding.binding_type = binding.binding_type;

						if (CorbaNodeImpl.COMARATOR.compare((Binding)link, (Binding)lastBinding) == 0) {
							return bindNode;
						}
					}

					return null;
				}

				@Override
				public LocalObject loadLocalObjectByRemoteObject(
						LocalObject localObject,
						SynchronizationManager synchronizationManager,
						Object link, Object[] remoteObject) {
					return synchronizationManager.createLocalObject(localObject,
							remoteObject, link, false);
				}

			} });

	/* (non-Javadoc)
	 * @see jp.go.aist.rtm.nameserviceview.model.nameservice.impl.NodeImpl#getCorbaObjectInterface()
	 */
	public NamingContext getCorbaObjectInterface() {
		return NamingContextHelper.narrow(super.getCorbaObject());
	}

	/* (non-Javadoc)
	 * @see jp.go.aist.rtm.nameserviceview.model.nameservice.NamingContextNode#createContextR(org.omg.CosNaming.NameComponent[])
	 */
	public void createContextR(NameComponent[] path) throws NotFound,
			AlreadyBound, CannotProceed, InvalidName {
		getCorbaObjectInterface().bind_new_context(path);
	}

	/* (non-Javadoc)
	 * @see jp.go.aist.rtm.nameserviceview.model.nameservice.NamingContextNode#createNamingObjectR(org.omg.CosNaming.NameComponent[], org.omg.CORBA.Object)
	 */
	public void createNamingObjectR(NameComponent[] path,
			org.omg.CORBA.Object object) throws NotFound, AlreadyBound,
			CannotProceed, InvalidName {
		getCorbaObjectInterface().bind(path, object);
	}
	
	public static final MappingRule MAPPING_RULE_NAMESERVER = new MappingRule(
			NamingContextNodeImpl.MAPPING_RULE,
			new ClassMapping(
					NamingContextNodeImpl.class,
					new ConstructorParamMapping[] { 
						new ConstructorParamMapping(
							NamingContext.class,
							CorePackage.eINSTANCE.getCorbaWrapperObject_CorbaObject())
						,new ConstructorParamMapping(
							NameServiceReference.class,
							NameservicePackage.eINSTANCE.getCorbaNode_NameServiceReference()) }
			) {
				@Override
				public boolean isTarget(LocalObject parent,
						Object[] remoteObjects, java.lang.Object link) {
					boolean result = false;
					if (super.isTarget(parent, remoteObjects, link)
							&& parent == null) {
						result = true;
					}

					return result;
				}
				@Override
				public LocalObject createLocalObject(LocalObject parent,
						Object[] remoteObjects, java.lang.Object link) {
					NamingContextNode result = (NamingContextNode)super.createLocalObject(parent,
							remoteObjects, link);
					result.setKind(KIND_SERVER);
					return result;
				}

			}, new AttributeMapping[] {}, new ReferenceMapping[] {});

} // NamingContextNodeImpl
