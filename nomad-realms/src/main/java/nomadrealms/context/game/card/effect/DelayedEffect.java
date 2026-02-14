package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.action.DelayedEffectAction;
import nomadrealms.context.game.card.expression.CardExpression;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class DelayedEffect extends Effect {

	private CardExpression expression;
	private int preDelay;
	private int postDelay;
	private Target target;

	/**
	 * No-arg constructor for serialization.
	 */
	public DelayedEffect() {
	}

	public DelayedEffect(CardExpression expression, int preDelay, int postDelay, Target target, Actor source) {
		this.expression = expression;
		this.preDelay = preDelay;
		this.postDelay = postDelay;
		this.target = target;
		this.source = source;
	}

	@Override
	public void resolve(World world) {
		source.queueAction(new DelayedEffectAction(expression, preDelay, postDelay, target, source));
	}

}
