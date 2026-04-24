package nomadrealms.context.game.actor.types.cardplayer;
import nomadrealms.context.game.interaction.InteractionState;

import static engine.common.colour.Colour.rgba;
import static engine.visuals.rendering.text.HorizontalAlign.CENTER;
import static engine.visuals.rendering.text.TextFormat.textFormat;
import static engine.visuals.rendering.text.VerticalAlign.TOP;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.ARM;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.EYE;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.HEAD;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.LEG;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.TORSO;
import static nomadrealms.context.game.card.GameCard.HEAL;
import static nomadrealms.context.game.card.GameCard.MOVE;
import static nomadrealms.context.game.card.GameCard.TILL_SOIL;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

import static java.util.Arrays.asList;

import engine.common.math.Vector2f;
import java.util.List;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.collection.DeckList;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;

public class Farmer extends CardPlayer {

	private final String name;

	/**
	 * No-arg constructor for serialization.
	 */
	protected Farmer() {
		this.name = "Farmer";
	}

	public Farmer(String name, Tile tile) {
		this.name = name;
		this.tile(tile);
		this.health(10);
		DeckList list = new DeckList(MOVE, HEAL, TILL_SOIL);
		this.deckCollection().deck1().addCards(list.toDeck().getCards());
	}

	public void render(RenderingEnvironment re, InteractionState interactionState) {
		float scale = 0.6f * TILE_RADIUS * interactionState.camera.zoom().get();
		Vector2f screenPosition = getScreenPosition(re, interactionState).vector();
		re.textureRenderer.render(
				re.imageMap.get("farmer"),
				screenPosition.x() - 0.5f * scale,
				screenPosition.y() - 0.7f * scale,
				scale, scale
		);
		re.textRenderer.render(
				screenPosition.x(),
				screenPosition.y() + 0.1f * scale,
				textFormat()
						.text(name + " FARMER")
						.font(re.font)
						.fontSize(0.5f * scale)
						.colour(rgba(255, 255, 255, (int) (interactionState.actorTextOpacity * 255)))
						.hAlign(CENTER)
						.vAlign(TOP)
		);
		re.textRenderer.render(
				screenPosition.x(),
				screenPosition.y() + 0.5f * scale,
				textFormat()
						.text(health() + " HP")
						.font(re.font)
						.fontSize(0.5f * scale)
						.colour(rgba(255, 255, 255, (int) (interactionState.actorTextOpacity * 255)))
						.hAlign(CENTER)
						.vAlign(TOP)
		);
		super.render(re, interactionState);
	}

	private int thinkingTime = 10;

	@Override
	public void update(GameState state) {
		super.update(state);
		if (thinkingTime > 0) {
			thinkingTime--;
			return;
		}
		thinkingTime = (int) (Math.random() * 20) + 15;
		WorldCard cardToPlay = deckCollection().deck1().peek();
		switch (cardToPlay.card().targetingInfo().targetType()) {
			case HEXAGON:
				// TODO figure out which chunk the next tile is on
				addNextPlay(new CardPlayedEvent(cardToPlay, this,
						tile().dr(state.world)));
				break;
			case NONE:
				addNextPlay(new CardPlayedEvent(cardToPlay, this, null));
				break;
			case CARD_PLAYER:
				addNextPlay(new CardPlayedEvent(cardToPlay, this, this));
				break;
		}
	}

	@Override
	public List<Appendage> appendages() {
		return asList(HEAD, EYE, EYE, TORSO, ARM, ARM, LEG, LEG);
	}

	@Override
	public String name() {
		return name;
	}

}
