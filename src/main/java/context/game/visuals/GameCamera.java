package context.game.visuals;

import common.math.Vector2f;
import common.math.Vector2i;
import context.visuals.gui.RootGui;
import model.tile.TileChunk;

public class GameCamera {

	private Vector2i chunkPos = new Vector2i();
	private Vector2f pos = new Vector2f();

	public Vector2i chunkPos() {
		return chunkPos;
	}

	public Vector2f pos() {
		return pos;
	}

	public void update(Vector2i targetChunk, Vector2f targetPos, RootGui rootGui) {
		Vector2i chunkDiff = targetChunk.copy().sub(chunkPos);

		if (chunkDiff.lengthSquared() >= 16) {
			chunkPos.add(chunkDiff.scale(4 / chunkDiff.lengthSquared()));
		} else {
			Vector2f posDiff = chunkDiff.toVec2f();
			posDiff.x *= TileChunk.CHUNK_PIXEL_WIDTH;
			posDiff.y *= TileChunk.CHUNK_PIXEL_HEIGHT;
//			System.out.println("Chunk pos: " + chunkPos);
//			System.out.println("Pos: " + pos);
//			System.out.println("Target chunk: " + targetChunk);
//			System.out.println("Target pos: " + targetPos);
//			System.out.println("Chunk diff: " + chunkDiff);
//			System.out.println(posDiff);
			chunkPos.add(chunkDiff);
			pos.sub(posDiff);
			pos.add(targetPos.copy().sub(pos).sub(rootGui.dimensions().scale(0.5f)).scale(0.3f));
		}
	}

}
