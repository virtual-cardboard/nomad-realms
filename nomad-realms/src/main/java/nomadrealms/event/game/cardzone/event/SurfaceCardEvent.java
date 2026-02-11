package nomadrealms.event.game.cardzone.event;

import nomadrealms.context.game.card.Card;
import nomadrealms.event.game.cardzone.CardZoneEvent;
import nomadrealms.event.game.cardzone.CardZoneListener;

public class SurfaceCardEvent<T extends Card> implements CardZoneEvent<T> {

	private final T card;

	public SurfaceCardEvent(T card) {
		this.card = card;
	}

	public T card() {
		return card;
	}

	public void handle(CardZoneListener<T> listener) {
		listener.handle(this);
	}
}
