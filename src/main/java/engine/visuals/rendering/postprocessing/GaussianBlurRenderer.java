package engine.visuals.rendering.postprocessing;

import engine.common.math.Gaussian;
import engine.common.math.Matrix4f;
import engine.visuals.lwjgl.GLContext;
import engine.visuals.lwjgl.render.FrameBufferObject;
import engine.visuals.lwjgl.render.ShaderProgram;
import engine.visuals.rendering.texture.TextureRenderer;

public class GaussianBlurRenderer {

	private static final int MAX_KERNEL_RADIUS = 15;

	private final ShaderProgram shaderProgram;
	private final TextureRenderer textureRenderer;

	public GaussianBlurRenderer(ShaderProgram shaderProgram, TextureRenderer textureRenderer) {
		this.shaderProgram = shaderProgram;
		this.textureRenderer = textureRenderer;
	}

	public void render(GLContext glContext, FrameBufferObject fbo1, FrameBufferObject fbo2, int radius, float sigma) {
		if (radius > MAX_KERNEL_RADIUS) {
			throw new IllegalArgumentException("Radius must not exceed " + MAX_KERNEL_RADIUS);
		}
		float[] weights = Gaussian.generate1DGaussianKernel(radius, sigma);
		fbo2.render(() -> {
			shaderProgram.use(glContext);
			shaderProgram.uniforms()
					.set("horizontal", 1)
					.set("radius", radius)
					.set("weights", weights)
					.set("transform", new Matrix4f(glContext.screen, glContext))
					.set("textureSampler", 0)
					.complete();
			fbo1.texture().bind();
			textureRenderer.render(fbo1.texture(), shaderProgram);
		});

		fbo1.render(() -> {
			shaderProgram.uniforms()
					.set("horizontal", 0)
					.set("radius", radius)
					.set("weights", weights)
					.set("transform", new Matrix4f(glContext.screen, glContext))
					.set("textureSampler", 0)
					.complete();
			fbo2.texture().bind();
			textureRenderer.render(fbo2.texture(), shaderProgram);
		});
	}
}
