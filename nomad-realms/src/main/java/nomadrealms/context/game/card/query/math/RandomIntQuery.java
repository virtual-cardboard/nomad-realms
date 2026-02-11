package nomadrealms.context.game.card.query.math;

import static java.util.Collections.singletonList;

import java.util.List;
import java.util.Random;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class RandomIntQuery implements Query<Integer> {

	private final int min;
	private final int max;
	private final Random random = new Random();

	public RandomIntQuery(int min, int max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public List<Integer> find(World world, Actor source, Target target) {
		return singletonList(random.nextInt(max - min + 1) + min);
	}
}
