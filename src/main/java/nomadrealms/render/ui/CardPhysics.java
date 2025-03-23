package nomadrealms.render.ui;

import static common.math.Matrix4f.screenToPixel;
import static java.lang.Math.toRadians;
import static nomadrealms.game.card.UICard.cardSize;
import static visuals.constraint.posdim.AbsoluteConstraint.absolute;

import common.math.Matrix4f;
import common.math.UnitQuaternion;
import common.math.Vector2f;
import common.math.Vector3f;
import nomadrealms.render.ui.card.CardTransform;
import visuals.constraint.box.ConstraintBox;
import visuals.constraint.box.ConstraintPair;
import visuals.lwjgl.GLContext;

public class CardPhysics {

	private static final CardTransform DEFAULT_TRANSFORM = new CardTransform(new UnitQuaternion(new Vector3f(0, 0, 1), 0),
			new Vector3f(0, 0, 0), cardSize(1));

	private CardTransform targetTransform = DEFAULT_TRANSFORM.copy();
	private CardTransform currentTransform = DEFAULT_TRANSFORM.copy();

	/**
	 * Represents the current position of the ui card
	 */
	public Vector2f currentPosition = new Vector2f(0, 0);

	/**
	 * Represents the current size of the ui card
	 */
	public ConstraintPair cardSize;
	public ConstraintPair targetCoord = new ConstraintPair(absolute(0), absolute(0));

	public ConstraintPair centerToTopLeft;

	public boolean pauseRestoration = false;

	public CardPhysics(ConstraintPair cardSize) {
		this.cardSize = cardSize;
		this.centerToTopLeft = new ConstraintPair(
				cardSize.x().multiply(-0.5f),
				cardSize.y().multiply(-0.5f)
		);
	}

	public CardPhysics targetCoord(ConstraintPair target) {
		targetCoord = target;
		return this;
	}

	public void restoreOrientation() {
		if (pauseRestoration) {
			return;
		}
		currentTransform = currentTransform.interpolate(DEFAULT_TRANSFORM, 0.1f);
	}

	public void restorePosition() {
		if (pauseRestoration) {
			return;
		}
		ConstraintPair offset = targetCoord.sub(currentPosition).scale(0.1f);
		currentPosition = currentPosition.add(offset.vector());
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
				.translate(currentPosition)
				.scale(new Vector3f(1, 1, 0f)) // Flatten the z-axis to avoid clipping
				.rotate((float) toRadians(currentTransform.orientation().getAngle()), currentTransform.orientation().getAxis())
				.translate(centerToTopLeft)
				.translate(offsetOnCard)
				.scale(scale.x(), scale.y());
	}

	public Vector2f position() {
		return centerToTopLeft.add(currentPosition).vector();
	}

	public Vector2f centerPosition() {
		return currentPosition;
	}

	public CardPhysics snap() {
		currentTransform = targetTransform;
		return this;
	}

	public ConstraintBox cardBox() {
		return new ConstraintBox(
				centerToTopLeft.add(currentPosition),
				cardSize
		);
	}
}
