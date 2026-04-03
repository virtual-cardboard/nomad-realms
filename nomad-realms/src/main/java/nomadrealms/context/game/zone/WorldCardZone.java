package nomadrealms.context.game.zone;

import nomadrealms.context.game.card.WorldCard;

public class WorldCardZone<T extends WorldCard> extends CardZone<T> {

	@Override
	public WorldCardZone<T> addCard(T card) {
		super.addCard(card);
		card.zone(this);
		return this;
	}

	/**
	 * purely done for the sake of adding references to optimize other algorithms
	 */
	public void reindex() {
		for (T card : cards) {
			card.zone(this);
		}
	}

}
