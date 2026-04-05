package nomadrealms.context.game.card.query.actor;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.event.game.effect.EffectContext;

public class SelfQuery<T extends Actor> implements Query<T> {

	@Override
	public List<T> find(EffectContext context) {
		return singletonList((T) context.source());
	}

}
