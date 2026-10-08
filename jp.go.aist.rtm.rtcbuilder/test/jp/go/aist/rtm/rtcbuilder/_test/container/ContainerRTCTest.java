package jp.go.aist.rtm.rtcbuilder._test.container;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.go.aist.rtm.rtcbuilder.Generator;
import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder._test.TestBase;
import jp.go.aist.rtm.rtcbuilder.container.param.ContainerParam;
import jp.go.aist.rtm.rtcbuilder.container.param.LibraryParam;
import jp.go.aist.rtm.rtcbuilder.container.param.RepositoryParam;
import jp.go.aist.rtm.rtcbuilder.container.param.setting.ContainerConfig;
import jp.go.aist.rtm.rtcbuilder.generator.GeneratedResult;
import jp.go.aist.rtm.rtcbuilder.generator.param.GeneratorParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.RtcParam;

public class ContainerRTCTest extends TestBase {

	RtcParam rtcParam;
	GeneratorParam genParam;
	ContainerConfig containerConfig;

	@Before
	protected void setUp() throws Exception {
		genParam = new GeneratorParam();
		rtcParam = new RtcParam(genParam, true);
		rtcParam.setOutputProject(rootPath + "/resource/work");
		rtcParam.setLanguage(IRtcBuilderConstants.LANG_CPP);
		rtcParam.setLanguageArg(IRtcBuilderConstants.LANG_CPP_ARG);
		rtcParam.setRtmVersion("1.0.0");
		rtcParam.setIsTest(true);
		genParam.setRtcParam(rtcParam);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			byte[] bytes = Files.readAllBytes(Paths.get(rootPath + "/resource/rtcbuilder_config.json"));
			String content = new String(bytes, StandardCharsets.UTF_8);
			
			containerConfig = mapper.readValue(content, ContainerConfig.class);
			rtcParam.setContainerConfig(containerConfig);
		} catch (Exception ex) {
			int a = 0;
		}
	}
	
	private void createLibraries(ContainerParam param, List<String> libraries, String installer) {
		for(String each : libraries) {
			LibraryParam lib01 = new LibraryParam();
			lib01.setName(each);
			lib01.setInstaller(installer);
			param.getLibraries().add(lib01);
		}
	}

	public void testManualCpp() throws Exception {
		rtcParam.setName("openrtm_manual_apt_pip");
		rtcParam.setDescription("OpenRTM C++ sample that installs JSON-unknown jq and packaging libraries through manually selected apt and pip Installers.");
		rtcParam.setVersion("1.0.1");
		rtcParam.setVender("rsdlab");
		rtcParam.setCategory("Sample");
		
		ContainerParam param = new ContainerParam();
		param.setMiddleware("OpenRTM");
		param.setMdlVersion("latest");
		param.setOsVersion("Ubuntu 24.04");
		param.setWorkspace("workspace");
		param.setLanguage("C++");
		param.setConfiguration("Std");
		param.updateDefaultLibs(containerConfig);
		rtcParam.getContainerSettings().add(param);
		/////
		List<String> libraries = Arrays.asList(
				"OpenRTM-aist", 
				"omniORB", 
				"jq"
				);
		createLibraries(param, libraries, "apt");
		
		LibraryParam lib02 = new LibraryParam();
		lib02.setName("packaging");
		lib02.setInstaller("pip");
		param.getLibraries().add(lib02);
		
		Generator generator = new Generator();
		List<GeneratedResult> result = generator.generateTemplateCode(genParam);

		String resourceDir = rootPath + "/resource/scripts/RTC/";
		checkCode(result, resourceDir, "openrtm_manual_apt_pip__Ubuntu-24.04__OpenRTM-latest__cpp_Std.Dockerfile");
	}
	
	public void testRTCActivationTestCpp() throws Exception {
		rtcParam.setName("RTCActivationTest");
		rtcParam.setDescription("OpenRTM 2.x C++ RTC sample for testing component activation. The repository root is directly buildable with CMake.");
		rtcParam.setVersion("1.0.1");
		rtcParam.setVender("Nobu19800");
		rtcParam.setCategory("Sample");
		
		ContainerParam param = new ContainerParam();
		param.setMiddleware("OpenRTM");
		param.setMdlVersion("latest");
		param.setOsVersion("Ubuntu 24.04");
		param.setWorkspace("workspace");
		param.setLanguage("C++");
		param.setConfiguration("Std");
		param.updateDefaultLibs(containerConfig);
		rtcParam.getContainerSettings().add(param);

		List<String> libraries = Arrays.asList(
				"OpenRTM-aist", 
				"omniORB" 
				);
		createLibraries(param, libraries, "apt");
		/////
		RepositoryParam rep1 = new RepositoryParam();
		rep1.setURL("https://github.com/Nobu19800/RTCActivationTest.git");
		rep1.setBranch("main");
		param.getRepositories().add(rep1);

		Generator generator = new Generator();
		List<GeneratedResult> result = generator.generateTemplateCode(genParam);

		String resourceDir = rootPath + "/resource/scripts/RTC/";
		checkCode(result, resourceDir, "RTCActivationTest__Ubuntu-24.04__OpenRTM-latest__cpp_Std.Dockerfile");
	}
	
	public void testRTCLoadTestCpp() throws Exception {
		rtcParam.setName("RTCLoadTest");
		rtcParam.setDescription("OpenRTM 2.x C++ RTC sample that measures RTC startup time. The repository root is directly buildable with CMake.");
		rtcParam.setVersion("1.0.1");
		rtcParam.setVender("Nobu19800");
		rtcParam.setCategory("Sample");
		
		ContainerParam param = new ContainerParam();
		param.setMiddleware("OpenRTM");
		param.setMdlVersion("latest");
		param.setOsVersion("Ubuntu 24.04");
		param.setWorkspace("workspace");
		param.setLanguage("C++");
		param.setConfiguration("Std");
		param.updateDefaultLibs(containerConfig);
		rtcParam.getContainerSettings().add(param);
		/////
		List<String> libraries = Arrays.asList(
				"OpenRTM-aist", 
				"omniORB" 
				);
		createLibraries(param, libraries, "apt");

		RepositoryParam rep1 = new RepositoryParam();
		rep1.setURL("https://github.com/Nobu19800/RTCLoadTest.git");
		rep1.setBranch("main");
		param.getRepositories().add(rep1);

		Generator generator = new Generator();
		List<GeneratedResult> result = generator.generateTemplateCode(genParam);

		String resourceDir = rootPath + "/resource/scripts/RTC/";
		checkCode(result, resourceDir, "RTCLoadTest__Ubuntu-24.04__OpenRTM-latest__cpp_Std.Dockerfile");
	}
	
	public void testSimplePythonRTCStdPython() throws Exception {
		rtcParam.setName("SimplePythonRTC");
		rtcParam.setDescription("Simple OpenRTM Python environment sample for RTCBuilder Dockerfile generation. This profile does not specify GitURL, so the generated Dockerfile only prepares the OpenRTM Python container environment.");
		rtcParam.setVersion("1.0.1");
		rtcParam.setVender("rsdlab");
		rtcParam.setCategory("Sample");
		
		ContainerParam param = new ContainerParam();
		param.setMiddleware("OpenRTM");
		param.setMdlVersion("latest");
		param.setOsVersion("Ubuntu 24.04");
		param.setWorkspace("workspace");
		param.setLanguage("Python");
		param.setConfiguration("Std");
		param.updateDefaultLibs(containerConfig);
		rtcParam.getContainerSettings().add(param);
		/////
		List<String> libraries = Arrays.asList(
				"openrtm-aist-python"
				);
		createLibraries(param, libraries, "apt");
		/////
		Generator generator = new Generator();
		List<GeneratedResult> result = generator.generateTemplateCode(genParam);

		String resourceDir = rootPath + "/resource/scripts/RTC/";
		checkCode(result, resourceDir, "SimplePythonRTC__Ubuntu-24.04__OpenRTM-latest__python_Std.Dockerfile");
	}
	
	public void testSimpleCppRTCStdCpp() throws Exception {
		rtcParam.setName("SimpleCppRTC");
		rtcParam.setDescription("Simple OpenRTM C++ environment sample for RTCBuilder Dockerfile generation. This profile does not specify GitURL, so the generated Dockerfile only prepares the OpenRTM C++ container environment.");
		rtcParam.setVersion("1.0.1");
		rtcParam.setVender("rsdlab");
		rtcParam.setCategory("Sample");
		
		ContainerParam param = new ContainerParam();
		param.setMiddleware("OpenRTM");
		param.setMdlVersion("latest");
		param.setOsVersion("Ubuntu 24.04");
		param.setWorkspace("workspace");
		param.setLanguage("C++");
		param.setConfiguration("Std");
		param.updateDefaultLibs(containerConfig);
		rtcParam.getContainerSettings().add(param);
		/////
		List<String> libraries = Arrays.asList(
				"OpenRTM-aist", 
				"omniORB"
				);
		createLibraries(param, libraries, "apt");
		/////
		Generator generator = new Generator();
		List<GeneratedResult> result = generator.generateTemplateCode(genParam);

		String resourceDir = rootPath + "/resource/scripts/RTC/";
		checkCode(result, resourceDir, "SimpleCppRTC__Ubuntu-24.04__OpenRTM-latest__cpp_Std.Dockerfile");
	}
}
