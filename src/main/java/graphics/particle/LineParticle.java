package graphics.particle;

import common.math.Vector2f;
import context.GLContext;
import context.game.visuals.renderer.ParticleRenderer;

public class LineParticle extends Particle {

	public float length;
	public float width;

	public LineParticle() {
	}

	@Override
	public void render(GLContext glContext, Vector2f screenDim, ParticleRenderer particleRenderer) {
		particleRenderer.render(glContext, screenDim, this);
	}

}
