package nomadrealms.game.card;

import static common.colour.Colour.rgb;
import static common.colour.Colour.toRangedVector;
import static common.math.Matrix4f.screenToPixel;
import static common.math.Quaternion.interpolate;
import static visuals.constraint.posdim.AbsolutePosDimConstraint.absolute;

import common.math.Matrix4f;
import common.math.UnitQuaternion;
import common.math.Vector2f;
import common.math.Vector3f;
import nomadrealms.game.card.target.TargetType;
import nomadrealms.game.card.target.TargetingInfo;
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
	public final ConstraintCoordinate baseCenter;
	public Vector2f positionOffset = new Vector2f(0, 0);

	public boolean pauseRestoration = false;
	private ConstraintCoordinate restorePosition;

	private WorldCard card;

	public UICard(WorldCard card, ConstraintBox basePosition) {
		this.card = card;
		this.basePosition = basePosition;
		this.baseCenter = basePosition.coordinate().translate(
				basePosition.size().w().multiply(0.5f),
				basePosition.size().h().multiply(0.5f)
		);
		this.restorePosition = basePosition.coordinate();
	}

	public boolean needsTarget() {
		return card.card.targetingInfo().targetType() != TargetType.NONE;
	}

	public TargetingInfo targetingInfo() {
		return card.card.targetingInfo();
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
											new Vector3f(-basePosition.w().get() * 0.5f, -basePosition.h().get() * 0.5f, 0),
											new Vector2f(1, 1)),
									card.card.name(), 0, re.font, 20f, rgb(255, 255, 255));
					re.textRenderer
							.render(cardTransform(
											re.glContext,
											new Vector3f(-basePosition.w().get() * 0.5f,
													-basePosition.h().get() * 0.3f, 0),
											new Vector2f(1, 1)),
									card.card.description(), 100, re.font, 15f, rgb(255, 255, 255));
				}
		);
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
		Vector2f currentPosition = position();
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
				.translate(baseCenter.translate(positionOffset))
				.scale(new Vector3f(1, 1, 0f)) // Flatten the z-axis to avoid clipping
				.rotate((float) Math.toRadians(currentOrientation.getAngle()), currentOrientation.getAxis())
				.translate(offsetOnCard)
				.scale(scale.x(), scale.y());
	}

	public Vector2f position() {
		return basePosition.coordinate().translate(positionOffset).value().toVector();
	}

	public Vector2f centerPosition() {
		return baseCenter.translate(positionOffset).value().toVector();
	}

}
