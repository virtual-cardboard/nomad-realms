package model.world.layer.actorcluster;

import common.math.Vector2f;

public class ActorClusterNode {

	private Vector2f pos;

	public ActorClusterNode(float x, float y) {
		this(new Vector2f(x, y));
	}

	public ActorClusterNode(Vector2f pos) {
		this.pos = pos;
	}

	public Vector2f pos() {
		return pos;
	}

}
