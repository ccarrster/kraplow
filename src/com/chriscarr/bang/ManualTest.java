package com.chriscarr.bang;

public class ManualTest {

	public ManualTest(){
		ManualUserInterface x = new ManualUserInterface();
		new Setup(7, x, x);
	}
	
	public static void main(String[] args) {
		new ManualTest();
	}

}
