package jp.go.aist.rtm.rtcbuilder._test.ROS;

import java.util.List;

import jp.go.aist.rtm.rtcbuilder.Generator;
import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder._test.TestBase;
import jp.go.aist.rtm.rtcbuilder.generator.GeneratedResult;
import jp.go.aist.rtm.rtcbuilder.generator.param.GeneratorParam;
import jp.go.aist.rtm.rtcbuilder.ros.IRtcBuilderConstantsROS;
import jp.go.aist.rtm.rtcbuilder.ros.manager.CXXGenerateManager;
import jp.go.aist.rtm.rtcbuilder.ros.manager.CommonGenerateManager;
import jp.go.aist.rtm.rtcbuilder.ros.param.ActionParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.PackageParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.ParameterParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.ROSParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.ServiceParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.TimerParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.TopicParam;

public class DocumentTest extends TestBase {

	ROSParam rosParam;
	GeneratorParam genParam;

	protected void setUp() throws Exception {
		genParam = new GeneratorParam();
		rosParam = new ROSParam(genParam, true);
		rosParam.setOutputProject(rootPath + "/resource/work");
		rosParam.setLanguage(IRtcBuilderConstants.LANG_CPP);
		rosParam.setLanguageArg(IRtcBuilderConstants.LANG_CPP_ARG);
		genParam.setROSParam(rosParam);
	}

	public void test01Minimal() throws Exception {
		rosParam.setPackageName("eval1_minimal_cpp_pkg");
		rosParam.setNodeName("eval1_minimal_node");
		rosParam.setClassName("Eval1MinimalNode");
		rosParam.setDescription("Minimal C++ Lifecycle Node sample.");
		rosParam.setVersion("0.0.1");
		rosParam.setMaintainer("rsdlab");
		rosParam.setContactAddress("todo@example.com");
		rosParam.setLicense("Apache-2.0");
		
		rosParam.setDocAlgorithm("AlgorithmSample");
		rosParam.setDocInOut("InOutSample");
		rosParam.setDocCreator("CreatorSample");
		rosParam.setDocReference("ReferSample");
		
		Generator generator = new Generator();
		generator.clearGenerateManager();
		generator.addGenerateManager(new CommonGenerateManager());
		generator.addGenerateManager(new CXXGenerateManager());

		List<GeneratedResult> result = generator.generateTemplateCodeROS(genParam);

		String resourceDir = rootPath + "/resource/ROS/Document/eval1_minimal_cpp_pkg_project/";

		//eval1_minimal_cpp_pkg
		checkCode(result, resourceDir, "eval1_minimal_cpp_pkg/README.md");
		checkCode(result, resourceDir, "eval1_minimal_cpp_pkg/package.xml");
		checkCode(result, resourceDir, "eval1_minimal_cpp_pkg/CMakeLists.txt");
		checkCode(result, resourceDir, "eval1_minimal_cpp_pkg/LICENSE");
		checkCode(result, resourceDir, "eval1_minimal_cpp_pkg/config/params.yaml");
		checkCode(result, resourceDir, "eval1_minimal_cpp_pkg/launch/eval1_minimal_node.launch.py");
		checkCode(result, resourceDir, "eval1_minimal_cpp_pkg/include/eval1_minimal_cpp_pkg/eval1_minimal_node.hpp");
		checkCode(result, resourceDir, "eval1_minimal_cpp_pkg/src/main.cpp");
		checkCode(result, resourceDir, "eval1_minimal_cpp_pkg/src/eval1_minimal_node.cpp");
		//eval1_minimal_cpp_pkg_interfaces
		checkCode(result, resourceDir, "eval1_minimal_cpp_pkg_interfaces/README.md");
		checkCode(result, resourceDir, "eval1_minimal_cpp_pkg_interfaces/package.xml");
		checkCode(result, resourceDir, "eval1_minimal_cpp_pkg_interfaces/CMakeLists.txt");
		checkCode(result, resourceDir, "eval1_minimal_cpp_pkg_interfaces/LICENSE");
	}
	
