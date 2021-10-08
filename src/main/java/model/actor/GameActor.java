package model.actor;

import static math.IDGenerator.genID;

public abstract class GameActor {

	private long id;

	public GameActor() {
		id = genID();
	}

	public long id() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
