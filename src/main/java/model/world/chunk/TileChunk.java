package model.world.chunk;

import java.util.ArrayList;
import java.util.List;

import engine.common.math.Vector2i;
import model.state.GameState;
import model.world.chunk.lyr3generateActors.GenerateActorsChunk;
import model.world.tile.Tile;

public class TileChunk extends GenerateActorsChunk {

	public static final int FINAL_LAYER_NUMBER = 4;

	private Tile[][] tiles;

	public TileChunk(Vector2i pos) {
		super(pos);
	}

	public static TileChunk create(Vector2i pos, GenerateActorsChunk prev, AbstractTileChunk[][] neighbours, long worldSeed) {
		TileChunk c = new TileChunk(pos);
		prev.cloneDataTo(c);

		c.tiles = new Tile[CHUNK_SIDE_LENGTH][CHUNK_SIDE_LENGTH];
		for (int y = 0; y < CHUNK_SIDE_LENGTH; y++) {
			for (int x = 0; x < CHUNK_SIDE_LENGTH; x++) {
				c.tiles[y][x] = new Tile(x, y, c.biomes[y][x].tileTypeFunction.apply(c.moisture[y][x], c.elevation[y][x]), c);
			}
		}
		return c;
	}

	@Override
	public int layer() {
		return FINAL_LAYER_NUMBER;
	}

	public Tile tile(int x, int y) {
		return tiles[y][x];
	}

	public Tile tile(Vector2i coords) {
		return tiles[coords.y()][coords.x()];
	}

	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}

	public List<Tile> getTilesAsList() {
		List<Tile> tiles = new ArrayList<>();
		for (int y = 0; y < CHUNK_SIDE_LENGTH; y++) {
			for (int x = 0; x < CHUNK_SIDE_LENGTH; x++) {
				tiles.add(tile(x, y));
			}
		}
		return tiles;
	}

	public void addTo(GameState state) {
		for (int i = 0; i < actors.length; i++) {
			state.add(actors[i]);
		}
	}

}
