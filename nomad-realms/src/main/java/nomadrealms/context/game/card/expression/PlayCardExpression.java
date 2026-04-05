package nomadrealms.context.game.card.expression;

import static java.util.stream.Collectors.toList;

import java.util.List;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.PlayCardEffect;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class PlayCardExpression implements CardExpression {

	private final Query<WorldCard> cardQuery;

	public PlayCardExpression(Query<WorldCard> cardQuery) {
		this.cardQuery = cardQuery;
	}

	public static PlayCardExpression playCard(Query<WorldCard> cardQuery) {
		return new PlayCardExpression(cardQuery);
	}

	@Override
	public List<Effect> effects(World world, Target target, CardPlayer source, WorldCard card) {
		return cardQuery.find(world, source, target, card).stream()
				.map(c -> new PlayCardEffect(source, c))
				.collect(toList());
	}

}
