package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.DelayedEffect;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.event.game.effect.EffectContext;

public class DelayedExpression implements CardExpression {

	private CardExpression expression;
	private int preDelay;
	private int postDelay;

	/**
	 * No-arg constructor for serialization.
	 */
	public DelayedExpression() {
	}

	public DelayedExpression(CardExpression expression, int preDelay, int postDelay) {
		this.expression = expression;
		this.preDelay = preDelay;
		this.postDelay = postDelay;
	}

	public static DelayedExpression delayed(CardExpression expression, int preDelay, int postDelay) {
		return new DelayedExpression(expression, preDelay, postDelay);
	}

	@Override
	public List<Effect> effects(EffectContext context) {
		return singletonList(new DelayedEffect(expression, preDelay, postDelay, context.target(), (CardPlayer) context.source()));
	}

}
