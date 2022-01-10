package model.world;

public class Seed {

	private Seed() {
	}

	public static long moisture(long worldSeed) {
		return worldSeed;
	}

	public static long elevation(long worldSeed) {
		return worldSeed + 1;
	}

	public static long node(long worldSeed) {
		return worldSeed + 2;
	}

}
