package jp.go.aist.rtm.rtcbuilder.processing._test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTestsProcessing {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for jp.go.aist.rtm.rtcbuilder._test");
		//$JUnit-BEGIN$
		suite.addTestSuite(ProcessingExampleTest.class);
		suite.addTestSuite(ProcessingExampleTest2.class);
		suite.addTestSuite(ProcessingExampleTest3.class);
		//$JUnit-END$
		return suite;
	}

}
