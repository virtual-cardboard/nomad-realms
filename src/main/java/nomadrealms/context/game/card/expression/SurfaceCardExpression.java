package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.intent.Intent;
import nomadrealms.context.game.card.intent.SurfaceCardIntent;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class SurfaceCardExpression implements CardExpression {

	private final Query<WorldCard> query;
	int delay = 0;

	public SurfaceCardExpression(Query<WorldCard> query, int delay) {
		this.query = query;
		this.delay = delay;
	}

	@Override
	public List<Intent> intents(World world, Target target, CardPlayer source) {
		return singletonList(new SurfaceCardIntent(source, query));
	}

}
