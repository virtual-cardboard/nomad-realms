package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.status.StatusEffect;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.ApplyStatusEffect;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class SelfApplyStatusExpression implements CardExpression {

	private final StatusEffect statusEffect;
	private final int count;

	public SelfApplyStatusExpression(StatusEffect statusEffect, int count) {
		this.statusEffect = statusEffect;
		this.count = count;
	}

	@Override
	public List<Effect> effects(World world, Target target, CardPlayer source) {
		return singletonList(new ApplyStatusEffect(source, statusEffect, count));
	}

}
