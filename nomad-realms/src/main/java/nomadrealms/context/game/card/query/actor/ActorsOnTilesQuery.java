package nomadrealms.context.game.card.query.actor;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.event.game.effect.EffectContext;

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
	public List<Actor> find(EffectContext context) {
		List<Tile> tiles = tileQuery.find(context);
		Stream<Actor> stream = tiles.stream()
				.map(Tile::actor)
				.filter(Objects::nonNull);
		if (excludeSource) {
			stream = stream.filter(actor -> actor != context.source());
		}
		return stream.collect(toList());
	}

}
