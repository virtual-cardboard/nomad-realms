package context.game.visuals;

import static model.tile.TileChunk.CHUNK_PIXEL_HEIGHT;
import static model.tile.TileChunk.CHUNK_PIXEL_WIDTH;

import common.math.Vector2f;
import common.math.Vector2i;
import context.visuals.gui.RootGui;

public class GameCamera {

	public static final int RENDER_RADIUS = 1;

	private Vector2i chunkPos = new Vector2i();
	private Vector2f pos = new Vector2f();

	public Vector2i chunkPos() {
		return chunkPos;
	}

	public Vector2f pos() {
		return pos;
	}

	public void update(Vector2i targetChunk, Vector2f targetPos, RootGui rootGui) {
		Vector2i chunkDiff = targetChunk.sub(chunkPos);

		if (chunkDiff.lengthSquared() >= 16) {
			chunkPos = chunkPos.add(chunkDiff.scale(4 / chunkDiff.lengthSquared()));
		} else {
			Vector2f posDiff = chunkDiff.toVec2f().multiply(CHUNK_PIXEL_WIDTH, CHUNK_PIXEL_HEIGHT);
			chunkPos = targetChunk;
			pos = pos.sub(posDiff);
			pos = pos.add(targetPos.sub(pos).sub(rootGui.dimensions().scale(0.5f)).scale(0.3f));
		}
	}

}
