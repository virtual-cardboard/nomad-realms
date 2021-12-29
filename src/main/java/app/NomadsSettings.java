package app;

public final class NomadsSettings {

	public static final NomadsSettings SMALL_SETTINGS = new NomadsSettings(0.036f, 0.75f, 1, 1);
	public static final NomadsSettings DEFAULT_SETTINGS = new NomadsSettings(0.068f, 1, 1, 1);

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

}
