package math;

import java.util.Random;

public class IntegerRandom {

	private static Random random = new Random();

	public static int randomInt(int max, long seed, int sample) {
		Random seedRandom = new Random(seed);
		for (int i = 0; i < sample - 1; i++) {
			seedRandom.nextFloat();
		}
		return (int) seedRandom.nextFloat() * max;
	}

	public static int randomInt(int max) {
		return (int) random.nextFloat() * max;
	}

}
