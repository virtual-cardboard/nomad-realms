package context.mainmenu;

import context.audio.GameAudio;
import context.audio.lwjgl.AudioSource;
import context.mainmenu.event.MainMenuGuiClickEvent;

public class MainMenuAudio extends GameAudio {

	private AudioSource source;

	@Override
	protected void init() {
		this.source = new AudioSource();
		source.setAudioClip(resourcePack().getAudioClip("sfx_card_flick1"));
		source.setGain(2);
		addHandler(MainMenuGuiClickEvent.class, event -> source.play());
	}

	@Override
	public void update() {
	}

}
