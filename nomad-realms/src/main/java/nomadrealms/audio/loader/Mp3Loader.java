package nomadrealms.audio.loader;

import engine.common.loader.ResourceLoader;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;
import nomadrealms.audio.data.AudioData;
import org.lwjgl.BufferUtils;

import java.io.InputStream;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads MP3 files and converts them to raw PCM data using JLayer.
 */
public class Mp3Loader extends ResourceLoader<AudioData> {

    public Mp3Loader(String path) {
        super(path);
    }

    @Override
    public AudioData load() {
        try (InputStream inputStream = Mp3Loader.class.getResourceAsStream(path())) {
            if (inputStream == null) {
                System.err.println("Resource not found: " + path());
                return null;
            }

            Bitstream bitstream = new Bitstream(inputStream);
            Decoder decoder = new Decoder();

            List<short[]> chunks = new ArrayList<>();
            int totalSamples = 0;
            int sampleRate = 44100;
            int channels = 2;
            boolean first = true;

            try {
                Header h;
                while ((h = bitstream.readFrame()) != null) {
                    if (first) {
                        sampleRate = h.frequency();
                        channels = (h.mode() == Header.SINGLE_CHANNEL) ? 1 : 2;
                        first = false;
                    }

                    SampleBuffer output = (SampleBuffer) decoder.decodeFrame(h, bitstream);
                    int len = output.getBufferLength();
                    short[] pcm = output.getBuffer();

                    // Copy valid samples
                    short[] copy = new short[len];
                    System.arraycopy(pcm, 0, copy, 0, len);
                    chunks.add(copy);
                    totalSamples += len;

                    bitstream.closeFrame();
                }
            } catch (Exception e) {
                // End of stream or error
                // JLayer often throws ArrayIndexOutOfBounds or similar at EOF if file is truncated
            } finally {
                bitstream.close();
            }

            if (totalSamples == 0) {
                return null;
            }

            ShortBuffer directBuffer = BufferUtils.createShortBuffer(totalSamples);
            for (short[] chunk : chunks) {
                directBuffer.put(chunk);
            }
            directBuffer.flip();

            return new AudioData(directBuffer, sampleRate, channels);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
