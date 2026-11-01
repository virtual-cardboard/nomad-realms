package nomadrealms.context.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;

import nomadrealms.context.game.event.InputEventFrame;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.DefaultMapInitialization;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameStateDerializerTest {

	private GameState gameState;

	@BeforeEach
	public void setUp() {
		gameState = new GameState();
	}

	@Test
	public void testSerializeAndDeserialize() {
		// Given a game state with non-default values
		gameState.frameNumber = 123L;
		gameState.update(new InputEventFrame(gameState.frameNumber)); // This increments frameNumber and adds an item to inputFrames list

		// When serializing and deserializing
		byte[] bytes = GameStateDerializer.serialize(gameState);
		GameState loadedGameState = GameStateDerializer.deserialize(bytes);

		// Then all serializable fields should be equal
		assertNotNull(loadedGameState);
		assertEquals(gameState.frameNumber, loadedGameState.frameNumber);

		// The uiEventChannel list serializes its size but its elements are intentionally not persisted:
		// InputEvent implementations reference live engine objects (mouse, network connections, frames).
		// World content equality assertions come with the world persistence work.
	}

	@Test
	public void testWeatherRoundTrip() {
		// Given a game state whose weather color stops are in place
		gameState.frameNumber = 123L;

		// When serializing and deserializing
		byte[] bytes = GameStateDerializer.serialize(gameState);
		GameState loadedGameState = GameStateDerializer.deserialize(bytes);

		// Then the sky color should be identical at sample points across the day-night cycle
		assertNotNull(loadedGameState);
		for (long time : new long[] {0L, 200L, 900L, 1700L, 2200L}) {
			assertEquals(gameState.weather.skyColor(time), loadedGameState.weather.skyColor(time),
					"sky color should round trip at time " + time);
		}
	}

	@Test
	public void testCloudsRoundTrip() {
		// Given a game state whose clouds have drifted away from their default position
		gameState.frameNumber = 0L;
		gameState.update(new InputEventFrame(gameState.frameNumber));
		gameState.update(new InputEventFrame(gameState.frameNumber));

		// When serializing and deserializing
		byte[] bytes = GameStateDerializer.serialize(gameState);
		GameState loadedGameState = GameStateDerializer.deserialize(bytes);

		// Then the cloud drift should round trip
		assertNotNull(loadedGameState);
		assertEquals(gameState.clouds.driftX(), loadedGameState.clouds.driftX(), 0.0001f);
		assertEquals(gameState.clouds.driftY(), loadedGameState.clouds.driftY(), 0.0001f);
	}

	@Test
	public void testWorldMapGraphRoundTrip() {
		// Given a fully materialized region of tiles in the source world
		GameState source = new GameState("World Persistence Probe", new LinkedList<>(),
				new OverworldGenerationStrategy(123456789).mapInitialization(new DefaultMapInitialization()));
		RegionCoordinate regionCoord = new RegionCoordinate(0, 0);
		ChunkCoordinate chunkCoord = new ChunkCoordinate(new ZoneCoordinate(regionCoord, 0, 0), 0, 0);
		Chunk sourceChunk = source.world.getChunk(chunkCoord);
		assertNotNull(sourceChunk);
		Tile[] sourceTiles = sourceChunk.tiles().toArray(new Tile[0]);
		assertTrue(sourceTiles.length > 0, "the generated chunk should contain tiles");

		// When serializing and deserializing the whole game state
		byte[] bytes = GameStateDerializer.serialize(source);
		GameState loaded = GameStateDerializer.deserialize(bytes);

		// Then the chunk and every tile resolve through the restored map graph
		Chunk loadedChunk = loaded.world.getChunk(chunkCoord);
		assertSame(loadedChunk, chunkAtFactory(loaded.world, chunkCoord), "chunks must alias through the map graph");
		for (Tile sourceTile : sourceTiles) {
			TileCoordinate coord = sourceTile.coord();
			Tile loadedTile = loaded.world.getTile(coord);
			assertNotNull(loadedTile, "tile should persist for " + coord);
			assertSame(sourceTile.type(), loadedTile.type(), "tile type should round trip at " + coord);
		}

		// And the deserialization has restored the generation strategy, so reindex works end to end
		loaded.reindex(new LinkedList<>());
		assertEquals(source.world.generation().parameters().seed(), loaded.world.generation().parameters().seed(),
				"strategy seed should restore with the world");
		Tile afterReindex = loaded.world.getTile(sourceTiles[0].coord());
		assertSame(sourceTiles[0].type(), afterReindex.type(), "tile type should survive reindexing");

		// And sizes round trip: every serialized region, zone, and chunk aspect persists
		assertEquals(sourceTiles.length, loadedChunk.tiles().size(), "tile count should round trip");
	}

	private Chunk chunkAtFactory(World world, ChunkCoordinate coord) {
		return world.getRegion(coord.region()).getZoneObject(coord.zone()).chunks()[coord.x()][coord.y()];
	}

}
