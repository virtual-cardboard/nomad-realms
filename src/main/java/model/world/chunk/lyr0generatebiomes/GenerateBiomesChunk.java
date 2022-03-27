package model.world.chunk.lyr0generatebiomes;

import static model.world.Biome.*;

import common.math.Vector2i;
import graphics.noise.OpenSimplexNoise;
import model.world.Biome;
import model.world.WorldSeed;
import model.world.chunk.AbstractTileChunk;
import model.world.chunk.lyr1generatenodes.GenerateNodesChunk;

public class GenerateBiomesChunk extends AbstractTileChunk {

	private static final double MOISTURE_SCALE = 200;
	private static final double ELEVATION_SCALE = 100;

	protected Biome[][] biomes;
	protected double[][] moisture;
	protected double[][] elevation;

	public GenerateBiomesChunk(Vector2i pos) {
		super(pos);
	}

	public static GenerateBiomesChunk create(Vector2i pos, long worldSeed) {
		GenerateBiomesChunk c = new GenerateBiomesChunk(pos);

		OpenSimplexNoise moistureNoise = new OpenSimplexNoise(WorldSeed.moisture(worldSeed));
		OpenSimplexNoise elevNoise = new OpenSimplexNoise(WorldSeed.elevation(worldSeed));

		c.biomes = new Biome[CHUNK_SIDE_LENGTH][CHUNK_SIDE_LENGTH];
		c.moisture = new double[CHUNK_SIDE_LENGTH][CHUNK_SIDE_LENGTH];
		c.elevation = new double[CHUNK_SIDE_LENGTH][CHUNK_SIDE_LENGTH];
		for (int y = 0; y < CHUNK_SIDE_LENGTH; y++) {
			for (int x = 0; x < CHUNK_SIDE_LENGTH; x++) {
				double moisture = moistureNoise.eval((x + pos.x * 16) / MOISTURE_SCALE,
						(y + (x % 2) * 0.5 + pos.y * 16) / MOISTURE_SCALE);
				double elevation = elevNoise.eval((x + pos.x * 16) / ELEVATION_SCALE,
						(y + (x % 2) * 0.5 + pos.y * 16) / ELEVATION_SCALE);

				Biome biomeType = generateBiome(moisture, elevation);
				c.biomes[y][x] = biomeType;
				c.moisture[y][x] = moisture;
				c.elevation[y][x] = elevation;
			}

		}
		return c;
	}

	private static Biome generateBiome(double moisture, double elevation) {
		if (moisture > 0.8) {
			if (elevation < 0.5) {
				return OCEAN;
			} else if (elevation < 0.8) {
				return ARCTIC;
			} else {
				return TUNDRA;
			}
		} else if (moisture < 0.3) {
			return DESERT;
		} else {
			// 0.3 <= moisture <= 0.8
			if (elevation < 0.3) {
				if (moisture < 0.5) {
					return SAVANNAH;
				} else {
					return RAINFOREST;
				}
			} else if (elevation < 0.6) {
				if (moisture < 0.5) {
					return TEMPERATE_FOREST;
				} else if (moisture < 0.55) {
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
	public GenerateNodesChunk upgrade(AbstractTileChunk[][] neighbours, long worldSeed) {
		return GenerateNodesChunk.create(pos(), this, worldSeed);
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
