package nomadrealms.render.ui.custom.card;

import static engine.common.math.Matrix4f.screenToPixel;
import static java.lang.Math.toRadians;

import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.common.math.Vector3f;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.lwjgl.GLContext;

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

	public void targetTransform(CardTransform transform) {
		this.targetTransform = transform;
	}

	public CardPhysics targetCoord(ConstraintPair target) {
		targetTransform.position(target);
		return this;
	}


	private CardTransform startTransform;
	private float linearProgress = -1;
	private float linearIncrement = 0;
	private int easingMode = 0; // 0 = linear, 1 = ease-in, 2 = ease-out

	public void interpolate() {
		if (pauseRestoration) {
			return;
		}
		if (linearProgress >= 0) {
			linearProgress += linearIncrement;
			if (linearProgress >= 1) {
				currentTransform = targetTransform;
				linearProgress = -1;
			} else {
				float t = linearProgress;
				if (easingMode == 1) {
					t = t * t;
				} else if (easingMode == 2) {
					t = 1 - (1 - t) * (1 - t);
				}
				currentTransform = startTransform.interpolate(targetTransform, t);
			}
		} else {
			currentTransform = currentTransform.interpolate(targetTransform, 0.1f);
		}
	}

	public boolean isLinearAnimationFinished() {
		return linearProgress == -1;
	}

	public void startLinearAnimation(CardTransform target, float frames) {
		startLinearAnimation(target, frames, 0);
	}

	public void startLinearAnimation(CardTransform target, float frames, int easing) {
		this.startTransform = currentTransform.copy();
		this.targetTransform = target;
		this.linearProgress = 0;
		this.linearIncrement = 1f / frames;
		this.easingMode = easing;
	}


	public void tilt(Vector2f velocity) {
		Vector3f perpendicular = new Vector3f(velocity.y(), -velocity.x(), 0);
		float rotateAmount = Math.min(30, velocity.length() * 0.3f);
		currentTransform = currentTransform.rotate(perpendicular, rotateAmount);
	}

	public void rotate(Vector3f axis, float degrees) {
		currentTransform = currentTransform.rotate(axis, degrees);
	}

	public Matrix4f cardTransform(GLContext glContext, Vector3f offsetOnCard) {
		return cardTransform(glContext, offsetOnCard, new Vector2f(1, 1));
	}

	public Matrix4f cardTransform(GLContext glContext, Vector3f offsetOnCard, Vector2f scale) {
		Vector2f halfDimensions = currentTransform.size().vector().scale(0.5f);
		return screenToPixel(glContext)
				.translate(position().vector().add(halfDimensions))
				.scale(new Vector3f(1, 1, 0f)) // Flatten the z-axis to avoid clipping

//				.translate(0.5f, 0.5f, 0)
				.rotate((float) toRadians(currentTransform.orientation().getAngle()), currentTransform.orientation().getAxis())
//				.translate(-0.5f, -0.5f, 0)

				.translate(offsetOnCard.add(new Vector3f(-halfDimensions.x(), -halfDimensions.y(), 0)))
				.scale(scale.x(), scale.y());
	}

	public ConstraintPair position() {
		return currentTransform.position();
	}

	public ConstraintPair centerPosition() {
		return currentTransform.position().add(currentTransform.size().scale(0.5f));
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

	public CardTransform currentTransform() {
		return currentTransform;
	}

}
