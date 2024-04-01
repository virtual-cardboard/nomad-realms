package nomadrealms.game.zone;

import nomadrealms.game.card.WorldCard;

public class WorldCardZone extends CardZone<WorldCard> {

	@Override
	public void addCard(WorldCard card) {
		super.addCard(card);
		card.zone(this);
	}

}
