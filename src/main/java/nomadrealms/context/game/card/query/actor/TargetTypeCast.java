package nomadrealms.context.game.card.query.actor;

import static java.util.stream.Collectors.toList;

import java.util.List;

import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class TargetTypeCast<T extends Target> implements Query<T> {

	private final Query<Target> query;

	public TargetTypeCast(Query<Target> query) {
		this.query = query;
	}

	@Override
	public List<T> find(World world, CardPlayer source, Target target) {
		return query.find(world, source, target).stream().map(t -> (T) t).collect(toList());
	}

}
