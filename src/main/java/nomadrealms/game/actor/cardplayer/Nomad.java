package nomadrealms.game.actor.cardplayer;

import static common.colour.Colour.rgb;
import static java.util.Arrays.stream;
import static java.util.Collections.singletonList;
import static nomadrealms.game.card.GameCard.*;
import static nomadrealms.game.world.map.area.Tile.TILE_RADIUS;

import java.util.List;

import common.math.Vector2f;
import nomadrealms.game.card.WorldCard;
import nomadrealms.game.card.action.Action;
import nomadrealms.game.card.action.MoveAction;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.game.zone.Deck;
import nomadrealms.render.RenderingEnvironment;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;

public class Nomad extends CardPlayer {

	private final String name;

	/**
	 * No-arg constructor for serialization.
	 */
	protected Nomad() {
		this.name = "Nomad";
	}

	public Nomad(String name, Tile tile) {
		this.name = name;
		this.tile(tile);
		this.health(10);
		stream(this.deckCollection().decks()).forEach(this::initializeDeck);
	}

	@Override
	public List<Action> actions() {
		return singletonList(new MoveAction(this,
				new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0),
						10, 15)));
	}

	private void initializeDeck(Deck deck) {
		deck
				.addCard(new WorldCard(MOVE))
				.addCard(new WorldCard(HEAL))
				.addCard(new WorldCard(ELECTROSTATIC_ZAPPER))
				.addCard(new WorldCard(ATTACK))
				.addCard(new WorldCard(GATHER));
		deck.shuffle();
	}

	@Override
	public void render(RenderingEnvironment re) {
		float scale = 0.6f * TILE_RADIUS;
		DefaultFrameBuffer.instance().render(
				() -> {
					Vector2f screenPosition = tile().getScreenPosition(re);
					re.textureRenderer.render(
							re.imageMap.get("nomad"),
							screenPosition.x() - 0.5f * scale,
							screenPosition.y() - 0.7f * scale,
							scale, scale
					);
					re.textRenderer.render(
							screenPosition.x(),
							screenPosition.y() + 0.1f * scale,
							name,
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

}
