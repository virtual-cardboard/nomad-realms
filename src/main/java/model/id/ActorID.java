package model.id;

import model.actor.Actor;
import model.state.GameState;

public class ActorID extends ID<Actor> {

	public ActorID(long id) {
		super(id);
	}

	@Override
	public Actor getFrom(GameState state) {
		return state.actor(id);
	}

}
