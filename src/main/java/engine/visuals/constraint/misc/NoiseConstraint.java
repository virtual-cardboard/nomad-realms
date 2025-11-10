package engine.visuals.constraint.misc;

import static java.lang.Long.MAX_VALUE;
import static java.lang.Math.random;

import engine.visuals.constraint.Constraint;
import nomadrealms.math.generation.map.OpenSimplexNoise;

public class NoiseConstraint implements Constraint {

	private final OpenSimplexNoise noise;
	private final Constraint constraint;

	public NoiseConstraint(Constraint constraint) {
		this.noise = new OpenSimplexNoise((long) (MAX_VALUE * random()));
		this.constraint = constraint;
	}

	@Override
	public float get() {
		return (float) noise.eval(constraint.get(), 0, 0);
	}

	public static NoiseConstraint noise(Constraint c) {
		return new NoiseConstraint(c);
	}


}
