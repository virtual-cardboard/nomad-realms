package nomadrealms.game.card.expression;

import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.event.Target;
import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.world.World;

import java.util.List;

public interface CardExpression {

	public List<Intent> intents(World world, Target target, CardPlayer source);

	default boolean isInRange(World world, Target target, CardPlayer source, int range) {
		int distance = world.calculateDistance(source.tile(), target.tile());
		return distance <= range;
	}
}
