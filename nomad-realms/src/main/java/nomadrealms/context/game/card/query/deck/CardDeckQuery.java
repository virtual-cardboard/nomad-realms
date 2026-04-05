package nomadrealms.context.game.card.query.deck;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;

import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.zone.Deck;
import nomadrealms.event.game.effect.EffectContext;

public class CardDeckQuery implements Query<Deck> {

	private final Query<WorldCard> cardQuery;

	public CardDeckQuery(Query<WorldCard> cardQuery) {
		this.cardQuery = cardQuery;
	}

	@Override
	public List<Deck> find(EffectContext context) {
		return cardQuery.find(context).stream()
				.map(WorldCard::deck)
				.filter(Objects::nonNull)
				.distinct()
				.collect(toList());
	}

}
