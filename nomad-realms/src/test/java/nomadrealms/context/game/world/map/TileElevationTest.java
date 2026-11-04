package nomadrealms.context.game.world.map;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

import engine.common.math.Vector2f;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import nomadrealms.context.game.interaction.InteractionState;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Region;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.overworld.biome.BiomeParameters;
import nomadrealms.context.game.world.map.tile.GrassTile;
import nomadrealms.context.game.world.map.tile.StoneTile;
import nomadrealms.context.game.world.map.tile.WaterTile;
import nomadrealms.render.RenderingEnvironment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TileElevationTest {

	private Chunk chunk;

	@BeforeEach
	void setUp() throws Exception {
		RegionCoordinate regionCoord = new RegionCoordinate(0, 0);
		ZoneCoordinate zoneCoord = new ZoneCoordinate(regionCoord, 0, 0);
		ChunkCoordinate chunkCoord = new ChunkCoordinate(zoneCoord, 0, 0);

		Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
		unsafeField.setAccessible(true);
		Unsafe unsafe = (Unsafe) unsafeField.get(null);

		Region region = (Region) unsafe.allocateInstance(Region.class);
		Field regionCoordField = Region.class.getDeclaredField("coord");
		regionCoordField.setAccessible(true);
		regionCoordField.set(region, regionCoord);

		Zone zone = (Zone) unsafe.allocateInstance(Zone.class);
		Field zoneRegionField = Zone.class.getDeclaredField("region");
		zoneRegionField.setAccessible(true);
		zoneRegionField.set(zone, region);

		Field zoneCoordField = Zone.class.getDeclaredField("coord");
		zoneCoordField.setAccessible(true);
		zoneCoordField.set(zone, zoneCoord);

		chunk = new Chunk(zone, chunkCoord);
	}

	@Test
	void testBiomeParametersElevation() {
		BiomeParameters params = new BiomeParameters(0f, 0f, 0.4f, 0f, 0f, 0.2f, 0f);
		assertEquals(0.5f, params.elevation(), 0.0001f);
	}

	@Test
	void testTileDefaultAndCustomElevation() {
		TileCoordinate tileCoord = new TileCoordinate(chunk.coord(), 0, 0);
		Tile grassTile = new GrassTile(chunk, tileCoord);
		assertEquals(0.0f, grassTile.elevation(), 0.0001f);

		Tile waterTile = new WaterTile(chunk, tileCoord);
		assertEquals(-0.3f, waterTile.elevation(), 0.0001f);

		Tile stoneTile = new StoneTile(chunk, tileCoord);
		assertEquals(0.5f, stoneTile.elevation(), 0.0001f);

		grassTile.elevation(1.2f);
		assertEquals(1.2f, grassTile.elevation(), 0.0001f);
	}

	@Test
	void testInteractionStateElevationToggle() {
		ConstraintBox dummyScreen = new ConstraintBox(absolute(0), absolute(0), absolute(800), absolute(600));
		InteractionState is = new InteractionState(null, dummyScreen);

		assertFalse(is.renderElevation, "renderElevation should default to false");
		is.renderElevation = true;
		assertTrue(is.renderElevation, "renderElevation should be true after setting");
	}

	@Test
	void testTileGetScreenPositionWithElevation() throws Exception {
		ConstraintBox dummyScreen = new ConstraintBox(absolute(0), absolute(0), absolute(800), absolute(600));
		InteractionState is = new InteractionState(null, dummyScreen);
		is.camera.position(new Vector2f(0, 0));
		is.camera.zoom(1.0f);

		TileCoordinate tileCoord = new TileCoordinate(chunk.coord(), 0, 0);
		Tile tile = new GrassTile(chunk, tileCoord);
		tile.elevation(0.5f);

		RenderingEnvironment dummyRe = createDummyRenderingEnvironment(is);

		// With elevation rendering disabled
		is.renderElevation = false;
		ConstraintPair posNormal = tile.getScreenPosition(dummyRe);

		// With elevation rendering enabled
		is.renderElevation = true;
		ConstraintPair posElevated = tile.getScreenPosition(dummyRe);

		float expectedYOffset = 0.5f * 20f * 1.0f; // elevation * 20 * zoom
		assertEquals(posNormal.x().get(), posElevated.x().get(), 0.0001f);
		assertEquals(posNormal.y().get() - expectedYOffset, posElevated.y().get(), 0.0001f);
	}

	private RenderingEnvironment createDummyRenderingEnvironment(InteractionState is) throws Exception {
		Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
		unsafeField.setAccessible(true);
		Unsafe unsafe = (Unsafe) unsafeField.get(null);

		RenderingEnvironment re = (RenderingEnvironment) unsafe.allocateInstance(RenderingEnvironment.class);
		re.is = is;
		return re;
	}

}
