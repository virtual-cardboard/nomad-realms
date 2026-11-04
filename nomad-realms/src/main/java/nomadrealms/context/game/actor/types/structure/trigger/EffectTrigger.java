package nomadrealms.context.game.actor.types.structure.trigger;

import nomadrealms.context.game.card.expression.CardExpression;
import engine.serialization.Derializable;

@Derializable
public class EffectTrigger extends Trigger {

	public EffectTrigger(CardExpression expression) {
		super(expression);
	}

	/**
	 * No-arg constructor for serialization.
	 */
	EffectTrigger() {
		super(null);
	}

}
