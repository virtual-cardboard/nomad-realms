package model.id;

import model.GameObject;
import model.state.GameState;

public abstract class ID {

	protected long id;

	public ID(long id) {
		this.id = id;
	}

	public abstract GameObject getFrom(GameState state);

	public long toLongID() {
		return id;
	}

	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (getClass() != obj.getClass()) {
			return false;
		}
		if (obj instanceof ID) {
			ID object = (ID) obj;
			return id == object.id;
		}
		return false;
	}

	@Override
	public String toString() {
		return getClass().getName() + " " + id;
	}

}
