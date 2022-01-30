package model.actor;

public abstract class EventEmitterActor extends HealthActor {

	// TODO store events?

	public EventEmitterActor(int maxHealth) {
		super(maxHealth);
	}

	public EventEmitterActor(long id, int maxHealth) {
		super(id, maxHealth);
	}

}
