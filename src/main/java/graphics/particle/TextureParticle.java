package graphics.particle;

import context.game.visuals.renderer.ParticleRenderer;
import context.visuals.lwjgl.Texture;
import engine.common.math.Vector2f;

public class TextureParticle extends Particle {

	public Texture tex;
	public Vector2f dim;

	@Override
	public void render(ParticleRenderer particleRenderer) {
		particleRenderer.render(this);
	}

}
