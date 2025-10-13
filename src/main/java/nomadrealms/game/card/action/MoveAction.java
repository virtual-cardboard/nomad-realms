package nomadrealms.game.card.action;

import java.util.List;

import engine.common.math.Vector2f;
import nomadrealms.game.actor.HasPosition;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
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
	public void update(World world) {
		if (counter >= delay) {
			counter = 0;
			List<Tile> path = world.map().path(source.tile(), world.getTile(target));
			if (path.size() > 1) {
				previousTile = source.tile();
				movementStart = System.currentTimeMillis();
				source.move(path.get(1));
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
		return 0;
	}

	@Override
	public int postDelay() {
		return delay;
	}

	// TODO: make it so that the delay is split between preDelay and postDelay, and the animation is split between the two
	public Vector2f getScreenOffset(RenderingEnvironment re, long currentTimeMillis) {
		if (previousTile == null) {
			return new Vector2f(0, 0);
		}
		long time = System.currentTimeMillis();
		float progress = (time - movementStart) / (float) (delay * re.config.getMillisPerTick());
		if (source.tile() == previousTile || progress > 1) {
			return new Vector2f(0, 0);
		}
		float vertical = 40 * progress * (1 - progress);
		Vector2f dir = previousTile.coord().sub(source.tile().coord()).toVector2f();
		return dir.scale(1 - progress).sub(0, vertical);
	}

}
