package context.game.audio.handler;

import java.util.function.Consumer;

import common.event.GameEvent;
import context.audio.lwjgl.AudioClip;
import context.audio.lwjgl.AudioSource;

public class PlayAudioHandler<T extends GameEvent> implements Consumer<T> {

	private AudioSource source;
	private AudioClip clip;
	private float gain;

	public PlayAudioHandler(AudioClip clip, float gain) {
		this.clip = clip;
		this.gain = gain;
	}

	@Override
	public void accept(T t) {
		this.source = new AudioSource();
		source.setGain(gain);
		source.setAudioClip(clip);
		source.play();
	}

}
