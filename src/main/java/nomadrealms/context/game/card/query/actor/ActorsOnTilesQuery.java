package nomadrealms.context.game.card.query.actor;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class ActorsOnTilesQuery implements Query<Actor> {

	private final Query<Tile> tileQuery;
	private final boolean excludeSource;

	public ActorsOnTilesQuery(Query<Tile> tileQuery, boolean excludeSource) {
		this.tileQuery = tileQuery;
		this.excludeSource = excludeSource;
	}

	public ActorsOnTilesQuery(Query<Tile> tileQuery) {
		this(tileQuery, false);
	}

	@Override
	public List<Actor> find(World world, CardPlayer source, Target target) {
		List<Tile> tiles = tileQuery.find(world, source, target);
		Stream<Actor> stream = tiles.stream()
				.map(Tile::actor)
				.filter(Objects::nonNull);
		if (excludeSource) {
			stream = stream.filter(actor -> actor != source);
		}
		return stream.collect(toList());
	}

}
