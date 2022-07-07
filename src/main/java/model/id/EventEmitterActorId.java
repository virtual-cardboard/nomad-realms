package model.id;

import model.actor.EventEmitterActor;
import model.state.GameState;

public class EventEmitterActorId extends HealthActorId {

	public EventEmitterActorId(long id) {
		super(id);
	}

	@Override
	public EventEmitterActor getFrom(GameState state) {
		return (EventEmitterActor) state.actor(id);
	}

}
