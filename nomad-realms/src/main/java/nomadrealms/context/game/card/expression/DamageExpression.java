package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.card.effect.DamageEffect;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.card.query.math.LiteralQuery;
import nomadrealms.event.game.effect.EffectContext;

public class DamageExpression implements CardExpression {

	private final Query<Integer> amount;

	public DamageExpression(int amount) {
		this.amount = new LiteralQuery(amount);
	}

	public DamageExpression(Query<Integer> amount) {
		this.amount = amount;
	}

	public static DamageExpression damage(int amount) {
		return new DamageExpression(amount);
	}

	public static DamageExpression damage(Query<Integer> amount) {
		return new DamageExpression(amount);
	}

	@Override
	public List<Effect> effects(EffectContext context) {
		int amount = this.amount.find(context).get(0);
		return singletonList(new DamageEffect(context.target(), context.source(), amount));
	}

}
