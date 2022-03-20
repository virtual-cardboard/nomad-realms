package model.id;

import model.actor.EventEmitterActor;
import model.state.GameState;

public class EventEmitterActorID extends HealthActorID {

	public EventEmitterActorID(long id) {
		super(id);
	}

	@Override
	public EventEmitterActor getFrom(GameState state) {
		return (EventEmitterActor) state.actor(id);
	}

}
