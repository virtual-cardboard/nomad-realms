package nomadrealms.game.card.action;

import java.util.List;

import nomadrealms.game.actor.HasPosition;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;

public class MoveAction implements Action {

	private final float delay;
	private final HasPosition source;
	private final TileCoordinate target;

	private int counter = 0;

	/**
	 * No-arg constructor for serialization.
	 */
	private MoveAction() {
		this.source = null;
		this.target = null;
		this.delay = 0;
	}

	public MoveAction(HasPosition source, Tile target) {
		this(source, target, 2);
	}

	public MoveAction(HasPosition source, Tile target, float delay) {
		this(source, target.coord(), delay);
	}

	public MoveAction(HasPosition source, TileCoordinate target, float delay) {
		this.source = source;
		this.target = target;
		this.delay = delay;
	}

	@Override
	public void update(World world) {
		if (counter >= delay) {
			counter = 0;
			List<Tile> path = world.map().path(source.tile(), world.getTile(target));
			if (path.size() > 1) {
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
		return 5;
	}

}
