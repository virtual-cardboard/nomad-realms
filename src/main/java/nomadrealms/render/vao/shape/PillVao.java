package nomadrealms.render.vao.shape;

import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;

import engine.visuals.lwjgl.render.VertexArrayObject;
import engine.visuals.lwjgl.render.VertexBufferObject;

public class PillVao {

	private static final int RESOLUTION = 32;
	public static final float[] VERTICES = genVertices();

	private static VertexArrayObject instance;

	private PillVao() {
	}

	public static VertexArrayObject instance() {
		if (instance == null) {
			VertexBufferObject vbo = new VertexBufferObject().index(0).data(VERTICES).dimensions(2).load();
			instance = new VertexArrayObject().vbos(vbo).load();
		}
		return instance;
	}

	private static float[] genVertices() {
		float[] vertices = new float[RESOLUTION * 2 * 2 + 4];
		int i = 0;
		// Top semicircle
		for (int j = 0; j <= RESOLUTION; j++) {
			double angle = Math.PI * j / RESOLUTION;
			vertices[i++] = (float) Math.cos(angle) * 0.5f;
			vertices[i++] = (float) Math.sin(angle) * 0.5f + 0.5f;
		}
		// Bottom semicircle
		for (int j = 0; j <= RESOLUTION; j++) {
			double angle = Math.PI + Math.PI * j / RESOLUTION;
			vertices[i++] = (float) Math.cos(angle) * 0.5f;
			vertices[i++] = (float) Math.sin(angle) * 0.5f - 0.5f;
		}
		return vertices;
	}

}
