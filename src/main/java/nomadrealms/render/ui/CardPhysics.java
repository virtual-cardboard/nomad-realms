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

	private CardTransform currentTransform;
	private CardTransform targetTransform;

	public boolean pauseRestoration = false;

	public CardPhysics(CardTransform initial) {
		this.currentTransform = initial;
		this.targetTransform = initial;
	}

	public CardPhysics targetCoord(ConstraintPair target) {
		targetTransform = new CardTransform(targetTransform.orientation(), target, targetTransform.size());
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
