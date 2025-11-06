package engine.visuals.rendering.texture;

import static org.lwjgl.stb.STBImage.stbi_image_free;

import java.nio.ByteBuffer;

/**
 * A class that represents an image.
 *
 * @author Lunkle
 */
public class Image {

	private ByteBuffer data;
	private int width;
	private int height;

	public Image data(ByteBuffer data) {
		this.data = data;
		return this;
	}

	public Image width(int width) {
		this.width = width;
		return this;
	}

	public Image height(int height) {
		this.height = height;
		return this;
	}

	public ByteBuffer data() {
		return data;
	}

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}

	public void delete() {
		stbi_image_free(data);
		data = null;
	}

}
