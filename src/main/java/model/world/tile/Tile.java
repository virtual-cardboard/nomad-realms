package model.world.tile;

import static java.lang.Math.abs;
import static model.world.WorldSerializationFormats.TILE;

import app.NomadsSettings;
import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import derealizer.format.Derealizable;
import engine.common.math.Vector2f;
import engine.common.math.Vector2i;
import math.WorldPos;
import model.GameObject;
import model.id.TileId;
import model.state.GameState;
import model.world.WorldSerializationFormats;
import model.world.chunk.AbstractTileChunk;

/**
 * <p>
 * A hexagonal space in the world. Tiles can be targets of some cards. We allow the tile's position to be stored in a
 * <code>long</code>, so that network events can send a <code>long</code> to represent a tile instead of a chunk
 * coordinate and tile coordinate (which would take much more data).
 * </p>
 * <p>
 * When encoding a tile, the first 4 bits represent the <code>x</code> coordinate of the tile. The next 4 bits represent
 * the <code>y</code> coordinate of the tile. The next 28 bits represent the <code>x</code> coordinate of the chunk. The
 * final 28 bits represent the <code>y</code> coordinate of the chunk.
 * </p>
 * <p>
 * 4 bits - x coord of tile<br> 4 bits - y coord of tile<br> 28 bits - x coord of chunk<br> 28 bits - y coord of
 * chunk<br>
 *
 * @author Jay
 */
public class Tile extends GameObject implements Derealizable {

	private WorldPos worldPos;
	private short tileType;

	public Tile() {
		worldPos = new WorldPos();
		tileType = -1;
	}

	public Tile(int x, int y, TileType type, AbstractTileChunk chunk) {
		this(new WorldPos(chunk.pos().x(), chunk.pos().y(), x, y), (short) type.ordinal());
	}

	public Tile(WorldPos worldPos, short tileType) {
		this.worldPos = worldPos;
		this.tileType = tileType;
	}

	public Tile(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

	@Override
	public TileId id() {
		Vector2i cPos = worldPos.chunkPos();
		long l = ((long) x()) << 60 | ((long) y()) << 56 | ((cPos.x() & 0xFFFFFFF)
				| (long) (cPos.x() >>> 4) & 0x8000000) << 28 | ((cPos.y() & 0xFFFFFFF) | (long) (cPos.y() >>> 4) & 0x8000000);
		return new TileId(l);
	}

	public int x() {
		return worldPos.tilePos().x();
	}

	public int y() {
		return worldPos.tilePos().y();
	}

	public TileType type() {
		return TileType.values()[tileType];
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

		int tx = (int) (pos.x() / threeQuartersWidth);
		int ty;
		if (pos.x() % threeQuartersWidth >= quarterWidth) {
			// In center rectangle of hexagon
			if (tx % 2 == 0) {
				// Not shifted
				ty = (int) (pos.y() / tileHeight);
			} else {
				// Shifted
				ty = (int) ((pos.y() - halfHeight) / tileHeight);
			}
		} else {
			// Beside the zig-zag
			float xOffset;
			if ((int) (pos.x() / threeQuartersWidth) % 2 == 0) {
				// Zig-zag starting from right side
				xOffset = quarterWidth * abs(pos.y() % tileHeight - halfHeight) / halfHeight;
			} else {
				// Zig-zag starting from left side
				xOffset = quarterWidth * abs((pos.y() + halfHeight) % tileHeight - halfHeight) / halfHeight;
			}
			if (pos.x() % threeQuartersWidth <= xOffset) {
				// Left of zig-zag
				tx--;
			}
			if (tx % 2 == 0) {
				// Not shifted
				ty = (int) (pos.y() / tileHeight);
			} else {
				// Shifted
				ty = (int) ((pos.y() - halfHeight) / tileHeight);
			}
		}
		return new Vector2i(tx, ty);
	}

	public int distanceTo(Tile other) {
		return worldPos().distanceTo(other.worldPos());
	}

	public boolean shifted() {
		return worldPos.shifted();
	}

	public WorldPos worldPos() {
		return worldPos;
	}

	@Override
	public Tile copy() {
		return new Tile(worldPos.copy(), tileType);
	}

	@Override
	public String description() {
		return "A " + type() + " tile at " + worldPos;
	}

	@Override
	public void addTo(GameState state) {
		throw new UnsupportedOperationException("Cannot add a tile to the game state. Tiles are stored in chunks.");
	}

	@Override
	public void removeFrom(GameState state) {
		throw new UnsupportedOperationException("Cannot remove a tile from the game state.");
	}

	@Override
	public WorldSerializationFormats formatEnum() {
		return TILE;
	}

	@Override
	public void read(SerializationReader reader) {
		this.tileType = reader.readShort();
	}

	@Override
	public void write(SerializationWriter writer) {
		writer.consume(tileType);
	}

}
