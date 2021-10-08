package math;

public class IDGenerator {

	private static volatile long counter = 0;

	public static synchronized long genID() {
		return counter++;
	}

}
