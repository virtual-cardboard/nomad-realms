package nomadrealms.particles;

import nomadrealms.render.RenderingEnvironment;

public abstract class BaseParticle implements Particle {

	protected float lifetime;
	protected float age;
	protected float x;
	protected float y;
	protected float vx;
	protected float vy;
	protected float ax;
	protected float ay;

	public BaseParticle(float lifetime, float x, float y, float vx, float vy, float ax, float ay) {
		this.lifetime = lifetime;
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.ax = ax;
		this.ay = ay;
	}

	@Override
	public void update(float deltaTime) {
		age += deltaTime;
		vx += ax * deltaTime;
		vy += ay * deltaTime;
		x += vx * deltaTime;
		y += vy * deltaTime;
	}

	@Override
	public boolean isAlive() {
		return age < lifetime;
	}

	@Override
	public void onDeath() {
		// No default behavior
	}

}
