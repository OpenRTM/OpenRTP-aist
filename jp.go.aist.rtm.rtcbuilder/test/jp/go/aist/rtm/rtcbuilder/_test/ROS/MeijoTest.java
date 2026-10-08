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

public class MeijoTest extends TestBase {

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

	public void testCountServer() throws Exception {
		rosParam.setPackageName("count_server_pkg");
		rosParam.setNodeName("count_server_node");
		rosParam.setClassName("CountServerNode");
		rosParam.setDescription("Count service server node.");
		rosParam.setVersion("0.0.1");
		rosParam.setMaintainer("rsdlab");
		rosParam.setCategory("Controller");
		rosParam.setContactAddress("todo@example.com");
		rosParam.setLicense("Apache-2.0");
		
		rosParam.getExtSrvFiles().add("Count.srv");

		ServiceParam service01 = new ServiceParam();
		service01.setName("count");
		service01.setType("count_server_pkg_interfaces/srv/Count");
		service01.setVarCallbackName("count");
		service01.setDocDescription("Add amount to the running total.");
		service01.setDocArgument("amount: value to add");
		service01.setDocReturn("total and success");
		rosParam.getServiceServers().add(service01);

		Generator generator = new Generator();
		generator.clearGenerateManager();
		generator.addGenerateManager(new CommonGenerateManager());
		generator.addGenerateManager(new CXXGenerateManager());

		List<GeneratedResult> result = generator.generateTemplateCodeROS(genParam);

		String resourceDir = rootPath + "/resource/ROS/count_server_pkg/";

		//count_server_pkg
		checkCode(result, resourceDir, "count_server_pkg/README.md");
		checkCode(result, resourceDir, "count_server_pkg/package.xml");
		checkCode(result, resourceDir, "count_server_pkg/CMakeLists.txt");
		checkCode(result, resourceDir, "count_server_pkg/LICENSE");
		checkCode(result, resourceDir, "count_server_pkg/config/params.yaml");
		checkCode(result, resourceDir, "count_server_pkg/launch/count_server_node.launch.py");
		checkCode(result, resourceDir, "count_server_pkg/include/count_server_pkg/count_server_node.hpp");
		checkCode(result, resourceDir, "count_server_pkg/src/main.cpp");
		checkCode(result, resourceDir, "count_server_pkg/src/count_server_node.cpp");
		//count_server_pkg_interfaces
		checkCode(result, resourceDir, "count_server_pkg_interfaces/README.md");
		checkCode(result, resourceDir, "count_server_pkg_interfaces/package.xml");
		checkCode(result, resourceDir, "count_server_pkg_interfaces/CMakeLists.txt");
		checkCode(result, resourceDir, "count_server_pkg_interfaces/LICENSE");
	}
	
	public void testCountClient() throws Exception {
		rosParam.setPackageName("count_client_pkg");
		rosParam.setNodeName("count_client_node");
		rosParam.setClassName("CountClientNode");
		rosParam.setDescription("Count service client node.");
		rosParam.setVersion("0.0.1");
		rosParam.setMaintainer("rsdlab");
		rosParam.setContactAddress("todo@example.com");
		rosParam.setLicense("Apache-2.0");
		
		rosParam.getExtSrvFiles().add("Count.srv");

		ServiceParam service01 = new ServiceParam();
		service01.setName("count");
		service01.setType("count_server_pkg_interfaces/srv/Count");
		service01.setVarCallbackName("count_client");
		service01.setDocDescription("Send an amount to the count service.");
		service01.setDocArgument("amount: value to add");
		service01.setDocReturn("total and success");
		rosParam.getServiceClients().add(service01);

		TimerParam timer = new TimerParam();
		timer.setName("count_call_timer");
		timer.setRate(2.0);
		timer.setCallBack("count_call_timer_callback");
		timer.setDescription("Call the count service periodically.");
		rosParam.getTimers().add(timer);

		Generator generator = new Generator();
		generator.clearGenerateManager();
		generator.addGenerateManager(new CommonGenerateManager());
		generator.addGenerateManager(new CXXGenerateManager());

		List<GeneratedResult> result = generator.generateTemplateCodeROS(genParam);

		String resourceDir = rootPath + "/resource/ROS/count_client_pkg/";

		//count_client_pkg
		checkCode(result, resourceDir, "count_client_pkg/README.md");
		checkCode(result, resourceDir, "count_client_pkg/package.xml");
		checkCode(result, resourceDir, "count_client_pkg/CMakeLists.txt");
		checkCode(result, resourceDir, "count_client_pkg/LICENSE");
		checkCode(result, resourceDir, "count_client_pkg/config/params.yaml");
		checkCode(result, resourceDir, "count_client_pkg/launch/count_client_node.launch.py");
		checkCode(result, resourceDir, "count_client_pkg/include/count_client_pkg/count_client_node.hpp");
		checkCode(result, resourceDir, "count_client_pkg/src/main.cpp");
		checkCode(result, resourceDir, "count_client_pkg/src/count_client_node.cpp");
		//count_client_pkg_interfaces
		checkCode(result, resourceDir, "count_client_pkg_interfaces/README.md");
		checkCode(result, resourceDir, "count_client_pkg_interfaces/package.xml");
		checkCode(result, resourceDir, "count_client_pkg_interfaces/CMakeLists.txt");
		checkCode(result, resourceDir, "count_client_pkg_interfaces/LICENSE");
	}
}
