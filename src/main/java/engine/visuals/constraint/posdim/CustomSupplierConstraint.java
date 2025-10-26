package engine.visuals.constraint.posdim;

import java.util.function.Supplier;

import engine.visuals.constraint.Constraint;

/**
 * A {@link Constraint} that represents a custom {@link Supplier} of {@link Float}.
 *
 * @see Constraint
 */
public class CustomSupplierConstraint implements Constraint {

	private final String name;
	/**
	 * The multiplier of this {@link Constraint}. This multiplier is used as an optimization, and should never be seen
	 * by the user of the {@link Constraint} library.
	 */
	private final float multiplier;
	private final Supplier<Float> supplier;

	protected CustomSupplierConstraint(String name, float multiplier, Supplier<Float> supplier) {
		this.name = name;
		this.multiplier = multiplier;
		this.supplier = supplier;
	}

	public CustomSupplierConstraint(String name, Supplier<Float> supplier) {
		this(name, 1, supplier);
	}

	public static Constraint custom(String name, Supplier<Float> supplier) {
		return new CustomSupplierConstraint(name, supplier);
	}

	@Override
	public float get() {
		return multiplier * supplier.get();
	}

	@Override
	public Constraint multiply(Constraint c) {
		return c.doMultiply(this);
	}

	@Override
	public Constraint doMultiply(AbsoluteConstraint c) {
		return multiply(c.get());
	}

	@Override
	public Constraint multiply(float f) {
		return new CustomSupplierConstraint(name, multiplier * f, supplier);
	}

	@Override
	public Constraint neg() {
		return new CustomSupplierConstraint(name, -multiplier, supplier);
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public String toString() {
		return name;
	}

	protected String name() {
		return name;
	}

	protected float multiplier() {
		return multiplier;
	}

	protected Constraint multiplier(float f) {
		return new CustomSupplierConstraint(name, f, supplier);
	}

	protected Supplier<Float> supplier() {
		return supplier;
	}

}
