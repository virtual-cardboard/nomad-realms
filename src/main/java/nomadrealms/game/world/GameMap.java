package nomadrealms.game.world;

import java.util.*;

import common.math.Vector2f;
import nomadrealms.game.world.map.area.Region;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
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

	public List<Tile> path(Tile source, Tile target) {
		if (source.equals(target)) {
			return Collections.singletonList(source);
		}

		Map<Tile, Tile> cameFrom = new HashMap<>();
		Queue<Tile> frontier = new LinkedList<>();
		frontier.add(source);

		while (!frontier.isEmpty()) {
			Tile current = frontier.poll();

			for (Tile next : getNeighbors(current)) {
				if (!cameFrom.containsKey(next)) {
					frontier.add(next);
					cameFrom.put(next, current);

					if (next.equals(target)) {
						return reconstructPath(cameFrom, source, target);
					}
				}
			}
		}

		return Collections.emptyList();
	}

	private List<Tile> getNeighbors(Tile tile) {
		List<Tile> neighbors = new ArrayList<>();
		neighbors.add(tile.ul(world));
		neighbors.add(tile.um(world));
		neighbors.add(tile.ur(world));
		neighbors.add(tile.dl(world));
		neighbors.add(tile.dm(world));
		neighbors.add(tile.dr(world));
		return neighbors;
	}

	private List<Tile> reconstructPath(Map<Tile, Tile> cameFrom, Tile start, Tile goal) {
		List<Tile> path = new ArrayList<>();
		Tile current = goal;
		while (!current.equals(start)) {
			path.add(current);
			current = cameFrom.get(current);
		}
		path.add(start);
		Collections.reverse(path);
		return path;
	}
}
