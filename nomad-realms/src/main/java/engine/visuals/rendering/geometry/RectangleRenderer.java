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
 * A {@link RectangleRenderer} that renders rectangles with rounded corners and optional outlines.
 *
 * @author Lunkle
 */
public class RectangleRenderer {

	/**
	 * The {@link ShaderProgram} to use when rendering rectangles.
	 */
	private final ShaderProgram program;
	private final VertexArrayObject vao;

	private final GLContext glContext;

	public RectangleRenderer(GLContext glContext) {
		this.glContext = glContext;
		Shader vertex = new VertexShader()
				.source(new StringLoader("/shaders/rectangleVertex.glsl").load())
				.load();
		Shader fragment = new FragmentShader()
				.source(new StringLoader("/shaders/rectangleFragment.glsl").load())
				.load();
		this.program = new ShaderProgram().attach(vertex, fragment).load();
		this.vao = RectangleVertexArrayObject.instance();
	}

	/**
	 * Renders a rectangle with rounded corners and optional outline in pixel coordinates.
	 *
	 * @param x           the x position in pixels of the top left corner of the rectangle
	 * @param y           the y position in pixels of the top left corner of the rectangle
	 * @param w           the width in pixels
	 * @param h           the height in pixels
	 * @param radius      the corner radius in pixels
	 * @param fillColor   the fill color (rgba)
	 * @param borderColor the border color (rgba)
	 * @param borderWidth the border width in pixels (inside)
	 */
	public void render(float x, float y, float w, float h, float radius, int fillColor, int borderColor, float borderWidth) {
		Matrix4f matrix4f = new Matrix4f()
				.translate(-1, 1)
				.scale(2, -2)
				.scale(1 / glContext.width(), 1 / glContext.height())
				.translate(x, y)
				.scale(w, h);
		render(matrix4f, w, h, radius, fillColor, borderColor, borderWidth);
	}

	public void render(ConstraintBox constraintBox, float radius, int fillColor, int borderColor, float borderWidth) {
		render(constraintBox.x().get(), constraintBox.y().get(), constraintBox.w().get(), constraintBox.h().get(), radius, fillColor, borderColor, borderWidth);
	}

	/**
	 * Renders a rectangle with rounded corners and no outline in pixel coordinates.
	 *
	 * @param x         the x position in pixels of the top left corner of the rectangle
	 * @param y         the y position in pixels of the top left corner of the rectangle
	 * @param w         the width in pixels
	 * @param h         the height in pixels
	 * @param radius    the corner radius in pixels
	 * @param fillColor the fill color (rgba)
	 */
	public void render(float x, float y, float w, float h, float radius, int fillColor) {
		render(x, y, w, h, radius, fillColor, 0, 0);
	}

	/**
	 * Renders a rectangle using a transformation matrix and extra parameters.
	 *
	 * @param matrix4f    the transformation matrix
	 * @param w           the width in pixels (for SDF calculations)
	 * @param h           the height in pixels (for SDF calculations)
	 * @param radius      the corner radius in pixels
	 * @param fillColor   the fill color (rgba)
	 * @param borderColor the border color (rgba)
	 * @param borderWidth the border width in pixels (inside)
	 */
	public void render(Matrix4f matrix4f, float w, float h, float radius, int fillColor, int borderColor, float borderWidth) {
		program.use(glContext);
		program.uniforms()
				.set("transform", matrix4f)
				.set("size", new Vector2f(w, h))
				.set("radius", radius)
				.set("borderWidth", borderWidth)
				.set("fillColor", Colour.toRangedVector(fillColor))
				.set("borderColor", Colour.toRangedVector(borderColor))
				.complete();
		vao.draw(glContext);
	}

}
