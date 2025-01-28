package nomadrealms.game.actor.cardplayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import common.math.Vector2f;
import nomadrealms.game.GameState;
import nomadrealms.game.actor.Actor;
import nomadrealms.game.actor.ai.CardPlayerAI;
import nomadrealms.game.actor.cardplayer.appendage.Appendage;
import nomadrealms.game.card.action.Action;
import nomadrealms.game.card.action.scheduler.CardPlayerActionScheduler;
import nomadrealms.game.event.CardPlayedEvent;
import nomadrealms.game.event.InputEvent;
import nomadrealms.game.item.Inventory;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.zone.DeckCollection;
import nomadrealms.render.RenderingEnvironment;

public abstract class CardPlayer implements Actor {

	private final CardPlayerActionScheduler actionScheduler = new CardPlayerActionScheduler();

	private CardPlayerAI ai;
	private transient Tile tile;
	private int health;

	private transient Tile previousTile;
	private transient long movementStart = 0;
	private transient int movementAnimationTime = 1;

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
		actionScheduler.update(state.world);
	}

	@Override
	public Inventory inventory() {
		return inventory;
	}

	@Override
	public Tile tile() {
		return tile;
	}

	@Override
	public void tile(Tile tile) {
		this.previousTile = this.tile;
		this.tile = tile;
	}

	@Override
	public void tile(Tile tile, int animationTime) {
		this.previousTile = this.tile;
		this.tile = tile;
		this.movementStart = System.currentTimeMillis();
		this.movementAnimationTime = animationTime;
	}

	public Vector2f getScreenPosition(RenderingEnvironment re) {
		long time = System.currentTimeMillis();
		
//		float progress = (time - movementStart) / (float) (movementAnimationTime * re.config.getTickRate());
//		if (tile == previousTile || progress > 1) {
//			return tile.getScreenPosition(re);
//		}
//		System.out.println(progress);
//		float vertical = 40 * progress * (1 - progress);
//		Vector2f dir = tile.coord().sub(previousTile.coord()).toVector2f();
//		return dir.scale(progress).add(previousTile.getScreenPosition(re)).sub(0, vertical);

		return tile.getScreenPosition(re).add(actionScheduler.getScreenOffset(re, time));
	}

	@Override
	public int health() {
		return health;
	}

	@Override
	public void health(int health) {
		this.health = health;
	}

	public abstract List<Appendage> appendages();

	public List<InputEvent> lastPlays() {
		return lastPlays;
	}

	public void queueAction(Action action) {
		actionScheduler.queue(action);
	}

}
