package nomadrealms.context.game.card.query.card;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

/**
 * A query expression that can be used by card expressions and intents to find the current card being played.
 *
 * @author Lunkle
 */
public class SelfCardQuery implements Query<WorldCard> {

	@Override
	public List<WorldCard> find(World world, Actor source, Target target, WorldCard card) {
		return singletonList(card);
	}

}
