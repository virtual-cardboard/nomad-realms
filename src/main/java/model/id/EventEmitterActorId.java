package model.id;

import model.actor.health.EventEmitter;
import model.state.GameState;

public class EventEmitterActorId extends HealthActorId {

	public EventEmitterActorId(long id) {
		super(id);
	}

	@Override
	public EventEmitter getFrom(GameState state) {
		return (EventEmitter) state.actor(id);
	}

}
