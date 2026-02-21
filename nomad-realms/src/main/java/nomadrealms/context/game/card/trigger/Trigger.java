package nomadrealms.context.game.card.trigger;

import java.util.Collections;
import java.util.List;

import engine.serialization.Derializable;
import nomadrealms.context.game.actor.types.structure.Structure;
import nomadrealms.context.game.card.action.Action;
import nomadrealms.context.game.card.action.DelayedEffectAction;
import nomadrealms.context.game.card.condition.Condition;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.expression.CardExpression;
import nomadrealms.context.game.world.World;

@Derializable
public abstract class Trigger {

	private Condition condition;
	private CardExpression expression;
	private int preDelay;
	private int postDelay;

	protected Trigger() {
	}

	public Trigger(Condition condition, CardExpression expression, int preDelay, int postDelay) {
		this.condition = condition;
		this.expression = expression;
		this.preDelay = preDelay;
		this.postDelay = postDelay;
	}

	public abstract boolean matches(World world, Structure self, Effect effect);

	public List<Action> createActions(World world, Structure self, Effect effect) {
		return Collections.singletonList(
				new DelayedEffectAction(expression, preDelay, postDelay, effect.target(), self)
		);
	}

	public Condition condition() {
		return condition;
	}

	public CardExpression expression() {
		return expression;
	}

	public int preDelay() {
		return preDelay;
	}

	public int postDelay() {
		return postDelay;
	}

}
