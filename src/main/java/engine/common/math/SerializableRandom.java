package engine.common.math;

import java.util.concurrent.atomic.AtomicLong;

class SerializableRandom {

	private AtomicLong seed;

	private static final long multiplier = 0x5DEECE66DL;
	private static final long addend = 0xBL;
	private static final long mask = (1L << 48) - 1;

	public SerializableRandom() {
	}

	public SerializableRandom(long seed) {
		this.seed = new AtomicLong(seed);
	}

	protected int next(int bits) {
		long oldseed, nextseed;
		AtomicLong seed = this.seed;
		do {
			oldseed = seed.get();
			nextseed = (oldseed * multiplier + addend) & mask;
		} while (!seed.compareAndSet(oldseed, nextseed));
		return (int) (nextseed >>> (48 - bits));
	}

	public int nextInt() {
		return next(32);
	}

	public int nextInt(int bound) {
		if (bound <= 0)
			throw new IllegalArgumentException("bound must be positive");

		int r = next(31);
		int m = bound - 1;
		if ((bound & m) == 0)  // i.e., bound is a power of 2
			r = (int) ((bound * (long) r) >> 31);
		else {
			for (int u = r;
			     u - (r = u % bound) + m < 0;
			     u = next(31))
				;
		}
		return r;
	}

	public long nextLong() {
		// it's okay that the bottom word remains signed.
		return ((long) (next(32)) << 32) + next(32);
	}

	public long seed() {
		return seed.get();
	}

}
