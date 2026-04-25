package nomadrealms.context.game.actor.types.cardplayer;
import nomadrealms.context.game.interaction.InteractionState;

import static engine.common.colour.Colour.rgba;
import static engine.visuals.rendering.text.HorizontalAlign.CENTER;
import static engine.visuals.rendering.text.TextFormat.textFormat;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.ARM;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.EYE;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.HEAD;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.TORSO;
import static nomadrealms.context.game.card.GameCard.HEAL;
import static nomadrealms.context.game.card.GameCard.MOVE;
import static nomadrealms.context.game.card.GameCard.TILL_SOIL;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

import engine.common.math.Vector2f;
import java.util.Arrays;
import java.util.List;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.ai.StupidAI;
import nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage;
import nomadrealms.context.game.card.collection.DeckList;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;

public class Bub extends CardPlayer {

	private final String name;

	public Bub(String name, Tile tile) {
		this.name = name;
		this.tile(tile);
		this.health(10);
		DeckList list = new DeckList(MOVE, HEAL, TILL_SOIL);
		this.deckCollection().deck1().addCards(list.toDeck().getCards());
		this.setAi(new StupidAI(this));
		assert ai() != null;
	}

	@Override
	public void render(RenderingEnvironment re, InteractionState is) {
		float scale = 0.6f * TILE_RADIUS;
		Vector2f screenPosition = tile().getScreenPosition(re, is).vector();
		re.textureRenderer.render(
				re.imageMap.get("farmer"),
				screenPosition.x() - 0.5f * scale,
				screenPosition.y() - 0.7f * scale,
				scale, scale
		);
		re.textRenderer.render(
				screenPosition.x(),
				screenPosition.y() + 0.1f * scale,
				textFormat()
						.text(name + " BUB12")
						.font(re.font)
						.fontSize(0.5f * scale)
						.colour(rgba(255, 255, 255, (int) (is.actorTextOpacity * 255)))
						.hAlign(CENTER));
		re.textRenderer.render(
				screenPosition.x(),
				screenPosition.y() + 0.5f * scale,
				textFormat()
						.text(health() + " HP")
						.font(re.font)
						.fontSize(0.5f * scale)
						.colour(rgba(255, 255, 255, (int) (is.actorTextOpacity * 255)))
						.hAlign(CENTER));
		super.render(re, is);
	}

	@Override
	public void update(GameState state) {
		super.update(state);
		assert ai() != null;
		ai().update(state);
	}

	@Override
	public List<Appendage> appendages() {
		return Arrays.asList(HEAD, EYE, EYE, TORSO, ARM, ARM);
	}

}
