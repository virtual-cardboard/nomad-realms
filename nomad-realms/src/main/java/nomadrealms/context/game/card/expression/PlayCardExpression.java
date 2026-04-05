package nomadrealms.context.game.card.expression;

import static java.util.stream.Collectors.toList;

import java.util.List;

import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.PlayCardEffect;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.event.game.effect.EffectContext;

public class PlayCardExpression implements CardExpression {

	private final Query<WorldCard> cardQuery;

	public PlayCardExpression(Query<WorldCard> cardQuery) {
		this.cardQuery = cardQuery;
	}

	public static PlayCardExpression playCard(Query<WorldCard> cardQuery) {
		return new PlayCardExpression(cardQuery);
	}

	@Override
	public List<Effect> effects(EffectContext context) {
		return cardQuery.find(context).stream()
				.map(c -> new PlayCardEffect(context.source(), c))
				.collect(toList());
	}

}
