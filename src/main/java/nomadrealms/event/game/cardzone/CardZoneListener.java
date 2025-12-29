package nomadrealms.event.game.cardzone;

import nomadrealms.context.game.card.Card;
import nomadrealms.event.EventListener;
import nomadrealms.event.game.cardzone.event.SurfaceCardEvent;

public interface CardZoneListener<T extends Card> extends EventListener<CardZoneEvent<T>> {

	public void handle(SurfaceCardEvent<T> event);

}
