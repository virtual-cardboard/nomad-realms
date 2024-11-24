package nomadrealms.game.world.map.generation.status.biome.noise;

import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.game.world.map.area.coordinate.RegionCoordinate.REGION_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.math.generation.map.LayeredNoise;

public class BiomeNoiseGenerator {

	private long seed;
	private LayeredNoise noise;

	public BiomeNoiseGenerator(long seed, LayeredNoise noise) {
		this.seed = seed;
		this.noise = noise;
	}

	public float eval(TileCoordinate coord) {
		double pointX = (((long) coord.region().x()
				* REGION_SIZE + coord.zone().x())
				* ZONE_SIZE + coord.chunk().x())
				* CHUNK_SIZE + coord.x();
		double pointY = (((long) coord.region().y()
				* REGION_SIZE + coord.zone().y())
				* ZONE_SIZE + coord.chunk().y())
				* CHUNK_SIZE + coord.y();
		return (float) noise.eval(pointX, pointY);
	}

}
