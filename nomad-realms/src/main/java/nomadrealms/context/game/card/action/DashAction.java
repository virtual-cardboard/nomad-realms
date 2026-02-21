package nomadrealms.context.game.card.action;

import static java.util.Collections.singletonList;
import static nomadrealms.context.game.world.map.area.Tile.TILE_VERTICAL_SPACING;

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

public class DashAction extends Action {

	private final int duration;
	private final TileCoordinate target;

	private TileCoordinate startTileCoord;
	private int totalTicks;
	private int ticksElapsed = 0;
	private transient long startTime = 0;

	/**
	 * No-arg constructor for serialization.
	 */
	private DashAction() {
		super();
		this.target = null;
		this.duration = 0;
	}

	public DashAction(CardPlayer source, Tile target) {
		this(source, target, 10);
	}

	/**
	 * Create a new dash action.
	 *
	 * @param source the entity to move
	 * @param target the tile to move to
	 * @param delay  in ticks
	 */
	public DashAction(CardPlayer source, Tile target, int delay) {
		this(source, target.coord(), delay);
	}

	public DashAction(CardPlayer source, TileCoordinate target, int delay) {
		super(source);
		this.target = target;
		this.duration = delay;
	}

	@Override
	public void init(World world) {
		this.startTileCoord = source.tile().coord();
		this.startTime = System.currentTimeMillis();
		this.totalTicks = (int) (duration * startTileCoord.euclideanDistanceTo(target) / TILE_VERTICAL_SPACING);
		world.addProcChain(new ProcChain(singletonList(new MoveEffect(source, world.getTile(target)))));
	}

	@Override
	public void update(World world) {
		ticksElapsed++;
	}

	@Override
	public boolean isComplete() {
		return ticksElapsed >= totalTicks;
	}

	@Override
	public int preDelay() {
		return 0;
	}

	@Override
	public int postDelay() {
		return 0;
	}

	public ConstraintPair screenOffset(RenderingEnvironment re) {
		return new ConstraintPair(
				new CustomSupplierConstraint("DashAction X Offset", () -> getRawScreenOffset(re).x()),
				new CustomSupplierConstraint("DashAction Y Offset", () -> getRawScreenOffset(re).y())
		);
	}

	private Vector2f getRawScreenOffset(RenderingEnvironment re) {
		if (startTileCoord == null || totalTicks == 0) {
			return new Vector2f(0, 0);
		}
		long time = System.currentTimeMillis();
		if (startTime == 0) {
			startTime = time - (long) ticksElapsed * re.config.getMillisPerTick();
		}
		float progress = (time - startTime) / (float) (totalTicks * re.config.getMillisPerTick());
		if (progress > 1 || progress < 0) {
			return new Vector2f(0, 0);
		}
		Vector2f dir = startTileCoord.sub(target).toVector2f();
		return dir.scale(1 - progress);
	}

}
