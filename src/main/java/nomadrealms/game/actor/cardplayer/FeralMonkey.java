package nomadrealms.game.actor.cardplayer;

import static common.colour.Colour.rgb;
import static java.util.Arrays.asList;
import static nomadrealms.game.actor.cardplayer.appendage.Appendage.ARM;
import static nomadrealms.game.actor.cardplayer.appendage.Appendage.EYE;
import static nomadrealms.game.actor.cardplayer.appendage.Appendage.HEAD;
import static nomadrealms.game.actor.cardplayer.appendage.Appendage.LEG;
import static nomadrealms.game.actor.cardplayer.appendage.Appendage.TAIL;
import static nomadrealms.game.actor.cardplayer.appendage.Appendage.TORSO;
import static nomadrealms.game.card.GameCard.ATTACK;
import static nomadrealms.game.card.GameCard.MEANDER;
import static nomadrealms.game.world.map.area.Tile.TILE_RADIUS;

import java.util.List;
import java.util.stream.Stream;

import common.math.Vector2f;
import nomadrealms.game.actor.ai.FeralMonkeyAI;
import nomadrealms.game.actor.cardplayer.appendage.Appendage;
import nomadrealms.game.card.WorldCard;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;

public class FeralMonkey extends CardPlayer {

	private final String name;

	/**
	 * No-arg constructor for serialization.
	 */
	protected FeralMonkey() {
		this.name = "Farmer";
	}

	public FeralMonkey(String name, Tile tile) {
		this.setAi(new FeralMonkeyAI(this));
		this.name = name;
		this.tile(tile);
		this.health(10);
		this.deckCollection().deck1().addCards(Stream.of(MEANDER).map(WorldCard::new));
		this.deckCollection().deck2().addCards(Stream.of(ATTACK).map(WorldCard::new));
	}

	public void render(RenderingEnvironment re) {
		float scale = 0.6f * TILE_RADIUS;
		DefaultFrameBuffer.instance().render(
				() -> {
					Vector2f screenPosition = tile().getScreenPosition(re);
					re.textureRenderer.render(
							re.imageMap.get("feral_monkey"),
							screenPosition.x() - 0.5f * scale,
							screenPosition.y() - 0.7f * scale,
							scale, scale
					);
					re.textRenderer.render(
							screenPosition.x(),
							screenPosition.y() + 0.1f * scale,
							name + " FERAL MONKEY",
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

	@Override
	public List<Appendage> appendages() {
		return asList(HEAD, EYE, EYE, TORSO, ARM, ARM, LEG, LEG, TAIL);
	}

}
