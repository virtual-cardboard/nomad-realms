package nomadrealms.render.particle;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;

import engine.common.math.Matrix4f;
import engine.common.math.Vector3f;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;

import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.vao.shape.PillVao;

public class PillParticle extends Particle {

	protected int color = rgb(126, 200, 80);

	public PillParticle(long lifetime, ConstraintBox box, Constraint rotation, int color) {
		super(lifetime, box, rotation);
		this.color = color;
	}

	@Override
	public void render(RenderingEnvironment re) {
		re.defaultShaderProgram
				.set("color", toRangedVector(color))
				.set("transform", new Matrix4f(box(), re.glContext).rotate(rotation().get(), new Vector3f(0, 0, 1)))
				.use(new DrawFunction()
						.vao(PillVao.instance())
						.glContext(re.glContext)
						.drawMode(GL_TRIANGLE_FAN)
						.count(PillVao.VERTICES.length / 2));
	}

	@Override
	public Particle clone() {
		return new PillParticle(
				lifetime(),
				box(),
				rotation(),
				color
		);
	}
}
