package nomadrealms.render.vao.shape;

import engine.visuals.lwjgl.render.ElementBufferObject;
import engine.visuals.lwjgl.render.VertexArrayObject;
import engine.visuals.lwjgl.render.VertexBufferObject;

public class HexagonVao {

	/**
	 * The side length of the hexagon.
	 */
	public static final float SIDE_LENGTH = 0.5f;
	/**
	 * Half the height of the hexagon.
	 */
	public static final float HEIGHT = 0.433f;

	private static VertexArrayObject vao;

	private HexagonVao() {
	}
	private static final float[] POSITIONS = {
			0, 0, 0,
			-SIDE_LENGTH, 0, 0,
			-SIDE_LENGTH * 0.5f, HEIGHT, 0,
			SIDE_LENGTH * 0.5f, HEIGHT, 0,
			SIDE_LENGTH, 0, 0,
			SIDE_LENGTH * 0.5f, -HEIGHT, 0,
			-SIDE_LENGTH * 0.5f, -HEIGHT, 0,
	};

	private static final float[] TEXTURE_COORDINATES = {
			0.5f, 0.5f,
			0.25f, 0.5f,
			0.125f, 0.25f,
			0.375f, 0.25f,
			0.5f, 0.5f,
			0.375f, 0.75f,
			0.125f, 0.75f,
	};

	private static final int[] INDICES = {
			0, 1, 2,
			0, 2, 3,
			0, 3, 4,
			0, 4, 5,
			0, 5, 6,
			0, 6, 1,
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
