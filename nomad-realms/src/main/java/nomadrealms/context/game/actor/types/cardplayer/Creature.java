package nomadrealms.context.game.actor.types.cardplayer;

import static engine.common.colour.Colour.rgba;
import static engine.visuals.rendering.text.HorizontalAlign.CENTER;
import static engine.visuals.rendering.text.TextFormat.textFormat;
import static engine.visuals.rendering.text.VerticalAlign.TOP;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

import engine.common.math.Vector2f;
import java.util.ArrayList;
import java.util.List;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.ai.CreatureAI;
import nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;

public class Creature extends CardPlayer {

	private String name;
	private String image;

	/**
	 * No-arg constructor for serialization.
	 */
	protected Creature() {
	}

	public Creature(String name, String image, Tile tile, int health) {
		this.name = name;
		this.image = image;
		this.tile(tile);
		this.health(health);
		this.setAi(new CreatureAI(this));
	}

	@Override
	public void update(GameState state) {
		super.update(state);
		if (deckCollection().deck1().getCards().isEmpty()
				&& cardStack().getCards().isEmpty()
				&& !isDestroyed()) {
			damage(health());
		}
	}

	@Override
	public void render(RenderingEnvironment re) {
		float scale = 0.6f * TILE_RADIUS * re.camera.zoom().get();
		Vector2f screenPosition = getScreenPosition(re).vector();
		re.textureRenderer.render(
				re.imageMap.get(image),
				screenPosition.x() - 0.5f * scale,
				screenPosition.y() - 0.7f * scale,
				scale, scale);
		re.textRenderer.render(
				screenPosition.x(),
				screenPosition.y() + 0.1f * scale,
				textFormat()
						.text(name)
						.font(re.font)
						.fontSize(0.4f * scale)
						.colour(rgba(255, 255, 255, (int) (re.actorTextOpacity * 255)))
						.hAlign(CENTER)
						.vAlign(TOP));
		re.textRenderer.render(
				screenPosition.x(),
				screenPosition.y() + 0.5f * scale,
				textFormat()
						.text(health() + " HP")
						.font(re.font)
						.fontSize(0.4f * scale)
						.colour(rgba(255, 255, 255, (int) (re.actorTextOpacity * 255)))
						.hAlign(CENTER)
						.vAlign(TOP));
		super.render(re);
	}

	@Override
	public List<Appendage> appendages() {
		return new ArrayList<>();
	}

	@Override
	public String name() {
		return name;
	}

}
