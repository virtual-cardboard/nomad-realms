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

	public static Constraint absolute(double value) {
		return absolute((float) value);
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
		return c.doAdd(this);
	}

	@Override
	public Constraint multiply(Constraint c) {
		return c.doMultiply(this);
	}

	@Override
	public Constraint doAdd(AbsoluteConstraint c) {
		return absolute(value + c.value);
	}

	@Override
	public Constraint doMultiply(AbsoluteConstraint c) {
		return absolute(value * c.value);
	}

	@Override
	public Constraint doMultiply(CustomSupplierConstraint c) {
		return c.multiply(value);
	}

	public Constraint neg() {
		return absolute(-value);
	}

}