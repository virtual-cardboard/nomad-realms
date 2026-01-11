package engine.visuals.constraint.posdim;

import engine.visuals.constraint.Constraint;

public class CosineConstraint implements Constraint {

	private final Constraint c;

	public CosineConstraint(Constraint c) {
		this.c = c;
	}

	@Override
	public float get() {
		return (float) Math.cos(c.get());
	}

	public static Constraint cos(Constraint c) {
		return new CosineConstraint(c);
	}

	@Override
	public int size() {
		return 1 + c.size();
	}

}
