package model.actor.health;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;

public abstract class EventEmitter extends HealthActor {

	// TODO store events?

	public EventEmitter() {
	}

	public EventEmitter(int maxHealth) {
		super(maxHealth);
	}

	public EventEmitter(long id, int maxHealth) {
		super(id, maxHealth);
	}

	@Override
	public void read(SerializationReader reader) {
		super.read(reader);
	}

	@Override
	public void write(SerializationWriter writer) {
		super.write(writer);
	}

}
