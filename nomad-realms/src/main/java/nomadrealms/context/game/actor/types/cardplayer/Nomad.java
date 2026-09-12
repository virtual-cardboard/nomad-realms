package nomadrealms.context.game.actor.types.cardplayer;

import static engine.common.colour.Colour.rgba;
import static java.util.Arrays.asList;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.ARM;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.EYE;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.HEAD;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.LEG;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.TORSO;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

import java.util.List;

import engine.common.math.Vector2f;
import static engine.visuals.rendering.text.HorizontalAlign.CENTER;
import static engine.visuals.rendering.text.VerticalAlign.TOP;
import engine.serialization.Derializable;
import nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage;

import static engine.visuals.rendering.text.TextFormat.textFormat;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;

@Derializable
public class Nomad extends CardPlayer {

	/**
	 * No-arg constructor for serialization.
	 */
	protected Nomad() {
		this.name = "Nomad";
	}

	public Nomad(String name, Tile tile) {
		this.name = name;
		this.tile(tile);
		this.health(10);
	}

	@Override
	public void render(RenderingEnvironment re) {
		float scale = 0.6f * TILE_RADIUS * re.is.camera.zoom().get();
		Vector2f screenPosition = getScreenPosition(re).vector();
		re.textureRenderer.render(
				re.imageMap.get("nomad"),
				screenPosition.x() - 0.5f * scale,
				screenPosition.y() - 0.7f * scale,
				scale, scale);
		re.textRenderer.render(
				textFormat()
						.text(name)
						.font(re.font)
						.fontSize(0.5f * scale)
						.colour(rgba(255, 255, 255, (int) (re.is.actorTextOpacity * 255)))
						.hAlign(CENTER)
						.vAlign(TOP)
						.transform(re.textRenderer.screenToPixel().copy().translate(screenPosition.x(), screenPosition.y() + 0.1f * scale)));
		re.textRenderer.render(
				textFormat()
						.text(health() + " HP")
						.font(re.font)
						.fontSize(0.5f * scale)
						.colour(rgba(255, 255, 255, (int) (re.is.actorTextOpacity * 255)))
						.hAlign(CENTER)
						.vAlign(TOP)
						.transform(re.textRenderer.screenToPixel().copy().translate(screenPosition.x(), screenPosition.y() + 0.5f * scale)));
		super.render(re);
	}

	@Override
	public String imageName() {
		return "nomad";
	}

	public float imageScale() {
		return 0.6f;
	}

	@Override
	public List<Appendage> appendages() {
		return asList(HEAD, EYE, EYE, TORSO, ARM, ARM, LEG, LEG);
	}

}
