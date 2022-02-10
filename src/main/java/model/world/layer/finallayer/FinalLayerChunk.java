package model.world.layer.finallayer;

import common.math.Vector2i;
import model.state.GameState;
import model.world.Tile;
import model.world.TileChunk;
import model.world.TileType;
import model.world.layer.actorcluster.ActorClusterChunk;

public class FinalLayerChunk extends ActorClusterChunk {

	private Tile[][] tiles;

	public FinalLayerChunk(Vector2i pos) {
		super(pos);
	}

	public static FinalLayerChunk create(Vector2i pos, ActorClusterChunk prev, TileChunk[][] neighbours, long worldSeed) {
		FinalLayerChunk c = new FinalLayerChunk(pos);

		prev.cloneDataTo(c);

		TileType[][] tileTypes = new TileType[16][16];

		for (int y = 0; y < 16; y++) {
			for (int x = 0; x < 16; x++) {
				tileTypes[y][x] = c.biomes[y][x].tileTypeFunction.apply(c.elevation[y][x], c.moisture[y][x]);
			}
		}

		c.tiles = new Tile[CHUNK_SIDE_LENGTH][CHUNK_SIDE_LENGTH];
		for (int y = 0; y < CHUNK_SIDE_LENGTH; y++) {
			for (int x = 0; x < CHUNK_SIDE_LENGTH; x++) {
				TileType tileType = c.biomes[y][x].tileTypeFunction.apply(c.elevation[y][x], c.moisture[y][x]);
				c.tiles[y][x] = new Tile(x, y, tileType, c);
			}
		}
		return c;
	}

	@Override
	public int layer() {
		return 4;
	}

	public Tile tile(int x, int y) {
		return tiles[y][x];
	}

	public Tile tile(Vector2i coords) {
		return tiles[coords.y][coords.x];
	}

	public void addTo(GameState state) {
		for (int i = 0; i < actors.length; i++) {
			state.add(actors[i]);
		}
	}

}
