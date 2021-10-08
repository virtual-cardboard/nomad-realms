package model.actor;

import common.math.Vector2f;

public abstract class PositionalActor extends GameActor {

	private Vector2f pos;

	public PositionalActor() {
		pos = new Vector2f();
	}

	public Vector2f pos() {
		return pos;
	}

	public void setPos(Vector2f position) {
		this.pos = position;
	}

}
