package nomadrealms.context.game.card.action;

import engine.common.math.Vector2f;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.constraint.posdim.CustomSupplierConstraint;
import nomadrealms.context.game.actor.types.HasPosition;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.render.RenderingEnvironment;

public class DashAction implements Action {

	private final int duration;
	private final HasPosition source;
	private final TileCoordinate target;

	private boolean complete = false;

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
	private DashAction() {
		this.source = null;
		this.target = null;
		this.duration = 0;
	}

	public DashAction(HasPosition source, Tile target) {
		this(source, target, 10);
	}

	/**
	 * Create a new dash action.
	 *
	 * @param source the entity to move
	 * @param target the tile to move to
	 * @param delay  in ticks
	 */
	public DashAction(HasPosition source, Tile target, int delay) {
		this(source, target.coord(), delay);
	}

	public DashAction(HasPosition source, TileCoordinate target, int delay) {
		this.source = source;
		this.target = target;
		this.duration = delay;
		counter = delay;
	}

	@Override
	public void update(World world) {
		if (counter >= duration) {
			counter = 0;
			Tile current = source.tile();
			Tile targetTile = world.getTile(target);
			if (current.equals(targetTile)) {
				complete = true;
				return;
			}
			Tile next = getNextTile(world, current, target);
			if (next != null) {
				previousTile = current;
				movementStart = System.currentTimeMillis();
				if (!source.move(next)) {
					complete = true;
				}
			} else {
				complete = true;
			}
		}
		counter++;
	}

	private Tile getNextTile(World world, Tile current, TileCoordinate target) {
		Tile next = null;
		int minDistance = current.coord().distanceTo(target);
		Tile[] neighbors = {
				current.ul(world), current.um(world), current.ur(world),
				current.dl(world), current.dm(world), current.dr(world)
		};
		for (Tile neighbor : neighbors) {
			if (neighbor == null) {
				continue;
			}
			int dist = neighbor.coord().distanceTo(target);
			if (dist < minDistance) {
				minDistance = dist;
				next = neighbor;
			}
		}
		return next;
	}

	@Override
	public boolean isComplete() {
		return complete || source.tile().coord().equals(target);
	}

	@Override
	public int preDelay() {
		return 0;
	}

	@Override
	public int postDelay() {
		return duration;
	}

	public ConstraintPair screenOffset(RenderingEnvironment re) {
		return new ConstraintPair(
				new CustomSupplierConstraint("DashAction X Offset", () -> getRawScreenOffset(re).x()),
				new CustomSupplierConstraint("DashAction Y Offset", () -> getRawScreenOffset(re).y())
		);
	}

	private Vector2f getRawScreenOffset(RenderingEnvironment re) {
		if (previousTile == null) {
			return new Vector2f(0, 0);
		}
		long time = System.currentTimeMillis();
		float progress = (time - movementStart) / (float) (duration * re.config.getMillisPerTick());
		if (source.tile() == previousTile || progress > 1) {
			return new Vector2f(0, 0);
		}
		Vector2f dir = previousTile.coord().sub(source.tile().coord()).toVector2f();
		return dir.scale(1 - progress);
	}

}
