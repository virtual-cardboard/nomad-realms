package graphics;

import context.visuals.lwjgl.ElementBufferObject;
import context.visuals.lwjgl.VertexArrayObject;
import context.visuals.lwjgl.VertexBufferObject;

public final class HexagonVertexArrayObject {

	public static final VertexArrayObject HEXAGON_VAO = new VertexArrayObject();

	private static final float HALF_SQT3 = 0.86602540378f;

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

	public static void createHexagonVAO() {
		HEXAGON_VAO.generateId();
		HEXAGON_VAO.bind();
		ElementBufferObject ebo = new ElementBufferObject(INDICES);
		VertexBufferObject positionsVBO = new VertexBufferObject(POSITIONS, 3);
		ebo.generateId();
		ebo.loadData();
		HEXAGON_VAO.setEbo(ebo);
		positionsVBO.generateId();
		positionsVBO.loadData();
		HEXAGON_VAO.attachVBO(positionsVBO);
		HEXAGON_VAO.enableVertexAttribPointers();
	}

}
