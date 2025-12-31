package nomadrealms.context.game.actor.cardplayer;

import java.util.ArrayList;
import java.util.List;

import engine.visuals.constraint.box.ConstraintPair;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.HasSpeech;
import nomadrealms.context.game.actor.ai.CardPlayerAI;
import nomadrealms.context.game.actor.cardplayer.appendage.Appendage;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.action.Action;
import nomadrealms.context.game.card.action.scheduler.CardPlayerActionScheduler;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.item.Inventory;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.zone.CardStack;
import nomadrealms.context.game.zone.DeckCollection;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.custom.speech.SpeechBubble;

public abstract class CardPlayer implements Actor, HasSpeech, Target {

	private final CardPlayerActionScheduler actionScheduler = new CardPlayerActionScheduler();

	private CardPlayerAI ai;
	private TileCoordinate tileCoord;
	private transient Tile tile;
	private Tile previousTile;
	private int health;

	private SpeechBubble speech;

	/**
	 * This is a list because theoretically an actor can make two input actions in the same frame if they're fast
	 * enough.
	 */
	private List<InputEvent> nextPlays = new ArrayList<>();
	private final CardStack cardStack = new CardStack();
	private final List<InputEvent> lastPlays = new ArrayList<>();

	protected final DeckCollection deckCollection = new DeckCollection();
	private final Inventory inventory = new Inventory(this);

	private WorldCard lastResolvedCard = null;

	/**
	 * No-arg constructor for serialization.
	 */
	protected CardPlayer() {
	}

	public DeckCollection deckCollection() {
		return deckCollection;
	}

	public CardStack cardStack() {
		return cardStack;
	}

	public void addNextPlay(InputEvent event) {
		List<InputEvent> newNextPlays = new ArrayList<>(nextPlays);
		newNextPlays.add(event);
		nextPlays = newNextPlays;
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
		cardStack().update(state.world);
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
		this.tile = tile;
		this.tileCoord = tile.coord();
	}

	@Override
	public Tile previousTile() {
		return previousTile;
	}

	@Override
	public void previousTile(Tile tile) {
		this.previousTile = tile;
	}

	public ConstraintPair getScreenPosition(RenderingEnvironment re) {
		long time = System.currentTimeMillis();
		return tile.getScreenPosition(re).add(actionScheduler.getScreenOffset(re, time));
	}

	/**
	 * Override this function to return the name of the image to use for this card player.
	 * <p>
	 * By default, this returns "feral_monkey".
	 *
	 * @return The name of the image.
	 */
	public String imageName() {
		return "feral_monkey";
	}

	/**
	 * Override this function to return the scale of the image to use for this card player.
	 * <p>
	 * By default, this returns 1.
	 *
	 * @return The scale of the image.
	 */
	public float imageScale() {
		return 1;
	}

	public void render(RenderingEnvironment re) {
		cardStack().render(re, getScreenPosition(re));
	}

	@Override
	public SpeechBubble speech() {
		return speech;
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

	public void reinitializeAfterLoad(World world) {
		tile = world.getTile(tileCoord);
		inventory.reinitializeAfterLoad(this);
		if (ai != null) {
			ai.setSelf(this);
		}
		deckCollection.reinitializeAfterLoad();
		cardStack.reinitializeAfterLoad(world);
		for (InputEvent lastPlay : lastPlays) {
			lastPlay.reinitializeAfterLoad(world);
		}
	}

	public WorldCard lastResolvedCard() {
		return lastResolvedCard;
	}

	public void lastResolvedCard(WorldCard card) {
		this.lastResolvedCard = card;
	}

	@Override
	public String name() {
		return "Card Player";
	}

}
