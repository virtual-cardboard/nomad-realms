package math;

import static java.lang.Math.abs;
import static java.lang.Math.floorMod;
import static java.lang.Math.signum;
import static model.world.chunk.AbstractTileChunk.CHUNK_SIDE_LENGTH;

import app.NomadsSettings;
import context.game.visuals.GameCamera;
import derealizer.Derealizable;
import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import engine.common.math.Vector2f;
import engine.common.math.Vector2i;

/**
 * <p>
 * A mutable container of a chunkPos and a tilePos.
 * </p>
 * Compared to {@link Vector2f},<code>WorldPos</code> is mutable so that a programmer can pass it into a function and
 * expect it to be mutated.
 *
 * @author Jay
 */
public class WorldPos implements Derealizable {

	public static Vector2i toChunkPos(long tileId) {
		return new Vector2i((int) (tileId << 8 >> 36), (int) (tileId << 36 >> 36));
	}

	public static Vector2i toTilePos(long tileId) {
		return new Vector2i((int) ((tileId >>> 60) & 0xF), (int) ((tileId >>> 56) & 0xF));
	}

	private Vector2i chunkPos;
	private Vector2i tilePos;

	public WorldPos() {
		chunkPos = new Vector2i();
		tilePos = new Vector2i();
	}

	public WorldPos(int cx, int cy, int x, int y) {
		this(new Vector2i(cx, cy), new Vector2i(x, y));
	}

	public WorldPos(Vector2i chunkPos, Vector2i tilePos) {
		this.chunkPos = chunkPos;
		this.tilePos = tilePos;
	}

	public WorldPos(long id) {
		chunkPos = toChunkPos(id);
		tilePos = toTilePos(id);
	}

