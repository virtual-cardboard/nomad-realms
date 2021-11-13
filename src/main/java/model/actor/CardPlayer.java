package model.actor;

import java.util.Map;

public abstract class CardPlayer extends HealthActor {

	public CardPlayer(int maxHealth) {
		super(maxHealth);
	}

	@Override
	public void addTo(Map<Long, Actor> actors, Map<Long, CardPlayer> cardPlayers) {
		super.addTo(actors, cardPlayers);
		cardPlayers.put(id, this);
	}

}
