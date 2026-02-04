package nomadrealms.audio;

import nomadrealms.audio.data.AudioData;
import nomadrealms.audio.loader.Mp3Loader;
import nomadrealms.audio.loader.OggLoader;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.system.MemoryUtil.NULL;

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
