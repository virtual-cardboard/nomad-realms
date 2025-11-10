package nomadrealms.render.particle;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;

import engine.common.math.Matrix4f;
import engine.common.math.Vector3f;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.GLContext;
import engine.visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.vao.shape.HexagonVao;

public class HexagonParticle extends Particle {

	private final GLContext glContext;
	protected int color = rgb(126, 200, 80);

	public HexagonParticle(ConstraintBox box, GLContext glContext, Constraint rotation, int color) {
		super(5000, box, rotation);
		this.glContext = glContext;
		this.color = color;
	}

	@Override
	public void render(RenderingEnvironment re) {
		DefaultFrameBuffer.instance().render(() -> {
			re.defaultShaderProgram
					.set("color", toRangedVector(color))
					.set("transform", new Matrix4f(box(), glContext).rotate(rotation().get(), new Vector3f(0, 0, 1)))
					.use(new DrawFunction().vao(HexagonVao.instance()).glContext(re.glContext));
		});
	}
}
