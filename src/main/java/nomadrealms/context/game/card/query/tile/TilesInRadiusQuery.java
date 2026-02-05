package nomadrealms.context.game.card.query.tile;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class TilesInRadiusQuery implements Query<Tile> {

	private final Query<Tile> centerQuery;
	private final int radius;

	public TilesInRadiusQuery(int radius) {
		this(new SourceTileQuery(), radius);
	}

	public TilesInRadiusQuery(Query<Tile> centerQuery, int radius) {
		this.centerQuery = centerQuery;
		this.radius = radius;
	}

	@Override
	public List<Tile> find(World world, Actor source, Target target) {
		List<Tile> centers = centerQuery.find(world, source, target);
		if (centers.isEmpty()) {
			return emptyList();
		}
		Tile startTile = centers.get(0);

		List<Tile> tiles = new ArrayList<>();
		Queue<Tile> queue = new LinkedList<>();
		Map<Tile, Integer> distance = new HashMap<>();

		queue.add(startTile);
		distance.put(startTile, 0);

		while (!queue.isEmpty()) {
			Tile currentTile = queue.poll();
			tiles.add(currentTile);

			int currentDistance = distance.get(currentTile);
			if (currentDistance < radius) {
				for (Tile neighbor : getNeighbors(world, currentTile)) {
					if (neighbor != null && !distance.containsKey(neighbor)) {
						distance.put(neighbor, currentDistance + 1);
						queue.add(neighbor);
					}
				}
			}
		}
		return tiles;
	}

	private List<Tile> getNeighbors(World world, Tile tile) {
		List<Tile> neighbors = new ArrayList<>();
		neighbors.add(tile.ul(world));
		neighbors.add(tile.um(world));
		neighbors.add(tile.ur(world));
		neighbors.add(tile.dl(world));
		neighbors.add(tile.dm(world));
		neighbors.add(tile.dr(world));
		return neighbors;
	}

}
