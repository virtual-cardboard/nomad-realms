package nomadrealms.context.game.card.query.card;

import static java.util.stream.Collectors.toList;

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
		return deckQuery.find(world, source, target, card).stream()
				.flatMap(deck -> deck.getCards().stream().limit(n))
				.filter(Objects::nonNull)
				.collect(toList());
	}

}
