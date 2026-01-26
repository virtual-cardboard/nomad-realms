package nomadrealms.render.particle.spawner;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static nomadrealms.render.particle.spawner.ParticleFactory.createParticle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.Particle;
import nomadrealms.render.particle.ParticleParameters;

public class BasicParticleSpawner implements ParticleSpawner {

	private static final Function<Integer, Constraint> DEFAULT_ROTATION_OFFSET = i -> absolute(0);
	private static final Function<Integer, ConstraintPair> DEFAULT_POSITION_OFFSET =
			i -> new ConstraintPair(absolute(0), absolute(0));
	private static final Function<Integer, ConstraintPair> DEFAULT_SIZE_OFFSET =
			i -> new ConstraintPair(absolute(100), absolute(100));

	private Query<? extends Target> query;
	private final String type;

	private int particleCount = 1;

	private Function<Integer, Constraint> rotationOffset = DEFAULT_ROTATION_OFFSET;
	private Function<Integer, ConstraintPair> positionOffset = DEFAULT_POSITION_OFFSET;
	private Function<Integer, ConstraintPair> sizeOffset = DEFAULT_SIZE_OFFSET;

	public BasicParticleSpawner(Query<? extends Target> query, String type) {
		this.query = query;
		this.type = type;
	}

	public BasicParticleSpawner particleCount(int particleCount) {
		this.particleCount = particleCount;
		return this;
	}

	public BasicParticleSpawner rotation(Function<Integer, Constraint> rotationOffset) {
		this.rotationOffset = rotationOffset;
		return this;
	}

	public BasicParticleSpawner positionOffset(Function<Integer, ConstraintPair> positionOffset) {
		this.positionOffset = positionOffset;
		return this;
	}

	public BasicParticleSpawner sizeOffset(Function<Integer, ConstraintPair> sizeOffset) {
		this.sizeOffset = sizeOffset;
		return this;
	}

	@Override
	public List<Particle> spawnParticles(ParticleParameters p) {
		RenderingEnvironment re = p.renderingEnvironment();
		List<? extends Target> results = query.find(p.world(), p.source(), p.target());

		List<Particle> particles = new ArrayList<>();
		for (Target result : results) {
			for (int i = 0; i < particleCount; i++) {
				Particle particle = createParticle(type, p);
				particle.rotation(rotationOffset.apply(i));
				particle.box(new ConstraintBox(
						result.tile().getScreenPosition(re)
								.add(re.camera.position().neg())
								.add(positionOffset.apply(i).scale(re.camera.zoom())),
						sizeOffset.apply(i).scale(re.camera.zoom())));
				particles.add(particle);
			}
		}
		return particles;
	}

}
