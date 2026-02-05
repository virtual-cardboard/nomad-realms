package nomadrealms.context.game.card.query.tile;

import java.util.List;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class TargetTileQuery implements Query<Tile> {

	@Override
	public List<Tile> find(World world, Actor source, Target target) {
		if (target == null || target.tile() == null) {
			return emptyList();
		}
		return singletonList(target.tile());
	}

}
