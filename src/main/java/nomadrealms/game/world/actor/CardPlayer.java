package nomadrealms.game.world.actor;

import nomadrealms.game.event.IntentEmitter;
import nomadrealms.game.zone.DeckCollection;
import nomadrealms.game.event.Target;

public class CardPlayer implements IntentEmitter, Target {

	private final DeckCollection deckCollection = new DeckCollection();

	public DeckCollection deckCollection() {
		return deckCollection;
	}

}
