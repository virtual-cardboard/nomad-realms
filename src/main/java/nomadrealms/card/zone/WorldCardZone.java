package nomadrealms.card.zone;

import nomadrealms.card.card.WorldCard;

public class WorldCardZone extends CardZone<WorldCard> {

	@Override
	public void addCard(WorldCard card) {
		super.addCard(card);
		card.zone(this);
	}

}
