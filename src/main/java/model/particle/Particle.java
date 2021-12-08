package model.particle;

import common.math.Vector2f;
import context.visuals.lwjgl.Texture;

public class Particle {

	public Vector2f pos;
	public Vector2f dim;

	public Vector2f vel;
	public Vector2f acc = new Vector2f();

	public float rot;// Rotation, not to be confused with decay
	public float rotVel;

	public Texture tex;

	public int age;
	public int lifetime;
	public int fadeStart;

	public float opacity = 1;
	public int diffuse = -1;

	public void update() {
		vel = vel.add(acc);
		pos = pos.add(vel);
		rot += rotVel;
		opacity = 1;
		if (age > fadeStart) {
			opacity = 1 - 1f * (age - fadeStart) / (lifetime - fadeStart);
		}
		age++;
	}

	public boolean isDead() {
		return age == lifetime;
	}

}
