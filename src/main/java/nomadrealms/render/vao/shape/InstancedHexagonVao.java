package nomadrealms.render.vao.shape;

import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.render.vao.shape.HexagonVao.INDICES;
import static nomadrealms.render.vao.shape.HexagonVao.POSITIONS;
import static nomadrealms.render.vao.shape.HexagonVao.TEXTURE_COORDINATES;

import engine.visuals.lwjgl.render.ElementBufferObject;
import engine.visuals.lwjgl.render.InstancedVertexBufferObject;
import engine.visuals.lwjgl.render.VertexArrayObject;
import engine.visuals.lwjgl.render.VertexBufferObject;

public class InstancedHexagonVao {

	public static final int MAX_INSTANCES = CHUNK_SIZE * CHUNK_SIZE;

	private static VertexArrayObject vao;
	private static VertexBufferObject colorVBO;
	private static VertexBufferObject transformVBO;

	private InstancedHexagonVao() {
	}

	public static VertexArrayObject instance() {
		if (vao == null) {
			load();
		}
		return vao;
	}

	public static VertexBufferObject colorVBO() {
		if (colorVBO == null) {
			load();
		}
		return colorVBO;
	}

	public static VertexBufferObject transformVBO() {
		if (transformVBO == null) {
			load();
		}
		return transformVBO;
	}

	private static void load() {
		ElementBufferObject ebo = new ElementBufferObject().indices(INDICES).load();
		VertexBufferObject positionsVBO = new VertexBufferObject().index(0).data(POSITIONS).dimensions(3).load();
		VertexBufferObject textureCoordinatesVBO = new VertexBufferObject().index(1).data(TEXTURE_COORDINATES).dimensions(2).load();

		colorVBO = new InstancedVertexBufferObject().index(2).data(new float[4 * MAX_INSTANCES]).dimensions(4).divisor().perInstance().load();
		transformVBO = new InstancedVertexBufferObject().index(3).data(new float[16 * MAX_INSTANCES]).dimensions(16).divisor().perInstance().load();

		vao = new VertexArrayObject().vbos(positionsVBO, textureCoordinatesVBO, colorVBO, transformVBO).ebo(ebo).load();
	}

}
