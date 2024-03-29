package nomadrealms.world.actor;

import nomadrealms.card.zone.DeckCollection;
import nomadrealms.misc.Target;

public class CardPlayer implements Target {

	private final DeckCollection deckCollection = new DeckCollection();

	public DeckCollection deckCollection() {
		return deckCollection;
	}

}
