package nomadrealms.context.game.actor.types.cardplayer.creature;

import engine.common.math.Vector2f;
import engine.serialization.Derializable;
import java.util.List;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.ai.CreatureAI;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage;
import nomadrealms.context.game.card.collection.DeckList;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;

import static engine.common.colour.Colour.rgba;
import static engine.visuals.rendering.text.HorizontalAlign.CENTER;
import static engine.visuals.rendering.text.TextFormat.textFormat;
import static java.util.Collections.emptyList;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

@Derializable
public class Creature extends CardPlayer {

	private String image;

	/**
	 * No-arg constructor for serialization.
	 */
	protected Creature() {
	}

	public Creature(String name, Tile tile, int health, int mana, String image, DeckList deck) {
		this.name = name;
		this.tile(tile);
		this.health(health);
		this.mana(mana);
		this.maxMana(mana);
		this.image = image;
		this.deckCollection().deck1().addCards(deck.toDeck().getCards());
		this.setAi(new CreatureAI(this));
	}

	@Override
	public void update(GameState state) {
		super.update(state);
		if (this.cardStack().getCards().isEmpty() && this.deckCollection().deck1().getCards().isEmpty()) {
			this.health(0);
		}
	}

	@Override
	public void render(RenderingEnvironment re) {
		float scale = 0.6f * TILE_RADIUS * re.is.camera.zoom().get();
		Vector2f screenPosition = getScreenPosition(re).vector();
		re.textureRenderer.render(
				re.imageMap.get(image),
				screenPosition.x() - 0.5f * scale,
				screenPosition.y() - 0.7f * scale,
				scale, scale);
		re.textRenderer.render(
				textFormat()
						.text(name.toUpperCase())
						.font(re.font)
						.fontSize(0.5f * scale)
						.colour(rgba(255, 255, 255, (int) (re.is.actorTextOpacity * 255)))
						.hAlign(CENTER)
						.transform(re.textRenderer.screenToPixel()).pixelPosition(screenPosition.x(), screenPosition.y() + 0.1f * scale));
		re.textRenderer.render(
				textFormat()
						.text(health() + " HP")
						.font(re.font)
						.fontSize(0.5f * scale)
						.colour(rgba(255, 255, 255, (int) (re.is.actorTextOpacity * 255)))
						.hAlign(CENTER)
						.transform(re.textRenderer.screenToPixel()).pixelPosition(screenPosition.x(), screenPosition.y() + 0.5f * scale));
		super.render(re);
	}

	@Override
	public List<Appendage> appendages() {
		return emptyList();
	}

	@Override
	public boolean shouldRestock() {
		return false;
	}

}
