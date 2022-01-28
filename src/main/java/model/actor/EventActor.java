package model.actor;

public abstract class EventActor extends HealthActor {

	// TODO store events?

	public EventActor(int maxHealth) {
		super(maxHealth);
	}

	public EventActor(long id, int maxHealth) {
		super(id, maxHealth);
	}

}
