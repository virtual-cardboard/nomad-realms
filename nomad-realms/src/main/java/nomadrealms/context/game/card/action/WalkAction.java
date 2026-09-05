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

	@Override
	public Vector2f renderScale(RenderingEnvironment re) {
		if (previousTile == null) {
			return new Vector2f(1.0f, 1.0f);
		}
		long time = System.currentTimeMillis();
		float progress = (time - movementStart) / (float) (delay * re.config.getMillisPerTick());
		if (source.tile() == previousTile || progress > 1) {
			return new Vector2f(1.0f, 1.0f);
		}

		float scaleX = 1.0f;
		float scaleY = 1.0f;

		if (progress < 0.15f) {
			// Takeoff crouch/squeeze: scaleY compresses to 0.85, scaleX expands to 1.15
			float t = progress / 0.15f;
			float squeeze = (float) Math.sin(t * Math.PI);
			scaleY = 1.0f - 0.15f * squeeze;
			scaleX = 1.0f + 0.15f * squeeze;
		} else if (progress < 0.35f) {
			// Launch elongation: scaleY elongates to 1.2, scaleX narrows to 0.85
			float t = (progress - 0.15f) / 0.20f;
			float stretch = (float) Math.sin(t * Math.PI);
			scaleY = 1.0f + 0.20f * stretch;
			scaleX = 1.0f - 0.15f * stretch;
		} else if (progress < 0.70f) {
			// Mid-air floating: normal 1.0 scale
			scaleX = 1.0f;
			scaleY = 1.0f;
		} else if (progress < 0.88f) {
			// Landing squeeze: scaleY compresses to 0.82, scaleX expands to 1.18
			float t = (progress - 0.70f) / 0.18f;
			float squeeze = (float) Math.sin(t * Math.PI);
			scaleY = 1.0f - 0.18f * squeeze;
			scaleX = 1.0f + 0.18f * squeeze;
		} else {
			// Recovery stretch back to 1.0
			float t = (progress - 0.88f) / 0.12f;
			float stretch = (float) Math.sin(t * Math.PI);
			scaleY = 1.0f + 0.08f * stretch;
			scaleX = 1.0f - 0.08f * stretch;
		}

		return new Vector2f(scaleX, scaleY);
	}

}
