package model.id;

import model.GameObject;
import model.state.GameState;

/**
 * A wrapper around a <code>long</code> id. Used to get actors of the same id
 * from different <code>GameStates</code>.
 *
 * @author Jay
 */
public abstract class Id {

	protected long id;

	public Id(long id) {
		this.id = id;
	}

	public abstract GameObject getFrom(GameState state);

	public final long toLongID() {
		return id;
	}

	@Override
	public final int hashCode() {
		return Long.hashCode(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Id)) {
			return false;
		}
		Id object = (Id) obj;
		return id == object.id;
	}

	public final <T extends Id> T as(Class<T> idClass) {
		return idClass.cast(this);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + id;
	}

}
