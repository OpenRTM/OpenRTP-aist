package jp.go.aist.rtm.toolscommon.model.component;

import jp.go.aist.rtm.toolscommon.model.core.CorbaWrapperObject;

/**
 * Portを表現するクラス
 * @model
 */
public interface Port extends ConnectorSource, ConnectorTarget, CorbaWrapperObject {
	/**
	 * @model containment="true"
	 */
	public PortProfile getPortProfile();

	void setPortProfile(PortProfile value);

	public RTC.PortService getCorbaObjectInterface();
}
