package engine.visuals.constraint.posdim;

import engine.visuals.constraint.Constraint;

public class SineConstraint implements Constraint {

	private final Constraint c;

	public SineConstraint(Constraint c) {
		this.c = c;
	}

	@Override
	public float get() {
		return (float) Math.sin(c.get());
	}

	public static Constraint sin(Constraint c) {
		return new SineConstraint(c);
	}

	@Override
	public int size() {
		return 1 + c.size();
	}

}
