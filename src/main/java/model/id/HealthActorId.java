package model.id;

import model.actor.health.HealthActor;
import model.state.GameState;

public class HealthActorId extends ActorId {

	public HealthActorId(long id) {
		super(id);
	}

	@Override
	public HealthActor getFrom(GameState state) {
		return (HealthActor) state.actor(id);
	}

}
