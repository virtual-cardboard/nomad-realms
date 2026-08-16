package engine.visuals.builtin;

import engine.visuals.lwjgl.render.ElementBufferObject;
import engine.visuals.lwjgl.render.VertexArrayObject;
import engine.visuals.lwjgl.render.VertexBufferObject;

/**
 * A {@link VertexArrayObject} that represents a rectangle.
 *
 * @author Lunkle
 */
public class RectangleVertexArrayObject {

	private static VertexArrayObject vao;

	private RectangleVertexArrayObject() {
	}

	private static final float[] POSITIONS = {
			1, 1, 0,
			1, 0, 0,
			0, 0, 0,
			0, 1, 0,
	};

	private static final float[] TEXTURE_COORDINATES = {
			1, 0,
			1, 1,
			0, 1,
			0, 0,
	};

	private static final int[] INDICES = {
			0, 1, 2,
			0, 2, 3
	};

	public static VertexArrayObject instance() {
		if (vao == null) {
			vao = newInstance().load();
		}
		return vao;
	}

	/**
	 * This version of the method is used to create a new {@link VertexArrayObject} every time it is called. It should
	 * only be necessary to call this method if you need to mutate the {@link VertexArrayObject} in some way. Otherwise,
	 * prefer to use the {@link #instance()} method instead.
	 * <br><br>
	 * Note that this method does not load the {@link VertexArrayObject}. You must call
	 * {@link VertexArrayObject#load() load()} on the returned {@link VertexArrayObject}.
	 *
	 * @return
	 */
	public static VertexArrayObject newInstance() {
		ElementBufferObject ebo = new ElementBufferObject().indices(INDICES).load();
		VertexBufferObject positionsVBO = new VertexBufferObject().index(0).data(POSITIONS).dimensions(3).load();
		VertexBufferObject textureCoordinatesVBO = new VertexBufferObject().index(1).data(TEXTURE_COORDINATES).dimensions(2).load();
		return new VertexArrayObject().vbos(positionsVBO, textureCoordinatesVBO).ebo(ebo);
	}

}
