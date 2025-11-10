package nomadrealms.context.game.zone;

import nomadrealms.context.game.card.WorldCard;

public class WorldCardZone extends CardZone<WorldCard> {

	@Override
	public WorldCardZone addCard(WorldCard card) {
		super.addCard(card);
		card.zone(this);
		return this;
	}

}
