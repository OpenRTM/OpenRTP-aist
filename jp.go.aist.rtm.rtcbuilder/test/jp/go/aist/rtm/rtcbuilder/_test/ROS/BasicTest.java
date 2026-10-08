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

public class BasicTest extends TestBase {

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
		
		Generator generator = new Generator();
		generator.clearGenerateManager();
		generator.addGenerateManager(new CommonGenerateManager());
		generator.addGenerateManager(new CXXGenerateManager());

		List<GeneratedResult> result = generator.generateTemplateCodeROS(genParam);

		String resourceDir = rootPath + "/resource/ROS/eval1_minimal_cpp_pkg_project/";

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
		rosParam.setDescription("C++ command source node with cmd_vel publisher and robot_status subscriber.");
		rosParam.setVersion("0.0.1");
		rosParam.setMaintainer("rsdlab");
		rosParam.setContactAddress("todo@example.com");
		rosParam.setLicense("Apache-2.0");
		
		TopicParam topic1 = new TopicParam();
		topic1.setName("cmd_vel");
		topic1.setMessageType("geometry_msgs/msg/Twist");
		topic1.setVarCallbackName("cmd_vel_publisher");
		topic1.setReliabilityType("Reliable");
		topic1.setHistoryType("Keep Last");
		topic1.setDepth(10);
		topic1.setDocDescription("Publish velocity command.");
		rosParam.getTopicPublishes().add(topic1);
		
		TopicParam topic2 = new TopicParam();
		topic2.setName("robot_status");
		topic2.setMessageType("std_msgs/msg/String");
		topic2.setVarCallbackName("robot_status_callback");
		topic2.setReliabilityType("Reliable");
		topic2.setHistoryType("Keep Last");
		topic2.setDepth(10);
		topic2.setDocDescription("Receive robot status.");
		rosParam.getTopicSubscribes().add(topic2);
		
		ParameterParam param1 = new ParameterParam();
		param1.setName("command_speed");
		param1.setType("double");
		param1.setDefaultValue("0.5");
		param1.setMin(0.0);
		param1.setMax(2.0);
		param1.setStep(0.1);
		param1.setReadOnly(false);
		param1.setDocDescription("Command speed.");
		rosParam.getParameters().add(param1);
		
		TimerParam timer = new TimerParam();
		timer.setName("cmd_timer");
		timer.setRate(1.0);
		timer.setCallBack("cmd_timer_callback");
		timer.setDescription("Publish command periodically.");
		rosParam.getTimers().add(timer);

		Generator generator = new Generator();
		generator.clearGenerateManager();
		generator.addGenerateManager(new CommonGenerateManager());
		generator.addGenerateManager(new CXXGenerateManager());

		List<GeneratedResult> result = generator.generateTemplateCodeROS(genParam);

		String resourceDir = rootPath + "/resource/ROS/eval2_cmd_source_cpp_pkg_project/";

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
	
