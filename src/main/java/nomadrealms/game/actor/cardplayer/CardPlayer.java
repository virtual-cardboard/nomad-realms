package nomadrealms.game.actor.cardplayer;

import static common.colour.Colour.rgb;
import static common.colour.Colour.toRangedVector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import common.math.Matrix4f;
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
import visuals.builtin.RectangleVertexArrayObject;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import visuals.lwjgl.render.meta.DrawFunction;

public abstract class CardPlayer implements Actor {

	private final CardPlayerActionScheduler actionScheduler = new CardPlayerActionScheduler();

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

	public void renderQueue(RenderingEnvironment re) {
		float padding = 5;
		Vector2f screenPos = getScreenPosition(re);
		DefaultFrameBuffer.instance().render(() -> {
			re.defaultShaderProgram
					.set("color", toRangedVector(rgb(100, 0, 0)))
					.set("transform", new Matrix4f(screenPos.x(), screenPos.y(), 100, 100, re.glContext))
					.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
		});
		for (int i = 0; i < queue.size(); i++) {
			CardPlayedEvent event = queue.get(i);
			//			event.card().moveTo(
			//					screenPos
			//							.add(padding, padding)
			//							.add(
			//									new Vector2f(padding + ).scale(i)));
			event.render(re);
		}
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
