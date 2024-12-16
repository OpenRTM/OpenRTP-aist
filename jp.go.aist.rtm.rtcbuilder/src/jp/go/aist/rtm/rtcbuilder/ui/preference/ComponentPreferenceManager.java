package jp.go.aist.rtm.rtcbuilder.ui.preference;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.RtcBuilderPlugin;

public class ComponentPreferenceManager {
	private static ComponentPreferenceManager __instance = new ComponentPreferenceManager();
	/**
	 * コンストラクタ
	 * 
	 * @return シングルトン
	 */
	public static ComponentPreferenceManager getInstance() {
		return __instance;
	}
	//コード生成
	/**
	 * Module Nameのキー
	 */
	public static final String Generate_Basic_Name = getClassName() + "GENERATE_MODULE_NAME";
	/**
	 * Descriptionのキー
	 */
	public static final String Generate_Basic_Description = getClassName() + "GENERATE_BASIC_DESCRIPTION";
	/**
	 * Versionのキー
	 */
	public static final String Generate_Basic_Version = getClassName() + "GENERATE_BASIC_VERSION";
	/**
	 * Vendor Nameのキー
	 */
	public static final String Generate_Basic_Vendor_Name = getClassName() + "GENERATE_BASIC_VENDOR_NAME";
	/**
	 * Categoryのキー
	 */
	public static final String Generate_Basic_Category = getClassName() + "GENERATE_BASIC_CATEGORY";
	/**
	 * Component Typeのキー
	 */
	public static final String Generate_Basic_ComponentType = getClassName() + "GENERATE_BASIC_COMPONENT_TYPE";
	/**
	 * Activity Typeのキー
	 */
	public static final String Generate_Basic_ActivityType = getClassName() + "GENERATE_BASIC_ACTIVITY_TYPE";
	/**
	 * Max Instanceのキー
	 */
	public static final String Generate_Basic_Max_Instance = getClassName() + "GENERATE_BASIC_MAX_INSTANCES";
	/**
	 * Execution Typeのキー
	 */
	public static final String Generate_Basic_ExecutionType = getClassName() + "GENERATE_BASIC_EXECUTION_TYPE";
	/**
	 * Execution Rateのキー
	 */
	public static final String Generate_Basic_Execution_Rate = getClassName() + "GENERATE_BASIC_EXECUTION_RATE";
	/**
	 * 共通接頭語のキー
	 */
	public static final String Generate_Basic_Prefix = getClassName() + "GENERATE_BASIC_PREFIX";
	/**
	 * 共通接尾語のキー
	 */
	public static final String Generate_Basic_Suffix = getClassName() + "GENERATE_BASIC_SUFFIX";
	////
	/**
	 * Configuration Nameのキー
	 */
	public static final String Generate_Configuration_Name = getClassName() + "GENERATE_CONFIGURATION_NAME";
	/**
	 * Configuration Typeのキー
	 */
	public static final String Generate_Configuration_Type = getClassName() + "GENERATE_CONFIGURATION_TYPE";
	/**
	 * Configuration Variable Nameのキー
	 */
	public static final String Generate_Configuration_VarName = getClassName() + "GENERATE_CONFIGURATION_VARNAME";
	/**
	 * Configuration Default Valueのキー
	 */
	public static final String Generate_Configuration_Default = getClassName() + "GENERATE_CONFIGURATION_DEFAULT";
	/**
	 * Configuration Constraintのキー
	 */
	public static final String Generate_Configuration_Constraint = getClassName() + "GENERATE_CONFIGURATION_CONSTRAINT";
	/**
	 * Configuration Unitのキー
	 */
	public static final String Generate_Configuration_Unit = getClassName() + "GENERATE_CONFIGURATION_UNIT";
	/**
	 * Configuration 接頭語のキー
	 */
	public static final String Generate_Configuration_Prefix = getClassName() + "GENERATE_CONFIGURATION_PREFIX";
	/**
	 * Configuration 接頭尾のキー
	 */
	public static final String Generate_Configuration_Suffix = getClassName() + "GENERATE_CONFIGURATION_SUFFIX";
	//////////
	/**
	 * DataPort Nameのキー
	 */
	public static final String Generate_DataPort_Name = getClassName() + "GENERATE_DATAPORT_NAME";
	/**
	 * DataPort Typeのキー
	 */
	public static final String Generate_DataPort_Type = getClassName() + "GENERATE_DATAPORT_TYPE";
	/**
	 * DataPort VarNameのキー
	 */
	public static final String Generate_DataPort_VarName = getClassName() + "GENERATE_DATAPORT_VARNAME";
	/**
	 * DataPort 接頭語のキー
	 */
	public static final String Generate_DataPort_Prefix = getClassName() + "GENERATE_DATAPORT_PREFIX";
	/**
	 * DataPort 接尾語のキー
	 */
	public static final String Generate_DataPort_Suffix = getClassName() + "GENERATE_DATAPORT_SUFFIX";
	////
	/**
	 * ServicePort Nameのキー
	 */
	public static final String Generate_ServicePort_Name = getClassName() + "GENERATE_SERVICEPORT_NAME";
	/**
	 * ServicePort 接頭語のキー
	 */
	public static final String Generate_ServicePort_Prefix = getClassName() + "GENERATE_SERVICEPORT_PREFIX";
	/**
	 * ServicePort 接尾語のキー
	 */
	public static final String Generate_ServicePort_Suffix = getClassName() + "GENERATE_SERVICEPORT_SUFFIX";
	/**
	 * ServiceInterfacet Nameのキー
	 */
	public static final String Generate_ServiceIF_Name = getClassName() + "GENERATE_SERVICEIF_NAME";
	/**
	 * ServiceInterfacet Instance Nameのキー
	 */
	public static final String Generate_ServiceIF_InstanceName = getClassName() + "GENERATE_SERVICEIF_INSTANCENAME";
	/**
	 * ServiceInterfacet Varriable Nameのキー
	 */
	public static final String Generate_ServiceIF_VarName = getClassName() + "GENERATE_SERVICEIF_VARNAME";
	/**
	 * ServiceInterfacet Instance 接頭語のキー
	 */
	public static final String Generate_ServiceIF_Prefix = getClassName() + "GENERATE_SERVICEIF_PREFIX";
	/**
	 * ServiceInterfacet Instance 接尾語のキー
	 */
	public static final String Generate_ServiceIF_Suffix = getClassName() + "GENERATE_SERVICEIF_SUFFIX";
	//
	/**
	 * EventPort 変数名のキー
	 */
	public static final String Generate_EventPort_VarName = getClassName() + "GENERATE_EVENTPORT_VARNAME";
	/**
	 * EventPort 接頭語のキー
	 */
	public static final String Generate_EventPort_Prefix = getClassName() + "GENERATE_EVENTPORT_PREFIX";
	/**
	 * EventPort 接尾語のキー
	 */
	public static final String Generate_EventPort_Suffix = getClassName() + "GENERATE_EVENTPORT_SUFFIX";
	/**
	 * バックアップファイル設定数のキー
	 */
	public static final String Generate_Backup_Num = getClassName() + "GENERATE_BACKUP_NUM";
	/**
	 * Generated時の警告のキー
	 */
	public static final String Not_Generated_Caution = getClassName() + "NOT_GENERATE_CAUTION";
	
