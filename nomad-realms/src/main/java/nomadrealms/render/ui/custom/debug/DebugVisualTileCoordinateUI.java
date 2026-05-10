package nomadrealms.render.ui.custom.debug;

import static engine.common.colour.Colour.rgb;
import static engine.visuals.rendering.text.HorizontalAlign.CENTER;
import static engine.visuals.rendering.text.TextFormat.textFormat;
import static engine.visuals.rendering.text.VerticalAlign.MIDDLE;
import static nomadrealms.context.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;
import static nomadrealms.context.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.render.vao.shape.HexagonVao.HEIGHT;
import static nomadrealms.render.vao.shape.HexagonVao.SIDE_LENGTH;

import java.util.List;

import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

public class DebugVisualTileCoordinateUI implements UI {

	private final World world;

	public DebugVisualTileCoordinateUI(World world) {
		this.world = world;
	}

	@Override
	public void render(RenderingEnvironment re) {
		List<Chunk> visibleChunks = world.getVisibleChunks(re);
		float zoom = re.camera.zoom().get();
		float cameraPosX = re.camera.position().vector().x();
		float cameraPosY = re.camera.position().vector().y();
		float toCenterX = TILE_RADIUS * SIDE_LENGTH;
		float toCenterY = TILE_RADIUS * HEIGHT;
		for (Chunk chunk : visibleChunks) {
			float chunkPosX = chunk.pos().vector().x();
			float chunkPosY = chunk.pos().vector().y();
			for (int x = 0; x < CHUNK_SIZE; x++) {
				float xOffset = x * TILE_HORIZONTAL_SPACING;
				float columnYOffset = (x % 2 == 0) ? 0 : TILE_RADIUS * HEIGHT;
				for (int y = 0; y < CHUNK_SIZE; y++) {
					Tile tile = chunk.tile(x, y);
					if (tile == null) {
						continue;
					}
					float yOffset = y * TILE_VERTICAL_SPACING;
					float screenX = (chunkPosX + toCenterX + xOffset - cameraPosX) * zoom;
					float screenY = (chunkPosY + toCenterY + yOffset + columnYOffset - cameraPosY) * zoom;
					re.textRenderer.render(screenX, screenY,
							textFormat()
									.text(tile.coord().x() + ", " + tile.coord().y())
									.font(re.font)
									.fontSize(0.35f * TILE_RADIUS * zoom)
									.colour(rgb(255, 255, 255))
									.hAlign(CENTER)
									.vAlign(MIDDLE));
				}
			}
		}
	}

}
