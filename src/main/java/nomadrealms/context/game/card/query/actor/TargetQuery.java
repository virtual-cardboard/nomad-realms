package nomadrealms.context.game.card.query.actor;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

/**
 * A query expression that can be used by card expressions and intents to find {@link CardPlayer CardPlayers} in the
 * game world.
 *
 * @author Lunkle
 */
public class TargetQuery implements Query<Target> {

	@Override
	public List<Target> find(World world, CardPlayer source, Target target) {
		return singletonList(target);
	}

}