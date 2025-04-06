package nomadrealms.render.ui.card;

import common.math.Quaternion;
import common.math.UnitQuaternion;
import common.math.Vector3f;
import visuals.constraint.box.ConstraintBox;
import visuals.constraint.box.ConstraintPair;


/**
 * Represents the transformation of a card in 3D space. Mutable.
 */
public class CardTransform {

	/**
	 * Represents the orientation of the card.
	 */
	private UnitQuaternion orientation;

	/**
	 * Represents the position of the card.
	 */
	private Vector3f position;

	/**
	 * Represents the bounding box of the card. The {@link ConstraintBox#coordinate()} represents the center of the card.
	 */
	private ConstraintBox box;

	/**
	 * Represents the size of the card.
	 */
	private ConstraintPair size;

	public CardTransform(UnitQuaternion orientation, Vector3f position, ConstraintPair size) {
		this.orientation = orientation;
		this.position = position;
		this.size = size;
	}

	public CardTransform(UnitQuaternion orientation, ConstraintBox box) {
		this.orientation = orientation;
		this.box = box;
	}

	public CardTransform rotate(Vector3f axis, float angle) {
		orientation = orientation.rotateBy(axis, angle);
		return this;
	}

	public CardTransform interpolate(CardTransform other, float t) {
		orientation = new UnitQuaternion(Quaternion.interpolate(orientation, other.orientation, t));
		position.add(other.position.sub(position).scale(t));
		//		size.add(other.size).scale(0.5f);
		return this;
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
	public Vector3f position() {
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
	public void position(Vector3f position) {
		this.position = position;
	}

	/**
	 * Set the size of the card.
	 */
	public void size(ConstraintPair size) {
		this.size = size;
	}

	public CardTransform copy() {
		return new CardTransform(orientation, position, size);
	}

}