	//デフォルト値を空白から変更する場合には，getterの定義が必要
	public static final String DEFAULT_COMPONENT_NAME = "ModuleName";
	public static final String DEFAULT_DESCRIPTION = "ModuleDescription";
	public static final String DEFAULT_CATEGORY = "Category";
	public static final String DEFAULT_VERSION = "1.0.0";
	public static final String DEFAULT_VENDER = "VenderName";
	public static final String DEFAULT_COMPONENT_TYPE = IRtcBuilderConstants.COMPONENT_TYPE_ITEMS[0];
	public static final String DEFAULT_ACTIVITY_TYPE = IRtcBuilderConstants.ACTIVITY_TYPE_ITEMS[0];
	public static final int DEFAULT_MAXINST = 1;
	public static final String DEFAULT_EXECUTION_TYPE = IRtcBuilderConstants.EXECUTIONCONTEXT_TYPE_ITEMS[0];
	public static final double DEFAULT_EXECUTION_RATE = 1000.0;
	public static final String DEFAULT_PREFIX = "m_";
	public static final String DEFAULT_SUFFIX = "";
	//
	public static final String DEFAULT_CONFIGURATION_NAME = "conf_name";
	public static final String DEFAULT_CONFIGURATION_TYPE = "";
	public static final String DEFAULT_CONFIGURATION_VARNAME = "";
	public static final String DEFAULT_CONFIGURATION_DEFAULT = "";
	public static final String DEFAULT_CONFIGURATION_CONSTRAINT = "";
	public static final String DEFAULT_CONFIGURATION_UNIT = "";
	public static final String DEFAULT_CONFIGURATION_PREFIX = "";
	public static final String DEFAULT_CONFIGURATION_SUFFIX = "";
	//////////
	public static final String DEFAULT_DATAPORT_NAME = "dp_name";
	public static final String DEFAULT_DATAPORT_TYPE = "";
	public static final String DEFAULT_DATAPORT_VARNAME = "";
	public static final String DEFAULT_DATAPORT_CONSTRAINT = "";
	public static final String DEFAULT_DATAPORT_UNIT = "";
	public static final String DEFAULT_DATAPORT_PREFIX = "";
	public static final String DEFAULT_DATAPORT_SUFFIX = "";
	//
	public static final String DEFAULT_SERVICEPORT_NAME = "sv_name";
	public static final String DEFAULT_SERVICEPORT_PREFIX = "";
	public static final String DEFAULT_SERVICEPORT_SUFFIX = "";
	//
	public static final String DEFAULT_SERVICEIF_NAME = "if_name";
	public static final String DEFAULT_SERVICEIF_INSTANCENAME = "";
	public static final String DEFAULT_SERVICEIF_VARNAME = "";
	public static final String DEFAULT_SERVICEIF_PREFIX = "";
	public static final String DEFAULT_SERVICEIF_SUFFIX = "";
	//
	public static final String DEFAULT_EVENTPORT_VARNAME = "event_name";
	public static final String DEFAULT_EVENTPORT_PREFIX = "";
	public static final String DEFAULT_EVENTPORT_SUFFIX = "";
	//
	public static final int DEFAULT_BACKUP_NUM = 3;
	public static final boolean DEFAULT_NOT_GENERATED_CAUTION = false;
	
