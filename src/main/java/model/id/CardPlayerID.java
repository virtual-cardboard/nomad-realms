package model.id;

import model.actor.CardPlayer;
import model.state.GameState;

public class CardPlayerID extends EventEmitterActorID {

	public CardPlayerID(long id) {
		super(id);
	}

	@Override
	public CardPlayer getFrom(GameState state) {
		return state.cardPlayer(id);
	}

}
