package jp.go.aist.rtm.rtcbuilder.ros.ui.editors;

import jp.go.aist.rtm.rtcbuilder.nl.Messages;
import jp.go.aist.rtm.rtcbuilder.util.StringUtil;

public interface IMessageConstantsROS {
	public static final String BASIC_HINT_PACKCGENAME_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_BASIC_HINT_PACKAGENAME_DESC_P1"),
			Messages.getString("IMC.ROS_BASIC_HINT_PACKAGENAME_DESC_P2")});
	public static final String BASIC_HINT_NODENAME_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_BASIC_HINT_NODE_NAME_DESC_P1"),
			Messages.getString("IMC.ROS_BASIC_HINT_NODE_NAME_DESC_P2")});
	public static final String BASIC_HINT_CLASSNAME_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_BASIC_HINT_CLASS_NAME_DESC_P1"),
			Messages.getString("IMC.ROS_BASIC_HINT_CLASS_NAME_DESC_P2")});
	public static final String BASIC_HINT_CATEGORY_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_BASIC_HINT_CATEGORY_DESC_P1"),
			Messages.getString("IMC.ROS_BASIC_HINT_CATEGORY_DESC_P2")});
	public static final String BASIC_HINT_DESCRIPTION_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_BASIC_HINT_DESCRIPTION_DESC_P1"),
			Messages.getString("IMC.ROS_BASIC_HINT_DESCRIPTION_DESC_P2")});
	public static final String BASIC_HINT_LICENSE_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_BASIC_HINT_LICENSR_DESC_P1"),
			Messages.getString("IMC.ROS_BASIC_HINT_LICENSR_DESC_P2"),
			Messages.getString("IMC.ROS_BASIC_HINT_LICENSR_DESC_P3"),
			Messages.getString("IMC.ROS_BASIC_HINT_LICENSR_DESC_P4"),
			Messages.getString("IMC.ROS_BASIC_HINT_LICENSR_DESC_P5"),
			Messages.getString("IMC.ROS_BASIC_HINT_LICENSR_DESC_P6"),
			Messages.getString("IMC.ROS_BASIC_HINT_LICENSR_DESC_P7"),
			Messages.getString("IMC.ROS_BASIC_HINT_LICENSR_DESC_P8"),
			Messages.getString("IMC.ROS_BASIC_HINT_LICENSR_DESC_P9"),
			Messages.getString("IMC.ROS_BASIC_HINT_LICENSR_DESC_P10")});

	public static final String BASIC_HINT_DEPENDENCY_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_BASIC_HINT_DEPENDENCY_DESC_P1"),
			Messages.getString("IMC.ROS_BASIC_HINT_DEPENDENCY_DESC_P2")});

	public static final String BASIC_HINT_LANGUAGE_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_BASIC_HINT_LANGUAGE_DESC_P1"),
			Messages.getString("IMC.ROS_BASIC_HINT_LANGUAGE_DESC_P2")});
	
	public static final String BASIC_HINT_CODE_GEN_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_BASIC_HINT_GENERATE_DESC_P1"),
			Messages.getString("IMC.ROS_BASIC_HINT_GENERATE_DESC_P2")});

	public static final String LIFECYCLE_CONFIGURE_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_LIFECYCLE_HINT_ON_CONFIGURE_DESC_P1"),
			Messages.getString("IMC.ROS_LIFECYCLE_HINT_ON_CONFIGURE_DESC_P2")});
	public static final String LIFECYCLE_ACTIVATE_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_LIFECYCLE_HINT_ON_ACTIVATE_DESC_P1"),
			Messages.getString("IMC.ROS_LIFECYCLE_HINT_ON_ACTIVATE_DESC_P2")});
	public static final String LIFECYCLE_DEACTIVATE_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_LIFECYCLE_HINT_ON_DEACTIVATE_DESC_P1"),
			Messages.getString("IMC.ROS_LIFECYCLE_HINT_ON_DEACTIVATE_DESC_P2")});
	public static final String LIFECYCLE_CLEANUP_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_LIFECYCLE_HINT_ON_CLEANUP_DESC_P1"),
			Messages.getString("IMC.ROS_LIFECYCLE_HINT_ON_CLEANUP_DESC_P2"),
			Messages.getString("IMC.ROS_LIFECYCLE_HINT_ON_CLEANUP_DESC_P3")});
	public static final String LIFECYCLE_SHUTDOWN_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_LIFECYCLE_HINT_ON_SHUTDOWN_DESC_P1"),
			Messages.getString("IMC.ROS_LIFECYCLE_HINT_ON_SHUTDOWN_DESC_P2")});
	public static final String LIFECYCLE_ERROR_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_LIFECYCLE_HINT_ON_ERROR_DESC_P1"),
			Messages.getString("IMC.ROS_LIFECYCLE_HINT_ON_ERROR_DESC_P2"),
			Messages.getString("IMC.ROS_LIFECYCLE_HINT_ON_ERROR_DESC_P3")});

	public static final String ACTIVITY_DOCUMENT_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_DOCUMENT_EXPL_P1"),
			Messages.getString("IMC.ROS_DOCUMENT_EXPL_P2")});

	public static final String TIMER_NAME_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_LIFECYCLE_HINT_TIMER_NAME_DESC_P1"),
			Messages.getString("IMC.ROS_LIFECYCLE_HINT_TIMER_NAME_DESC_P2")});
	public static final String TIMER_CALLBACK_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_LIFECYCLE_HINT_TIMER_CALLBACK_DESC_P1"),
			Messages.getString("IMC.ROS_LIFECYCLE_HINT_TIMER_CALLBACK_DESC_P2")});
	public static final String TIMER_DESC_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_LIFECYCLE_HINT_TIMER_DESC_P1"),
			Messages.getString("IMC.ROS_LIFECYCLE_HINT_TIMER_DESC_P2")});
	
	public static final String TIMER_DOCUMENT_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_LIFECYCLE_HINT_TIMER_DESC_P1"),
			Messages.getString("IMC.ROS_LIFECYCLE_HINT_TIMER_DESC_P2")});

	public static final String TOPIC_CALLBACK_LBL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_TOPIC_LBL_VARNAME_P1"),
			Messages.getString("IMC.ROS_TOPIC_LBL_VARNAME_P2")
	});
	public static final String TOPIC_DOCUMENT_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_TOPIC_DOCUMENT_EXPL_P1"),
			Messages.getString("IMC.ROS_TOPIC_DOCUMENT_EXPL_P2")
	});

	public static final String TOPIC_HINT_TOPIC_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_TOPIC_HINT_TOPIC_DESC_P1"),
			Messages.getString("IMC.ROS_TOPIC_HINT_TOPIC_DESC_P2")
	});
	public static final String TOPIC_HINT_SUBSCRIBE_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_TOPIC_HINT_SUBSCRIBE_DESC_P1"),
			Messages.getString("IMC.ROS_TOPIC_HINT_SUBSCRIBE_DESC_P2")
	});
	public static final String TOPIC_HINT_PACKAGE_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_TOPIC_HINT_PACKAGE_DESC_P1"),
			Messages.getString("IMC.ROS_TOPIC_HINT_PACKAGE_DESC_P2"),
			Messages.getString("IMC.ROS_TOPIC_HINT_PACKAGE_DESC_P3"),
			Messages.getString("IMC.ROS_TOPIC_HINT_PACKAGE_DESC_P4"),
			Messages.getString("IMC.ROS_TOPIC_HINT_PACKAGE_DESC_P5"),
			Messages.getString("IMC.ROS_TOPIC_HINT_PACKAGE_DESC_P6"),
			Messages.getString("IMC.ROS_TOPIC_HINT_PACKAGE_DESC_P7"),
			Messages.getString("IMC.ROS_TOPIC_HINT_PACKAGE_DESC_P8"),
			Messages.getString("IMC.ROS_TOPIC_HINT_PACKAGE_DESC_P9"),
			Messages.getString("IMC.ROS_TOPIC_HINT_PACKAGE_DESC_P10"),
			Messages.getString("IMC.ROS_TOPIC_HINT_PACKAGE_DESC_P11"),
	});
	public static final String TOPIC_HINT_RELIABILITY_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_TOPIC_HINT_RELIABILITY_DESC_P1"),
			Messages.getString("IMC.ROS_TOPIC_HINT_RELIABILITY_DESC_P2"),
			Messages.getString("IMC.ROS_TOPIC_HINT_RELIABILITY_DESC_P3")
	});
	public static final String TOPIC_HINT_HISTORY_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_TOPIC_HINT_HISTORY_DESC_P1"),
			Messages.getString("IMC.ROS_TOPIC_HINT_HISTORY_DESC_P2"),
			Messages.getString("IMC.ROS_TOPIC_HINT_HISTORY_DESC_P3")
	});
	public static final String TOPIC_HINT_DEPTH_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_TOPIC_HINT_DEPTH_DESC_P1"),
			Messages.getString("IMC.ROS_TOPIC_HINT_DEPTH_DESC_P2")
	});
	public static final String TOPIC_HINT_VARNAME_SUBSCRIBE_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_TOPIC_HINT_VARNAME_SUBSCRIBE_DESC_P1"),
			Messages.getString("IMC.ROS_TOPIC_HINT_VARNAME_SUBSCRIBE_DESC_P2")
	});
	public static final String TOPIC_HINT_VARNAME_PUBLISH_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_TOPIC_HINT_VARNAME_PUBLISH_DESC_P1"),
			Messages.getString("IMC.ROS_TOPIC_HINT_VARNAME_PUBLISH_DESC_P2")
	});
	public static final String TOPIC_HINT_DOC_OVERVIEW_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_TOPIC_HINT_DOC_OVERVIEW_P1"),
			Messages.getString("IMC.ROS_TOPIC_HINT_DOC_OVERVIEW_P2")
	});
	public static final String TOPIC_HINT_DOC_UNIT_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_TOPIC_HINT_DOC_UNIT_P1"),
			Messages.getString("IMC.ROS_TOPIC_HINT_DOC_UNIT_P2")
	});
	public static final String TOPIC_HINT_DOC_OCCUR_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_TOPIC_HINT_DOC_OCCUR_P1"),
			Messages.getString("IMC.ROS_TOPIC_HINT_DOC_OCCUR_P2")
	});
	
	public static final String SERVICE_CALLBACK_LBL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_SERVICE_LBL_VARNAME_P1"),
			Messages.getString("IMC.ROS_SERVICE_LBL_VARNAME_P2")
	});
	public static final String SERVICE_DOCUMENT_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_SERVICE_DOCUMENT_EXPL_P1"),
			Messages.getString("IMC.ROS_SERVICE_DOCUMENT_EXPL_P2")
	});
	public static final String SERVICE_HINT_SERVICE_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_SERVICE_HINT_SERVICE_DESC_P1"),
			Messages.getString("IMC.ROS_SERVICE_HINT_SERVICE_DESC_P2")
	});
	public static final String SERVICE_HINT_SERVICE_PACKAGE_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_SERVICE_HINT_PACKAGE_DESC_P1"),
			Messages.getString("IMC.ROS_SERVICE_HINT_PACKAGE_DESC_P2"),
			Messages.getString("IMC.ROS_SERVICE_HINT_PACKAGE_DESC_P3"),
			Messages.getString("IMC.ROS_SERVICE_HINT_PACKAGE_DESC_P4"),
			Messages.getString("IMC.ROS_SERVICE_HINT_PACKAGE_DESC_P5"),
			Messages.getString("IMC.ROS_SERVICE_HINT_PACKAGE_DESC_P6"),
			Messages.getString("IMC.ROS_SERVICE_HINT_PACKAGE_DESC_P7"),
			Messages.getString("IMC.ROS_SERVICE_HINT_PACKAGE_DESC_P8"),
			Messages.getString("IMC.ROS_SERVICE_HINT_PACKAGE_DESC_P9"),
			Messages.getString("IMC.ROS_SERVICE_HINT_PACKAGE_DESC_P10"),
			Messages.getString("IMC.ROS_SERVICE_HINT_PACKAGE_DESC_P11"),
	});
	public static final String SERVICE_HINT_VAR_SERVER_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_SERVICE_HINT_VARNAME_SERVER_DESC_P1"),
			Messages.getString("IMC.ROS_SERVICE_HINT_VARNAME_SERVER_DESC_P2")
	});
	public static final String SERVICE_HINT_VAR_CLIENT_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_SERVICE_HINT_VARNAME_CLIENT_DESC_P1"),
			Messages.getString("IMC.ROS_SERVICE_HINT_VARNAME_CLIENT_DESC_P2")
	});
	public static final String SERVICE_HINT_OVERVIEW_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_SERVICE_HINT_OVERVIEW_DESC_P1"),
			Messages.getString("IMC.ROS_SERVICE_HINT_OVERVIEW_DESC_P2")
	});
	public static final String SERVICE_HINT_ARGUMENT_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_SERVICE_HINT_ARGUMEN_DESC_P1"),
			Messages.getString("IMC.ROS_SERVICE_HINT_ARGUMEN_DESC_P2"),
			Messages.getString("IMC.ROS_SERVICE_HINT_ARGUMEN_DESC_P3")
	});
	public static final String SERVICE_HINT_RETURN_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_SERVICE_HINT_RETURN_DESC_P1"),
			Messages.getString("IMC.ROS_SERVICE_HINT_RETURN_DESC_P2"),
			Messages.getString("IMC.ROS_SERVICE_HINT_RETURN_DESC_P3")
	});

	public static final String ACTION_DOCUMENT_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_ACTION_DOCUMENT_EXPL_P1"),
			Messages.getString("IMC.ROS_ACTION_DOCUMENT_EXPL_P2")
	});
	public static final String ACTION_HINT_ACTION_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_ACTION_HINT_ACTION_DESC_P1"),
			Messages.getString("IMC.ROS_ACTION_HINT_ACTION_DESC_P2"),
			Messages.getString("IMC.ROS_ACTION_HINT_ACTION_DESC_P3")
	});
	public static final String ACTION_HINT_NAME_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_ACTION_HINT_ACTION_NAME_DESC_P1"),
			Messages.getString("IMC.ROS_ACTION_HINT_ACTION_NAME_DESC_P2")
	});
	public static final String ACTION_HINT_PACKAGE_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_ACTION_HINT_PACKAGE_DESC_P1"),
			Messages.getString("IMC.ROS_ACTION_HINT_PACKAGE_DESC_P2"),
			Messages.getString("IMC.ROS_ACTION_HINT_PACKAGE_DESC_P3"),
			Messages.getString("IMC.ROS_ACTION_HINT_PACKAGE_DESC_P4"),
			Messages.getString("IMC.ROS_ACTION_HINT_PACKAGE_DESC_P5"),
			Messages.getString("IMC.ROS_ACTION_HINT_PACKAGE_DESC_P6"),
			Messages.getString("IMC.ROS_ACTION_HINT_PACKAGE_DESC_P7"),
			Messages.getString("IMC.ROS_ACTION_HINT_PACKAGE_DESC_P8"),
			Messages.getString("IMC.ROS_ACTION_HINT_PACKAGE_DESC_P9"),
			Messages.getString("IMC.ROS_ACTION_HINT_PACKAGE_DESC_P10"),
			Messages.getString("IMC.ROS_ACTION_HINT_PACKAGE_DESC_P11"),
	});
	public static final String ACTION_HINT_CALLBACK_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_ACTION_HINT_CALLBACK_DESC_P1"),
			Messages.getString("IMC.ROS_ACTION_HINT_CALLBACK_DESC_P2")
	});
	public static final String ACTION_HINT_DESC_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_ACTION_HINT_DESCRIPTION_DESC_P1"),
			Messages.getString("IMC.ROS_ACTION_HINT_DESCRIPTION_DESC_P2")
	});
	
	public static final String TOPIC_HINT_QoS_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_TOPIC_HINT_QoS_DESC_P1"),
			Messages.getString("IMC.ROS_TOPIC_HINT_QoS_DESC_P2")
	});

	public static final String PARAMETER_DOCUMENT_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_PARAMETER_DOCUMENT_EXPL_P1"),
			Messages.getString("IMC.ROS_PARAMETER_DOCUMENT_EXPL_P2")
	});
	public static final String PARAMETER_HINT_PARAMETER_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_PARAMETER_HINT_PARAMETER_DESC_P1"),
			Messages.getString("IMC.ROS_PARAMETER_HINT_PARAMETER_DESC_P2")
	});
	public static final String PARAMETER_HINT_DEFAULT_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_PARAMETER_HINT_DEFAULT_DESC_P1"),
			Messages.getString("IMC.ROS_PARAMETER_HINT_DEFAULT_DESC_P2")
	});
	public static final String PARAMETER_HINT_MIN_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_PARAMETER_HINT_MIN_DESC_P1"),
			Messages.getString("IMC.ROS_PARAMETER_HINT_MIN_DESC_P2"),
			Messages.getString("IMC.ROS_PARAMETER_HINT_MIN_DESC_P3")
	});
	public static final String PARAMETER_HINT_MAX_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_PARAMETER_HINT_MAX_DESC_P1"),
			Messages.getString("IMC.ROS_PARAMETER_HINT_MAX_DESC_P2"),
			Messages.getString("IMC.ROS_PARAMETER_HINT_MAX_DESC_P3")
	});
	public static final String PARAMETER_HINT_STEP_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_PARAMETER_HINT_STEP_DESC_P1"),
			Messages.getString("IMC.ROS_PARAMETER_HINT_STEP_DESC_P2"),
			Messages.getString("IMC.ROS_PARAMETER_HINT_STEP_DESC_P3")
	});
	public static final String PARAMETER_HINT_DESC_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROS_PARAMETER_HINT_DESC_DESC_P1"),
			Messages.getString("IMC.ROS_PARAMETER_HINT_DESC_DESC_P2")
	});

	public static final String ROSXML_CAUTION = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ROSXML_CAUTION_P1"),
			Messages.getString("IMC.ROSXML_CAUTION_P2")
	});

	public static final String PROFIE_LOAD_ERROR = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.ERROR_PROFILE_RESTORE_P1"),
			Messages.getString("IMC.ERROR_PROFILE_RESTORE_P2")
	});
}
