package model.actor;

import java.util.Map;

import model.card.GameCard;

public abstract class CardPlayer extends HealthActor {

	public CardPlayer(int maxHealth) {
		super(maxHealth);
	}

	@Override
	public abstract CardPlayer copy();

	@Override
	public void addTo(Map<Long, Actor> actors, Map<Long, CardPlayer> cardPlayers, Map<Long, GameCard> cards) {
		super.addTo(actors, cardPlayers, cards);
		cardPlayers.put(id, this);
	}

}
