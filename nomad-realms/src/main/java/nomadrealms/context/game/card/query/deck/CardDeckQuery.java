package nomadrealms.context.game.card.query.deck;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.zone.Deck;

public class CardDeckQuery implements Query<Deck> {

	private final Query<WorldCard> cardQuery;

	public CardDeckQuery(Query<WorldCard> cardQuery) {
		this.cardQuery = cardQuery;
	}

	@Override
	public List<Deck> find(World world, Actor source, Target target, WorldCard card) {
		return cardQuery.find(world, source, target, card).stream()
				.map(WorldCard::deck)
				.filter(Objects::nonNull)
				.distinct()
				.collect(toList());
	}

}
