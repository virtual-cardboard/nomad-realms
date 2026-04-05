package nomadrealms.context.game.card.query.math;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.card.query.Query;
import nomadrealms.event.game.effect.EffectContext;

public class LiteralQuery implements Query<Integer> {

	private final int value;

	public LiteralQuery(int value) {
		this.value = value;
	}

	@Override
	public List<Integer> find(EffectContext context) {
		return singletonList(value);
	}
}
