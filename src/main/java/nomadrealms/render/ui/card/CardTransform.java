package nomadrealms.render.ui.card;

import common.math.Quaternion;
import common.math.UnitQuaternion;
import common.math.Vector3f;
import visuals.constraint.box.ConstraintBox;
import visuals.constraint.box.ConstraintPair;

/**
 * Represents the transformation of a card in 3D space. Immutable.
 */
public class CardTransform {

	/**
	 * Represents the orientation of the card.
	 */
	private final UnitQuaternion orientation;

	/**
	 * Represents the position of the card.
	 */
	private final ConstraintPair position;

	/**
	 * Represents the size of the card.
	 * 
	 * 
	 */
	private final ConstraintPair size;

	public CardTransform(UnitQuaternion orientation, ConstraintPair position, ConstraintPair size) {
		this.orientation = orientation;
		this.position = position;
		this.size = size;
	}

	public CardTransform(UnitQuaternion orientation, ConstraintBox box) {
		this.orientation = orientation;
		this.position = box.coordinate();
		this.size = box.dimensions();
	}

	public CardTransform rotate(Vector3f axis, float angle) {
		UnitQuaternion newOrientation = orientation.rotateBy(axis, angle);
		return new CardTransform(newOrientation, position, size);
	}

	public CardTransform interpolate(CardTransform other, float t) {
		UnitQuaternion newOrientation = new UnitQuaternion(Quaternion.interpolate(orientation, other.orientation, t));
		ConstraintPair newPosition = position.add(other.position.sub(position).scale(t));
		ConstraintPair newSize = size.add(other.size.sub(size).scale(t));
		return new CardTransform(newOrientation, newPosition, newSize);
	}

	public CardTransform interpolate(UnitQuaternion other, float t) {
		return interpolate(new CardTransform(other, position, size), t);
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

	public CardTransform copy() {
		return new CardTransform(orientation, position, size);
	}

}