	/**
	 * コード生成時の ModuleName デフォルト値を取得する
	 * 
	 * @return Module Name デフォルト値
	 */
	public String getBasic_ComponentName() {
		return getStringStoreValue(Generate_Basic_Name, DEFAULT_COMPONENT_NAME);
	}

	/**
	 * コード生成時の ModuleDescription デフォルト値を取得する
	 * 
	 * @return Module Description デフォルト値
	 */
	public String getBasic_Description() {
		return getStringStoreValue(Generate_Basic_Description, DEFAULT_DESCRIPTION);
	}
	
	/**
	 * コード生成時の Module Category デフォルト値を取得する
	 * 
	 * @return Module Category デフォルト値
	 */
	public String getBasic_Category() {
		return getStringStoreValue(Generate_Basic_Category, DEFAULT_CATEGORY);
	}
	
	/**
	 * コード生成時の Module Version デフォルト値を取得する
	 * 
	 * @return Module Version デフォルト値
	 */
	public String getBasic_Version() {
		return getStringStoreValue(Generate_Basic_Version, DEFAULT_VERSION);
	}

	/**
	 * コード生成時の Module Component Type デフォルト値を取得する
	 * 
	 * @return Module Component Type デフォルト値
	 */
	public String getBasic_ComponentType() {
		return getStringStoreValue(Generate_Basic_ComponentType, DEFAULT_COMPONENT_TYPE);
	}

	/**
	 * コード生成時の Module Activity Type デフォルト値を取得する
	 * 
	 * @return Module Activity Type デフォルト値
	 */
	public String getBasic_ActivityType() {
		return getStringStoreValue(Generate_Basic_ActivityType, DEFAULT_ACTIVITY_TYPE);
	}

	/**
	 * コード生成時の Module Vendor Name デフォルト値を取得する
	 * 
	 * @return Module Vendor Name デフォルト値
	 */
	public String getBasic_VendorName() {
		return getStringStoreValue(Generate_Basic_Vendor_Name, DEFAULT_VENDER);
	}
	
	/**
	 * コード生成時の Module Max Instances デフォルト値を取得する
	 * 
	 * @return Module Max Instances デフォルト値
	 */
	public int getBasic_MaxInstances() {
		return getIntegaerStoreValue(Generate_Basic_Max_Instance, DEFAULT_MAXINST);
	}

	/**
	 * コード生成時の Module Execution Type デフォルト値を取得する
	 * 
	 * @return Module Execution Type デフォルト値
	 */
	public String getBasic_ExecutionType() {
		return getStringStoreValue(Generate_Basic_ExecutionType, DEFAULT_EXECUTION_TYPE);
	}

	/**
	 * コード生成時の Execution Rate デフォルト値を取得する
	 * 
	 * @return Execution Rate デフォルト値
	 */
	public double getBasic_ExecutionRate() {
		return getDoubleStoreValue(Generate_Basic_Execution_Rate, DEFAULT_EXECUTION_RATE);
	}
	
