package nomadrealms.render.ui.custom.card;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;
import static engine.common.math.Matrix4f.screenToPixel;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;
import static nomadrealms.render.vao.shape.HexagonVao.SIDE_LENGTH;

import engine.common.math.Matrix4f;
import engine.common.math.Vector3f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.lwjgl.GLContext;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;
import nomadrealms.render.vao.shape.HexagonVao;

public class Arrow implements UI {

	private final ConstraintPair source;
	private final ConstraintPair targetPoint;
	private ConstraintPair targetCenter;

	public Arrow(ConstraintPair source, ConstraintPair targetPoint) {
		this.source = source;
		this.targetPoint = targetPoint;
	}

	public Arrow targetCenter(ConstraintPair targetCenter) {
		this.targetCenter = targetCenter;
		return this;
	}

	@Override
	public void render(RenderingEnvironment re) {
		if (targetCenter != null) {
			re.defaultShaderProgram
					.set("color", toRangedVector(rgb(255, 255, 0)))
					.set("transform", new Matrix4f(
							targetCenter.x().get(), targetCenter.y().get(),
							TILE_RADIUS * 2 * SIDE_LENGTH * 0.98f * re.camera.zoom().get(),
							TILE_RADIUS * 2 * SIDE_LENGTH * 0.98f * re.camera.zoom().get(),
							re.glContext))
					.use(new DrawFunction()
							.vao(HexagonVao.instance())
							.glContext(re.glContext)
					);
		}

		re.defaultShaderProgram
				.set("color", toRangedVector(rgb(0, 0, 0)))
				.set("transform", lineTransform(re.glContext, targetPoint, source))
				.use(
						new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext)
				);
	}

	private Matrix4f lineTransform(GLContext glContext, ConstraintPair point1, ConstraintPair point2) {
		float angle = (float) Math.atan2(point2.y().sub(point1.y()).get(), point2.x().sub(point1.x()).get());
		return screenToPixel(glContext)
				.translate(point1.vector())
				.scale(new Vector3f(1, 1, 0f)) // Flatten the z-axis to avoid clipping
				.rotate(angle, new Vector3f(0, 0, 1))
				.translate(0, -5, 0)
				.scale(point1.sub(point2).vector().length(), 3);
	}

}
