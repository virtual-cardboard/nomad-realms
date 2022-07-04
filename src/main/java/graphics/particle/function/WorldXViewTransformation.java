package graphics.particle.function;

import static model.world.chunk.AbstractTileChunk.CHUNK_SIDE_LENGTH;

import app.NomadsSettings;
import context.game.visuals.GameCamera;
import math.WorldPos;

public class WorldXViewTransformation implements ParticleTransformation {

	private int chunkPos;
	private int pos;
	private GameCamera cam;
	private final float tileWidth3_4;

	public WorldXViewTransformation(WorldPos pos, GameCamera cam, NomadsSettings s) {
		this(pos.chunkPos().x(), pos.tilePos().x(), cam, s);
	}

	public WorldXViewTransformation(int chunkPos, int tilePos, GameCamera cam, NomadsSettings s) {
		this.chunkPos = chunkPos;
		this.pos = tilePos;
		this.cam = cam;
		tileWidth3_4 = s.tileWidth3_4();
	}

	@Override
	public Float apply(int age) {
		return ((chunkPos - cam.chunkPos().x()) * CHUNK_SIDE_LENGTH + pos) * tileWidth3_4 - cam.pos().x();
	}

}