	public void test02CmdSource() throws Exception {
		rosParam.setPackageName("eval2_cmd_source_cpp_pkg");
		rosParam.setNodeName("eval2_cmd_source_node");
		rosParam.setClassName("Eval2CmdSourceNode");
		rosParam.setDescription("C++ command source node that publishes cmd_vel and subscribes robot_status for evaluation.");
		rosParam.setVersion("0.0.1");
		rosParam.setMaintainer("rsdlab");
		rosParam.setContactAddress("todo@example.com");
		rosParam.setLicense("BSD-3-Clause");
		
		TopicParam topic1 = new TopicParam();
		topic1.setName("cmd_vel");
		topic1.setMessageType("geometry_msgs/msg/Twist");
		topic1.setVarCallbackName("cmd_vel_pub");
		topic1.setReliabilityType("Reliable");
		topic1.setHistoryType("Keep Last");
		topic1.setDepth(10);
		topic1.setDocDescription("Publish velocity command.");
		topic1.setDocType("データの型");
		topic1.setDocSemantics("データの意味");
		topic1.setDocUnit("データの単位");
		topic1.setDocOccurrence("データの発生頻度");
		rosParam.getTopicPublishes().add(topic1);
		
		TopicParam topic2 = new TopicParam();
		topic2.setName("robot_status");
		topic2.setMessageType("std_msgs/msg/String");
		topic2.setVarCallbackName("robot_status_callback");
		topic2.setReliabilityType("Reliable");
		topic2.setHistoryType("Keep Last");
		topic2.setDepth(10);
		topic2.setDocDescription("Receive robot status text.");
		topic2.setDocType("データの型2");
		topic2.setDocSemantics("データの意味3");
		topic2.setDocUnit("データの単位4");
		topic2.setDocOccurrence("データの発生頻度5");
		rosParam.getTopicSubscribes().add(topic2);
		
		TimerParam timer = new TimerParam();
		timer.setName("cmd_timer");
		timer.setRate(1.0);
		timer.setCallBack("cmd_timer_callback");
		timer.setDescription("Publish velocity command periodically.");
		rosParam.getTimers().add(timer);

		Generator generator = new Generator();
		generator.clearGenerateManager();
		generator.addGenerateManager(new CommonGenerateManager());
		generator.addGenerateManager(new CXXGenerateManager());

		List<GeneratedResult> result = generator.generateTemplateCodeROS(genParam);

		String resourceDir = rootPath + "/resource/ROS/Document/eval2_cmd_source_cpp_pkg_project/";

		//eval2_cmd_source_cpp_pkg
		checkCode(result, resourceDir, "eval2_cmd_source_cpp_pkg/README.md");
		checkCode(result, resourceDir, "eval2_cmd_source_cpp_pkg/package.xml");
		checkCode(result, resourceDir, "eval2_cmd_source_cpp_pkg/CMakeLists.txt");
		checkCode(result, resourceDir, "eval2_cmd_source_cpp_pkg/LICENSE");
		checkCode(result, resourceDir, "eval2_cmd_source_cpp_pkg/config/params.yaml");
		checkCode(result, resourceDir, "eval2_cmd_source_cpp_pkg/launch/eval2_cmd_source_node.launch.py");
		checkCode(result, resourceDir, "eval2_cmd_source_cpp_pkg/include/eval2_cmd_source_cpp_pkg/eval2_cmd_source_node.hpp");
		checkCode(result, resourceDir, "eval2_cmd_source_cpp_pkg/src/main.cpp");
		checkCode(result, resourceDir, "eval2_cmd_source_cpp_pkg/src/eval2_cmd_source_node.cpp");
		//eval2_cmd_source_cpp_pkg_interfaces
		checkCode(result, resourceDir, "eval2_cmd_source_cpp_pkg_interfaces/README.md");
		checkCode(result, resourceDir, "eval2_cmd_source_cpp_pkg_interfaces/package.xml");
		checkCode(result, resourceDir, "eval2_cmd_source_cpp_pkg_interfaces/CMakeLists.txt");
		checkCode(result, resourceDir, "eval2_cmd_source_cpp_pkg_interfaces/LICENSE");
	}
	
