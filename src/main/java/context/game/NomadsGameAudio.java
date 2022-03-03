package context.game;

import context.audio.GameAudio;
import context.audio.lwjgl.AudioClip;
import context.audio.lwjgl.AudioSource;

public class NomadsGameAudio extends GameAudio {

	private AudioSource source;
	private AudioClip peacefulSong;
	private boolean played = false;

	@Override
	protected void init() {
		peacefulSong = resourcePack().getAudioClip("peaceful_song");
		source = new AudioSource();
		source.genID();
		source.setGain(0.00f);
		source.setAudioBuffer(peacefulSong);
	}

	@Override
	public void update() {
		if (!played) {
			source.play();
			played = true;
		}
	}

	@Override
	protected void terminate() {
		source.delete();
		peacefulSong.delete();
		super.terminate();
	}

}
