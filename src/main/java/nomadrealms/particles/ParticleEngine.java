package nomadrealms.particles;

import java.util.ArrayList;
import java.util.List;

import nomadrealms.render.Renderable;
import nomadrealms.render.RenderingEnvironment;

public class ParticleEngine implements Renderable {

	private final List<Particle> worldParticles = new ArrayList<>();
	private final List<Particle> screenParticles = new ArrayList<>();

	public void update(float deltaTime) {
		updateParticleList(worldParticles, deltaTime);
		updateParticleList(screenParticles, deltaTime);
	}

	private void updateParticleList(List<Particle> particles, float deltaTime) {
		for (int i = particles.size() - 1; i >= 0; i--) {
			Particle particle = particles.get(i);
			particle.update(deltaTime);
			if (!particle.isAlive()) {
				particle.onDeath();
				particles.remove(i);
			}
		}
	}

	@Override
	public void render(RenderingEnvironment re) {
		for (Particle particle : worldParticles) {
			particle.render(re);
		}
		for (Particle particle : screenParticles) {
			particle.render(re);
		}
	}

	public void spawnWorldParticle(Particle particle) {
		worldParticles.add(particle);
	}

	public void spawnScreenParticle(Particle particle) {
		screenParticles.add(particle);
	}

}
