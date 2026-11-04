package nomadrealms.context.game.card.query.math;

import static java.util.Collections.singletonList;

import java.util.List;
import java.util.Random;

import nomadrealms.context.game.card.query.Query;
import nomadrealms.event.game.effect.EffectContext;
import engine.serialization.Derializable;

@Derializable
public class RandomIntQuery implements Query<Integer> {

	private final int min;
	private final int max;
	/**
	 * Transient: {@link Random} carries no serializable state; a restored query uses a fresh RNG.
	 */
	private transient final Random random = new Random();

	public RandomIntQuery(int min, int max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public List<Integer> find(EffectContext context) {
		return singletonList(random.nextInt(max - min + 1) + min);
	}
	/**
	 * No-arg constructor for serialization.
	 */
	RandomIntQuery() {
		this.min = 0;
		this.max = 0;
	}
}

