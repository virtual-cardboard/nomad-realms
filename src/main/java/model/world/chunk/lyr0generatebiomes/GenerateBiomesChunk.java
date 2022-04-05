package model.world.chunk.lyr0generatebiomes;

import static model.world.Biome.*;

import common.math.Vector2i;
import graphics.noise.OpenSimplexNoise;
import model.world.Biome;
import model.world.WorldSeed;
import model.world.chunk.AbstractTileChunk;
import model.world.chunk.lyr1randompoints.GeneratePointsChunk;

public class GenerateBiomesChunk extends AbstractTileChunk {

	private static final int NUM_OCTAVES = 6;
	private static final double OCTAVE_AMPLITUDE_FACTOR = 2.0;
	private static final double OCTAVE_IMPACT_FACTOR = 1.8;
	private static final double MOISTURE_SCALE = 600;
	private static final double ELEVATION_SCALE = 200;

	protected Biome[][] biomes;
	protected double[][] moisture;
	protected double[][] elevation;

	public GenerateBiomesChunk(Vector2i pos) {
		super(pos);
	}

	public static GenerateBiomesChunk create(Vector2i pos, long worldSeed) {
		GenerateBiomesChunk c = new GenerateBiomesChunk(pos);

		OpenSimplexNoise[] moistureOctaves = new OpenSimplexNoise[NUM_OCTAVES];
		OpenSimplexNoise[] elevationOctaves = new OpenSimplexNoise[NUM_OCTAVES];
		for (int i = 0; i < NUM_OCTAVES; i++) {
			moistureOctaves[i] = new OpenSimplexNoise(WorldSeed.moisture(worldSeed) + i);
			elevationOctaves[i] = new OpenSimplexNoise(WorldSeed.elevation(worldSeed) + i);
		}

		c.biomes = new Biome[CHUNK_SIDE_LENGTH][CHUNK_SIDE_LENGTH];
		c.moisture = new double[CHUNK_SIDE_LENGTH][CHUNK_SIDE_LENGTH];
		c.elevation = new double[CHUNK_SIDE_LENGTH][CHUNK_SIDE_LENGTH];
		for (int y = 0; y < CHUNK_SIDE_LENGTH; y++) {
			for (int x = 0; x < CHUNK_SIDE_LENGTH; x++) {

				double moisture = generate(moistureOctaves, pos, x, y, MOISTURE_SCALE);
				double elevation = generate(elevationOctaves, pos, x, y, ELEVATION_SCALE);

				Biome biomeType = generateBiome(moisture, elevation);
				c.biomes[y][x] = biomeType;
				c.moisture[y][x] = moisture;
				c.elevation[y][x] = elevation;
			}

		}
		return c;
	}

	private static double generate(OpenSimplexNoise[] octaves, Vector2i chunkPos, int x, int y, double scale) {
		int cx = chunkPos.x;
		int cy = chunkPos.y;
		double n = 0;
		for (int i = 0; i < NUM_OCTAVES; i++) {
			double pow = Math.pow(OCTAVE_AMPLITUDE_FACTOR, i);
			double ex = pow / scale * (x + cx * CHUNK_SIDE_LENGTH);
			double ey = pow / scale * (y + (x % 2) * 0.5 + cy * CHUNK_SIDE_LENGTH);
			n += octaves[i].eval(ex, ey) / pow;
		}
		double recip = 1 / OCTAVE_IMPACT_FACTOR;
		return n * (1 - recip) / (1 - Math.pow(recip, NUM_OCTAVES));
	}

	private static Biome generateBiome(double moisture, double elevation) {
		if (moisture > 0.7) {
			if (elevation < 0.5) {
				return OCEAN;
			} else if (elevation < 0.8) {
				return ARCTIC;
			} else {
				return TUNDRA;
			}
		} else if (moisture < 0.4) {
			return DESERT;
		} else {
			// 0.4 <= moisture <= 0.7
			if (elevation < 0.3) {
				if (moisture < 0.5) {
					return SAVANNAH;
				} else {
					return RAINFOREST;
				}
			} else if (elevation < 0.6) {
				if (moisture < 0.5) {
					return TEMPERATE_FOREST;
				} else if (moisture < 0.53) {
					return FRESHWATER;
				} else {
					return FOREST;
				}
			} else {
				if (moisture < 0.4) {
					return GRASSLAND;
				} else if (moisture < 0.55) {
					return TAIGA;
				} else {
					return TUNDRA;
				}
			}
		}
	}

	@Override
	public GeneratePointsChunk upgrade(AbstractTileChunk[][] neighbours, long worldSeed) {
		return GeneratePointsChunk.create(pos(), this, worldSeed);
	}

	public <T extends GenerateBiomesChunk> void cloneDataTo(T chunk) {
		chunk.biomes = biomes;
		chunk.moisture = moisture;
		chunk.elevation = elevation;
	}

	@Override
	public int layer() {
		return 0;
	}

}
