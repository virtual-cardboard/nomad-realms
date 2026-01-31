package nomadrealms.context.game.world.map.generation;

import static nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;
import static nomadrealms.context.game.world.map.tile.factory.TileType.GRASS;
import static nomadrealms.context.game.world.map.tile.factory.TileType.WATER;

import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.tile.factory.TileFactory;
import nomadrealms.context.game.world.map.tile.factory.TileType;

public class TemplateGenerationStrategy implements MapGenerationStrategy {

	private MapInitialization mapInitialization;

	@Override
	public MapGenerationParameters parameters() {
		return new MapGenerationParameters()
				.seed(0);
	}

	@Override
	public Tile[][] generateChunk(Zone zone, Chunk chunk, ChunkCoordinate coord) {
		return TileFactory.createTiles(chunk, new TileType[][]{
				{GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS,
						GRASS, GRASS},
				{GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS,
						GRASS, GRASS},
				{GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS,
						GRASS, GRASS},
				{GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS,
						GRASS, GRASS},
				{GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS,
						GRASS, GRASS},
				{GRASS, GRASS, GRASS, GRASS, GRASS, WATER, WATER, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS,
						GRASS, GRASS},
				{GRASS, GRASS, GRASS, GRASS, GRASS, WATER, WATER, WATER, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS,
						GRASS, GRASS},
				{GRASS, GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, GRASS, GRASS, GRASS, GRASS, GRASS,
						GRASS, GRASS},
				{GRASS, GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS, GRASS,
						GRASS, GRASS},
				{GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS,
						GRASS, GRASS},
				{GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS,
						GRASS, GRASS},
				{GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS,
						GRASS, GRASS},
				{GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS,
						GRASS, GRASS},
				{GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS, GRASS,
						GRASS, GRASS},
				{GRASS, GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS, GRASS,
						GRASS, GRASS},
				{GRASS, GRASS, GRASS, GRASS, WATER, WATER, WATER, WATER, WATER, WATER, WATER, WATER, GRASS, GRASS,
						GRASS, GRASS},
		});
	}

	@Override
	public Chunk[][] generateZone(World world, Zone zone) {
		Chunk[][] chunks = new Chunk[ZONE_SIZE][ZONE_SIZE];
		for (int x = 0; x < ZONE_SIZE; x++) {
			for (int y = 0; y < ZONE_SIZE; y++) {
				ChunkCoordinate chunkCoord = new ChunkCoordinate(zone.coord(), x, y);
				chunks[x][y] = new Chunk(zone, chunkCoord);
				chunks[x][y].tiles(generateChunk(zone, chunks[x][y], chunkCoord));
			}
		}
		return chunks;
	}

	@Override
	public MapGenerationStrategy mapInitialization(MapInitialization mapInitialization) {
		this.mapInitialization = mapInitialization;
		return this;
	}

	@Override
	public void initializeMap(World world) {
		if (mapInitialization != null) {
			mapInitialization.initialize(world);
		}
	}

}