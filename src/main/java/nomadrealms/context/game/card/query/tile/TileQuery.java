package nomadrealms.context.game.card.query.tile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class TileQuery implements Query<Tile> {

	private final Query<? extends Target> targetQuery;

	public TileQuery(Query<? extends Target> targetQuery) {
		this.targetQuery = targetQuery;
	}

	@Override
	public List<Tile> find(World world, Actor source, Target target) {
		return targetQuery.find(world, source, target).stream()
				.map(Target::tile)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

}
