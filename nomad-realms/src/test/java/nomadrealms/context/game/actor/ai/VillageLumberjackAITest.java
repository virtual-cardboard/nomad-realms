package nomadrealms.context.game.actor.ai;

import static nomadrealms.context.game.item.Item.OAK_LOG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;

import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.VillageLumberjack;
import nomadrealms.context.game.actor.types.structure.TreeStructure;
import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.card.query.tile.TilesInRadiusQuery;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VillageLumberjackAITest {

	private GameState gameState;
	private VillageLumberjack lumberjack;
	private ChunkCoordinate chunkCoord;

	@BeforeEach
	public void setUp() {
		gameState = new GameState("Test World", new LinkedList<>(), new OverworldGenerationStrategy(123));
		chunkCoord = new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0);
		Tile lumberjackTile = gameState.world.getTile(new TileCoordinate(chunkCoord, 5, 5));
		lumberjack = new VillageLumberjack("Jack", lumberjackTile);
		List<Tile> tilesToClear = new TilesInRadiusQuery(10).find(gameState.world, lumberjack, lumberjack);
		for (Tile tile : tilesToClear) {
			tile.clearActor();
		}
		gameState.world.addActor(lumberjack);
	}

	@Test
	public void testAI_nothingNearby_meanders() {
		lumberjack.ai().update(gameState);
		assertEquals(1, lumberjack.retrieveNextPlays().size());
		InputEvent event = lumberjack.lastPlays().get(0);
		assertTrue(event instanceof CardPlayedEvent);
		assertEquals(GameCard.MEANDER, ((CardPlayedEvent) event).card().card());
	}

	@Test
	public void testAI_treeNearby_cutsTree() {
		Tile treeTile = gameState.world.getTile(new TileCoordinate(chunkCoord, 5, 7));
		treeTile.clearActor(); // Make sure it's clear
		TreeStructure tree = new TreeStructure();
		tree.tile(treeTile);
		gameState.world.addActor(tree);

		lumberjack.ai().update(gameState);
		assertEquals(1, lumberjack.retrieveNextPlays().size());
		InputEvent event = lumberjack.lastPlays().get(0);
		assertTrue(event instanceof CardPlayedEvent);
		assertEquals(GameCard.CUT_TREE, ((CardPlayedEvent) event).card().card());
		assertEquals(tree, ((CardPlayedEvent) event).target());
	}

	@Test
	public void testAI_logOnTile_gathers() {
		lumberjack.tile().addItem(new WorldItem(OAK_LOG));

		lumberjack.ai().update(gameState);
		assertEquals(1, lumberjack.retrieveNextPlays().size());
		InputEvent event = lumberjack.lastPlays().get(0);
		assertTrue(event instanceof CardPlayedEvent);
		assertEquals(GameCard.GATHER, ((CardPlayedEvent) event).card().card());
	}

	@Test
	public void testAI_logNearby_movesToLog() {
		Tile logTile = gameState.world.getTile(new TileCoordinate(chunkCoord, 5, 6));
		logTile.addItem(new WorldItem(OAK_LOG));

		lumberjack.ai().update(gameState);
		assertEquals(1, lumberjack.retrieveNextPlays().size());
		InputEvent event = lumberjack.lastPlays().get(0);
		assertTrue(event instanceof CardPlayedEvent);
		assertEquals(GameCard.MOVE, ((CardPlayedEvent) event).card().card());
		assertEquals(logTile, ((CardPlayedEvent) event).target());
	}
}
