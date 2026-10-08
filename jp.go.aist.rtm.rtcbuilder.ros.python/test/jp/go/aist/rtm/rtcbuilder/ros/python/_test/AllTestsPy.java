package jp.go.aist.rtm.rtcbuilder.ros.python._test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTestsPy {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for jp.go.aist.rtm.rtcbuilder.python._test._100");
		//$JUnit-BEGIN$
		suite.addTestSuite(BasicTest.class);
		suite.addTestSuite(DocumentTest.class);
		suite.addTestSuite(MeijoTest.class);
		//$JUnit-END$
		return suite;
	}

}
