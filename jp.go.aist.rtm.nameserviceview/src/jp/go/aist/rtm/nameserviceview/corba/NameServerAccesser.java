package jp.go.aist.rtm.nameserviceview.corba;

import java.util.ArrayList;
import java.util.List;

import org.omg.CosNaming.Binding;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import RTM.ManagerHelper;
import jp.go.aist.rtm.nameserviceview.manager.NameServiceViewPreferenceManager;
import jp.go.aist.rtm.toolscommon.corba.CorbaUtil;

/**
 * ネームサーバにアクセスするユーティリティ CORBA専用のクラスである
 */
public class NameServerAccesser {
	/**
	 * シングルトンインスタンス
	 */
	private static NameServerAccesser __instance = new NameServerAccesser();

	static org.omg.PortableServer.POA rootpoa = null;

	/**
	 * シングルトンへのアクセサ
	 * 
	 * @return シングルトン
	 */
	public static NameServerAccesser getInstance() {
		return __instance;
	}

	/**
	 * アドレスを引数に取り、ネームサーバのルートのNamingContextExtを返す
	 * <p>
	 * 形式は、「address:port」となる。ポートが指定されていない場合には、ユーザ設定ポートを使用する
	 * 
	 * @param address
	 *            ネームサーバのアドレス
	 * @return ネームサーバのルートのNamingContextExt
	 */
	public NamingContextExt getNameServerRootContext(String address) {
		String url = toCORBAURL(address);
		try {
			NamingContextExt ns = NamingContextExtHelper.narrow(CorbaUtil
					.getOrb().string_to_object(url + "/NameService"));
			return ns;
		} catch (Exception e) {
			return null;
		}
	}

	public OpenRTMNaming.NamingNotifier getNamingNotifier(String address) {
		String url = toCORBAURL(address);
		try {
			OpenRTMNaming.NamingNotifier notifier = OpenRTMNaming.NamingNotifierHelper
					.narrow(CorbaUtil.getOrb().string_to_object(
							url + "/OpenRTMNamingNotifier"));
			return notifier;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * アドレスポートから CORBA URLへ変換します。(ポート指定がない場合はデフォルトポートを使用)
	 * 
	 * @param addressport
	 * @return
	 */
	public String toCORBAURL(String addressport) {
		if ("".equals(addressport)) {
			return null;
		}
		if (addressport.indexOf(":") == -1) {
			String defaultPort = NameServiceViewPreferenceManager
					.getInstance()
					.getDefaultPort(
							NameServiceViewPreferenceManager.DEFAULT_CONNECTION_PORT);
			// address = address + ":2809";
			addressport = addressport + ":" + defaultPort;
		}
		String version = "1.0";
		return "corbaloc:iiop:" + version + "@" + addressport;
	}

	/**
	 * サーバントを活性化します。
	 * 
	 * @param servant
	 * @return
	 */
	public org.omg.CORBA.Object activateServant(
			org.omg.PortableServer.Servant servant) {
		if (rootpoa == null) {
			try {
				// RootPOAの参照を取得しPOAManagerを使用可能にします
				rootpoa = org.omg.PortableServer.POAHelper.narrow(CorbaUtil
						.getOrb().resolve_initial_references("RootPOA"));
				rootpoa.the_POAManager().activate();
			} catch (Exception e) {
				throw new RuntimeException("Initialize RootPOA error.", e);
			}
		}
		// サーバントからオブジェクトの参照を取得します
		try {
			org.omg.CORBA.Object servant_ref = rootpoa
					.servant_to_reference(servant);
			return servant_ref;
		} catch (Exception e) {
			throw new RuntimeException("Activate servant error.", e);
		}
	}

	/**
	 * サーバントを不活性化します。
	 * 
	 * @param servant_ref
	 */
	public void deactivateServant(org.omg.CORBA.Object servant_ref) {
		try {
			byte[] oid = rootpoa.reference_to_id(servant_ref);
			rootpoa.deactivate_object(oid);
		} catch (Exception e) {
		}
	}

	/**
	 * PathIdからObjectを取得する
	 * 
	 * @param pathId
	 * @return
	 */
	public org.omg.CORBA.Object getObjectFromPathId(String pathId) {
		try {
			NamingContextExt namingContext = getNameServerRootContext(getNameServerNameFromPathId(pathId));
			return namingContext.resolve(getNameComponentsFromPathId(pathId));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * PathIdからNameServerNameを取得する
	 * 
	 * @param pathId
	 * @return
	 */
	public String getNameServerNameFromPathId(String pathId) {
		return pathId.split("/")[0];
	}

	/**
	 * PathIdからNameComponentを取得する
	 * <p>
	 * ネームサーバ名は除く
	 * 
	 * @param pathId
	 * @return
	 */
	public NameComponent[] getNameComponentsFromPathId(String pathId) {
		List<NameComponent> result = new ArrayList<NameComponent>();
		String[] split = pathId.split("/");
		for (int i = 0; i < split.length; ++i) {
			if (i > 0) {
				int index = split[i].lastIndexOf(".");
				result.add(new NameComponent(split[i].substring(0, index),
						split[i].substring(index + ".".length())));
			}
		}

		return result.toArray(new NameComponent[result.size()]);
	}

	/**
	 * contextからRTM.Managerを取得する
	 * 
	 * @param context
	 * @return
	 */
	public RTM.Manager findManager(NamingContext context) {
		List<Binding> bindingList = CorbaUtil.getBindingList(context);
		for (Binding b : bindingList) {
			try {
				org.omg.CORBA.Object resolve = context.resolve(b.binding_name);
				if (resolve._is_a(ManagerHelper.id()))
					return ManagerHelper.narrow(resolve);
				if (resolve instanceof NamingContext) {
					RTM.Manager temp = findManager((NamingContext) resolve);
					if (temp != null)
						return temp;
				}
			} catch (Exception e) {
				// continue
			}
		}
		return null;
	}

	/**
	 * contextIdからRTM.Managerを取得する
	 * 
	 * @param contextId
	 * @return
	 */
	public RTM.Manager getManagerFromContextId(String contextId) {
		int index = contextId.lastIndexOf("/");
		if (index < 0) {
			return findManager(getNameServerRootContext(getNameServerNameFromPathId(contextId)));
		} else {
			return findManager((NamingContext) getObjectFromPathId(contextId));
		}
	}

}
