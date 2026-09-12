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