	public void test02LifeCycleCallback() throws Exception {
		rosParam.setPackageName("eval2_controller_cpp_pkg");
		rosParam.setNodeName("eval2_controller_node");
		rosParam.setClassName("Eval2ControllerNode");
		rosParam.setDescription("C++ controller node with cmd_vel subscription, robot_status publisher, timer, and multiple parameters.");
		rosParam.setVersion("0.0.1");
		rosParam.setMaintainer("maintainer");
		rosParam.setContactAddress("todo@example.com");
		rosParam.setLicense("Apache-2.0");
		
		rosParam.setDocActionOverView(IRtcBuilderConstantsROS.ACTIVITY_CONFIGURE, "Desc1");
		rosParam.setDocActionPreCondition(IRtcBuilderConstantsROS.ACTIVITY_CONFIGURE, "preCond1");
		rosParam.setDocActionPostCondition(IRtcBuilderConstantsROS.ACTIVITY_CONFIGURE, "postCond1");

		rosParam.setDocActionOverView(IRtcBuilderConstantsROS.ACTIVITY_ACTIVATE, "Desc2");
		rosParam.setDocActionPreCondition(IRtcBuilderConstantsROS.ACTIVITY_ACTIVATE, "preCond2");
		rosParam.setDocActionPostCondition(IRtcBuilderConstantsROS.ACTIVITY_ACTIVATE, "postCond2");

		rosParam.setDocActionOverView(IRtcBuilderConstantsROS.ACTIVITY_DEACTIVATE, "Desc3");
		rosParam.setDocActionPreCondition(IRtcBuilderConstantsROS.ACTIVITY_DEACTIVATE, "preCond3");
		rosParam.setDocActionPostCondition(IRtcBuilderConstantsROS.ACTIVITY_DEACTIVATE, "postCond3");

		rosParam.setActionImplemented(IRtcBuilderConstantsROS.ACTIVITY_CLEANUP, true);
		rosParam.setDocActionOverView(IRtcBuilderConstantsROS.ACTIVITY_CLEANUP, "Desc4");
		rosParam.setDocActionPreCondition(IRtcBuilderConstantsROS.ACTIVITY_CLEANUP, "preCond4");
		rosParam.setDocActionPostCondition(IRtcBuilderConstantsROS.ACTIVITY_CLEANUP, "postCond4");

		rosParam.setActionImplemented(IRtcBuilderConstantsROS.ACTIVITY_SHUTDOWN, true);
		rosParam.setDocActionOverView(IRtcBuilderConstantsROS.ACTIVITY_SHUTDOWN, "Desc5");
		rosParam.setDocActionPreCondition(IRtcBuilderConstantsROS.ACTIVITY_SHUTDOWN, "preCond5");
		rosParam.setDocActionPostCondition(IRtcBuilderConstantsROS.ACTIVITY_SHUTDOWN, "postCond5");

		rosParam.setActionImplemented(IRtcBuilderConstantsROS.ACTIVITY_ERROR, true);
		rosParam.setDocActionOverView(IRtcBuilderConstantsROS.ACTIVITY_ERROR, "Desc6");
		rosParam.setDocActionPreCondition(IRtcBuilderConstantsROS.ACTIVITY_ERROR, "preCond6");
		rosParam.setDocActionPostCondition(IRtcBuilderConstantsROS.ACTIVITY_ERROR, "postCond6");
		
		TopicParam topic1 = new TopicParam();
		topic1.setName("robot_status");
		topic1.setMessageType("std_msgs/msg/String");
		topic1.setVarCallbackName("robot_status_publisher");
		topic1.setReliabilityType("Reliable");
		topic1.setHistoryType("Keep Last");
		topic1.setDepth(10);
		topic1.setDocDescription("Publish robot status text.");
		rosParam.getTopicPublishes().add(topic1);
		
		TopicParam topic2 = new TopicParam();
		topic2.setName("cmd_vel");
		topic2.setMessageType("geometry_msgs/msg/Twist");
		topic2.setVarCallbackName("cmd_vel_callback");
		topic2.setReliabilityType("Reliable");
		topic2.setHistoryType("Keep Last");
		topic2.setDepth(10);
		topic2.setDocDescription("Receive velocity command.");
		rosParam.getTopicSubscribes().add(topic2);
		
		TimerParam timer = new TimerParam();
		timer.setName("status_timer");
		timer.setRate(1.0);
		timer.setCallBack("status_timer_callback");
		timer.setDescription("Publish status periodically.");
		rosParam.getTimers().add(timer);
		
		ParameterParam param1 = new ParameterParam();
		param1.setName("max_speed");
		param1.setType("double");
		param1.setDefaultValue("1.0");
		param1.setMin(0.0);
		param1.setMax(5.0);
		param1.setStep(0.1);
		param1.setDocDescription("Maximum speed.");
		rosParam.getParameters().add(param1);

		ParameterParam param2 = new ParameterParam();
		param2.setName("status_prefix");
		param2.setType("string");
		param2.setDefaultValue("status");
		param2.setReadOnly(true);
		param2.setDocDescription("Status prefix.");
		rosParam.getParameters().add(param2);

		ParameterParam param3 = new ParameterParam();
		param3.setName("enable_safety_limit");
		param3.setType("bool");
		param3.setDefaultValue("true");
		param3.setDocDescription("Enable safety limit.");
		rosParam.getParameters().add(param3);

		Generator generator = new Generator();
		generator.clearGenerateManager();
		generator.addGenerateManager(new CommonGenerateManager());
		generator.addGenerateManager(new CXXGenerateManager());

		List<GeneratedResult> result = generator.generateTemplateCodeROS(genParam);

		String resourceDir = rootPath + "/resource/ROS/Document/eval2_controller_cpp_pkg_project_cb/";

		//eval2_controller_cpp_pkg
		checkCode(result, resourceDir, "eval2_controller_cpp_pkg/README.md");
		checkCode(result, resourceDir, "eval2_controller_cpp_pkg/package.xml");
		checkCode(result, resourceDir, "eval2_controller_cpp_pkg/CMakeLists.txt");
		checkCode(result, resourceDir, "eval2_controller_cpp_pkg/LICENSE");
		checkCode(result, resourceDir, "eval2_controller_cpp_pkg/config/params.yaml");
		checkCode(result, resourceDir, "eval2_controller_cpp_pkg/launch/eval2_controller_node.launch.py");
		checkCode(result, resourceDir, "eval2_controller_cpp_pkg/include/eval2_controller_cpp_pkg/eval2_controller_node.hpp");
		checkCode(result, resourceDir, "eval2_controller_cpp_pkg/src/main.cpp");
		checkCode(result, resourceDir, "eval2_controller_cpp_pkg/src/eval2_controller_node.cpp");
		//eval2_controller_cpp_pkg_interfaces
		checkCode(result, resourceDir, "eval2_controller_cpp_pkg_interfaces/README.md");
		checkCode(result, resourceDir, "eval2_controller_cpp_pkg_interfaces/package.xml");
		checkCode(result, resourceDir, "eval2_controller_cpp_pkg_interfaces/CMakeLists.txt");
		checkCode(result, resourceDir, "eval2_controller_cpp_pkg_interfaces/LICENSE");
	}
	
