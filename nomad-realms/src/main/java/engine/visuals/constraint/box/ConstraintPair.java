package engine.visuals.constraint.box;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.common.math.Vector2f;
import engine.visuals.constraint.Constraint;

public class ConstraintPair {

	protected final Constraint x;
	protected final Constraint y;

	public ConstraintPair(Constraint x, Constraint y) {
		this.x = x;
		this.y = y;
	}

	public ConstraintPair(Vector2f absolute) {
		this(absolute(absolute.x()), absolute(absolute.y()));
	}

	/**
	 * Calculates the value of this {@link ConstraintPair} at a given time and returns it in a {@link Vector2f}.
	 *
	 * @return The value of this {@link ConstraintPair} as a {@link Vector2f}
	 */
	public Vector2f vector() {
		return new Vector2f(x.get(), y.get());
	}

	public Constraint x() {
		return x;
	}

	public Constraint y() {
		return y;
	}

	public ConstraintPair scale(float f) {
		return scale(absolute(f));
	}

	public ConstraintPair scale(Constraint f) {
		return scale(f, f);
	}

	public ConstraintPair scale(float f1, float f2) {
		return scale(absolute(f1), absolute(f2));
	}

	public ConstraintPair scale(Constraint f1, Constraint f2) {
		return new ConstraintPair(x.multiply(f1), y.multiply(f2));
	}

	public ConstraintPair add(float x, float y) {
		return add(absolute(x), absolute(y));
	}

	public ConstraintPair add(Vector2f vector) {
		return add(vector.x(), vector.y());
	}

	public ConstraintPair add(ConstraintPair other) {
		return add(other.x(), other.y());
	}

	public ConstraintPair add(Constraint cx, Constraint cy) {
		return new ConstraintPair(x.add(cx), y.add(cy));
	}

	public ConstraintPair neg() {
		return new ConstraintPair(x.neg(), y.neg());
	}

	public ConstraintPair sub(Vector2f vector) {
		return sub(vector.x(), vector.y());
	}

	public ConstraintPair sub(float x, float y) {
		return sub(absolute(x), absolute(y));
	}

	public ConstraintPair sub(ConstraintPair other) {
		return sub(other.x(), other.y());
	}

	public ConstraintPair sub(Constraint cx, Constraint cy) {
		return add(cx.neg(), cy.neg());
	}

}
