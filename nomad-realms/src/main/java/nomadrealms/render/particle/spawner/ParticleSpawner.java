package nomadrealms.render.particle.spawner;

import java.util.List;

import nomadrealms.event.game.effect.EffectContext;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.Particle;

public interface ParticleSpawner {

	List<Particle> spawnParticles(RenderingEnvironment re, EffectContext params);

	boolean isComplete();

	default ParticleSpawner copy() {
		return this;
	}

}