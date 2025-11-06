package engine.visuals.constraint.box;

import engine.visuals.constraint.Constraint;

public class ConstraintLine {

	private final Constraint pos;
	private final Constraint dim;

	public ConstraintLine(Constraint pos, Constraint dim) {
		this.pos = pos;
		this.dim = dim;
	}

	public float x() {
		return pos.get();
	}

	public float w() {
		return dim.get();
	}

	public Constraint xConstraint() {
		return pos;
	}

	public Constraint wConstraint() {
		return dim;
	}

}
