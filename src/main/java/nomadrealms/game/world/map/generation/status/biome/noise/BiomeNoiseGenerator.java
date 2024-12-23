package nomadrealms.game.world.map.generation.status.biome.noise;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.signum;
import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.game.world.map.area.coordinate.RegionCoordinate.REGION_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.math.generation.map.LayeredNoise;

/**
 * A {@link LayeredNoise} generator for biome noise, with utilities for evaluating the noise at a given coordinate.
 */
public class BiomeNoiseGenerator {

	private long seed;
	private LayeredNoise noise;
	private final float frequency;
	private final float power;

	/**
	 * No-arg constructor for serialization.
	 */
	protected BiomeNoiseGenerator() {
		this(0, null, 0);
	}

	public BiomeNoiseGenerator(long seed, LayeredNoise noise, float frequency) {
		this(seed, noise, frequency, 1);
	}

	public BiomeNoiseGenerator(long seed, LayeredNoise noise, float frequency, float power) {
		this.seed = seed;
		this.noise = noise;
		this.frequency = frequency;
		this.power = power;
	}

	public float eval(TileCoordinate coord) {
		double pointX = (((long) coord.region().x()
				* REGION_SIZE + coord.zone().x())
				* ZONE_SIZE + coord.chunk().x())
				* CHUNK_SIZE + coord.x();
		double pointY = (((long) coord.region().y()
				* REGION_SIZE + coord.zone().y())
				* ZONE_SIZE + coord.chunk().y())
				* CHUNK_SIZE + coord.y()
				+ (double) (coord.y() % 2) / 2; // Offset for hexagonal grid
		double eval = noise.eval(frequency * pointX, frequency * pointY);
		return (float) (pow(abs(eval), power) * signum(eval));
	}

}
