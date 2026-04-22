package nomadrealms.context.game.actor.types.cardplayer;
import nomadrealms.context.game.interaction.InteractionState;

import static engine.common.colour.Colour.rgba;
import static engine.visuals.rendering.text.HorizontalAlign.CENTER;
import static engine.visuals.rendering.text.TextFormat.textFormat;
import static engine.visuals.rendering.text.VerticalAlign.TOP;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.EYE;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.HEAD;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.LEG;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.TAIL;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.TORSO;
import static nomadrealms.context.game.card.GameCard.MEANDER;
import static nomadrealms.context.game.card.GameCard.MELEE_ATTACK;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

import static java.util.Arrays.asList;

import engine.common.math.Vector2f;
import java.util.List;
import nomadrealms.context.game.actor.ai.WolfAI;
import nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage;
import nomadrealms.context.game.card.collection.DeckList;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;

public class Wolf extends CardPlayer {

	private final String name;

	/**
	 * No-arg constructor for serialization.
	 */
	protected Wolf() {
		this.name = "Wolf";
	}

	public Wolf(String name, Tile tile) {
		this.setAi(new WolfAI(this));
		this.name = name;
		this.tile(tile);
		this.health(15);
		this.deckCollection().deck1().addCards(new DeckList(MEANDER).toDeck().getCards());
		this.deckCollection().deck2().addCards(new DeckList(MELEE_ATTACK).toDeck().getCards());
	}

	public void render(RenderingEnvironment re, InteractionState interactionState) {
		float scale = 0.6f * TILE_RADIUS * interactionState.camera.zoom().get();
		Vector2f screenPosition = getScreenPosition(re, interactionState).vector();
		re.textureRenderer.render(
				re.imageMap.get("wolf"),
				screenPosition.x() - 0.5f * scale,
				screenPosition.y() - 0.7f * scale,
				scale, scale);
		re.textRenderer.render(
				screenPosition.x(),
				screenPosition.y() + 0.1f * scale,
				textFormat()
						.text(name + " WOLF")
						.font(re.font)
						.fontSize(0.5f * scale)
						.colour(rgba(255, 255, 255, (int) (interactionState.actorTextOpacity * 255)))
						.hAlign(CENTER)
						.vAlign(TOP));
		re.textRenderer.render(
				screenPosition.x(),
				screenPosition.y() + 0.5f * scale,
				textFormat()
						.text(health() + " HP")
						.font(re.font)
						.fontSize(0.5f * scale)
						.colour(rgba(255, 255, 255, (int) (interactionState.actorTextOpacity * 255)))
						.hAlign(CENTER)
						.vAlign(TOP));
		super.render(re, interactionState);
	}

	@Override
	public List<Appendage> appendages() {
		return asList(HEAD, EYE, EYE, TORSO, LEG, LEG, LEG, LEG, TAIL);
	}

	@Override
	public String name() {
		return name;
	}

}
