package event.game;

import static java.lang.System.currentTimeMillis;

import model.card.GameCard;

public class CardResolvedEvent extends CardMovementEvent {

	private static final long serialVersionUID = -5498891422754809199L;

	private GameCard card;

	public CardResolvedEvent(GameCard card) {
		super(currentTimeMillis(), card);
		this.card = card;
	}

	public GameCard card() {
		return card;
	}

}
