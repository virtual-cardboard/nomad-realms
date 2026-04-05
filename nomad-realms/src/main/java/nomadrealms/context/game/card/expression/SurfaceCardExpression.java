package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.SurfaceCardEffect;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.event.game.effect.EffectContext;

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
	public List<Effect> effects(EffectContext context) {
		List<WorldCard> cards = query.find(context);
		return singletonList(new SurfaceCardEffect((CardPlayer) context.source(), cards));
	}

}
