package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.SurfaceCardEffect;
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

	public static SurfaceCardExpression surfaceCard(Query<WorldCard> query, int delay) {
		return new SurfaceCardExpression(query, delay);
	}

	@Override
	public List<Effect> effects(World world, Target target, Actor source) {
		List<WorldCard> cards = query.find(world, source, target);
		return singletonList(new SurfaceCardEffect(source, cards));
	}

}
