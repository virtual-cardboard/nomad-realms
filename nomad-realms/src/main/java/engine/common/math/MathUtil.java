package engine.common.math;

public class MathUtil {

	/**
	 * This is an overloaded hash method for the most common cases to prevent array allocation on every invocation of
	 * hash()
	 */
	public static int hash(int v1, int v2) {
		int h = 7;
		h = mix(h, v1);
		h = mix(h, v2);
		return h;
	}

	/**
	 * This is an overloaded hash method for the most common cases to prevent array allocation on every invocation of
	 * hash()
	 */
	public static int hash(int v1, int v2, int v3) {
		int h = 7;
		h = mix(h, v1);
		h = mix(h, v2);
		h = mix(h, v3);
		return h;
	}

	public static int hash(int... values) {
		int h = 7;
		for (int v : values) {
			h = mix(h, v);
		}
		return h;
	}

	private static int mix(int h, int v) {
		h = 31 * h + v;
		h ^= (h >>> 16);
		h *= 0x85ebca6b;
		h ^= (h >>> 13);
		h *= 0xc2b2ae35;
		h ^= (h >>> 16);
		return h;
	}

	public static int posMod(int val, int mod) {
		return (val % mod + mod) % mod;
	}

	public static float posMod(float val, float mod) {
		return (val % mod + mod) % mod;
	}

}
