package nomadrealms.render.particle;

import nomadrealms.render.RenderingEnvironment;

public class NullParticlePool extends ParticlePool {

	public NullParticlePool() {
		super(null);
	}

	@Override
	public void render(RenderingEnvironment re) {
		// Do nothing
	}

	@Override
	public void addParticle(Particle particle) {
		System.out.println("Warning: Particle added to NullParticlePool");
	}
}
