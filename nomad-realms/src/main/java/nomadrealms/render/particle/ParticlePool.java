package nomadrealms.render.particle;

import static java.lang.System.currentTimeMillis;

import engine.common.misc.DensePool;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.GLContext;
import java.util.ArrayList;
import java.util.List;
import nomadrealms.context.game.card.effect.SpawnParticlesEffect;
import nomadrealms.render.Renderable;
import nomadrealms.render.RenderingEnvironment;

/**
 * A pool for managing particles. This class is responsible for creating, updating, and recycling particles to
 * optimize performance.
 *
 * @author Lunkle
 */
public class ParticlePool implements Renderable {

	public static final int MAX_PARTICLES = 10000;

	private final ConstraintBox bounds;
	private final GLContext glContext;
	private final DensePool<Particle> pool;

	private final List<SpawnParticlesEffect> activeEffects = new ArrayList<>();

	/**
	 * Creates a new ParticlePool with the specified bounds.
	 *
	 * @param glContext The OpenGL context.
	 * @param bounds    The constraint box defining the bounds for the particles. Usually the screen size.
	 */
	public ParticlePool(GLContext glContext, ConstraintBox bounds) {
		this(glContext, bounds, MAX_PARTICLES);
	}

	public ParticlePool(GLContext glContext, ConstraintBox bounds, int maxParticles) {
		this(glContext, bounds, new DensePool<>(maxParticles));
	}

	public ParticlePool(GLContext glContext, ConstraintBox bounds, DensePool<Particle> pool) {
		this.glContext = glContext;
		this.bounds = bounds;
		this.pool = pool;
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

	public DensePool<Particle> pool() {
		return pool;
	}

	@Override
	public void render(RenderingEnvironment re) {
		for (int i = activeEffects.size() - 1; i >= 0; i--) {
			SpawnParticlesEffect effect = activeEffects.get(i);
			for (Particle particle : effect.spawnParticles(re)) {
				addParticle(particle);
			}
			if (effect.spawner().isComplete()) {
				activeEffects.remove(i);
			}
		}

		long currentTime = currentTimeMillis();
		pool.process((particle, startTime) -> {
			if (particle.lifetime() <= currentTime - startTime) {
				return false;
			}
			return true;
		}, particle -> {
			if (bounds == null || bounds.overlaps(particle.bigBoundingBox())) {
				particle.render(re);
			}
		});
	}

	public void addParticle(Particle particle) {
		pool.add(particle);
	}

	public void addParticles(SpawnParticlesEffect effect) {
		activeEffects.add(effect);
	}

}
