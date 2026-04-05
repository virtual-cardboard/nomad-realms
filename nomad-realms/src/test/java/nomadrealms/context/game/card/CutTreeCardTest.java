package nomadrealms.context.game.card;

import static nomadrealms.context.game.actor.types.structure.factory.StructureType.TREE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.actor.types.structure.TreeStructure;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.item.Item;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CutTreeCardTest {

	private GameState gameState;
	private Nomad source;
	private TreeStructure tree;
	private Tile treeTile;

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
		treeTile = gameState.world.getTile(new TileCoordinate(chunkCoord, 2, 0));
		source = new Nomad("Source", sourceTile);
		tree = new TreeStructure();
		tree.tile(treeTile);
		gameState.world.addActor(source);
		gameState.world.addActor(tree);
	}

	@Test
	public void testCutTree_targetsTree_destroysItAndSpawnsLogs() {
		// Test condition
		assertTrue(GameCard.CUT_TREE.targetingInfo().conditions().get(0).test(gameState.world, tree, source));
		assertTrue(GameCard.CUT_TREE.targetingInfo().conditions().get(1).test(gameState.world, tree, source));

		List<Effect> effects = GameCard.CUT_TREE.expression().effects(gameState.world, tree, source);
		assertEquals(2, effects.size());

		// Resolve effects
		effects.forEach(effect -> effect.resolve(gameState.world));

		// Manually update world to run actions
		// WalkToAdjacentAction + DestroyStructureAndSpawnItemsAction
		for (int i = 0; i < 200; i++) {
			gameState.update();
		}

		assertTrue(source.tile().coord().distanceTo(treeTile.coord()) <= 1);
		assertTrue(tree.isDestroyed());
		assertEquals(0, tree.health());
		assertEquals(3, treeTile.items().stream().filter(item -> item.item() == Item.OAK_LOG).count());
	}
}
