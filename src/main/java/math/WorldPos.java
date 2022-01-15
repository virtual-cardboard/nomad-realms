package math;

import static java.lang.Math.floorMod;
import static model.world.TileChunk.CHUNK_SIDE_LENGTH;

import common.math.Vector2f;
import common.math.Vector2i;

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

	public WorldPos(Vector2i chunkPos, Vector2i tilePos) {
		this.chunkPos = chunkPos;
		this.tilePos = tilePos;
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
	 * @return the absolute tile pos from the origin of the world
	 */
	public Vector2i absoluteTilePos() {
		return chunkPos.multiply(CHUNK_SIDE_LENGTH, CHUNK_SIDE_LENGTH).add(tilePos);
	}

	/**
	 * Non-mutating function
	 * 
	 * @return a copy of this <code>WorldPos</code>
	 */
	public WorldPos copy() {
		return new WorldPos(chunkPos, tilePos);
	}

}
