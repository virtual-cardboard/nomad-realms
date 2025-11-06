package nomadrealms.game.card;

import nomadrealms.game.zone.WorldCardZone;

/**
 * A card that exists in the world.
 * <p>
 * This class is used to represent a card when it is in the world.
 *
 * @author Lunkle
 */
public class WorldCard implements Card {

	transient WorldCardZone zone;
	GameCard card;
	CardMemory memory = new CardMemory();

	private boolean ephemeral = false;

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
