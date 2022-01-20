package app;

import static model.world.Tile.WIDTH_TO_HEIGHT_RATIO;
import static model.world.TileChunk.CHUNK_SIDE_LENGTH;

public final class NomadsSettings {

	public static final NomadsSettings SMALL_SETTINGS = new NomadsSettings(36f, 0.75f, 1, 1);
	public static final NomadsSettings DEFAULT_SETTINGS = new NomadsSettings(48f, 1, 1, 1);

	public final float worldScale;
	public final float guiScale;
	public final float musicVolume;
	public final float fxVolume;

	public NomadsSettings(float worldScale, float guiScale, float musicVolume, float fxVolume) {
		this.worldScale = worldScale;
		this.guiScale = guiScale;
		this.musicVolume = musicVolume;
		this.fxVolume = fxVolume;
	}

	public float tileWidth() {
		return WIDTH_TO_HEIGHT_RATIO * worldScale;
	}

	public float tileWidth3_4() {
		return tileWidth() * 0.75f;
	}

	public float tileHeight() {
		return worldScale;
	}

	public float chunkWidth() {
		return CHUNK_SIDE_LENGTH * tileWidth3_4();
	}

	public float chunkHeight() {
		return CHUNK_SIDE_LENGTH * worldScale;
	}

}
