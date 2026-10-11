package experimental.shader.bloom;

import static engine.common.colour.Colour.rgb;

import engine.common.math.Matrix4f;
import engine.context.GameContext;
import engine.context.input.event.CharacterTypedInputEvent;
import engine.context.input.event.KeyPressedInputEvent;
import engine.context.input.event.KeyReleasedInputEvent;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.context.input.event.MouseScrolledInputEvent;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import nomadrealms.render.RenderingEnvironment;

public class BloomContext extends GameContext {

	private RenderingEnvironment re;
	private float time = 0;
	private float radius = 8.0f;

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config(), mouse());
	}

	@Override
	public void update() {
	}

	@Override
	public void render(float alpha) {
		time += 0.04f;

		// 1. Render scene (4 blue rectangles on a black background) to fbo1
		re.fbo1.render(() -> {
			background(rgb(0, 0, 0));
			float width = glContext().width();
			float height = glContext().height();
			float rectW = width * 0.22f;
			float rectH = height * 0.22f;

			for (int i = 0; i < 4; i++) {
				float factor = (float) (0.55 + 0.45 * Math.sin(time + i * Math.PI / 2.0));
				int r = (int) (20 * factor);
				int g = (int) (120 * factor);
				int b = (int) (255 * factor);
				int fillColor = rgb(r, g, b);

				float x = (i % 2 == 0) ? width * 0.2f : width * 0.58f;
				float y = (i / 2 == 0) ? height * 0.2f : height * 0.58f;

				re.rectangleRenderer.render(x, y, rectW, rectH, 12, fillColor);
			}
		});

		// 2. Apply Bloom
		re.bloomRenderer.backgroundColor(rgb(0, 0, 0))
				.threshold(0.4f)
				.blurRadius(radius)
				.render(re.fbo1.texture(), re.fbo2, re.fbo3, DefaultFrameBuffer.instance());
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
	public void input(CharacterTypedInputEvent event) {
	}

	@Override
	public void input(MouseScrolledInputEvent event) {
		radius = Math.max(0, Math.min(30, radius + event.yAmount()));
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
