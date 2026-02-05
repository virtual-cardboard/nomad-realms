package nomadrealms.audio.data;

import java.nio.ShortBuffer;

/**
 * Represents raw PCM audio data ready for playback.
 */
public class AudioData {

    private final ShortBuffer pcm;
    private final int sampleRate;
    private final int channels;

    public AudioData(ShortBuffer pcm, int sampleRate, int channels) {
        this.pcm = pcm;
        this.sampleRate = sampleRate;
        this.channels = channels;
    }

    public ShortBuffer pcm() {
        return pcm;
    }

    public int sampleRate() {
        return sampleRate;
    }

    public int channels() {
        return channels;
    }
}
