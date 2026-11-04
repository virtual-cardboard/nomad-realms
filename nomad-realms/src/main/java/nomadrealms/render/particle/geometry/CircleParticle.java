package nomadrealms.render.particle.geometry;

import static engine.common.colour.Colour.rgb;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.common.math.Vector3f;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.posdim.CustomSupplierConstraint;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.Particle;

public class CircleParticle extends Particle {

	protected int color = rgb(126, 200, 80);
	private Vector2f worldPos;
	private float diameter;

	public CircleParticle(long lifetime, ConstraintBox box, Constraint rotation, int color) {
		super(lifetime, box, rotation);
		this.color = color;
	}

	public CircleParticle(long lifetime, ConstraintBox box, int color) {
		this(lifetime, box, absolute(0), color);
	}

	public CircleParticle(long lifetime, Vector2f worldPos, float diameter, int color) {
		super(
				lifetime,
				new ConstraintBox(
						new CustomSupplierConstraint("CircleParticle World X", () -> worldPos.x()),
						new CustomSupplierConstraint("CircleParticle World Y", () -> worldPos.y()),
						absolute(diameter),
						absolute(diameter)
				),
				absolute(0)
		);
		this.worldPos = worldPos;
		this.diameter = diameter;
		this.color = color;
	}

	public int color() {
		return color;
	}

	public Vector2f worldPos() {
		return worldPos;
	}

	public float diameter() {
		return diameter;
	}

	@Override
	public void render(RenderingEnvironment re) {
		if (worldPos != null) {
			float zoom = re.is.camera.zoom().get();
			Vector2f camPos = re.is.camera.position().vector();
			Vector2f screenPos = worldPos.sub(camPos).scale(zoom);
			float screenSize = diameter * zoom;
			float x = screenPos.x() - screenSize * 0.5f;
			float y = screenPos.y() - screenSize * 0.5f;
			Matrix4f matrix = new Matrix4f(x, y, screenSize, screenSize, re.glContext);
			if (rotation() != null && rotation().get() != 0) {
				matrix.translate(0.5f, 0.5f, 0)
						.rotate(rotation().get(), new Vector3f(0, 0, 1))
						.translate(-0.5f, -0.5f, 0);
			}
			re.circleRenderer.render(matrix, screenSize, color);
		} else if (box() != null) {
			re.circleRenderer.render(
					new Matrix4f(box(), re.glContext)
							.translate(0.5f, 0.5f, 0)
							.rotate(rotation().get(), new Vector3f(0, 0, 1))
							.translate(-0.5f, -0.5f, 0),
					box().w().get(),
					color
			);
		}
	}

	@Override
	public Particle clone() {
		if (worldPos != null) {
			return new CircleParticle(lifetime(), worldPos, diameter, color);
		}
		return new CircleParticle(
				lifetime(),
				box(),
				rotation(),
				color
		);
	}

}
