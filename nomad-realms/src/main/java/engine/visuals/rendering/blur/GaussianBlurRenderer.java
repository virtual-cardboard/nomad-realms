package engine.visuals.rendering.blur;

import engine.common.loader.StringLoader;
import engine.common.math.Matrix4f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.lwjgl.GLContext;
import engine.visuals.lwjgl.render.FragmentShader;
import engine.visuals.lwjgl.render.FrameBufferObject;
import engine.visuals.lwjgl.render.Shader;
import engine.visuals.lwjgl.render.ShaderProgram;
import engine.visuals.lwjgl.render.Texture;
import engine.visuals.lwjgl.render.VertexArrayObject;
import engine.visuals.lwjgl.render.VertexShader;

/**
 * A renderer that applies a Gaussian blur to textures/framebuffers.
 * Precomputes Gaussian weights on the CPU to avoid per-fragment exp() evaluations.
 */
public class GaussianBlurRenderer {

	public static final int MAX_SAMPLE_RADIUS = 32;

	private final ShaderProgram program;
	private final VertexArrayObject vao;
	private final GLContext glContext;

	public GaussianBlurRenderer(GLContext glContext) {
		this.glContext = glContext;
		Shader vertex = new VertexShader()
				.source(new StringLoader("/shaders/gaussian_blur_vertex.glsl").load())
				.load();
		Shader fragment = new FragmentShader()
				.source(new StringLoader("/shaders/gaussian_blur.glsl").load())
				.load();
		this.program = new ShaderProgram().attach(vertex, fragment).load();
		this.vao = RectangleVertexArrayObject.instance();
	}

	public GaussianBlurRenderer(GLContext glContext, ShaderProgram program) {
		this.glContext = glContext;
		this.program = program;
		this.vao = RectangleVertexArrayObject.instance();
	}

	public ShaderProgram program() {
		return program;
	}

	/**
	 * Precomputes normalized Gaussian weights for a given blur radius.
	 * Returns an array of size MAX_SAMPLE_RADIUS + 1 (33 elements).
	 */
	public static float[] calculateWeights(float radius) {
		float r = Math.max(radius, 1.0f);
		float sigma = Math.max(r / 2.0f, 0.5f);
		float twoSigmaSq = 2.0f * sigma * sigma;

		int sampleRadius = Math.min(Math.max((int) Math.ceil(sigma * 3.0f), 1), MAX_SAMPLE_RADIUS);

		float[] weights = new float[MAX_SAMPLE_RADIUS + 1];
		weights[0] = 1.0f;
		float sum = 1.0f;

		for (int i = 1; i <= sampleRadius; i++) {
			float w = (float) Math.exp(-(float) (i * i) / twoSigmaSq);
			weights[i] = w;
			sum += 2.0f * w;
		}

		for (int i = 0; i <= sampleRadius; i++) {
			weights[i] /= sum;
		}

		return weights;
	}

	public static int calculateSampleRadius(float radius) {
		float r = Math.max(radius, 1.0f);
		float sigma = Math.max(r / 2.0f, 0.5f);
		return Math.min(Math.max((int) Math.ceil(sigma * 3.0f), 1), MAX_SAMPLE_RADIUS);
	}

	/**
	 * Performs a single pass (horizontal or vertical) of Gaussian blur.
	 *
	 * @param texture the input texture
	 * @param targetFbo the destination framebuffer
	 * @param horizontal true for horizontal pass, false for vertical pass
	 * @param radius the blur radius
	 * @param transform the transformation matrix
	 */
	public void renderPass(Texture texture, FrameBufferObject targetFbo, boolean horizontal, float radius, Matrix4f transform) {
		int sampleRadius = calculateSampleRadius(radius);
		float[] weights = calculateWeights(radius);

		targetFbo.render(() -> {
			program.use(glContext);
			program.set("horizontal", horizontal ? 1 : 0)
					.set("sampleRadius", sampleRadius)
					.set("weights", weights)
					.set("transform", transform)
					.set("textureSampler", 0);
			texture.bind(glContext, 0);
			vao.draw(glContext);
		});
	}

	/**
	 * Performs a single pass (horizontal or vertical) of Gaussian blur using full-screen matrix.
	 */
	public void renderPass(Texture texture, FrameBufferObject targetFbo, boolean horizontal, float radius) {
		Matrix4f defaultTransform = new Matrix4f().translate(-1, 1).scale(2, -2);
		renderPass(texture, targetFbo, horizontal, radius, defaultTransform);
	}

	/**
	 * Performs a full two-pass Gaussian blur (horizontal pass into intermediateFbo, then vertical pass into targetFbo).
	 *
	 * @param sourceTexture the input texture
	 * @param intermediateFbo intermediate framebuffer for the horizontal pass
	 * @param targetFbo destination framebuffer for the vertical pass
	 * @param radius the blur radius
	 * @param transform transformation matrix for rendering quad
	 */
	public void render(Texture sourceTexture, FrameBufferObject intermediateFbo, FrameBufferObject targetFbo, float radius, Matrix4f transform) {
		renderPass(sourceTexture, intermediateFbo, true, radius, transform);
		renderPass(intermediateFbo.texture(), targetFbo, false, radius, transform);
	}

	/**
	 * Performs a full two-pass Gaussian blur using standard full-screen transformation matrix.
	 *
	 * @param sourceTexture the input texture
	 * @param intermediateFbo intermediate framebuffer for the horizontal pass
	 * @param targetFbo destination framebuffer for the vertical pass
	 * @param radius the blur radius
	 */
	public void render(Texture sourceTexture, FrameBufferObject intermediateFbo, FrameBufferObject targetFbo, float radius) {
		Matrix4f defaultTransform = new Matrix4f().translate(-1, 1).scale(2, -2);
		render(sourceTexture, intermediateFbo, targetFbo, radius, defaultTransform);
	}

}
