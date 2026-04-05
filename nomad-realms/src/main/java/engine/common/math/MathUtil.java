package engine.common.math;

public class MathUtil {

	public static int posMod(int val, int mod) {
		return (val % mod + mod) % mod;
	}

	public static float posMod(float val, float mod) {
		return (val % mod + mod) % mod;
	}

}
