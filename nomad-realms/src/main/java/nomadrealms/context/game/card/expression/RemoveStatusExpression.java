package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.status.StatusEffect;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.RemoveStatusEffect;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class RemoveStatusExpression implements CardExpression {

	private final StatusEffect statusEffect;
	private final Query<Integer> count;

	public RemoveStatusExpression(StatusEffect statusEffect, Query<Integer> count) {
		this.statusEffect = statusEffect;
		this.count = count;
	}

	public static RemoveStatusExpression removeStatus(StatusEffect statusEffect, Query<Integer> count) {
		return new RemoveStatusExpression(statusEffect, count);
	}

	@Override
	public List<Effect> effects(World world, Target target, CardPlayer source) {
		int count = this.count.find(world, source, target).get(0);
		return singletonList(new RemoveStatusEffect(source, target, statusEffect, count));
	}
}
