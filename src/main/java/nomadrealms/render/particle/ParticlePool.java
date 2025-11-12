package nomadrealms.render.particle;

import static java.lang.System.currentTimeMillis;

import engine.visuals.constraint.box.ConstraintBox;
import nomadrealms.render.Renderable;
import nomadrealms.render.RenderingEnvironment;

/**
 * A pool for managing particles. This class is responsible for creating, updating, and recycling particles to
 * optimize  performance.
 *
 * @author Lunkle
 */
public class ParticlePool implements Renderable {

	public static final int MAX_PARTICLES = 10000;

	private final ConstraintBox bounds;

	private Particle[] particles = new Particle[MAX_PARTICLES];
	private long[] particleStartTimes = new long[MAX_PARTICLES];

	/**
	 * Creates a new ParticlePool with the specified bounds.
	 *
	 * @param bounds The constraint box defining the bounds for the particles. Usually the screen size.
	 */
	public ParticlePool(ConstraintBox bounds) {
		this.bounds = bounds;
	}

	@Override
	public void render(RenderingEnvironment re) {
		long currentTime = currentTimeMillis();
		for (int i = 0; i < particles.length; i++) {
			Particle particle = particles[i];
			if (particle == null || particle.lifetime() <= currentTime - particleStartTimes[i]) {
				particles[i] = null;
				continue;
			}
			if (!bounds.overlaps(particle.bigBoundingBox())) {
				continue;
			}
			particle.render(re);
		}
	}

	public void addParticle(Particle particle) {
		for (int i = 0; i < particles.length; i++) {
			if (particles[i] == null || particles[i].lifetime() <= 0) {
				particles[i] = particle;
				particleStartTimes[i] = currentTimeMillis();
				return;
			}
		}
	}

}

