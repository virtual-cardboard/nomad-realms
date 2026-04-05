package nomadrealms.context.game.card.query.tile;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.HasPosition;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.event.game.effect.EffectContext;

public class PreviousTileQuery implements Query<Tile> {

	private final Query<Actor> player;

	public PreviousTileQuery(Query<Actor> player) {
		this.player = player;
	}

	@Override
	public List<Tile> find(EffectContext context) {
		return player.find(context).stream()
				.map(HasPosition::previousTile)
				.filter(Objects::nonNull)
				.collect(toList());
	}

}
