package nomadrealms.game.zone;

import nomadrealms.game.card.WorldCard;

public class WorldCardZone extends CardZone<WorldCard> {

	@Override
	public WorldCardZone addCard(WorldCard card) {
		super.addCard(card);
		card.zone(this);
		return this;
	}

}
