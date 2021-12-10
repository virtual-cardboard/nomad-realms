package model.actor;

import java.util.List;
import java.util.Map;

import common.math.Vector2i;
import context.game.visuals.displayer.CardPlayerDisplayer;
import model.card.CardDashboard;
import model.card.GameCard;

public abstract class CardPlayer extends HealthActor {

	private CardDashboard cardDashboard = new CardDashboard();

	public CardPlayer(int maxHealth) {
		super(maxHealth);
	}

	@Override
	public abstract CardPlayer copy();

	public abstract CardPlayerDisplayer<?> displayer();

	public CardDashboard cardDashboard() {
		return cardDashboard;
	}

	@Override
	public void addTo(Map<Long, Actor> actors, Map<Long, CardPlayer> cardPlayers, Map<Long, GameCard> cards, Map<Vector2i, List<Actor>> chunkToActors) {
		super.addTo(actors, cardPlayers, cards, chunkToActors);
		cardPlayers.put(id, this);
	}

}
