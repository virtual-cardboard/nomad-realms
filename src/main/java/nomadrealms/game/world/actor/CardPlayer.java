package nomadrealms.game.world.actor;

import nomadrealms.game.zone.DeckCollection;
import nomadrealms.game.event.Target;

public class CardPlayer implements Target {

	private final DeckCollection deckCollection = new DeckCollection();

	public DeckCollection deckCollection() {
		return deckCollection;
	}

}
