package model.actor;

import java.util.Map;

import context.game.visuals.displayer.CardPlayerDisplayer;
import model.card.GameCard;

public abstract class CardPlayer extends HealthActor {

	public CardPlayer(int maxHealth) {
		super(maxHealth);
	}

	@Override
	public abstract CardPlayer copy();

	public abstract <T extends CardPlayer> CardPlayerDisplayer<T> displayer();

	@Override
	public void addTo(Map<Long, Actor> actors, Map<Long, CardPlayer> cardPlayers, Map<Long, GameCard> cards) {
		super.addTo(actors, cardPlayers, cards);
		cardPlayers.put(id, this);
	}

}
