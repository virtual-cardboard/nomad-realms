package nomadrealms.context.game.card.query.math;

import static java.util.stream.Collectors.toList;

import engine.serialization.Derializable;
import java.util.List;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

@Derializable
public class StackSizeQuery implements Query<Integer> {

	private Query<CardPlayer> source;

	protected StackSizeQuery() {
	}

	public StackSizeQuery(Query<CardPlayer> source) {
		this.source = source;
	}

	@Override
	public List<Integer> find(World world, Actor source, Target target) {
		return this.source.find(world, source, target).stream()
				.map(player -> player.cardStack().size())
				.collect(toList());
	}
}
