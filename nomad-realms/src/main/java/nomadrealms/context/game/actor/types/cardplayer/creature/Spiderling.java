package nomadrealms.context.game.actor.types.cardplayer.creature;

import engine.common.math.Vector2f;
import engine.serialization.Derializable;
import nomadrealms.context.game.actor.ai.CreatureAI;
import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.card.collection.DeckList;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;

import static engine.common.colour.Colour.rgba;
import static engine.visuals.rendering.text.HorizontalAlign.CENTER;
import static engine.visuals.rendering.text.TextFormat.textFormat;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

@Derializable
public class Spiderling extends Creature {

	/**
	 * No-arg constructor for serialization.
	 */
	protected Spiderling() {
	}

	public Spiderling(Tile tile) {
		super(tile, 3, 10);
		this.setAi(new CreatureAI(this));
		this.deckCollection().deck1().addCards(new DeckList(GameCard.MOVE, GameCard.CREATE_ROCK).toDeck().getCards());
	}

	@Override
	public void render(RenderingEnvironment re) {
		float scale = 0.6f * TILE_RADIUS * re.camera.zoom().get();
		Vector2f screenPosition = getScreenPosition(re).vector();
		re.textureRenderer.render(
				re.imageMap.get("spiderling"),
				screenPosition.x() - 0.5f * scale,
				screenPosition.y() - 0.7f * scale,
				scale, scale);
		re.textRenderer.render(
				screenPosition.x(),
				screenPosition.y() + 0.1f * scale,
				textFormat()
						.text("SPIDERLING")
						.font(re.font)
						.fontSize(0.5f * scale)
						.colour(rgba(255, 255, 255, (int) (re.actorTextOpacity * 255)))
						.hAlign(CENTER));
		re.textRenderer.render(
				screenPosition.x(),
				screenPosition.y() + 0.5f * scale,
				textFormat()
						.text(health() + " HP")
						.font(re.font)
						.fontSize(0.5f * scale)
						.colour(rgba(255, 255, 255, (int) (re.actorTextOpacity * 255)))
						.hAlign(CENTER));
		super.render(re);
	}

	@Override
	public String name() {
		return "Spiderling";
	}

}
