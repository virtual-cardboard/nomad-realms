package model.tile;

import static model.tile.TileChunk.CHUNK_PIXEL_HEIGHT;
import static model.tile.TileChunk.CHUNK_PIXEL_WIDTH;

import common.math.Vector2f;
import common.math.Vector2i;
import common.math.Vector2l;
import model.actor.Actor;

public class Tile extends Actor {

	public static final int TILE_WIDTH = 80;
	public static final int TILE_HEIGHT = 64;
	public static final int TILE_OUTLINE = 3;

	private int x;
	private int y;
	private TileType type;
	private TileChunk chunk;

	public Tile(int x, int y, TileType type) {
		super(0);
		this.x = x;
		this.y = y;
		this.type = type;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public Vector2f pos() {
		return new Vector2f(x, y);
	}

	public Vector2l absPos() {
		Vector2i cpos = chunk.pos();
		return new Vector2l(x, y).add(cpos.x * CHUNK_PIXEL_WIDTH, cpos.y * CHUNK_PIXEL_HEIGHT);
	}

	public TileType type() {
		return type;
	}

	@Override
	public long id() {
		Vector2i cPos = chunk.pos();
		return (((long) x) << 60) | (((long) y) << 56) | ((long) (cPos.x) << 28) | cPos.y;
	}

	@Override
	public void setID(long id) {
		throw new RuntimeException("Cannot set ID of Tile");
	}

	public static Vector2i tilePos(long tileID) {
		int x = (int) ((tileID >>> 60) & 0b1111);
		int y = (int) ((tileID >>> 56) & 0b1111);
		return new Vector2i(x, y);
	}

	public TileChunk chunk() {
		return chunk;
	}

	public void setChunk(TileChunk chunk) {
		this.chunk = chunk;
	}

	@Override
	public Tile copy() {
		Tile copy = new Tile(x, y, type);
		copy.chunk = chunk;
		return copy;
	}

}
