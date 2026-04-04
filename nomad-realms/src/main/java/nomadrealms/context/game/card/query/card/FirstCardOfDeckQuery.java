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

public class FirstCardOfDeckQuery implements Query<WorldCard> {

	private final Query<Deck> deckQuery;

	public FirstCardOfDeckQuery(Query<Deck> deckQuery) {
		this.deckQuery = deckQuery;
	}

	@Override
	public List<WorldCard> find(World world, Actor source, Target target, WorldCard card) {
		return deckQuery.find(world, source, target, card).stream()
				.map(Deck::peek)
				.filter(Objects::nonNull)
				.collect(toList());
	}

}
