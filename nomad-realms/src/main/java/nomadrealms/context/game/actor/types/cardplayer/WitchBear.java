package nomadrealms.context.game.actor.types.cardplayer;

import static engine.common.colour.Colour.rgba;
import static engine.visuals.rendering.text.HorizontalAlign.CENTER;
import static engine.visuals.rendering.text.TextFormat.textFormat;
import static engine.visuals.rendering.text.VerticalAlign.TOP;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.ARM;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.EYE;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.HEAD;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.LEG;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.TORSO;
import static nomadrealms.context.game.card.GameCard.DEBILITATING_FEAR;
import static nomadrealms.context.game.card.GameCard.MEANDER;
import static nomadrealms.context.game.card.GameCard.TOTEM_OF_PAIN;
import static nomadrealms.context.game.card.GameCard.VOODOO_HEX;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

import static java.util.Arrays.asList;

import engine.common.math.Vector2f;
import java.util.List;
import nomadrealms.context.game.actor.ai.WitchBearAI;
import nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage;
import nomadrealms.context.game.card.collection.DeckList;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;

public class WitchBear extends CardPlayer {

	/**
	 * No-arg constructor for serialization.
	 */
	protected WitchBear() {
		this.name = "Witch Bear";
	}

	public WitchBear(String name, Tile tile) {
		this.setAi(new WitchBearAI(this));
		this.name = name;
		this.tile(tile);
		this.health(20);
		this.deckCollection().deck1().addCards(new DeckList(MEANDER).toDeck().getCards());
		this.deckCollection().deck2().addCards(new DeckList(TOTEM_OF_PAIN).toDeck().getCards());
		this.deckCollection().deck3().addCards(new DeckList(VOODOO_HEX).toDeck().getCards());
		this.deckCollection().deck4().addCards(new DeckList(DEBILITATING_FEAR).toDeck().getCards());
	}

	public void render(RenderingEnvironment re) {
		float scale = 0.6f * TILE_RADIUS * re.is.camera.zoom().get();
		Vector2f screenPosition = getScreenPosition(re).vector();
		re.textureRenderer.render(
				re.imageMap.get("witch_bear"),
				screenPosition.x() - 0.5f * scale,
				screenPosition.y() - 0.7f * scale,
				scale, scale);
		re.textRenderer.render(
				textFormat()
						.text(name + " WITCH BEAR")
						.font(re.font)
						.fontSize(0.5f * scale)
						.colour(rgba(255, 255, 255, (int) (re.is.actorTextOpacity * 255)))
						.hAlign(CENTER)
						.vAlign(TOP)
						.transform(re.textRenderer.screenToPixel().copy().translate(screenPosition.x(), screenPosition.y() + 0.1f * scale)));
		renderHealth(re, screenPosition, scale);
		super.render(re);
	}

	@Override
	public List<Appendage> appendages() {
		return asList(HEAD, EYE, EYE, TORSO, ARM, ARM, LEG, LEG);
	}

}
