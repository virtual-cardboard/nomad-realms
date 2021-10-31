package model.actor;

import java.util.Map;

public abstract class CardPlayer extends PositionalActor {

	@Override
	public void addTo(Map<Long, Actor> actors, Map<Long, CardPlayer> cardPlayers) {
		super.addTo(actors, cardPlayers);
		cardPlayers.put(id, this);
	}

}
