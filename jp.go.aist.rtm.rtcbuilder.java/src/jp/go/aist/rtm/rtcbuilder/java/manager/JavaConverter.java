package jp.go.aist.rtm.rtcbuilder.java.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.fsm.StateParam;
import jp.go.aist.rtm.rtcbuilder.fsm.TransitionParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.ConfigSetParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.DataPortParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.RtcParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.ServiceArgumentParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.ServiceClassParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.ServiceMethodParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.TypeDefParam;

/**
 * Javaソースを出力する際に使用されるユーティリティ
 */
public class JavaConverter {
	protected Map<String, String> mapType;
	protected Map<String, String> mapTypeHolder;
	protected Map<String, String> mapParamHolder;

	private final String dirIn = "in";
//	private final String dirOut = "out";
//	private final String dirInOut = "inout";

	private final String idlLongLong = "longlong";
	private final String idlLong = "long";
	private final String idlUnsignedLong = "unsignedlong";
	private final String idlUnsignedLongLong = "unsignedlonglong";
	private final String idlShort = "short";
	private final String idlUnsignedShort = "unsignedshort";
	private final String idlFloat = "float";
	private final String idlDouble = "double";
	private final String idlLongDouble = "longdouble";
	private final String idlChar = "char";
	private final String idlWchar = "wchar";
	private final String idlOctet = "octet";
	private final String idlBoolean = "boolean";
	private final String idlString = "string";
	private final String idlWstring = "wstring";
	private final String idlAny = "any";
	private final String idlVoid= "void";
	//
	private final String javaLongLong = "long";
	private final String javaLong = "int";
	private final String javaUnsignedLong = "int";
	private final String javaUnsignedLongLong = "long";
	private final String javaShort = "short";
	private final String javaUnsignedShort = "short";
	private final String javaFloat = "float";
	private final String javaDouble = "double";
	private final String javaLongDouble = "double";
	private final String javaChar = "char";
	private final String javaWchar = "char";
	private final String javaOctet = "byte";
	private final String javaBoolean = "boolean";
	private final String javaStringS = "string";
	private final String javaString = "String";
	private final String javaWstring = "String";
	private final String javaAny = "org.omg.CORBA.Any";
	private final String javaVoid= "void";
	//
	private final String javaInt = "int";
	private final String javaByte = "byte";
	//
	private final String javaShortHolder = "org.omg.CORBA.ShortHolder";
	private final String javaLongHolder = "org.omg.CORBA.IntHolder";
	private final String javaLongLongHolder = "org.omg.CORBA.LongHolder";
	private final String javaUnsignedLongHolder = "org.omg.CORBA.IntHolder";
	private final String javaUnsignedLongLongHolder = "org.omg.CORBA.LongHolder";
	private final String javaUnsignedShortHolder = "org.omg.CORBA.ShortHolder";
	private final String javaFloatHolder = "org.omg.CORBA.FloatHolder";
	private final String javaDoubleHolder = "org.omg.CORBA.DoubleHolder";
	private final String javaCharHolder = "org.omg.CORBA.CharHolder";
	private final String javaWcharHolder = "org.omg.CORBA.CharHolder";
	private final String javaOctetHolder = "org.omg.CORBA.ByteHolder";
	private final String javaBooleanHolder = "org.omg.CORBA.BooleanHolder";
	private final String javaStringHolder = "org.omg.CORBA.StringHolder";
	private final String javaWstringHolder = "org.omg.CORBA.StringHolder";
	private final String javaAnyHolder = "org.omg.CORBA.AnyHolder";
	private final String javaLongDoubleHolder = "org.omg.CORBA.DoubleHolder";
	//
	private final String javaShortParam = "ShortHolder";
	private final String javaIntParam = "IntegerHolder";
	private final String javaLongParam = "LongHolder";
	private final String javaFloatParam = "FloatHolder";
	private final String javaDoubleParam = "DoubleHolder";
	private final String javaByteParam = "ByteHolder";
	private final String javaStringParam = "StringHolder";
//	private final String javaLongDouble = "double";

