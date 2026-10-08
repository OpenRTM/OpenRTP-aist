package jp.go.aist.rtm.rtcbuilder.manager;

import java.util.List;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.ParamBase;
import jp.go.aist.rtm.rtcbuilder.generator.GeneratedResult;
import jp.go.aist.rtm.rtcbuilder.generator.param.RtcParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.IdlFileParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.ServiceClassParam;
import jp.go.aist.rtm.rtcbuilder.ui.Perspective.LanguageProperty;

public abstract class GenerateManager {
	public static String RTC_PROFILE_SERVICE_PORTS_INAPPLICABLE = "RTC_PROFILE_SERVICE_PORTS_INAPPLICABLE";

	// 生成対象言語
	public abstract String getManagerKey();

	// 生成対象言語名称(引数用)
	public abstract String getLangArgList();

	// スケルトンコードの生成
	public abstract List<GeneratedResult> generateTemplateCode(ParamBase rtcParam);

	public abstract String getTargetMiddleware();

	// 生成対象言語用開発プラグイン情報の取得
	public LanguageProperty getLanguageProperty(ParamBase rtcParam) {
		return null;
	}

	// 生成対象言語名称(引数用)
	public String getTargetVersion() {
		return IRtcBuilderConstants.DEFAULT_RTM_VERSION;
	}

	// 特定のタグを適用対象外とするか否か
	public List<String> getInapplicables() {
		// 適用対象外とする場合は、オーバーライドして上で
		// 定義されている定数をListとして返す
		return null;
	}

	// Profileデータ変換用
	public void convertProfile(Object profile) {
		//
	}

	// IDLファイル内に記述されているServiceClassParamを設定する
	public void resetIDLServiceClass(RtcParam rtcParam) {
		for (IdlFileParam idl : rtcParam.getProviderIdlPathes()) {
			for (ServiceClassParam svc : rtcParam.getServiceClassParams()) {
				if (idl.getIdlPath().equals(svc.getIdlPath())) {
					if (!idl.getServiceClassParams().contains(svc)) {
						idl.addServiceClassParams(svc);
					}
				}
			}
		}
		for (IdlFileParam idl : rtcParam.getConsumerIdlPathes()) {
			for (ServiceClassParam svc : rtcParam.getServiceClassParams()) {
				if (idl.getIdlPath().equals(svc.getIdlPath())) {
					if (!idl.getTestServiceClassParams().contains(svc)) {
						idl.addTestServiceClassParams(svc);
					}
				}
			}
		}
	}

}
