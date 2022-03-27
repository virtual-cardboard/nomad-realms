package model.world.chunk.lyr3generateActors;

import common.math.Vector2f;

public class PointOfInterest {

	private Vector2f pos;
	private float radius;

	public PointOfInterest(double x, double y, double radius) {
		this(new Vector2f((float) x, (float) y), (float) radius);
	}

	public PointOfInterest(Vector2f pos, float radius) {
		this.pos = pos;
		this.radius = radius;
	}

	public Vector2f pos() {
		return pos;
	}

	public float radius() {
		return radius;
	}

}
