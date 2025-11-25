package jp.go.aist.rtm.rtcbuilder._test.generateCode;

import java.util.ArrayList;
import java.util.List;

import jp.go.aist.rtm.rtcbuilder.Generator;
import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder._test.TestBase;
import jp.go.aist.rtm.rtcbuilder.generator.GeneratedResult;
import jp.go.aist.rtm.rtcbuilder.generator.param.DataTypeParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.GeneratorParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.RtcParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.ServicePortInterfaceParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.ServicePortParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.IdlPathParam;

public class ServicePortTest extends TestBase {
	RtcParam rtcParam;
	GeneratorParam genParam;

	protected void setUp() throws Exception {
		genParam = new GeneratorParam();
		rtcParam = new RtcParam(genParam, true);
		rtcParam.setOutputProject(rootPath + "/resource/work");
		rtcParam.setLanguage(IRtcBuilderConstants.LANG_CPP);
		rtcParam.setLanguageArg(IRtcBuilderConstants.LANG_CPP_ARG);
		rtcParam.setRtmVersion("1.0.0");
		rtcParam.setIsTest(true);
		rtcParam.setName("ModuleName");
		rtcParam.setDescription("ModuleDescription");
		rtcParam.setVersion("1.0.0");
		rtcParam.setVender("VenderName");
		rtcParam.setCategory("Category");
		rtcParam.setComponentType("DataFlowComponent");
		rtcParam.setActivityType("PERIODIC");
		rtcParam.setComponentKind("DataFlowComponent");
		rtcParam.setMaxInstance(1);

		DataTypeParam param1 = new DataTypeParam();
		param1.setFullPath("C:\\Program Files\\OpenRTM-aist\\1.2.0\\rtm\\idl\\CameraCommonInterface.idl");
		param1.getDefinedTypes().add("RTC::CameraImage");
		param1.getDefinedTypes().add("RTC::CameraInfo");
		param1.setDefault(true);
		genParam.getDataTypeParams().add(param1);

		DataTypeParam param2 = new DataTypeParam();
		param2.setFullPath("C:\\Program Files\\OpenRTM-aist\\1.2.0\\rtm\\idl\\InterfaceDataTypes.idl");
		param2.setDefault(true);
		genParam.getDataTypeParams().add(param2);

		DataTypeParam param3 = new DataTypeParam();
		param3.setFullPath("C:\\Program Files\\OpenRTM-aist\\1.2.0\\rtm\\idl\\BasicDataType.idl");
		param3.setDefault(true);
		genParam.getDataTypeParams().add(param3);

		DataTypeParam param4 = new DataTypeParam();
		param4.setFullPath("C:\\Program Files\\OpenRTM-aist\\1.2.0\\rtm\\idl\\ExtendedDataTypes.idl");
		param4.setDefault(true);
		genParam.getDataTypeParams().add(param4);

		DataTypeParam param5 = new DataTypeParam();
		param5.setFullPath("C:\\Program Files\\OpenRTM-aist\\1.2.0\\rtm\\idl\\ManipulatorCommonInterface_Common.idl");
		param5.setDefault(true);
		genParam.getDataTypeParams().add(param5);
		
		genParam.setRtcParam(rtcParam);
	}

	public void testServicePort() throws Exception {
		rtcParam.getIdlSearchPathList().add(new IdlPathParam("C:\\Program Files\\OpenRTM-aist\\1.2.0\\rtm\\idl", false));
		
		ServicePortParam service1 = new ServicePortParam("sv_name",0);
		List<ServicePortInterfaceParam> srvinterts = new ArrayList<ServicePortInterfaceParam>();
		ServicePortInterfaceParam int1 = new ServicePortInterfaceParam(service1, "if_name", "", "",
				rootPath + "resource/CalibrationService.idl",
				"ImageCalibService::CalibrationService",
				0);
		srvinterts.add(int1);
		service1.getServicePortInterfaces().addAll(srvinterts);
		List<ServicePortParam> srvports = new ArrayList<ServicePortParam>();
		srvports.add(service1);
		rtcParam.getServicePorts().addAll(srvports);

		Generator generator = new Generator();
		List<GeneratedResult> result = generator.generateTemplateCode(genParam);

		String targetDir = rootPath + "/resource/100/ServicePort/";
		checkCode(result, targetDir, "ModuleNameComp.cpp");
		checkCode(result, targetDir, "ModuleName.h");
		checkCode(result, targetDir, "ModuleName.cpp");
		checkCode(result, targetDir, "CalibrationServiceSVC_impl.h");
		checkCode(result, targetDir, "CalibrationServiceSVC_impl.cpp");
		checkCode(result, targetDir, "idl/CMakeLists.txt");
	}

	public void testServicePortJARA() throws Exception {
		rtcParam.getIdlSearchPathList().add(new IdlPathParam("C:\\Program Files\\OpenRTM-aist\\1.2.0\\rtm\\idl", false));
		
		ServicePortParam service1 = new ServicePortParam("sv_name",0);
		List<ServicePortInterfaceParam> srvinterts = new ArrayList<ServicePortInterfaceParam>();
		ServicePortInterfaceParam int1 = new ServicePortInterfaceParam(service1, "if_name", "", "",
				"C:\\Program Files\\OpenRTM-aist\\2.1.0\\rtm\\idl\\ManipulatorCommonInterface_Common.idl",
				"JARA_ARM::ManipulatorCommonInterface_Common",
				0);
		srvinterts.add(int1);
		service1.getServicePortInterfaces().addAll(srvinterts);
		List<ServicePortParam> srvports = new ArrayList<ServicePortParam>();
		srvports.add(service1);
		rtcParam.getServicePorts().addAll(srvports);

		Generator generator = new Generator();
		List<GeneratedResult> result = generator.generateTemplateCode(genParam);

		String targetDir = rootPath + "/resource/100/ServicePortJARA/";
		checkCode(result, targetDir, "ModuleNameComp.cpp");
		checkCode(result, targetDir, "ModuleName.h");
		checkCode(result, targetDir, "ModuleName.cpp");
		checkCode(result, targetDir, "ManipulatorCommonInterface_CommonSVC_impl.h");
		checkCode(result, targetDir, "ManipulatorCommonInterface_CommonSVC_impl.cpp");
	}
}
