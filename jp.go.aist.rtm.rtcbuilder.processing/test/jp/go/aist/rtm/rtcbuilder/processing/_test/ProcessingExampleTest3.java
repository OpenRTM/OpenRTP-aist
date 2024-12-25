package jp.go.aist.rtm.rtcbuilder.processing._test;

import java.util.ArrayList;
import java.util.List;

import jp.go.aist.rtm.rtcbuilder.Generator;
import jp.go.aist.rtm.rtcbuilder.generator.GeneratedResult;
import jp.go.aist.rtm.rtcbuilder.generator.ProfileHandler;
import jp.go.aist.rtm.rtcbuilder.generator.param.DataTypeParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.GeneratorParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.IdlFileParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.IdlPathParam;
import jp.go.aist.rtm.rtcbuilder.processing.IRtcBuilderConstantsProcessing;
import jp.go.aist.rtm.rtcbuilder.processing.manager.ProcessingCMakeGenerateManager;
import jp.go.aist.rtm.rtcbuilder.processing.manager.ProcessingGenerateManager;

public class ProcessingExampleTest3 extends TestBase {
	String resourceDir = rootPath + "/resource/ProcessingExample3/";

	public void testProcessingExample03() throws Exception {
		ProfileHandler handler = new ProfileHandler(true);
		handler.addManager(new ProcessingGenerateManager());
		handler.addManager(new ProcessingCMakeGenerateManager());
		GeneratorParam genParam = handler.restorefromXMLFile(rootPath + "/resource/ProcessingExample3.xml", true);
		genParam.getRtcParam().setLanguage(IRtcBuilderConstantsProcessing.LANG_PROCESSING);
		genParam.getRtcParam().setLanguageArg(IRtcBuilderConstantsProcessing.LANG_PROCESSING_ARG);
		genParam.getRtcParam().setIsTest(true);
		genParam.getRtcParam().getOutports().get(0).setIdlFile(rootPath + "/resource/ProcessingExample3/idl/test.idl");
		IdlPathParam test = new IdlPathParam(rootPath + "/resource/ProcessingExample3/idl", "idl", false);
		genParam.getRtcParam().getIdlPathes().add(test);
		IdlFileParam testFile = new IdlFileParam();
		testFile.setIdlPath(rootPath + "/resource/ProcessingExample3/idl/test.idl");
		testFile.setDataPort(true);
		testFile.setParent(genParam.getRtcParam());
		genParam.getRtcParam().getConsumerIdlPathes().add(testFile);
		
		DataTypeParam dp = new DataTypeParam();
		dp.setDefault(false);
		dp.getDefinedTypes().add("Sample::SampleDataType");
		dp.setDispPath("idl\\test.idl");
		dp.setContent("aaa");
		dp.setFullPath(rootPath + "/resource/ProcessingExample3/idl/test.idl");
		genParam.getDataTypeParams().add(dp);
		
		List<IdlPathParam> idlDir = new ArrayList<IdlPathParam>();
		IdlPathParam idlParam01 = new IdlPathParam("<RTM_ROOT>\\rtm\\idl", "<RTM_ROOT>\\rtm\\idl", true);
		idlDir.add(idlParam01);
		IdlPathParam idlParam02 = new IdlPathParam(rootPath + "/resource/ProcessingExample3/idl", "idf", false);
		idlDir.add(idlParam02);
		
		Generator generator = new Generator();
		generator.addGenerateManager(new ProcessingGenerateManager());
		generator.addGenerateManager(new ProcessingCMakeGenerateManager());
		List<GeneratedResult> result = generator.generateTemplateCode(genParam, idlDir);

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
	}
}
