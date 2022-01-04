package graphics.particle.function;

import static model.world.TileChunk.CHUNK_HEIGHT;

import common.math.Vector2f;
import common.math.Vector2i;
import context.game.visuals.GameCamera;

public class WorldYViewTransformation implements ParticleTransformation {

	private int chunkPos;
	private float pos;
	private GameCamera cam;

	public WorldYViewTransformation(Vector2i chunkPos, Vector2f pos, GameCamera cam) {
		this(chunkPos.y, pos.y, cam);
	}

	public WorldYViewTransformation(int chunkPos, float pos, GameCamera cam) {
		this.chunkPos = chunkPos;
		this.pos = pos;
		this.cam = cam;
	}

	@Override
	public Float apply(int age) {
		return (chunkPos - cam.chunkPos().y) * CHUNK_HEIGHT + pos - cam.pos().y;
	}

}
