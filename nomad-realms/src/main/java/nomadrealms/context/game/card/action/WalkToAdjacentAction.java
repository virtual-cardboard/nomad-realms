package nomadrealms.context.game.card.action;

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.List;

import engine.common.math.Vector2f;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.constraint.posdim.CustomSupplierConstraint;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.MoveEffect;
import nomadrealms.context.game.event.ProcChain;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.render.RenderingEnvironment;

public class WalkToAdjacentAction extends Action {

	private final int delay;
	private final Target target;
	private TileCoordinate adjacentTarget;

	private int counter = 0;
	private transient Tile previousTile = null;
	private transient long movementStart = 0;

	public WalkToAdjacentAction(CardPlayer source, Target target, int delay) {
		super(source);
		this.target = target;
		this.delay = delay;
		this.counter = delay;
	}

	@Override
	public void init(World world) {
		Tile targetTile = target.tile();
		if (targetTile == null) {
			return;
		}
		List<Tile> neighbors = new ArrayList<>();
		neighbors.add(targetTile.ul(world));
		neighbors.add(targetTile.um(world));
		neighbors.add(targetTile.ur(world));
		neighbors.add(targetTile.dl(world));
		neighbors.add(targetTile.dm(world));
		neighbors.add(targetTile.dr(world));

		Tile bestTile = null;
		int minDistance = Integer.MAX_VALUE;
		for (Tile neighbor : neighbors) {
			if (neighbor != null && (neighbor.actor() == null || neighbor.actor() == source)) {
				int distance = source.tile().coord().distanceTo(neighbor.coord());
				if (distance < minDistance) {
					minDistance = distance;
					bestTile = neighbor;
				}
			}
		}
		if (bestTile != null) {
			this.adjacentTarget = bestTile.coord();
		}
	}

	@Override
	public void update(World world) {
		if (adjacentTarget == null) {
			return;
		}
		if (counter >= delay) {
			counter = 0;
			List<Tile> path = world.map().path(source.tile(), world.getTile(adjacentTarget));
			if (path.size() > 1) {
				previousTile = source.tile();
				movementStart = System.currentTimeMillis();
				world.addProcChain(new ProcChain(singletonList(new MoveEffect(source, path.get(1)))));
			} else if (!source.tile().coord().equals(adjacentTarget)) {
				adjacentTarget = null;
			}
		}
		counter++;
	}

	@Override
	public boolean isComplete() {
		return adjacentTarget == null || source.tile().coord().equals(adjacentTarget);
	}

	@Override
	public int preDelay() {
		return 0;
	}

	@Override
	public int postDelay() {
		return delay;
	}

	public ConstraintPair screenOffset(RenderingEnvironment re) {
		return new ConstraintPair(
				new CustomSupplierConstraint("WalkToAdjacentAction X Offset", () -> getRawScreenOffset(re).x()),
				new CustomSupplierConstraint("WalkToAdjacentAction Y Offset", () -> getRawScreenOffset(re).y())
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
