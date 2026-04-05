package nomadrealms.context.game.card.query.math;

import static java.util.Collections.singletonList;

import java.util.List;
import java.util.Random;

import nomadrealms.context.game.card.query.Query;
import nomadrealms.event.game.effect.EffectContext;

public class RandomIntQuery implements Query<Integer> {

	private final int min;
	private final int max;
	private final Random random = new Random();

	public RandomIntQuery(int min, int max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public List<Integer> find(EffectContext context) {
		return singletonList(random.nextInt(max - min + 1) + min);
	}
}
