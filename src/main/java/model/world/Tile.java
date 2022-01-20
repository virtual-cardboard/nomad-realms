package model.world;

import static java.lang.Math.abs;

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
		float quarterWidth = s.tileWidth() / 4;
		float threeQuartersWidth = s.tileWidth3_4();
		float tileHeight = s.tileHeight();
		float halfHeight = tileHeight / 2;

		int tx = (int) (pos.x / threeQuartersWidth);
		int ty;
		if (pos.x % threeQuartersWidth >= quarterWidth) {
			// In center rectangle of hexagon
			if (tx % 2 == 0) {
				// Not shifted
				ty = (int) (pos.y / tileHeight);
			} else {
				// Shifted
				ty = (int) ((pos.y - halfHeight) / tileHeight);
			}
		} else {
			// Beside the zig-zag
			float xOffset;
			if ((int) (pos.x / threeQuartersWidth) % 2 == 0) {
				// Zig-zag starting from right side
				xOffset = quarterWidth * abs(pos.y % tileHeight - halfHeight) / halfHeight;
			} else {
				// Zig-zag starting from left side
				xOffset = quarterWidth * abs((pos.y + halfHeight) % tileHeight - halfHeight) / halfHeight;
			}
			if (pos.x % threeQuartersWidth <= xOffset) {
				// Left of zig-zag
				tx--;
			}
			if (tx % 2 == 0) {
				// Not shifted
				ty = (int) (pos.y / tileHeight);
			} else {
				// Shifted
				ty = (int) ((pos.y - halfHeight) / tileHeight);
			}
		}
		return new Vector2i(tx, ty);
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
