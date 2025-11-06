package engine.visuals.lwjgl.render.vbo;

import engine.visuals.lwjgl.render.VertexBufferObject;

/**
 * Builder class for setting the divisor of a {@link VertexBufferObject}.
 * <p>
 * By default, the divisor is 0, meaning that the data is per vertex. If the divisor is 1, the data is per instance. Per
 * instance is only necessary when using instanced rendering. If you are not using instanced rendering, you do not need
 * to use this class.
 *
 * @author Lunkle
 */
public class VertexBufferObjectDivisorBuilder {

	private final VertexBufferObject vbo;

	public VertexBufferObjectDivisorBuilder(VertexBufferObject vbo) {
		this.vbo = vbo;
	}

	public VertexBufferObject perVertex() {
		vbo.divisor(0);
		return vbo;
	}

	public VertexBufferObject perInstance() {
		vbo.divisor(1);
		return vbo;
	}

}
