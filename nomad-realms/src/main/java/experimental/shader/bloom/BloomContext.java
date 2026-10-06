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

		// 2. Extract bright areas from fbo1 into fbo2
		re.fbo2.render(() -> {
			background(rgb(0, 0, 0));
			re.brightnessShaderProgram.use(glContext());
			re.textureRenderer.render(re.fbo1.texture(), 0, 0, glContext().width(), glContext().height(), re.brightnessShaderProgram);
		});

		// 3. Apply Horizontal Gaussian Blur to bright areas (fbo2 -> fbo3)
		re.fbo3.render(() -> {
			background(rgb(0, 0, 0));
			re.gaussianBlurShaderProgram.use(glContext());
			re.gaussianBlurShaderProgram.uniforms()
					.set("horizontal", 1)
					.set("radius", 8.0f)
					.set("transform", new Matrix4f().translate(-1, 1).scale(2, -2))
					.set("textureSampler", 0)
					.complete();
			re.fbo2.texture().bind(glContext(), 0);
			RectangleVertexArrayObject.instance().draw(glContext());
		});

		// 4. Apply Vertical Gaussian Blur (fbo3 -> fbo2)
		re.fbo2.render(() -> {
			background(rgb(0, 0, 0));
			re.gaussianBlurShaderProgram.use(glContext());
			re.gaussianBlurShaderProgram.uniforms()
					.set("horizontal", 0)
					.set("radius", 8.0f)
					.set("transform", new Matrix4f().translate(-1, 1).scale(2, -2))
					.set("textureSampler", 0)
					.complete();
			re.fbo3.texture().bind(glContext(), 0);
			RectangleVertexArrayObject.instance().draw(glContext());
		});

		// 5. Combine original scene (fbo1) with blurred bright areas (fbo2) onto screen
		DefaultFrameBuffer.instance().render(() -> {
			background(rgb(0, 0, 0));
			re.bloomCombinationShaderProgram.use(glContext());
			re.bloomCombinationShaderProgram.uniforms()
					.set("transform", new Matrix4f().translate(-1, 1).scale(2, -2))
					.set("sceneTexture", 0)
					.set("bloomTexture", 1)
					.complete();
			re.fbo1.texture().bind(glContext(), 0);
			re.fbo2.texture().bind(glContext(), 1);
			RectangleVertexArrayObject.instance().draw(glContext());
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
	public void input(CharacterTypedInputEvent event) {
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
