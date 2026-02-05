package nomadrealms.render.world;

import static nomadrealms.context.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;
import static nomadrealms.context.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.render.vao.shape.HexagonVao.HEIGHT;
import static nomadrealms.render.vao.shape.HexagonVao.SIDE_LENGTH;

import engine.common.math.Vector2f;
import engine.visuals.lwjgl.render.VertexArrayObject;
import engine.visuals.lwjgl.render.VertexBufferObject;
import nomadrealms.render.vao.shape.HexagonVao;

public class InstancedTileRenderer {

	private static VertexBufferObject tileOffsetsVBO;

	public static VertexArrayObject createChunkVao(VertexBufferObject colorsVBO) {
		if (tileOffsetsVBO == null) {
			initTileOffsetsVBO();
		}
		return new VertexArrayObject()
				.vbos(
						HexagonVao.positionsVBO(),
						HexagonVao.textureCoordinatesVBO(),
						tileOffsetsVBO,
						colorsVBO
				)
				.ebo(HexagonVao.ebo())
				.load();
	}

	private static void initTileOffsetsVBO() {
		float[] offsets = new float[CHUNK_SIZE * CHUNK_SIZE * 2];
		int i = 0;
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int y = 0; y < CHUNK_SIZE; y++) {
				Vector2f pos = calculateTileOffset(x, y);
				offsets[i++] = pos.x();
				offsets[i++] = pos.y();
			}
		}
		tileOffsetsVBO = new VertexBufferObject().index(2).data(offsets).dimensions(2);
		tileOffsetsVBO.divisor(1);
		tileOffsetsVBO.load();
	}

	private static Vector2f calculateTileOffset(int x, int y) {
		Vector2f toCenter = new Vector2f(TILE_RADIUS * SIDE_LENGTH, TILE_RADIUS * HEIGHT);
		Vector2f base = new Vector2f(x * TILE_HORIZONTAL_SPACING, y * TILE_VERTICAL_SPACING);
		Vector2f columnOffset = new Vector2f(0, (x % 2 == 0) ? 0 : TILE_RADIUS * HEIGHT);
		return toCenter.add(base).add(columnOffset);
	}

}
