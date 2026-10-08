package jp.go.aist.rtm.rtcbuilder.ros.python._test;

import java.util.List;

import jp.go.aist.rtm.rtcbuilder.Generator;
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
import jp.go.aist.rtm.rtcbuilder.ros.python.IRtcBuilderConstantsPython;
import jp.go.aist.rtm.rtcbuilder.ros.python.manager.PythonCommonGenerateManager;
import jp.go.aist.rtm.rtcbuilder.ros.python.manager.PythonGenerateManager;

public class MeijoTest extends TestBase {

	Generator generator;
	GeneratorParam genParam;
	ROSParam rosParam;

	protected void setUp() throws Exception {
		genParam = new GeneratorParam();
		rosParam = new ROSParam(genParam, true);
		rosParam.setOutputProject(rootPath + "/resource/work");
		rosParam.setLanguage(IRtcBuilderConstantsPython.LANG_PYTHON);
		rosParam.setLanguageArg(IRtcBuilderConstantsPython.LANG_PYTHON_ARG);
		genParam.setROSParam(rosParam);

		generator = new Generator();
		generator.clearGenerateManager();
		generator.addGenerateManager(new PythonGenerateManager());
		generator.addGenerateManager(new PythonCommonGenerateManager());
	}
	
	public void testCountServer() throws Exception {
		rosParam.setPackageName("custom_service_server_py_pkg");
		rosParam.setNodeName("custom_service_server_node");
		rosParam.setClassName("CustomServiceServerNode");
		rosParam.setDescription("Python custom Count service server node.");
		rosParam.setVersion("0.0.1");
		rosParam.setMaintainer("rsdlab");
		rosParam.setCategory("Controller");
		rosParam.setContactAddress("todo@example.com");
		rosParam.setLicense("Apache-2.0");
		
		rosParam.getExtSrvFiles().add("Count.srv");

		ServiceParam service01 = new ServiceParam();
		service01.setName("count");
		service01.setType("custom_service_server_py_pkg_interfaces/srv/Count");
		service01.setVarCallbackName("count");
		service01.setDocDescription("Add amount to the running total.");
		service01.setDocArgument("amount: value to add");
		service01.setDocReturn("total and success");
		rosParam.getServiceServers().add(service01);
		
		List<GeneratedResult> result = generator.generateTemplateCodeROS(genParam);

		String resourceDir = rootPath + "/resource/custom_service_server_py_pkg/";

		//custom_service_server_py_pkg
		checkCode(result, resourceDir, "custom_service_server_py_pkg/README.md");
		checkCode(result, resourceDir, "custom_service_server_py_pkg/package.xml");
		checkCode(result, resourceDir, "custom_service_server_py_pkg/LICENSE");
		checkCode(result, resourceDir, "custom_service_server_py_pkg/setup.cfg");
		checkCode(result, resourceDir, "custom_service_server_py_pkg/setup.py");
		checkCode(result, resourceDir, "custom_service_server_py_pkg/config/params.yaml");
		checkCode(result, resourceDir, "custom_service_server_py_pkg/launch/custom_service_server_node.launch.py");
		checkCode(result, resourceDir, "custom_service_server_py_pkg/custom_service_server_py_pkg/__init__.py");
		checkCode(result, resourceDir, "custom_service_server_py_pkg/custom_service_server_py_pkg/custom_service_server_node.py");
		checkCode(result, resourceDir, "custom_service_server_py_pkg/resource/custom_service_server_py_pkg");
		//custom_service_server_py_pkg_interfaces
		checkCode(result, resourceDir, "custom_service_server_py_pkg_interfaces/README.md");
		checkCode(result, resourceDir, "custom_service_server_py_pkg_interfaces/package.xml");
		checkCode(result, resourceDir, "custom_service_server_py_pkg_interfaces/CMakeLists.txt");
		checkCode(result, resourceDir, "custom_service_server_py_pkg_interfaces/LICENSE");
	}

	public void testCountClient() throws Exception {
		rosParam.setPackageName("custom_service_client_py_pkg");
		rosParam.setNodeName("custom_service_client_node");
		rosParam.setClassName("CustomServiceClientNode");
		rosParam.setDescription("Python custom Count service client node.");
		rosParam.setVersion("0.0.1");
		rosParam.setMaintainer("rsdlab");
		rosParam.setCategory("Controller");
		rosParam.setContactAddress("todo@example.com");
		rosParam.setLicense("Apache-2.0");
		
		rosParam.getExtSrvFiles().add("Count.srv");

		ServiceParam service01 = new ServiceParam();
		service01.setName("count");
		service01.setType("custom_service_server_py_pkg_interfaces/srv/Count");
		service01.setVarCallbackName("client_variable");
		service01.setDocDescription("Send an amount to the Count service.");
		service01.setDocArgument("amount: value to add");
		service01.setDocReturn("total and success");
		rosParam.getServiceClients().add(service01);
		
		TimerParam timer = new TimerParam();
		timer.setName("service_call_timer");
		timer.setRate(2.0);
		timer.setCallBack("service_call_timer_callback");
		timer.setDescription("Call the Count service periodically.");
		rosParam.getTimers().add(timer);

		List<GeneratedResult> result = generator.generateTemplateCodeROS(genParam);

		String resourceDir = rootPath + "/resource/custom_service_client_py_pkg/";

		//custom_service_client_py_pkg
		checkCode(result, resourceDir, "custom_service_client_py_pkg/README.md");
		checkCode(result, resourceDir, "custom_service_client_py_pkg/package.xml");
		checkCode(result, resourceDir, "custom_service_client_py_pkg/LICENSE");
		checkCode(result, resourceDir, "custom_service_client_py_pkg/setup.cfg");
		checkCode(result, resourceDir, "custom_service_client_py_pkg/setup.py");
		checkCode(result, resourceDir, "custom_service_client_py_pkg/config/params.yaml");
		checkCode(result, resourceDir, "custom_service_client_py_pkg/launch/custom_service_client_node.launch.py");
		checkCode(result, resourceDir, "custom_service_client_py_pkg/custom_service_client_py_pkg/__init__.py");
		checkCode(result, resourceDir, "custom_service_client_py_pkg/custom_service_client_py_pkg/custom_service_client_node.py");
		checkCode(result, resourceDir, "custom_service_client_py_pkg/resource/custom_service_client_py_pkg");
		//custom_service_client_py_pkg_interfaces
		checkCode(result, resourceDir, "custom_service_client_py_pkg_interfaces/README.md");
		checkCode(result, resourceDir, "custom_service_client_py_pkg_interfaces/package.xml");
		checkCode(result, resourceDir, "custom_service_client_py_pkg_interfaces/CMakeLists.txt");
		checkCode(result, resourceDir, "custom_service_client_py_pkg_interfaces/LICENSE");
	}

}
