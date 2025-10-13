package engine.visuals.constraint.posdim;

import engine.visuals.constraint.Constraint;

/*
 * A {@link PosDimConstraint} that represents an absolute {@link Float} value.
 */
public class AbsoluteConstraint implements Constraint {

	private final float value;

	public AbsoluteConstraint(float value) {
		this.value = value;
	}

	public static Constraint absolute(float value) {
		return new AbsoluteConstraint(value);
	}

	public static Constraint zero() {
		return absolute(0);
	}

	@Override
	public float get() {
		return value;
	}

	@Override
	public Constraint add(Constraint c) {
		if (c instanceof AbsoluteConstraint) {
			AbsoluteConstraint constraint = (AbsoluteConstraint) c;
			return absolute(value + constraint.value);
		}
		return Constraint.super.add(c);
	}

	public Constraint multiply(Constraint c) {
		if (c instanceof AbsoluteConstraint) {
			AbsoluteConstraint constraint = (AbsoluteConstraint) c;
			return absolute(value * constraint.value);
		}
		return Constraint.super.multiply(c);
	}

	public Constraint neg() {
		return absolute(-value);
	}

}