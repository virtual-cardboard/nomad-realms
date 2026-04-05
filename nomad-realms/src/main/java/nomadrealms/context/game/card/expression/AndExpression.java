package nomadrealms.context.game.card.expression;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.util.List;

import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.event.game.effect.EffectContext;

public class AndExpression implements CardExpression {

	private final CardExpression[] expressions;

	public AndExpression(CardExpression... expressions) {
		this.expressions = expressions;
	}

	public static AndExpression and(CardExpression... expressions) {
		return new AndExpression(expressions);
	}

	@Override
	public List<CardExpression> list() {
		return asList(expressions);
	}

	@Override
	public List<Effect> effects(EffectContext context) {
		return stream(expressions)
				.flatMap(expr -> expr.effects(context).stream())
				.collect(toList());
	}

}
