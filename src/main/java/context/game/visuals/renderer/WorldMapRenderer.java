package context.game.visuals.renderer;

import static context.game.visuals.GameCamera.RENDER_RADIUS;
import static model.world.TileChunk.CHUNK_SIDE_LENGTH;

import app.NomadsSettings;
import common.math.Vector2f;
import common.math.Vector2i;
import context.GLContext;
import context.game.visuals.GameCamera;
import context.game.visuals.renderer.hexagon.HexagonRenderer;
import context.visuals.renderer.GameRenderer;
import model.world.Tile;
import model.world.WorldMap;
import model.world.layer.finallayer.FinalLayerChunk;

public class WorldMapRenderer extends GameRenderer {

	private HexagonRenderer hexagonRenderer;

	public WorldMapRenderer(HexagonRenderer hexagonRenderer) {
		this.hexagonRenderer = hexagonRenderer;
	}

	public WorldMapRenderer(GLContext glContext, HexagonRenderer hexagonRenderer) {
		super(glContext);
		this.hexagonRenderer = hexagonRenderer;
	}

	public void renderMap(NomadsSettings s, WorldMap map, GameCamera camera) {
		Vector2i cameraChunkPos = camera.chunkPos();
		for (int cy = -RENDER_RADIUS; cy <= RENDER_RADIUS; cy++) {
			for (int cx = -RENDER_RADIUS; cx <= RENDER_RADIUS; cx++) {
				FinalLayerChunk chunk = map.finalLayerChunk(cameraChunkPos.add(cx, cy));
				if (chunk != null) {
					renderChunk(s, camera, chunk);
				}
			}
		}
	}

	private void renderChunk(NomadsSettings s, GameCamera camera, FinalLayerChunk chunk) {
		for (int i = 0; i < CHUNK_SIDE_LENGTH; i++) {
			for (int j = 0; j < CHUNK_SIDE_LENGTH; j++) {
				Tile tile = chunk.tile(j, i);
				Vector2f screenPos = tile.worldPos().screenPos(camera, s);
				float x = screenPos.x - s.tileWidth() / 2;
				float y = screenPos.y - s.tileHeight() / 2;
				int outlineColour = tile.type().outlineColour();
				int colour = tile.type().colour();
				hexagonRenderer.render(x, y, s.tileWidth(), s.tileHeight(),
						outlineColour);
				hexagonRenderer.render(x + 3, y + 3, s.tileWidth() - 6, s.tileHeight() - 6,
						colour);
			}
		}
	}

}
