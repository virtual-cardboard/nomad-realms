package nomadrealms.render.ui.custom.indicator;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;

public class PlayerIndicator {

	public void render(RenderingEnvironment re, CardPlayer player) {
		if (player == null || player.tile() == null) {
			return;
		}
		engine.common.math.Vector2f pos = player.getScreenPosition(re).vector();
		float scale = 0.4f * Tile.TILE_RADIUS * re.camera.zoom().get();
		re.textureRenderer.render(
				re.imageMap.get("triangle_indicator"),
				pos.x() - 0.5f * scale,
				pos.y() - 1.2f * Tile.TILE_RADIUS * re.camera.zoom().get(),
				scale, scale);
	}

}
