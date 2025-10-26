package nomadrealms.render.ui;

import static engine.common.math.Matrix4f.screenToPixel;
import static java.lang.Math.toRadians;

import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.common.math.Vector3f;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.lwjgl.GLContext;
import nomadrealms.render.ui.card.CardTransform;

/**
 * Represents the physics of a card, to control its animation. Mutable.
 */
public class CardPhysics {

	private CardTransform currentTransform;
	private CardTransform targetTransform;

	public boolean pauseRestoration = false;

	public CardPhysics(CardTransform initial) {
		this.currentTransform = initial;
		this.targetTransform = initial;
	}

	public CardTransform targetTransform() {
		return targetTransform;
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
		currentTransform = currentTransform.rotate(perpendicular, rotateAmount);
	}

	public Matrix4f cardTransform(GLContext glContext, Vector3f offsetOnCard) {
		return cardTransform(glContext, offsetOnCard, new Vector2f(1, 1));
	}

	public Matrix4f cardTransform(GLContext glContext, Vector3f offsetOnCard, Vector2f scale) {
		return screenToPixel(glContext)
				.translate(position().vector())
				.scale(new Vector3f(1, 1, 0f)) // Flatten the z-axis to avoid clipping
				.rotate((float) toRadians(currentTransform.orientation().getAngle()), currentTransform.orientation().getAxis())
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
