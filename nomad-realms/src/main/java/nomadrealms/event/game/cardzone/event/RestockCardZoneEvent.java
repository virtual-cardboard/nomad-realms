package nomadrealms.event.game.cardzone.event;

import nomadrealms.context.game.card.Card;
import nomadrealms.context.game.zone.CardZone;
import nomadrealms.event.game.cardzone.CardZoneEvent;
import nomadrealms.event.game.cardzone.CardZoneListener;

public class RestockCardZoneEvent<T extends Card> implements CardZoneEvent<T> {

	private final CardZone<T> zone;

	public RestockCardZoneEvent(CardZone<T> zone) {
		this.zone = zone;
	}

	public CardZone<T> zone() {
		return zone;
	}

	@Override
	public void handle(CardZoneListener<T> listener) {
		listener.handle(this);
	}

}
