package nomadrealms.game.actor.cardplayer;

import static common.colour.Colour.rgb;
import static nomadrealms.game.card.GameCard.HEAL;
import static nomadrealms.game.card.GameCard.MOVE;
import static nomadrealms.game.card.GameCard.TILL_SOIL;
import static nomadrealms.game.world.map.tile.Tile.SCALE;

import java.util.stream.Stream;

import common.math.Vector2f;
import nomadrealms.game.GameState;
import nomadrealms.game.actor.ai.StupidAI;
import nomadrealms.game.card.WorldCard;
import nomadrealms.game.world.map.tile.Tile;
import nomadrealms.render.RenderingEnvironment;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;

public class Bub extends CardPlayer {

	private final String name;

	public Bub(String name, Tile tile) {
		this.name = name;
		this.tile(tile);
		this.health(10);
		this.deckCollection().deck1().addCards(Stream.of(MOVE, HEAL, TILL_SOIL).map(WorldCard::new));
		this.setAi(new StupidAI());
		assert ai() != null;
	}

	@Override
	public void render(RenderingEnvironment re) {
		float scale = 0.6f * SCALE;
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
							name + " BUB12",
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
	public void update(GameState state) {
		super.update(state);
		assert ai() != null;
		ai().update(this, state);
	}

}
