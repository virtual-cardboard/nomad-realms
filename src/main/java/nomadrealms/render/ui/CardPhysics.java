package nomadrealms.render.ui;

import common.math.Matrix4f;
import common.math.UnitQuaternion;
import common.math.Vector2f;
import common.math.Vector3f;
import visuals.constraint.ConstraintBox;
import visuals.constraint.ConstraintCoordinate;
import visuals.constraint.ConstraintSize;
import visuals.lwjgl.GLContext;

import static common.math.Matrix4f.screenToPixel;
import static common.math.Quaternion.interpolate;
import static java.lang.Math.toRadians;
import static visuals.constraint.posdim.AbsolutePosDimConstraint.absolute;

public class CardPhysics {

    private static final UnitQuaternion DEFAULT_ORIENTATION = new UnitQuaternion(new Vector3f(0, 0, 1), 0);

    public UnitQuaternion currentOrientation = DEFAULT_ORIENTATION;
    public Vector2f cardPos = new Vector2f(0, 0);
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
        currentOrientation = new UnitQuaternion(interpolate(currentOrientation, DEFAULT_ORIENTATION, 0.1f));
    }

    public void restorePosition() {
        if (pauseRestoration) {
            return;
        }
        ConstraintCoordinate offset = targetCoord.subtract(cardPos).multiply(0.1f);
        cardPos = cardPos.add(offset.value().toVector());
    }

    public void tilt(Vector2f velocity) {
        Vector3f perpendicular = new Vector3f(velocity.y(), -velocity.x(), 0);
        float rotateAmount = Math.min(30, velocity.length() * 0.3f);
        currentOrientation = currentOrientation.rotateBy(perpendicular, rotateAmount);
    }

    public Matrix4f cardTransform(GLContext glContext, Vector3f offsetOnCard, Vector2f scale) {
        return screenToPixel(glContext)
                .translate(cardPos)
                .scale(new Vector3f(1, 1, 0f)) // Flatten the z-axis to avoid clipping
                .rotate((float) toRadians(currentOrientation.getAngle()), currentOrientation.getAxis())
                .translate(centerToTopLeft)
				.translate(offsetOnCard)
				.scale(scale.x(), scale.y());
    }

    public Vector2f position() {
        return centerToTopLeft.translate(cardPos).value().toVector();
    }

    public Vector2f centerPosition() {
        return cardPos;
    }

    public CardPhysics snap() {
        cardPos = targetCoord.value().toVector();
        currentOrientation = DEFAULT_ORIENTATION;
        return this;
    }

    public ConstraintBox cardBox() {
        return new ConstraintBox(
                centerToTopLeft.translate(cardPos),
                cardSize
        );
    }
}
