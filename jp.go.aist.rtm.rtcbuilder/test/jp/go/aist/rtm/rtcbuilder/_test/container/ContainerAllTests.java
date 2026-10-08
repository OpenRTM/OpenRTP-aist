package jp.go.aist.rtm.rtcbuilder._test.container;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ContainerAllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for jp.go.aist.rtm.rtcbuilder._test");
		//$JUnit-BEGIN$
		//Common
		suite.addTestSuite(ContainerROS2Test.class);
		
		suite.addTestSuite(ContainerRTCTest.class);
		//$JUnit-END$
		return suite;
	}

}
