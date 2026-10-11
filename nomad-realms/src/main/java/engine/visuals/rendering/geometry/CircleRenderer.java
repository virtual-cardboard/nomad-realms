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

/**
 * A {@link CircleRenderer} that renders circles with anti-aliasing.
 *
 * @author Lunkle
 */
public class CircleRenderer {

	private static final float PADDING = 0.1f;

	private final ShaderProgram program;
	private final VertexArrayObject vao;
	private final GLContext glContext;

	public CircleRenderer(GLContext glContext, Shader vertexShader) {
		this.glContext = glContext;
		this.vao = RectangleVertexArrayObject.instance();
		Shader fragment = new FragmentShader()
				.source(new StringLoader("/shaders/circleFrag.glsl").load())
				.load();
		this.program = new ShaderProgram().attach(vertexShader, fragment).load();
	}

	/**
	 * Renders a circle using pixel coordinates for the center point and radius.
	 *
	 * @param cx     the x position in pixels of the center of the circle
	 * @param cy     the y position in pixels of the center of the circle
	 * @param radius the radius in pixels
	 * @param color  the fill color (rgba)
	 */
	public void render(float cx, float cy, float radius, int color) {
		float diameter = radius * 2;
		Matrix4f matrix4f = new Matrix4f()
				.translate(-1, 1)
				.scale(2, -2)
				.scale(1 / glContext.width(), 1 / glContext.height())
				.translate(cx - radius, cy - radius)
				.scale(diameter, diameter);
		render(matrix4f, diameter, color);
	}

	/**
	 * Renders a circle using a transformation matrix and extra parameters.
	 */
	public void render(Matrix4f matrix4f, float size, int color) {
		float ps = size * (1 + PADDING);
		Matrix4f paddedMatrix = new Matrix4f(matrix4f)
				.translate(0.5f, 0.5f)
				.scale(1 + PADDING, 1 + PADDING)
				.translate(-0.5f, -0.5f);
		program.use(glContext);
		program.uniforms()
				.set("transform", paddedMatrix)
				.set("size", new Vector2f(ps, ps))
				.set("radius", size * 0.5f)
				.set("color", Colour.toRangedVector(color))
				.complete();
		vao.draw(glContext);
	}

}
