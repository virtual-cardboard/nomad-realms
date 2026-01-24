package nomadrealms.render.particle.context.home;

import static engine.visuals.constraint.misc.TimedConstraint.time;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.zero;
import static engine.visuals.constraint.posdim.CosineConstraint.cos;
import static engine.visuals.constraint.posdim.SineConstraint.sin;
import static java.lang.Math.PI;
import static java.lang.Math.random;

import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.constraint.misc.TimedConstraint;
import engine.visuals.lwjgl.GLContext;
import nomadrealms.render.particle.TextureParticle;

public class PillParticle extends TextureParticle {

	private static final long LIFETIME = 1000;
	private static final float SIZE = 11;


	private float speed = (float) (random() * 4);

	public PillParticle(GLContext glContext, ConstraintPair position) {
		super(
				glContext,
				LIFETIME,
				new ConstraintBox(zero(), zero(), zero(), zero()),
				zero(),
				"pill_blue"
		);
		rotation(absolute(random() * 2 * PI));
		box(generateBox(position));
	}

	private ConstraintBox generateBox(ConstraintPair position) {
		TimedConstraint time = time().activate();
		float speed = (float) random() * 0.5f;
		return new ConstraintBox(
				position.x().add(time.multiply(sin(rotation())).multiply(speed)),
				position.y().add(time.multiply(cos(rotation())).multiply(-speed)),
				absolute(SIZE * 5 / 11),
				absolute(SIZE)
		);
	}

}
