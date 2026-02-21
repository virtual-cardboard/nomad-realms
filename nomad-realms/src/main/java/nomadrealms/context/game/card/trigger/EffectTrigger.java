package nomadrealms.context.game.card.trigger;

import engine.serialization.Derializable;
import nomadrealms.context.game.actor.types.structure.Structure;
import nomadrealms.context.game.card.condition.Condition;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.expression.CardExpression;
import nomadrealms.context.game.world.World;

@Derializable
public class EffectTrigger extends Trigger {

	private String effectClassName;

	protected EffectTrigger() {
	}

	public EffectTrigger(String effectClassName, Condition condition, CardExpression expression, int preDelay, int postDelay) {
		super(condition, expression, preDelay, postDelay);
		this.effectClassName = effectClassName;
	}

	@Override
	public boolean matches(World world, Structure self, Effect effect) {
		return effect.getClass().getSimpleName().equals(effectClassName)
				&& condition().test(world, effect.target(), effect.source(), self);
	}

}
