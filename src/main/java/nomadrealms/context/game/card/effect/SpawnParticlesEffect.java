package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.world.World;
import nomadrealms.render.particle.ParticleParameters;
import nomadrealms.render.particle.spawner.ParticleSpawner;

public class SpawnParticlesEffect extends Effect {

	private final ParticleSpawner spawner;
	private final ParticleParameters params;

	public SpawnParticlesEffect(ParticleSpawner spawner, ParticleParameters params) {
		this.spawner = spawner;
		this.params = params;
	}

	@Override
	public void resolve(World world) {
		world.state().particlePool.addParticles(this);
	}

	public ParticleSpawner spawner() {
		return spawner;
	}

	public ParticleParameters params() {
		return params;
	}
	
}
