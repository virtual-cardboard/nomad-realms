package nomadrealms.context.game.card.query.actor;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.event.game.effect.EffectContext;

public class StaticTargetQuery<T extends Target> implements Query<T> {

	private final T target;

	public StaticTargetQuery(T target) {
		this.target = target;
	}

	@Override
	public List<T> find(EffectContext context) {
		return singletonList(target);
	}

}
