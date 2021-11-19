package event.game.logicprocessing;

import java.util.Queue;

import event.game.logicprocessing.chain.ChainEvent;
import event.network.CardPlayedNetworkEvent;
import model.GameObject;
import model.GameState;
import model.actor.CardPlayer;
import model.card.GameCard;

public class CardPlayedEvent extends NomadRealmsLogicProcessingEvent {

	private CardPlayer player;
	private GameCard card;
	private GameObject target;

	public CardPlayedEvent(CardPlayer player, GameCard card, GameObject target) {
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

	public GameObject target() {
		return target;
	}

	public void process(GameState state, Queue<ChainEvent> events) {
		card.effect().expression.process(player, target, state, events);
	}

	public CardPlayedNetworkEvent toNetworkEvent() {
		return new CardPlayedNetworkEvent(time(), player.id(), card.id(), target != null ? target.id() : 0);
	}

}
