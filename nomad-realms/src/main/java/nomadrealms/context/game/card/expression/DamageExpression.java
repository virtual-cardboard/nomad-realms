package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.effect.DamageEffect;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.card.query.math.LiteralQuery;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class DamageExpression implements CardExpression {

	private final Query<Integer> amount;

	public DamageExpression(int amount) {
		this.amount = new LiteralQuery(amount);
	}

	public DamageExpression(Query<Integer> amount) {
		this.amount = amount;
	}

	public static DamageExpression damage(int amount) {
		return new DamageExpression(amount);
	}

	public static DamageExpression damage(Query<Integer> amount) {
		return new DamageExpression(amount);
	}

	@Override
	public List<Effect> effects(World world, Target target, Actor source) {
		int amount = this.amount.find(world, source, target).get(0);
		return singletonList(new DamageEffect(target, source, amount));
	}

}
