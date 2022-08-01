package graphics.particle;

import context.game.visuals.renderer.ParticleRenderer;
import graphics.particle.function.ColourFunction;
import graphics.particle.function.ParticleTransformation;
import graphics.particle.function.RotationFunction;

public abstract class Particle {

	public ParticleTransformation xFunc;
	public ParticleTransformation yFunc;
	public ColourFunction colourFunc;
	public RotationFunction rotFunc;

	public int age;
	public int lifetime;
	public int delay;

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
