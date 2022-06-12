package context.game.audio.handler;

import context.audio.lwjgl.AudioClip;
import context.game.NomadsGameData;
import event.logicprocessing.CardPlayedEvent;

public class CurrentPlayerPlayCardEventAudioSyncHandler extends PlayAudioHandler<CardPlayedEvent> {

	private NomadsGameData data;

	public CurrentPlayerPlayCardEventAudioSyncHandler(NomadsGameData data, AudioClip clip, float gain) {
		super(clip, gain);
		this.data = data;
	}

	@Override
	public void accept(CardPlayedEvent t) {
		if (data.playerID().equals(t.playerID())) {
			super.accept(t);
		}
	}

}
