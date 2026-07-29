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
 * A renderer that renders triangles with hand-drawn brush outlines.
 *
 * @author Jules
 */
public class BrushTriangleRenderer {

	private final ShaderProgram program;
	private final VertexArrayObject vao;
	private final GLContext glContext;

	public BrushTriangleRenderer(GLContext glContext) {
		this.glContext = glContext;
		Shader vertex = new VertexShader()
				.source(new StringLoader("/shaders/triangleVertex.glsl").load())
				.load();
		Shader fragment = new FragmentShader()
				.source(new StringLoader("/shaders/brushTriangleFragment.glsl").load())
				.load();
		this.program = new ShaderProgram().attach(vertex, fragment).load();
		this.vao = RectangleVertexArrayObject.instance();
	}

	/**
	 * Renders a triangle with hand-drawn brush outlines.
	 *
	 * @param x1           the x position of the first corner
	 * @param y1           the y position of the first corner
	 * @param x2           the x position of the second corner
	 * @param y2           the y position of the second corner
	 * @param x3           the x position of the third corner
	 * @param y3           the y position of the third corner
	 * @param brushSize    the diameter of the brush in pixels (e.g., 32 or 64)
	 * @param hardness     the hardness of the brush (e.g., 0.75f)
	 * @param fillColor    the fill color (rgba)
	 * @param borderColor  the border/brush outline color (rgba)
	 */
	public void render(float x1, float y1, float x2, float y2, float x3, float y3, float brushSize, float hardness, int fillColor, int borderColor) {
		float minX = Math.min(x1, Math.min(x2, x3));
		float maxX = Math.max(x1, Math.max(x2, x3));
		float minY = Math.min(y1, Math.min(y2, y3));
		float maxY = Math.max(y1, Math.max(y2, y3));

		float w = maxX - minX;
		float h = maxY - minY;

		float brushRadius = brushSize * 0.5f;

		// Expand the bounding box by brushRadius on all sides to prevent outline clipping
		float paddedX = minX - brushRadius;
		float paddedY = minY - brushRadius;
		float paddedW = w + brushSize;
		float paddedH = h + brushSize;

		Matrix4f matrix4f = new Matrix4f()
				.translate(-1, 1)
				.scale(2, -2)
				.scale(1 / glContext.width(), 1 / glContext.height())
				.translate(paddedX, paddedY)
				.scale(paddedW, paddedH);

		program.use(glContext);
		program.uniforms()
				.set("transform", matrix4f)
				.set("size", new Vector2f(w, h))
				.set("paddedSize", new Vector2f(paddedW, paddedH))
				.set("v1", new Vector2f(x1 - minX, y1 - minY))
				.set("v2", new Vector2f(x2 - minX, y2 - minY))
				.set("v3", new Vector2f(x3 - minX, y3 - minY))
				.set("brushRadius", brushRadius)
				.set("hardness", hardness)
				.set("fillColor", Colour.toRangedVector(fillColor))
				.set("borderColor", Colour.toRangedVector(borderColor))
				.complete();
		vao.draw(glContext);
	}

}
