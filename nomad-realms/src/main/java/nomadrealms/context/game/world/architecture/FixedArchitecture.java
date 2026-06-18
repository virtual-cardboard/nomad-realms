package nomadrealms.context.game.world.architecture;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import engine.serialization.Derializable;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.diff.TileCoordinateDiff;
import nomadrealms.context.game.world.map.tile.factory.TileFactory;
import nomadrealms.context.game.world.map.tile.factory.TileType;

@Derializable
public class FixedArchitecture extends Architecture {

	private Map<TileCoordinateDiff, TileType> tiles = new HashMap<>();
	private Map<TileCoordinateDiff, Actor> actors = new HashMap<>();

	@Override
	public void place(World world, TileCoordinate coord) {
		for (Entry<TileCoordinateDiff, TileType> entry : tiles.entrySet()) {
			TileCoordinate targetCoord = coord.add(entry.getKey());
			Tile oldTile = world.getTile(targetCoord);
			if (oldTile == null) {
				continue;
			}
			Tile newTile = TileFactory.createTile(entry.getValue(), oldTile.chunk(), targetCoord);
			oldTile.copyStateTo(newTile);
			// We clear actor just in case it's replaced by a tile that might not support it or just for cleanliness
			newTile.clearActor();
			world.setTile(newTile);
		}
		for (Entry<TileCoordinateDiff, Actor> entry : actors.entrySet()) {
			TileCoordinate targetCoord = coord.add(entry.getKey());
			Tile tile = world.getTile(targetCoord);
			if (tile == null) {
				continue;
			}
			tile.clearActor();
			tile.actor(entry.getValue());
		}
	}

	public void addTile(TileCoordinateDiff diff, TileType type) {
		tiles.put(diff, type);
	}

	public void addActor(TileCoordinateDiff diff, Actor actor) {
		actors.put(diff, actor);
	}

}
