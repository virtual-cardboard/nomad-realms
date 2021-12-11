package model.particle;

import common.math.Vector2f;
import context.GLContext;
import context.game.visuals.renderer.ParticleRenderer;

public abstract class Particle {

	public Vector2f pos;
	public Vector2f vel;
	public Vector2f acc = new Vector2f();

	public float rot;// Rotation, not to be confused with decay
	public float rotVel;

	public int age;
	public int lifetime;
	public int fadeStart;

	public float opacity = 1;
	public int diffuse = -1;
	
	public int spawnDelay;

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

	public final boolean isDead() {
		return age == lifetime;
	}

	public abstract void render(GLContext glContext, Vector2f screenDim, ParticleRenderer particleRenderer);

}
