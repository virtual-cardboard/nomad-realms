package nomadrealms.context.game.world.map.generation;

import static java.util.Objects.requireNonNull;
import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;
import static nomadrealms.context.game.world.map.tile.factory.TileFactory.createTiles;
import static nomadrealms.context.game.world.map.tile.factory.TileType.GRASS;
import static nomadrealms.context.game.world.map.tile.factory.TileType.VOID;
import static nomadrealms.context.game.world.map.tile.factory.TileType.WATER;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.tile.factory.TileType;

public class FileBasedGenerationStrategy implements MapGenerationStrategy {

	private static final String WORLD_FILE = "/worlds/predefined_world.txt";
	private final List<String> worldData;

	public FileBasedGenerationStrategy() {
		this.worldData = loadWorldData();
	}

	@Override
	public MapGenerationParameters parameters() {
		return new MapGenerationParameters()
				.seed(0);
	}

	@Override
	public Tile[][] generateChunk(Zone zone, Chunk chunk, ChunkCoordinate coord) {
		TileType[][] chunkTileTypes = new TileType[CHUNK_SIZE][CHUNK_SIZE];
		int startX = coord.x() * CHUNK_SIZE;
		int startY = coord.y() * CHUNK_SIZE;

		for (int y = 0; y < CHUNK_SIZE; y++) {
			for (int x = 0; x < CHUNK_SIZE; x++) {
				int worldX = startX + x;
				int worldY = startY + y;
				if (worldY < worldData.size() && worldX < worldData.get(worldY).length()) {
					chunkTileTypes[y][x] = charToTileType(worldData.get(worldY).charAt(worldX));
				} else {
					chunkTileTypes[y][x] = VOID;
				}
			}
		}
		return createTiles(chunk, chunkTileTypes);
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

	private List<String> loadWorldData() {
		List<String> lines = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(getFile()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	private TileType charToTileType(char c) {
		switch (c) {
			case 'W':
				return WATER;
			case 'G':
				return GRASS;
			default:
				return VOID;
		}
	}

	private File getFile() {
		return new File(requireNonNull(getClass().getResource(WORLD_FILE)).getFile());
	}

}
