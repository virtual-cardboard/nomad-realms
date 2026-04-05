package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.HealEffect;
import nomadrealms.event.game.effect.EffectContext;

public class SelfHealExpression implements CardExpression {

	private final int amount;

	public SelfHealExpression(int amount) {
		this.amount = amount;
	}

	public static SelfHealExpression selfHeal(int amount) {
		return new SelfHealExpression(amount);
	}

	@Override
	public List<Effect> effects(EffectContext context) {
		return singletonList(new HealEffect(context.source(), (CardPlayer) context.source(), amount));
	}

}
