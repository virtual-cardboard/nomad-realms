package engine.visuals.rendering.geometry;

import engine.common.colour.Colour;
import engine.common.loader.StringLoader;
import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.lwjgl.GLContext;
import engine.visuals.lwjgl.render.FragmentShader;
import engine.visuals.lwjgl.render.Shader;
import engine.visuals.lwjgl.render.ShaderProgram;
import engine.visuals.lwjgl.render.VertexArrayObject;
import engine.visuals.lwjgl.render.VertexShader;

/**
 * A {@link TriangleRenderer} that renders triangles with optional outlines.
 *
 * @author Lunkle
 */
public class TriangleRenderer {

	/**
	 * The {@link ShaderProgram} to use when rendering triangles.
	 */
	private final ShaderProgram program;
	private final VertexArrayObject vao;

	private final GLContext glContext;

	public TriangleRenderer(GLContext glContext) {
		this.glContext = glContext;
		Shader vertex = new VertexShader()
				.source(new StringLoader("/shaders/triangleVertex.glsl").load())
				.load();
		Shader fragment = new FragmentShader()
				.source(new StringLoader("/shaders/triangleFragment.glsl").load())
				.load();
		this.program = new ShaderProgram().attach(vertex, fragment).load();
		this.vao = RectangleVertexArrayObject.instance();
	}

	/**
	 * Renders a triangle with optional outline in pixel coordinates.
	 *
	 * @param x1          the x position of the first corner
	 * @param y1          the y position of the first corner
	 * @param x2          the x position of the second corner
	 * @param y2          the y position of the second corner
	 * @param x3          the x position of the third corner
	 * @param y3          the y position of the third corner
	 * @param fillColor   the fill color (rgba)
	 * @param borderColor the border color (rgba)
	 * @param borderWidth the border width in pixels (inside)
	 */
	public void render(float x1, float y1, float x2, float y2, float x3, float y3, int fillColor, int borderColor, float borderWidth) {
		float minX = Math.min(x1, Math.min(x2, x3));
		float maxX = Math.max(x1, Math.max(x2, x3));
		float minY = Math.min(y1, Math.min(y2, y3));
		float maxY = Math.max(y1, Math.max(y2, y3));

		float w = maxX - minX;
		float h = maxY - minY;

		Matrix4f matrix4f = new Matrix4f()
				.translate(-1, 1)
				.scale(2, -2)
				.scale(1 / glContext.width(), 1 / glContext.height())
				.translate(minX, minY)
				.scale(w, h);

		program.use(glContext);
		program.uniforms()
				.set("transform", matrix4f)
				.set("size", new Vector2f(w, h))
				.set("v1", new Vector2f(x1 - minX, y1 - minY))
				.set("v2", new Vector2f(x2 - minX, y2 - minY))
				.set("v3", new Vector2f(x3 - minX, y3 - minY))
				.set("borderWidth", borderWidth)
				.set("fillColor", Colour.toRangedVector(fillColor))
				.set("borderColor", Colour.toRangedVector(borderColor))
				.complete();
		vao.draw(glContext);
	}

	/**
	 * Renders a triangle with no outline in pixel coordinates.
	 *
	 * @param x1        the x position of the first corner
	 * @param y1        the y position of the first corner
	 * @param x2        the x position of the second corner
	 * @param y2        the y position of the second corner
	 * @param x3        the x position of the third corner
	 * @param y3        the y position of the third corner
	 * @param fillColor the fill color (rgba)
	 */
	public void render(float x1, float y1, float x2, float y2, float x3, float y3, int fillColor) {
		render(x1, y1, x2, y2, x3, y3, fillColor, 0, 0);
	}

}
