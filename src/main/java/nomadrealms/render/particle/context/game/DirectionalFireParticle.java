package nomadrealms.render.particle.context.game;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.GLContext;
import nomadrealms.render.particle.TextureParticle;

public class DirectionalFireParticle extends TextureParticle {


	private DirectionalFireParticle(GLContext glContext) {
		super(
				glContext,
				1000,
				new ConstraintBox(absolute(0), absolute(0), absolute(100), absolute(100)),
				absolute(0),
				"directional_fire_small"
		);
	}

}