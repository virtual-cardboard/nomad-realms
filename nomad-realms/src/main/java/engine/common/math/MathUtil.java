package engine.common.math;

public class MathUtil {

	public static int hash(int... values) {
		int h = 7;
		for (int v : values) {
			h = 31 * h + v;
			h ^= (h >>> 16);
			h *= 0x85ebca6b;
			h ^= (h >>> 13);
			h *= 0xc2b2ae35;
			h ^= (h >>> 16);
		}
		return h;
	}

	public static int posMod(int val, int mod) {
		return (val % mod + mod) % mod;
	}

	public static float posMod(float val, float mod) {
		return (val % mod + mod) % mod;
	}

}
