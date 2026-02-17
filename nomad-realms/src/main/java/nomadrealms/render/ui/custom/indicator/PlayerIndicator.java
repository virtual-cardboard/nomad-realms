package nomadrealms.render.ui.custom.indicator;

import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

import engine.common.math.Vector2f;
import engine.visuals.lwjgl.render.Texture;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.render.RenderingEnvironment;

public class PlayerIndicator {

	public void render(RenderingEnvironment re, CardPlayer player) {
		if (player == null || player.tile() == null) {
			return;
		}
		Vector2f pos = player.getScreenPosition(re).vector();
		float scale = 0.4f * TILE_RADIUS * re.camera.zoom().get();
		Texture indicator = re.imageMap.get("triangle_indicator");
		re.textureRenderer.render(
				indicator,
				pos.x() - 0.5f * scale,
				pos.y() - 0.6f * TILE_RADIUS * re.camera.zoom().get(),
				scale, scale * 0.8f);
	}

}
