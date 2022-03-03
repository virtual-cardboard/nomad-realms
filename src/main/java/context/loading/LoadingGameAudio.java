package context.loading;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import context.audio.GameAudio;
import context.audio.lwjgl.AudioClip;
import loading.NomadRealmsAudioBufferLoadTask;

public class LoadingGameAudio extends GameAudio {

	boolean done;

	@Override
	protected void init() {
		Map<String, String> clipMap = new HashMap<>();

		clipMap.put("peaceful_song", "music/peaceful_song.ogg");
		clipMap.put("sfx_card_flick1", "sfx/card_flick1.ogg");
		clipMap.put("sfx_card_flick2", "sfx/card_flick2.ogg");
		clipMap.put("sfx_card_hover", "sfx/card_hover.ogg");
		clipMap.put("sfx_card_unhover", "sfx/card_unhover.ogg");
		clipMap.put("sfx_card_shuffle", "sfx/card_shuffle.ogg");

		Map<String, Future<AudioClip>> fClipMap = new HashMap<>();
		clipMap.forEach((name, path) -> fClipMap.put(name, loader().submit(new NomadRealmsAudioBufferLoadTask(path))));
		fClipMap.forEach((name, fClip) -> {
			try {
				resourcePack().putAudioClip(name, fClip.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		});
		done = true;
	}

	@Override
	public void update() {
	}

}
