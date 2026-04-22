package nomadrealms.render.particle;
import nomadrealms.context.game.interaction.InteractionState;

import nomadrealms.render.RenderingEnvironment;

public class NullParticlePool extends ParticlePool {

	public NullParticlePool() {
		super(null);
	}

	@Override
	public void render(RenderingEnvironment re, InteractionState interactionState) {
		// Do nothing
	}

	@Override
	public void addParticle(Particle particle) {
		System.out.println("Warning: Particle added to NullParticlePool");
	}
}
