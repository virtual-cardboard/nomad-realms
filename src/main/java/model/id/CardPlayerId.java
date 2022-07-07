package model.id;

import model.actor.CardPlayer;
import model.state.GameState;

public class CardPlayerId extends EventEmitterActorId {

	public CardPlayerId(long id) {
		super(id);
	}

	@Override
	public CardPlayer getFrom(GameState state) {
		return state.cardPlayer(id);
	}

}
