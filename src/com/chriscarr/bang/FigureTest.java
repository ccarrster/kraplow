package com.chriscarr.bang;

import junit.framework.TestCase;

public class FigureTest extends TestCase {
	public void testName(){
		Figure figure = new Figure();
		String setName = "Big Hank";
		figure.setName(setName);
		String gotName = figure.getName();
		assertEquals(setName, gotName);
	}
	
	public void testDescription(){
		Figure figure = new Figure();
		String setDescription = "Can play 2 bangs";
		figure.setDescription(setDescription);
		String gotDescription = figure.getDescription();
		assertEquals(setDescription, gotDescription);
	}
}
