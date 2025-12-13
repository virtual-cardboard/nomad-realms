package nomadrealms.app.context;

import static engine.common.colour.Colour.rgb;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import engine.context.GameContext;
import engine.context.input.event.KeyPressedInputEvent;
import engine.context.input.event.KeyReleasedInputEvent;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.context.input.event.MouseScrolledInputEvent;
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
		// 1. Bind FBO
		re.fbo1.bind();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// 2. Render Image to FBO
		background(rgb(100, 100, 100));
		re.textureRenderer.render(re.imageMap.get("card_front"), 300, 100, 200, 300);

		// 3. Unbind FBO (bind default framebuffer)
		DefaultFrameBuffer.instance().bind();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// 4. Render FBO texture to screen with Blur Shader
		re.textureRenderer.render(re.fbo1.texture(), 0, 0, glContext().width(), glContext().height(), re.blurShaderProgram);
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
