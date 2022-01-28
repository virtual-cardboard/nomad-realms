package math;

import static java.lang.Math.abs;
import static java.lang.Math.floorMod;
import static model.world.TileChunk.CHUNK_SIDE_LENGTH;

import app.NomadsSettings;
import common.math.Vector2f;
import common.math.Vector2i;
import context.game.visuals.GameCamera;

/**
 * <p>
 * A mutable container of a chunkPos and a tilePos.
 * </p>
 * Compared to {@link Vector2f},<code>WorldPos</code> is mutable so that a
 * programmer can pass it into a function and expect it to be mutated.
 * 
 * 
 * @author Jay
 */
public class WorldPos {

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

	/**
	 * Mutating function
	 */
	public WorldPos add(WorldPos worldPos) {
		chunkPos = chunkPos.add(worldPos.chunkPos);
		setTilePos(tilePos.add(worldPos.tilePos));
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
	 * @param other the position that the returned <code>WorldPos</code> would be
	 *              relative to
	 * @return A new <code>WorldPos</code> representing the relative position from
	 *         <code>other</code>.
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
		int absDiffX = abs(diff.x);

		int yDiffAchievedGoingAlongX;
		if (shifted() == other.shifted()) {
			yDiffAchievedGoingAlongX = absDiffX / 2;
		} else if (shifted()) {
			if (diff.y < 0) {
				yDiffAchievedGoingAlongX = absDiffX / 2;
			} else {
				yDiffAchievedGoingAlongX = (absDiffX + 1) / 2;
			}
		} else {
			if (diff.y < 0) {
				yDiffAchievedGoingAlongX = (absDiffX + 1) / 2;
			} else {
				yDiffAchievedGoingAlongX = absDiffX / 2;
			}
		}
		if (yDiffAchievedGoingAlongX >= abs(diff.y)) {
			return absDiffX;
		} else {
			return absDiffX + abs(diff.y) - yDiffAchievedGoingAlongX;
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
		float x = tilePos.x * s.tileWidth3_4();
		float y = (tilePos.y + 0.5f * (tilePos.x % 2)) * s.tileHeight();
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
		return tilePos.x % 2 == 1;
	}

	/**
	 * Non-mutating function
	 * 
	 * @return a copy of this <code>WorldPos</code>
	 */
	public WorldPos copy() {
		return new WorldPos(chunkPos, tilePos);
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
		return "WorldPos[" + chunkPos.x + ", " + chunkPos.y + "][" + tilePos.x + ", " + tilePos.y + "]";
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
		Vector2i modifiedTilePos = new Vector2i(floorMod(tilePos.x, CHUNK_SIDE_LENGTH), floorMod(tilePos.y, CHUNK_SIDE_LENGTH));
		this.tilePos = modifiedTilePos;
		chunkPos = chunkPos.add((modifiedTilePos.x - tilePos.x) / 16, (modifiedTilePos.y - tilePos.y) / 16);
	}

	public void set(WorldPos other) {
		this.chunkPos = other.chunkPos;
		this.tilePos = other.tilePos;
	}

}
