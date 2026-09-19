package nomadrealms.context.game.world.map.generation;

import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;
import static nomadrealms.context.game.world.map.tile.factory.TileType.GRASS;
import static nomadrealms.context.game.world.map.tile.factory.TileType.VOID;

import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.tile.factory.TileFactory;
import nomadrealms.context.game.world.map.tile.factory.TileType;

public class TutorialGenerationStrategy extends MapGenerationStrategy {

	@Override
	public MapGenerationParameters parameters() {
		return new MapGenerationParameters().seed(0);
	}

	@Override
	public Tile[][] generateChunk(Zone zone, Chunk chunk, ChunkCoordinate coord) {
		TileType[][] tileTypes = new TileType[CHUNK_SIZE][CHUNK_SIZE];

		boolean isOriginChunk = coord.region().x() == 0 && coord.region().y() == 0
				&& coord.zone().x() == 0 && coord.zone().y() == 0
				&& coord.x() == 0 && coord.y() == 0;

		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int y = 0; y < CHUNK_SIZE; y++) {
				if (isOriginChunk && isGrassTile(x, y)) {
					tileTypes[x][y] = GRASS;
				} else {
					tileTypes[x][y] = VOID;
				}
			}
		}
		return TileFactory.createTiles(chunk, tileTypes);
	}

	private boolean isGrassTile(int x, int y) {
		return x >= 6 && x <= 10 && (y == 7 || y == 8);
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

}
