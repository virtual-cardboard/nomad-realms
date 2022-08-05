package graphics.particle;

import context.game.visuals.renderer.ParticleRenderer;
import graphics.particle.function.ColourFunction;
import graphics.particle.function.ParticleTransformation;
import graphics.particle.function.RotationFunction;

/**
 * A visual effect, usually used in large quantities
 */
public abstract class Particle {

	public ParticleTransformation xFunc;
	public ParticleTransformation yFunc;
	public ColourFunction colourFunc;
	public RotationFunction rotFunc;

	/**
	 * The number of frames that the particle has been alive for
	 */
	public int age;
	/**
	 * The lifespan of the particle, in frames. Doesn't include the delay time.
	 */
	public int lifetime = 60; // 60 frames
	/**
	 * The number of frames before the particle is shown
	 */
	public int delay = 0;

	public void update() {
		if (delay > 0) {
			delay--;
			return;
		}
		age++;
	}

	public final boolean isDead() {
		return age == lifetime;
	}

	public abstract void render(ParticleRenderer particleRenderer);

	public final void cleanup() {
		xFunc = null;
		yFunc = null;
		colourFunc = null;
		rotFunc = null;
	}

}
