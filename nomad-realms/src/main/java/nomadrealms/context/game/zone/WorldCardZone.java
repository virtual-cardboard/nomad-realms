package nomadrealms.context.game.zone;

import nomadrealms.context.game.card.WorldCard;
import engine.serialization.Derializable;

@Derializable
public class WorldCardZone extends CardZone<WorldCard> {

	@Override
	public WorldCardZone addCard(WorldCard card) {
		super.addCard(card);
		card.zone(this);
		return this;
	}

	/**
	 * purely done for the sake of adding references to optimize other algorithms
	 */
	public void reindex() {
		for (WorldCard card : cards) {
			card.zone(this);
		}
	}

}
