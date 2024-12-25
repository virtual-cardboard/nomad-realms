package nomadrealms.game.card.action;

import java.util.List;

import nomadrealms.game.actor.HasPosition;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Tile;

public class MoveAction implements Action {

	private final HasPosition source;
	private final Tile target;

	public MoveAction(HasPosition source, Tile target) {
		this.source = source;
		this.target = target;
	}

	@Override
	public void update(World world) {
		List<Tile> path = world.map().path(source.tile(), target);
		if (!path.isEmpty()) {
			source.move(path.get(0));
		}
	}

	@Override
	public boolean isComplete() {
		return source.tile().equals(target);
	}

}
