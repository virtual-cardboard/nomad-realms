package app;

import static model.world.TileChunk.CHUNK_SIDE_LENGTH;

public final class NomadsSettings {

	public static final float WIDTH_TO_HEIGHT_RATIO = 1.1547005f;

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
