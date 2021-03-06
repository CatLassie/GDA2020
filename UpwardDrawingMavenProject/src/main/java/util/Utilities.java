package util;

public class Utilities {
	public static long timer;
	
	public static void startTimer() {
		timer = System.currentTimeMillis();
	}
	
	// timeout set to timeLimit
	public static boolean isTimeOver (int timeLimit) {
		long currentTime = System.currentTimeMillis();
		double diffSec = ((double) currentTime - timer)/1000;
		return diffSec > timeLimit;
	}
	
	public static double elapsedTime() {
		long currentTime = System.currentTimeMillis();
		return ((double) currentTime - timer)/1000;
	}
}
