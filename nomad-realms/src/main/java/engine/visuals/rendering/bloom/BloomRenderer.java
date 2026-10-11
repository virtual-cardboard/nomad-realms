package engine.visuals.rendering.bloom;

import engine.common.colour.Colour;
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
import engine.visuals.rendering.blur.GaussianBlurRenderer;

/**
 * Renderer for applying bloom effects to a scene.
 */
public class BloomRenderer {

	private final ShaderProgram brightnessProgram;
	private final ShaderProgram combinationProgram;
	private final GaussianBlurRenderer blurRenderer;
	private final VertexArrayObject vao;
	private final GLContext glContext;

	private float threshold = 0.4f;
	private float blurRadius = 8.0f;
	private float intensity = 1.0f;
	private int backgroundColor = 0;

	public BloomRenderer(GLContext glContext) {
		this(glContext, new GaussianBlurRenderer(glContext));
	}

	public BloomRenderer(GLContext glContext, GaussianBlurRenderer blurRenderer) {
		this.glContext = glContext;
		this.blurRenderer = blurRenderer;
		this.vao = RectangleVertexArrayObject.instance();

		Shader vertex = new VertexShader()
				.source(new StringLoader("/shaders/bloomVertex.glsl").load())
				.load();
		Shader brightnessFrag = new FragmentShader()
				.source(new StringLoader("/shaders/brightness.glsl").load())
				.load();
		Shader combinationFrag = new FragmentShader()
				.source(new StringLoader("/shaders/bloom_combination.glsl").load())
				.load();

		this.brightnessProgram = new ShaderProgram().attach(vertex, brightnessFrag).load();
		this.combinationProgram = new ShaderProgram().attach(vertex, combinationFrag).load();
	}

	public BloomRenderer threshold(float threshold) {
		this.threshold = threshold;
		return this;
	}

	public float threshold() {
		return threshold;
	}

	public BloomRenderer blurRadius(float blurRadius) {
		this.blurRadius = blurRadius;
		return this;
	}

	public float blurRadius() {
		return blurRadius;
	}

	public BloomRenderer intensity(float intensity) {
		this.intensity = intensity;
		return this;
	}

	public float intensity() {
		return intensity;
	}

	public BloomRenderer backgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
		return this;
	}

	public int backgroundColor() {
		return backgroundColor;
	}

	public GaussianBlurRenderer blurRenderer() {
		return blurRenderer;
	}

	/**
	 * Renders the bloom effect.
	 *
	 * @param sceneTexture input scene texture
	 * @param brightFbo    destination FBO for extracted bright areas
	 * @param blurFbo      intermediate FBO for blur passes
	 * @param targetFbo    destination FBO for combined scene and bloom
	 */
	public void render(Texture sceneTexture, FrameBufferObject brightFbo, FrameBufferObject blurFbo, FrameBufferObject targetFbo) {
		Matrix4f transform = new Matrix4f().translate(-1, 1).scale(2, -2);

		// 1. Extract bright areas into brightFbo
		brightFbo.render(() -> {
			brightnessProgram.use(glContext);
			brightnessProgram.set("transform", transform)
					.set("textureSampler", 0)
					.set("threshold", threshold);
			sceneTexture.bind(glContext, 0);
			vao.draw(glContext);
		});

		// 2. Blur bright areas (brightFbo -> blurFbo -> brightFbo)
		blurRenderer.render(brightFbo.texture(), blurFbo, brightFbo, blurRadius);

		// 3. Combine original scene and bloom texture into targetFbo
		targetFbo.render(() -> {
			combinationProgram.use(glContext);
			combinationProgram.set("transform", transform)
					.set("sceneTexture", 0)
					.set("bloomTexture", 1)
					.set("bloomIntensity", intensity)
					.set("backgroundColor", Colour.toRangedVector(backgroundColor));
			sceneTexture.bind(glContext, 0);
			brightFbo.texture().bind(glContext, 1);
			vao.draw(glContext);
		});
	}

	public void render(FrameBufferObject sceneFbo, FrameBufferObject brightFbo, FrameBufferObject blurFbo, FrameBufferObject targetFbo) {
		render(sceneFbo.texture(), brightFbo, blurFbo, targetFbo);
	}

}
