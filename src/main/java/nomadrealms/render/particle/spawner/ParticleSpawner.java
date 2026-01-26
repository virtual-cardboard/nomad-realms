package nomadrealms.render.particle.spawner;

import java.util.List;

import nomadrealms.render.particle.Particle;
import nomadrealms.render.particle.ParticleParameters;

public interface ParticleSpawner {

	List<Particle> spawnParticles(ParticleParameters params);

}