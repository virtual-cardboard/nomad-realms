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
		float width = box().w().get();
		float height = box().h().get();
		float padding = 0.1f;
		float pw = width * (1 + padding);
		float ph = height * (1 + padding);
		re.hexagonRenderer.render(
				new Matrix4f(box(), re.glContext)
						.translate(0.5f, 0.5f)
						.rotate(rotation().get(), new Vector3f(0, 0, 1))
						.scale(1 + padding, 1 + padding)
						.translate(-0.5f, -0.5f),
				pw, ph, height * 0.5f, color, 0, 0);
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
