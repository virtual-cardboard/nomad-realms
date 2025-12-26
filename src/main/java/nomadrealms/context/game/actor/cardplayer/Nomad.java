package nomadrealms.context.game.actor.cardplayer;

import static engine.common.colour.Colour.rgb;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static nomadrealms.context.game.actor.cardplayer.appendage.Appendage.ARM;
import static nomadrealms.context.game.actor.cardplayer.appendage.Appendage.EYE;
import static nomadrealms.context.game.actor.cardplayer.appendage.Appendage.HEAD;
import static nomadrealms.context.game.actor.cardplayer.appendage.Appendage.LEG;
import static nomadrealms.context.game.actor.cardplayer.appendage.Appendage.TORSO;
import static nomadrealms.context.game.card.GameCard.ELECTROSTATIC_ZAPPER;
import static nomadrealms.context.game.card.GameCard.GATHER;
import static nomadrealms.context.game.card.GameCard.HEAL;
import static nomadrealms.context.game.card.GameCard.MELEE_ATTACK;
import static nomadrealms.context.game.card.GameCard.MOVE;
import static nomadrealms.context.game.card.GameCard.WOODEN_CHEST;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

import java.util.Collections;
import java.util.List;

import engine.common.math.Vector2f;
import nomadrealms.context.game.actor.cardplayer.appendage.Appendage;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.action.Action;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.zone.Deck;
import nomadrealms.render.RenderingEnvironment;

public class Nomad extends CardPlayer {

	private final String name;

	/**
	 * No-arg constructor for serialization.
	 */
	protected Nomad() {
		this.name = "Nomad";
	}

	public Nomad(String name, Tile tile) {
		this(name, tile, new DeckCollection());
	}

	public Nomad(String name, Tile tile, DeckCollection deckCollection) {
		this.name = name;
		this.tile(tile);
		this.health(10);
		this.deckCollection(deckCollection);
	}

	@Override
	public List<Action> actions() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public void render(RenderingEnvironment re) {
		float scale = 0.6f * TILE_RADIUS;
		Vector2f screenPosition = getScreenPosition(re);
		re.textureRenderer.render(
				re.imageMap.get("nomad"),
				screenPosition.x() - 0.5f * scale,
				screenPosition.y() - 0.7f * scale,
				scale, scale);
		re.textRenderer.alignCenterHorizontal().alignTop();
		re.textRenderer.render(
				screenPosition.x(),
				screenPosition.y() + 0.1f * scale,
				name,
				0,
				re.font,
				0.5f * scale,
				rgb(255, 255, 255));
		re.textRenderer.render(
				screenPosition.x(),
				screenPosition.y() + 0.5f * scale,
				health() + " HP",
				0,
				re.font,
				0.5f * scale,
				rgb(255, 255, 255));
		queue().render(re, getScreenPosition(re));
	}

	@Override
	public String imageName() {
		return "nomad";
	}

	public float imageScale() {
		return 0.6f;
	}

	@Override
	public List<Appendage> appendages() {
		return asList(HEAD, EYE, EYE, TORSO, ARM, ARM, LEG, LEG);
	}

}
