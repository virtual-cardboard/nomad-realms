package nomadrealms.context.game.actor.cardplayer;

import static engine.common.colour.Colour.rgb;
import static java.util.Arrays.asList;
import static nomadrealms.context.game.actor.cardplayer.appendage.Appendage.ARM;
import static nomadrealms.context.game.actor.cardplayer.appendage.Appendage.EYE;
import static nomadrealms.context.game.actor.cardplayer.appendage.Appendage.HEAD;
import static nomadrealms.context.game.actor.cardplayer.appendage.Appendage.LEG;
import static nomadrealms.context.game.actor.cardplayer.appendage.Appendage.TORSO;
import static nomadrealms.context.game.card.GameCard.HEAL;
import static nomadrealms.context.game.card.GameCard.MOVE;
import static nomadrealms.context.game.card.GameCard.TILL_SOIL;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

import java.util.List;
import java.util.stream.Stream;

import engine.common.math.Vector2f;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.cardplayer.appendage.Appendage;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.world.map.area.Tile;
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
					re.textureRenderer.render(
							re.imageMap.get("farmer"),
							box(re)
					);
					re.textRenderer.render(
							box(re).center().x().get(),
							box(re).y().get() + box(re).h().get() * 0.8f,
							name + " FARMER",
							0,
							re.font,
							0.5f * scale,
							rgb(255, 255, 255)
					);
					re.textRenderer.render(
							box(re).center().x().get(),
							box(re).y().get() + box(re).h().get() * 1.2f,
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
