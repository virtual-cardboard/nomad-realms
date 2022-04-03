package math;

public class IDGenerator {

	private static volatile long counter = 0;
	private static volatile long collectionCardCounter = 0;

	public static synchronized long genID() {
		return counter++;
	}

	public static synchronized long genCollectionCardID() {
		return collectionCardCounter++;
	}

}
