package model.id;

import model.actor.resource.TreeActor;
import model.state.GameState;

public class TreeId extends ActorId {

	public TreeId(long id) {
		super(id);
	}

	@Override
	public TreeActor getFrom(GameState state) {
		return (TreeActor) state.actor(id);
	}

}
