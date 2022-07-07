package model.actor;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;

public abstract class EventEmitterActor extends HealthActor {

	// TODO store events?

	public EventEmitterActor() {
	}

	public EventEmitterActor(int maxHealth) {
		super(maxHealth);
	}

	public EventEmitterActor(long id, int maxHealth) {
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
