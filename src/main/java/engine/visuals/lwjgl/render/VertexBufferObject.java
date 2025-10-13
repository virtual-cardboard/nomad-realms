package engine.visuals.lwjgl.render;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

import engine.visuals.lwjgl.render.vbo.VertexBufferObjectDivisorBuilder;

/**
 * An object that contains data about the available vertices able to be used in a {@link VertexArrayObject}. Use
 * {@link ElementBufferObject} to determine which vertices to use.
 *
 * @author Jay
 */
public class VertexBufferObject extends GLRegularObject {

	protected float[] data;
	protected int dimensions;
	protected int index;
	private int divisor;

	@Override
	public void genID() {
		// TODO: Delete
	}

	private void bind() {
		glBindBuffer(GL_ARRAY_BUFFER, id);
	}

	public VertexBufferObject data(float[] data) {
		this.data = data;
		return this;
	}

	public VertexBufferObject dimensions(int dimensions) {
		this.dimensions = dimensions;
		return this;
	}

	public VertexBufferObject index(int index) {
		this.index = index;
		return this;
	}

	public VertexBufferObjectDivisorBuilder divisor() {
		return new VertexBufferObjectDivisorBuilder(this);
	}

	public void divisor(int divisor) {
		this.divisor = divisor;
	}

	public VertexBufferObject load() {
		id = glGenBuffers();
		bind();
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
		return this;
	}

	protected void enableVertexAttribArray() {
		bind();
		glVertexAttribPointer(index, dimensions, GL_FLOAT, false, dimensions * Float.BYTES, 0);
		glEnableVertexAttribArray(index);
		glVertexAttribDivisor(index, divisor);
	}

	public void updateData() {
		bind();
		glBufferSubData(GL_ARRAY_BUFFER, 0, data);
	}

	public void delete() {
		glDeleteBuffers(id);
	}

}
