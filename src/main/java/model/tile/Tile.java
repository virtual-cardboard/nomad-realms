package model.tile;

import common.math.Vector2f;
import common.math.Vector2i;
import model.actor.GameObject;

public class Tile extends GameObject {

	public static final int TILE_WIDTH = 1155; // Approx. 2sqrt(3)/3
	public static final int TILE_HEIGHT = 1000;

	public static final int QUARTER_WIDTH = TILE_WIDTH / 4;
	public static final int THREE_QUARTERS_WIDTH = TILE_WIDTH * 3 / 4;
	public static final int HALF_HEIGHT = TILE_HEIGHT / 2;

	private int x;
	private int y;
	private TileType type;
	private TileChunk chunk;

	public Tile(int x, int y, TileType type, TileChunk chunk) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.chunk = chunk;
	}

	@Override
	protected long genID() {
		return 0;
	}

	@Override
	public long id() {
		Vector2i cPos = chunk.pos();
		return (((long) x) << 60) | (((long) y) << 56) | ((long) (cPos.x) << 28) | cPos.y;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public Vector2f pos() {
		float posX = x * THREE_QUARTERS_WIDTH + TILE_WIDTH / 2;
		float posY = y * TILE_HEIGHT + (x % 2 == 0 ? 0 : HALF_HEIGHT) + HALF_HEIGHT;
		return new Vector2f(posX, posY);
	}

	public TileType type() {
		return type;
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

	public static Vector2i tilePos(Vector2f pos) {
		int tx = (int) (pos.x / THREE_QUARTERS_WIDTH);
		int ty;
		if (pos.x % THREE_QUARTERS_WIDTH >= QUARTER_WIDTH) {
			// In center rectangle of hexagon
			if (tx % 2 == 0) {
				// Not shifted
				ty = (int) (pos.y / TILE_HEIGHT);
			} else {
				// Shifted
				ty = (int) ((pos.y - HALF_HEIGHT) / TILE_HEIGHT);
			}
		} else {
			// Beside the zig-zag
			float xOffset;
			if ((int) (pos.x / THREE_QUARTERS_WIDTH) % 2 == 0) {
				// Zig-zag starting from right side
				xOffset = QUARTER_WIDTH * Math.abs(pos.y % TILE_HEIGHT - HALF_HEIGHT) / HALF_HEIGHT;
			} else {
				// Zig-zag starting from left side
				xOffset = QUARTER_WIDTH * Math.abs((pos.y + HALF_HEIGHT) % TILE_HEIGHT - HALF_HEIGHT) / HALF_HEIGHT;
			}
			if (pos.x % THREE_QUARTERS_WIDTH <= xOffset) {
				// Left of zig-zag
				tx--;
			}
			if (tx % 2 == 0) {
				// Not shifted
				ty = (int) (pos.y / TILE_HEIGHT);
			} else {
				// Shifted
				ty = (int) ((pos.y - HALF_HEIGHT) / TILE_HEIGHT);
			}
		}
		return new Vector2i(tx, ty);
	}

	public int distanceTo(Vector2i tilePosOther, Vector2i chunkPosOther) {
		if (chunk.pos() != chunkPosOther) {
			tilePosOther
					.add(new Vector2i(16 * (chunk.pos().x - chunkPosOther.x), 16 * (chunk.pos().y - chunkPosOther.y)));
		}

		return 0;
	}

	public boolean shifted() {
		return x % 2 == 1;
	}

	public TileChunk chunk() {
		return chunk;
	}

	@Override
	public Tile copy() {
		Tile copy = new Tile(x, y, type, chunk);
		copy.chunk = chunk;
		return copy;
	}

	@Override
	public String description() {
		return "A " + type + " tile at chunk (" + chunk.pos().x + ", " + chunk.pos().y + " at pos (" + x + ", " + y + ")";
	}

}
