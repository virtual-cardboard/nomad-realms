package nomadrealms.context.game.card.query.zone;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.zone.CardZone;
import nomadrealms.event.game.effect.EffectContext;

public class CardZoneQuery implements Query<CardZone<?>> {

	private final Query<? extends CardPlayer> cardPlayerQuery;

	public CardZoneQuery(Query<? extends CardPlayer> cardPlayerQuery) {
		this.cardPlayerQuery = cardPlayerQuery;
	}

	public static CardZoneQuery cardZone(Query<? extends CardPlayer> cardPlayerQuery) {
		return new CardZoneQuery(cardPlayerQuery);
	}

	public Query<? extends CardPlayer> cardPlayerQuery() {
		return cardPlayerQuery;
	}

	@Override
	public List<CardZone<?>> find(EffectContext context) {
		return cardPlayerQuery.find(context).stream()
				.filter(Objects::nonNull)
				.map(CardPlayer::cardStack)
				.collect(toList());
	}

}
