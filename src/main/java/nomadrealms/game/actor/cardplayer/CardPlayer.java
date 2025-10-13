package nomadrealms.game.actor.cardplayer;

import static engine.common.colour.Colour.rgba;
import static engine.common.colour.Colour.toRangedVector;
import static nomadrealms.game.card.UICard.cardSize;
import static nomadrealms.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
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
import nomadrealms.game.zone.CardQueue;
import nomadrealms.game.zone.DeckCollection;
import nomadrealms.render.RenderingEnvironment;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import engine.visuals.lwjgl.render.meta.DrawFunction;

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
		Constraint padding = absolute(5);
		Vector2f screenPos = getScreenPosition(re);
		ConstraintPair cardSize = cardSize(0.4f);
		Constraint length = cardSize.x().add(padding).multiply(5).add(padding);
		ConstraintBox box =
				new ConstraintBox(
						absolute(screenPos.x()).add(length.multiply(0.5f).neg()),
						absolute(screenPos.y()).add(cardSize.y().multiply(0.5f).add(absolute(TILE_VERTICAL_SPACING)).neg()),
						length,
						cardSize.y());
		DefaultFrameBuffer.instance().render(() -> {
			re.defaultShaderProgram
					.set("color", toRangedVector(rgba(100, 0, 0, 60)))
					.set("transform", new Matrix4f(box, re.glContext))
					.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
		});
		Queue<CardPlayedEvent> currentQueue = this.queue.getQueue();
		for (int i = 0; i < currentQueue.size(); i++) {
			System.out.println("Rendering queue card: " + i);
			CardPlayedEvent event = currentQueue.poll();
			event.card().physics().targetCoord(
					new ConstraintPair(
							box.x().add(padding).add(cardSize(0.4f).x().add(padding).multiply(i)),
							box.y().add(padding))).snap();
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