	public void test02Conrtoller() throws Exception {
		rosParam.setPackageName("eval2_controller_cpp_pkg");
		rosParam.setNodeName("eval2_controller_node");
		rosParam.setClassName("Eval2ControllerNode");
		rosParam.setDescription("C++ controller node with cmd_vel subscription, robot_status publisher, timer, and multiple parameters.");
		rosParam.setVersion("0.0.1");
		rosParam.setMaintainer("rsdlab");
		rosParam.setContactAddress("todo@example.com");
		rosParam.setLicense("Apache-2.0");
		
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

		String resourceDir = rootPath + "/resource/ROS/eval2_controller_cpp_pkg_project/";

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
	
	public void test02LifeCycleCallback() throws Exception {
		rosParam.setPackageName("eval2_controller_cpp_pkg");
		rosParam.setNodeName("eval2_controller_node");
		rosParam.setClassName("Eval2ControllerNode");
		rosParam.setDescription("C++ controller node with cmd_vel subscription, robot_status publisher, timer, and multiple parameters.");
		rosParam.setVersion("0.0.1");
		rosParam.setMaintainer("maintainer");
		rosParam.setContactAddress("todo@example.com");
		rosParam.setLicense("Apache-2.0");
		
		rosParam.setActionImplemented(IRtcBuilderConstantsROS.ACTIVITY_CLEANUP, true);
		rosParam.setActionImplemented(IRtcBuilderConstantsROS.ACTIVITY_SHUTDOWN, true);
		rosParam.setActionImplemented(IRtcBuilderConstantsROS.ACTIVITY_ERROR, true);
		
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

		String resourceDir = rootPath + "/resource/ROS/eval2_controller_cpp_pkg_project_cb/";

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
		rosParam.getServiceClients().add(service01);
		
		ServiceParam service02 = new ServiceParam();
		service02.setName("set_bool");
		service02.setType("std_srvs/srv/SetBool");
		service02.setVarCallbackName("set_bool_client");
		service02.setDocDescription("Call SetBool service.");
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

		String resourceDir = rootPath + "/resource/ROS/eval3_service_client_cpp_pkg_project/";

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
		rosParam.getServiceServers().add(service01);
		
		ServiceParam service02 = new ServiceParam();
		service02.setName("set_bool");
		service02.setType("std_srvs/srv/SetBool");
		service02.setVarCallbackName("handle_set_bool");
		service02.setDocDescription("Set boolean state.");
		rosParam.getServiceServers().add(service02);

		Generator generator = new Generator();
		generator.clearGenerateManager();
		generator.addGenerateManager(new CommonGenerateManager());
		generator.addGenerateManager(new CXXGenerateManager());

		List<GeneratedResult> result = generator.generateTemplateCodeROS(genParam);

		String resourceDir = rootPath + "/resource/ROS/eval3_service_server_cpp_pkg_project/";

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

		String resourceDir = rootPath + "/resource/ROS/eval4_action_client_cpp_pkg_project/";

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
		rosParam.getActionServers().add(action);

		Generator generator = new Generator();
		generator.clearGenerateManager();
		generator.addGenerateManager(new CommonGenerateManager());
		generator.addGenerateManager(new CXXGenerateManager());

		List<GeneratedResult> result = generator.generateTemplateCodeROS(genParam);

		String resourceDir = rootPath + "/resource/ROS/eval4_action_server_cpp_pkg_project/";

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
		rosParam.getParameters().add(param);
		
		Generator generator = new Generator();
		generator.clearGenerateManager();
		generator.addGenerateManager(new CommonGenerateManager());
		generator.addGenerateManager(new CXXGenerateManager());

		List<GeneratedResult> result = generator.generateTemplateCodeROS(genParam);

		String resourceDir = rootPath + "/resource/ROS/eval5_operator_cpp_pkg_project/";

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
	
	public void test05Robot() throws Exception {
		rosParam.setPackageName("eval5_robot_cpp_pkg");
		rosParam.setNodeName("eval5_robot_node");
		rosParam.setClassName("Eval5RobotNode");
		rosParam.setDescription("C++ robot-side all-in-one sample node with topics, services, actions, timers, parameters, and custom interfaces.");
		rosParam.setVersion("0.0.1");
		rosParam.setMaintainer("rsdlab");
		rosParam.setContactAddress("todo@example.com");
		rosParam.setLicense("LGPL-3.0");
		
		rosParam.getExtMsgFiles().add("CustomStatus.msg");
		rosParam.getExtSrvFiles().add("SetMode.srv");
		rosParam.getExtActionFiles().add("MoveToTarget.action");
		
		TopicParam topic1 = new TopicParam();
		topic1.setName("cmd_vel");
		topic1.setMessageType("geometry_msgs/msg/Twist");
		topic1.setReliabilityType("Reliable");
		topic1.setHistoryType("Keep Last");
		topic1.setDepth(10);
		topic1.setVarCallbackName("cmd_vel_callback");
		topic1.setDocDescription("Receive velocity command.");
		rosParam.getTopicSubscribes().add(topic1);

		TopicParam topic2 = new TopicParam();
		topic2.setName("robot_status");
		topic2.setMessageType("CustomStatus");
		topic2.setVarCallbackName("robot_status_publisher");
		topic2.setReliabilityType("Reliable");
		topic2.setHistoryType("Keep Last");
		topic2.setDepth(10);
		topic2.setDocDescription("Publish custom robot status.");
		rosParam.getTopicPublishes().add(topic2);
		
		ServiceParam service = new ServiceParam();
		service.setName("set_mode");
		service.setType("SetMode");
		service.setVarCallbackName("handle_set_mode");
		service.setDocDescription("Set robot operation mode.");
		rosParam.getServiceServers().add(service);
		
		ActionParam action = new ActionParam();
		action.setName("move_to_target");
		action.setType("MoveToTarget");
		action.setCallbackName("execute_move_to_target");
		action.setDocDescription("Move robot to target pose.");
		rosParam.getActionServers().add(action);
		
		TimerParam timer = new TimerParam();
		timer.setName("status_timer");
		timer.setRate(1.0);
		timer.setCallBack("status_timer_callback");
		timer.setDescription("Publish robot status periodically.");
		rosParam.getTimers().add(timer);
		
		ParameterParam param1 = new ParameterParam();
		param1.setName("max_speed");
		param1.setType("double");
		param1.setDefaultValue("1.0");
		param1.setMin(0.0);
		param1.setMax(5.0);
		param1.setStep(0.1);
		param1.setDocDescription("Maximum linear speed.");
		rosParam.getParameters().add(param1);
		
		ParameterParam param2 = new ParameterParam();
		param2.setName("robot_id");
		param2.setType("string");
		param2.setDefaultValue("robot_01");
		param2.setReadOnly(true);
		param2.setDocDescription("Robot identifier.");
		rosParam.getParameters().add(param2);

		Generator generator = new Generator();
		generator.clearGenerateManager();
		generator.addGenerateManager(new CommonGenerateManager());
		generator.addGenerateManager(new CXXGenerateManager());

		List<GeneratedResult> result = generator.generateTemplateCodeROS(genParam);

		String resourceDir = rootPath + "/resource/ROS/eval5_robot_cpp_pkg_project/";

		//eval5_robot_cpp_pkg
		checkCode(result, resourceDir, "eval5_robot_cpp_pkg/README.md");
		checkCode(result, resourceDir, "eval5_robot_cpp_pkg/package.xml");
		checkCode(result, resourceDir, "eval5_robot_cpp_pkg/CMakeLists.txt");
		checkCode(result, resourceDir, "eval5_robot_cpp_pkg/LICENSE");
		checkCode(result, resourceDir, "eval5_robot_cpp_pkg/config/params.yaml");
		checkCode(result, resourceDir, "eval5_robot_cpp_pkg/launch/eval5_robot_node.launch.py");
		checkCode(result, resourceDir, "eval5_robot_cpp_pkg/include/eval5_robot_cpp_pkg/eval5_robot_node.hpp");
		checkCode(result, resourceDir, "eval5_robot_cpp_pkg/src/main.cpp");
		checkCode(result, resourceDir, "eval5_robot_cpp_pkg/src/eval5_robot_node.cpp");
		//eval5_robot_cpp_pkg_interfaces
		checkCode(result, resourceDir, "eval5_robot_cpp_pkg_interfaces/README.md");
		checkCode(result, resourceDir, "eval5_robot_cpp_pkg_interfaces/package.xml");
		checkCode(result, resourceDir, "eval5_robot_cpp_pkg_interfaces/CMakeLists.txt");
		checkCode(result, resourceDir, "eval5_robot_cpp_pkg_interfaces/LICENSE");
	}
	
	public void test06Image() throws Exception {
		rosParam.setPackageName("eval6_image_topic_cpp_pkg");
		rosParam.setNodeName("eval6_image_topic_node");
		rosParam.setClassName("Eval6ImageTopicNode");
		rosParam.setDescription("C++ image topic sample node that demonstrates sensor_msgs Image type dependency inference.");
		rosParam.setVersion("0.0.1");
		rosParam.setMaintainer("rsdlab");
		rosParam.setContactAddress("todo@example.com");
		rosParam.setLicense("Apache-2.0");
		
		TopicParam topic1 = new TopicParam("Publish");
		topic1.setName("image_debug");
		topic1.setMessageType("sensor_msgs/msg/Image");
		topic1.setVarCallbackName("image_debug_publisher");
		topic1.setDocDescription("Publish debug image output.");
		rosParam.getTopicPublishes().add(topic1);

		TopicParam topic2 = new TopicParam("Subscribe");
		topic2.setName("image_raw");
		topic2.setMessageType("sensor_msgs/msg/Image");
		topic2.setVarCallbackName("image_raw_callback");
		topic2.setDocDescription("Receive raw image input.");
		rosParam.getTopicSubscribes().add(topic2);
		
		rosParam.convertInfo();

		Generator generator = new Generator();
		generator.clearGenerateManager();
		generator.addGenerateManager(new CommonGenerateManager());
		generator.addGenerateManager(new CXXGenerateManager());

		List<GeneratedResult> result = generator.generateTemplateCodeROS(genParam);

		String resourceDir = rootPath + "/resource/ROS/eval6_image_topic_cpp_pkg_project/";

		//eval6_image_topic_cpp_pkg
		checkCode(result, resourceDir, "eval6_image_topic_cpp_pkg/README.md");
		checkCode(result, resourceDir, "eval6_image_topic_cpp_pkg/package.xml");
		checkCode(result, resourceDir, "eval6_image_topic_cpp_pkg/CMakeLists.txt");
		checkCode(result, resourceDir, "eval6_image_topic_cpp_pkg/LICENSE");
		checkCode(result, resourceDir, "eval6_image_topic_cpp_pkg/config/params.yaml");
		checkCode(result, resourceDir, "eval6_image_topic_cpp_pkg/launch/eval6_image_topic_node.launch.py");
		checkCode(result, resourceDir, "eval6_image_topic_cpp_pkg/include/eval6_image_topic_cpp_pkg/eval6_image_topic_node.hpp");
		checkCode(result, resourceDir, "eval6_image_topic_cpp_pkg/src/main.cpp");
		checkCode(result, resourceDir, "eval6_image_topic_cpp_pkg/src/eval6_image_topic_node.cpp");
		//eval6_image_topic_cpp_pkg_interfaces
		checkCode(result, resourceDir, "eval6_image_topic_cpp_pkg_interfaces/README.md");
		checkCode(result, resourceDir, "eval6_image_topic_cpp_pkg_interfaces/package.xml");
		checkCode(result, resourceDir, "eval6_image_topic_cpp_pkg_interfaces/CMakeLists.txt");
		checkCode(result, resourceDir, "eval6_image_topic_cpp_pkg_interfaces/LICENSE");
	}
	
	public void test07Dependency() throws Exception {
		rosParam.setPackageName("eval7_extra_dependency_cpp_pkg");
		rosParam.setNodeName("eval7_extra_dependency_node");
		rosParam.setClassName("Eval7ExtraDependencyNode");
		rosParam.setDescription("C++ sample node that demonstrates explicit cv_bridge extra dependency without generated cv_bridge API code.");
		rosParam.setVersion("0.0.1");
		rosParam.setCategory("Controller");
		rosParam.setMaintainer("rsdlab");
		rosParam.setContactAddress("todo@example.com");
		rosParam.setLicense("Apache-2.0");
		
		PackageParam pack = new PackageParam();
		pack.setName("cv_bridge");
		rosParam.getTargetEnv().getLibraries().add(pack);
		
		
		TopicParam topic1 = new TopicParam("Publish");
		topic1.setName("output_text");
		topic1.setMessageType("std_msgs/msg/String");
		topic1.setVarCallbackName("output_text_publisher");
		topic1.setDocDescription("Publish text output.");
		rosParam.getTopicPublishes().add(topic1);

		TopicParam topic2 = new TopicParam("Subscribe");
		topic2.setName("input_text");
		topic2.setMessageType("std_msgs/msg/String");
		topic2.setVarCallbackName("input_text_callback");
		topic2.setDocDescription("Receive text input.");
		rosParam.getTopicSubscribes().add(topic2);
		
		TimerParam timer = new TimerParam();
		timer.setName("text_timer");
		timer.setRate(1.0);
		timer.setCallBack("text_timer_callback");
		timer.setDescription("Periodic placeholder for user logic.");
		rosParam.getTimers().add(timer);

		rosParam.convertInfo();

		Generator generator = new Generator();
		generator.clearGenerateManager();
		generator.addGenerateManager(new CommonGenerateManager());
		generator.addGenerateManager(new CXXGenerateManager());

		List<GeneratedResult> result = generator.generateTemplateCodeROS(genParam);

		String resourceDir = rootPath + "/resource/ROS/eval7_extra_dependency_cpp_pkg_project/";

		//eval7_extra_dependency_cpp_pkg
		checkCode(result, resourceDir, "eval7_extra_dependency_cpp_pkg/README.md");
		checkCode(result, resourceDir, "eval7_extra_dependency_cpp_pkg/package.xml");
		checkCode(result, resourceDir, "eval7_extra_dependency_cpp_pkg/CMakeLists.txt");
		checkCode(result, resourceDir, "eval7_extra_dependency_cpp_pkg/LICENSE");
		checkCode(result, resourceDir, "eval7_extra_dependency_cpp_pkg/config/params.yaml");
		checkCode(result, resourceDir, "eval7_extra_dependency_cpp_pkg/launch/eval7_extra_dependency_node.launch.py");
		checkCode(result, resourceDir, "eval7_extra_dependency_cpp_pkg/include/eval7_extra_dependency_cpp_pkg/eval7_extra_dependency_node.hpp");
		checkCode(result, resourceDir, "eval7_extra_dependency_cpp_pkg/src/main.cpp");
		checkCode(result, resourceDir, "eval7_extra_dependency_cpp_pkg/src/eval7_extra_dependency_node.cpp");
		//eval7_extra_dependency_cpp_pkg_interfaces
		checkCode(result, resourceDir, "eval7_extra_dependency_cpp_pkg_interfaces/README.md");
		checkCode(result, resourceDir, "eval7_extra_dependency_cpp_pkg_interfaces/package.xml");
		checkCode(result, resourceDir, "eval7_extra_dependency_cpp_pkg_interfaces/CMakeLists.txt");
		checkCode(result, resourceDir, "eval7_extra_dependency_cpp_pkg_interfaces/LICENSE");
	}
	
	public void test08PoseController() throws Exception {
		rosParam.setPackageName("eval8_pose_controller_cpp_pkg");
		rosParam.setNodeName("pose_controller_node");
		rosParam.setClassName("PoseControllerNode");
		rosParam.setDescription("Lifecycle pose controller that drives a simulated mobile robot to a target pose using odometry feedback.");
		rosParam.setVersion("0.0.1");
		rosParam.setCategory("Controller");
		rosParam.setMaintainer("rsdlab");
		rosParam.setContactAddress("todo@example.com");
		rosParam.setLicense("Apache-2.0");
		
		rosParam.setActionImplemented(IRtcBuilderConstantsROS.ACTIVITY_CLEANUP, true);
		rosParam.setActionImplemented(IRtcBuilderConstantsROS.ACTIVITY_SHUTDOWN, true);
		rosParam.setActionImplemented(IRtcBuilderConstantsROS.ACTIVITY_ERROR, true);
		
		rosParam.getExtActionFiles().add("MoveToPose.action");
		
		TopicParam topic1 = new TopicParam("Subscribe");
		topic1.setName("odom");
		topic1.setMessageType("nav_msgs/msg/Odometry");
		topic1.setVarCallbackName("odom_callback");
		topic1.setReliabilityType("BestEffort");
		topic1.setHistoryType("Keep Last");
		topic1.setDepth(10);
		topic1.setDocDescription("Receive the current simulated robot pose and velocity.");
		rosParam.getTopicSubscribes().add(topic1);

		TopicParam topic2 = new TopicParam("Publish");
		topic2.setName("cmd_vel");
		topic2.setMessageType("geometry_msgs/msg/Twist");
		topic2.setVarCallbackName("cmd_vel_publisher");
		topic2.setReliabilityType("Reliable");
		topic2.setHistoryType("Keep Last");
		topic2.setDepth(10);
		topic2.setDocDescription("Publish linear and angular velocity commands for the simulated mobile base.");
		rosParam.getTopicPublishes().add(topic2);
		
		ActionParam action = new ActionParam();
		action.setName("move_to_pose");
		action.setType("MoveToPose");
		action.setCallbackName("execute_move_to_pose");
		action.setDocDescription("Drive the simulated mobile robot to a requested target pose.");
		rosParam.getActionServers().add(action);
		
		ParameterParam param1 = new ParameterParam();
		param1.setName("linear_gain");
		param1.setType("double");
		param1.setDefaultValue("1.0");
		param1.setMin(0.0);
		param1.setMax(10.0);
		param1.setStep(0.1);
		param1.setReadOnly(false);
		param1.setDocDescription("Proportional gain for linear velocity control.");
		rosParam.getParameters().add(param1);
		
		ParameterParam param2 = new ParameterParam();
		param2.setName("angular_gain");
		param2.setType("double");
		param2.setDefaultValue("2.0");
		param2.setMin(0.0);
		param2.setMax(10.0);
		param2.setStep(0.1);
		param2.setReadOnly(false);
		param2.setDocDescription("Proportional gain for angular velocity control.");
		rosParam.getParameters().add(param2);

		ParameterParam param3 = new ParameterParam();
		param3.setName("max_linear_speed");
		param3.setType("double");
		param3.setDefaultValue("0.5");
		param3.setMin(0.0);
		param3.setMax(2.0);
		param3.setStep(0.1);
		param3.setReadOnly(false);
		param3.setDocDescription("Maximum commanded linear velocity.");
		rosParam.getParameters().add(param3);

		ParameterParam param4 = new ParameterParam();
		param4.setName("max_angular_speed");
		param4.setType("double");
		param4.setDefaultValue("1.0");
		param4.setMin(0.0);
		param4.setMax(5.0);
		param4.setStep(0.1);
		param4.setReadOnly(false);
		param4.setDocDescription("Maximum commanded angular velocity.");
		rosParam.getParameters().add(param4);
		
		ParameterParam param5 = new ParameterParam();
		param5.setName("goal_tolerance");
		param5.setType("double");
		param5.setDefaultValue("0.05");
		param5.setMin(0.001);
		param5.setMax(1.0);
		param5.setStep(0.01);
		param5.setReadOnly(false);
		param5.setDocDescription("Position tolerance used to determine goal completion.");
		rosParam.getParameters().add(param5);

		ParameterParam param6 = new ParameterParam();
		param6.setName("yaw_tolerance");
		param6.setType("double");
		param6.setDefaultValue("0.05");
		param6.setMin(0.001);
		param6.setMax(3.14);
		param6.setStep(0.01);
		param6.setReadOnly(false);
		param6.setDocDescription("Yaw tolerance used to determine final orientation completion.");
		rosParam.getParameters().add(param6);
		
		ParameterParam param7 = new ParameterParam();
		param7.setName("odom_timeout_sec");
		param7.setType("double");
		param7.setDefaultValue("0.5");
		param7.setMin(0.05);
		param7.setMax(10.0);
		param7.setStep(0.05);
		param7.setReadOnly(false);
		param7.setDocDescription("Maximum allowed age of odometry data before stopping the robot.");
		rosParam.getParameters().add(param7);

		ParameterParam param8 = new ParameterParam();
		param8.setName("control_enabled");
		param8.setType("bool");
		param8.setDefaultValue("true");
		param8.setReadOnly(false);
		param8.setDocDescription("Enable or disable velocity control.");
		rosParam.getParameters().add(param8);
		
		TimerParam timer = new TimerParam();
		timer.setName("control_timer");
		timer.setRate(0.05);
		timer.setCallBack("control_timer_callback");
		timer.setDescription("Calculate and publish velocity commands while a move-to-pose goal is active.");
		rosParam.getTimers().add(timer);

		rosParam.convertInfo();

		Generator generator = new Generator();
		generator.clearGenerateManager();
		generator.addGenerateManager(new CommonGenerateManager());
		generator.addGenerateManager(new CXXGenerateManager());

		List<GeneratedResult> result = generator.generateTemplateCodeROS(genParam);

		String resourceDir = rootPath + "/resource/ROS/eval8_pose_controller_cpp_pkg_project/";

		//eval8_pose_controller_cpp_pkg
		checkCode(result, resourceDir, "eval8_pose_controller_cpp_pkg/README.md");
		checkCode(result, resourceDir, "eval8_pose_controller_cpp_pkg/package.xml");
		checkCode(result, resourceDir, "eval8_pose_controller_cpp_pkg/CMakeLists.txt");
		checkCode(result, resourceDir, "eval8_pose_controller_cpp_pkg/LICENSE");
		checkCode(result, resourceDir, "eval8_pose_controller_cpp_pkg/config/params.yaml");
		checkCode(result, resourceDir, "eval8_pose_controller_cpp_pkg/launch/pose_controller_node.launch.py");
		checkCode(result, resourceDir, "eval8_pose_controller_cpp_pkg/include/eval8_pose_controller_cpp_pkg/pose_controller_node.hpp");
		checkCode(result, resourceDir, "eval8_pose_controller_cpp_pkg/src/main.cpp");
		checkCode(result, resourceDir, "eval8_pose_controller_cpp_pkg/src/pose_controller_node.cpp");
		//eval8_pose_controller_cpp_pkg_interfaces
		checkCode(result, resourceDir, "eval8_pose_controller_cpp_pkg_interfaces/README.md");
		checkCode(result, resourceDir, "eval8_pose_controller_cpp_pkg_interfaces/package.xml");
		checkCode(result, resourceDir, "eval8_pose_controller_cpp_pkg_interfaces/CMakeLists.txt");
		checkCode(result, resourceDir, "eval8_pose_controller_cpp_pkg_interfaces/LICENSE");
	}
}
