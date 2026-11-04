package nomadrealms.context.game.card.effect;

import java.util.List;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.world.World;
import nomadrealms.event.game.effect.EffectContext;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.Particle;
import nomadrealms.render.particle.spawner.ParticleSpawner;
import engine.serialization.Derializable;

/**
 * An effect that spawns particles in the world using a specified particle spawner and parameters.
 * <p>
 * The resolve method adds this effect to the world's particle pool, which will handle the actual spawning.
 *
 * @author Lunkle
 */
@Derializable
public class SpawnParticlesEffect extends Effect {

	/**
	 * The spawner holds runtime lambda callbacks; a restored effect settles for spawning nothing rather than
	 * breaking the load, so both fields are transient.
	 */
	private transient final ParticleSpawner spawner;
	private transient final EffectContext params;

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
	public List<Particle> spawnParticles(RenderingEnvironment re) {
		return spawner.spawnParticles(re, params);
	}

	/**
	 * No-arg constructor for serialization.
	 */
	SpawnParticlesEffect() {
		this.spawner = null;
		this.params = null;
	}
}

