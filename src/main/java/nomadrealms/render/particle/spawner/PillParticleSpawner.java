package nomadrealms.render.particle.spawner;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static java.lang.Math.random;

import java.util.ArrayList;
import java.util.List;

import engine.common.colour.Colour;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.misc.TimedConstraint;
import nomadrealms.render.particle.Particle;
import nomadrealms.render.particle.PillParticle;

public class PillParticleSpawner implements ParticleSpawner {

	private final float startX;
	private final float startY;

	public PillParticleSpawner(float startX, float startY) {
		this.startX = startX;
		this.startY = startY;
	}

	@Override
	public List<Particle> spawnParticles() {
		List<Particle> particles = new ArrayList<>();
		int particleCount = 50 + (int) (random() * 50);
		for (int i = 0; i < particleCount; i++) {
			particles.add(createParticle());
		}
		return particles;
	}

	private Particle createParticle() {
		long lifetime = 1000 + (long) (random() * 2000);
		float size = 5 + (float) (random() * 10);
		TimedConstraint time = new TimedConstraint().activate();

		float initialVelocityX = (float) (random() * 600 - 300);
		float initialVelocityY = (float) (random() * 600 - 300);
		float gravity = -800f;

		Constraint x = absolute(startX).add(time.multiply(0.001f * initialVelocityX));
		Constraint y = absolute(startY).add(time.multiply(0.001f * initialVelocityY)).add(time.multiply(time).multiply(0.000001f * gravity));
		Constraint w = absolute(size);
		Constraint h = absolute(size * 2);

		ConstraintBox box = new ConstraintBox(x, y, w, h);

		float rotationSpeed = (float) (random() * 10 - 5);
		Constraint rotation = time.multiply(0.001f * rotationSpeed);

		int color = Colour.rgb(100 + (int) (random() * 155), 100 + (int) (random() * 155), 100 + (int) (random() * 155));

		return new PillParticle(lifetime, box, rotation, color);
	}

}
