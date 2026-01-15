package nomadrealms.render.ui.custom.card;

import engine.common.math.Quaternion;
import engine.common.math.UnitQuaternion;
import engine.common.math.Vector3f;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;

/**
 * Represents the transformation of a card in 3D space. Immutable.
 */
public class CardTransform {

	/**
	 * Represents the orientation of the card.
	 */
	private UnitQuaternion orientation;

	/**
	 * Represents the position of the card.
	 */
	private ConstraintPair position;

	/**
	 * Represents the size of the card.
	 */
	private ConstraintPair size;

	public CardTransform(UnitQuaternion orientation, ConstraintPair position, ConstraintPair size) {
		this.orientation = orientation;
		this.position = position;
		this.size = size;
	}

	public CardTransform(UnitQuaternion orientation, ConstraintBox box) {
		this(orientation, box.coordinate(), box.dimensions());
	}

	/**
	 * Interpolate the current transform to another transform by a factor of t.
	 *
	 * @param other the other transform
	 * @param t     the factor to interpolate by
	 * @return the interpolated transform
	 */
	public CardTransform interpolate(CardTransform other, float t) {
		UnitQuaternion newOrientation = new UnitQuaternion(Quaternion.interpolate(orientation, other.orientation, t));
		ConstraintPair newPosition = position.add(other.position.sub(position).scale(t));
		ConstraintPair newSize = size.add(other.size.sub(size).scale(t));
		return new CardTransform(newOrientation, newPosition, newSize);
	}

	/**
	 * Interpolate the current transform to a rotation by a factor of t.
	 *
	 * @param other the rotation
	 * @param t     the factor to interpolate by
	 * @return the interpolated transform
	 */
	public CardTransform interpolate(UnitQuaternion other, float t) {
		return interpolate(new CardTransform(other, position, size), t);
	}

	/**
	 * Rotate the card by a given angle around a given axis.
	 *
	 * @param axis    the axis of rotation
	 * @param degrees the angle of rotation in degrees
	 * @return the rotated transform
	 */
	public CardTransform rotate(Vector3f axis, float degrees) {
		UnitQuaternion newOrientation = orientation.rotateBy(axis, degrees);
		return new CardTransform(newOrientation, position, size);
	}

	/**
	 * Scale the card by a given factor.
	 *
	 * @param factor the scaling factor
	 * @return the scaled transform
	 */
	public CardTransform scale(float factor) {
		ConstraintPair newSize = size.scale(factor);
		return new CardTransform(orientation, position, newSize);
	}

	/**
	 * Translate the card by a given constraint pair.
	 *
	 * @param translation the translation
	 * @return the translated transform
	 */
	public CardTransform translate(ConstraintPair translation) {
		ConstraintPair newPosition = position.add(translation);
		return new CardTransform(orientation, newPosition, size);
	}

	/**
	 * The orientation of the card.
	 *
	 * @return the orientation of the card
	 */
	public UnitQuaternion orientation() {
		return orientation;
	}

	/**
	 * The position of the card.
	 *
	 * @return the position of the card
	 */
	public ConstraintPair position() {
		return position;
	}

	/**
	 * The size of the card.
	 *
	 * @return the size of the card
	 */
	public ConstraintPair size() {
		return size;
	}

	/**
	 * Set the orientation of the card.
	 */
	public void orientation(UnitQuaternion orientation) {
		this.orientation = orientation;
	}

	/**
	 * Set the position of the card.
	 */
	public void position(ConstraintPair position) {
		this.position = position;
	}

	/**
	 * Set the size of the card.
	 */
	public void size(ConstraintPair size) {
		this.size = size;
	}

	/**
	 * Create a copy of the transform.
	 *
	 * @return a copy of the transform
	 */
	public CardTransform copy() {
		return new CardTransform(orientation, position, size);
	}
}