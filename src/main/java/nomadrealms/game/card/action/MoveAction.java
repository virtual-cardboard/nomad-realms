package nomadrealms.game.card.action;

import java.util.List;

import nomadrealms.game.actor.HasPosition;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;

public class MoveAction implements Action {

	private final HasPosition source;
	private final TileCoordinate target;

	/**
	 * No-arg constructor for serialization.
	 */
	private MoveAction() {
		this.source = null;
		this.target = null;
	}

	public MoveAction(HasPosition source, Tile target) {
		this(source, target.coord());
	}

	public MoveAction(HasPosition source, TileCoordinate target) {
		this.source = source;
		this.target = target;
	}

	@Override
	public void update(World world) {
		List<Tile> path = world.map().path(source.tile(), world.getTile(target));
		if (path.size() > 1) {
			source.move(path.get(1));
		}
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
