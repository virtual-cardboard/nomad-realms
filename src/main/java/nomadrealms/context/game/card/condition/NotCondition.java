package nomadrealms.context.game.card.condition;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class NotCondition implements Condition {

	private final Condition condition;

	public NotCondition(Condition condition) {
		this.condition = condition;
	}

	public boolean test(World world, Target target, CardPlayer source) {
		return !condition.test(world, target, source);
	}

}
