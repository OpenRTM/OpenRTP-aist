package jp.go.aist.rtm.rtcbuilder.generator.param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.go.aist.rtm.rtcbuilder.ros.param.ROSParam;

/**
 * ジェネレータの引数となるクラス
 */
public class GeneratorParam implements Serializable {

	private static final long serialVersionUID = -935608504783590375L;
	
	private RtcParam rtcParam;
	private List<DataTypeParam> dataTypeParams = new ArrayList<DataTypeParam>();
	
	private ROSParam rosParam;

	public RtcParam getRtcParam() {
		return rtcParam;
	}
	public void setRtcParam(RtcParam rtcParam) {
		this.rtcParam = rtcParam;
	}

	public List<DataTypeParam> getDataTypeParams() {
		return dataTypeParams;
	}

	public ROSParam getROSParam() {
		return rosParam;
	}
	public void setROSParam(ROSParam rosParam) {
		this.rosParam = rosParam;
	}

	HashMap<String , Object> extensionDatas = new HashMap<String, Object>();

	public HashMap<String, Object> getExtensionDatas() {
		return extensionDatas;
	}
}
