package jp.go.aist.rtm.rtcbuilder._test;

import jp.go.aist.rtm.rtcbuilder._test.ROS.DocumentTest;
import jp.go.aist.rtm.rtcbuilder._test.container.ContainerROS2Test;
import jp.go.aist.rtm.rtcbuilder._test.container.ContainerRTCTest;
import jp.go.aist.rtm.rtcbuilder._test.etc.MergeBlockParserTest;
import jp.go.aist.rtm.rtcbuilder._test.etc.PreProcessorTest;
import jp.go.aist.rtm.rtcbuilder._test.etc.StringUtilTest;
import jp.go.aist.rtm.rtcbuilder._test.etc.TemplateHelperTest;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.BasicTest;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.BuildTest;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.CheckIDLTest;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.ConfigSetTest;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.Constraint;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.ContentTest;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.DataPortIDLTest;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.DocLongTest;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.ExCxtTest;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.FSMTest;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.IDLInheritTest;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.IDLModuleTest;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.IDLPathTest;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.IDLStructTest;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.IDLType;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.ImplTest;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.LibraryTest;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.LuckTest;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.ManipTest;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.MultiTest;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.PrefixSuffixTest;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.ServicePortTest;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.TemplateTestAIST2;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.TestComp;
import jp.go.aist.rtm.rtcbuilder._test.generateCode.VariableTest;
import jp.go.aist.rtm.rtcbuilder._test.param.ConfigSetParamTest;
import jp.go.aist.rtm.rtcbuilder._test.param.DataPortParamTest;
import jp.go.aist.rtm.rtcbuilder._test.param.ParamActionsTest;
import jp.go.aist.rtm.rtcbuilder._test.param.RecordedListTest;
import jp.go.aist.rtm.rtcbuilder._test.param.RtcParamTest;
import jp.go.aist.rtm.rtcbuilder._test.param.ServicePortInterfaceParamTest;
import jp.go.aist.rtm.rtcbuilder._test.param.ServicePortParamTest;
import jp.go.aist.rtm.rtcbuilder._test.param.TargetEnvParamTest;
import jp.go.aist.rtm.rtcbuilder._test.parse.CORBAParseCommentTest;
import jp.go.aist.rtm.rtcbuilder._test.parse.CORBAParseInheritTest;
import jp.go.aist.rtm.rtcbuilder._test.parse.CORBAParseMethodTypeTest;
import jp.go.aist.rtm.rtcbuilder._test.parse.CORBAParseServiceTest;
import jp.go.aist.rtm.rtcbuilder._test.parse.CORBAParseTest;
import jp.go.aist.rtm.rtcbuilder._test.parse.CORBAParseTypeDefTest;
import jp.go.aist.rtm.rtcbuilder._test.parse.CORBAParseTypeTest;
import jp.go.aist.rtm.rtcbuilder._test.parse.IDLParamConverterTest;
import jp.go.aist.rtm.rtcbuilder._test.parse.IDLParserTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for jp.go.aist.rtm.rtcbuilder._test");
		//$JUnit-BEGIN$
		//Common
		suite.addTestSuite(CORBAParseTypeTest.class);
		suite.addTestSuite(CORBAParseTest.class);
		suite.addTestSuite(CORBAParseServiceTest.class);
		suite.addTestSuite(CORBAParseMethodTypeTest.class);
		suite.addTestSuite(CORBAParseCommentTest.class);
		suite.addTestSuite(CORBAParseTypeDefTest.class);
		suite.addTestSuite(CORBAParseInheritTest.class);
		suite.addTestSuite(StringUtilTest.class);
		//cxx
		suite.addTestSuite(IDLParamConverterTest.class);
		suite.addTestSuite(MergeBlockParserTest.class);
		suite.addTestSuite(IDLParserTest.class);
		suite.addTestSuite(PreProcessorTest.class);
		//
		suite.addTestSuite(CheckIDLTest.class);
		//
		suite.addTestSuite(IDLType.class);
		suite.addTestSuite(DocLongTest.class);
		suite.addTestSuite(DataPortIDLTest.class);
		suite.addTestSuite(ExCxtTest.class);
		suite.addTestSuite(BasicTest.class);
		suite.addTestSuite(BuildTest.class);
		suite.addTestSuite(LuckTest.class);
		suite.addTestSuite(TemplateTestAIST2.class);
		suite.addTestSuite(PrefixSuffixTest.class);
		suite.addTestSuite(IDLModuleTest.class);
		suite.addTestSuite(Constraint.class);
		suite.addTestSuite(IDLInheritTest.class);
		suite.addTestSuite(IDLPathTest.class);
		suite.addTestSuite(ImplTest.class);
		suite.addTestSuite(MultiTest.class);
		suite.addTestSuite(VariableTest.class);
		suite.addTestSuite(LibraryTest.class);
		suite.addTestSuite(ConfigSetTest.class);
		suite.addTestSuite(IDLStructTest.class);
		suite.addTestSuite(ManipTest.class);
		suite.addTestSuite(ContentTest.class);
		//
		suite.addTestSuite(FSMTest.class);
		//
		suite.addTestSuite(TemplateHelperTest.class);
		//
		suite.addTestSuite(TargetEnvParamTest.class);
		suite.addTestSuite(DataPortParamTest.class);
		suite.addTestSuite(ConfigSetParamTest.class);
		suite.addTestSuite(RtcParamTest.class);
		suite.addTestSuite(ServicePortParamTest.class);
		suite.addTestSuite(ServicePortInterfaceParamTest.class);
		suite.addTestSuite(RecordedListTest.class);
		suite.addTestSuite(ParamActionsTest.class);
		//
		suite.addTestSuite(ServicePortTest.class);
		//
		suite.addTestSuite(TestComp.class);
		/////
		suite.addTestSuite(jp.go.aist.rtm.rtcbuilder._test.ROS.BasicTest.class);
		suite.addTestSuite(DocumentTest.class);
		
		suite.addTestSuite(ContainerROS2Test.class);
		suite.addTestSuite(ContainerRTCTest.class);


		//$JUnit-END$
		return suite;
	}

}
