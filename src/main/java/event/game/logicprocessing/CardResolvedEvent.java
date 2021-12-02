package event.game.logicprocessing;

import model.actor.Actor;
import model.actor.CardPlayer;
import model.card.GameCard;

public class CardResolvedEvent extends NomadRealmsLogicProcessingEvent {

	private CardPlayer player;
	private GameCard card;
	private Actor target;

	public CardResolvedEvent(CardPlayer player, GameCard card, Actor target) {
		super(player);
		this.player = player;
		this.card = card;
		this.target = target;
	}

	public CardPlayer player() {
		return player;
	}

	public GameCard card() {
		return card;
	}

	public Actor target() {
		return target;
	}

}
