package graphics.shape;

import context.visuals.lwjgl.ElementBufferObject;
import context.visuals.lwjgl.VertexArrayObject;
import context.visuals.lwjgl.VertexArrayObjectBuilder;
import context.visuals.lwjgl.VertexBufferObject;

public final class HexagonVertexArrayObject {

	private static VertexArrayObject hexagonVAO;

	private static final float HALF_SQT3 = (float) (Math.sqrt(3) / 2);

	private static final float[] POSITIONS = {
			-1.0f, 0.0f, 0.0f,
			-0.5f, HALF_SQT3, 0.0f,
			0.5f, HALF_SQT3, 0.0f,
			1.0f, 0.0f, 0.0f,
			0.5f, -HALF_SQT3, 0.0f,
			-0.5f, -HALF_SQT3, 0.0f,
	};

	private static final int[] INDICES = {
			0, 1, 5,
			1, 5, 4,
			1, 2, 4,
			2, 3, 4
	};

	/**
	 * The VAO must be created before the getter is called
	 */
	public static void createHexagonVAO() {
		ElementBufferObject ebo = new ElementBufferObject(INDICES);
		VertexBufferObject positionsVBO = new VertexBufferObject(POSITIONS, 3);
		hexagonVAO = new VertexArrayObjectBuilder(ebo, positionsVBO).build();
	}

	public static final VertexArrayObject getHexagonVAO() {
		return hexagonVAO;
	}

}
