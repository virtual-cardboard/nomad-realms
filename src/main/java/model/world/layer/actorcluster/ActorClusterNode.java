package model.world.layer.actorcluster;

import common.math.Vector2i;

public class ActorClusterNode {

	private Vector2i pos;

	public ActorClusterNode(int x, int y) {
		this(new Vector2i(x, y));
	}

	public ActorClusterNode(Vector2i pos) {
		this.pos = pos;
	}

	public Vector2i tilePos() {
		return pos;
	}

}
