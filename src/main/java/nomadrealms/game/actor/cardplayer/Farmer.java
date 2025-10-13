package nomadrealms.game.actor.cardplayer;

import static engine.common.colour.Colour.rgb;
import static java.util.Arrays.asList;
import static nomadrealms.game.actor.cardplayer.appendage.Appendage.ARM;
import static nomadrealms.game.actor.cardplayer.appendage.Appendage.EYE;
import static nomadrealms.game.actor.cardplayer.appendage.Appendage.HEAD;
import static nomadrealms.game.actor.cardplayer.appendage.Appendage.LEG;
import static nomadrealms.game.actor.cardplayer.appendage.Appendage.TORSO;
import static nomadrealms.game.card.GameCard.HEAL;
import static nomadrealms.game.card.GameCard.MOVE;
import static nomadrealms.game.card.GameCard.TILL_SOIL;
import static nomadrealms.game.world.map.area.Tile.TILE_RADIUS;

import java.util.List;
import java.util.stream.Stream;

import engine.common.math.Vector2f;
import nomadrealms.game.GameState;
import nomadrealms.game.actor.cardplayer.appendage.Appendage;
import nomadrealms.game.card.WorldCard;
import nomadrealms.game.event.CardPlayedEvent;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;
import engine.visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;

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
		this.deckCollection().deck1().addCards(Stream.of(MOVE, HEAL, TILL_SOIL).map(WorldCard::new));
	}

	public void render(RenderingEnvironment re) {
		float scale = 0.6f * TILE_RADIUS;
		DefaultFrameBuffer.instance().render(
				() -> {
					Vector2f screenPosition = tile().getScreenPosition(re);
					re.textureRenderer.render(
							re.imageMap.get("farmer"),
							screenPosition.x() - 0.5f * scale,
							screenPosition.y() - 0.7f * scale,
							scale, scale
					);
					re.textRenderer.render(
							screenPosition.x(),
							screenPosition.y() + 0.1f * scale,
							name + " FARMER",
							0,
							re.font,
							0.5f * scale,
							rgb(255, 255, 255)
					);
					re.textRenderer.render(
							screenPosition.x(),
							screenPosition.y() + 0.5f * scale,
							health() + " HP",
							0,
							re.font,
							0.5f * scale,
							rgb(255, 255, 255)
					);
				}
		);
	}

	private int thinkingTime = 10;

	@Override
	public void update(GameState state) {
		if (thinkingTime > 0) {
			thinkingTime--;
			return;
		}
		thinkingTime = (int) (Math.random() * 20) + 4;
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

}
