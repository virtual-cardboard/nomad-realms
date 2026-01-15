package nomadrealms.context.game.card.action;

import java.util.List;

import engine.common.math.Vector2f;
import nomadrealms.context.game.actor.types.HasPosition;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.render.RenderingEnvironment;

public class MoveAction implements Action {

	private final int delay;
	private final HasPosition source;
	private final TileCoordinate target;

	/**
	 * The number of ticks that have passed since the last jump. It is reset to 0 once it equals the delay.
	 */
	private int counter = 0;

	/**
	 * The previous tile the entity was on. Updated when the entity jumps.
	 */
	private transient Tile previousTile = null;
	/**
	 * The start timestamp of the jump
	 */
	private transient long movementStart = 0;

	private transient long initTime = 0;
	private transient Tile nextTile = null;
	private transient boolean hasMoved = false;

	/**
	 * No-arg constructor for serialization.
	 */
	private MoveAction() {
		this.source = null;
		this.target = null;
		this.delay = 0;
	}

	public MoveAction(HasPosition source, Tile target) {
		this(source, target, 10);
	}

	/**
	 * Create a new move action.
	 *
	 * @param source the entity to move
	 * @param target the tile to move to
	 * @param delay  in ticks
	 */
	public MoveAction(HasPosition source, Tile target, int delay) {
		this(source, target.coord(), delay);
	}

	public MoveAction(HasPosition source, TileCoordinate target, int delay) {
		this.source = source;
		this.target = target;
		this.delay = delay;
		counter = delay;
	}

	@Override
	public void init(World world) {
		initTime = System.currentTimeMillis();
		calculateNextTile(world);
	}

	private void calculateNextTile(World world) {
		List<Tile> path = world.map().path(source.tile(), world.getTile(target));
		if (path.size() > 1) {
			nextTile = path.get(1);
		} else {
			nextTile = null;
		}
	}

	@Override
	public void update(World world) {
		if (counter >= delay) {
			counter = 0;
			if (nextTile == null) {
				calculateNextTile(world);
			}
			if (nextTile != null) {
				previousTile = source.tile();
				movementStart = System.currentTimeMillis();
				hasMoved = true;
				source.move(nextTile);
				calculateNextTile(world);
			}
		}
		counter++;
	}

	@Override
	public boolean isComplete() {
		return source.tile().coord().equals(target);
	}

	@Override
	public int preDelay() {
		return delay / 2;
	}

	@Override
	public int postDelay() {
		return delay - preDelay();
	}

	public Vector2f getScreenOffset(RenderingEnvironment re, long currentTimeMillis) {
		float millisPerTick = re.config.getMillisPerTick();
		float totalDelayMs = delay * millisPerTick;
		float preDelayMs = preDelay() * millisPerTick;
		float postDelayMs = postDelay() * millisPerTick;

		// Pre-move phase of the first step
		if (!hasMoved) {
			if (nextTile == null) {
				return new Vector2f(0, 0);
			}
			float progress = (currentTimeMillis - initTime) / totalDelayMs;
			if (progress > 0.5f) {
				progress = 0.5f;
			}
			float vertical = 40 * progress * (1 - progress);
			Vector2f dir = nextTile.coord().sub(source.tile().coord()).toVector2f();
			return dir.scale(progress).sub(0, vertical);
		}

		// Post-move phase of current step OR Pre-move phase of next step
		long timeSinceMove = currentTimeMillis - movementStart;
		if (timeSinceMove < postDelayMs) {
			// Post-move phase
			float progress = (currentTimeMillis - movementStart + preDelayMs) / totalDelayMs;
			if (previousTile == null) {
				return new Vector2f(0, 0);
			}
			float vertical = 40 * progress * (1 - progress);
			Vector2f dir = previousTile.coord().sub(source.tile().coord()).toVector2f();
			return dir.scale(1 - progress).sub(0, vertical);
		} else {
			// Pre-move phase for next step
			if (nextTile == null) {
				return new Vector2f(0, 0);
			}
			long nextMoveTime = (long) (movementStart + totalDelayMs);
			float progress = (currentTimeMillis - nextMoveTime + preDelayMs) / totalDelayMs;
			if (progress > 0.5f) {
				progress = 0.5f;
			}
			float vertical = 40 * progress * (1 - progress);
			Vector2f dir = nextTile.coord().sub(source.tile().coord()).toVector2f();
			return dir.scale(progress).sub(0, vertical);
		}
	}

}
