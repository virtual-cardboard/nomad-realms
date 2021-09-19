package model.actor;

import common.math.Vector2f;

public abstract class GameActor {

	private Vector2f pos;

	public Vector2f pos() {
		return pos;
	}

	public void pos(Vector2f position) {
		this.pos = position;
	}

}
