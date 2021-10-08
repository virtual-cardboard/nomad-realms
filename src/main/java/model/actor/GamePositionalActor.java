package model.actor;

import common.math.Vector2f;

public abstract class GamePositionalActor extends GameActor {

	private Vector2f pos;

	public GamePositionalActor() {
		pos = new Vector2f();
	}

	public Vector2f pos() {
		return pos;
	}

	public void setPos(Vector2f position) {
		this.pos = position;
	}

}
