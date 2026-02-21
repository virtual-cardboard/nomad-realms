package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.MeleeDamageEffect;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class MeleeDamageExpression implements CardExpression {

	private final int amount;

	public MeleeDamageExpression(int amount) {
		this.amount = amount;
	}

	public static MeleeDamageExpression meleeDamage(int amount) {
		return new MeleeDamageExpression(amount);
	}

	@Override
	public List<Effect> effects(World world, Target target, Actor source) {
		return singletonList(new MeleeDamageEffect(target, source, amount));
	}

}
