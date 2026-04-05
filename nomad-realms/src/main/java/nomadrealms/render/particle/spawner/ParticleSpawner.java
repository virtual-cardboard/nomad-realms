package nomadrealms.render.particle.spawner;

import java.util.List;

import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.Particle;
import nomadrealms.render.particle.ParticleParameters;

public interface ParticleSpawner {

	List<Particle> spawnParticles(RenderingEnvironment re, ParticleParameters params);

	boolean isComplete();

	default ParticleSpawner copy() {
		return this;
	}

}