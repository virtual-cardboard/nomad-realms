package nomadrealms.game.actor.cardplayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nomadrealms.game.GameState;
import nomadrealms.game.actor.Actor;
import nomadrealms.game.actor.ai.CardPlayerAI;
import nomadrealms.game.card.action.ActionScheduler;
import nomadrealms.game.event.CardPlayedEvent;
import nomadrealms.game.event.InputEvent;
import nomadrealms.game.item.Inventory;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.zone.DeckCollection;

public abstract class CardPlayer implements Actor {

	private final ActionScheduler actionScheduler = new ActionScheduler();

	private CardPlayerAI ai;
	private transient Tile tile;
	private int health;

	/**
	 * This is a list because theoretically an actor can make two input actions in the same frame if they're fast
	 * enough.
	 */
	private List<InputEvent> nextPlays = new ArrayList<>();
	private final List<CardPlayedEvent> queue = new ArrayList<>();
	private final List<InputEvent> lastPlays = new ArrayList<>();

	private final DeckCollection deckCollection = new DeckCollection();
	private final Inventory inventory = new Inventory(this);

	/**
	 * No-arg constructor for serialization.
	 */
	protected CardPlayer() {
	}

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
		lastPlays.addAll(nextPlays);
		nextPlays = new ArrayList<>();
		return events;
	}

	public CardPlayerAI ai() {
		return ai;
	}

	public void setAi(CardPlayerAI ai) {
		this.ai = ai;
	}

	@Override
	public void update(GameState state) {
		if (ai() != null) {
			ai().doUpdate(state);
		}
	}

	@Override
	public Inventory inventory() {
		return inventory;
	}

	@Override
	public void tile(Tile tile) {
		this.tile = tile;
	}

	@Override
	public Tile tile() {
		return tile;
	}

	@Override
	public int health() {
		return health;
	}

	@Override
	public void health(int health) {
		this.health = health;
	}

	public List<InputEvent> lastPlays() {
		return lastPlays;
	}

}