	public void test03ServiceClient() throws Exception {
		rosParam.setPackageName("eval3_service_client_cpp_pkg");
		rosParam.setNodeName("eval3_service_client_node");
		rosParam.setClassName("Eval3ServiceClientNode");
		rosParam.setDescription("C++ service client node that calls Trigger and SetBool services from a timer.");
		rosParam.setVersion("0.0.1");
		rosParam.setMaintainer("rsdlab");
		rosParam.setContactAddress("todo@example.com");
		rosParam.setLicense("Apache-2.0");
		
		ServiceParam service01 = new ServiceParam();
		service01.setName("reset");
		service01.setType("std_srvs/srv/Trigger");
		service01.setVarCallbackName("reset_client");
		service01.setDocDescription("Call reset service.");
		service01.setDocArgument("DocArg01");
		service01.setDocReturn("DocRet01");
		rosParam.getServiceClients().add(service01);
		
		ServiceParam service02 = new ServiceParam();
		service02.setName("set_bool");
		service02.setType("std_srvs/srv/SetBool");
		service02.setVarCallbackName("set_bool_client");
		service02.setDocDescription("Call SetBool service.");
		service02.setDocArgument("DocArg02");
		service02.setDocReturn("DocRet02");
		rosParam.getServiceClients().add(service02);

		TimerParam timer = new TimerParam();
		timer.setName("service_call_timer");
		timer.setRate(2.0);
		timer.setCallBack("service_call_timer_callback");
		timer.setDescription("Call services periodically.");
		rosParam.getTimers().add(timer);
		
		Generator generator = new Generator();
		generator.clearGenerateManager();
		generator.addGenerateManager(new CommonGenerateManager());
		generator.addGenerateManager(new CXXGenerateManager());

		List<GeneratedResult> result = generator.generateTemplateCodeROS(genParam);

		String resourceDir = rootPath + "/resource/ROS/Document/eval3_service_client_cpp_pkg_project/";

		//eval3_service_client_cpp_pkg
		checkCode(result, resourceDir, "eval3_service_client_cpp_pkg/README.md");
		checkCode(result, resourceDir, "eval3_service_client_cpp_pkg/package.xml");
		checkCode(result, resourceDir, "eval3_service_client_cpp_pkg/CMakeLists.txt");
		checkCode(result, resourceDir, "eval3_service_client_cpp_pkg/LICENSE");
		checkCode(result, resourceDir, "eval3_service_client_cpp_pkg/config/params.yaml");
		checkCode(result, resourceDir, "eval3_service_client_cpp_pkg/launch/eval3_service_client_node.launch.py");
		checkCode(result, resourceDir, "eval3_service_client_cpp_pkg/include/eval3_service_client_cpp_pkg/eval3_service_client_node.hpp");
		checkCode(result, resourceDir, "eval3_service_client_cpp_pkg/src/main.cpp");
		checkCode(result, resourceDir, "eval3_service_client_cpp_pkg/src/eval3_service_client_node.cpp");
		//eval3_service_client_cpp_pkg_interfaces
		checkCode(result, resourceDir, "eval3_service_client_cpp_pkg_interfaces/README.md");
		checkCode(result, resourceDir, "eval3_service_client_cpp_pkg_interfaces/package.xml");
		checkCode(result, resourceDir, "eval3_service_client_cpp_pkg_interfaces/CMakeLists.txt");
		checkCode(result, resourceDir, "eval3_service_client_cpp_pkg_interfaces/LICENSE");
	}
	
