package engine.visuals.lwjgl.render;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
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
public class VertexBufferObject {

	protected VertexBufferData vboData;
	protected int dimensions;
	protected int index;
	protected int divisor;
	protected int stride;
	protected int offset;

	public VertexBufferObject() {
		this.vboData = new VertexBufferData();
	}

	public VertexBufferObject buffer(VertexBufferData vboData) {
		this.vboData = vboData;
		return this;
	}

	public VertexBufferData buffer() {
		return vboData;
	}

	public VertexBufferObject data(float[] data) {
		this.vboData.data(data);
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

	public VertexBufferObject stride(int stride) {
		this.stride = stride;
		return this;
	}

	public VertexBufferObject offset(int offset) {
		this.offset = offset;
		return this;
	}

	public VertexBufferObject usage(int usage) {
		this.vboData.usage(usage);
		return this;
	}

	public VertexBufferObjectDivisorBuilder divisor() {
		return new VertexBufferObjectDivisorBuilder(this);
	}

	public void divisor(int divisor) {
		this.divisor = divisor;
	}

	public VertexBufferObject load() {
		vboData.load();
		return this;
	}

	public void reallocate() {
		vboData.reallocate();
	}

	public void updateData() {
		vboData.updateData();
	}

	protected void enableVertexAttribArray() {
		vboData.bind();
		int s = stride == 0 ? dimensions * Float.BYTES : stride;
		glVertexAttribPointer(index, dimensions, GL_FLOAT, false, s, offset);
		glEnableVertexAttribArray(index);
		glVertexAttribDivisor(index, divisor);
	}

	public void delete() {
		vboData.delete();
	}

}
