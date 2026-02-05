package nomadrealms.audio;

import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.openal.AL10.AL_GAIN;
import static org.lwjgl.openal.AL10.AL_LOOPING;
import static org.lwjgl.openal.AL10.AL_TRUE;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.AL10.alDeleteSources;
import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alSourcef;
import static org.lwjgl.openal.AL10.alSourcei;
import static org.lwjgl.openal.ALC10.ALC_DEFAULT_DEVICE_SPECIFIER;
import static org.lwjgl.openal.ALC10.alcCloseDevice;
import static org.lwjgl.openal.ALC10.alcCreateContext;
import static org.lwjgl.openal.ALC10.alcDestroyContext;
import static org.lwjgl.openal.ALC10.alcGetString;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.openal.ALC10.alcOpenDevice;
import static org.lwjgl.system.MemoryUtil.NULL;

import nomadrealms.audio.data.AudioData;
import nomadrealms.audio.loader.Mp3Loader;
import nomadrealms.audio.loader.OggLoader;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

public class MusicPlayer {

	private long device;
	private long context;
	private int source;
	private int buffer;

	public MusicPlayer() {
		initOpenAL();
	}

	private void initOpenAL() {
		String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
		device = alcOpenDevice(defaultDeviceName);
		int[] attributes = {0};
		context = alcCreateContext(device, attributes);
		alcMakeContextCurrent(context);
		ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
		ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

		if (!alCapabilities.OpenAL10) {
			System.err.println("OpenAL 1.0 not supported");
		}
	}

	// This implementation loads the entire audio file into a memory buffer before playing. While simple, this can
	// consume a large amount of memory for long background music tracks. For better memory efficiency, consider
	// implementing audio streaming. This would involve loading and playing the audio in smaller chunks, which is a more
	// standard approach for background music and would improve scalability.
	public void playBackgroundMusic(String filePath) {
		stop(); // Stop any currently playing music

		AudioData audioData;
		if (filePath.toLowerCase().endsWith(".ogg")) {
			audioData = new OggLoader(filePath).load();
		} else if (filePath.toLowerCase().endsWith(".mp3")) {
			audioData = new Mp3Loader(filePath).load();
		} else {
			System.err.println("Unsupported audio format: " + filePath);
			return;
		}

		if (audioData == null) {
			System.err.println("Failed to load audio: " + filePath);
			return;
		}

		// Create AL buffer
		buffer = alGenBuffers();
		int format = (audioData.channels() == 1) ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16;
		alBufferData(buffer, format, audioData.pcm(), audioData.sampleRate());

		// Create AL source
		source = alGenSources();
		alSourcei(source, AL_BUFFER, buffer);
		alSourcei(source, AL_LOOPING, AL_TRUE);
		alSourcePlay(source);
	}

	public void setVolume(float gain) {
		if (source != 0) {
			alSourcef(source, AL_GAIN, gain);
		}
	}

	public void stop() {
		if (source != 0) {
			alSourceStop(source);
			alDeleteSources(source);
			source = 0;
		}
		if (buffer != 0) {
			alDeleteBuffers(buffer);
			buffer = 0;
		}
	}

	public void cleanUp() {
		stop();
		if (context != NULL) {
			alcDestroyContext(context);
			context = NULL;
		}
		if (device != NULL) {
			alcCloseDevice(device);
			device = NULL;
		}
	}

}
