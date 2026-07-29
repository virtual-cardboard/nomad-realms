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
 * A renderer that renders rectangles with rounded corners and hand-drawn brush outlines.
 *
 * @author Jules
 */
public class BrushRectangleRenderer {

	private final ShaderProgram program;
	private final VertexArrayObject vao;
	private final GLContext glContext;

	public BrushRectangleRenderer(GLContext glContext, VertexShader vertexShader) {
		this.glContext = glContext;
		Shader fragment = new FragmentShader()
				.source(new StringLoader("/shaders/brushRectangleFragment.glsl").load())
				.load();
		this.program = new ShaderProgram().attach(vertexShader, fragment).load();
		this.vao = RectangleVertexArrayObject.instance();
	}

	/**
	 * Renders a rounded rectangle with hand-drawn brush outlines.
	 *
	 * @param x            the x position in pixels of the top left corner of the rectangle
	 * @param y            the y position in pixels of the top left corner of the rectangle
	 * @param w            the width in pixels
	 * @param h            the height in pixels
	 * @param radius       the corner radius in pixels
	 * @param brushSize    the diameter of the brush in pixels (e.g., 32 or 64)
	 * @param hardness     the hardness of the brush (e.g., 0.75f)
	 * @param fillColor    the fill color (rgba)
	 * @param borderColor  the border color (rgba)
	 */
	public void render(float x, float y, float w, float h, float radius, float brushSize, float hardness, int fillColor, int borderColor) {
		float brushRadius = brushSize * 0.5f;

		// Expand the drawing geometry by brushRadius in all directions
		float paddedX = x - brushRadius;
		float paddedY = y - brushRadius;
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
				.set("radius", radius)
				.set("brushRadius", brushRadius)
				.set("hardness", hardness)
				.set("fillColor", Colour.toRangedVector(fillColor))
				.set("borderColor", Colour.toRangedVector(borderColor))
				.complete();
		vao.draw(glContext);
	}

}
