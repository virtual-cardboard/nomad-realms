package nomadrealms.context.game.card.expression;

import static java.util.stream.Collectors.toList;

import java.util.List;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.card.effect.AddCardToStackEffect;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.event.game.effect.EffectContext;

public class AddCardToStackExpression implements CardExpression {

	private final GameCard cardToPlay;
	private final Query<CardPlayer> targets;

	public AddCardToStackExpression(GameCard card, Query<CardPlayer> targets) {
		this.cardToPlay = card;
		this.targets = targets;
	}

	public static AddCardToStackExpression addCardToStack(GameCard card, Query<CardPlayer> targets) {
		return new AddCardToStackExpression(card, targets);
	}

	@Override
	public List<Effect> effects(EffectContext context) {
		return targets.find(context).stream()
				.map(t -> new AddCardToStackEffect(context.source(), t, cardToPlay))
				.collect(toList());
	}

}
