package nomadrealms.render.ui.custom.card;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;
import static engine.common.math.Matrix4f.screenToPixel;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;
import static nomadrealms.render.vao.shape.HexagonVao.SIDE_LENGTH;

import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.common.math.Vector3f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.lwjgl.GLContext;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;
import nomadrealms.render.vao.shape.HexagonVao;

public class Arrow implements UI {

	private final Vector2f source;
	private final Vector2f targetPoint;
	private Vector2f targetCenter;

	public Arrow(Vector2f source, Vector2f targetPoint) {
		this.source = source;
		this.targetPoint = targetPoint;
	}

	public Arrow targetCenter(Vector2f targetCenter) {
		this.targetCenter = targetCenter;
		return this;
	}

	@Override
	public void render(RenderingEnvironment re) {
		if (targetCenter != null) {
			re.defaultShaderProgram
					.set("color", toRangedVector(rgb(255, 255, 0)))
					.set("transform", new Matrix4f(
							targetCenter.x(), targetCenter.y(),
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

	private Matrix4f lineTransform(GLContext glContext, Vector2f point1, Vector2f point2) {
		float angle = (float) Math.atan2(point2.y() - point1.y(), point2.x() - point1.x());
		return screenToPixel(glContext)
				.translate(point1.x(), point1.y())
				.scale(new Vector3f(1, 1, 0f)) // Flatten the z-axis to avoid clipping
				.rotate(angle, new Vector3f(0, 0, 1))
				.translate(0, -5, 0)
				.scale(point1.sub(point2).length(), 3);
	}

}
