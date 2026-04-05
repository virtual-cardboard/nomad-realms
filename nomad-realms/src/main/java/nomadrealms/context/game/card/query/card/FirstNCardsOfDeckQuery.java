package nomadrealms.context.game.card.query.card;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.zone.Deck;

public class FirstNCardsOfDeckQuery implements Query<WorldCard> {

	private final Query<Deck> deckQuery;
	private final int n;

	public FirstNCardsOfDeckQuery(Query<Deck> deckQuery, int n) {
		this.deckQuery = deckQuery;
		this.n = n;
	}

	@Override
	public List<WorldCard> find(World world, Actor source, Target target, WorldCard card) {
		List<Deck> decks = deckQuery.find(world, source, target, card);
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
