package nomadrealms.card.card;

import static common.math.Quaternion.interpolate;

import common.math.Matrix4f;
import common.math.UnitQuaternion;
import common.math.Vector2f;
import common.math.Vector3f;

/**
 * UI cards are temporary objects that are used to display cards in the UI. They should be created and destroyed as
 * cards are added and removed from the UI.
 */
public class UICard implements Card {

	private static final UnitQuaternion DEFAULT_ORIENTATION = new UnitQuaternion(new Vector3f(0, 0, 1), 0);

	UnitQuaternion currentOrientation = DEFAULT_ORIENTATION;
	Vector2f position;

	WorldCard card;



	public UICard(WorldCard card, Vector2f position) {
		this.card = card;
		this.position = position;
	}

	public WorldCard card() {
		return card;
	}

	private void restore() {
		currentOrientation = new UnitQuaternion(interpolate(currentOrientation, DEFAULT_ORIENTATION, 0.2f));
	}

	private void tilt(Vector2f velocity) {
		Vector3f perpendicular = new Vector3f(velocity.y(), -velocity.x(), 0);
		float rotateAmount = Math.min(40, velocity.length() * 0.3f);
		currentOrientation = currentOrientation.rotateBy(perpendicular, rotateAmount);
	}


}
