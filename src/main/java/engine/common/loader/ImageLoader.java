package engine.common.loader;

import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import engine.visuals.rendering.texture.Image;
import org.lwjgl.system.MemoryStack;

/**
 * A loader for loading images from files.
 *
 * @author Lunkle
 */
public class ImageLoader extends FileLoader<Image> {

	public ImageLoader(File file) {
		super(file);
	}

	public static Image loadImage(File file) {
		ImageLoader imageLoader = new ImageLoader(file);
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
			data = stbi_load(getFile().getAbsolutePath(), w, h, comp, 4);
			if (data == null) {
				System.err.println("Failed to load texture at " + getFile().getAbsolutePath() + ".");
				throw new RuntimeException(stbi_failure_reason());
			}
			width = w.get();
			height = h.get();
		}
		return new Image().data(data).width(width).height(height);
	}

}
