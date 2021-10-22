package graphics.shape;

import common.loader.loadtask.ElementBufferObjectLoadTask;
import common.loader.loadtask.VertexBufferObjectLoadTask;
import context.GLContext;

public final class HexagonVertexArrayObject {

	public static final float[] POSITIONS = {
			0.00f, 0.5f, 0,
			0.25f, 1.0f, 0,
			0.75f, 1.0f, 0,
			1.00f, 0.5f, 0,
			0.75f, 0.0f, 0,
			0.25f, 0.0f, 0
	};

	public static final int[] INDICES = {
			0, 1, 5,
			1, 5, 4,
			1, 2, 4,
			2, 3, 4
	};

	public static VertexBufferObjectLoadTask createHexagonVBOLoadTask(GLContext glContext) {
		return new VertexBufferObjectLoadTask(glContext, POSITIONS, 3);
	}

	public static ElementBufferObjectLoadTask createHexagonEBOLoadTask(GLContext glContext) {
		return new ElementBufferObjectLoadTask(glContext, INDICES);
	}

}
