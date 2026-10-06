package jp.go.aist.rtm.toolscommon.corba;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jp.go.aist.rtm.toolscommon.manager.ToolsCommonPreferenceManager;
import jp.go.aist.rtm.toolscommon.model.core.CorbaWrapperObject;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.Binding;
import org.omg.CosNaming.BindingIteratorHolder;
import org.omg.CosNaming.BindingListHolder;
import org.omg.CosNaming.NamingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CORBAに関するユーティリティ
 */
public class CorbaUtil {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CorbaUtil.class);

	private static ORB orb = null;
	
	private static Properties createProps() {
	    Properties props = new Properties();
	    
	    props.put("org.omg.CORBA.ORBClass", "com.sun.corba.se.impl.orb.ORBImpl");
	    props.put("org.omg.CORBA.ORBSingletonClass", "com.sun.corba.se.impl.orb.ORBSingleton");
	    
	    return props;
	}

	public static synchronized ORB getOrb() {
        if (orb == null) {
            try {
                LOGGER.info("Initializing ORB with openjdk-orb...");
                orb = ORB.init(new String[] {}, createProps());
                if (orb != null) {
                    try {
                        Method m = orb.getClass().getMethod("getLogger", String.class);
                        java.util.logging.Logger l = (java.util.logging.Logger) m.invoke(orb, "");
                        l.setLevel(java.util.logging.Level.SEVERE);
                    } catch (Exception e) {
                        LOGGER.trace("ORB getLogger not supported, skipping.");
                    }
                }
            } catch(Exception ex) {
            }
        }
        return orb;
    }
	/**
	 * 対象のNamingContextExtから子供のBindingをListとして返す
	 * 
	 * @param target 対象のNamingContextExt
	 * @return 子供のBindingのList
	 */
	public static List<Binding> getBindingList(NamingContext target) {
		BindingListHolder bindingListHolder = new BindingListHolder();
		BindingIteratorHolder bindingIteratorHolder = new BindingIteratorHolder();
		List<Binding> result = new ArrayList<>();

		try {
			target.list(9999, bindingListHolder, bindingIteratorHolder);
		} catch (org.omg.CORBA.COMM_FAILURE | org.omg.CORBA.OBJECT_NOT_EXIST e) {
			LOGGER.trace("getBindingList: CORBA unreachable: {} {}", e.getClass().getName(), e.getMessage());
			return result;
		} catch (Exception e) {
			LOGGER.error("Fail to get binding list: nc={}", target);
			LOGGER.error("ERROR:", e);
			return result;
		}
		for (int i = 0; i < bindingListHolder.value.length; i++) {
			Binding binding = bindingListHolder.value[i];
			result.add(binding);
		}
		return result;
	}

	private static void setConnectionTimeout() {
		int defaultTimeout = ToolsCommonPreferenceManager.getInstance().getDefaultTimeout(
				ToolsCommonPreferenceManager.DEFAULT_TIMEOUT_PERIOD);
		TimeoutCorbaORBSocketFactory.setConnectionTimeout(defaultTimeout);
	}

	private static PropertyChangeListener createListner() {
		return new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setConnectionTimeout();
			}
		};
	}

	/**
	 * CORBAオブジェクト（プロキシ）をシリアライズした文字列から、CORBAオブジェクトへ変換する
	 * 
	 * @param str
	 *            CORBAオブジェクト（プロキシ）をシリアライズした文字列
	 * @return CORBAオブジェクト
	 */
	public static org.omg.CORBA.Object stringToObject(String str) {
		return getOrb().string_to_object(str);
	}

	/**
	 * CORBAラッパからCORBAオブジェクトを取得します。
	 * 
	 * @param wrapper
	 *            CORBAラッパ
	 * @return CORBAオブジェクト
	 */
	public static org.omg.CORBA.Object getCorbaObject(CorbaWrapperObject wrapper) {
		if (wrapper == null) {
			return null;
		}
		return wrapper.getCorbaObject();
	}

	/**
	 * CORBAオブジェクトからIORオブジェクトを取得します。
	 * 
	 * @param obj
	 *            CORBAオブジェクト
	 * @return IORオブジェクト
	 */
	public static com.sun.corba.se.spi.ior.IOR getIOR(org.omg.CORBA.Object obj) {
		return com.sun.corba.se.impl.orbutil.ORBUtility.getIOR(obj);
	}

	/**
	 * CORBAオブジェクトからIOR情報を取得します。
	 * 
	 * @param obj
	 *            CORBAオブジェクト
	 * @return IOR情報
	 */
	public static IORInfo getIORInfo(org.omg.CORBA.Object obj) {
		return parseIOR(getIOR(obj));
	}

	/**
	 * IOR情報を解析します。
	 * 
	 * @param ior
	 *            IORオブジェクト
	 * @return IOR情報
	 */
	public static IORInfo parseIOR(com.sun.corba.se.spi.ior.IOR ior) {
		IORInfo ret = new IORInfo();
		ret.ior = ior.stringify();
		ret.typeId = ior.getTypeId();

		IORInfo.TaggedProfile prof = new IORInfo.TaggedProfile();
		ret.taggedProfiles.add(prof);
		com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate profTmpl = (com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate) ior
				.getProfile().getTaggedProfileTemplate();
		prof.host = profTmpl.getPrimaryAddress().getHost().toLowerCase();
		prof.port = Integer.toString(profTmpl.getPrimaryAddress().getPort());
		prof.gpioVersion = profTmpl.getGIOPVersion().toString();

		String objKey = "";
		byte[] bb = ior.getProfile().getObjectKey().getId().getId();
		for (byte b : bb) {
			if (!objKey.isEmpty()) {
				objKey += " ";
			}
			objKey += String.format("%02x", b);
		}
		prof.objKey = objKey;

		for (Object o : profTmpl.toArray()) {
			if (o instanceof com.sun.corba.se.spi.ior.iiop.CodeSetsComponent) {
				prof.components.add(
						((com.sun.corba.se.spi.ior.iiop.CodeSetsComponent) o).getCodeSetComponentInfo().toString());
			} else if (o instanceof com.sun.corba.se.spi.ior.iiop.ORBTypeComponent) {
				prof.components.add(((com.sun.corba.se.spi.ior.iiop.ORBTypeComponent) o).toString());
			} else if (o instanceof com.sun.corba.se.spi.ior.iiop.AlternateIIOPAddressComponent) {
				prof.components.add(((com.sun.corba.se.spi.ior.iiop.AlternateIIOPAddressComponent) o).toString());
			} else if (o instanceof com.sun.corba.se.spi.ior.iiop.JavaCodebaseComponent) {
				prof.components.add(((com.sun.corba.se.spi.ior.iiop.JavaCodebaseComponent) o).toString());
			} else if (o instanceof com.sun.corba.se.spi.ior.iiop.MaxStreamFormatVersionComponent) {
				prof.components.add(((com.sun.corba.se.spi.ior.iiop.MaxStreamFormatVersionComponent) o).toString());
			} else if (o instanceof com.sun.corba.se.spi.ior.iiop.RequestPartitioningComponent) {
				prof.components.add(((com.sun.corba.se.spi.ior.iiop.RequestPartitioningComponent) o).toString());
			}
		}
		return ret;
	}

	/**
	 * IOR情報を表します。
	 */
	public static class IORInfo {

		public String ior;
		public String typeId;
		public List<TaggedProfile> taggedProfiles = new ArrayList<>();

		@Override
		public String toString() {
			return this.getClass().getSimpleName() + "<" + this.ior + "|" + this.typeId + "|" + this.taggedProfiles
					+ ">";
		}

		public static class TaggedProfile {
			public String host;
			public String port;
			public String gpioVersion;
			public String objKey;
			public List<String> components = new ArrayList<>();

			@Override
			public String toString() {
				return this.getClass().getSimpleName() + "<" + this.host + ":" + this.port + "|" + this.gpioVersion
						+ "|" + this.objKey + "|" + this.components + ">";
			}
		}

	}

}
