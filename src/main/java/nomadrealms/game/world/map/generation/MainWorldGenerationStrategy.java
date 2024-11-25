package nomadrealms.game.world.map.generation;

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
import nomadrealms.game.world.map.tile.DeepWaterTile;
import nomadrealms.game.world.map.tile.GrassTile;
import nomadrealms.game.world.map.tile.GrayscaleTile;
import nomadrealms.game.world.map.tile.MossyTile;
import nomadrealms.game.world.map.tile.SandTile;
import nomadrealms.game.world.map.tile.SnowTile;
import nomadrealms.game.world.map.tile.WaterTile;

public class MainWorldGenerationStrategy implements MapGenerationStrategy {

	private final long worldSeed;

	private final BiomeNoiseGeneratorCluster biomeNoise;

	public MainWorldGenerationStrategy(long worldSeed) {
		this.worldSeed = worldSeed;
		biomeNoise = new BiomeNoiseGeneratorCluster(worldSeed);
	}

	@Override
	public Tile[][] generateChunk(Zone zone, Chunk chunk, ChunkCoordinate coord) {
		Tile[][] tiles = new Tile[CHUNK_SIZE][CHUNK_SIZE];
		for (TileCoordinate tileCoord : flatten(coord.tileCoordinates())) {
			switch (zone.biomeGenerationStep().biomeAt(tileCoord)) {
				case UNDEFINED:
					throw new IllegalStateException("Biome is undefined at " + tileCoord);
				case DEEP_OCEAN:
					tiles[tileCoord.x()][tileCoord.y()] = new DeepWaterTile(chunk, tileCoord);
				case OCEAN:
					tiles[tileCoord.x()][tileCoord.y()] = new WaterTile(chunk, tileCoord);
					break;
				case GRASSLAND:
					tiles[tileCoord.x()][tileCoord.y()] = new GrassTile(chunk, tileCoord);
					break;
				case FOREST:
					tiles[tileCoord.x()][tileCoord.y()] = new MossyTile(chunk, tileCoord);
					break;
				case DESERT:
					tiles[tileCoord.x()][tileCoord.y()] = new SandTile(chunk, tileCoord);
					break;
				case SNOW:
					tiles[tileCoord.x()][tileCoord.y()] = new SnowTile(chunk, tileCoord);
					break;
				case PLAINS:
				case TUNDRA:
				case SWAMP:
				case BEACH:
				case VOID:
				case HILLS:
				case MOUNTAINS:
				case JUNGLE:
				case SAVANNA:
				case TAIGA:
				case SNOWY_TUNDRA:
				case SNOWY_MOUNTAINS:
				case RIVER:
				case ICY_SHORE:
				case ROCKY_SHORE:
					tiles[tileCoord.x()][tileCoord.y()] = new GrayscaleTile(chunk, tileCoord, (biomeNoise.weirdness().eval(tileCoord) + 1) / 2);
					break;
			}
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
