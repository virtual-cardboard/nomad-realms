package nomadrealms.context.game.card.query.math;

import static java.lang.Math.min;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.util.List;
import java.util.Optional;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class MinQuery implements Query<Integer> {

	private final Query<Integer> query1;
	private final Query<Integer> query2;

	public MinQuery(Query<Integer> query1, Query<Integer> query2) {
		this.query1 = query1;
		this.query2 = query2;
	}

	@Override
	public List<Integer> find(World world, Actor source, Target target) {
		Optional<Integer> value1 = query1.find(world, source, target).stream().findFirst();
		Optional<Integer> value2 = query2.find(world, source, target).stream().findFirst();
		if (value1.isPresent() && value2.isPresent()) {
			return singletonList(min(value1.get(), value2.get()));
		} else if (value1.isPresent()) {
			return singletonList(value1.get());
		} else if (value2.isPresent()) {
			return singletonList(value2.get());
		}
		return emptyList();
	}
}
