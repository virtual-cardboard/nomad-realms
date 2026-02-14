package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.HealEffect;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class SelfHealExpression implements CardExpression {

	private final int amount;

	public SelfHealExpression(int amount) {
		this.amount = amount;
	}

	public static SelfHealExpression selfHeal(int amount) {
		return new SelfHealExpression(amount);
	}

	@Override
	public List<Effect> effects(World world, Target target, Actor source) {
		return singletonList(new HealEffect(source, source, amount));
	}

}
