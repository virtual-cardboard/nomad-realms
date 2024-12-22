package nomadrealms.game.world;

import java.util.HashMap;
import java.util.Map;

import common.math.Vector2f;
import nomadrealms.game.world.map.area.Region;
import nomadrealms.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.render.RenderingEnvironment;

public class GameMap {

	private final World world;
	private final Map<RegionCoordinate, Region> regions = new HashMap<>();
	private final MapGenerationStrategy strategy;

	/**
	 * No-arg constructor for serialization.
	 */
	protected GameMap() {
		this(null, null);
	}

	public GameMap(World world, MapGenerationStrategy strategy) {
		this.world = world;
		this.strategy = strategy;
	}

	public void render(RenderingEnvironment re, Vector2f origin) {
		RegionCoordinate regionCoord = RegionCoordinate.regionCoordinateOf(origin);
		getRegion(regionCoord).render(re, origin);
	}

	public Region getRegion(RegionCoordinate coord) {
		return regions.computeIfAbsent(coord, c -> new Region(strategy, world, c));
	}

	public Iterable<Region> regions() {
		return regions.values();
	}

}
