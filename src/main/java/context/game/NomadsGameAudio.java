package context.game;

import context.audio.GameAudio;
import context.audio.lwjgl.AudioClip;
import context.audio.lwjgl.AudioSource;
import context.game.audio.handler.CurrentPlayerPlayCardEventAudioSyncHandler;
import event.game.logicprocessing.CardPlayedEvent;

public class NomadsGameAudio extends GameAudio {

	private AudioSource source;
	private AudioClip peacefulSong;
	private boolean played = false;

	@Override
	protected void init() {
		peacefulSong = resourcePack().getAudioClip("peaceful_song");
		AudioClip cardFlick2 = resourcePack().getAudioClip("sfx_card_flick2");
		source = new AudioSource();
		source.setGain(0.00f);
		source.setAudioClip(peacefulSong);
		NomadsGameData data = (NomadsGameData) context().data();
		addHandler(CardPlayedEvent.class, new CurrentPlayerPlayCardEventAudioSyncHandler(data, cardFlick2, 0.4f));
	}

	@Override
	public void update() {
//		if (!played) {
//			source.play();
//			played = true;
//		}
	}

	@Override
	protected void terminate() {
		source.delete();
		peacefulSong.delete();
		super.terminate();
	}

}
