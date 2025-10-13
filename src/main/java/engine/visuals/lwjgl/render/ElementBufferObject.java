package engine.visuals.lwjgl.render;

import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import engine.visuals.lwjgl.GLContext;

/**
 * An object that tells OpenGL which vertices in a {@link VertexBufferObject} to display.
 *
 * @author Jay
 * @see VertexArrayObject
 */
public class ElementBufferObject extends GLRegularObject {

	private int[] data;

	public ElementBufferObject() {
	}

	@Override
	public void genID() {
		this.id = glGenBuffers();
		initialize();
	}

	public ElementBufferObject indices(final int[] indices) {
		this.data = indices;
		return this;
	}

	public ElementBufferObject load() {
		this.id = glGenBuffers();
		initialize();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW);
		return this;
	}

	public void delete() {
		glDeleteBuffers(id);
	}

	protected void bind(GLContext glContext) {
		verifyInitialized();
		if (glContext.bufferID == id) {
			return;
		}
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
		glContext.bufferID = id;
	}

	public int size() {
		return data.length;
	}

}
