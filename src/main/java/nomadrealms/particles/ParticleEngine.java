package nomadrealms.particles;

import java.util.ArrayList;
import java.util.List;

import nomadrealms.render.Renderable;
import nomadrealms.render.RenderingEnvironment;

public class ParticleEngine implements Renderable {

	private final List<Particle> worldParticles = new ArrayList<>();
	private final List<Particle> screenParticles = new ArrayList<>();

	public void render(RenderingEnvironment re, long timestamp) {
		renderParticleList(worldParticles, re, timestamp);
		renderParticleList(screenParticles, re, timestamp);
	}

	private void renderParticleList(List<Particle> particles, RenderingEnvironment re, long timestamp) {
		for (int i = particles.size() - 1; i >= 0; i--) {
			Particle particle = particles.get(i);
			if (timestamp - ((BaseParticle) particle).spawnTime > particle.lifetime()) {
				particle.onDeath();
				particles.remove(i);
			} else {
				particle.render(re, timestamp);
			}
		}
	}

	@Override
	public void render(RenderingEnvironment re) {
		render(re, System.currentTimeMillis());
	}

	public void spawnWorldParticle(Particle particle) {
		worldParticles.add(particle);
	}

	public void spawnScreenParticle(Particle particle) {
		screenParticles.add(particle);
	}

}
