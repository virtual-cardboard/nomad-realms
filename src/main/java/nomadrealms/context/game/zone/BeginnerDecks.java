package nomadrealms.context.game.zone;

import nomadrealms.context.game.card.collection.DeckList;

public enum BeginnerDecks {

	RUNNING_AND_WALKING("Running & Walking",
			new DeckList(

			)),
	PUNCH_AND_GRAPPLE("Punch & Grapple", new DeckList()),
	CYCLE_AND_SEARCH("Cycle & Search ", new DeckList()),
	FIRE_AND_ICE("Fire & Ice", new DeckList()),
	;

	private final String name;
	private final DeckList deckList;

	BeginnerDecks(String name, DeckList deckList) {
		this.name = name;
		this.deckList = deckList;
	}

	public String deckName() {
		return name;
	}

	public DeckList deckList() {
		return deckList;
	}
}
