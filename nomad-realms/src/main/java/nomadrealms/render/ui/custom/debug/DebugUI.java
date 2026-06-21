package nomadrealms.render.ui.custom.debug;

import static engine.common.colour.Colour.rgb;
import static engine.visuals.rendering.text.HorizontalAlign.CENTER;
import static engine.visuals.rendering.text.HorizontalAlign.LEFT;
import static engine.visuals.rendering.text.TextFormat.textFormat;
import static engine.visuals.rendering.text.VerticalAlign.MIDDLE;
import static engine.visuals.rendering.text.VerticalAlign.TOP;
import static nomadrealms.context.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;
import static nomadrealms.context.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.render.vao.shape.HexagonVao.HEIGHT;
import static nomadrealms.render.vao.shape.HexagonVao.SIDE_LENGTH;

import java.util.ArrayList;
import java.util.List;

import engine.common.math.Matrix4f;
import engine.common.time.FPSCounter;
import engine.common.time.PerformanceProfiler;
import nomadrealms.context.game.world.World;
import engine.visuals.rendering.text.TextFormat;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

public class DebugUI implements UI {

	private final World world;
	private final FPSCounter fpsCounter = new FPSCounter(100);
	private final PerformanceChartUI performanceChartUI;

	public DebugUI(World world, PerformanceProfiler profiler) {
		this.world = world;
		this.performanceChartUI = new PerformanceChartUI(profiler);
	}

	public void update() {
		fpsCounter.update();
	}

	@Override
	public void render(RenderingEnvironment re) {
		re.textRenderer.render(
				textFormat()
						.text(String.format("FPS: %.1f", fpsCounter.getFPS()))
						.font(re.font)
						.fontSize(20)
						.colour(rgb(255, 255, 255))
						.hAlign(LEFT)
						.vAlign(TOP)
						.transform(re.textRenderer.screenToPixel()).pixelPosition(20, 20));
		performanceChartUI.render(re);
		float zoom = re.is.camera.zoom().get();
		if (zoom < 0.2f) {
			return;
		}
		List<Chunk> visibleChunks = world.getVisibleChunks(re);
		float cameraPosX = re.is.camera.position().vector().x();
		float cameraPosY = re.is.camera.position().vector().y();
		float toCenterX = TILE_RADIUS * SIDE_LENGTH;
		float toCenterY = TILE_RADIUS * HEIGHT;
		List<TextFormat> tileCoords = new ArrayList<>();
		Matrix4f screenToPixel = re.textRenderer.screenToPixel();
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
					tileCoords.add(textFormat()
							.text(tile.coord().x() + ", " + tile.coord().y())
							.font(re.font)
							.fontSize(0.35f * TILE_RADIUS * zoom)
							.colour(rgb(255, 255, 255))
							.hAlign(CENTER)
							.vAlign(MIDDLE)
							.transform(screenToPixel).pixelPosition(screenX, screenY));
				}
			}
		}
		re.textRenderer.render(tileCoords);
	}

}
