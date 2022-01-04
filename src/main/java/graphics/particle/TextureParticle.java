package graphics.particle;

import common.math.Vector2f;
import context.game.visuals.renderer.ParticleRenderer;
import context.visuals.lwjgl.Texture;

public class TextureParticle extends Particle {

	public Texture tex;
	public Vector2f dim;

	@Override
	public void render(ParticleRenderer particleRenderer) {
		particleRenderer.render(this);
	}

}
