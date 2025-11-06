package engine.common.math;

public class MathUtil {

	public static int posMod(int val, int mod) {
		return (val % mod + mod) % mod;
	}

}
