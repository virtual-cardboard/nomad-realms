package model.particle;

import common.math.Vector2f;
import context.visuals.lwjgl.Texture;

public class Particle {

	public Vector2f pos = new Vector2f();
	public Vector2f dim = new Vector2f();

	public Vector2f vel = new Vector2f();

	public float rot;// Rotation, not to be confused with decay
	public float rotVel;

	public Texture tex;

	public int age;
	public int lifetime;
	public int fadeStart;

	public float opacityMultiplier = 1;
	public float opacity = 1;

	public void update() {
		pos.add(vel);
		rot += rotVel;
		opacity = 1;
		if (age > fadeStart) {
			opacity = (1 - 1f * (age - fadeStart) / (lifetime - fadeStart)) * opacityMultiplier;
		}
		age++;
	}

	public boolean isDead() {
		return age == lifetime;
	}

}
