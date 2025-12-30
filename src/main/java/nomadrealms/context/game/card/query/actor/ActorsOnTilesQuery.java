package nomadrealms.context.game.card.query.actor;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.query.Query;
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
	public List<Actor> find(World world, CardPlayer source) {
		List<Tile> tiles = tileQuery.find(world, source);
		// TODO: Tiles need to store the actors on them for efficiency. Once we have that, we can optimize this.
		Stream<CardPlayer> stream = world.actors.stream()
				.filter(CardPlayer.class::isInstance)
				.map(CardPlayer.class::cast)
				.filter(actor -> tiles.contains(actor.tile()));
		if (excludeSource) {
			stream = stream.filter(actor -> actor != source);
		}
		return stream.collect(toList());
	}

}
