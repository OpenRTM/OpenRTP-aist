package jp.go.aist.rtm.rtcbuilder.processing._test;

import java.util.List;

import jp.go.aist.rtm.rtcbuilder.Generator;
import jp.go.aist.rtm.rtcbuilder.generator.GeneratedResult;
import jp.go.aist.rtm.rtcbuilder.generator.ProfileHandler;
import jp.go.aist.rtm.rtcbuilder.generator.param.GeneratorParam;
import jp.go.aist.rtm.rtcbuilder.processing.IRtcBuilderConstantsProcessing;
import jp.go.aist.rtm.rtcbuilder.processing.manager.ProcessingCMakeGenerateManager;
import jp.go.aist.rtm.rtcbuilder.processing.manager.ProcessingGenerateManager;

public class ProcessingExampleTest2 extends TestBase {
	String resourceDir = rootPath + "/resource/ProcessingExample2/";

	public void testProcessingExample02() throws Exception {
		ProfileHandler handler = new ProfileHandler(true);
		handler.addManager(new ProcessingGenerateManager());
		handler.addManager(new ProcessingCMakeGenerateManager());
		GeneratorParam genParam = handler.restorefromXMLFile(rootPath + "/resource/ProcessingExample2.xml", true);
		genParam.getRtcParam().setLanguage(IRtcBuilderConstantsProcessing.LANG_PROCESSING);
		genParam.getRtcParam().setLanguageArg(IRtcBuilderConstantsProcessing.LANG_PROCESSING_ARG);
		genParam.getRtcParam().setIsTest(true);
		genParam.getRtcParam().getServicePorts().get(0).getServicePortInterfaces().get(0).setIdlFile(rootPath + "/resource/ProcessingExample2/idl/MyService.idl");
		
		Generator generator = new Generator();
		generator.addGenerateManager(new ProcessingGenerateManager());
		generator.addGenerateManager(new ProcessingCMakeGenerateManager());
		List<GeneratedResult> result = generator.generateTemplateCode(genParam);

		checkCode(result, resourceDir, "COPYING");
		checkCode(result, resourceDir, "COPYING.LESSER");
		checkCode(result, resourceDir, "README.md");
		checkCode(result, resourceDir, "rtc.conf");
		checkCode(result, resourceDir, "ProcessingExample.conf");
		checkCode(result, resourceDir, "ProcessingExample.bat");
//		checkCode(result, resourceDir, "ProcessingExample.sh");
//		checkCode(result, resourceDir, "build_ProcessingExample.xml");
		checkCode(result, resourceDir, "CMakeLists.txt");
		checkCode(result, resourceDir, "run_ProcessingExample.xml");
		
		checkCode(result, resourceDir, "cmake/CMakeLists.txt");
		checkCode(result, resourceDir, "cmake/cpack_options.cmake.in");
		checkCode(result, resourceDir, "cmake/License.rtf");
		checkCode(result, resourceDir, "cmake/uninstall_target.cmake.in");
		checkCode(result, resourceDir, "cmake/utils.cmake");
		checkCode(result, resourceDir, "cmake/processingexample-config-version.cmake.in");
		checkCode(result, resourceDir, "cmake/processingexample-config.cmake.in");
		checkCode(result, resourceDir, "cmake/processingexample.pc.in");
		
		checkCode(result, resourceDir, "cmake_modules/cmake_javacompile.cmake.in");

		checkCode(result, resourceDir, "idl/CMakeLists.txt");
		
		checkCode(result, resourceDir, "ProcessingExampleMain/ProcessingExampleMain.pde");
		checkCode(result, resourceDir, "ProcessingExampleMain/ProcessingExampleComp.pde");
		checkCode(result, resourceDir, "ProcessingExampleMain/ProcessingExample.pde");
		checkCode(result, resourceDir, "ProcessingExampleMain/ProcessingExampleImpl.pde");
		
		checkCode(result, resourceDir, "test/CMakeLists.txt");
		
		checkCode(result, resourceDir, "test/src/CMakeLists.txt");
		checkCode(result, resourceDir, "test/src/ProcessingExampleTestMain/ProcessingExampleTestMain.pde");
		checkCode(result, resourceDir, "test/src/ProcessingExampleTestMain/ProcessingExampleTestComp.pde");
		checkCode(result, resourceDir, "test/src/ProcessingExampleTestMain/ProcessingExampleTest.pde");
		checkCode(result, resourceDir, "test/src/ProcessingExampleTestMain/ProcessingExampleTestImpl.pde");
		checkCode(result, resourceDir, "test/src/ProcessingExampleTestMain/MyServiceSVC_impl.pde");
	}
}
