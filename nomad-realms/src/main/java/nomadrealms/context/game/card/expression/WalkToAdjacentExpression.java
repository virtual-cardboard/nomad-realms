package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.WalkToAdjacentEffect;
import nomadrealms.event.game.effect.EffectContext;

public class WalkToAdjacentExpression implements CardExpression {

	private final int delay;

	public WalkToAdjacentExpression(int delay) {
		this.delay = delay;
	}

	public static WalkToAdjacentExpression walkToAdjacent(int delay) {
		return new WalkToAdjacentExpression(delay);
	}

	@Override
	public List<Effect> effects(EffectContext context) {
		return singletonList(new WalkToAdjacentEffect((CardPlayer) context.source(), context.target(), delay));
	}

}
