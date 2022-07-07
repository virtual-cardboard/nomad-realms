package model.id;

import model.actor.Actor;
import model.state.GameState;

public class ActorId extends Id {

	public ActorId(long id) {
		super(id);
	}

	@Override
	public Actor getFrom(GameState state) {
		return state.actor(id);
	}

}
