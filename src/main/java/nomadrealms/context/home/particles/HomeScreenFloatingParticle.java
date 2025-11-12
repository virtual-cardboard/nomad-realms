package nomadrealms.context.home.particles;

import static engine.common.colour.Colour.rgb;
import static engine.visuals.constraint.misc.NoiseConstraint.noise;
import static engine.visuals.constraint.misc.TimedConstraint.time;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static java.lang.Math.random;

import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.GLContext;
import nomadrealms.render.particle.HexagonParticle;

public class HomeScreenFloatingParticle extends HexagonParticle {

	public HomeScreenFloatingParticle(GLContext glContext) {
		this(glContext, 5000 + (long) (random() * 5000), 20 + (float) (random() * 10));
	}

	private HomeScreenFloatingParticle(GLContext glContext, long lifetime, float size) {
		super(
				glContext,
				lifetime,
				generateBox(glContext, lifetime, size),
				generateRotation(),
				color()
		);
	}

	private static ConstraintBox generateBox(GLContext glContext, long lifetime, float size) {
		float speed = 40 + (float) (random() * 5);
		float startX = (float) (random() * glContext.screen.w().get());
		return new ConstraintBox(
				absolute(startX).add(noise(time().multiply(0.001)).multiply(50)),
				glContext.screen.h().add(time().multiply(-speed * 0.001)).add(absolute(size)),
				absolute(size).add(time().multiply(size).multiply(1.0 / lifetime).neg()),
				absolute(size).add(time().multiply(size).multiply(1.0 / lifetime).neg()));
	}

	private static Constraint generateRotation() {
		float totalRotations = 1 + (float) (random() * 3);
		return time().multiply(0.0002 * totalRotations);
	}

	private static int color() {
		return rgb(100 + (int) (random() * 155), 100 + (int) (random() * 155), 100 + (int) (random() * 155));
	}

}