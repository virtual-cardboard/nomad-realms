package nomadrealms.event.game.cardzone;

import nomadrealms.context.game.card.Card;
import nomadrealms.event.Event;

public interface CardZoneEvent<T extends Card> extends Event {

	public void handle(CardZoneListener<T> listener);

}
