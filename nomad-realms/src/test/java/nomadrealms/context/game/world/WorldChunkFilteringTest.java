package nomadrealms.context.game.world;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import engine.visuals.constraint.box.ConstraintBox;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.interaction.InteractionState;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import nomadrealms.render.RenderingEnvironment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorldChunkFilteringTest {

	private World world;
	private InteractionState interactionState;
	private DummyRenderingEnvironment dummyRe;

	// Minimal subclass to provide interactionState without initializing OpenGL resources
	private static class DummyRenderingEnvironment extends RenderingEnvironment {
		public DummyRenderingEnvironment(InteractionState is) {
			this.is = is;
		}
	}

	@BeforeEach
	void setUp() {
		GameState state = new GameState("Test World", new LinkedList<>(), new OverworldGenerationStrategy(123456789)
				.mapInitialization(new nomadrealms.context.game.world.map.generation.DefaultMapInitialization()));
		this.world = state.world;
		this.interactionState = new InteractionState(null, new ConstraintBox(
				absolute(0),
				absolute(0),
				absolute(100),
				absolute(100)
		));
		this.dummyRe = new DummyRenderingEnvironment(interactionState);
	}

	@Test
	void testFilterChunksToRenderWhenLoadAllChunksIsFalse() {
		interactionState.loadAllChunks = false;

		// Nomad is located in zone (0, 0, 0, 0)
		ZoneCoordinate centerZone = world.nomad.tile().coord().chunk().zone();

		// Create chunks in center zone, 1 zone away, and 2 zones away
		Chunk centerChunk = world.getChunk(new ChunkCoordinate(centerZone, 0, 0));
		Chunk neighborZoneChunk = world.getChunk(new ChunkCoordinate(centerZone.add(1, 1), 0, 0));
		Chunk farZoneChunk = world.getChunk(new ChunkCoordinate(centerZone.add(2, 0), 0, 0));

		List<Chunk> visibleChunks = new ArrayList<>();
		visibleChunks.add(centerChunk);
		visibleChunks.add(neighborZoneChunk);
		visibleChunks.add(farZoneChunk);

		List<Chunk> filtered = world.filterChunksToRender(dummyRe, visibleChunks);

		assertEquals(2, filtered.size());
		assertTrue(filtered.contains(centerChunk));
		assertTrue(filtered.contains(neighborZoneChunk));
		assertFalse(filtered.contains(farZoneChunk));
	}

	@Test
	void testFilterChunksToRenderWhenLoadAllChunksIsTrue() {
		interactionState.loadAllChunks = true;

		ZoneCoordinate centerZone = world.nomad.tile().coord().chunk().zone();

		Chunk centerChunk = world.getChunk(new ChunkCoordinate(centerZone, 0, 0));
		Chunk farZoneChunk = world.getChunk(new ChunkCoordinate(centerZone.add(2, 0), 0, 0));

		List<Chunk> visibleChunks = new ArrayList<>();
		visibleChunks.add(centerChunk);
		visibleChunks.add(farZoneChunk);

		List<Chunk> filtered = world.filterChunksToRender(dummyRe, visibleChunks);

		assertEquals(2, filtered.size());
		assertTrue(filtered.contains(farZoneChunk));
	}
}