	/**
	 * コード生成時の 接頭語 デフォルト値を取得する
	 * 
	 * @return 接頭語 デフォルト値
	 */
	public String getBasic_Prefix() {
		return getStringStoreValue(Generate_Basic_Prefix, DEFAULT_PREFIX);
	}
	
	////
	/**
	 * コード生成時の Configuration Name デフォルト値を取得する
	 * 
	 * @return Configuration Name デフォルト値
	 */
	public String getConfiguration_Name() {
		return getStringStoreValue(Generate_Configuration_Name, DEFAULT_CONFIGURATION_NAME);
	}
	//////////
	/**
	 * コード生成時の DataPort Name デフォルト値を取得する
	 * 
	 * @return DataPort Name デフォルト値
	 */
	public String getDataPort_Name() {
		return getStringStoreValue(Generate_DataPort_Name, DEFAULT_DATAPORT_NAME);
	}
	/////
	/**
	 * コード生成時の ServicePort 名 デフォルト値を取得する
	 * 
	 * @return ServicePort 変数名 デフォルト値
	 */
	public String getServicePort_Name() {
		return getStringStoreValue(Generate_ServicePort_Name, DEFAULT_SERVICEPORT_NAME);
	}
	/////
	/**
	 * コード生成時の ServiceIF 名 デフォルト値を取得する
	 * 
	 * @return ServiceIF名 デフォルト値
	 */
	public String getServiceIF_Name() {
		return getStringStoreValue(Generate_ServiceIF_Name, DEFAULT_SERVICEIF_NAME);
	}
	//
	/**
	 * コード生成時の バックアップファイル数を取得する
	 * 
	 * @return バックアップファイル数 デフォルト値
	 */
	public int getBackup_Num() {
		return getIntegaerStoreValue(Generate_Backup_Num, DEFAULT_BACKUP_NUM);
	}

	/**
	 * コード生成時の 警告表示設定を取得する
	 * 
	 * @return コード生成時の 警告表示設定値
	 */
	public boolean getNot_Generated_Caution() {
		return getBooleanStoreValue(Not_Generated_Caution, DEFAULT_NOT_GENERATED_CAUTION);
	}

	/**
	 * コード生成時の 警告表示設定を設定する
	 * 
	 * @param value コード生成時の 警告表示設定値
	 */
	public void setNot_Generated_Caution(boolean value) {
		RtcBuilderPlugin.getDefault().getPreferenceStore().setValue(Not_Generated_Caution, value);
	}
	/////
	private String getStringStoreValue(String key, String defaultValue) {
		RtcBuilderPlugin.getDefault().getPreferenceStore().setDefault(key, "");

		String resultTemp = RtcBuilderPlugin.getDefault().getPreferenceStore().getString(key);
		String result = new String();
		if (resultTemp.equals("")) { // defaultvalue
			result = defaultValue;
		} else {
			result = resultTemp;
		}
		return result;
	}
	
	private int getIntegaerStoreValue(String key, int defaultValue) {
		RtcBuilderPlugin.getDefault().getPreferenceStore().setDefault(key, "");

		int resultTemp = RtcBuilderPlugin.getDefault().getPreferenceStore().getInt(key);
		int defaultReg = RtcBuilderPlugin.getDefault().getPreferenceStore().getDefaultInt(key);
		int result;
		if (resultTemp == defaultReg) { // defaultvalue
			result = defaultValue;
		} else {
			result = resultTemp;
		}
		return result;
	}
	
	private double getDoubleStoreValue(String key, double defaultValue) {
		RtcBuilderPlugin.getDefault().getPreferenceStore().setDefault(key, "");

		double resultTemp = RtcBuilderPlugin.getDefault().getPreferenceStore().getDouble(key);
		double defaultReg = RtcBuilderPlugin.getDefault().getPreferenceStore().getDefaultDouble(key);
		double result;
		if (resultTemp == defaultReg) { // defaultvalue
			result = defaultValue;
		} else {
			result = resultTemp;
		}
		return result;
	}
	
	private boolean getBooleanStoreValue(String key, boolean defaultValue) {
		RtcBuilderPlugin.getDefault().getPreferenceStore().setDefault(key, "");

		boolean resultTemp = RtcBuilderPlugin.getDefault().getPreferenceStore().getBoolean(key);
		boolean defaultReg = RtcBuilderPlugin.getDefault().getPreferenceStore().getDefaultBoolean(key);
		boolean result;
		if (resultTemp == defaultReg) { // defaultvalue
			result = defaultValue;
		} else {
			result = resultTemp;
		}
		return result;
	}
	
	private static String getClassName() {
		return ComponentPreferenceManager.class.getName();
	}
}
