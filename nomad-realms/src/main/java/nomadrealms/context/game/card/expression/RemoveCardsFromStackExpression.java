package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.List;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.RemoveCardsFromStackEffect;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.card.query.zone.GetCardsInZoneQuery;
import nomadrealms.context.game.zone.CardStackEntry;
import nomadrealms.context.game.zone.CardZone;
import nomadrealms.event.game.effect.EffectContext;

public class RemoveCardsFromStackExpression implements CardExpression {

	private final Query<CardStackEntry> cardsQuery;

	public RemoveCardsFromStackExpression(Query<CardStackEntry> cardsQuery) {
		this.cardsQuery = cardsQuery;
	}

	public static RemoveCardsFromStackExpression removeCardsFromStack(Query<CardStackEntry> cardsQuery) {
		return new RemoveCardsFromStackExpression(cardsQuery);
	}

	@Override
	public List<Effect> effects(EffectContext context) {
		List<CardStackEntry> entries = cardsQuery.find(context);
		List<CardZone> zones = new ArrayList<>();
		if (cardsQuery instanceof GetCardsInZoneQuery) {
			GetCardsInZoneQuery getCardsQuery = (GetCardsInZoneQuery) cardsQuery;
			zones.addAll(getCardsQuery.zoneQuery().find(context));
		}
		if (context.target() instanceof CardPlayer) {
			zones.add(((CardPlayer) context.target()).cardStack());
		}
		return singletonList(new RemoveCardsFromStackEffect(context.source(), zones, entries));
	}

}
