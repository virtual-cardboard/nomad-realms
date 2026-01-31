package nomadrealms.context.game.world.map.generation;

import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;

public abstract class MapGenerationStrategy {

	public abstract MapGenerationParameters parameters();

	public abstract Tile[][] generateChunk(Zone zone, Chunk chunk, ChunkCoordinate coord);

	public abstract Chunk[][] generateZone(World world, Zone zone);

	private MapInitialization mapInitialization;

	public MapGenerationStrategy mapInitialization(MapInitialization mapInitialization) {
		this.mapInitialization = mapInitialization;
		return this;
	}

	public void initializeMap(World world) {
		if (mapInitialization != null) {
			mapInitialization.initialize(world);
		}
	}

}
