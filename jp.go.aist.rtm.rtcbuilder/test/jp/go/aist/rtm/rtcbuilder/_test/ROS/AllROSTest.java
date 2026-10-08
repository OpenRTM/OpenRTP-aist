package jp.go.aist.rtm.rtcbuilder._test.ROS;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllROSTest {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for ROS");
		//$JUnit-BEGIN$
		suite.addTestSuite(BasicTest.class);
		suite.addTestSuite(DocumentTest.class);
		suite.addTestSuite(MeijoTest.class);

		return suite;
	}

}
