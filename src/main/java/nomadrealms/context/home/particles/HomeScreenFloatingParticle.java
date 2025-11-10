package nomadrealms.context.home.particles;

import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.GLContext;
import nomadrealms.render.particle.HexagonParticle;

public class HomeScreenFloatingParticle extends HexagonParticle {

	public HomeScreenFloatingParticle(GLContext glContext, long lifetime, ConstraintBox box, Constraint rotation, int color) {
		super(glContext, lifetime, box, rotation, color);
	}
	
}
