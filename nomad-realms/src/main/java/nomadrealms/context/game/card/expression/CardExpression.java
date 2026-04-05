package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.event.game.effect.EffectContext;

public interface CardExpression {

	public default List<CardExpression> list() {
		return singletonList(this);
	}

	public List<Effect> effects(EffectContext context);

}
