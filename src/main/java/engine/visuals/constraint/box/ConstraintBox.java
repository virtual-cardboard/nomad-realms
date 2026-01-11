package engine.visuals.constraint.box;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.common.math.Vector2f;
import engine.common.math.Vector4f;
import engine.visuals.constraint.Constraint;

public class ConstraintBox {

	private final Constraint x, y, w, h;

	public ConstraintBox(Constraint x, Constraint y, Constraint w, Constraint h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public ConstraintBox(ConstraintPair coordinate, ConstraintPair size) {
		this(coordinate.x(), coordinate.y(), size.x(), size.y());
	}

	public ConstraintBox(Constraint x, Constraint y, ConstraintPair size) {
		this(x, y, size.x(), size.y());
	}

	public ConstraintBox(ConstraintPair coordinate, Constraint w, Constraint h) {
		this(coordinate.x(), coordinate.y(), w, h);
	}

	public ConstraintBoxValue get() {
		return new ConstraintBoxValue(x.get(), y.get(), w.get(), h.get());
	}

	public Constraint x() {
		return x;
	}

	public Constraint y() {
		return y;
	}

	public Constraint w() {
		return w;
	}

	public Constraint h() {
		return h;
	}

	public boolean contains(ConstraintPair coordinate) {
		return contains(coordinate.vector());
	}

	public boolean contains(Vector2f point) {
		return x.get() <= point.x() && point.x() <= x.get() + w.get() &&
				y.get() <= point.y() && point.y() <= y.get() + h.get();
	}

	public boolean contains(ConstraintBox box) {
		ConstraintBoxValue b = box.get();
		return contains(box.coordinate()) &&
				contains(box.coordinate().add(box.dimensions()));
	}

	public boolean overlaps(ConstraintBox box) {
		ConstraintBoxValue b = box.get();
		return x.get() < b.x() + b.w() &&
				x.get() + w.get() > b.x() &&
				y.get() < b.y() + b.h() &&
				y.get() + h.get() > b.y();
	}

	public ConstraintLine horizontal() {
		return new ConstraintLine(x, w);
	}

	public ConstraintLine vertical() {
		return new ConstraintLine(y, h);
	}

	public ConstraintPair coordinate() {
		return new ConstraintPair(x, y);
	}

	public ConstraintPair dimensions() {
		return new ConstraintPair(w, h);
	}

	public ConstraintBox translate(Constraint x, Constraint y) {
		return new ConstraintBox(this.x.add(x), this.y.add(y), w, h);
	}

	public ConstraintBox translate(float x, float y) {
		return translate(absolute(x), absolute(y));
	}

	public ConstraintBox translate(Vector2f v) {
		return translate(v.x(), v.y());
	}

	/**
	 * @return the coordinate of the center of the ConstraintBox.
	 */
	public ConstraintPair center() {
		return coordinate().add(dimensions().scale(0.5f));
	}

	public Constraint left() {
		return x();
	}

	public Constraint right() {
		return x().add(w());
	}

	public Constraint top() {
		return y();
	}

	public Constraint bottom() {
		return y().add(h());
	}

	public ConstraintBox expand(float w, float h) {
		return new ConstraintBox(
				coordinate(),
				dimensions().add(w, h)
		);
	}

	public Vector4f vector() {
		return new Vector4f(this);
	}
	
}
