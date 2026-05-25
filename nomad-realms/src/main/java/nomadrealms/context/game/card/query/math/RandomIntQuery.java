package nomadrealms.context.game.card.query.math;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.card.query.Query;
import nomadrealms.event.game.effect.EffectContext;

public class RandomIntQuery implements Query<Integer> {

	private final int min;
	private final int max;

	public RandomIntQuery(int min, int max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public List<Integer> find(EffectContext context) {
		return singletonList(context.source().tile().chunk().zone().rng().nextInt(max - min + 1) + min);
	}
}
