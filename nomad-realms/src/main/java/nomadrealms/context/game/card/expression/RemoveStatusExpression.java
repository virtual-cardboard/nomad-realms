package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.status.StatusEffect;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.RemoveStatusEffect;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.event.game.effect.EffectContext;

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
	public List<Effect> effects(EffectContext context) {
		int count = this.count.find(context).get(0);
		return singletonList(new RemoveStatusEffect((CardPlayer) context.source(), context.target(), statusEffect, count));
	}
}
