package nomadrealms.context.game.card.action;

import static java.util.Collections.singletonList;

import java.util.List;

import engine.common.math.Vector2f;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.constraint.posdim.CustomSupplierConstraint;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.MoveEffect;
import nomadrealms.context.game.event.ProcChain;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.render.RenderingEnvironment;

public class WalkAction extends Action {

	private final int delay;
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
	private WalkAction() {
		super();
		this.target = null;
		this.delay = 0;
	}

	public WalkAction(CardPlayer source, Tile target) {
		this(source, target, 10);
	}

	/**
	 * Create a new walk action.
	 *
	 * @param source the entity to move
	 * @param target the tile to move to
	 * @param delay  in ticks
	 */
	public WalkAction(CardPlayer source, Tile target, int delay) {
		this(source, target.coord(), delay);
	}

	public WalkAction(CardPlayer source, TileCoordinate target, int delay) {
		super(source);
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
				world.addProcChain(new ProcChain(singletonList(new MoveEffect(source, path.get(1)))));
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
	public ConstraintPair screenOffset(RenderingEnvironment re) {
		return new ConstraintPair(
				new CustomSupplierConstraint("WalkAction X Offset", () -> getRawScreenOffset(re).x()),
				new CustomSupplierConstraint("WalkAction Y Offset", () -> getRawScreenOffset(re).y())
		);
	}

	private Vector2f getRawScreenOffset(RenderingEnvironment re) {
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
