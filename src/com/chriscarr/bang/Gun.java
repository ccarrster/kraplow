package com.chriscarr.bang;

public class Gun {

	int range;
	boolean limitlessBangs = false;
	
	public void setRange(int i) {
		range = i;
	}

	public int getRange() {		
		return range;
	}

	public void setLimitlessBangs(boolean b) {
		limitlessBangs = b;
	}

	public boolean getLimitlessBangs() {
		return limitlessBangs;
	}

}
