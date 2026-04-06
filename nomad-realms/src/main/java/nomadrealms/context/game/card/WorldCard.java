package nomadrealms.context.game.card;

import engine.serialization.Derializable;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.zone.Deck;
import nomadrealms.context.game.zone.WorldCardZone;

/**
 * A card that exists in the {@link World}.
 * <p>
 * This class is used to represent a card when it is in the world.
 *
 * @author Lunkle
 */
@Derializable
public class WorldCard implements Card {

	/**
	 * Every world card must have an originating deck.
	 */
	private Deck deck;

	transient WorldCardZone zone;
	GameCard card;
	CardMemory memory = new CardMemory();

	private boolean ephemeral = false;

	/**
	 * No-arg constructor for serialization.
	 */
	protected WorldCard() {
	}

	public WorldCard(Deck deck, GameCard card) {
		this.deck = deck;
		this.card = card;
		this.zone = deck;
	}

	@Override
	public CardType type() {
		return card.type();
	}

	public GameCard card() {
		return card;
	}

	public Deck deck() {
		return deck;
	}

	public void deck(Deck deck) {
		this.deck = deck;
	}

	public WorldCardZone zone() {
		return zone;
	}

	public void zone(WorldCardZone zone) {
		this.zone = zone;
	}

	public boolean ephemeral() {
		return ephemeral;
	}

	public WorldCard ephemeral(boolean ephemeral) {
		this.ephemeral = ephemeral;
		return this;
	}

	@Override
	public String toString() {
		return "WorldCard{" +
				card +
//				", memory=" + memory +
				'}';
	}

}
