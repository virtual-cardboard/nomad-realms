package nomadrealms.render.particle.spawner;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static nomadrealms.render.particle.spawner.ParticleFactory.createParticle;

import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.Particle;
import nomadrealms.render.particle.ParticleParameters;

public class BasicParticleSpawner implements ParticleSpawner {

	private static final ParticlePropertyFunction<Constraint> DEFAULT_ROTATION_OFFSET = (i, s, t) -> absolute(0);
	private static final ParticlePropertyFunction<ConstraintPair> DEFAULT_POSITION_OFFSET =
			(i, s, t) -> new ConstraintPair(absolute(0), absolute(0));
	private static final ParticlePropertyFunction<ConstraintPair> DEFAULT_SIZE_OFFSET =
			(i, s, t) -> new ConstraintPair(absolute(100), absolute(100));

	private Query<? extends Target> query;
	private final String type;

	private int particleCount = 1;

	private ParticlePropertyFunction<Constraint> rotationOffset = DEFAULT_ROTATION_OFFSET;
	private ParticlePropertyFunction<ConstraintPair> positionOffset = DEFAULT_POSITION_OFFSET;
	private ParticlePropertyFunction<ConstraintPair> sizeOffset = DEFAULT_SIZE_OFFSET;
	private ParticlePropertyFunction<Long> lifetime = (i, s, t) -> 1000L;

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

	public BasicParticleSpawner rotation(ParticlePropertyFunction<Constraint> rotationOffset) {
		this.rotationOffset = rotationOffset;
		return this;
	}

	public BasicParticleSpawner rotation(Function<Integer, Constraint> rotationOffset) {
		this.rotationOffset = (i, s, t) -> rotationOffset.apply(i);
		return this;
	}

	public BasicParticleSpawner positionOffset(ParticlePropertyFunction<ConstraintPair> positionOffset) {
		this.positionOffset = positionOffset;
		return this;
	}

	public BasicParticleSpawner positionOffset(Function<Integer, ConstraintPair> positionOffset) {
		this.positionOffset = (i, s, t) -> positionOffset.apply(i);
		return this;
	}

	public BasicParticleSpawner positionOffset(ConstraintPair positionOffset) {
		this.positionOffset = (i, s, t) -> positionOffset;
		return this;
	}

	public BasicParticleSpawner sizeOffset(ParticlePropertyFunction<ConstraintPair> sizeOffset) {
		this.sizeOffset = sizeOffset;
		return this;
	}

	public BasicParticleSpawner sizeOffset(Function<Integer, ConstraintPair> sizeOffset) {
		this.sizeOffset = (i, s, t) -> sizeOffset.apply(i);
		return this;
	}

	public BasicParticleSpawner sizeOffset(ConstraintPair sizeOffset) {
		this.sizeOffset = (i, s, t) -> sizeOffset;
		return this;
	}

	public BasicParticleSpawner lifetime(ParticlePropertyFunction<Long> lifetime) {
		this.lifetime = lifetime;
		return this;
	}

	public BasicParticleSpawner lifetime(Function<Integer, Long> lifetime) {
		this.lifetime = (i, s, t) -> lifetime.apply(i);
		return this;
	}

	public BasicParticleSpawner lifetime(long lifetime) {
		this.lifetime = (i, s, t) -> lifetime;
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
		clone.rotationOffset = this.rotationOffset;
		clone.positionOffset = this.positionOffset;
		clone.sizeOffset = this.sizeOffset;
		clone.lifetime = this.lifetime;
		clone.delay = this.delay;
		clone.lastSpawnTime = 0;
		clone.spawnedCount = 0;
		return clone;
	}

	@Override
	public List<Particle> spawnParticles(ParticleParameters p) {
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

		RenderingEnvironment re = p.renderingEnvironment();
		List<? extends Target> results = query.find(p.world(), p.source(), p.target());

		List<Particle> particles = new ArrayList<>();
		for (int k = 0; k < countToSpawn; k++) {
			if (spawnedCount >= particleCount) {
				break;
			}
			int i = spawnedCount;
			for (Target result : results) {
				Particle particle = createParticle(type, p);
				particle.rotation(rotationOffset.apply(i, p.source(), result));
				particle.box(new ConstraintBox(
						result.tile().getScreenPosition(re)
								.add(positionOffset.apply(i, p.source(), result).scale(re.camera.zoom())),
						sizeOffset.apply(i, p.source(), result).scale(re.camera.zoom())));
				particle.lifetime(lifetime.apply(i, p.source(), result));
				particles.add(particle);
			}
			spawnedCount++;
			lastSpawnTime = now;
		}
		return particles;
	}

}
