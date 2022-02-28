package app;

import static model.world.AbstractTileChunk.CHUNK_SIDE_LENGTH;

import common.math.Vector2f;

public final class NomadsSettings {

	private static final float WIDTH_TO_HEIGHT_RATIO = 1.1547005f;
	public static final float ENLARGED_CARD_SIZE_FACTOR = 1.2f;

	public final float worldScale; // Default 48
	public final float cardGuiScale; // Default 0.375
	public final float guiScale; // Default 1
	public final float musicVolume; // Default 1
	public final float fxVolume; // Default 1

	public NomadsSettings(float worldScale, float cardGuiScale, float guiScale, float musicVolume, float fxVolume) {
		this.worldScale = worldScale;
		this.cardGuiScale = cardGuiScale;
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

	public float cardWidth() {
		return 640 * cardGuiScale;
	}

	public float cardHeight() {
		return 1024 * cardGuiScale;
	}

	public Vector2f cardDim() {
		return new Vector2f(cardWidth(), cardHeight());
	}

	public float enlargedCardWidth() {
		return cardWidth() * ENLARGED_CARD_SIZE_FACTOR;
	}

	public float enlargedCardHeight() {
		return cardHeight() * ENLARGED_CARD_SIZE_FACTOR;
	}

	public Vector2f enlargedCardDim() {
		return cardDim().scale(ENLARGED_CARD_SIZE_FACTOR);
	}

}
