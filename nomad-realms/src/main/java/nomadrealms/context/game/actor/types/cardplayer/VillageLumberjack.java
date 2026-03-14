package nomadrealms.context.game.actor.types.cardplayer;

import engine.common.math.Vector2f;
import static engine.visuals.rendering.text.HorizontalAlign.CENTER;
import static engine.visuals.rendering.text.VerticalAlign.TOP;
import nomadrealms.context.game.actor.ai.VillageLumberjackAI;
import nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;

import java.util.List;
import java.util.stream.Stream;

import static engine.common.colour.Colour.rgb;
import static engine.visuals.rendering.text.TextFormat.textFormat;
import static java.util.Arrays.asList;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.*;
import static nomadrealms.context.game.card.GameCard.*;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

public class VillageLumberjack extends CardPlayer {

	private final String name;

	/**
	 * No-arg constructor for serialization.
	 */
	protected VillageLumberjack() {
		this.name = "Lumberjack";
	}

	public VillageLumberjack(String name, Tile tile) {
		this.setAi(new VillageLumberjackAI(this));
		this.name = name;
		this.tile(tile);
		this.health(20);
		this.deckCollection().deck1().addCards(Stream.of(MEANDER).map(WorldCard::new));
		this.deckCollection().deck2().addCards(Stream.of(CUT_TREE).map(WorldCard::new));
		this.deckCollection().deck3().addCards(Stream.of(GATHER).map(WorldCard::new));
		this.deckCollection().deck4().addCards(Stream.of(MOVE).map(WorldCard::new));
	}

	@Override
	public void render(RenderingEnvironment re) {
		float scale = 0.6f * TILE_RADIUS * re.camera.zoom().get();
		Vector2f screenPosition = getScreenPosition(re).vector();
		re.textureRenderer.render(
				re.imageMap.get(imageName()),
				screenPosition.x() - 0.5f * scale,
				screenPosition.y() - 0.7f * scale,
				scale, scale
		);
		re.textRenderer.render(
				screenPosition.x(),
				screenPosition.y() + 0.1f * scale,
				textFormat()
						.text(name + " LUMBERJACK")
						.font(re.font)
						.fontSize(0.5f * scale)
						.colour(rgb(255, 255, 255))
						.hAlign(CENTER)
						.vAlign(TOP)
		);
		re.textRenderer.render(
				screenPosition.x(),
				screenPosition.y() + 0.5f * scale,
				textFormat()
						.text(health() + " HP")
						.font(re.font)
						.fontSize(0.5f * scale)
						.colour(rgb(255, 255, 255))
						.hAlign(CENTER)
						.vAlign(TOP)
		);
		super.render(re);
	}

	@Override
	public String imageName() {
		return "villager_lumberjack";
	}

	@Override
	public List<Appendage> appendages() {
		return asList(HEAD, EYE, EYE, TORSO, ARM, ARM, LEG, LEG);
	}

	@Override
	public String name() {
		return name;
	}

}
