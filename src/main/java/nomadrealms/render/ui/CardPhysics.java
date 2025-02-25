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
import visuals.constraint.box.ConstraintCoordinate;
import visuals.constraint.box.ConstraintSize;
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
	public ConstraintSize cardSize;
	public ConstraintCoordinate targetCoord = new ConstraintCoordinate(absolute(0), absolute(0));

	public ConstraintCoordinate centerToTopLeft;

	public boolean pauseRestoration = false;

	public CardPhysics(ConstraintSize cardSize) {
		this.cardSize = cardSize;
		this.centerToTopLeft = new ConstraintCoordinate(
				cardSize.w().multiply(-0.5f),
				cardSize.h().multiply(-0.5f)
		);
	}

	public CardPhysics targetCoord(ConstraintCoordinate target) {
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
		ConstraintCoordinate offset = targetCoord.subtract(currentPosition).multiply(0.1f);
		currentPosition = currentPosition.add(offset.value().toVector());
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
		return centerToTopLeft.translate(currentPosition).value().toVector();
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
				centerToTopLeft.translate(currentPosition),
				cardSize
		);
	}
}
