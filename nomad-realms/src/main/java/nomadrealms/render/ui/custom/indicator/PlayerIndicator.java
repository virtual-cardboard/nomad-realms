package nomadrealms.render.ui.custom.indicator;

import static engine.visuals.constraint.misc.TimedConstraint.time;
import static engine.visuals.constraint.posdim.SineConstraint.sin;
import static java.lang.Math.PI;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

import engine.common.math.Vector2f;
import engine.visuals.constraint.Constraint;
import engine.visuals.lwjgl.render.Texture;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.render.RenderingEnvironment;

public class PlayerIndicator {

	private final Constraint bob = sin(time().activate().multiply(2 * PI / 4000)).multiply(0.1f * TILE_RADIUS);

	public void render(RenderingEnvironment re, CardPlayer player) {
		if (player == null || player.tile() == null) {
			return;
		}
		Vector2f pos = player.getScreenPosition(re).vector();
		float zoom = re.camera.zoom().get();
		float scale = 0.4f * TILE_RADIUS * zoom;
		Texture indicator = re.imageMap.get("triangle_indicator");
		re.textureRenderer.render(
				indicator,
				pos.x() - 0.5f * scale,
				pos.y() - 0.6f * TILE_RADIUS * zoom + bob.get() * zoom,
				scale, scale * 0.8f);
	}

}
