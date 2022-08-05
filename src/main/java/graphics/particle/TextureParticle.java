package graphics.particle;

import context.game.visuals.renderer.ParticleRenderer;
import context.visuals.lwjgl.Texture;
import engine.common.math.Vector2f;

/**
 * A particle with a texture, as opposed to a line.
 */
public class TextureParticle extends Particle {

	public Texture tex;
	public Vector2f dim = new Vector2f(1, 1);

	@Override
	public void render(ParticleRenderer particleRenderer) {
		particleRenderer.render(this);
	}

}
