package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.action.ThenAction;
import nomadrealms.context.game.card.expression.CardExpression;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class ThenEffect extends Effect {

	private CardExpression expression1;
	private CardExpression expression2;
	private Target target;
	private CardPlayer source;

	/**
	 * No-arg constructor for serialization.
	 */
	public ThenEffect() {
	}

	public ThenEffect(CardExpression expression1, CardExpression expression2, Target target, CardPlayer source) {
		this.expression1 = expression1;
		this.expression2 = expression2;
		this.target = target;
		this.source = source;
	}

	@Override
	public void resolve(World world) {
		source.queueAction(new ThenAction(expression1, expression2, target, source));
	}

}
