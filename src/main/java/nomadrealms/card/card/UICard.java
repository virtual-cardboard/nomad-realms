package nomadrealms.card.card;

import static common.colour.Colour.rgb;
import static common.colour.Colour.toRangedVector;
import static common.math.Matrix4f.screenToPixel;
import static common.math.Quaternion.interpolate;
import static visuals.constraint.posdim.AbsolutePosDimConstraint.absolute;

import common.math.Matrix4f;
import common.math.UnitQuaternion;
import common.math.Vector2f;
import common.math.Vector3f;
import nomadrealms.render.RenderingEnvironment;
import visuals.builtin.RectangleVertexArrayObject;
import visuals.constraint.ConstraintBox;
import visuals.constraint.ConstraintCoordinate;
import visuals.constraint.ConstraintSize;
import visuals.lwjgl.GLContext;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import visuals.lwjgl.render.meta.DrawFunction;

/**
 * UI cards are temporary objects that are used to display cards in the UI. They should be created and destroyed as
 * cards are added and removed from the UI.
 */
public class UICard implements Card {

	private static final UnitQuaternion DEFAULT_ORIENTATION = new UnitQuaternion(new Vector3f(0, 0, 1), 0);

	public UnitQuaternion currentOrientation = DEFAULT_ORIENTATION;
	public final ConstraintBox basePosition;
	public final ConstraintCoordinate cardCenter;
	public Vector2f positionOffset = new Vector2f(0, 0);

	public WorldCard card;

	public UICard(WorldCard card, ConstraintBox basePosition) {
		this.card = card;
		this.basePosition = basePosition;
		this.cardCenter = basePosition.coordinate().translate(
				basePosition.size().w().multiply(0.5f),
				basePosition.size().h().multiply(0.5f)
		);
	}

	public WorldCard card() {
		return card;
	}

	public void render(RenderingEnvironment re) {
		DefaultFrameBuffer.instance().render(
				() -> {
					// Simple rotate
					re.defaultShaderProgram
							.set("color", toRangedVector(rgb(100, 0, 0)))
							.set("transform", cardTransform(
									re.glContext,
									new Vector3f(-basePosition.w().get() * 0.5f, -basePosition.h().get() * 0.5f, 0),
									new Vector2f(basePosition.w().get(), basePosition.h().get())))
							.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));

					re.textRenderer
							.render(cardTransform(
											re.glContext,
											new Vector3f(0, 0, 0),
											new Vector2f(1, 1)),
									"hi", 0, re.font, 30f, rgb(255, 255, 255));
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
		float rotateAmount = Math.min(30, velocity.length() * 0.3f);
		currentOrientation = currentOrientation.rotateBy(perpendicular, rotateAmount);
	}

	public static ConstraintSize size(ConstraintBox screen, float scale) {
		return new ConstraintSize(
				absolute(100),
				absolute(200)
		);
	}

	public Matrix4f cardTransform(GLContext glContext, Vector3f offsetOnCard, Vector2f scale) {
		return screenToPixel(glContext)
				.translate(cardCenter.translate(positionOffset))
				.scale(new Vector3f(1, 1, 0f)) // Flatten the z-axis to avoid clipping
				.rotate((float) Math.toRadians(currentOrientation.getAngle()), currentOrientation.getAxis())
				.translate(offsetOnCard)
				.scale(scale.x(), scale.y());
	}

	public Vector2f position() {
		return basePosition.coordinate().translate(positionOffset).value().toVector();
	}

}
