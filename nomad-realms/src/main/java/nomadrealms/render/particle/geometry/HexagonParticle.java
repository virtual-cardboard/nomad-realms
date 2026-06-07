package nomadrealms.render.particle.geometry;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;

import engine.common.math.Matrix4f;
import engine.common.math.Vector3f;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.Particle;

public class HexagonParticle extends Particle {

	protected int color = rgb(126, 200, 80);

	public HexagonParticle(long lifetime, ConstraintBox box, Constraint rotation, int color) {
		super(lifetime, box, rotation);
		this.color = color;
	}

	@Override
	public void render(RenderingEnvironment re) {
		re.hexagonRenderer.render(
				new Matrix4f(box(), re.glContext).rotate(rotation().get(), new Vector3f(0, 0, 1)),
				box().w().get(), box().h().get(), color, 0, 0);
	}

	@Override
	public Particle clone() {
		return new HexagonParticle(
				lifetime(),
				box(),
				rotation(),
				color
		);
	}
}
