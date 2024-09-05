package nomadrealms.game.world.map.generation;

import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.Zone;
import nomadrealms.game.world.map.area.coordinate.ChunkCoordinate;

public interface MapGenerationStrategy {

	public Tile[][] generateChunk(Chunk chunk, ChunkCoordinate coord);

	public Chunk[][] generateZone(World world, Zone zone);

}
