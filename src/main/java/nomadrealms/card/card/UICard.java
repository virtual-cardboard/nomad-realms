package nomadrealms.card.card;

import static common.colour.Colour.rgb;
import static common.colour.Colour.toRangedVector;
import static common.math.Quaternion.interpolate;
import static visuals.constraint.posdim.AbsolutePosDimConstraint.absolute;

import common.math.Matrix4f;
import common.math.UnitQuaternion;
import common.math.Vector2f;
import common.math.Vector3f;
import nomadrealms.render.RenderingEnvironment;
import visuals.builtin.RectangleVertexArrayObject;
import visuals.constraint.ConstraintBox;
import visuals.constraint.ConstraintSize;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import visuals.lwjgl.render.meta.DrawFunction;

/**
 * UI cards are temporary objects that are used to display cards in the UI. They should be created and destroyed as
 * cards are added and removed from the UI.
 */
public class UICard implements Card {

	private static final UnitQuaternion DEFAULT_ORIENTATION = new UnitQuaternion(new Vector3f(0, 0, 1), 0);

	public UnitQuaternion currentOrientation = DEFAULT_ORIENTATION;
	public ConstraintBox basePosition;
	public Vector2f positionOffset = new Vector2f(0, 0);

	public WorldCard card;

	public UICard(WorldCard card, ConstraintBox basePosition) {
		this.card = card;
		this.basePosition = basePosition;
	}

	public WorldCard card() {
		return card;
	}

	public void render(RenderingEnvironment re) {
		DefaultFrameBuffer.instance().render(
				() -> {
					re.defaultShaderProgram
							.set("color", toRangedVector(rgb(10, 180, 140)))
							.set("transform",
									new Matrix4f(basePosition.translate(positionOffset), re.glContext).multiply(currentOrientation.toRotationMatrix()))
							.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
				}
		);
	}

	public void restoreOrientation() {
		currentOrientation = new UnitQuaternion(interpolate(currentOrientation, DEFAULT_ORIENTATION, 0.1f));
	}

	public void restorePosition() {
		positionOffset = positionOffset.scale(0.9f);
	}

	public void tilt(Vector2f velocity) {
		Vector3f perpendicular = new Vector3f(velocity.y(), -velocity.x(), 0);
		float rotateAmount = Math.min(40, velocity.length() * 0.3f);
		currentOrientation = currentOrientation.rotateBy(perpendicular, rotateAmount);
	}

	public static ConstraintSize size(ConstraintBox screen, float scale) {
		return new ConstraintSize(
				absolute(100),
				absolute(200)
		);
	}

}
