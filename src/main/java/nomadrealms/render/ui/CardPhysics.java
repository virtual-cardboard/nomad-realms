package nomadrealms.render.ui;

import static common.math.Matrix4f.screenToPixel;
import static java.lang.Math.toRadians;

import common.math.Matrix4f;
import common.math.Vector2f;
import common.math.Vector3f;
import nomadrealms.render.ui.card.CardTransform;
import visuals.constraint.box.ConstraintBox;
import visuals.constraint.box.ConstraintPair;
import visuals.lwjgl.GLContext;

/**
 *
 */
public class CardPhysics {

	private static final CardTransform DEFAULT_TRANSFORM = new CardTransform(new UnitQuaternion(new Vector3f(0, 0, 1), 0),
			new Vector3f(0, 0, 0), cardSize(1));

	private CardTransform targetTransform = DEFAULT_TRANSFORM.copy();
	private CardTransform currentTransform = DEFAULT_TRANSFORM.copy();

	public boolean pauseRestoration = false;

	public CardPhysics(CardTransform initialTransform) {
		currentTransform = initialTransform.copy();
		targetTransform = currentTransform.copy();

	}

	public CardPhysics targetCoord(ConstraintPair target) {
		targetTransform.position();
		targetCoord = target;
		return this;
	}

	public CardPhysics targetCoord(ConstraintPair target) {
		targetTransform.position(target);
		return this;
	}

	public void interpolate() {
		if (pauseRestoration) {
			return;
		}
		currentTransform = currentTransform.interpolate(targetTransform, 0.1f);
	}

	public void tilt(Vector2f velocity) {
		Vector3f perpendicular = new Vector3f(velocity.y(), -velocity.x(), 0);
		float rotateAmount = Math.min(30, velocity.length() * 0.3f);
		currentTransform.rotate(perpendicular, rotateAmount);
	}

	public Matrix4f cardTransform(GLContext glContext, Vector3f offsetOnCard) {
		return cardTransform(glContext, offsetOnCard, new Vector2f(1, 1));
	}

	public Matrix4f cardTransform(GLContext glContext, Vector3f offsetOnCard, Vector2f scale) {
		return screenToPixel(glContext)
				.translate(position().vector())
				.scale(new Vector3f(1, 1, 0f)) // Flatten the z-axis to avoid clipping
				.rotate((float) toRadians(currentTransform.orientation().getAngle()), currentTransform.orientation().getAxis())
				.translate(centerToTopLeft())
				.translate(offsetOnCard)
				.scale(scale.x(), scale.y());
	}

	public ConstraintPair position() {
		return centerToTopLeft().add(currentTransform.position());
	}

	public ConstraintPair centerPosition() {
		return currentTransform.position();
	}

	public CardPhysics snap() {
		currentTransform = targetTransform;
		return this;
	}

	public ConstraintBox cardBox() {
		return new ConstraintBox(
				position(), currentTransform.size()
		);
	}

	private ConstraintPair centerToTopLeft() {
		return currentTransform.size().scale(-0.5f);
	}

}