	public WorldPos(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

	/**
	 * Mutating function
	 */
	public WorldPos add(WorldPos worldPos) {
		chunkPos = chunkPos.add(worldPos.chunkPos);
		setTilePos(tilePos.add(worldPos.tilePos));
		return this;
	}

	public WorldPos add(Vector2i tilePosChange) {
		setTilePos(tilePos.add(tilePosChange));
		return this;
	}

	/**
	 * Mutating function
	 */
	public WorldPos sub(WorldPos worldPos) {
		chunkPos = chunkPos.sub(worldPos.chunkPos);
		setTilePos(tilePos.sub(worldPos.tilePos));
		return this;
	}

	/**
	 * Non-mutating function
	 *
	 * @param other the position that the returned <code>WorldPos</code> would be relative to
	 * @return A new <code>WorldPos</code> representing the relative position from
	 * <code>other</code>.
	 */
	public WorldPos relativePos(WorldPos other) {
		return copy().sub(other);
	}

	/**
	 * Non-mutating function
	 *
	 * @param other the other <code>WorldPos</code>
	 * @return the number of tiles <code>other</code> is away from this
	 */
	public int distanceTo(WorldPos other) {
		Vector2i diff = other.tilePos.sub(tilePos).add(other.chunkPos.sub(chunkPos).scale(CHUNK_SIDE_LENGTH));
		int absDiffX = abs(diff.x());

		int yDiffAchievedGoingAlongX;
		if (shifted() == other.shifted()) {
			yDiffAchievedGoingAlongX = absDiffX / 2;
		} else if (shifted()) {
			if (diff.y() < 0) {
				yDiffAchievedGoingAlongX = absDiffX / 2;
			} else {
				yDiffAchievedGoingAlongX = (absDiffX + 1) / 2;
			}
		} else {
			if (diff.y() < 0) {
				yDiffAchievedGoingAlongX = (absDiffX + 1) / 2;
			} else {
				yDiffAchievedGoingAlongX = absDiffX / 2;
			}
		}
		if (yDiffAchievedGoingAlongX >= abs(diff.y())) {
			return absDiffX;
		} else {
			return absDiffX + abs(diff.y()) - yDiffAchievedGoingAlongX;
		}
	}

	public Vector2i directionTo(WorldPos other) {
		Vector2i diff = other.tilePos.sub(tilePos).add(other.chunkPos.sub(chunkPos).scale(CHUNK_SIDE_LENGTH));

		// Other is directly above or below
		if (diff.x() == 0) {
			return new Vector2i(0, (int) signum(diff.y()));
		}
		int xDirection = (int) signum(diff.x());
		if (shifted() && other.shifted()) {
			if (diff.y() <= 0) {
				return new Vector2i(xDirection, 0);
			}
			return new Vector2i(xDirection, 1);
		} else if (!shifted() && !other.shifted()) {
			if (diff.y() <= 0) {
				return new Vector2i(xDirection, -1);
			}
			return new Vector2i(xDirection, 0);
		} else if (shifted()) {
			if (diff.y() <= 0) {
				return new Vector2i(xDirection, 0);
			}
			return new Vector2i(xDirection, 1);
		} else {
			if (diff.y() < 0) {
				return new Vector2i(xDirection, -1);
			}
			return new Vector2i(xDirection, 0);
		}
	}

	/**
	 * Non-mutating function
	 *
	 * @param camera the camera
	 * @param s      the settings
	 * @return the position on screen, in pixels
	 */
	public Vector2f screenPos(GameCamera camera, NomadsSettings s) {
		Vector2f c = chunkPos.sub(camera.chunkPos()).toVec2f().multiply(s.chunkWidth(), s.chunkHeight());
		float x = tilePos.x() * s.tileWidth3_4();
		float y = (tilePos.y() + 0.5f * (tilePos.x() % 2)) * s.tileHeight();
		return c.add(x, y).add(s.tileWidth() / 2, s.tileHeight() / 2).sub(camera.pos());
	}

	/**
	 * Non-mutating function
	 *
	 * @return the absolute tile pos from the origin of the world
	 */
	public Vector2i absoluteTilePos() {
		return chunkPos.multiply(CHUNK_SIDE_LENGTH, CHUNK_SIDE_LENGTH).add(tilePos);
	}

	public boolean shifted() {
		return tilePos.x() % 2 == 1;
	}

	/**
	 * Non-mutating function
	 *
	 * @return a copy of this <code>WorldPos</code>
	 */
	public WorldPos copy() {
		return new WorldPos(chunkPos, tilePos);
	}

	/**
	 * @return a <code>long</code> encoding of this <code>WorldPos</code>
	 */
	public long toId() {
		return ((long) tilePos.x()) << 60 | ((long) tilePos.y()) << 56 |
				((chunkPos.x() & 0xFFFFFFF) | (long) (chunkPos.x() >>> 4) & 0x8000000) << 28 |
				((chunkPos.y() & 0xFFFFFFF) | (long) (chunkPos.y() >>> 4) & 0x8000000);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj instanceof WorldPos) {
			WorldPos worldPos = (WorldPos) obj;
			return chunkPos.equals(worldPos.chunkPos) && tilePos.equals(worldPos.tilePos);
		}
		return false;
	}

	@Override
	public String toString() {
		return "WorldPos[" + chunkPos.x() + ", " + chunkPos.y() + "][" + tilePos.x() + ", " + tilePos.y() + "]";
	}

	public Vector2i chunkPos() {
		return chunkPos;
	}

	/**
	 * Mutating function
	 */
	public void setChunkPos(Vector2i chunkPos) {
		this.chunkPos = chunkPos;
	}

	public Vector2i tilePos() {
		return tilePos;
	}

	/**
	 * Mutating function
	 */
	public void setTilePos(Vector2i tilePos) {
		Vector2i modifiedTilePos = new Vector2i(floorMod(tilePos.x(), CHUNK_SIDE_LENGTH), floorMod(tilePos.y(), CHUNK_SIDE_LENGTH));
		this.tilePos = modifiedTilePos;
		chunkPos = chunkPos.add((tilePos.x() - modifiedTilePos.x()) / 16, (tilePos.y() - modifiedTilePos.y()) / 16);
	}

	public void set(WorldPos other) {
		this.chunkPos = other.chunkPos;
		this.tilePos = other.tilePos;
	}

	@Override
	public void read(SerializationReader reader) {
		long l = reader.readLong();
		chunkPos = toChunkPos(l);
		tilePos = toTilePos(l);
	}

	@Override
	public void write(SerializationWriter writer) {
		writer.consume(toId());
	}

}
