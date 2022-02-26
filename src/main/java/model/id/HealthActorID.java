package model.id;

import model.actor.HealthActor;
import model.state.GameState;

public class HealthActorID extends ID<HealthActor> {

	public HealthActorID(long id) {
		super(id);
	}

	@Override
	public HealthActor getFrom(GameState state) {
		return (HealthActor) state.actor(id);
	}

}
