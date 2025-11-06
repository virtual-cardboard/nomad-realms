package nomadrealms.particles;

import nomadrealms.render.RenderingEnvironment;

public abstract class BaseParticle implements Particle {

	protected final long spawnTime;
	protected final long lifetime;
	protected float x;
	protected float y;
	protected float vx;
	protected float vy;
	protected float ax;
	protected float ay;

	public BaseParticle(long lifetime, float x, float y, float vx, float vy, float ax, float ay) {
		this.spawnTime = System.currentTimeMillis();
		this.lifetime = lifetime;
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.ax = ax;
		this.ay = ay;
	}

	@Override
	public long lifetime() {
		return lifetime;
	}

	@Override
	public void onDeath() {
		// No default behavior
	}

}
