package engine.visuals.lwjgl.render;

import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30.glDeleteRenderbuffers;
import static org.lwjgl.opengl.GL30.glGenRenderbuffers;

import engine.visuals.lwjgl.GLContext;

public class RenderBufferObject extends GLRegularObject {

	private final int format;
	private final int formatType;

	public RenderBufferObject(int format, int formatType) {
		this.format = format;
		this.formatType = formatType;
	}

	@Override
	public void genID() {
		this.id = glGenRenderbuffers();
		initialize();
	}

	public void bind(GLContext glContext) {
		verifyInitialized();
		if (glContext.renderBufferID == id) {
			return;
		}
		glBindRenderbuffer(GL_RENDERBUFFER, id);
		glContext.renderBufferID = id;
	}

	public static void unbind(GLContext glContext) {
		if (glContext.renderBufferID == 0) {
			return;
		}
		glBindRenderbuffer(GL_RENDERBUFFER, 0);
		glContext.renderBufferID = 0;
	}

	public void delete() {
		glDeleteRenderbuffers(id);
	}

	public int id() {
		return id;
	}

	public int format() {
		return format;
	}

	public int formatType() {
		return formatType;
	}

}
