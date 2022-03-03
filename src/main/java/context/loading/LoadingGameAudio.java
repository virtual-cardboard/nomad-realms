package context.loading;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import context.audio.GameAudio;
import context.audio.lwjgl.AudioClip;
import loading.NomadRealmsAudioBufferLoadTask;

public class LoadingGameAudio extends GameAudio {

	boolean done;

	@Override
	protected void init() {
		Future<AudioClip> fBuffer = loader().submit(new NomadRealmsAudioBufferLoadTask("music/peaceful_song.ogg"));
		try {
			AudioClip peacefulSong = fBuffer.get();
			resourcePack().putAudioClip("peaceful_song", peacefulSong);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		done = true;
	}

	@Override
	public void update() {
	}

}
