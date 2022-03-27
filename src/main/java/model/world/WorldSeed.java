package model.world;

public class WorldSeed {

	private WorldSeed() {
	}

	public static long moisture(long worldSeed) {
		return worldSeed;
	}

	public static long elevation(long worldSeed) {
		return worldSeed + 10;
	}

}
