package experimental.shader.blur;

import static engine.common.colour.Colour.rgb;

import engine.common.math.Matrix4f;
import engine.context.GameContext;
import engine.context.input.event.KeyPressedInputEvent;
import engine.context.input.event.KeyReleasedInputEvent;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.context.input.event.MouseScrolledInputEvent;
import engine.visuals.lwjgl.render.Texture;
import engine.visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import nomadrealms.render.RenderingEnvironment;

public class BlurContext extends GameContext {

	private RenderingEnvironment re;

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config());
	}

	@Override
	public void update() {
	}

	@Override
	public void render(float alpha) {
		re.fbo1.render(() -> {
			background(rgb(100, 100, 100));
			Texture nomadTexture = re.imageMap.get("nomad");
			if (nomadTexture != null) {
				re.textureRenderer.render(nomadTexture, 100, 100, 200, 200);
			}
		});

		re.fbo2.render(() -> {
			re.gaussianBlurShaderProgram.use(glContext());
			re.gaussianBlurShaderProgram.uniforms().set("horizontal", 1);
			re.gaussianBlurShaderProgram.uniforms().set("radius", 5.0f);
			re.fbo1.texture().bind();
			re.textureRenderer.render(re.fbo1.texture(), new Matrix4f(glContext().screen, glContext()), re.gaussianBlurShaderProgram);
		});

		DefaultFrameBuffer.instance().render(() -> {
			re.gaussianBlurShaderProgram.uniforms().set("horizontal", 0);
			re.gaussianBlurShaderProgram.uniforms().set("radius", 5.0f);
			re.fbo2.texture().bind();
			re.textureRenderer.render(re.fbo2.texture(), new Matrix4f(glContext().screen, glContext()), re.gaussianBlurShaderProgram);
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
