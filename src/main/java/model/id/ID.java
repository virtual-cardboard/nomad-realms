package model.id;

import model.GameObject;
import model.state.GameState;

public abstract class ID<T extends GameObject> {

	protected long id;

	public ID(long id) {
		this.id = id;
	}

	public abstract T getFrom(GameState state);

}
