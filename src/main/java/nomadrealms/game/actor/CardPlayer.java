package nomadrealms.game.actor;

import nomadrealms.game.event.CardPlayedEvent;
import nomadrealms.game.event.InputEvent;
import nomadrealms.game.event.IntentEmitter;
import nomadrealms.game.event.Target;
import nomadrealms.game.item.Inventory;
import nomadrealms.game.zone.DeckCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class CardPlayer implements Actor, IntentEmitter, Target {

	/**
	 * This is a list because theoretically an actor can make two input actions in the same frame if they're fast
	 * enough.
	 */
	private List<InputEvent> nextPlays = new ArrayList<>();
	private final ArrayList<CardPlayedEvent> queue = new ArrayList<>();

	private final DeckCollection deckCollection = new DeckCollection();
	private final Inventory inventory = new Inventory(this);

	public DeckCollection deckCollection() {
		return deckCollection;
	}

	public Collection<CardPlayedEvent> queue() {
		return queue;
	}

	public void addNextPlay(InputEvent event) {
		nextPlays.add(event);
	}

	@Override
	public List<InputEvent> retrieveNextPlays() {
		List<InputEvent> events = nextPlays;
		nextPlays = new ArrayList<>();
		return events;
	}

	@Override
	public Inventory inventory() {
		return inventory;
	}
}
