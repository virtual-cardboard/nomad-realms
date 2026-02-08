package engine.visuals.constraint.posdim;

import engine.visuals.constraint.Constraint;

/**
 * Represents a {@link Constraint} that requires a parent Constraint.
 *
 * @author Lunkle
 */
public class NegativeConstraint implements Constraint {

	protected Constraint constraint;

	public NegativeConstraint(Constraint constraint) {
		this.constraint = constraint;
	}

	@Override
	public float get() {
		return -constraint.get();
	}

	public static NegativeConstraint negative(Constraint constraint) {
		return new NegativeConstraint(constraint);
	}

	@Override
	public Constraint flatten() {
		if (constraint instanceof NegativeConstraint) {
			return ((NegativeConstraint) constraint).constraint;
		}
		return this;
	}

}