	public void test03ServiceServer() throws Exception {
		rosParam.setPackageName("eval3_service_server_cpp_pkg");
		rosParam.setNodeName("eval3_service_server_node");
		rosParam.setClassName("Eval3ServiceServerNode");
		rosParam.setDescription("C++ service server node with Trigger and SetBool services.");
		rosParam.setVersion("0.0.1");
		rosParam.setMaintainer("rsdlab");
		rosParam.setContactAddress("todo@example.com");
		rosParam.setLicense("Apache-2.0");
		
		ServiceParam service01 = new ServiceParam();
		service01.setName("reset");
		service01.setType("std_srvs/srv/Trigger");
		service01.setVarCallbackName("handle_reset");
		service01.setDocDescription("Reset internal state.");
		service01.setDocArgument("Doc Arg Server1");
		service01.setDocReturn("Doc Ret Server1");
		rosParam.getServiceServers().add(service01);
		
		ServiceParam service02 = new ServiceParam();
		service02.setName("set_bool");
		service02.setType("std_srvs/srv/SetBool");
		service02.setVarCallbackName("handle_set_bool");
		service02.setDocDescription("Set boolean state.");
		service02.setDocArgument("Doc Arg Server2");
		service02.setDocReturn("Doc Ret Server2");
		rosParam.getServiceServers().add(service02);

		Generator generator = new Generator();
		generator.clearGenerateManager();
		generator.addGenerateManager(new CommonGenerateManager());
		generator.addGenerateManager(new CXXGenerateManager());

		List<GeneratedResult> result = generator.generateTemplateCodeROS(genParam);

		String resourceDir = rootPath + "/resource/ROS/Document/eval3_service_server_cpp_pkg_project/";

		//eval3_service_server_cpp_pkg
		checkCode(result, resourceDir, "eval3_service_server_cpp_pkg/README.md");
		checkCode(result, resourceDir, "eval3_service_server_cpp_pkg/package.xml");
		checkCode(result, resourceDir, "eval3_service_server_cpp_pkg/CMakeLists.txt");
		checkCode(result, resourceDir, "eval3_service_server_cpp_pkg/LICENSE");
		checkCode(result, resourceDir, "eval3_service_server_cpp_pkg/config/params.yaml");
		checkCode(result, resourceDir, "eval3_service_server_cpp_pkg/launch/eval3_service_server_node.launch.py");
		checkCode(result, resourceDir, "eval3_service_server_cpp_pkg/include/eval3_service_server_cpp_pkg/eval3_service_server_node.hpp");
		checkCode(result, resourceDir, "eval3_service_server_cpp_pkg/src/main.cpp");
		checkCode(result, resourceDir, "eval3_service_server_cpp_pkg/src/eval3_service_server_node.cpp");
		//eval3_service_server_cpp_pkg_interfaces
		checkCode(result, resourceDir, "eval3_service_server_cpp_pkg_interfaces/README.md");
		checkCode(result, resourceDir, "eval3_service_server_cpp_pkg_interfaces/package.xml");
		checkCode(result, resourceDir, "eval3_service_server_cpp_pkg_interfaces/CMakeLists.txt");
		checkCode(result, resourceDir, "eval3_service_server_cpp_pkg_interfaces/LICENSE");
	}
	
