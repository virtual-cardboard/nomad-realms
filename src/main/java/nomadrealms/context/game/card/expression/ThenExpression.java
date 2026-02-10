package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.ThenEffect;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class ThenExpression implements CardExpression {

	private CardExpression expression1;
	private CardExpression expression2;

	/**
	 * No-arg constructor for serialization.
	 */
	public ThenExpression() {
	}

	public ThenExpression(CardExpression expression1, CardExpression expression2) {
		if (expression1 instanceof ThenExpression) {
			ThenExpression inner = (ThenExpression) expression1;
			this.expression1 = inner.expression1;
			this.expression2 = new ThenExpression(inner.expression2, expression2);
		} else {
			this.expression1 = expression1;
			this.expression2 = expression2;
		}
	}

	@Override
	public List<Effect> effects(World world, Target target, CardPlayer source) {
		return singletonList(new ThenEffect(expression1, expression2, target, source));
	}

}
