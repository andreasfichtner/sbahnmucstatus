package sbahnmucstatustracker;

public class SBahnMucStatusTracker {

	static boolean stopped = false;
	
	public static void main(String[] args) {
		while(!stopped) {
			System.out.println("Test");
		}
	}
}
