package graphics.particle.function;

import static model.tile.TileChunk.CHUNK_PIXEL_WIDTH;

import common.math.Vector2f;
import common.math.Vector2i;
import context.game.visuals.GameCamera;

public class WorldXViewTransformation implements ParticleTransformation {

	private int chunkPos;
	private float pos;
	private GameCamera cam;

	public WorldXViewTransformation(Vector2i chunkPos, Vector2f pos, GameCamera cam) {
		this(chunkPos.x, pos.x, cam);
	}

	public WorldXViewTransformation(int chunkPos, float pos, GameCamera cam) {
		this.chunkPos = chunkPos;
		this.pos = pos;
		this.cam = cam;
	}

	@Override
	public Float apply(int age) {
		return (chunkPos - cam.chunkPos().x) * CHUNK_PIXEL_WIDTH + pos - cam.pos().x;
	}

}
