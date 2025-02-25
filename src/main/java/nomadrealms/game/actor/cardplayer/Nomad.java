package nomadrealms.game.actor.cardplayer;

import static common.colour.Colour.rgb;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static nomadrealms.game.actor.cardplayer.appendage.Appendage.ARM;
import static nomadrealms.game.actor.cardplayer.appendage.Appendage.EYE;
import static nomadrealms.game.actor.cardplayer.appendage.Appendage.HEAD;
import static nomadrealms.game.actor.cardplayer.appendage.Appendage.LEG;
import static nomadrealms.game.actor.cardplayer.appendage.Appendage.TORSO;
import static nomadrealms.game.card.GameCard.GATHER;
import static nomadrealms.game.card.GameCard.HEAL;
import static nomadrealms.game.card.GameCard.MELEE_ATTACK;
import static nomadrealms.game.card.GameCard.MOVE;
import static nomadrealms.game.world.map.area.Tile.TILE_RADIUS;

import java.util.Collections;
import java.util.List;

import common.math.Vector2f;
import nomadrealms.game.actor.cardplayer.appendage.Appendage;
import nomadrealms.game.card.WorldCard;
import nomadrealms.game.card.action.Action;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.zone.Deck;
import nomadrealms.render.RenderingEnvironment;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;

public class Nomad extends CardPlayer {

	private final String name;

	/**
	 * No-arg constructor for serialization.
	 */
	protected Nomad() {
		this.name = "Nomad";
	}

	public Nomad(String name, Tile tile) {
		this.name = name;
		this.tile(tile);
		this.health(10);
		stream(this.deckCollection().decks()).forEach(this::initializeDeck);
	}

	@Override
	public List<Action> actions() {
		return Collections.EMPTY_LIST;
	}

	private void initializeDeck(Deck deck) {
		deck
				.addCard(new WorldCard(MOVE))
				.addCard(new WorldCard(HEAL))
				// .addCard(new WorldCard(ELECTROSTATIC_ZAPPER))
				.addCard(new WorldCard(MELEE_ATTACK))
				.addCard(new WorldCard(GATHER));
		deck.shuffle();
	}

	@Override
	public void render(RenderingEnvironment re) {
		float scale = 0.6f * TILE_RADIUS;
		DefaultFrameBuffer.instance().render(
				() -> {
					Vector2f screenPosition = getScreenPosition(re);
					re.textureRenderer.render(
							re.imageMap.get("nomad"),
							screenPosition.x() - 0.5f * scale,
							screenPosition.y() - 0.7f * scale,
							scale, scale);
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
				});
		renderQueue(re);
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
