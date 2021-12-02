package model.actor;

import static model.tile.TileChunk.CHUNK_PIXEL_HEIGHT;
import static model.tile.TileChunk.CHUNK_PIXEL_WIDTH;

import common.math.Vector2f;
import common.math.Vector2i;
import context.game.visuals.GameCamera;

public abstract class PositionalActor extends Actor {

	protected Vector2i chunkPos = new Vector2i();
	protected Vector2f pos = new Vector2f();

	public Vector2f pos() {
		return pos;
	}

	public void setPos(Vector2f pos) {
		this.pos = pos;
	}

	public Vector2i chunkPos() {
		return chunkPos;
	}

	public void setChunkPos(Vector2i chunkPos) {
		this.chunkPos = chunkPos;
	}

	public <A extends PositionalActor> A copyTo(A copy) {
		copy.id = id;
		copy.chunkPos = chunkPos.copy();
		copy.pos = pos.copy();
		return copy;
	}

	public Vector2f viewPos(GameCamera camera) {
		return relativePos(camera.chunkPos(), camera.pos());
	}

	public Vector2f relativePos(Vector2i chunkPos, Vector2f pos) {
		Vector2i chunkDiff = this.chunkPos.copy().sub(chunkPos);
		return this.pos.copy().sub(pos).add(chunkDiff.x * CHUNK_PIXEL_WIDTH, chunkDiff.y * CHUNK_PIXEL_HEIGHT);
	}

}
