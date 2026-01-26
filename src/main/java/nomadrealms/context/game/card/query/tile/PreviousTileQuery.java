package nomadrealms.context.game.card.query.tile;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.HasPosition;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

/**
 * A query expression that can be used by card expressions and intents to find {@link Tile Tiles} in the game world.
 *
 * @author Lunkle
 */
public class PreviousTileQuery implements Query<Tile> {

	private final Query<Actor> player;

	public PreviousTileQuery(Query<Actor> player) {
		this.player = player;
	}

	@Override
	public List<Tile> find(World world, Actor source, Target target) {
		return player.find(world, source, target).stream()
				.map(HasPosition::previousTile)
				.filter(Objects::nonNull)
				.collect(toList());
	}

}
