package nomadrealms.game.world.map.generation.status.biome;

import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.game.world.map.area.coordinate.RegionCoordinate.REGION_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.math.generation.map.LayeredNoise;
import nomadrealms.math.generation.map.NoiseOctave;
import nomadrealms.math.generation.map.OpenSimplexNoise;

public class NoiseGenerator {

	private long seed;
	private LayeredNoise noise;

	public NoiseGenerator(long seed) {
		this.seed = seed;
		OpenSimplexNoise base = new OpenSimplexNoise(seed);
		this.noise = new LayeredNoise(
				new NoiseOctave(base, 0.05, 0.4, 0),
				new NoiseOctave(base, 2, 0.02, 1),
				new NoiseOctave(base, 1, 0.3, 2),
				new NoiseOctave(base, 0.5, 0.18, 3),
				new NoiseOctave(base, 0.25, 0.1, 4),
				new NoiseOctave(base, 0.1, 0.05, 5),
				new NoiseOctave(base, 0.05, 0.02, 6));
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
