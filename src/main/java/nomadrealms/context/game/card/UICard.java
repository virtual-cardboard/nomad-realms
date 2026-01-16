package nomadrealms.context.game.card;

import static engine.common.colour.Colour.rgb;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.common.math.UnitQuaternion;
import engine.common.math.Vector2f;
import engine.common.math.Vector3f;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import nomadrealms.context.game.card.target.TargetType;
import nomadrealms.context.game.card.target.TargetingInfo;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.custom.card.CardPhysics;
import nomadrealms.render.ui.custom.card.CardTransform;

/**
 * UI cards are temporary objects that are used to display cards in the UI. They should be created and destroyed as
 * cards are added and removed from the UI.
 * <p>
 * This class describes how to render the card displayed on screen.
 *
 * @author Lunkle
 */
public class UICard implements Card {

	private final CardPhysics physics;

	private final WorldCard card;

	public UICard(WorldCard card, ConstraintBox constraintBox) {
		this(card, new CardTransform(new UnitQuaternion(), constraintBox));
	}

	public UICard(WorldCard card, CardTransform transform) {
		this.card = card;
		physics = new CardPhysics(transform);
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
		return card.card().targetingInfo().targetType() != TargetType.NONE;
	}

	/**
	 * Returns the targeting info for this card.
	 *
	 * @return the targeting info for this card
	 * @see TargetingInfo
	 */
	public TargetingInfo targetingInfo() {
		return card.card().targetingInfo();
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
		if (isUpsideDown()) {
			re.textureRenderer.render(
					re.imageMap.get("card_back"),
					physics.cardTransform(
							re.glContext,
							new Vector3f(0, 0, 0), physics.cardBox().dimensions().vector())
			);
			return;
		}
		re.textureRenderer.render(
				re.imageMap.get("card_front"),
				physics.cardTransform(
						re.glContext,
						new Vector3f(0, 0, 0), physics.cardBox().dimensions().vector())
		);
		re.textureRenderer.render(
				re.imageMap.get(card().card().artwork()),
				physics.cardTransform(
						re.glContext,
						new Vector3f(0, 0, 10),
						new Vector2f(
								physics.cardBox().w().get(),
								physics.cardBox().w().get()))
		);
		re.textRenderer.alignLeft().alignTop();
		re.textRenderer
				.render(
						physics.cardTransform(
								re.glContext,
								new Vector3f(
										physics.cardBox().w().multiply(0.06f).get(),
										physics.cardBox().w().multiply(0.01f).get(),
										0)),
						card.card().title(), 0,
						re.font, 20f,
						rgb(255, 255, 255));
		re.textRenderer
				.render(physics.cardTransform(
								re.glContext,
								new Vector3f(
										physics.cardBox().w().multiply(0.06f).get(),
										physics.cardBox().h().multiply(0.5f).get(),
										0)),
						card.card().description(),
						physics.cardBox().w().multiply(0.88f).get(),
						re.font, 14f,
						rgb(255, 255, 255));
		re.textRenderer.alignRight().alignTop();
		re.textRenderer.render(
				physics.cardTransform(
						re.glContext,
						new Vector3f(
								physics.cardBox().w().multiply(0.94f).get(),
								physics.cardBox().h().multiply(0.01f).get(),
								0)),
				String.valueOf(card.card().resolutionTime()), 0,
				re.font, 20f,
				rgb(255, 255, 255));
	}

	/**
	 * Returns whether the card is upside down (i.e., rotated 180 degrees around the X axis). Determines if the card
	 * front or back should be rendered.
	 *
	 * @return whether the card is upside down
	 */
	private boolean isUpsideDown() {
		Vector3f up = new Vector3f(0, 0, 1);
		Vector3f rotatedUp = physics.currentTransform().orientation().rotateVector3f(up);
		return rotatedUp.z() < 0;
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

	public static ConstraintPair cardSize(float scale) {
		return new ConstraintPair(
				absolute(2.5f * 30 * scale),
				absolute(3.5f * 30 * scale)
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
