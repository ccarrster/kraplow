package com.chriscarr.bang;

public class AlivePlayers {

	public static int getDistance(int indexA, int indexB, int size){	
		int highIndex;
		int lowIndex;
		if(indexA > indexB){
			highIndex = indexA;
			lowIndex = indexB;
		} else {
			highIndex = indexB;
			lowIndex = indexA;			
		}	
		int distanceGreater = highIndex - lowIndex;
		int distanceLesser = size - highIndex + lowIndex;
		return Math.min(distanceGreater, distanceLesser);
	}
}