	public JavaConverter() {
		mapType = new HashMap<String, String>();
		mapType.put(idlLongLong, javaLongLong);
		mapType.put(idlLong, javaLong);
		mapType.put(idlUnsignedLong, javaUnsignedLong);
		mapType.put(idlUnsignedLongLong, javaUnsignedLongLong);
		mapType.put(idlShort, javaShort);
		mapType.put(idlUnsignedShort, javaUnsignedShort);
		mapType.put(idlFloat, javaFloat);
		mapType.put(idlDouble, javaDouble);
		mapType.put(idlLongDouble, javaLongDouble);
		mapType.put(idlChar, javaChar);
		mapType.put(idlWchar, javaWchar);
		mapType.put(idlOctet, javaOctet);
		mapType.put(idlBoolean, javaBoolean);
		mapType.put(idlString, javaString);
		mapType.put(idlWstring, javaWstring);
		mapType.put(idlAny, javaAny);
		mapType.put(idlVoid, javaVoid);
		//
		mapTypeHolder = new HashMap<String, String>();
		mapTypeHolder.put(idlLongLong, javaLongLongHolder);
		mapTypeHolder.put(idlLong, javaLongHolder);
		mapTypeHolder.put(idlUnsignedLong, javaUnsignedLongHolder);
		mapTypeHolder.put(idlUnsignedLongLong, javaUnsignedLongLongHolder);
		mapTypeHolder.put(idlShort, javaShortHolder);
		mapTypeHolder.put(idlUnsignedShort, javaUnsignedShortHolder);
		mapTypeHolder.put(idlFloat, javaFloatHolder);
		mapTypeHolder.put(idlDouble, javaDoubleHolder);
		mapTypeHolder.put(idlChar, javaCharHolder);
		mapTypeHolder.put(idlWchar, javaWcharHolder);
		mapTypeHolder.put(idlOctet, javaOctetHolder);
		mapTypeHolder.put(idlBoolean, javaBooleanHolder);
		mapTypeHolder.put(idlString, javaStringHolder);
		mapTypeHolder.put(idlWstring, javaWstringHolder);
		mapTypeHolder.put(idlAny, javaAnyHolder);
		mapTypeHolder.put(idlLongDouble, javaLongDoubleHolder);
		//
		mapParamHolder = new HashMap<String, String>();
		mapParamHolder.put(javaShort, javaShortParam);
		mapParamHolder.put(javaInt, javaIntParam);
		mapParamHolder.put(javaLongLong, javaLongParam);
		mapParamHolder.put(javaFloat, javaFloatParam);
		mapParamHolder.put(javaDouble, javaDoubleParam);
		mapParamHolder.put(javaByte, javaByteParam);
		mapParamHolder.put(javaString, javaStringParam);
		mapParamHolder.put(javaStringS, javaStringParam);
	}

	/**
	 * Portに設定された型の一覧を取得する
	 * 
	 * @param param  RtcParam
	 * @return 型一覧リスト
	 */
	public List<String> getPortTypes(RtcParam param) {
		List<String> portTypes = new ArrayList<String>();
		for (DataPortParam dataPort : param.getInports()) {
			if (!portTypes.contains(dataPort.getType())) {
				portTypes.add(dataPort.getType());
			}
		}
		for (DataPortParam dataPort : param.getOutports()) {
			if (!portTypes.contains(dataPort.getType())) {
				portTypes.add(dataPort.getType());
			}
		}
		return portTypes;
	}

	public List<String> getEventTypes(StateParam param) {
		List<String> eventTypes = new ArrayList<String>();
		for (TransitionParam trans : param.getAllTransList()) {
			if(trans.existDataType()==false) continue;
			
			if (!eventTypes.contains(trans.getDataType())) {
				eventTypes.add(trans.getDataType());
			}
		}
		return eventTypes;
	}
	
