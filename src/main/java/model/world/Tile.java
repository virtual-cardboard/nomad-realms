package model.world;

import static model.world.TileChunk.CHUNK_HEIGHT;
import static model.world.TileChunk.CHUNK_WIDTH;

import common.math.Vector2f;
import common.math.Vector2i;
import model.actor.GameObject;

/**
 * <p>
 * A hexagonal space in the world. Tiles can be targets of some cards. We allow
 * the tile's position to be stored in a <code>long</code>, so that network
 * events can send a <code>long</code> to represent a tile instead of a chunk
 * coordinate and tile coordinate (which would take much more data).
 * </p>
 * <p>
 * When encoding a tile, the first 4 bits represent the <code>x</code>
 * coordinate of the tile. The next 4 bits represent the <code>y</code>
 * coordinate of the tile. The next 28 bits represent the <code>x</code>
 * coordinate of the chunk. The final 28 bits represent the <code>y</code>
 * coordinate of the chunk.
 * </p>
 * 
 * 4 bits - x coord of tile<br>
 * 4 bits - y coord of tile<br>
 * 28 bits - x coord of chunk<br>
 * 28 bits - y coord of chunk<br>
 * 
 * @author Jay
 *
 */
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
		return ((long) x) << 60 | ((long) y) << 56 | ((cPos.x & 0xFFFFFFF)
				| (long) (cPos.x >>> 4) & 0x8000000) << 28 | ((cPos.y & 0xFFFFFFF) | (long) (cPos.y >>> 4) & 0x8000000);
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

	public static Vector2f tilePos(long tileID) {
		int x = (int) ((tileID >>> 60) & 0b1111);
		int y = (int) ((tileID >>> 56) & 0b1111);
		float posX = x * THREE_QUARTERS_WIDTH + TILE_WIDTH / 2;
		float posY = y * TILE_HEIGHT + (x % 2 == 0 ? 0 : HALF_HEIGHT) + HALF_HEIGHT;
		return new Vector2f(posX, posY);
	}

	public static Vector2i tileCoords(long tileID) {
		int x = (int) ((tileID >>> 60) & 0b1111);
		int y = (int) ((tileID >>> 56) & 0b1111);
		return new Vector2i(x, y);
	}

	public static Vector2i tileCoords(Vector2f pos) {
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

	public float distanceTo(Tile other) {
		Vector2i chunkDiff = other.chunk.pos().sub(chunk.pos());
		return other.pos().sub(pos()).add(chunkDiff.x * CHUNK_WIDTH, chunkDiff.y * CHUNK_HEIGHT).length();
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
