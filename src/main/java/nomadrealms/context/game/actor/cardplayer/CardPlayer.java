package nomadrealms.context.game.actor.cardplayer;

import static engine.common.colour.Colour.rgba;
import static engine.common.colour.Colour.toRangedVector;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static nomadrealms.context.game.card.UICard.cardSize;
import static nomadrealms.context.game.world.map.area.Tile.TILE_VERTICAL_SPACING;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.HasSpeech;
import nomadrealms.context.game.actor.ai.CardPlayerAI;
import nomadrealms.context.game.actor.cardplayer.appendage.Appendage;
import nomadrealms.context.game.card.action.Action;
import nomadrealms.context.game.card.action.scheduler.CardPlayerActionScheduler;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.item.Inventory;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.zone.CardQueue;
import nomadrealms.context.game.zone.DeckCollection;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.custom.speech.SpeechBubble;

public abstract class CardPlayer implements Actor, HasSpeech {

	private final CardPlayerActionScheduler actionScheduler = new CardPlayerActionScheduler();

	private CardPlayerAI ai;
	private TileCoordinate tileCoord;
	private transient Tile tile;
	private int health;

	private SpeechBubble speech;

	/**
	 * This is a list because theoretically an actor can make two input actions in the same frame if they're fast
	 * enough.
	 */
	private List<InputEvent> nextPlays = new ArrayList<>();
	private final CardQueue queue = new CardQueue();
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

	public CardQueue queue() {
		return queue;
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
		queue().update(state.world);
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

	public Vector2f getScreenPosition(RenderingEnvironment re) {
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

	public void render() {

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
		queue.reinitializeAfterLoad(world);
		for (InputEvent lastPlay : lastPlays) {
			lastPlay.reinitializeAfterLoad(world);
		}
	}

}
