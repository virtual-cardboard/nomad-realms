package nomadrealms.context.game.card.query.zone;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;

import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.zone.CardStackEntry;
import nomadrealms.context.game.zone.CardZone;
import nomadrealms.event.game.effect.EffectContext;

public class GetCardsInZoneQuery implements Query<CardStackEntry> {

	private final Query<? extends CardZone> zoneQuery;

	public GetCardsInZoneQuery(Query<? extends CardZone> zoneQuery) {
		this.zoneQuery = zoneQuery;
	}

	public static GetCardsInZoneQuery getCardsInZone(Query<? extends CardZone> zoneQuery) {
		return new GetCardsInZoneQuery(zoneQuery);
	}

	public Query<? extends CardZone> zoneQuery() {
		return zoneQuery;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CardStackEntry> find(EffectContext context) {
		return zoneQuery.find(context).stream()
				.filter(Objects::nonNull)
				.flatMap(zone -> ((List<Object>) (List<?>) zone.getCards()).stream())
				.filter(CardStackEntry.class::isInstance)
				.map(CardStackEntry.class::cast)
				.collect(toList());
	}

}
