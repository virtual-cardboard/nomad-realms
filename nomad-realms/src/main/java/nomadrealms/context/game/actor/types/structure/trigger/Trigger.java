package nomadrealms.context.game.actor.types.structure.trigger;

import java.util.List;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.expression.CardExpression;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

/**
 * Represents a way in which a structure can fire off its events.
 */
public class Trigger {

	private final CardExpression expression;

	public Trigger(CardExpression expression) {
		this.expression = expression;
	}

	public List<Effect> effects(World world, Target target, CardPlayer source) {
		return expression.effects(world, target, source);
	}

}
