package com.chriscarr.bang;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for com.chriscarr.bang");
		//$JUnit-BEGIN$
		suite.addTestSuite(DistanceTest.class);
		suite.addTestSuite(HandTest.class);
		suite.addTestSuite(DeckTest.class);
		suite.addTestSuite(DiscardTest.class);
		suite.addTestSuite(InPlayTest.class);
		suite.addTestSuite(FigureTest.class);
		suite.addTestSuite(GunTest.class);
		suite.addTestSuite(CardTest.class);
		suite.addTestSuite(SetupTest.class);
		suite.addTestSuite(PlayerTest.class);
		suite.addTestSuite(TurnTest.class);
		//$JUnit-END$
		return suite;
	}

}
