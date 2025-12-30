package nomadrealms.context.game.card.query.actor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class ActorsOnTilesQuery implements Query<CardPlayer> {

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
	public List<CardPlayer> find(World world, CardPlayer source) {
		List<Tile> tiles = tileQuery.find(world, source);
		Stream<CardPlayer> stream = world.actors.stream()
				.filter(CardPlayer.class::isInstance)
				.map(CardPlayer.class::cast)
				.filter(actor -> tiles.contains(actor.tile()));
		if (excludeSource) {
			stream = stream.filter(actor -> actor != source);
		}
		return stream.collect(Collectors.toList());
	}

}
