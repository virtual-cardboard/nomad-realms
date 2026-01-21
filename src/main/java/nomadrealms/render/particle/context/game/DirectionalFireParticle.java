package nomadrealms.render.particle.context.game;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.GLContext;
import nomadrealms.render.particle.TextureParticle;

public class DirectionalFireParticle extends TextureParticle {


	public DirectionalFireParticle(GLContext glContext, long lifetime, ConstraintBox box, Constraint rotation) {
		super(glContext, lifetime, box, rotation, "directional_fire_small", true);
	}

}