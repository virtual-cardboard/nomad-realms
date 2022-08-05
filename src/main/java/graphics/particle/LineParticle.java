package graphics.particle;

import context.game.visuals.renderer.ParticleRenderer;

/**
 * A particle shaped like a rounded line.
 */
public class LineParticle extends Particle {

	public float length;
	public float width;

	public LineParticle() {
	}

	@Override
	public void render(ParticleRenderer particleRenderer) {
		particleRenderer.render(this);
	}

}
