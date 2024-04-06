package nomadrealms.render.ui;

import static common.colour.Colour.rgb;
import static common.colour.Colour.toRangedVector;
import static common.math.Matrix4f.screenToPixel;

import common.colour.Colour;
import common.math.Matrix4f;
import common.math.Vector2f;
import common.math.Vector3f;
import context.input.Mouse;
import nomadrealms.game.card.UICard;
import nomadrealms.render.RenderingEnvironment;
import visuals.builtin.RectangleVertexArrayObject;
import visuals.lwjgl.GLContext;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import visuals.lwjgl.render.meta.DrawFunction;

public class TargetingArrow extends UI {

	UICard origin;
	Mouse mouse;

	@Override
	public void render(RenderingEnvironment re) {
		if (origin == null || mouse == null) {
			return;
		}
		DefaultFrameBuffer.instance().render(() -> {
					re.defaultShaderProgram
							.set("color", toRangedVector(rgb(0, 0, 0)))
							.set("transform", lineTransform(re.glContext, mouse.coordinate().value().toVector(),
									origin.position()))
							.use(
									new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext)
							);
				}
		);
	}

	public TargetingArrow origin(UICard origin) {
		this.origin = origin;
		return this;
	}

	public TargetingArrow mouse(Mouse mouse) {
		this.mouse = mouse;
		return this;
	}

	private Matrix4f lineTransform(GLContext glContext, Vector2f point1, Vector2f point2) {
		float angle = (float) Math.atan2(point2.y() - point1.y(), point2.x() - point1.x());
		return screenToPixel(glContext)
				.translate(point1.x(), point1.y())
				.scale(new Vector3f(1, 1, 0f)) // Flatten the z-axis to avoid clipping
				.rotate(angle, new Vector3f(0, 0, 1))
				.translate(0, -5, 0)
				.scale(point1.sub(point2).length(), 10);
	}

}
