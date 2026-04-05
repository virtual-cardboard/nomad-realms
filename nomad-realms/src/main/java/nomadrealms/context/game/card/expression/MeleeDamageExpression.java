package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.MeleeDamageEffect;
import nomadrealms.event.game.effect.EffectContext;

public class MeleeDamageExpression implements CardExpression {

	private final int amount;

	public MeleeDamageExpression(int amount) {
		this.amount = amount;
	}

	public static MeleeDamageExpression meleeDamage(int amount) {
		return new MeleeDamageExpression(amount);
	}

	@Override
	public List<Effect> effects(EffectContext context) {
		return singletonList(new MeleeDamageEffect(context.target(), context.source(), amount));
	}

}
