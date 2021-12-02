package event.game.logicprocessing;

import event.network.CardPlayedNetworkEvent;
import model.GameState;
import model.actor.Actor;
import model.actor.CardPlayer;
import model.card.GameCard;
import model.chain.EffectChain;

public class CardPlayedEvent extends NomadRealmsLogicProcessingEvent {

	private CardPlayer player;
	private GameCard card;
	private Actor target;

	public CardPlayedEvent(CardPlayer player, GameCard card, Actor target) {
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

	public void process(GameState state, EffectChain events) {
		card.effect().expression.handle(player, target, state, events);
	}

	public CardPlayedNetworkEvent toNetworkEvent() {
		return new CardPlayedNetworkEvent(time(), player.id(), target != null ? target.id() : 0, card.id());
	}

	public CardPlayedEvent copy(GameState state) {
		return new CardPlayedEvent(state.cardPlayer(player.id()), state.card(card.id()), state.getCorresponding(target));
	}

}
