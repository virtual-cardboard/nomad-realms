package model.id;

import model.actor.resource.ResourceActor;
import model.state.GameState;

public class ResourceActorId extends ActorId {

	public ResourceActorId(long id) {
		super(id);
	}

	@Override
	public ResourceActor getFrom(GameState state) {
		return (ResourceActor) state.actor(id);
	}

}
