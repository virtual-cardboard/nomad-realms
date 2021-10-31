package event.game;

import static context.data.user.LocalUser.LOCAL_USER;
import static java.lang.System.currentTimeMillis;

import common.event.GameEvent;
import event.network.CardPlayedNetworkEvent;
import model.actor.Actor;
import model.actor.CardPlayer;
import model.card.GameCard;

public class CardPlayedEvent extends GameEvent {

	private static final long serialVersionUID = 3131040194635457053L;

	private CardPlayer playedBy;
	private GameCard card;
	private Actor target;

	public CardPlayedEvent(CardPlayer playedBy, GameCard card, Actor target) {
		super(currentTimeMillis(), LOCAL_USER);
		this.playedBy = playedBy;
		this.card = card;
		this.target = target;
	}

	public CardPlayer playedBy() {
		return playedBy;
	}

	public GameCard card() {
		return card;
	}

	public Actor target() {
		return target;
	}

	public CardPlayedNetworkEvent toNetworkEvent() {
		return new CardPlayedNetworkEvent(time(), playedBy.id(), card.id(), target != null ? target.id() : 0);
	}

}
