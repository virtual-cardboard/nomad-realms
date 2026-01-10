package nomadrealms.render.particle;

import static engine.visuals.constraint.misc.TimedConstraint.time;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static java.lang.Math.random;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.misc.TimedConstraint;
import engine.visuals.lwjgl.GLContext;

public class PillParticle extends TextureParticle {

	private static final long LIFETIME = 1000;
	private static final float SIZE = 30;

	public PillParticle(GLContext glContext, Constraint x, Constraint y) {
		super(
				glContext,
				LIFETIME,
				generateBox(x, y),
				absolute(0),
				"images/particles/pill_blue.png"
		);
	}

	private static ConstraintBox generateBox(Constraint x, Constraint y) {
		TimedConstraint time = time().activate();
		float xVelocity = (float) (random() - 0.5f) * 2;
		float yVelocity = (float) (random() - 0.5f) * 2;
		return new ConstraintBox(
				x.add(time.multiply(xVelocity)),
				y.add(time.multiply(time).multiply(-0.002f).add(time.multiply(yVelocity))),
				absolute(SIZE),
				absolute(SIZE)
		);
	}

}