	public List<String> getEachEventTypes(StateParam param) {
		List<String> eventTypes = new ArrayList<String>();
		for (TransitionParam trans : param.getTransList()) {
			if(trans.existDataType()==false) continue;
			
			if (!eventTypes.contains(trans.getDataType())) {
				eventTypes.add(trans.getDataType());
			}
		}
		return eventTypes;
	}
	/**
	 * パラメータの型一覧を取得する
	 * 
	 * @param param  RtcParam
	 * @return パラメータ型一覧リスト
	 */
	public List<String> getParamTypes(RtcParam param) {
		List<String> paramTypes = new ArrayList<String>();
		for( ConfigSetParam config : param.getConfigParams() ) {
			String paramType = convJava2ParamHolder(config.getType(),false);
			if( paramType!=null && !paramTypes.contains(paramType) ) {
				paramTypes.add(paramType);
			}
		}
		return paramTypes;
	}
	/**
	 * CORBA型からJava型へ型を変換する(TypeDef考慮)
	 * 
	 * @param strCorba CORBA型
	 * @return Java型
	 */
	public String convCORBA2Java(ServiceMethodParam typeDef, ServiceClassParam scp) {
		String strType = getTypeDefs(typeDef.getType(), scp);
		if( strType==null ) {
			strType = typeDef.getType();
		} else {
			strType = strType.replaceAll("::", ".");
		}
		
		String rawType = strType.replaceAll("\\[\\]", "");
		String convType = mapType.get(rawType);
		
		String result;
		if( convType == null ) {
			if(typeDef.isSequence() ) {
				result = strType;
				
			} else {
				if(typeDef.isAlias()){
					result = typeDef.getOriginalType();
				}else{
					result = typeDef.getType();
				}
				result = result.replaceAll(scp.getName()+"::", "");

				if(typeDef.isArray()){
					for(int i=0; i< typeDef.getArrayDim() ; i++){
				       		result += "[]";
					}
				}
			}
			String mdl = typeDef.getModule();
			if(mdl.length() > 0){ result = mdl + result; }
			result=result.replaceAll("::", ".");

		} else {
			result = strType.replaceAll(rawType, convType);
			
		}
		
		return result;
	}

