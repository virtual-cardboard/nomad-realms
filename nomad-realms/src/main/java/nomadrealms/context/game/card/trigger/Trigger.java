package nomadrealms.context.game.card.trigger;

import engine.serialization.Derializable;
import nomadrealms.context.game.actor.types.structure.Structure;
import nomadrealms.context.game.card.action.Action;
import nomadrealms.context.game.card.action.DelayedEffectAction;
import nomadrealms.context.game.card.condition.Condition;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.expression.CardExpression;
import nomadrealms.context.game.world.World;

import java.util.List;

import static java.util.Collections.singletonList;

@Derializable
public abstract class Trigger {

	private final CardExpression expression;
	private final Condition condition;

	protected Trigger() {
		this.expression = null;
		this.condition = null;
	}

	public Trigger(CardExpression expression, Condition condition) {
		this.expression = expression;
		this.condition = condition;
	}

	public abstract boolean matches(World world, Structure self, Effect effect);

	public CardExpression expression() {
		return expression;
	}

	public Condition condition() {
		return condition;
	}

	public List<Action> createActions(World world, Structure self, Effect effect) {
		return singletonList(new DelayedEffectAction(expression, 0, 0, effect.source(), self));
	}

}
