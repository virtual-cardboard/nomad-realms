package engine.audio;

/**
 * A dummy {@link AudioPlayer} implementation that bypasses OpenAL initialization
 * for headless testing environments without audio hardware.
 */
public class NullAudioPlayer extends AudioPlayer {

	@Override
	protected void initOpenAL() {
		// Do nothing: bypass OpenAL hardware initialization
	}

}
