package engine.visuals.constraint.misc;

import engine.visuals.constraint.Constraint;
import nomadrealms.math.generation.map.OpenSimplexNoise;

public class NoiseConstraint implements Constraint {

	private final OpenSimplexNoise noise;
	private final Constraint constraint;

	public NoiseConstraint(Constraint constraint) {
		this.noise = new OpenSimplexNoise();
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
