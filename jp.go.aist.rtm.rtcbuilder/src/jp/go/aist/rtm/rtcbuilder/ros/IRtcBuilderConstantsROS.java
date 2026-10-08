package jp.go.aist.rtm.rtcbuilder.ros;


public interface IRtcBuilderConstantsROS {
	public static final String SCHEMA_VERSION_ROS = "0.1";
	
	public static final String LICENSE_APACHE = "Apache-2.0";
	public static final String LICENSE_MIT = "MIT";
	public static final String LICENSE_BSD = "BSD-3-Clause";
	public static final String LICENSE_GPL = "GPL-3.0";
	public static final String LICENSE_LGPL = "LGPL-3.0";
	public static final String LICENSE_PROPRIETARY = "Proprietary";

	public static final String SPEC_TOPIC_SUBSCRIBE = "Subscribe";
	public static final String SPEC_TOPIC_PUBLISH = "Publish";
	public static final String SPEC_SERVICE_SERVER = "Server";
	public static final String SPEC_SERVICE_CLIENT = "Client";
	public static final String SPEC_ACTION_SERVER = "ActionServer";
	public static final String SPEC_ACTION_CLIENT = "ActionClient";

	public static final String DEFAULT_ROS_XML = "ROS.xml";

	public static final String[] ACTION_TYPE_ITEMS = new String[] {
			"on_configure", "on_activate", "on_deactivate", "on_cleanup", "on_shutdown", "on_error"};

	public static final int ACTIVITY_CONFIGURE = 0; 
	public static final int ACTIVITY_ACTIVATE = 1;
	public static final int ACTIVITY_DEACTIVATE = 2; 
	public static final int ACTIVITY_CLEANUP = 3;
	public static final int ACTIVITY_SHUTDOWN = 4;
	public static final int ACTIVITY_ERROR = 5;
	public static final int ACTIVITY_DUMMY = 6;

	public static final int ACTIVITY_CAN_EDIT_NUM = 3;
}
