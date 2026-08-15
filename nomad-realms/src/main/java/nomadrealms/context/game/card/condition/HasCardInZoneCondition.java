package nomadrealms.context.game.card.condition;

import java.util.Objects;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.zone.CardStackEntry;
import nomadrealms.context.game.zone.CardZone;
import nomadrealms.event.game.effect.EffectContext;

public class HasCardInZoneCondition implements Condition {

	private final Query<? extends CardZone> zoneQuery;
	private final GameCard card;

	public HasCardInZoneCondition(Query<? extends CardZone> zoneQuery, GameCard card) {
		this.zoneQuery = zoneQuery;
		this.card = card;
	}

	public static HasCardInZoneCondition hasCardInZone(Query<? extends CardZone> zoneQuery, GameCard card) {
		return new HasCardInZoneCondition(zoneQuery, card);
	}

	@Override
	public boolean test(World world, Target target, CardPlayer source) {
		EffectContext context = new EffectContext().world(world).source(source).target(target);
		return zoneQuery.find(context).stream()
				.filter(Objects::nonNull)
				.flatMap(zone -> zone.getCards().stream())
				.anyMatch(this::matches);
	}

	private boolean matches(Object item) {
		if (item == null) {
			return false;
		}
		if (item instanceof CardStackEntry) {
			CardStackEntry entry = (CardStackEntry) item;
			return entry.event() != null && entry.event().card() != null && card.equals(entry.event().card().card());
		}
		if (item instanceof WorldCard) {
			WorldCard worldCard = (WorldCard) item;
			return card.equals(worldCard.card());
		}
		if (item instanceof GameCard) {
			return card.equals(item);
		}
		return false;
	}

}
