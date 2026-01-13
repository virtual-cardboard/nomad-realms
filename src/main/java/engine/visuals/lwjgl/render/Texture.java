package engine.visuals.lwjgl.render;

import engine.common.math.Vector2f;
import engine.common.math.Vector2i;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.GLContext;
import engine.visuals.lwjgl.ResourcePack;
import engine.visuals.rendering.texture.Image;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * An OpenGL object that represents a 2D image.
 *
 * @author Jay
 */
public class Texture extends GLRegularObject {

	private transient Image image;

	private int width, height;
	private ConstraintBox crop;

	public Texture dimensions(Vector2i dimensions) {
		return dimensions(dimensions.x(), dimensions.y());
	}

	public Texture dimensions(int width, int height) {
		this.width = width;
		this.height = height;
		return this;
	}

	public Texture image(Image image) {
		this.image = image;
		this.width = image.width();
		this.height = image.height();
		return this;
	}

	/**
	 * Binds the texture to texture unit 0. Should only be called if this is the first texture to be bound.
	 *
	 * @param glContext the <code>GLContext</code>
	 */
	public void bind(GLContext glContext) {
		bind(glContext, 0);
	}

	/**
	 * Binds the texture to the given texture unit.
	 *
	 * @param glContext   the <code>GLContext</code>
	 * @param textureUnit the texture unit
	 */
	public void bind(GLContext glContext, int textureUnit) {
		verifyInitialized();
		if (glContext.textureIDs[textureUnit] == id) {
			return;
		}
		glActiveTexture(GL_TEXTURE0 + textureUnit);
		glBindTexture(GL_TEXTURE_2D, id);
		glContext.textureIDs[textureUnit] = id;
	}

	public void bind() {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public Texture load() {
		this.id = glGenTextures();
		initialize();
		glBindTexture(GL_TEXTURE_2D, id);
//		System.out.println(id);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		if (image != null) {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image.data());
		} else {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL);
		}
		glGenerateMipmap(GL_TEXTURE_2D);
		return this;
	}

	/**
	 * Deletes the texture.
	 */
	public void delete() {
		glDeleteTextures(id);
	}

	public void resize(GLContext glContext, int width, int height) {
		bind(glContext);
		this.width = width;
		this.height = height;
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL);
		glGenerateMipmap(GL_TEXTURE_2D);
	}

	public int width() {
		return width;
	}

	public void width(int width) {
		assert image == null : "Cannot set dimensions of texture with image";
		this.width = width;
	}

	public int height() {
		return height;
	}

	public void height(int height) {
		assert image == null : "Cannot set dimensions of texture with image";
		this.height = height;
	}

	public Vector2f dimensions() {
		return new Vector2f(width, height);
	}

	public ConstraintBox crop() {
		return crop;
	}

	public void crop(ConstraintBox crop) {
		this.crop = crop;
	}

	int id() {
		return id;
	}

	@Override
	public void putInto(String name, ResourcePack resourcePack) {
		resourcePack.putTexture(name, this);
	}

}