	public void test04ActionClient() throws Exception {
		rosParam.setPackageName("eval4_action_client_cpp_pkg");
		rosParam.setNodeName("eval4_action_client_node");
		rosParam.setClassName("Eval4ActionClientNode");
		rosParam.setDescription("C++ action client node that sends Fibonacci goals periodically.");
		rosParam.setVersion("0.0.1");
		rosParam.setMaintainer("rsdlab");
		rosParam.setContactAddress("todo@example.com");
		rosParam.setLicense("GPL-3.0");
		
		ActionParam action = new ActionParam();
		action.setName("fibonacci");
		action.setType("example_interfaces/action/Fibonacci");
		action.setCallbackName("fibonacci_action");
		action.setDocDescription("Send Fibonacci action goals.");
		action.setDocGoal("Doc Goal");
		action.setDocFeedback("Doc Feedback");
		action.setDocResult("Doc Return");
		rosParam.getActionClients().add(action);

		TimerParam timer = new TimerParam();
		timer.setName("action_goal_timer");
		timer.setRate(3.0);
		timer.setCallBack("action_goal_timer_callback");
		timer.setDescription("Trigger action goal helper periodically.");
		rosParam.getTimers().add(timer);
		
		Generator generator = new Generator();
		generator.clearGenerateManager();
		generator.addGenerateManager(new CommonGenerateManager());
		generator.addGenerateManager(new CXXGenerateManager());

		List<GeneratedResult> result = generator.generateTemplateCodeROS(genParam);

		String resourceDir = rootPath + "/resource/ROS/Document/eval4_action_client_cpp_pkg_project/";

		//eval4_action_client_cpp_pkg
		checkCode(result, resourceDir, "eval4_action_client_cpp_pkg/README.md");
		checkCode(result, resourceDir, "eval4_action_client_cpp_pkg/package.xml");
		checkCode(result, resourceDir, "eval4_action_client_cpp_pkg/CMakeLists.txt");
		checkCode(result, resourceDir, "eval4_action_client_cpp_pkg/LICENSE");
		checkCode(result, resourceDir, "eval4_action_client_cpp_pkg/config/params.yaml");
		checkCode(result, resourceDir, "eval4_action_client_cpp_pkg/launch/eval4_action_client_node.launch.py");
		checkCode(result, resourceDir, "eval4_action_client_cpp_pkg/include/eval4_action_client_cpp_pkg/eval4_action_client_node.hpp");
		checkCode(result, resourceDir, "eval4_action_client_cpp_pkg/src/main.cpp");
		checkCode(result, resourceDir, "eval4_action_client_cpp_pkg/src/eval4_action_client_node.cpp");
		//eval4_action_client_cpp_pkg_interfaces
		checkCode(result, resourceDir, "eval4_action_client_cpp_pkg_interfaces/README.md");
		checkCode(result, resourceDir, "eval4_action_client_cpp_pkg_interfaces/package.xml");
		checkCode(result, resourceDir, "eval4_action_client_cpp_pkg_interfaces/CMakeLists.txt");
		checkCode(result, resourceDir, "eval4_action_client_cpp_pkg_interfaces/LICENSE");
	}
	
