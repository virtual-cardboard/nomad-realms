package nomadrealms.game.card;

import static common.colour.Colour.rgb;
import static common.colour.Colour.toRangedVector;
import static visuals.constraint.posdim.AbsolutePosDimConstraint.absolute;

import common.math.Vector2f;
import common.math.Vector3f;
import nomadrealms.game.card.target.TargetType;
import nomadrealms.game.card.target.TargetingInfo;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.CardPhysics;
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

	public final ConstraintBox basePosition;
	private final CardPhysics physics;

	private final WorldCard card;

	public UICard(WorldCard card, ConstraintBox screen, ConstraintBox basePosition) {
		this.card = card;
		this.basePosition = basePosition;
		physics = new CardPhysics(UICard.size(screen, 2)).targetCoord(basePosition.coordinate()).snap();
	}

	/**
	 * Returns whether this card needs a target to be played.
	 * <p>
	 * For a human player, the card will freeze in place when dragged out and the player needs to use the targeting
	 * arrow to select a valid target.
	 *
	 * @return whether this card needs a target to be played
	 */
	public boolean needsTarget() {
		return card.card.targetingInfo().targetType() != TargetType.NONE;
	}

	/**
	 * Returns the targeting info for this card.
	 *
	 * @return the targeting info for this card
	 * @see TargetingInfo
	 */
	public TargetingInfo targetingInfo() {
		return card.card.targetingInfo();
	}

	/**
	 * Returns the card that this UI card represents.
	 *
	 * @return the world card
	 */
	public WorldCard card() {
		return card;
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
									new Vector2f(basePosition.w().get(), basePosition.h().get())))
							.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));

					re.textRenderer
							.render(physics.cardTransform(
											re.glContext,
											new Vector3f(0, 0, 0),
											new Vector2f(1, 1)),
									card.card.title(), 0, re.font, 20f, rgb(255, 255, 255));
					re.textRenderer
							.render(physics.cardTransform(
											re.glContext,
											new Vector3f(0, 40, 0),
											new Vector2f(1, 1)),
									card.card.description(), 100, re.font, 15f, rgb(255, 255, 255));
				}
		);
	}

	public void restoreOrientation() {
		physics.restoreOrientation();
	}

	public void restorePosition() {
		physics.restorePosition();
	}

	public void move(float x, float y) {
		physics.cardPos = physics.cardPos.add(x, y);
	}

	public void tilt(Vector2f velocity) {
		physics.tilt(velocity);
	}

	public static ConstraintSize size(ConstraintBox screen, float scale) {
		return new ConstraintSize(
				absolute(100),
				absolute(200)
		);
	}

	public Vector2f position() {
		return physics.position();
	}

	public Vector2f centerPosition() {
		return physics.centerPosition();
	}

	public CardPhysics physics() {
		return physics;
	}

}
