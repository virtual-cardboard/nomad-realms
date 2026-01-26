package nomadrealms.render.particle;

import engine.common.math.Matrix4f;
import engine.common.math.Vector3f;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.GLContext;
import nomadrealms.render.RenderingEnvironment;

public class TextureParticle extends Particle {

	protected final GLContext glContext;
	private final String texture;

	public TextureParticle(GLContext glContext, long lifetime, ConstraintBox box, Constraint rotation, String texture) {
		super(lifetime, box, rotation);
		this.glContext = glContext;
		this.texture = texture;
	}

	@Override
	public void render(RenderingEnvironment re) {
		float x = box().x().get();
		float y = box().y().get();
		re.textureRenderer
				.render(re.imageMap.get(texture), new Matrix4f()
						.translate(-1, 1)
						.scale(2, -2)
						.scale(1 / glContext.width(), 1 / glContext.height())
						.translate(x, y)
						.translate(0.5f, 0.5f, 0)
						.rotate(rotation().get(), new Vector3f(0, 0, 1))
						.translate(-0.5f, -0.5f, 0)
						.scale(box().w().get(), box().h().get())
						.translate(-0.5f, -0.5f, 0));
	}

	@Override
	public Particle clone() {
		return new TextureParticle(
				glContext,
				lifetime(),
				box(),
				rotation(),
				texture
		);
	}
}
