package experimental.shader.blur;

import static engine.common.colour.Colour.rgb;

import engine.context.GameContext;
import engine.context.input.event.KeyPressedInputEvent;
import engine.context.input.event.KeyReleasedInputEvent;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.context.input.event.MouseScrolledInputEvent;
import engine.visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import engine.visuals.rendering.postprocessing.GaussianBlurRenderer;
import nomadrealms.render.RenderingEnvironment;

public class BlurContext extends GameContext {

	private int x = 0;
	private int delta = 1;
	private RenderingEnvironment re;
	private GaussianBlurRenderer gaussianBlurRenderer;

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config());
		gaussianBlurRenderer = new GaussianBlurRenderer(re.gaussianBlurShaderProgram, re.textureRenderer);
	}

	@Override
	public void update() {
	}

	@Override
	public void render(float alpha) {
		x += delta;
		if (x > glContext().screen.w().get() - 200 || x < 0) {
			delta = -delta;
		}
		// Render the scene to fbo1
		re.fbo1.render(() -> {
			background(rgb(100, 100, 100));
			re.textureRenderer.render(re.imageMap.get("nomad"), x, 100, 200, 200);
		});

		// Apply gaussian blur
		gaussianBlurRenderer.render(glContext(), re.fbo1, re.fbo2, 5, 5.0f);

		// Render the final blurred image to the screen
		DefaultFrameBuffer.instance().render(() -> {
			re.textureRenderer.render(re.fbo1.texture());
		});
	}

	@Override
	public void cleanUp() {
	}

	@Override
	public void input(KeyPressedInputEvent event) {
	}

	@Override
	public void input(KeyReleasedInputEvent event) {
	}

	@Override
	public void input(MouseScrolledInputEvent event) {
	}

	@Override
	public void input(MouseMovedInputEvent event) {
	}

	@Override
	public void input(MousePressedInputEvent event) {
	}

	@Override
	public void input(MouseReleasedInputEvent event) {
	}

}
