package graphics.particle.function;

import static model.world.TileChunk.CHUNK_SIDE_LENGTH;

import app.NomadsSettings;
import context.game.visuals.GameCamera;
import math.WorldPos;

public class WorldYViewTransformation implements ParticleTransformation {

	private int chunkPos;
	private int pos;
	private GameCamera cam;
	private final float tileHeight;

	public WorldYViewTransformation(WorldPos pos, GameCamera cam, NomadsSettings s) {
		this(pos.chunkPos().y, pos.tilePos().y, cam, s);
	}

	public WorldYViewTransformation(int chunkPos, int tilePos, GameCamera cam, NomadsSettings s) {
		this.chunkPos = chunkPos;
		this.pos = tilePos;
		this.cam = cam;
		tileHeight = s.tileHeight();
	}

	@Override
	public Float apply(int age) {
		return ((chunkPos - cam.chunkPos().y) * CHUNK_SIDE_LENGTH + pos) * tileHeight - cam.pos().y;
	}

}
