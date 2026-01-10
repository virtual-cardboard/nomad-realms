package nomadrealms.render.particle.spawner;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import engine.visuals.constraint.box.ConstraintPair;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.render.particle.Particle;

public class BasicParticleSpawner implements ParticleSpawner {

	private Query<Actor> source;

	private int particleCount = 1;
	private Function<Integer, Float> rotation = i -> 0f;
	private Function<Integer, ConstraintPair> positionOffset = i -> new ConstraintPair(absolute(0), absolute(0));

	public BasicParticleSpawner(Query<Actor> source) {
		this.source = source;
	}

	public BasicParticleSpawner particleCount(int particleCount) {
		this.particleCount = particleCount;
		return this;
	}

	public BasicParticleSpawner rotation(Function<Integer, Float> rotation) {
		this.rotation = rotation;
		return this;
	}

	public BasicParticleSpawner positionOffset(Function<Integer, ConstraintPair> positionOffset) {
		this.positionOffset = positionOffset;
		return this;
	}

	@Override
	public List<Particle> spawnParticles() {
		List<Particle> particles = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
//			particles.add(new Particle());
		}
		return particles;
	}

}
