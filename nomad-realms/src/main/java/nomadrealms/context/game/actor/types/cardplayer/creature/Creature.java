package nomadrealms.context.game.actor.types.cardplayer.creature;

import static engine.common.colour.Colour.rgba;
import static engine.visuals.rendering.text.HorizontalAlign.CENTER;
import static engine.visuals.rendering.text.TextFormat.textFormat;
import static engine.visuals.rendering.text.VerticalAlign.TOP;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

import engine.common.math.Vector2f;
import java.util.Collections;
import java.util.List;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage;
import nomadrealms.render.RenderingEnvironment;

public class Creature extends CardPlayer {

	private String name;
	private String imageName;

	public Creature(String name, String imageName, int health, int mana) {
		this.name = name;
		this.imageName = imageName;
		this.health(health);
		this.mana(mana);
		this.maxMana(mana);
		this.setAi(new CreatureAI(this));
	}

	@Override
	public void update(GameState state) {
		super.update(state);
		if (deckCollection().deck1().size() == 0 && cardStack().getCards().isEmpty()) {
			health(0);
		}
	}

	@Override
	public void render(RenderingEnvironment re) {
		float scale = 0.6f * TILE_RADIUS * re.camera.zoom().get();
		Vector2f screenPosition = getScreenPosition(re).vector();
		re.textureRenderer.render(
				re.imageMap.get(imageName),
				screenPosition.x() - 0.5f * scale,
				screenPosition.y() - 0.7f * scale,
				scale, scale);
		re.textRenderer.render(
				screenPosition.x(),
				screenPosition.y() + 0.1f * scale,
				textFormat()
						.text(name)
						.font(re.font)
						.fontSize(0.5f * scale)
						.colour(rgba(255, 255, 255, (int) (re.actorTextOpacity * 255)))
						.hAlign(CENTER)
						.vAlign(TOP));
		re.textRenderer.render(
				screenPosition.x(),
				screenPosition.y() + 0.5f * scale,
				textFormat()
						.text(health() + " HP")
						.font(re.font)
						.fontSize(0.5f * scale)
						.colour(rgba(255, 255, 255, (int) (re.actorTextOpacity * 255)))
						.hAlign(CENTER)
						.vAlign(TOP));
		super.render(re);
	}

	@Override
	public List<Appendage> appendages() {
		return Collections.emptyList();
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String imageName() {
		return imageName;
	}

}
