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
import engine.visuals.lwjgl.render.Texture;
import engine.visuals.lwjgl.render.VertexArrayObject;
import engine.visuals.lwjgl.render.VertexShader;

/**
 * A {@link HexagonRenderer} that renders hexagons with optional outlines.
 *
 * @author Lunkle
 */
public class HexagonRenderer {

	private final ShaderProgram program;
	private final ShaderProgram texturedProgram;
	private final ShaderProgram instancedProgram;

	private final VertexArrayObject vao;
	private final GLContext glContext;

	public HexagonRenderer(GLContext glContext) {
		this.glContext = glContext;
		this.vao = RectangleVertexArrayObject.instance();

		Shader vertex = new VertexShader()
				.source(new StringLoader("/shaders/hexagonVertex.glsl").load())
				.load();
		Shader fragment = new FragmentShader()
				.source(new StringLoader("/shaders/hexagonFragment.glsl").load())
				.load();
		this.program = new ShaderProgram().attach(vertex, fragment).load();

		Shader texturedFragment = new FragmentShader()
				.source(new StringLoader("/shaders/texturedHexagonFragment.glsl").load())
				.load();
		this.texturedProgram = new ShaderProgram().attach(vertex, texturedFragment).load();

		Shader instancedVertex = new VertexShader()
				.source(new StringLoader("/shaders/instancedHexagonVertex.glsl").load())
				.load();
		Shader instancedFragment = new FragmentShader()
				.source(new StringLoader("/shaders/instancedHexagonFrag.glsl").load())
				.load();
		this.instancedProgram = new ShaderProgram().attach(instancedVertex, instancedFragment).load();
	}

	/**
	 * Renders a hexagon with optional outline in pixel coordinates.
	 */
	public void render(float x, float y, float w, float h, int fillColor, int borderColor, float borderWidth) {
		float padding = 0.1f;
		float pw = w * (1 + padding);
		float ph = h * (1 + padding);
		Matrix4f matrix4f = new Matrix4f()
				.translate(-1, 1)
				.scale(2, -2)
				.scale(1 / glContext.width(), 1 / glContext.height())
				.translate(x - (pw - w) * 0.5f, y - (ph - h) * 0.5f)
				.scale(pw, ph);
		render(matrix4f, pw, ph, h * 0.5f, fillColor, borderColor, borderWidth);
	}

	public void render(ConstraintBox constraintBox, int fillColor, int borderColor, float borderWidth) {
		render(constraintBox.x().get(), constraintBox.y().get(), constraintBox.w().get(), constraintBox.h().get(), fillColor, borderColor, borderWidth);
	}

	/**
	 * Renders a hexagon using a transformation matrix and extra parameters.
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

	/**
	 * Renders a textured hexagon using a transformation matrix and extra parameters.
	 */
	public void renderTextured(Matrix4f matrix4f, float w, float h, float radius, int color, Texture texture) {
		texturedProgram.use(glContext);
		texturedProgram.uniforms()
				.set("transform", matrix4f)
				.set("size", new Vector2f(w, h))
				.set("radius", radius)
				.set("color", Colour.toRangedVector(color))
				.set("textureSampler", 0)
				.complete();
		texture.bind(glContext, 0);
		vao.draw(glContext);
	}

	public ShaderProgram instancedProgram() {
		return instancedProgram;
	}

}
