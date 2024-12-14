package nomadrealms.game.world.map.generation;

import static common.colour.Colour.rgb;
import static common.java.JavaUtil.flatten;
import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.Zone;
import nomadrealms.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.game.world.map.generation.status.biome.noise.BiomeNoiseGeneratorCluster;
import nomadrealms.game.world.map.tile.GrassTile;
import nomadrealms.game.world.map.tile.GrayscaleTile;
import nomadrealms.game.world.map.tile.IceTile;
import nomadrealms.game.world.map.tile.SandTile;
import nomadrealms.game.world.map.tile.SnowTile;
import nomadrealms.game.world.map.tile.SoilTile;
import nomadrealms.game.world.map.tile.StoneTile;
import nomadrealms.game.world.map.tile.WaterTile;

public class MainWorldGenerationStrategy implements MapGenerationStrategy {

	private final long worldSeed;

	private final BiomeNoiseGeneratorCluster biomeNoise;

	public MainWorldGenerationStrategy(long worldSeed) {
		this.worldSeed = worldSeed;
		biomeNoise = new BiomeNoiseGeneratorCluster(worldSeed, 500);
	}

	@Override
	public Tile[][] generateChunk(Zone zone, Chunk chunk, ChunkCoordinate coord) {
		Tile[][] tiles = new Tile[CHUNK_SIZE][CHUNK_SIZE];
		for (TileCoordinate tileCoord : flatten(coord.tileCoordinates())) {
			Tile tile = null;
			switch (zone.biomeGenerationStep().biomes()[chunk.coord().x() * CHUNK_SIZE + tileCoord.x()][chunk.coord().y() * CHUNK_SIZE + tileCoord.y()]) {
				case NORMAL_OCEAN:
					tile = new WaterTile(chunk, tileCoord, rgb(0, 141, 207));
					break;
				case DEEP_OCEAN:
					tile = new WaterTile(chunk, tileCoord, rgb(1, 4, 69));
					break;
				case CORAL_REEF:
					tile = new WaterTile(chunk, tileCoord, rgb(68, 15, 148));
					break;
				case FROZEN_OCEAN:
					tile = new WaterTile(chunk, tileCoord, rgb(116, 204, 244));
					break;
				case WARM_OCEAN:
					tile = new WaterTile(chunk, tileCoord, rgb(58, 176, 164));
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
					tile = new GrassTile(chunk, tileCoord);
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

	@Override
	public Chunk[][] generateZone(World world, Zone zone) {
		Zone[][] zones = zone.getSurroundingZones(world, 0);
		zone.biomeGenerationStep().generate(biomeNoise, zones);

		Chunk[][] chunks = new Chunk[ZONE_SIZE][ZONE_SIZE];
		for (ChunkCoordinate chunkCoord : flatten(zone.coord().chunkCoordinates())) {
			Chunk chunk = new Chunk(zone, chunkCoord);
			chunk.tiles(generateChunk(zone, chunk, chunkCoord));
			chunks[chunkCoord.x()][chunkCoord.y()] = chunk;
		}
		return chunks;
	}

}
