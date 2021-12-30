package event.game.logicprocessing;

import model.actor.CardPlayer;
import model.actor.GameObject;
import model.card.GameCard;

public class CardResolvedEvent extends NomadRealmsLogicProcessingEvent {

	private CardPlayer player;
	private GameCard card;
	private GameObject target;

	public CardResolvedEvent(CardPlayer player, GameCard card, GameObject target) {
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

	public GameObject target() {
		return target;
	}

}
