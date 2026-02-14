package nomadrealms.context.game.actor.types.cardplayer;

import static engine.common.colour.Colour.rgb;
import static java.util.Arrays.asList;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.ARM;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.EYE;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.HEAD;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.LEG;
import static nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage.TORSO;
import static nomadrealms.context.game.card.GameCard.CUT_TREE;
import static nomadrealms.context.game.card.GameCard.GATHER;
import static nomadrealms.context.game.card.GameCard.MEANDER;
import static nomadrealms.context.game.card.GameCard.MOVE;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

import java.util.List;
import java.util.stream.Stream;

import engine.common.math.Vector2f;
import nomadrealms.context.game.actor.ai.VillageLumberjackAI;
import nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;

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
		re.textRenderer.alignCenterHorizontal().alignTop();
		re.textRenderer.render(
				screenPosition.x(),
				screenPosition.y() + 0.1f * scale,
				name + " LUMBERJACK",
				0,
				re.font,
				0.5f * scale,
				rgb(255, 255, 255)
		);
		re.textRenderer.render(
				screenPosition.x(),
				screenPosition.y() + 0.5f * scale,
				health() + " HP",
				0,
				re.font,
				0.5f * scale,
				rgb(255, 255, 255)
		);
		super.render(re);
	}

	@Override
	public String imageName() {
		return "farmer";
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
