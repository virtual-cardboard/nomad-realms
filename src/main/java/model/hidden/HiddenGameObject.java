package model.hidden;

import model.GameObject;
import model.state.GameState;

public abstract class HiddenGameObject extends GameObject {

	@Override
	public void addTo(GameState state) {
		state.hiddens().put(id, this);
	}

	@Override
	public void removeFrom(GameState state) {
		state.hiddens().remove(id);
	}

}