	public void test04ActionServer() throws Exception {
		rosParam.setPackageName("eval4_action_server_cpp_pkg");
		rosParam.setNodeName("eval4_action_server_node");
		rosParam.setClassName("Eval4ActionServerNode");
		rosParam.setDescription("C++ action server node using example_interfaces Fibonacci action.");
		rosParam.setVersion("0.0.1");
		rosParam.setMaintainer("rsdlab");
		rosParam.setContactAddress("todo@example.com");
		rosParam.setLicense("GPL-3.0");
		
		ActionParam action = new ActionParam();
		action.setName("fibonacci");
		action.setType("example_interfaces/action/Fibonacci");
		action.setCallbackName("execute_fibonacci");
		action.setDocDescription("Execute Fibonacci action goals.");
		action.setDocGoal("Goal Status");
		action.setDocFeedback("Feedback value");
		action.setDocResult("Result value");
		rosParam.getActionServers().add(action);

		Generator generator = new Generator();
		generator.clearGenerateManager();
		generator.addGenerateManager(new CommonGenerateManager());
		generator.addGenerateManager(new CXXGenerateManager());

		List<GeneratedResult> result = generator.generateTemplateCodeROS(genParam);

		String resourceDir = rootPath + "/resource/ROS/Document/eval4_action_server_cpp_pkg_project/";

		//eval4_action_server_cpp_pkg
		checkCode(result, resourceDir, "eval4_action_server_cpp_pkg/README.md");
		checkCode(result, resourceDir, "eval4_action_server_cpp_pkg/package.xml");
		checkCode(result, resourceDir, "eval4_action_server_cpp_pkg/CMakeLists.txt");
//		checkCode(result, resourceDir, "LICENSE");
		checkCode(result, resourceDir, "eval4_action_server_cpp_pkg/config/params.yaml");
		checkCode(result, resourceDir, "eval4_action_server_cpp_pkg/launch/eval4_action_server_node.launch.py");
		checkCode(result, resourceDir, "eval4_action_server_cpp_pkg/include/eval4_action_server_cpp_pkg/eval4_action_server_node.hpp");
		checkCode(result, resourceDir, "eval4_action_server_cpp_pkg/src/main.cpp");
		checkCode(result, resourceDir, "eval4_action_server_cpp_pkg/src/eval4_action_server_node.cpp");
		//eval4_action_server_cpp_pkg_interfaces
		checkCode(result, resourceDir, "eval4_action_server_cpp_pkg_interfaces/README.md");
		checkCode(result, resourceDir, "eval4_action_server_cpp_pkg_interfaces/package.xml");
		checkCode(result, resourceDir, "eval4_action_server_cpp_pkg_interfaces/CMakeLists.txt");
		checkCode(result, resourceDir, "eval4_action_server_cpp_pkg_interfaces/LICENSE");
	}
	
