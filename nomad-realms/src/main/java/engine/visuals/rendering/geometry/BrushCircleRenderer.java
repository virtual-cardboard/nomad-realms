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
 * A renderer that renders circles with hand-drawn brush outlines.
 *
 * @author Jules
 */
public class BrushCircleRenderer {

	private final ShaderProgram program;
	private final VertexArrayObject vao;
	private final GLContext glContext;

	public BrushCircleRenderer(GLContext glContext, VertexShader vertexShader) {
		this.glContext = glContext;
		this.vao = RectangleVertexArrayObject.instance();
		Shader fragment = new FragmentShader()
				.source(new StringLoader("/shaders/brushCircleFragment.glsl").load())
				.load();
		this.program = new ShaderProgram().attach(vertexShader, fragment).load();
	}

	/**
	 * Renders a circle with a hand-drawn brush outline.
	 *
	 * @param cx           the x position of the center of the circle in pixels
	 * @param cy           the y position of the center of the circle in pixels
	 * @param radius       the radius of the circle in pixels
	 * @param brushSize    the diameter of the brush in pixels (e.g., 32 or 64)
	 * @param hardness     the hardness of the brush (e.g., 0.75f)
	 * @param fillColor    the fill color (rgba)
	 * @param borderColor  the border/brush outline color (rgba)
	 */
	public void render(float cx, float cy, float radius, float brushSize, float hardness, int fillColor, int borderColor) {
		float brushRadius = brushSize * 0.5f;

		// Original bounding box size is 2 * radius
		float size = radius * 2.0f;

		// Expanded bounds
		float paddedSize = size + brushSize;
		float paddedX = cx - radius - brushRadius;
		float paddedY = cy - radius - brushRadius;

		Matrix4f matrix4f = new Matrix4f()
				.translate(-1, 1)
				.scale(2, -2)
				.scale(1 / glContext.width(), 1 / glContext.height())
				.translate(paddedX, paddedY)
				.scale(paddedSize, paddedSize);

		program.use(glContext);
		program.uniforms()
				.set("transform", matrix4f)
				.set("size", new Vector2f(size, size))
				.set("paddedSize", new Vector2f(paddedSize, paddedSize))
				.set("radius", radius)
				.set("brushRadius", brushRadius)
				.set("hardness", hardness)
				.set("fillColor", Colour.toRangedVector(fillColor))
				.set("borderColor", Colour.toRangedVector(borderColor))
				.complete();
		vao.draw(glContext);
	}

}
