package jp.go.aist.rtm.rtcbuilder.ui.editors;

import jp.go.aist.rtm.rtcbuilder.nl.Messages;
import jp.go.aist.rtm.rtcbuilder.util.StringUtil;

public interface IMessageConstants {
	public static final String REQUIRED = "*";
	
	public static final String HINT_TITLE = Messages.getString("IMessageConstants.HINT_TITLE"); //$NON-NLS-1$

	public static final String BASIC_HINT_MODULENAME_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.BASIC_HINT_MODULENAME_DESC_P1"),
			Messages.getString("IMC.BASIC_HINT_MODULENAME_DESC_P2"),
			Messages.getString("IMC.BASIC_HINT_MODULENAME_DESC_P3"),
			Messages.getString("IMC.BASIC_HINT_MODULENAME_DESC_P4")});
	public static final String BASIC_HINT_DESCRIPTION_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.BASIC_HINT_DESCRIPTION_DESC_P1"),
			Messages.getString("IMC.BASIC_HINT_DESCRIPTION_DESC_P2")});
	public static final String BASIC_HINT_VERSION_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.BASIC_HINT_VERSION_DESC_P1"),
			Messages.getString("IMC.BASIC_HINT_VERSION_DESC_P2")});
	public static final String BASIC_HINT_VENDOR_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.BASIC_HINT_VENDOR_DESC_P1"),
			Messages.getString("IMC.BASIC_HINT_VENDOR_DESC_P2")});
	public static final String BASIC_HINT_CATEGORY_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.BASIC_HINT_CATEGORY_DESC_P1"),
			Messages.getString("IMC.BASIC_HINT_CATEGORY_DESC_P2"),
			Messages.getString("IMC.BASIC_HINT_CATEGORY_DESC_P3")});
	public static final String BASIC_HINT_COMPTYPE_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.BASIC_HINT_COMPTYPE_DESC_P1"),
			" " + Messages.getString("IMC.BASIC_HINT_COMPTYPE_DESC_P2"),
			" " + Messages.getString("IMC.BASIC_HINT_COMPTYPE_DESC_P3"),
			" " + Messages.getString("IMC.BASIC_HINT_COMPTYPE_DESC_P4")});
	public static final String BASIC_HINT_ACTIVITYTYPE_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.BASIC_HINT_ACTIVITYTYPE_DESC_P1"),
			Messages.getString("IMC.BASIC_HINT_ACTIVITYTYPE_DESC_P2"),
			Messages.getString("IMC.BASIC_HINT_ACTIVITYTYPE_DESC_P3"),
			Messages.getString("IMC.BASIC_HINT_ACTIVITYTYPE_DESC_P4")});
	public static final String BASIC_HINT_MAXINST_TITLE = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.BASIC_HINT_MAXINST_TITLE_P1"),
			Messages.getString("IMC.BASIC_HINT_MAXINST_TITLE_P2")});
	public static final String BASIC_HINT_MAXINST_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.BASIC_HINT_MAXINST_DESC_P1"),
			Messages.getString("IMC.BASIC_HINT_MAXINST_DESC_P2")});
	public static final String BASIC_HINT_EXECUTIONRATE_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.BASIC_HINT_EXECUTIONRATE_DESC_P1"),
			Messages.getString("IMC.BASIC_HINT_EXECUTIONRATE_DESC_P2"),
			Messages.getString("IMC.BASIC_HINT_EXECUTIONRATE_DESC_P3")});
	public static final String BASIC_HINT_RTCTYPE_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.BASIC_HINT_RTCTYPE_DESC_P1"),
			Messages.getString("IMC.BASIC_HINT_RTCTYPE_DESC_P2"),
			Messages.getString("IMC.BASIC_HINT_RTCTYPE_DESC_P3")});
	
	public static final String BASIC_VALIDATE_NAME2 = Messages.getString("IMessageConstants.BASIC_VALIDATE_NAME2"); //$NON-NLS-1$
	public static final String BASIC_VALIDATE_CATEGORY = Messages.getString("IMessageConstants.BASIC_VALIDATE_CATEGORY"); //$NON-NLS-1$

	public static final String DATAPORT_DOCUMENT_EXPL = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.DATAPORT_DOCUMENT_EXPL_P1"),
			Messages.getString("IMC.DATAPORT_DOCUMENT_EXPL_P2")
	});

	public static final String DATAPORT_HINT_DATAPORT_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.DATAPORT_HINT_DATAPORT_DESC_P1"),
			Messages.getString("IMC.DATAPORT_HINT_DATAPORT_DESC_P2"),
			Messages.getString("IMC.DATAPORT_HINT_DATAPORT_DESC_P3")
	});
	public static final String DATAPORT_HINT_INPORT_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.DATAPORT_HINT_INPORT_DESC_P1"),
			Messages.getString("IMC.DATAPORT_HINT_INPORT_DESC_P2")
	});
	public static final String DATAPORT_HINT_OUTPORT_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.DATAPORT_HINT_OUTPORT_DESC_P1"),
			Messages.getString("IMC.DATAPORT_HINT_OUTPORT_DESC_P2")
	});
	public static final String DATAPORT_HINT_PORTNAME_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.DATAPORT_HINT_PORTNAME_DESC_P1"),
			Messages.getString("IMC.DATAPORT_HINT_PORTNAME_DESC_P2"),
			Messages.getString("IMC.DATAPORT_HINT_PORTNAME_DESC_P3"),
			Messages.getString("IMC.DATAPORT_HINT_PORTNAME_DESC_P4"),
			Messages.getString("IMC.DATAPORT_HINT_PORTNAME_DESC_P5")
	});
	public static final String DATAPORT_HINT_DATATYPE_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.DATAPORT_HINT_DATATYPE_DESC_P1"),
			Messages.getString("IMC.DATAPORT_HINT_DATATYPE_DESC_P2"),
			Messages.getString("IMC.DATAPORT_HINT_DATATYPE_DESC_P3"),
			Messages.getString("IMC.DATAPORT_HINT_DATATYPE_DESC_P4"),
			Messages.getString("IMC.DATAPORT_HINT_DATATYPE_DESC_P5"),
			Messages.getString("IMC.DATAPORT_HINT_DATATYPE_DESC_P6"),
			Messages.getString("IMC.DATAPORT_HINT_DATATYPE_DESC_P7"),
			Messages.getString("IMC.DATAPORT_HINT_DATATYPE_DESC_P8"),
			Messages.getString("IMC.DATAPORT_HINT_DATATYPE_DESC_P9")
	});
	public static final String DATAPORT_HINT_VARNAME_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.DATAPORT_HINT_VARNAME_DESC_P1"),
			Messages.getString("IMC.DATAPORT_HINT_VARNAME_DESC_P2")
	});
	public static final String DATAPORT_HINT_POSITION_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.DATAPORT_HINT_POSITION_DESC_P1"),
			Messages.getString("IMC.DATAPORT_HINT_POSITION_DESC_P2")
	});
	public static final String DATAPORT_HINT_OCCUR_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.DATAPORT_HINT_DOC_OCCUR_P1"),
			Messages.getString("IMC.DATAPORT_HINT_DOC_OCCUR_P2")
	});
	public static final String DATAPORT_HINT_OPERAT_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.DATAPORT_HINT_DOC_OPERAT_P1"),
			Messages.getString("IMC.DATAPORT_HINT_DOC_OPERAT_P2")
	});

	public static final String DATAPORT_HINT_ROS_DATAPORT_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.DATAPORT_HINT_DATAPORT_DESC_ROS_P1"),
			Messages.getString("IMC.DATAPORT_HINT_DATAPORT_DESC_ROS_P2"),
			Messages.getString("IMC.DATAPORT_HINT_DATAPORT_DESC_ROS_P3"),
			Messages.getString("IMC.DATAPORT_HINT_DATAPORT_DESC_ROS_P4"),
			Messages.getString("IMC.DATAPORT_HINT_DATAPORT_DESC_ROS_P5"),
			Messages.getString("IMC.DATAPORT_HINT_DATAPORT_DESC_ROS_P6"),
			Messages.getString("IMC.DATAPORT_HINT_DATAPORT_DESC_ROS_P7")
	});
	public static final String DATAPORT_HINT_ROS_INPORT_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.DATAPORT_HINT_INPORT_DESC_ROS_P1"),
			Messages.getString("IMC.DATAPORT_HINT_INPORT_DESC_ROS_P2")
	});
	public static final String DATAPORT_HINT_ROS_OUTPORT_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.DATAPORT_HINT_OUTPORT_DESC_ROS_P1"),
			Messages.getString("IMC.DATAPORT_HINT_OUTPORT_DESC_ROS_P2")
	});
	public static final String DATAPORT_HINT_ROS_PORTNAME_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.DATAPORT_HINT_PORTNAME_DESC_ROS_P1"),
			Messages.getString("IMC.DATAPORT_HINT_PORTNAME_DESC_ROS_P2"),
			Messages.getString("IMC.DATAPORT_HINT_PORTNAME_DESC_ROS_P3"),
			Messages.getString("IMC.DATAPORT_HINT_PORTNAME_DESC_ROS_P4")
	});
	public static final String DATAPORT_HINT_ROS_DATATYPE_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.DATAPORT_HINT_DATATYPE_DESC_ROS_P1"),
			Messages.getString("IMC.DATAPORT_HINT_DATATYPE_DESC_ROS_P2"),
			Messages.getString("IMC.DATAPORT_HINT_DATATYPE_DESC_ROS_P3"),
			Messages.getString("IMC.DATAPORT_HINT_DATATYPE_DESC_ROS_P4"),
			Messages.getString("IMC.DATAPORT_HINT_DATATYPE_DESC_ROS_P5")
	});

	public static final String DATAPORT_VALIDATE_PORTNAME1 = Messages.getString("IMessageConstants.DATAPORT_VALIDATE_PORTNAME1"); //$NON-NLS-1$
	public static final String DATAPORT_VALIDATE_PORTNAME2 = Messages.getString("IMessageConstants.DATAPORT_VALIDATE_PORTNAME2"); //$NON-NLS-1$
	public static final String DATAPORT_VALIDATE_PORTTYPE = Messages.getString("IMessageConstants.DATAPORT_VALIDATE_PORTTYPE"); //$NON-NLS-1$
	public static final String DATAPORT_VALIDATE_PORTTYPE_INVALID = Messages.getString("IMessageConstants.DATAPORT_VALIDATE_PORTTYPE_INVALID"); //$NON-NLS-1$
	public static final String DATAPORT_VALIDATE_DUPLICATE = Messages.getString("IMessageConstants.DATAPORT_VALIDATE_DUPLICATE"); //$NON-NLS-1$
	public static final String DATAPORT_VALIDATE_VAR_DUPLICATE = Messages.getString("IMessageConstants.DATAPORT_VALIDATE_VAR_DUPLICATE"); //$NON-NLS-1$
	public static final String DATAPORT_VALIDATE_PORTVARNAME = Messages.getString("IMessageConstants.DATAPORT_VALIDATE_PORTVARNAME"); //$NON-NLS-1$

	public static final String SERVICEPORT_VALIDATE_PORTNAME1 = Messages.getString("IMessageConstants.SERVICEPORT_VALIDATE_PORTNAME1"); //$NON-NLS-1$
	public static final String SERVICEPORT_VALIDATE_PORTNAME2 = Messages.getString("IMessageConstants.SERVICEPORT_VALIDATE_PORTNAME2"); //$NON-NLS-1$
	public static final String SERVICEPORT_VALIDATE_DUPLICATE = Messages.getString("IMessageConstants.SERVICEPORT_VALIDATE_DUPLICATE"); //$NON-NLS-1$
	public static final String SERVICEPORT_VALIDATE_VAR_DUPLICATE = Messages.getString("IMessageConstants.SERVICEPORT_VALIDATE_VAR_DUPLICATE"); //$NON-NLS-1$
	public static final String SERVICEPORT_VALIDATE_IFNAME1 = Messages.getString("IMessageConstants.SERVICEPORT_VALIDATE_IFNAME1"); //$NON-NLS-1$
	public static final String SERVICEPORT_VALIDATE_IFNAME2 = Messages.getString("IMessageConstants.SERVICEPORT_VALIDATE_IFNAME2"); //$NON-NLS-1$
	public static final String SERVICEPORT_VALIDATE_INSTNAME1 = Messages.getString("IMessageConstants.SERVICEPORT_VALIDATE_INSTNAME1"); //$NON-NLS-1$
	public static final String SERVICEPORT_VALIDATE_INSTNAME2 = Messages.getString("IMessageConstants.SERVICEPORT_VALIDATE_INSTNAME2"); //$NON-NLS-1$
	public static final String SERVICEPORT_VALIDATE_IFTYPE1 = Messages.getString("IMessageConstants.SERVICEPORT_VALIDATE_IFTYPE1"); //$NON-NLS-1$
	public static final String SERVICEPORT_VALIDATE_IFTYPE2 = Messages.getString("IMessageConstants.SERVICEPORT_VALIDATE_IFTYPE2"); //$NON-NLS-1$
	public static final String SERVICEPORT_VALIDATE_VARNAME = Messages.getString("IMessageConstants.SERVICEPORT_VALIDATE_VARNAME"); //$NON-NLS-1$

	public static final String SERVICEPORT_HINT_PORT_NAME_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.SERVIVEPORT_HINT_PORT_NAME_DESC_P1"),
			Messages.getString("IMC.SERVIVEPORT_HINT_PORT_NAME_DESC_P2"),
			Messages.getString("IMC.SERVIVEPORT_HINT_PORT_NAME_DESC_P3"),
			Messages.getString("IMC.SERVIVEPORT_HINT_PORT_NAME_DESC_P4"),
			Messages.getString("IMC.SERVIVEPORT_HINT_PORT_NAME_DESC_P5")
	});
	
	public static final String SERVICEPORT_HINT_IF_NAME_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.SERVIVEPORT_HINT_IF_NAME_DESC_P1"),
			Messages.getString("IMC.SERVIVEPORT_HINT_IF_NAME_DESC_P2"),
			Messages.getString("IMC.SERVIVEPORT_HINT_IF_NAME_DESC_P3")
	});
	
	public static final String SERVICEPORT_HINT_IF_DIRECTION_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.SERVIVEPORT_HINT_IF_DIRECTION_DESC_P1"),
			Messages.getString("IMC.SERVIVEPORT_HINT_IF_DIRECTION_DESC_P2"),
			Messages.getString("IMC.SERVIVEPORT_HINT_IF_DIRECTION_DESC_P3")
	});
	
	public static final String SERVICEPORT_HINT_IF_INSTANCE_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.SERVIVEPORT_HINT_IF_INSTANCE_DESC_P1"),
			Messages.getString("IMC.SERVIVEPORT_HINT_IF_INSTANCE_DESC_P2")});
	
	public static final String SERVICEPORT_HINT_IF_VARNAME_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.SERVIVEPORT_HINT_IF_VARNAME_DESC_P1"),
			Messages.getString("IMC.SERVIVEPORT_HINT_IF_VARNAME_DESC_P2")});
	
	public static final String SERVICEPORT_HINT_IFTYPE_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.SERVIVEPORT_HINT_IFTYPE_DESC_P1"),
			Messages.getString("IMC.SERVIVEPORT_HINT_IFTYPE_DESC_P2"),
			Messages.getString("IMC.SERVIVEPORT_HINT_IFTYPE_DESC_P3"),
			Messages.getString("IMC.SERVIVEPORT_HINT_IFTYPE_DESC_P4"),
			Messages.getString("IMC.SERVIVEPORT_HINT_IFTYPE_DESC_P5")
	});
	
	public static final String SERVICEPORT_HINT_IDLFILE_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.SERVIVEPORT_HINT_IDLFILE_DESC_P1"),
			Messages.getString("IMC.SERVIVEPORT_HINT_IDLFILE_DESC_P2")});
	
	public static final String SERVICEPORT_HINT_ARGUMENT_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.SERVIVEPORT_HINT_ARGUMEN_DESC_P1"),
			Messages.getString("IMC.SERVIVEPORT_HINT_ARGUMEN_DESC_P2"),
			Messages.getString("IMC.SERVIVEPORT_HINT_ARGUMEN_DESC_P3")});
	
	public static final String SERVICEPORT_HINT_RETURN_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.SERVIVEPORT_HINT_RETURN_DESC_P1"),
			Messages.getString("IMC.SERVIVEPORT_HINT_RETURN_DESC_P2"),
			Messages.getString("IMC.SERVIVEPORT_HINT_RETURN_DESC_P3")});
	
	public static final String SERVICEPORT_HINT_EXCEPTION_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.SERVIVEPORT_HINT_EXCEPTION_DESC_P1"),
			Messages.getString("IMC.SERVIVEPORT_HINT_EXCEPTION_DESC_P2"),
			Messages.getString("IMC.SERVIVEPORT_HINT_EXCEPTION_DESC_P3")});
	
	public static final String SERVICEPORT_HINT_PRE_CONDITION_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.SERVIVEPORT_HINT_PRE_CONDITION_DESC_P1"),
			Messages.getString("IMC.SERVIVEPORT_HINT_PRE_CONDITION_DESC_P2")});
	
	public static final String SERVICEPORT_HINT_POST_CONDITION_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.SERVIVEPORT_HINT_POST_CONDITION_DESC_P1"),
			Messages.getString("IMC.SERVIVEPORT_HINT_POST_CONDITION_DESC_P2")});
		
	public static final String SERVICEPORT_HINT_ROS_PORT_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.SERVIVEPORT_HINT_PORT_DESC_ROS_P1"),
			Messages.getString("IMC.SERVIVEPORT_HINT_PORT_DESC_ROS_P2"),
			Messages.getString("IMC.SERVIVEPORT_HINT_PORT_DESC_ROS_P3"),
			Messages.getString("IMC.SERVIVEPORT_HINT_PORT_DESC_ROS_P4"),
			Messages.getString("IMC.SERVIVEPORT_HINT_PORT_DESC_ROS_P5"),
			Messages.getString("IMC.SERVIVEPORT_HINT_PORT_DESC_ROS_P6")
	});
	public static final String SERVICEPORT_HINT_ROS_PORT_NAME_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.SERVIVEPORT_HINT_PORT_NAME_DESC_ROS_P1"),
			Messages.getString("IMC.SERVIVEPORT_HINT_PORT_NAME_DESC_ROS_P2"),
			Messages.getString("IMC.SERVIVEPORT_HINT_PORT_NAME_DESC_ROS_P3"),
			Messages.getString("IMC.SERVIVEPORT_HINT_PORT_NAME_DESC_ROS_P4")
	});
	public static final String SERVICEPORT_HINT_IF_ROS_NAME_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.SERVIVEPORT_HINT_IF_NAME_DESC_ROS")
	});
	public static final String SERVICEPORT_HINT_IF_ROS_DIRECTION_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.SERVIVEPORT_HINT_IF_DIRECTION_DESC_ROS_P1"),
			Messages.getString("IMC.SERVIVEPORT_HINT_IF_DIRECTION_DESC_ROS_P2"),
			Messages.getString("IMC.SERVIVEPORT_HINT_IF_DIRECTION_DESC_ROS_P3")
	});
	public static final String SERVICEPORT_HINT_ROS_IFTYPE_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.SERVIVEPORT_HINT_IFTYPE_DESC_ROS_P1"),
			Messages.getString("IMC.SERVIVEPORT_HINT_IFTYPE_DESC_ROS_P2"),
			Messages.getString("IMC.SERVIVEPORT_HINT_IFTYPE_DESC_ROS_P3"),
			Messages.getString("IMC.SERVIVEPORT_HINT_IFTYPE_DESC_ROS_P4"),
			Messages.getString("IMC.SERVIVEPORT_HINT_IFTYPE_DESC_ROS_P5")
	});

	//
	public static final String CONFIGURATION_HINT_COFIGPARAM_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.CONFIGURATION_HINT_COFIGPARAM_DESC_P1"),
			Messages.getString("IMC.CONFIGURATION_HINT_COFIGPARAM_DESC_P2"),
			Messages.getString("IMC.CONFIGURATION_HINT_COFIGPARAM_DESC_P3"),
			Messages.getString("IMC.CONFIGURATION_HINT_COFIGPARAM_DESC_P4"),
			Messages.getString("IMC.CONFIGURATION_HINT_COFIGPARAM_DESC_P5"),
			Messages.getString("IMC.CONFIGURATION_HINT_COFIGPARAM_DESC_P6")
	});
	public static final String CONFIGURATION_HINT_PARAMNAME_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.CONFIGURATION_HINT_PARAMNAME_DESC_P1"),
			Messages.getString("IMC.CONFIGURATION_HINT_PARAMNAME_DESC_P2"),
			Messages.getString("IMC.CONFIGURATION_HINT_PARAMNAME_DESC_P3")
	});
	public static final String CONFIGURATION_HINT_PARAMTYPE_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.CONFIGURATION_HINT_PARAMTYPE_DESC_P1"),
			Messages.getString("IMC.CONFIGURATION_HINT_PARAMTYPE_DESC_P2")
	});
	public static final String CONFIGURATION_HINT_VARNAME_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.CONFIGURATION_HINT_VARNAME_DESC_P1"),
			Messages.getString("IMC.CONFIGURATION_HINT_VARNAME_DESC_P2")
	});
	public static final String CONFIGURATION_HINT_DEFAULT_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.CONFIGURATION_HINT_DEFAULT_DESC_P1"),
			Messages.getString("IMC.CONFIGURATION_HINT_DEFAULT_DESC_P2"),
			Messages.getString("IMC.CONFIGURATION_HINT_DEFAULT_DESC_P3")
	});
	public static final String CONFIGURATION_HINT_CONSTRAINT_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.CONFIGURATION_HINT_CONSTRAINT_DESC_P1"),
			Messages.getString("IMC.CONFIGURATION_HINT_CONSTRAINT_DESC_P2"),
			Messages.getString("IMC.CONFIGURATION_HINT_CONSTRAINT_DESC_P3"),
			Messages.getString("IMC.CONFIGURATION_HINT_CONSTRAINT_DESC_P4"),
			Messages.getString("IMC.CONFIGURATION_HINT_CONSTRAINT_DESC_P5"),
			Messages.getString("IMC.CONFIGURATION_HINT_CONSTRAINT_DESC_P6"),
			Messages.getString("IMC.CONFIGURATION_HINT_CONSTRAINT_DESC_P7")
	});
	public static final String CONFIGURATION_HINT_WIDGET_DESC = StringUtil.connectMessageWithSepalator( new String[]{
		Messages.getString("IMC.CONFIGURATION_HINT_WIDGET_DESC_P1"),
		Messages.getString("IMC.CONFIGURATION_HINT_WIDGET_DESC_P2"),
		Messages.getString("IMC.CONFIGURATION_HINT_WIDGET_DESC_P3")
	});
	public static final String CONFIGURATION_HINT_STEP_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.CONFIGURATION_HINT_STEP_DESC_P1"),
			Messages.getString("IMC.CONFIGURATION_HINT_STEP_DESC_P2")
	});
	public static final String CONFIGURATION_HINT_DOC_RANGE = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.CONFIGURATION_HINT_DOC_RANGE_DESC_P1"),
			Messages.getString("IMC.CONFIGURATION_HINT_DOC_RANGE_DESC_P2")
	});
	public static final String CONFIGURATION_HINT_DOC_CONSTRAINT = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.CONFIGURATION_HINT_DOC_CONTRAINT_DESC_P1"),
			Messages.getString("IMC.CONFIGURATION_HINT_DOC_CONTRAINT_DESC_P2")
	});

	public static final String CONFIGURATION_VALIDATE_NAME1 = Messages.getString("IMessageConstants.CONFIGURATION_VALIDATE_NAME1"); //$NON-NLS-1$
	public static final String CONFIGURATION_VALIDATE_NAME2 = Messages.getString("IMessageConstants.CONFIGURATION_VALIDATE_NAME2"); //$NON-NLS-1$
	public static final String CONFIGURATION_VALIDATE_TYPE = Messages.getString("IMessageConstants.CONFIGURATION_VALIDATE_TYPE"); //$NON-NLS-1$
	public static final String CONFIGURATION_VALIDATE_DEFVALUE = Messages.getString("IMessageConstants.CONFIGURATION_VALIDATE_DEFVALUE"); //$NON-NLS-1$
	public static final String CONFIGURATION_VALIDATE_VARIABLE_P1 = Messages.getString("IMessageConstants.CONFIGURATION_VALIDATE_VARIABLE_P1"); //$NON-NLS-1$
	public static final String CONFIGURATION_VALIDATE_VARIABLE_P2 = Messages.getString("IMessageConstants.CONFIGURATION_VALIDATE_VARIABLE_P2"); //$NON-NLS-1$
	public static final String CONFIGURATION_VALIDATE_VARIABLE = StringUtil.connectMessageWithSepalator( new String[]{CONFIGURATION_VALIDATE_VARIABLE_P1, CONFIGURATION_VALIDATE_VARIABLE_P2});
	public static final String CONFIGURATION_VALIDATE_DUPLICATE = Messages.getString("IMessageConstants.CONFIGURATION_VALIDATE_DUPLICATE"); //$NON-NLS-1$
	public static final String CONFIGURATION_VALIDATE_VAR_DUPLICATE = Messages.getString("IMessageConstants.CONFIGURATION_VALIDATE_VAR_DUPLICATE"); //$NON-NLS-1$

	public static final String CONFIGURATION_VALIDATE_RADIO = Messages.getString("IMessageConstants.CONFIGURATION_VALIDATE_RADIO"); //$NON-NLS-1$
	public static final String CONFIGURATION_VALIDATE_RADIO_DUPRICATE = Messages.getString("IMessageConstants.CONFIGURATION_VALIDATE_RADIO_DUPLICATE"); //$NON-NLS-1$
	public static final String CONFIGURATION_VALIDATE_SPIN = Messages.getString("IMessageConstants.CONFIGURATION_VALIDATE_SPIN"); //$NON-NLS-1$


	public static final String DOCUMENT_HINT_COMPONENT_TITLE = Messages.getString("IMessageConstants.DOCUMENT_HINT_COMPONENT_TITLE"); //$NON-NLS-1$
	public static final String DOCUMENT_HINT_COMPONENT_DESC = Messages.getString("IMessageConstants.DOCUMENT_HINT_COMPONENT_DESC"); //$NON-NLS-1$
	public static final String DOCUMENT_HINT_ETC_TITLE = Messages.getString("IMessageConstants.DOCUMENT_HINT_ETC_TITLE"); //$NON-NLS-1$
	public static final String DOCUMENT_HINT_ETC_DESC = Messages.getString("IMessageConstants.DOCUMENT_HINT_ETC_DESC"); //$NON-NLS-1$
	public static final String DOCUMENT_HINT_CREATOR_TITLE = Messages.getString("IMessageConstants.DOCUMENT_HINT_CREATOR_TITLE"); //$NON-NLS-1$
	public static final String DOCUMENT_HINT_CREATOR_DESC_P1 = Messages.getString("IMessageConstants.DOCUMENT_HINT_CREATOR_DESC_P1"); //$NON-NLS-1$
	public static final String DOCUMENT_HINT_CREATOR_DESC_P2 = Messages.getString("IMessageConstants.DOCUMENT_HINT_CREATOR_DESC_P2"); //$NON-NLS-1$
	public static final String DOCUMENT_HINT_CREATOR_DESC_P3 = Messages.getString("IMessageConstants.DOCUMENT_HINT_CREATOR_DESC_P3"); //$NON-NLS-1$
	public static final String DOCUMENT_HINT_CREATOR_DESC = StringUtil.connectMessageWithSepalator( new String[]{DOCUMENT_HINT_CREATOR_DESC_P1, DOCUMENT_HINT_CREATOR_DESC_P2, DOCUMENT_HINT_CREATOR_DESC_P3});

	public static final String LANGUAGE_SECTION = Messages.getString("IMessageConstants.LANGUAGE_SECTION"); //$NON-NLS-1$
	public static final String LANGUAGE_LANG_TITLE = Messages.getString("IMessageConstants.LANGUAGE_LANG_TITLE"); //$NON-NLS-1$
	public static final String LANGUAGE_ENV_TITLE = Messages.getString("IMessageConstants.LANGUAGE_ENV_TITLE"); //$NON-NLS-1$
	public static final String LANGUAGE_LANG_EXPL = Messages.getString("IMessageConstants.LANGUAGE_LANG_EXPL"); //$NON-NLS-1$
	public static final String LANGUAGE_ENV_EXPL = Messages.getString("IMessageConstants.LANGUAGE_ENV_EXPL"); //$NON-NLS-1$

	public static final String LANGUAGE_HINT_LANG_TITLE = Messages.getString("IMessageConstants.LANGUAGE_HINT_LANG_TITLE"); //$NON-NLS-1$
	public static final String LANGUAGE_HINT_LANG_DESC = Messages.getString("IMessageConstants.LANGUAGE_HINT_LANG_DESC"); //$NON-NLS-1$
	public static final String LANGUAGE_HINT_ENV_TITLE = Messages.getString("IMessageConstants.LANGUAGE_HINT_ENV_TITLE"); //$NON-NLS-1$
	public static final String LANGUAGE_HINT_ENV_DESC_P1 = Messages.getString("IMessageConstants.LANGUAGE_HINT_ENV_DESC_P1"); //$NON-NLS-1$
	public static final String LANGUAGE_HINT_ENV_DESC_P2 = Messages.getString("IMessageConstants.LANGUAGE_HINT_ENV_DESC_P2"); //$NON-NLS-1$
	public static final String LANGUAGE_HINT_ENV_DESC_P3 = Messages.getString("IMessageConstants.LANGUAGE_HINT_ENV_DESC_P3"); //$NON-NLS-1$
	public static final String LANGUAGE_HINT_ENV_DESC_P4 = Messages.getString("IMessageConstants.LANGUAGE_HINT_ENV_DESC_P4"); //$NON-NLS-1$
	public static final String LANGUAGE_HINT_ENV_DESC = StringUtil.connectMessageWithSepalator( new String[]{LANGUAGE_HINT_ENV_DESC_P1, LANGUAGE_HINT_ENV_DESC_P2, LANGUAGE_HINT_ENV_DESC_P3, LANGUAGE_HINT_ENV_DESC_P4});

	public static final String LANGUAGE_LBL_DETAIL = Messages.getString("IMessageConstants.LANGUAGE_LBL_DETAIL"); //$NON-NLS-1$
	public static final String LANGUAGE_LBL_OSETC = Messages.getString("IMessageConstants.LANGUAGE_LBL_OSETC"); //$NON-NLS-1$
	public static final String LANGUAGE_LBL_CPUETC = Messages.getString("IMessageConstants.LANGUAGE_LBL_CPUETC"); //$NON-NLS-1$
	public static final String LANGUAGE_LBL_LIBRARY = Messages.getString("IMessageConstants.LANGUAGE_LBL_LIBRARY"); //$NON-NLS-1$

	public static final String LANGUAGE_SELECTION_CAUTION = Messages.getString("IMessageConstants.LANGUAGE_SELECTION_CAUTION"); //$NON-NLS-1$
	public static final String LANGUAGE_CHOREONOID_CAUTION = Messages.getString("IMessageConstants.LANGUAGE_CHOREONOID_CAUTION"); //$NON-NLS-1$

	public static final String RTCXML_SECTION = Messages.getString("IMessageConstants.RTCXML_SECTION"); //$NON-NLS-1$
	public static final String RTCXML_CAUTION_P1 = Messages.getString("IMessageConstants.RTCXML_CAUTION_P1"); //$NON-NLS-1$
	public static final String RTCXML_CAUTION_P2 = Messages.getString("IMessageConstants.RTCXML_CAUTION_P2"); //$NON-NLS-1$
	public static final String RTCXML_CAUTION = StringUtil.connectMessageWithSepalator( new String[]{RTCXML_CAUTION_P1, RTCXML_CAUTION_P2});

	public static final String RTCXML_MSG_VALIDATE_ERROR = Messages.getString("IMessageConstants.RTCXML_MSG_VALIDATE_ERROR"); //$NON-NLS-1$
	public static final String RTCXML_MSG_UPDATE_ERROR = Messages.getString("IMessageConstants.RTCXML_MSG_UPDATE_ERROR"); //$NON-NLS-1$
	public static final String RTCXML_MSG_UPDATE = Messages.getString("IMessageConstants.RTCXML_MSG_UPDATE"); //$NON-NLS-1$
	public static final String RTCXML_MSG_NOUPDATE = Messages.getString("IMessageConstants.RTCXML_MSG_NOUPDATE"); //$NON-NLS-1$
	public static final String RTCXML_MSG_DONEUPDATE = Messages.getString("IMessageConstants.RTCXML_MSG_DONEUPDATE"); //$NON-NLS-1$
	public static final String RTCXML_MSG_COMPARE = Messages.getString("IMessageConstants.RTCXML_MSG_COMPARE"); //$NON-NLS-1$


	public static final String DOCUMENT_SECTION = Messages.getString("IMessageConstants.DOCUMENT_SECTION"); //$NON-NLS-1$
	public static final String DOCUMENT_OVERVIEW_TITLE = Messages.getString("IMessageConstants.DOCUMENT_OVERVIEW_TITLE"); //$NON-NLS-1$
	public static final String DOCUMENT_ETC_TITLE = Messages.getString("IMessageConstants.DOCUMENT_ETC_TITLE"); //$NON-NLS-1$
	public static final String DOCUMENT_VERSIONUP_LOGS = Messages.getString("IMessageConstants.DOCUMENT_VERSIONUP_LOGS"); //$NON-NLS-1$

	public static final String DOCUMENT_LBL_DESCRIPTION = Messages.getString("IMessageConstants.DOCUMENT_LBL_DESCRIPTION"); //$NON-NLS-1$
	public static final String DOCUMENT_LBL_INOUT = Messages.getString("IMessageConstants.DOCUMENT_LBL_INOUT"); //$NON-NLS-1$
	public static final String DOCUMENT_LBL_ALGORITHM = Messages.getString("IMessageConstants.DOCUMENT_LBL_ALGORITHM"); //$NON-NLS-1$
	public static final String DOCUMENT_LBL_CREATOR = Messages.getString("IMessageConstants.DOCUMENT_LBL_CREATOR"); //$NON-NLS-1$
	public static final String DOCUMENT_LBL_LICENSE = Messages.getString("IMessageConstants.DOCUMENT_LBL_LICENSE"); //$NON-NLS-1$
	public static final String DOCUMENT_LBL_REFERENCE = Messages.getString("IMessageConstants.DOCUMENT_LBL_REFERENCE"); //$NON-NLS-1$

	public static final String ACTIVITY_SECTION = Messages.getString("IMessageConstants.ACTIVITY_SECTION"); //$NON-NLS-1$
	public static final String ACTIVITY_ACTIVITY_TITLE = Messages.getString("IMessageConstants.ACTIVITY_ACTIVITY_TITLE"); //$NON-NLS-1$

	public static final String ACTIVITY_ACTIVITY_EXPL = Messages.getString("IMessageConstants.ACTIVITY_ACTIVITY_EXPL"); //$NON-NLS-1$
	public static final String ACTIVITY_DOCUMENT_EXPL_P1 = Messages.getString("IMessageConstants.ACTIVITY_DOCUMENT_EXPL_P1"); //$NON-NLS-1$
	public static final String ACTIVITY_DOCUMENT_EXPL_P2 = Messages.getString("IMessageConstants.ACTIVITY_DOCUMENT_EXPL_P2"); //$NON-NLS-1$
	public static final String ACTIVITY_DOCUMENT_EXPL = StringUtil.connectMessageWithSepalator( new String[]{ACTIVITY_DOCUMENT_EXPL_P1, ACTIVITY_DOCUMENT_EXPL_P2});

	public static final String ACTIVITY_LBL_INIT_FINAL = Messages.getString("IMessageConstants.ACTIVITY_LBL_INIT_FINAL"); //$NON-NLS-1$
	public static final String ACTIVITY_LBL_START_END = Messages.getString("IMessageConstants.ACTIVITY_LBL_START_END"); //$NON-NLS-1$
	public static final String ACTIVITY_LBL_ALIVE = Messages.getString("IMessageConstants.ACTIVITY_LBL_ALIVE"); //$NON-NLS-1$
	public static final String ACTIVITY_LBL_DATAFLOW = Messages.getString("IMessageConstants.ACTIVITY_LBL_DATAFLOW"); //$NON-NLS-1$
	public static final String ACTIVITY_LBL_ACTIVITYNAME = Messages.getString("IMessageConstants.ACTIVITY_LBL_ACTIVITYNAME"); //$NON-NLS-1$
	public static final String ACTIVITY_LBL_DESCRIPTION = Messages.getString("IMessageConstants.ACTIVITY_LBL_DESCRIPTION"); //$NON-NLS-1$
	public static final String ACTIVITY_LBL_PRECONDITION = Messages.getString("IMessageConstants.ACTIVITY_LBL_PRECONDITION"); //$NON-NLS-1$
	public static final String ACTIVITY_LBL_POSTCONDITION = Messages.getString("IMessageConstants.ACTIVITY_LBL_POSTCONDITION"); //$NON-NLS-1$

	public static final String ACTIVITY_HINT_ONINITIALIZE_DESC = Messages.getString("IMessageConstants.ACTIVITY_HINT_ONINITIALIZE_DESC"); //$NON-NLS-1$
	public static final String ACTIVITY_HINT_ONFINALIZE_DESC = Messages.getString("IMessageConstants.ACTIVITY_HINT_ONFINALIZE_DESC"); //$NON-NLS-1$
	public static final String ACTIVITY_HINT_ONSTARTUP_DESC = Messages.getString("IMessageConstants.ACTIVITY_HINT_ONSTARTUP_DESC"); //$NON-NLS-1$
	public static final String ACTIVITY_HINT_ONSHUTDOWN_DESC = Messages.getString("IMessageConstants.ACTIVITY_HINT_ONSHUTDOWN_DESC"); //$NON-NLS-1$
	public static final String ACTIVITY_HINT_ONACTIVATED_DESC = Messages.getString("IMessageConstants.ACTIVITY_HINT_ONACTIVATED_DESC"); //$NON-NLS-1$
	public static final String ACTIVITY_HINT_ONDEACTIVATED_DESC = Messages.getString("IMessageConstants.ACTIVITY_HINT_ONDEACTIVATED_DESC"); //$NON-NLS-1$
	public static final String ACTIVITY_HINT_ONABORTING_DESC = Messages.getString("IMessageConstants.ACTIVITY_HINT_ONABORTING_DESC"); //$NON-NLS-1$
	public static final String ACTIVITY_HINT_ONERROR_DESC = Messages.getString("IMessageConstants.ACTIVITY_HINT_ONERROR_DESC"); //$NON-NLS-1$
	public static final String ACTIVITY_HINT_ONRESET_DESC_P1 = Messages.getString("IMessageConstants.ACTIVITY_HINT_ONRESET_DESC_P1"); //$NON-NLS-1$
	public static final String ACTIVITY_HINT_ONRESET_DESC_P2 = Messages.getString("IMessageConstants.ACTIVITY_HINT_ONRESET_DESC_P2"); //$NON-NLS-1$
	public static final String ACTIVITY_HINT_ONRESET_DESC = StringUtil.connectMessageWithSepalator( new String[]{ACTIVITY_HINT_ONRESET_DESC_P1, ACTIVITY_HINT_ONRESET_DESC_P2});
	public static final String ACTIVITY_HINT_ONEXECUTE_DESC = Messages.getString("IMessageConstants.ACTIVITY_HINT_ONEXECUTE_DESC"); //$NON-NLS-1$
	public static final String ACTIVITY_HINT_ONSTATEUPDATE_DESC = Messages.getString("IMessageConstants.ACTIVITY_HINT_ONSTATEUPDATE_DESC"); //$NON-NLS-1$
	public static final String ACTIVITY_HINT_ONRATECHANGED_DESC = Messages.getString("IMessageConstants.ACTIVITY_HINT_ONRATECHANGED_DESC"); //$NON-NLS-1$
	public static final String ACTIVITY_HINT_INITIALIZE_IO_DESC = Messages.getString("IMessageConstants.ACTIVITY_HINT_INITIALIZE_IO_DESC"); //$NON-NLS-1$
	public static final String ACTIVITY_HINT_INITIALIZE_SIMULATION_DESC = Messages.getString("IMessageConstants.ACTIVITY_HINT_INITIALIZE_SIMULATION_DESC"); //$NON-NLS-1$
	public static final String ACTIVITY_HINT_START_SIMULATION_DESC = Messages.getString("IMessageConstants.ACTIVITY_HINT_START_SIMULATION_DESC"); //$NON-NLS-1$
	public static final String ACTIVITY_HINT_INPUT_SIMULATION_DESC = Messages.getString("IMessageConstants.ACTIVITY_HINT_INPUT_SIMULATION_DESC"); //$NON-NLS-1$
	public static final String ACTIVITY_HINT_OUTPUT_SIMULATION_DESC = Messages.getString("IMessageConstants.ACTIVITY_HINT_OUTPUT_SIMULATION_DESC"); //$NON-NLS-1$
	public static final String ACTIVITY_HINT_STOP_SIMULATION_DESC = Messages.getString("IMessageConstants.ACTIVITY_HINT_STOP_SIMULATION_DESC"); //$NON-NLS-1$

	public static final String ACTIVITY_HINT_DESCRIPTION_TITLE = Messages.getString("IMessageConstants.ACTIVITY_HINT_DESCRIPTION_TITLE"); //$NON-NLS-1$
	public static final String ACTIVITY_HINT_DESCRIPTION_DESC = Messages.getString("IMessageConstants.ACTIVITY_HINT_DESCRIPTION_DESC"); //$NON-NLS-1$
	public static final String ACTIVITY_HINT_PRECONDITION_TITLE = Messages.getString("IMessageConstants.ACTIVITY_HINT_PRECONDITION_TITLE"); //$NON-NLS-1$
	public static final String ACTIVITY_HINT_PRECONDITION_DESC = Messages.getString("IMessageConstants.ACTIVITY_HINT_PRECONDITION_DESC"); //$NON-NLS-1$
	public static final String ACTIVITY_HINT_POSTCONDITION_TITLE = Messages.getString("IMessageConstants.ACTIVITY_HINT_POSTCONDITION_TITLE"); //$NON-NLS-1$
	public static final String ACTIVITY_HINT_POSTCONDITION_DESC = Messages.getString("IMessageConstants.ACTIVITY_HINT_POSTCONDITION_DESC"); //$NON-NLS-1$


	public static final String PREF_IDL_SELECTION = Messages.getString("IMessageConstants.PREF_IDL_SELECTION"); //$NON-NLS-1$
	public static final String PREF_IDLPARSE_NOFILE = Messages.getString("IMessageConstants.PREF_IDLPARSE_NOFILE"); //$NON-NLS-1$
	public static final String PREF_IDLPARSE_ERROR_P1 = Messages.getString("IMessageConstants.PREF_IDLPARSE_ERROR_P1"); //$NON-NLS-1$
	public static final String PREF_IDLPARSE_ERROR_P2 = Messages.getString("IMessageConstants.PREF_IDLPARSE_ERROR_P2"); //$NON-NLS-1$
	public static final String PREF_IDLPARSE_ERROR = StringUtil.connectMessageWithSepalator( new String[]{PREF_IDLPARSE_ERROR_P1, PREF_IDLPARSE_ERROR_P2});
	public static final String FILETYPE_XML = Messages.getString("IMessageConstants.FILETYPE_XML"); //$NON-NLS-1$
	public static final String PROFILE_COMPARE_MESSAGE_P1 = Messages.getString("IMessageConstants.PROFILE_COMPARE_MESSAGE_P1"); //$NON-NLS-1$
	public static final String PROFILE_COMPARE_MESSAGE_P2 = Messages.getString("IMessageConstants.PROFILE_COMPARE_MESSAGE_P2"); //$NON-NLS-1$
	public static final String PROFILE_COMPARE_MESSAGE = StringUtil.connectMessageWithSepalator( new String[]{PROFILE_COMPARE_MESSAGE_P1, PROFILE_COMPARE_MESSAGE_P2});

	public static final String CREATE_FILE_ERROR = Messages.getString("IMessageConstants.CREATE_FILE_ERROR"); //$NON-NLS-1$
	public static final String FILETYPE_YAML = Messages.getString("IMessageConstants.FILETYPE_YAML"); //$NON-NLS-1$
	public static final String COMPARE_TITLE = Messages.getString("IMessageConstants.COMPARE_TITLE"); //$NON-NLS-1$
	public static final String COMMON_LABEL_UPDATE = Messages.getString("IMessageConstants.COMMON_LABEL_UPDATE"); //$NON-NLS-1$

	public static final String BACKUP_FILE_NUM = Messages.getString("IMessageConstants.BACKUP_FILE_NUM"); //$NON-NLS-1$
	
	public static final String TITLE_FONT = Messages.getString("IMessageConstants.TITLE_FONT"); //$NON-NLS-1$

	public static final String PROFILE_VALIDATE_ERROR_MESSAGE = Messages.getString("IMessageConstants.PROFILE_VALIDATE_ERROR_MESSAGE"); //$NON-NLS-1$

	public static final String SELECT_DIRECTORY = Messages.getString("IMessageConstants.SELECT_DIRECTORY"); //$NON-NLS-1$
	
	public static final String SAVE_MESSAGE = Messages.getString("IMessageConstants.SAVE_MESSAGE"); //$NON-NLS-1$
	public static final String IDL_PARSE_EROOR = Messages.getString("IMessageConstants.IDL_PARSE_EROOR"); //$NON-NLS-1$
	
	public static final String FSM_HINT_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.FSM_HINT_DESC_P1"),
			Messages.getString("IMC.FSM_HINT_DESC_P2")});

	public static final String FSM_STATIC_HINT_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.FSM_STATIC_HINT_DESC_P1"),
			Messages.getString("IMC.FSM_STATIC_HINT_DESC_P2"),
			Messages.getString("IMC.FSM_STATIC_HINT_DESC_P3"),
			Messages.getString("IMC.FSM_STATIC_HINT_DESC_P4"),
			Messages.getString("IMC.FSM_STATIC_HINT_DESC_P5")});
	
	public static final String FSM_DYNAMIC_HINT_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.FSM_DYNAMIC_HINT_DESC_P1"),
			Messages.getString("IMC.FSM_DYNAMIC_HINT_DESC_P2"),
			Messages.getString("IMC.FSM_DYNAMIC_HINT_DESC_P3"),
			Messages.getString("IMC.FSM_DYNAMIC_HINT_DESC_P4"),
			Messages.getString("IMC.FSM_DYNAMIC_HINT_DESC_P5")});
	
	public static final String FSM_SCXML_NEW_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.FSM_SCXML_NEW_DESC_P1"),
			Messages.getString("IMC.FSM_SCXML_NEW_DESC_P2"),
			Messages.getString("IMC.FSM_SCXML_NEW_DESC_P3")});
	
	public static final String FSM_SCXML_EDIT_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.FSM_SCXML_EDIT_DESC_P1"),
			Messages.getString("IMC.FSM_SCXML_EDIT_DESC_P2")});
	
	public static final String FSM_SCXML_IMPORT_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.FSM_SCXML_IMPORT_DESC_P1"),
			Messages.getString("IMC.FSM_SCXML_IMPORT_DESC_P2")});
	
	public static final String FSM_PORT_NAME_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.FSM_HINT_PORT_NAME_DESC_1"),
			Messages.getString("IMC.FSM_HINT_PORT_NAME_DESC_2")});
	
	public static final String FSM_SORCE_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.FSM_HINT_SOURCE_DESC_1"),
			Messages.getString("IMC.FSM_HINT_SOURCE_DESC_2")});
	
	public static final String FSM_TARGET_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.FSM_HINT_TARGET_DESC_1"),
			Messages.getString("IMC.FSM_HINT_TARGET_DESC_2")});
	
	public static final String FSM_DOC_DETAIL_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.FSM_HINT_DOC_DETAIL_DESC_1"),
			Messages.getString("IMC.FSM_HINT_DOC_DETAIL_DESC_2")});
	/////
	public static final String CONTAINERC_HINT_MODULENAME_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.CONTAINER_HINT_MODULE_NAME_DESC_P1"),
			Messages.getString("IMC.CONTAINER_HINT_MODULE_NAME_DESC_P2")});
	public static final String CONTAINERC_HINT_NODENAME_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.CONTAINER_HINT_NODE_NAME_DESC_P1"),
			Messages.getString("IMC.CONTAINER_HINT_MODULE_NAME_DESC_P2")});

	public static final String CONTAINERC_HINT_DESCRIPTION_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.CONTAINER_HINT_DESCRIPTION_DESC_P1"),
			Messages.getString("IMC.CONTAINER_HINT_DESCRIPTION_DESC_P2")});

	public static final String CONTAINERC_HINT_WORKSPACE_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.CONTAINER_HINT_WORKSPACE_DESC_P1"),
			Messages.getString("IMC.CONTAINER_HINT_WORKSPACE_DESC_P2")});

	public static final String CONTAINERC_HINT_CONFIGURATION_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.CONTAINER_HINT_CONFIGURATION_DESC_P1"),
			Messages.getString("IMC.CONTAINER_HINT_CONFIGURATION_DESC_P2"),
			Messages.getString("IMC.CONTAINER_HINT_CONFIGURATION_DESC_P3")});

	public static final String CONTAINERC_HINT_LIBRARY_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.CONTAINER_HINT_LIBRARY_DESC_P1"),
			Messages.getString("IMC.CONTAINER_HINT_LIBRARY_DESC_P2"),
			Messages.getString("IMC.CONTAINER_HINT_LIBRARY_DESC_P3")});

	public static final String CONTAINERC_HINT_URL_DESC = StringUtil.connectMessageWithSepalator( new String[]{
			Messages.getString("IMC.CONTAINER_HINT_URL_DESC_P1"),
			Messages.getString("IMC.CONTAINER_HINT_URL_DESC_P2")});
}
