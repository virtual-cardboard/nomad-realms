package event.game.logicprocessing;

import event.network.CardPlayedNetworkEvent;
import model.GameObject;
import model.GameState;
import model.actor.CardPlayer;
import model.card.GameCard;
import model.chain.EffectChain;

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

	public void process(GameState state, EffectChain events) {
		card.effect().expression.handle(player, target, state, events);
	}

	public CardPlayedNetworkEvent toNetworkEvent() {
		return new CardPlayedNetworkEvent(time(), player.id(), target != null ? target.id() : 0, card.id());
	}

}
