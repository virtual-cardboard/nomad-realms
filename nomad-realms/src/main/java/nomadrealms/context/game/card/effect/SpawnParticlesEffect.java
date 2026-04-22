package nomadrealms.context.game.card.effect;

import java.util.List;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.interaction.InteractionState;
import nomadrealms.context.game.world.World;
import nomadrealms.event.game.effect.EffectContext;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.Particle;
import nomadrealms.render.particle.spawner.ParticleSpawner;

/**
 * An effect that spawns particles in the world using a specified particle spawner and parameters.
 * <p>
 * The resolve method adds this effect to the world's particle pool, which will handle the actual spawning.
 *
 * @author Lunkle
 */
public class SpawnParticlesEffect extends Effect {

	private final ParticleSpawner spawner;
	private final EffectContext params;

	public SpawnParticlesEffect(Actor source, ParticleSpawner spawner, EffectContext params) {
		super(source);
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

	public EffectContext params() {
		return params;
	}

	/**
	 * Spawns particles using the spawner and parameters of this effect. If a delay is set in the spawner, this
	 * method may return different particles each time it is called until the spawner is complete.
	 *
	 * @param re the rendering environment.
	 * @return the list of particles.
	 */
	public List<Particle> spawnParticles(RenderingEnvironment re, InteractionState interactionState) {
		return spawner.spawnParticles(re, interactionState, params);
	}

}
