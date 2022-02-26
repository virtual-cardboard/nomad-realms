package model.id;

import model.GameObject;
import model.state.GameState;

public class ActorID extends ID<GameObject> {

	public ActorID(long id) {
		super(id);
	}

	@Override
	public GameObject getFrom(GameState state) {
		return state.actor(id);
	}

}
