package nomadrealms.render.ui.custom.card;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;
import static engine.common.math.Matrix4f.screenToPixel;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;
import static nomadrealms.render.vao.shape.HexagonVao.SIDE_LENGTH;

import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.common.math.Vector3f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.GLContext;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import engine.visuals.rendering.texture.ImageCropBox;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.Target;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;
import nomadrealms.render.vao.shape.HexagonVao;

public class StackIcon implements UI {

	private final CardPlayedEvent event;
	private final ConstraintBox constraintBox;

	public StackIcon(CardPlayedEvent event, ConstraintBox constraintBox) {
		this.event = event;
		this.constraintBox = constraintBox;
	}

	@Override
	public void render(RenderingEnvironment re) {
		re.defaultShaderProgram
				.set("color", toRangedVector(rgb(210, 180, 140)))
				.set("transform", new Matrix4f(constraintBox, re.glContext))
				.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
		re.textureRenderer.render(
				re.imageMap.get(event.card().card().artwork()),
				new Matrix4f(constraintBox, re.glContext),
				new ImageCropBox(new ConstraintBox(absolute(0.1f), absolute(0.1f), absolute(0.8f), absolute(0.8f)))
		);

		if (constraintBox.contains(re.mouse.coordinate()) && event.target() != null) {
			renderTargetingArrow(re);
		}
	}

	private void renderTargetingArrow(RenderingEnvironment re) {
		Target target = event.target();
		Vector2f targetPos = target.tile().getScreenPosition(re).vector();
		Vector2f startPos = constraintBox.center().vector();

		re.defaultShaderProgram
				.set("color", toRangedVector(rgb(255, 255, 0)))
				.set("transform", new Matrix4f(
						targetPos.x(), targetPos.y(),
						TILE_RADIUS * 2 * SIDE_LENGTH * 0.98f * re.camera.zoom().get(),
						TILE_RADIUS * 2 * SIDE_LENGTH * 0.98f * re.camera.zoom().get(),
						re.glContext))
				.use(new DrawFunction()
						.vao(HexagonVao.instance())
						.glContext(re.glContext)
				);

		re.defaultShaderProgram
				.set("color", toRangedVector(rgb(0, 0, 0)))
				.set("transform", lineTransform(re.glContext, startPos, targetPos))
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
