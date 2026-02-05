package engine.visuals.lwjgl.render;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;

import engine.visuals.lwjgl.GLContext;
import engine.visuals.lwjgl.ResourcePack;
import engine.visuals.lwjgl.render.meta.RenderExecutable;

public class FrameBufferObject extends GLContainerObject {

	private Texture texture;
	private RenderBufferObject rbo;
	private GLContext glContext;

	@Override
	public void genID() {
		this.id = glGenFramebuffers();
		initialize();
	}

	public FrameBufferObject load() {
		this.id = glGenFramebuffers();
		initialize();
		glBindFramebuffer(GL_FRAMEBUFFER, id);
		if (texture != null) {
			glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture.id(), 0);
		} else if (rbo != null) {
			glFramebufferRenderbuffer(GL_FRAMEBUFFER, rbo.formatType(), GL_RENDERBUFFER, rbo.id());
		} else {
			throw new RuntimeException("FBO must have a texture or render buffer object attached to it.");
		}
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			throw new RuntimeException("FBO failed to initialize properly, glFramebufferStatus: " + glCheckFramebufferStatus(GL_FRAMEBUFFER));
		}
		return this;
	}

	public FrameBufferObject texture(Texture texture) {
		this.texture = texture;
		return this;
	}

	@Deprecated
	public FrameBufferObject texture(Texture texture, int attachmentType) {
		verifyInitialized();
		glFramebufferTexture2D(GL_FRAMEBUFFER, attachmentType, GL_TEXTURE_2D, texture.id(), 0);
		this.texture = texture;
		return this;
	}

	// Unless for performance reasons, use the texture method instead. I don't fully understand RenderBufferObjects yet.
	public void rbo(RenderBufferObject rbo) {
		verifyInitialized();
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, rbo.formatType(), GL_RENDERBUFFER, rbo.id());
		this.rbo = rbo;
	}

	public void bind(GLContext glContext) {
		verifyInitialized();
		if (glContext.framebufferID == id) {
			return;
		}
		glBindFramebuffer(GL_FRAMEBUFFER, id);
		glContext.framebufferID = id;
	}

	public void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, id);
	}

	public static void unbind(GLContext glContext) {
		if (glContext.framebufferID == 0) {
			return;
		}
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glContext.framebufferID = 0;
	}

	public static void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public void delete() {
		verifyInitialized();
		glDeleteFramebuffers(id);
	}

	public Texture texture() {
		return texture;
	}

	public RenderBufferObject rbo() {
		return rbo;
	}

	@Override
	public void putInto(String name, ResourcePack resourcePack) {
		resourcePack.putFBO(name, this);
	}

	public void render(RenderExecutable renderExecutable) {
		bind(glContext);
		if (texture != null) {
			glViewport(0, 0, texture.width(), texture.height());
		} else if (id == 0) {
			glViewport(0, 0, (int) glContext.fbWidth(), (int) glContext.fbHeight());
		}
		renderExecutable.render();
		unbind(glContext);
		glViewport(0, 0, (int) glContext.fbWidth(), (int) glContext.fbHeight());
	}

	public void render(GLContext glContext, RenderExecutable renderExecutable) {

	}

	public FrameBufferObject glContext(GLContext glContext) {
		this.glContext = glContext;
		return this;
	}

}
