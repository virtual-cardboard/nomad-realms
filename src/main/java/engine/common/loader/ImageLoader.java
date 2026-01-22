package engine.common.loader;

import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import engine.visuals.rendering.texture.Image;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

/**
 * A loader for loading images from files.
 *
 * @author Lunkle
 */
public class ImageLoader extends FileLoader<Image> {

	private final String path;

	public ImageLoader(String path) {
		this.path = path;
	}

	public static Image loadImage(String path) {
		ImageLoader imageLoader = new ImageLoader(path);
		return imageLoader.load();
	}

	@Override
	public Image load() {
		ByteBuffer data;
		int width;
		int height;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);
			stbi_set_flip_vertically_on_load(true);
			ByteBuffer imageBuffer = ioResourceToByteBuffer(path);
			data = stbi_load_from_memory(imageBuffer, w, h, comp, 4);
			if (data == null) {
				System.err.println("Failed to load texture at " + path + ".");
				throw new RuntimeException(stbi_failure_reason());
			}
			width = w.get();
			height = h.get();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return new Image().data(data).width(width).height(height);
	}

	private static ByteBuffer ioResourceToByteBuffer(String resource) throws IOException {
		ByteBuffer buffer;
		try (InputStream source = ResourceLoader.getStream(resource)) {
			byte[] bytes = new byte[8192];
			buffer = BufferUtils.createByteBuffer(bytes.length);
			while (true) {
				int n = source.read(bytes);
				if (n == -1) break;
				if (n > buffer.remaining()) {
					ByteBuffer newBuffer = BufferUtils.createByteBuffer(buffer.capacity() * 2);
					buffer.flip();
					newBuffer.put(buffer);
					buffer = newBuffer;
				}
				buffer.put(bytes, 0, n);
			}
		}
		buffer.flip();
		return buffer;
	}

}
