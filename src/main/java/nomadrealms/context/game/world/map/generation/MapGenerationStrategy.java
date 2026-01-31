package nomadrealms.context.game.world.map.generation;

import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;

public interface MapGenerationStrategy {

	public MapGenerationParameters parameters();

	public Tile[][] generateChunk(Zone zone, Chunk chunk, ChunkCoordinate coord);

	public Chunk[][] generateZone(World world, Zone zone);

	void initialize(World world, MapInitialization mapInitialization);

}
