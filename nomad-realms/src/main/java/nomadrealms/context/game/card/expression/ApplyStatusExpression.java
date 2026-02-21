package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.status.StatusEffect;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.effect.ApplyStatusEffect;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class ApplyStatusExpression implements CardExpression {

	private final Query<? extends Target> target;
	private final StatusEffect statusEffect;
	private final Query<Integer> count;

	public ApplyStatusExpression(Query<? extends Target> target, StatusEffect statusEffect, Query<Integer> count) {
		this.target = target;
		this.statusEffect = statusEffect;
		this.count = count;
	}

	public static ApplyStatusExpression applyStatus(Query<? extends Target> target, StatusEffect statusEffect, Query<Integer> count) {
		return new ApplyStatusExpression(target, statusEffect, count);
	}

	@Override
	public List<Effect> effects(World world, Target target, Actor source) {
		Target t = this.target.find(world, source, target).get(0);
		int count = this.count.find(world, source, target).get(0);
		return singletonList(new ApplyStatusEffect(source, t, statusEffect, count));
	}
}
