package model.world;

import app.NomadsSettings;
import common.math.Vector2f;
import common.math.Vector2i;
import math.WorldPos;
import model.actor.GameObject;
import model.state.GameState;

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

	public static final float WIDTH_TO_HEIGHT_RATIO = 1.1547005f;

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

	public TileType type() {
		return type;
	}

	@Override
	public void setID(long id) {
		throw new RuntimeException("Cannot set ID of Tile");
	}

	public static Vector2i tileCoords(long tileID) {
		int x = (int) ((tileID >>> 60) & 0b1111);
		int y = (int) ((tileID >>> 56) & 0b1111);
		return new Vector2i(x, y);
	}

	public static Vector2i tileCoords(Vector2f pos, NomadsSettings s) {
//		pos = pos.multiply(1 / (s.worldScale * 1.1547005f), 1 / s.worldScale);
//		int tx = (int) (pos.x / 0.75f);
//		int ty;
//		if (pos.x % 0.75f >= 0.25f) {
//			// In center rectangle of hexagon
//			if (tx % 2 == 0) {
//				// Not shifted
//				ty = (int) (pos.y);
//			} else {
//				// Shifted
//				ty = (int) (pos.y - 0.5f);
//			}
//		} else {
//			// Beside the zig-zag
//			float xOffset;
//			if ((int) (pos.x / 0.75f) % 2 == 0) {
//				// Zig-zag starting from right side
//				xOffset = QUARTER_WIDTH * abs(pos.y % TILE_HEIGHT - HALF_HEIGHT) / HALF_HEIGHT;
//			} else {
//				// Zig-zag starting from left side
//				xOffset = QUARTER_WIDTH * abs((pos.y + HALF_HEIGHT) % TILE_HEIGHT - HALF_HEIGHT) / HALF_HEIGHT;
//			}
//			if (pos.x % THREE_QUARTERS_WIDTH <= xOffset) {
//				// Left of zig-zag
//				tx--;
//			}
//			if (tx % 2 == 0) {
//				// Not shifted
//				ty = (int) (pos.y / TILE_HEIGHT);
//			} else {
//				// Shifted
//				ty = (int) ((pos.y - HALF_HEIGHT) / TILE_HEIGHT);
//			}
//		}
//		return new Vector2i(tx, ty);
		// TODO
		return null;
	}

	public int distanceTo(Tile other) {
		return worldPos().distanceTo(other.worldPos());
	}

	public boolean shifted() {
		return x % 2 == 1;
	}

	public TileChunk chunk() {
		return chunk;
	}

	public WorldPos worldPos() {
		return new WorldPos(chunk.pos(), new Vector2i(x, y));
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

	@Override
	public void addTo(GameState state) {
	}

	public static void main(String[] args) {
		System.out.println(new Tile(0, 3, null, null).distanceTo(new Tile(3, 4, null, null)));
	}

}
