package nomadrealms.game.world.map.generation;

import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.Zone;
import nomadrealms.game.world.map.area.coordinate.ChunkCoordinate;

public class MainWorldGenerationStrategy implements MapGenerationStrategy {

	private long worldSeed;

	public MainWorldGenerationStrategy(long worldSeed) {
		this.worldSeed = worldSeed;
	}

	@Override
	public Tile[][] generateChunk(Chunk chunk, ChunkCoordinate coord) {
		return new TemplateGenerationStrategy().generateChunk(chunk, coord);
	}

	@Override
	public Chunk[][] generateZone(World world, Zone zone) {
		return new TemplateGenerationStrategy().generateZone(world, zone);
	}

}
