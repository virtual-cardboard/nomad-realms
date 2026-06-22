package engine.audio;

public class NullAudioPlayer extends AudioPlayer {

	public NullAudioPlayer() {
	}

	@Override
	protected void initOpenAL() {
	}

	@Override
	public void playBackgroundMusic(String filePath) {
	}

	@Override
	public void setVolume(float gain) {
	}

	@Override
	public void stop() {
	}

	@Override
	public void cleanUp() {
	}

}