	private String getTypeDefs(String target, ServiceClassParam scp) {
		String result = null;
		target = target.replaceAll(scp.getName()+"::", "");
		
		TypeDefParam source = scp.getTypeDef().get(target);
		if( source==null || source.getOriginalDef()==null || source.getOriginalDef().length()==0 ) {
			return target;
		} else {
			result = getTypeDefs(source.getOriginalDef(), scp);
			if( source!=null ) {
				if( source.isSequence()  ){
				       	result += "[]";
				}else if(  source.isArray() ){
					for(int i=0; i< source.getArrayDim() ; i++){
				       		result += "[]";
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * CORBA型からJava型へ型を変換する
	 * 
	 * @param strCorba CORBA型
	 * @return Java型
	 */
	public String convCORBA2JavaNoDef(String strCorba) {
		String result = mapType.get(strCorba);
		if( result == null ) result = strCorba;
		return result;
	}
	/**
	 * CORBA型からJava型へ型を変換する(引数用,TypeDef考慮)
	 * 
	 * @param strCorba CORBA型
	 * @param strDirection 入出力方向
	 * @return Java型
	 */
	public String convCORBA2JavaforArg(ServiceArgumentParam typeDef, String strDirection, ServiceClassParam scp) {
		String result = "";
		String strType = getTypeDefs(typeDef.getType(), scp);
		if(strType.endsWith("[]")) {
			typeDef.setSequence(true);
		}
		strType = strType.replaceAll("::", ".");
		

		if( mapType.get(strType) != null){
			if( strDirection.equals(dirIn) ) {
				result = mapType.get(strType);
			} else {
				result = mapTypeHolder.get(strType);
			}
			return result;

		}else if( typeDef.getType().equals(strType) ) {
			if( strDirection.equals(dirIn) ) {
				result = mapType.get(typeDef.getType());
				if( result == null ) result = typeDef.getType();
			} else {
				result = mapTypeHolder.get(typeDef.getType());
				if( result == null ) result = typeDef.getType() + "Holder";
			}

		}else if( typeDef.isArray() || typeDef.isSequence() ) {

			if( !strDirection.equals(dirIn) ) {
				result = typeDef.getType() + "Holder";
			}else{
				String rawType = strType.replaceAll("\\[\\]", "");
				String convType = mapType.get(rawType);

				if( convType != null ) {
					result = strType.replaceAll(rawType, convType);
					return result;
				}else{
					result = strType;
				}
			}

		}else if( typeDef.isStruct() ) {
			result = strType;
			if( !strDirection.equals(dirIn) ){
			       	result = result + "Holder";
			}

		} else {

			if( typeDef.isEnum()) {
				result = typeDef.getType();
			} else {
				result = strType;
			}

			if( !strDirection.equals(dirIn) ){
				if( typeDef.isUnbounded() ) {
					result = typeDef.getType() + "Holder";
				}else{
					result = result + "Holder";
				}
			}

		}

	 	if(typeDef.getModule().length() == 0){
			result = result.replaceAll("::", "Package.");
		}else{
	  	result = typeDef.getModule() + result;
			result = result.replaceAll("::", ".");
		}

		return result;
	}
	/**
	 * CORBA型からJava型へ型を変換する(引数用)
	 * 
	 * @param strCorba CORBA型
	 * @param strDirection 入出力方向
	 * @return Java型
	 */
	public String convCORBA2JavaforArg(String strCorba, String strDirection) {
		String result = "";
		if( strDirection.equals(dirIn) ) {
			result = mapType.get(strCorba);
			
		} else {
			result = mapTypeHolder.get(strCorba);
		}
		if( result == null ) result = strCorba;
		return result;
	}
	/**
	 * Java型からパラメータ用ホルダ型へ型を変換する
	 * 
	 * @param strJava Java型
	 * @return パラメータ用ホルダ型
	 */
	public String convJava2ParamHolder(String strJava, boolean isNullAdd) {
		String result = mapParamHolder.get(strJava);
		if( isNullAdd && result == null ) result = strJava + "Holder";
		return result;
	}
	/**
	 * String型か判断する
	 * 
	 * @param srvMethod 検証対象メソッド
	 * @return 検証結果
	 */
	public boolean isRetNull(ServiceMethodParam srvMethod, ServiceClassParam scp) {
		if(srvMethod.isStruct()) return true;
		String conv = this.convCORBA2Java(srvMethod, scp);
		if(conv.equals(javaString) || conv.equals(javaWstring) || conv.equals(javaAny) || conv.endsWith("[]") )
			return true;
		return false;
	}
	
	public boolean isRetNumber(ServiceMethodParam srvMethod, ServiceClassParam scp) {
		String sType = getTypeDefs(srvMethod.getType(), scp);
		String strType=mapType.get(sType);

		if( strType==null ) {
			return false;
		} else {
		  if( strType.equals("boolean") || strType.equals("string") || strType.equals("String") || strType.equals("org.omg.CORBA.Any") || strType.equals("void") ) {
			return false;
			}
		}
		return true;
	}

	public boolean isRetBoolean(ServiceMethodParam srvMethod, ServiceClassParam scp) {
		if( srvMethod.isArray() || srvMethod.isSequence() ) {
			return false;
		}
		String sType = getTypeDefs(srvMethod.getType(), scp);
		String strType=mapType.get(sType);

		if( strType != null && strType.equals("boolean") ){
			return true;
		}
		if( srvMethod.isAlias() ) {
			sType = getTypeDefs(srvMethod.getOriginalType(), scp);
			strType=mapType.get(sType);
			if( strType != null && strType.equals("boolean") ){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * データポート用のデータ型import文を返す
	 * 
	 * @param rtcType ポートの型
	 * @return import文字列
	 */
	public String getDataportPackageName(String rtcType) {
		//module名が付いていないデータ型（::が付いていない）はimport文を生成しない
		if(!rtcType.matches(".*::.*")) return "";
		
		//module名=パッケージ名
		//struct名=クラス名
		String importDef = "import " + rtcType.replace("::", ".") + ";";
		return importDef;
	}
	
	/**
	 * データポート初期化用にmodule名をカットしたデータ型クラス名を返す
	 * 
	 * @param rtcType ポートの型
	 * @return クラス名
	 */
	public String getDataTypeName(String rtcType) {
		
		//module名が付いていないデータ型（::が付いていない）はそのまま返す
		if(!rtcType.matches(".*::.*")) return rtcType;

		String dataTypeNames[] = rtcType.split("::", 0);
		return dataTypeNames[1];
	}

	/**
	 * RTC.ReturnCode_tのインポートが必要な場合を判定します。
	 * 
	 * @param rtcParam
	 *            RtcParam
	 * @return RTC.ReturnCode_tのインポートが必要な場合は true
	 */
	public boolean useReturnCode(RtcParam rtcParam) {
		if( rtcParam.getInports().size()>0 || rtcParam.getOutports().size()>0 ||
				rtcParam.getServicePorts().size()>0 ) {
			return true;
		}
		if (!rtcParam.getConfigParams().isEmpty()) {
			return true;
		}
		for (int i = IRtcBuilderConstants.ACTIVITY_INITIALIZE; i < IRtcBuilderConstants.ACTIVITY_DUMMY; i++) {
			if (!rtcParam.IsNotImplemented(i)) {
				return true;
			}
		}
		return false;
	}

}
