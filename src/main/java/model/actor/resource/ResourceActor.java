package model.actor.resource;

import model.actor.Actor;
import model.id.ResourceActorId;

public abstract class ResourceActor extends Actor {

	public ResourceActor() {
	}

	public ResourceActor(long id) {
		super(id);
	}

	@Override
	public abstract ResourceActorId id();

}