	public void test05Operator() throws Exception {
		rosParam.setPackageName("eval5_operator_cpp_pkg");
		rosParam.setNodeName("eval5_operator_node");
		rosParam.setClassName("Eval5OperatorNode");
		rosParam.setDescription("C++ operator-side all-in-one sample node that uses the robot interface package.");
		rosParam.setVersion("0.0.1");
		rosParam.setMaintainer("rsdlab");
		rosParam.setContactAddress("todo@example.com");
		rosParam.setLicense("LGPL-3.0");
		
		TopicParam topic1 = new TopicParam();
		topic1.setName("cmd_vel");
		topic1.setMessageType("geometry_msgs/msg/Twist");
		topic1.setReliabilityType("Reliable");
		topic1.setHistoryType("Keep Last");
		topic1.setDepth(10);
		topic1.setVarCallbackName("cmd_vel_publisher");
		topic1.setDocDescription("Publish velocity command.");
		rosParam.getTopicPublishes().add(topic1);

		TopicParam topic2 = new TopicParam();
		topic2.setName("robot_status");
		topic2.setMessageType("eval5_robot_cpp_pkg_interfaces/msg/CustomStatus");
		topic2.setVarCallbackName("robot_status_callback");
		topic2.setReliabilityType("Reliable");
		topic2.setHistoryType("Keep Last");
		topic2.setDepth(10);
		topic2.setDocDescription("Receive custom robot status.");
		rosParam.getTopicSubscribes().add(topic2);
		
		ServiceParam service = new ServiceParam();
		service.setName("set_mode");
		service.setType("eval5_robot_cpp_pkg_interfaces/srv/SetMode");
		service.setVarCallbackName("set_mode_client");
		service.setDocDescription("Request robot operation mode.");
		rosParam.getServiceClients().add(service);
		
		ActionParam action = new ActionParam();
		action.setName("move_to_target");
		action.setType("eval5_robot_cpp_pkg_interfaces/action/MoveToTarget");
		action.setCallbackName("move_to_target_action");
		action.setDocDescription("Send move target goal.");
		rosParam.getActionClients().add(action);
		
		TimerParam timer = new TimerParam();
		timer.setName("command_timer");
		timer.setRate(1.0);
		timer.setCallBack("command_timer_callback");
		timer.setDescription("Publish commands periodically.");
		rosParam.getTimers().add(timer);
		
		ParameterParam param = new ParameterParam();
		param.setName("operator_name");
		param.setType("string");
		param.setDefaultValue("operator");
		param.setReadOnly(true);
		param.setDocDescription("Operator name.");
		param.setDocDataname("Doc Data name.");
		param.setDocDefault("Doc Default value.");
		param.setDocUnit("Doc Unit.");
		param.setDocRange("Doc Range.");
		param.setDocConstraint("Doc Constraint.");
		rosParam.getParameters().add(param);
		
		Generator generator = new Generator();
		generator.clearGenerateManager();
		generator.addGenerateManager(new CommonGenerateManager());
		generator.addGenerateManager(new CXXGenerateManager());

		List<GeneratedResult> result = generator.generateTemplateCodeROS(genParam);

		String resourceDir = rootPath + "/resource/ROS/Document/eval5_operator_cpp_pkg_project/";

		//eval5_operator_cpp_pkg
		checkCode(result, resourceDir, "eval5_operator_cpp_pkg/README.md");
		checkCode(result, resourceDir, "eval5_operator_cpp_pkg/package.xml");
		checkCode(result, resourceDir, "eval5_operator_cpp_pkg/CMakeLists.txt");
		checkCode(result, resourceDir, "eval5_operator_cpp_pkg/LICENSE");
		checkCode(result, resourceDir, "eval5_operator_cpp_pkg/config/params.yaml");
		checkCode(result, resourceDir, "eval5_operator_cpp_pkg/launch/eval5_operator_node.launch.py");
		checkCode(result, resourceDir, "eval5_operator_cpp_pkg/include/eval5_operator_cpp_pkg/eval5_operator_node.hpp");
		checkCode(result, resourceDir, "eval5_operator_cpp_pkg/src/main.cpp");
		checkCode(result, resourceDir, "eval5_operator_cpp_pkg/src/eval5_operator_node.cpp");
		//eval5_operator_cpp_pkg_interfaces
		checkCode(result, resourceDir, "eval5_operator_cpp_pkg_interfaces/README.md");
		checkCode(result, resourceDir, "eval5_operator_cpp_pkg_interfaces/package.xml");
		checkCode(result, resourceDir, "eval5_operator_cpp_pkg_interfaces/CMakeLists.txt");
		checkCode(result, resourceDir, "eval5_operator_cpp_pkg_interfaces/LICENSE");
	}
	
}
