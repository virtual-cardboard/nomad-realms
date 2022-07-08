package model.world.chunk;

import static model.world.WorldSerializationFormats.TILE_CHUNK;

import java.util.ArrayList;
import java.util.List;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import derealizer.format.Serializable;
import engine.common.math.Vector2i;
import math.IdGenerator;
import math.WorldPos;
import model.actor.Actor;
import model.state.GameState;
import model.world.WorldSerializationFormats;
import model.world.chunk.lyr3generateActors.GenerateActorsChunk;
import model.world.tile.Tile;

public class TileChunk extends GenerateActorsChunk implements Serializable {

	public static final int FINAL_LAYER_NUMBER = 4;

	private Tile[][] tiles;

	public TileChunk() {
		super(new Vector2i());
	}

	public TileChunk(Vector2i pos) {
		super(pos);
	}

	public TileChunk(byte[] bytes) {
		super(new Vector2i());
		read(new SerializationReader(bytes));
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

	public void genActorIds(IdGenerator idGenerator) {
		for (Actor actor : actors) {
			actor.setId(idGenerator.genId());
		}
	}

	public void addActorsTo(GameState state) {
		for (int i = 0; i < actors.length; i++) {
			state.add(actors[i]);
		}
	}

	@Override
	public WorldSerializationFormats formatEnum() {
		return TILE_CHUNK;
	}

	/**
	 * Non-default implementation
	 *
	 * @param reader The reader
	 */
	@Override
	public void read(SerializationReader reader) {
		pos().read(reader);
		// Convert list of tiles into 2D array
		tiles = new Tile[CHUNK_SIDE_LENGTH][CHUNK_SIDE_LENGTH];
		for (int y = 0; y < CHUNK_SIDE_LENGTH; y++) {
			for (int x = 0; x < CHUNK_SIDE_LENGTH; x++) {
				tiles[y][x] = new Tile();
				tiles[y][x].worldPos().set(new WorldPos(pos(), new Vector2i(x, y)));
				tiles[y][x].read(reader);
			}
		}
	}

	/**
	 * Non-default implementation
	 *
	 * @param writer The writer
	 */
	@Override
	public void write(SerializationWriter writer) {
		pos().write(writer);
		for (int y = 0; y < CHUNK_SIDE_LENGTH; y++) {
			for (int x = 0; x < CHUNK_SIDE_LENGTH; x++) {
				tiles[y][x].write(writer);
			}
		}
	}

}
