package graphics.particle.function;

import static app.NomadsSettings.WIDTH_TO_HEIGHT_RATIO;
import static model.world.TileChunk.CHUNK_SIDE_LENGTH;

import app.NomadsSettings;
import context.game.visuals.GameCamera;
import math.WorldPos;

public class WorldXViewTransformation implements ParticleTransformation {

	private int chunkPos;
	private int pos;
	private GameCamera cam;
	private final float worldScale;

	public WorldXViewTransformation(WorldPos pos, GameCamera cam, NomadsSettings s) {
		this(pos.chunkPos().x, pos.tilePos().x, cam, s);
	}

	public WorldXViewTransformation(int chunkPos, int tilePos, GameCamera cam, NomadsSettings s) {
		this.chunkPos = chunkPos;
		this.pos = tilePos;
		this.cam = cam;
		worldScale = s.worldScale;
	}

	@Override
	public Float apply(int age) {
		return ((chunkPos - cam.chunkPos().x) * CHUNK_SIDE_LENGTH * worldScale + pos) * 0.75f * WIDTH_TO_HEIGHT_RATIO - cam.pos().x;
	}

}
