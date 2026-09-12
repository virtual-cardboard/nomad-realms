package nomadrealms.context.game.card.query.tile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;

import engine.common.math.Vector2f;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.TemplateGenerationStrategy;
import nomadrealms.event.game.effect.EffectContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RectangleTileQueryTest {

	private GameState gameState;
	private Nomad source;
	private Tile centerTile;

	@BeforeEach
	public void setUp() {
		gameState = new GameState("Test World", new LinkedList<>(), new TemplateGenerationStrategy());
		ChunkCoordinate chunkCoord = new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0);
		TileCoordinate centerCoord = new TileCoordinate(chunkCoord, 5, 5);
		centerTile = gameState.world.getTile(centerCoord);
		source = new Nomad("Source", centerTile);
		gameState.world.addActor(source, true);
	}

	@Test
	public void testRectangleTileQuery_getters() {
		Vector2f center = new Vector2f(100, 200);
		RectangleTileQuery query = new RectangleTileQuery(center, 10, 50, 60);

		assertEquals(center, query.center());
		assertEquals(10f, query.outlineSize());
		assertEquals(50f, query.width());
		assertEquals(60f, query.height());

		RectangleTileQuery query2 = new RectangleTileQuery(center, 15, new Vector2f(80, 90));
		assertEquals(15f, query2.outlineSize());
		assertEquals(80f, query2.width());
		assertEquals(90f, query2.height());
	}

	@Test
	public void testRectangleTileQuery_smallOutlineExcludesInnerCenter() {
		Vector2f centerPos = centerTile.pos().vector();
		// Large rectangle (300x300) centered at centerPos with small outline (10)
		RectangleTileQuery query = new RectangleTileQuery(centerPos, 10f, 300f, 300f);

		EffectContext context = new EffectContext().world(gameState.world).source(source);
		List<Tile> tiles = query.find(context);

		// Outer tiles should be selected, but the center tile itself (well within the inner cutout) should not be selected
		assertFalse(tiles.contains(centerTile), "Center tile inside the inner cutout should be excluded");
		assertFalse(tiles.isEmpty(), "Outline tiles should be found");
	}

	@Test
	public void testRectangleTileQuery_fullSolidRectangleIncludesCenter() {
		Vector2f centerPos = centerTile.pos().vector();
		// Rectangle where outlineSize (200) >= half dimension (100) -> solid fill
		RectangleTileQuery query = new RectangleTileQuery(centerPos, 200f, 200f, 200f);

		EffectContext context = new EffectContext().world(gameState.world).source(source);
		List<Tile> tiles = query.find(context);

		assertTrue(tiles.contains(centerTile), "Center tile should be included when rectangle outline covers the whole area");
	}

}
