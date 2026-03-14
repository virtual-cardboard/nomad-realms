package nomadrealms.context.game.actor.types.cardplayer;

import static engine.common.colour.Colour.rgb;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.ARM;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.EYE;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.HEAD;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.TORSO;
import static nomadrealms.context.game.card.GameCard.HEAL;
import static nomadrealms.context.game.card.GameCard.MOVE;
import static nomadrealms.context.game.card.GameCard.TILL_SOIL;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import engine.common.math.Vector2f;
import engine.visuals.rendering.text.HorizontalAlign;
import engine.visuals.rendering.text.TextFormat;
import nomadrealms.context.game.GameState;

import static engine.visuals.rendering.text.TextFormat.textFormat;
import nomadrealms.context.game.actor.ai.StupidAI;
import nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;

public class Bub extends CardPlayer {

	private final String name;

	public Bub(String name, Tile tile) {
		this.name = name;
		this.tile(tile);
		this.health(10);
		this.deckCollection().deck1().addCards(Stream.of(MOVE, HEAL, TILL_SOIL).map(WorldCard::new));
		this.setAi(new StupidAI(this));
		assert ai() != null;
	}

	@Override
	public void render(RenderingEnvironment re) {
		float scale = 0.6f * TILE_RADIUS;
		Vector2f screenPosition = tile().getScreenPosition(re).vector();
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
						.colour(rgb(255, 255, 255))
						.hAlign(HorizontalAlign.CENTER));
		re.textRenderer.render(
				screenPosition.x(),
				screenPosition.y() + 0.5f * scale,
				textFormat()
						.text(health() + " HP")
						.font(re.font)
						.fontSize(0.5f * scale)
						.colour(rgb(255, 255, 255))
						.hAlign(HorizontalAlign.CENTER));
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
