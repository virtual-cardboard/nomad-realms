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
import nomadrealms.game.world.map.tile.GrayscaleTile;

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
			GrayscaleTile tile = new GrayscaleTile(chunk, tileCoord, biomeNoise.depth().eval(tileCoord));
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
