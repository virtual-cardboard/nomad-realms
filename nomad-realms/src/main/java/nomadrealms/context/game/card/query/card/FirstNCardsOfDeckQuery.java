package nomadrealms.context.game.card.query.card;

import java.util.ArrayList;
import java.util.List;

import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.zone.Deck;
import nomadrealms.event.game.effect.EffectContext;

public class FirstNCardsOfDeckQuery implements Query<WorldCard> {

	private final Query<Deck> deckQuery;
	private final int n;

	public FirstNCardsOfDeckQuery(Query<Deck> deckQuery, int n) {
		this.deckQuery = deckQuery;
		this.n = n;
	}

	@Override
	public List<WorldCard> find(EffectContext context) {
		List<Deck> decks = deckQuery.find(context);
		List<WorldCard> results = new ArrayList<>();
		for (Deck deck : decks) {
			List<WorldCard> cardsInDeck = deck.getCards();
			for (int i = 0; i < Math.min(n, cardsInDeck.size()); i++) {
				results.add(cardsInDeck.get(i));
			}
		}
		return results;
	}

}
