package engine.visuals.lwjgl.render;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

public class VertexBufferData extends GLRegularObject {

	private float[] data;
	private int usage = GL_STATIC_DRAW;

	public int id() {
		return id;
	}

	public void bind() {
		glBindBuffer(GL_ARRAY_BUFFER, id);
	}

	public VertexBufferData data(float[] data) {
		this.data = data;
		return this;
	}

	public VertexBufferData usage(int usage) {
		this.usage = usage;
		return this;
	}

	public VertexBufferData load() {
		return load(glGenBuffers());
	}

	public VertexBufferData load(int id) {
		this.id = id;
		bind();
		glBufferData(GL_ARRAY_BUFFER, data, usage);
		initialize();
		return this;
	}

	public void reallocate() {
		bind();
		glBufferData(GL_ARRAY_BUFFER, data, usage);
	}

	public void updateData() {
		bind();
		glBufferSubData(GL_ARRAY_BUFFER, 0, data);
	}

	public void delete() {
		glDeleteBuffers(id);
	}

	public float[] data() {
		return data;
	}
}
