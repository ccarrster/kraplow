package com.chriscarr.bang.test;

import java.util.ArrayList;

import com.chriscarr.bang.userinterface.WebGameUserInterface;

import junit.framework.TestCase;

public class WebGameuserInterfaceTest extends TestCase {
	public void testAI(){
		WebGameUserInterface wgui = new WebGameUserInterface(new ArrayList<String>());
		assertEquals("1", wgui.somethingAI("respondBeer Bang!@true, Beer@false, Missed!@false, Missed!@false, "));
	}
}
