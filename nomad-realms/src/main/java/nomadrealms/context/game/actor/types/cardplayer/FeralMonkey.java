package nomadrealms.context.game.actor.types.cardplayer;

import static engine.common.colour.Colour.rgba;
import static engine.visuals.rendering.text.HorizontalAlign.CENTER;
import static engine.visuals.rendering.text.TextFormat.textFormat;
import static engine.visuals.rendering.text.VerticalAlign.TOP;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.ARM;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.EYE;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.HEAD;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.LEG;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.TAIL;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.TORSO;
import static nomadrealms.context.game.card.GameCard.MEANDER;
import static nomadrealms.context.game.card.GameCard.MELEE_ATTACK;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

import static java.util.Arrays.asList;

import engine.common.math.Vector2f;
import java.util.List;
import nomadrealms.context.game.actor.ai.FeralMonkeyAI;
import nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage;
import nomadrealms.context.game.card.collection.DeckList;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;

public class FeralMonkey extends CardPlayer {

	/**
	 * No-arg constructor for serialization.
	 */
	protected FeralMonkey() {
		this.name = "Feral Monkey";
	}

	public FeralMonkey(String name, Tile tile) {
		this.setAi(new FeralMonkeyAI(this));
		this.name = name;
		this.tile(tile);
		this.health(10);
		DeckList list1 = new DeckList(MEANDER);
		this.deckCollection().deck1().addCards(list1.toDeck().getCards());
		DeckList list2 = new DeckList(MELEE_ATTACK);
		this.deckCollection().deck2().addCards(list2.toDeck().getCards());
	}

	public void render(RenderingEnvironment re) {
		float scale = 0.6f * TILE_RADIUS * re.camera.zoom().get();
		Vector2f screenPosition = getScreenPosition(re).vector();
		re.textureRenderer.render(
				re.imageMap.get("feral_monkey"),
				screenPosition.x() - 0.5f * scale,
				screenPosition.y() - 0.7f * scale,
				scale, scale);
		re.textRenderer.render(
				screenPosition.x(),
				screenPosition.y() + 0.1f * scale,
				textFormat()
						.text(name + " FERAL MONKEY")
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
		return asList(HEAD, EYE, EYE, TORSO, ARM, ARM, LEG, LEG, TAIL);
	}

}
