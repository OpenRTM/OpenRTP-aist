package jp.go.aist.rtm.rtcbuilder;


public interface IRtcBuilderConstants {

    /**
     * OpenNewEditor ActionSet のID
     */
    public static final String NEWEDITOR_ACTION_ID = RtcBuilderPlugin.PLUGIN_ID + ".ui.actionSet";

	public static final String RTM_VERSION_100 = "1.0.0";
	public static final String DEFAULT_RTM_VERSION = "2.0.0";
	
	public static final String MIDDLEWARE_ALL = "ALL";
	public static final String MIDDLEWARE_RTM = "OpenRTM";
	public static final String MIDDLEWARE_ROS = "ROS";

	/**
	 * サービス実装のデフォルトサフィックス
	 */
	public static final String DEFAULT_SVC_IMPL_SUFFIX = "SVC_impl";

	/**
	 * サービススタブのデフォルトサフィックス
	 */
	public static final String DEFAULT_SVC_STUB_SUFFIX = "Stub";

	/**
	 * サービススケルトンのデフォルトサフィックス
	 */
	public static final String DEFAULT_SVC_SKEL_SUFFIX = "Skel";
	
	public static final String SPEC_SUFFIX = "RTC";
	public static final String SPEC_MAJOR_SEPARATOR = ":";
	public static final String SPEC_MINOR_SEPARATOR = ".";
	//
	public static final String SPEC_DATA_INPORT_KIND = "DataInPort";
	public static final String SPEC_DATA_OUTPORT_KIND = "DataOutPort";
	public static final String SPEC_EVENTPORT_KIND = "EventPort";

	public static final String DEFAULT_RTC_XML = "RTC.xml";
	public static final String DEFAULT_ISO_202_XML = "iso22166_202.xml";
	public static final String YAML_EXTENSION = "yaml";
	public static final String XML_EXTENSION = "xml";
	//
	public static final String SCHEMA_VERSION = "0.3";

	public static final String NEWLINE_CODE = "<br/>";

	public static final String[] DIRECTION_ITEMS = new String[] {
		"LEFT", "RIGHT" ,"TOP", "BOTTOM" };

	public static final String[] COMPONENT_TYPE_ITEMS = new String[] {
		"STATIC", "UNIQUE", "COMMUTATIVE" };

	public static final String[] ACTIVITY_TYPE_ITEMS = new String[] {
			"PERIODIC", "SPORADIC", "EVENTDRIVEN" };

	public static final String[] EXECUTIONCONTEXT_TYPE_ITEMS = new String[] {
		"PeriodicExecutionContext", "ExtTrigExecutionContext" };

	public static final String[] ACTION_TYPE_ITEMS = new String[] {
		"onInitialize", "onFinalize", "onStartup", "onShutdown", "onActivated",
		"onDeactivated", "onAborting", "onError", "onReset",
		"onExecute", "onStateUpdate", "onRateChanged"};
	
	public static final String TAG_BACKEND = "backend";
	public static final String TAG_SVC_IDL = "svc-idl";
	public static final String TAG_SVC_NAME = "svc-name";
	public static final String TAG_INPORT_TYPE = "type";
	public static final String TAG_OUTPORT_TYPE = "type";
	public static final String TAG_SERVICE_PORT_PORTNAME = "portname";
	public static final String TAG_SERVICE_PORT_TYPE = "type";

	public static final String LANG_CPP = "C++";
	public static final String LANG_CPPWIN = "C++(Windows)";
	public static final String LANG_PYTHON = "Python";
	public static final String LANG_JAVA = "Java";

	public static final String LANG_CPP_ARG = "cxx";
	public static final String LANG_CPPWIN_ARG = "cxxwin";
	
	public static final String CONFIG_WIDGET = "__widget__";
	public static final String CONFIG_WIDGET_SPIN = "spin";
	public static final String CONFIG_WIDGET_SLIDER = "slider";

