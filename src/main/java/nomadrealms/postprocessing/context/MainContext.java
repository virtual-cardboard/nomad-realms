package nomadrealms.postprocessing.context;

import static common.NengenFileUtil.loadFont;
import static common.NengenFileUtil.loadImage;
import static common.NengenFileUtil.readFileAsString;
import static common.colour.Colour.rgb;
import static common.colour.Colour.rgba;

import java.io.File;
import java.net.URL;

import common.math.Matrix4f;
import common.math.Vector2f;
import context.GameContext;
import context.input.event.KeyPressedInputEvent;
import context.input.event.KeyReleasedInputEvent;
import context.input.event.MouseScrolledInputEvent;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.world.map.Map;
import visuals.builtin.RectangleVertexArrayObject;
import visuals.builtin.TexturedTransformationVertexShader;
import visuals.lwjgl.render.FragmentShader;
import visuals.lwjgl.render.FrameBufferObject;
import visuals.lwjgl.render.ShaderProgram;
import visuals.lwjgl.render.Texture;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import visuals.lwjgl.render.meta.DrawFunction;
import visuals.lwjgl.render.shader.ShaderUniformInputList;
import visuals.rendering.text.GameFont;
import visuals.rendering.text.TextRenderer;
import visuals.rendering.texture.TextureRenderer;

public class MainContext extends GameContext {

	RenderingEnvironment re;
	Map map;

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext());
		map = new Map();
	}

	@Override
	public void update() {
	}

	@Override
	public void render(float alpha) {
		map.render(re);
	}

	@Override
	public void terminate() {
		System.out.println("second context terminate");
	}

	public void input(KeyPressedInputEvent event) {
		int key = event.code();
		System.out.println("second context key pressed: " + key);
	}

	public void input(KeyReleasedInputEvent event) {
		int key = event.code();
		System.out.println("second context key released: " + key);
	}

	public void input(MouseScrolledInputEvent event) {
		float amount = event.yAmount();
		System.out.println("second context mouse scrolled: " + amount);
	}

}
