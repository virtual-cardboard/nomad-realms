package nomadrealms.game.world.actor;

import nomadrealms.game.event.CardPlayedEvent;
import nomadrealms.game.event.IntentEmitter;
import nomadrealms.game.event.Target;
import nomadrealms.game.zone.DeckCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class CardPlayer implements IntentEmitter, Target {

	private final List<CardPlayedEvent> queue = new ArrayList<>();

	private final DeckCollection deckCollection = new DeckCollection();

	public DeckCollection deckCollection() {
		return deckCollection;
	}

	public Collection<CardPlayedEvent> queue() {
		return queue;
	}
}
