package nomadrealms.game.card;

import static common.colour.Colour.rgb;
import static visuals.constraint.posdim.AbsoluteConstraint.absolute;

import common.math.Vector2f;
import common.math.Vector3f;
import nomadrealms.game.card.target.TargetType;
import nomadrealms.game.card.target.TargetingInfo;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.CardPhysics;
import visuals.constraint.box.ConstraintBox;
import visuals.constraint.box.ConstraintPair;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;

/**
 * UI cards are temporary objects that are used to display cards in the UI. They should be created and destroyed as
 * cards are added and removed from the UI.
 * <p>
 * This class describes how to render the card displayed on screen.
 *
 * @author Lunkle
 */
public class UICard implements Card {

	public final ConstraintBox constraintBox;
	private final CardPhysics physics;

	private final WorldCard card;

	public UICard(WorldCard card, ConstraintBox constraintBox) {
		this.card = card;
		this.constraintBox = constraintBox;
		physics = new CardPhysics(UICard.cardSize(2)).targetCoord(constraintBox.coordinate()).snap();
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
					re.textureRenderer.render(
							re.imageMap.get("card_front"),
							physics.cardTransform(
									re.glContext,
									new Vector3f(0, 0, 0), constraintBox.dimensions().vector())
					);
					re.textRenderer
							.render(
									physics.cardTransform(
											re.glContext,
											new Vector3f(
													constraintBox.w().multiply(0.06f).get(),
													constraintBox.w().multiply(0.01f).get(),
													0)),
									card.card.title(), 0, re.font, 20f, rgb(255, 255, 255));
					re.textRenderer
							.render(physics.cardTransform(
											re.glContext,
											new Vector3f(
													constraintBox.w().multiply(0.06f).get(),
													constraintBox.h().multiply(0.5f).get(),
													0)),
									card.card.description(), constraintBox.w().multiply(0.88f).get(), re.font, 15f,
									rgb(255, 255, 255));
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
		physics.currentPosition = physics.currentPosition.add(x, y);
	}

	public void tilt(Vector2f velocity) {
		physics.tilt(velocity);
	}

	public static ConstraintPair cardSize(float scale) {
		return new ConstraintPair(
				absolute(2.5f * 30 * scale),
				absolute(3.5f * 30 * scale)
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
