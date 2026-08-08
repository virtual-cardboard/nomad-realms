package nomadrealms.context.game.world.map.generation;

import static engine.common.colour.Colour.b;
import static engine.common.colour.Colour.g;
import static engine.common.colour.Colour.r;
import static engine.common.colour.Colour.rgb;
import static engine.common.java.JavaUtil.flatten;
import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.generation.overworld.biome.BiomeParameters;
import nomadrealms.context.game.world.map.generation.overworld.biome.noise.BiomeNoiseGeneratorCluster;
import nomadrealms.context.game.world.map.tile.GrassTile;
import nomadrealms.context.game.world.map.tile.GrayscaleTile;
import nomadrealms.context.game.world.map.tile.IceTile;
import nomadrealms.context.game.world.map.tile.SandTile;
import nomadrealms.context.game.world.map.tile.SnowTile;
import nomadrealms.context.game.world.map.tile.SoilTile;
import nomadrealms.context.game.world.map.tile.StoneTile;
import nomadrealms.context.game.world.map.tile.WaterTile;

public class OverworldGenerationStrategy extends MapGenerationStrategy {

	private final long worldSeed;
	private final BiomeNoiseGeneratorCluster biomeNoise;

	/**
	 * No-arg constructor for serialization.
	 */
	protected OverworldGenerationStrategy() {
		worldSeed = 0;
		biomeNoise = null;
	}

	public OverworldGenerationStrategy(long worldSeed) {
		this.worldSeed = worldSeed;
		biomeNoise = new BiomeNoiseGeneratorCluster(worldSeed, 0.01f);
	}

	@Override
	public MapGenerationParameters parameters() {
		return new MapGenerationParameters()
				.seed(worldSeed)
				.biomeNoise(biomeNoise);
	}

	@Override
	public Tile[][] generateChunk(Zone zone, Chunk chunk, ChunkCoordinate coord) {
		Tile[][] tiles = new Tile[CHUNK_SIZE][CHUNK_SIZE];
		for (TileCoordinate tileCoord : flatten(coord.tileCoordinates())) {
			Tile tile = null;
			switch (zone.biomeGenerationStep().biomes()[chunk.coord().x() * CHUNK_SIZE + tileCoord.x()][chunk.coord().y() * CHUNK_SIZE + tileCoord.y()]) {
				case NORMAL_OCEAN:
				case DEEP_OCEAN:
					tile = new WaterTile(chunk, tileCoord, adjustOceanColor(zone, tileCoord, rgb(0, 141, 207)));
					break;
				case CORAL_REEF:
					tile = new WaterTile(chunk, tileCoord, adjustOceanColor(zone, tileCoord, rgb(68, 15, 148)));
					break;
				case FROZEN_OCEAN:
					tile = new WaterTile(chunk, tileCoord, adjustOceanColor(zone, tileCoord, rgb(116, 204, 244)));
					break;
				case WARM_OCEAN:
					tile = new WaterTile(chunk, tileCoord, adjustOceanColor(zone, tileCoord, rgb(58, 176, 164)));
					break;
				case SWAMP:
					tile = new WaterTile(chunk, tileCoord, rgb(6, 74, 40));
					break;
				case MANGROVE_SWAMP:
					tile = new WaterTile(chunk, tileCoord, rgb(7, 74, 40));
					break;
				case RIVER:
					tile = new WaterTile(chunk, tileCoord, rgb(0, 141, 207));
					break;
				case TROPICAL_RAINFOREST:
					tile = new SoilTile(chunk, tileCoord);
					break;
				case TEMPERATE_RAINFOREST:
					tile = new SoilTile(chunk, tileCoord);
					break;
				case DRY_RAINFOREST:
					tile = new SoilTile(chunk, tileCoord);
					break;
				case JUNGLE:
					tile = new SoilTile(chunk, tileCoord);
					break;
				case GRASSLAND:
					tile = new GrassTile(chunk, tileCoord);
					break;
				case FOREST:
					tile = new GrassTile(chunk, tileCoord, rgb(46, 111, 64));
					break;
				case DESERT:
					tile = new SandTile(chunk, tileCoord);
					break;
				case SNOW:
					tile = new SnowTile(chunk, tileCoord);
					break;
				case SNOWY_MOUNTAINS:
					tile = new SnowTile(chunk, tileCoord);
					break;
				case SNOWY_TUNDRA:
					tile = new IceTile(chunk, tileCoord);
					break;
				case TUNDRA:
					tile = new IceTile(chunk, tileCoord);
					break;
				case BEACH:
					tile = new SandTile(chunk, tileCoord);
					break;
				case PLAINS:
					tile = new GrassTile(chunk, tileCoord);
					break;
				case HILLS:
					tile = new GrassTile(chunk, tileCoord);
					break;
				case MOUNTAINS:
					tile = new StoneTile(chunk, tileCoord);
					break;
				case SAVANNA:
					tile = new GrassTile(chunk, tileCoord);
					break;
				case TAIGA:
					tile = new GrassTile(chunk, tileCoord);
					break;
				default:
					tile = new GrayscaleTile(chunk, tileCoord, biomeNoise.depth().eval(tileCoord));
			}
			tiles[tileCoord.x()][tileCoord.y()] = tile;
		}
		return tiles;
	}

	private int adjustOceanColor(Zone zone, TileCoordinate tileCoord, int rgbColor) {
		if (zone == null || zone.biomeGenerationStep() == null) {
			return rgbColor;
		}
		BiomeParameters params = zone.biomeGenerationStep().parametersAt(tileCoord);
		if (params == null) {
			return rgbColor;
		}
		float continentalness = params.continentalness();
		if (continentalness >= -0.15f) {
			float t = (continentalness + 0.15f) / 0.15f;
			if (t < 0f) t = 0f;
			if (t > 1f) t = 1f;
			float sandBlend = 0.45f * t;
			int baseR = r(rgbColor);
			int baseG = g(rgbColor);
			int baseB = b(rgbColor);
			int sandR = 194;
			int sandG = 178;
			int sandB = 128;
			int finalR = (int) ((1f - sandBlend) * baseR + sandBlend * sandR);
			int finalG = (int) ((1f - sandBlend) * baseG + sandBlend * sandG);
			int finalB = (int) ((1f - sandBlend) * baseB + sandBlend * sandB);
			return rgb(finalR, finalG, finalB);
		} else {
			float ratio = (continentalness + 0.6f) / 0.45f;
			if (ratio < 0f) ratio = 0f;
			if (ratio > 1f) ratio = 1f;
			float factor = 0.4f + 0.6f * ratio;
			int finalR = (int) (r(rgbColor) * factor);
			int finalG = (int) (g(rgbColor) * factor);
			int finalB = (int) (b(rgbColor) * factor);
			return rgb(finalR, finalG, finalB);
		}
	}

	@Override
	public Chunk[][] generateZone(World world, Zone zone) {
		throw new UnsupportedOperationException("Overworld generation should be done layer by layer.");
	}

}
