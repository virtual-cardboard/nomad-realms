package nomadrealms.context.game.card;

import static nomadrealms.context.game.world.map.tile.factory.TileType.SOIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.LinkedList;
import java.util.List;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.item.Item;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TillSoilCardTest {

	private GameState gameState;
	private Nomad source;
	private Tile targetTile;

	@BeforeEach
	public void setUp() {
		gameState = new GameState("Test World", new LinkedList<>(), new OverworldGenerationStrategy(123));
		ChunkCoordinate chunkCoord = new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0);
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				gameState.world.getTile(new TileCoordinate(chunkCoord, x, y)).clearActor();
			}
		}
		Tile sourceTile = gameState.world.getTile(new TileCoordinate(chunkCoord, 0, 0));
		targetTile = gameState.world.getTile(new TileCoordinate(chunkCoord, 1, 0));
		source = new Nomad("Source", sourceTile);
		gameState.world.addActor(source);
	}

	@Test
	public void testTillSoil_preservesActorAndItems() {
		// Place an actor on target tile
		Nomad otherNomad = new Nomad("Other", targetTile);
		gameState.world.addActor(otherNomad);

		// Place an item on target tile
		WorldItem item = new WorldItem(Item.OAK_LOG);
		targetTile.addItem(item);

		// Till soil
		List<Effect> effects = GameCard.TILL_SOIL.expression().effects(gameState.world, targetTile, source);
		assertEquals(1, effects.size());
		effects.get(0).resolve(gameState.world);

		Tile newTile = gameState.world.getTile(targetTile.coord());
		assertEquals(SOIL, newTile.type());

		// Check if actor is still there
		assertNotNull(newTile.actor(), "Actor should be preserved on the new tile");
		assertEquals(otherNomad, newTile.actor());
		assertEquals(newTile, otherNomad.tile(), "Actor's tile reference should be updated to the new tile");

		// Check if item is still there
		assertEquals(1, newTile.items().size(), "Item should be preserved on the new tile");
		assertEquals(item, newTile.items().get(0));
		assertEquals(newTile, item.tile(), "Item's tile reference should be updated to the new tile");

		// Place a buried item on target tile
		WorldItem buriedItem = new WorldItem(Item.OAK_LOG);
		newTile.buryItem(buriedItem);

		// Till again to see if buried item is preserved
		effects = GameCard.TILL_SOIL.expression().effects(gameState.world, newTile, source);
		effects.get(0).resolve(gameState.world);

		Tile evenNewerTile = gameState.world.getTile(newTile.coord());
		assertEquals(buriedItem, evenNewerTile.buried(), "Buried item should be preserved");
	}
}
