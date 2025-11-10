package nomadrealms.context.game.item;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.common.math.UnitQuaternion;
import engine.common.math.Vector2f;
import engine.common.math.Vector3f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.custom.card.CardPhysics;
import nomadrealms.render.ui.custom.card.CardTransform;

/**
 * UI cards are temporary objects that are used to display cards in the UI. They should be created and destroyed as
 * cards are added and removed from the UI.
 */
public class UIItem {

	private final CardPhysics physics;

	private final WorldItem item;

	public UIItem(WorldItem item, ConstraintBox screen, ConstraintPair basePosition) {
		this.item = item;
		physics = new CardPhysics(new CardTransform(new UnitQuaternion(), basePosition, UIItem.size(screen, 2)));
	}

	public void render(RenderingEnvironment re) {
		DefaultFrameBuffer.instance().render(
				() -> {
					// Simple rotate
					re.defaultShaderProgram
							.set("color", toRangedVector(rgb(100, 0, 0)))
							.set("transform", physics.cardTransform(
									re.glContext,
									new Vector3f(0, 0, 0),
									new Vector2f(physics.cardBox().w().get(), physics.cardBox().h().get())))
							.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
					re.textureRenderer.render(
							re.imageMap.get(item.item.image()),
							physics.cardTransform(
									re.glContext,
									new Vector3f(0, 0, 0),
									new Vector2f(physics.cardBox().w().get(), physics.cardBox().h().get()))
					);
					re.textRenderer
							.render(physics.cardTransform(
											re.glContext,
											new Vector3f(0, 0, 0),
											new Vector2f(1, 1)),
									item.item.name(), 0, re.font, 20f, rgb(255, 255, 255));
					re.textRenderer
							.render(physics.cardTransform(
											re.glContext,
											new Vector3f(0, 40, 0),
											new Vector2f(1, 1)),
									item.item.description(), 100, re.font, 15f, rgb(255, 255, 255));
				}
		);
	}

	public WorldItem item() {
		return item;
	}

	public void interpolate() {
		physics.interpolate();
	}

	public void move(float x, float y) {
		physics.targetCoord(physics.targetTransform().position().add(x, y));
	}

	public void tilt(Vector2f velocity) {
		physics.tilt(velocity);
	}

	public static ConstraintPair size(ConstraintBox screen, float scale) {
		return new ConstraintPair(
				absolute(100),
				absolute(100)
		);
	}

	public ConstraintPair position() {
		return physics.position();
	}

	public ConstraintPair centerPosition() {
		return physics.centerPosition();
	}

	public CardPhysics physics() {
		return physics;
	}

}
