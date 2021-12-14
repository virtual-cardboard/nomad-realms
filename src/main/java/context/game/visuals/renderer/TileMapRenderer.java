package context.game.visuals.renderer;

import static model.tile.Tile.TILE_HEIGHT;
import static model.tile.Tile.TILE_OUTLINE;
import static model.tile.Tile.TILE_WIDTH;
import static model.tile.TileChunk.CHUNK_PIXEL_HEIGHT;
import static model.tile.TileChunk.CHUNK_PIXEL_WIDTH;
import static model.tile.TileChunk.CHUNK_SIDE_LENGTH;

import context.GLContext;
import context.game.visuals.GameCamera;
import context.game.visuals.renderer.hexagon.HexagonRenderer;
import context.visuals.gui.RootGui;
import context.visuals.renderer.GameRenderer;
import model.tile.Tile;
import model.tile.TileChunk;
import model.tile.TileMap;

public class TileMapRenderer extends GameRenderer {

//	private static final int RENDER_DISTANCE = 2;
	private HexagonRenderer hexagonRenderer;

	public TileMapRenderer(HexagonRenderer hexagonRenderer) {
		this.hexagonRenderer = hexagonRenderer;
	}

	public void renderTiles(GLContext glContext, RootGui rootGui, TileMap map, GameCamera camera) {
//		TODO: render tiles within distance of camera
//		Vector2i cameraChunkPos = camera.chunkPos();
//		List<TileChunk> chunks = new ArrayList<>(); 
//		for (int i = -RENDER_DISTANCE; i < RENDER_DISTANCE; i++) {
//		}
		for (TileChunk chunk : map.chunks()) {
			for (int i = 0; i < CHUNK_SIDE_LENGTH; i++) {
				for (int j = 0; j < CHUNK_SIDE_LENGTH; j++) {
					float x = j * TILE_WIDTH * 0.75f + (chunk.pos().x - camera.chunkPos().x) * CHUNK_PIXEL_WIDTH - camera.pos().x;
					float y = i * TILE_HEIGHT + (j % 2) * TILE_HEIGHT * 0.5f + (chunk.pos().y - camera.chunkPos().y) * CHUNK_PIXEL_HEIGHT - camera.pos().y;
					Tile tile = chunk.tile(j, i);
					int outlineColour = tile.type().outlineColour();
					int colour = tile.type().colour();
					hexagonRenderer.render(glContext, rootGui, x, y, 1, TILE_WIDTH, TILE_HEIGHT, outlineColour);
					hexagonRenderer.render(glContext, rootGui, x + TILE_OUTLINE, y + TILE_OUTLINE, 1, TILE_WIDTH - 2 * TILE_OUTLINE,
							TILE_HEIGHT - 2 * TILE_OUTLINE, colour);
				}
			}
		}
	}

}
