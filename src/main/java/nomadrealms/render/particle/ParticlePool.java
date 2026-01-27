package nomadrealms.render.particle;

import static java.lang.System.currentTimeMillis;

import java.util.ArrayList;
import java.util.List;

import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.GLContext;
import nomadrealms.context.game.card.effect.SpawnParticlesEffect;
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
	private final GLContext glContext;

	private Particle[] particles = new Particle[MAX_PARTICLES];
	private long[] particleStartTimes = new long[MAX_PARTICLES];

	private List<SpawnParticlesEffect> activeEffects = new ArrayList<>();

	/**
	 * Creates a new ParticlePool with the specified bounds.
	 *
	 * @param glContext The OpenGL context.
	 * @param bounds    The constraint box defining the bounds for the particles. Usually the screen size.
	 */
	public ParticlePool(GLContext glContext, ConstraintBox bounds) {
		this.glContext = glContext;
		this.bounds = bounds;
	}

	/**
	 * Creates a new ParticlePool with no bounds.
	 *
	 * @param glContext The OpenGL context.
	 */
	public ParticlePool(GLContext glContext) {
		this(glContext, null);
	}

	public GLContext glContext() {
		return glContext;
	}

	@Override
	public void render(RenderingEnvironment re) {
		if (activeEffects != null) {
			for (int i = 0; i < activeEffects.size(); i++) {
				SpawnParticlesEffect effect = activeEffects.get(i);
				effect.params().re = re;
				for (Particle particle : effect.spawner().spawnParticles(effect.params())) {
					addParticle(particle);
				}
				if (effect.spawner().isComplete()) {
					activeEffects.remove(i);
					i--;
				}
			}
		}

		long currentTime = currentTimeMillis();
		for (int i = 0; i < particles.length; i++) {
			Particle particle = particles[i];
			if (particle == null || particle.lifetime() <= currentTime - particleStartTimes[i]) {
				particles[i] = null;
				continue;
			}
			if (bounds != null && !bounds.overlaps(particle.bigBoundingBox())) {
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

	public void addParticles(SpawnParticlesEffect effect) {
		activeEffects.add(effect);
	}

}
