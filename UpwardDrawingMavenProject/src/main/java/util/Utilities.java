package util;

public class Utilities {
	public static long timer;
	
	public static void startTimer() {
		timer = System.currentTimeMillis();
	}
	
	// timeout set to 1 minute
	public static boolean isTimeOver () {
		long currentTime = System.currentTimeMillis();
		double diffSec = ((double) currentTime - timer)/1000;
		return diffSec > 60;
	}
	
	public static double elapsedTime() {
		long currentTime = System.currentTimeMillis();
		return ((double) currentTime - timer)/1000;
	}
}
