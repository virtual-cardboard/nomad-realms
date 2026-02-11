package nomadrealms.render.particle.geometry;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;

import engine.common.math.Matrix4f;
import engine.common.math.Vector3f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.Particle;

public class RectangleParticle extends Particle {

	protected int color = rgb(126, 200, 80);

	public RectangleParticle(long lifetime, ConstraintBox box, Constraint rotation, int color) {
		super(lifetime, box, rotation);
		this.color = color;
	}

	@Override
	public void render(RenderingEnvironment re) {
		re.defaultShaderProgram
				.set("color", toRangedVector(color))
				.set("transform", new Matrix4f()
						.translate(-1, 1)
						.scale(2, -2)
						.scale(1 / re.glContext.width(), 1 / re.glContext.height())
						.translate(box().coordinate().vector())
						.translate(0.5f, 0.5f, 0)
						.rotate(rotation().get(), new Vector3f(0, 0, 1))
						.translate(-0.5f, -0.5f, 0)
						.scale(box().w().get(), box().h().get())
						.translate(-0.5f, -0.5f, 0))
				.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
	}

	@Override
	public Particle clone() {
		return new RectangleParticle(
				lifetime(),
				box(),
				rotation(),
				color
		);
	}
}
