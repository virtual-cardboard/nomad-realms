package nomadrealms.render.particle.spawner;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static nomadrealms.render.particle.spawner.ParticleFactory.createParticle;

import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import java.util.ArrayList;
import java.util.List;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.event.game.effect.EffectContext;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.Particle;

public class BasicParticleSpawner implements ParticleSpawner {

	private static final ParticlePropertyFunction<Constraint> DEFAULT_ROTATION = (i, s, t) -> absolute(0);
	private static final ParticlePropertyFunction<ConstraintPair> DEFAULT_POSITION =
			(i, s, t) -> t.tile().pos();
	private static final ParticlePropertyFunction<ConstraintPair> DEFAULT_SIZE =
			(i, s, t) -> new ConstraintPair(absolute(100), absolute(100));

	private Query<? extends Target> query;
	private final String type;

	private int particleCount = 1;

	private ParticlePropertyFunction<Constraint> rotation = DEFAULT_ROTATION;
	private ParticlePropertyFunction<ConstraintPair> position = DEFAULT_POSITION;
	private ParticlePropertyFunction<ConstraintPair> size = DEFAULT_SIZE;
	private ParticlePropertyFunction<Long> lifetime = (i, s, t) -> 1000L;
	private ParticlePropertyFunction<Integer> color = (i, s, t) -> -1;

	private long delay = 0;
	private long lastSpawnTime = 0;
	private int spawnedCount = 0;

	public BasicParticleSpawner(Query<? extends Target> query, String type) {
		this.query = query;
		this.type = type;
	}

	public BasicParticleSpawner particleCount(int particleCount) {
		this.particleCount = particleCount;
		return this;
	}

	public BasicParticleSpawner rotation(ParticlePropertyFunction<Constraint> rotation) {
		this.rotation = rotation;
		return this;
	}

	public BasicParticleSpawner position(ParticlePropertyFunction<ConstraintPair> position) {
		this.position = position;
		return this;
	}

	public BasicParticleSpawner size(ParticlePropertyFunction<ConstraintPair> size) {
		this.size = size;
		return this;
	}

	public BasicParticleSpawner lifetime(ParticlePropertyFunction<Long> lifetime) {
		this.lifetime = lifetime;
		return this;
	}

	public BasicParticleSpawner color(ParticlePropertyFunction<Integer> color) {
		this.color = color;
		return this;
	}

	public BasicParticleSpawner delay(long delay) {
		this.delay = delay;
		return this;
	}

	@Override
	public boolean isComplete() {
		return spawnedCount >= particleCount;
	}

	@Override
	public BasicParticleSpawner copy() {
		BasicParticleSpawner clone = new BasicParticleSpawner(query, type);
		clone.particleCount = this.particleCount;
		clone.rotation = this.rotation;
		clone.position = this.position;
		clone.size = this.size;
		clone.lifetime = this.lifetime;
		clone.color = this.color;
		clone.delay = this.delay;
		clone.lastSpawnTime = 0;
		clone.spawnedCount = 0;
		return clone;
	}

	@Override
	public List<Particle> spawnParticles(RenderingEnvironment re, EffectContext p) {
		if (isComplete()) {
			return new ArrayList<>();
		}
		long now = System.currentTimeMillis();
		int countToSpawn = 0;
		if (delay <= 0) {
			countToSpawn = particleCount - spawnedCount;
		} else if (now - lastSpawnTime >= delay) {
			countToSpawn = 1;
		}

		if (countToSpawn == 0) {
			return new ArrayList<>();
		}

		List<? extends Target> results = query.find(p);

		List<Particle> particles = new ArrayList<>();
		for (int k = 0; k < countToSpawn; k++) {
			if (spawnedCount >= particleCount) {
				break;
			}
			int i = spawnedCount;
			for (Target result : results) {
				Particle particle = createParticle(type, re, p, color.apply(i, p.source(), result));
				particle.rotation(rotation.apply(i, p.source(), result));
				particle.box(new ConstraintBox(
						position.apply(i, p.source(), result)
								.sub(re.is.camera.position()).scale(re.is.camera.zoom()),
						size.apply(i, p.source(), result).scale(re.is.camera.zoom())));
				particle.lifetime(lifetime.apply(i, p.source(), result));
				particles.add(particle);
			}
			spawnedCount++;
			lastSpawnTime = now;
		}
		return particles;
	}

}
