package nomadrealms.game.card;

import nomadrealms.game.zone.WorldCardZone;

/**
 * A card that exists in the world.
 * <p>
 * This class is used to represent a card when it is in the world. A
 */
public class WorldCard implements Card {

	transient WorldCardZone zone;
	GameCard card;
	CardMemory memory = new CardMemory();

	/**
	 * No-arg constructor for serialization.
	 */
	protected WorldCard() {
	}

	public WorldCard(GameCard card) {
		this.card = card;
		this.zone = zone;
	}

	public GameCard card() {
		return card;
	}

	public WorldCardZone zone() {
		return zone;
	}

	public void zone(WorldCardZone zone) {
		this.zone = zone;
	}

	@Override
	public String toString() {
		return "WorldCard{" +
				card +
//				", memory=" + memory +
				'}';
	}

}
