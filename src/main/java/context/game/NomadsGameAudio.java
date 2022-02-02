package context.game;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import context.audio.GameAudio;
import context.audio.lwjgl.AudioBuffer;
import context.audio.lwjgl.AudioSource;
import loading.NomadRealmsAudioBufferLoadTask;

public class NomadsGameAudio extends GameAudio {

	private AudioSource source;
	private AudioBuffer buffer;
	private boolean played = false;

	@Override
	protected void init() {
		Future<AudioBuffer> fBuffer = loader().submit(new NomadRealmsAudioBufferLoadTask("music/peaceful_song.ogg"));
		try {
			buffer = fBuffer.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		source = new AudioSource();
		source.genID();
		source.setAudioBuffer(buffer);
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
		System.out.println("Terminating");
		source.delete();
		buffer.delete();
		// TODO Auto-generated method stub
		super.terminate();
	}

}