	public static final String CONTAINER_PREFIX = "__container__";
	//
	public static final String SPACE1 = " "; 
	public static final String SPACE2 = "  "; 
	public static final String SPACE3 = "   ";
	public static final String SPACE9 = "         ";
	public static final String SPACE10 = "          ";
	public static final String SPACE11 = "           ";
	public static final String SPACE12 = "            ";
	public static final String SPACE13 = "             ";
	public static final String SPACE14 = "              ";
	public static final String SPACE15 = "               ";
	public static final String SPACE17 = "                 ";
	public static final String SPACE18 = "                  ";
	public static final String SPACE20 = "                    ";
	//
	public static final String DOC_DEFAULT_PREFIX = SPACE1 + "*" + SPACE1; 
	public static final String DOC_DESC_PREFIX = SPACE3 + "*" + SPACE1;
	public static final String DOC_UNIT_PREFIX = SPACE3 + "*" + SPACE9; 
	public static final String DOC_RANGE_PREFIX = SPACE3 + "*" + SPACE10;
	public static final String DOC_CONSTRAINT_PREFIX = SPACE3 + "*" + SPACE15; 
	public static final String DOC_NUMBER_PREFIX = SPACE3 + "*" + SPACE11;
	public static final String DOC_SEMANTICS_PREFIX = SPACE3 + "*" + SPACE14;
	public static final String DOC_CYCLE_PREFIX = SPACE3 + "*" + SPACE20; 
	public static final String DOC_INTERFACE_PREFIX = SPACE3 + "*" + SPACE12;
	public static final String DOC_INTERFACE_DETAIL_PREFIX = SPACE3 + "*" + SPACE18; 
	//
	public static final String DOC_README_ACTIVITY_PREFIX = "\t" + SPACE15; 
	//
	public static final int DOC_DEFAULT_WIDTH = 66; 
	public static final int DOC_AUTHOR_OFFSET = 11; 
	public static final int DOC_DEFAULT_OFFSET = 3; 
	public static final int DOC_DESC_OFFSET = 5;
	public static final int DOC_PRE_OFFSET = 10;
	public static final int DOC_POST_OFFSET = 11;
	public static final int DOC_UNIT_OFFSET = 13;
	public static final int DOC_RANGE_OFFSET = 14;
	public static final int DOC_CONSTRAINT_OFFSET = 19;
	public static final int DOC_NUMBER_OFFSET = 15;
	public static final int DOC_SEMANTICS_OFFSET = 18;
	public static final int DOC_CYCLE_OFFSET = 24;
	public static final int DOC_INTERFACE_OFFSET = 16;
	public static final int DOC_INTERFACE_DETAIL_OFFSET = 22;
	//
	public static final int DOC_README_ACTIVITY_OFFSET = 17;
	//
	public static final int ACTIVITY_INITIALIZE = 0; 
	public static final int ACTIVITY_FINALIZE = 1;
	public static final int ACTIVITY_STARTUP = 2;
	public static final int ACTIVITY_SHUTDOWN = 3; 
	public static final int ACTIVITY_ACTIVATED = 4;
	public static final int ACTIVITY_DEACTIVATED = 5; 
	public static final int ACTIVITY_ABORTING = 6;
	public static final int ACTIVITY_ERROR = 7;
	public static final int ACTIVITY_RESET = 8;
	public static final int ACTIVITY_EXECUTE = 9;
	public static final int ACTIVITY_STATE_UPDATE = 10;
	public static final int ACTIVITY_RATE_CHANGED = 11;
	public static final int ACTIVITY_DUMMY = 12;
	//
	public static final int PORT_SPACE_HEIGHT = 60;
	public static final int PORT_SPACE_WIDTH = 150;

	public static final int SINGLE_COLUMN_WIDTH = 200;
	//FSM
	public static final String PROP_TYPE_FSM =  "FSM";
	public static final String PROP_TYPE_FSMTYTPE =  "FSMType";
	public static final String FSMTYTPE_STATIC =  "StaticFSM";
	public static final String FSMTYTPE_DYNAMIC =  "DynamicFSM";
	
	public static final int Type_Event = 1;
}
