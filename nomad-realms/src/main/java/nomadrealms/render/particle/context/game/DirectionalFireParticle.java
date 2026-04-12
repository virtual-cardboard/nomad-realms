package nomadrealms.render.particle.context.game;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.GLContext;
import nomadrealms.event.game.effect.EffectContext;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.TextureParticle;

public class DirectionalFireParticle extends TextureParticle {

	public DirectionalFireParticle(GLContext glContext, long lifetime, ConstraintBox box, Constraint rotation) {
		super(glContext, lifetime, box, rotation, "directional_fire_small");
	}

	public DirectionalFireParticle(RenderingEnvironment re, EffectContext p) {
		this(re, p, -1);
	}

	public DirectionalFireParticle(RenderingEnvironment re, EffectContext p, int color) {
		super(re.glContext, 100000,
				new ConstraintBox(absolute(20), absolute(20), absolute(20), absolute(20)),
				absolute(0),
				"directional_fire_small",
				color);
	}

}
