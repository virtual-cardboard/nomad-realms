package engine.visuals.rendering.geometry;

import engine.common.colour.Colour;
import engine.common.loader.StringLoader;
import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.GLContext;
import engine.visuals.lwjgl.render.FragmentShader;
import engine.visuals.lwjgl.render.Shader;
import engine.visuals.lwjgl.render.ShaderProgram;
import engine.visuals.lwjgl.render.VertexArrayObject;
import engine.visuals.lwjgl.render.VertexShader;

/**
 * A {@link HexagonRenderer} that renders hexagons with optional outlines.
 *
 * @author Lunkle
 */
public class HexagonRenderer {

	/**
	 * The {@link ShaderProgram} to use when rendering hexagons.
	 */
	private final ShaderProgram program;
	private final VertexArrayObject vao;

	private final GLContext glContext;

	public HexagonRenderer(GLContext glContext) {
		this.glContext = glContext;
		Shader vertex = new VertexShader()
				.source(new StringLoader("/shaders/hexagonVertex.glsl").load())
				.load();
		Shader fragment = new FragmentShader()
				.source(new StringLoader("/shaders/hexagonFragment.glsl").load())
				.load();
		this.program = new ShaderProgram().attach(vertex, fragment).load();
		this.vao = RectangleVertexArrayObject.instance();
	}

	/**
	 * Renders a hexagon with optional outline in pixel coordinates.
	 *
	 * @param x           the x position in pixels of the top left corner of the bounding box
	 * @param y           the y position in pixels of the top left corner of the bounding box
	 * @param w           the width in pixels
	 * @param h           the height in pixels
	 * @param fillColor   the fill color (rgba)
	 * @param borderColor the border color (rgba)
	 * @param borderWidth the border width in pixels (inside)
	 */
	public void render(float x, float y, float w, float h, int fillColor, int borderColor, float borderWidth) {
		Matrix4f matrix4f = new Matrix4f()
				.translate(-1, 1)
				.scale(2, -2)
				.scale(1 / glContext.width(), 1 / glContext.height())
				.translate(x, y)
				.scale(w, h);
		render(matrix4f, w, h, fillColor, borderColor, borderWidth);
	}

	public void render(ConstraintBox constraintBox, int fillColor, int borderColor, float borderWidth) {
		render(constraintBox.x().get(), constraintBox.y().get(), constraintBox.w().get(), constraintBox.h().get(), fillColor, borderColor, borderWidth);
	}

	/**
	 * Renders a hexagon with no outline in pixel coordinates.
	 *
	 * @param x         the x position in pixels of the top left corner of the bounding box
	 * @param y         the y position in pixels of the top left corner of the bounding box
	 * @param w         the width in pixels
	 * @param h         the height in pixels
	 * @param fillColor the fill color (rgba)
	 */
	public void render(float x, float y, float w, float h, int fillColor) {
		render(x, y, w, h, fillColor, 0, 0);
	}

	/**
	 * Renders a hexagon using a transformation matrix and extra parameters.
	 *
	 * @param matrix4f    the transformation matrix
	 * @param w           the width in pixels (for SDF calculations)
	 * @param h           the height in pixels (for SDF calculations)
	 * @param fillColor   the fill color (rgba)
	 * @param borderColor the border color (rgba)
	 * @param borderWidth the border width in pixels (inside)
	 */
	public void render(Matrix4f matrix4f, float w, float h, int fillColor, int borderColor, float borderWidth) {
		program.use(glContext);
		program.uniforms()
				.set("transform", matrix4f)
				.set("size", new Vector2f(w, h))
				.set("borderWidth", borderWidth)
				.set("fillColor", Colour.toRangedVector(fillColor))
				.set("borderColor", Colour.toRangedVector(borderColor))
				.complete();
		vao.draw(glContext);
	}

}
