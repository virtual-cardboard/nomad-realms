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

	private GameFont font;
	private TextRenderer textRenderer;
	private Texture texture;
	private TextureRenderer textureRenderer;
	private FrameBufferObject fbo1;
	private FrameBufferObject fbo2;
	private ShaderProgram blurTextureProgram;

	@Override
	public void init() {
		font = loadFont(getFile("../fonts/baloo2.vcfont"), getFile("../fonts/baloo2.png"));
		FragmentShader blurShader = new FragmentShader().source(readFileAsString(getFile("../shaders/blur.glsl"))).load();
		blurTextureProgram = new ShaderProgram().attach(TexturedTransformationVertexShader.instance(), blurShader).load();
		textRenderer = new TextRenderer(glContext());
		texture = new Texture().image(loadImage(getFile("../image.png"))).load();
		textureRenderer = new TextureRenderer(glContext());
		fbo1 = new FrameBufferObject().texture(new Texture().dimensions(800, 600).load()).load();
		fbo2 = new FrameBufferObject().texture(new Texture().dimensions(800, 600).load()).load();
	}

	private File getFile(String name) {
		URL resource = getClass().getResource(name);
		if (resource == null) {
			System.out.println("Could not find resource " + name);
		}
		assert resource != null : "Could not find resource " + name;
		return new File(resource.getFile());
	}

	float fontSize = 1;
	float x = 0;

	@Override
	public void update() {
		x += 1;
	}

	@Override
	public void render(float alpha) {
		System.out.println(x + " " + alpha);
		fontSize = (float) (100 + 5 * Math.cos(x + 1 * alpha));
		fbo1.render(
				() -> {
					background(rgba(100, 100, 100, 255));
					textureRenderer.render(texture, 100, 100, 600, 400);
					textRenderer.render(0, 0, "@ Hello Oliver *='\"!", 500, font, fontSize, rgb(2, 255, 255));
				});
		blurTextureProgram
				.set("transform", new Matrix4f(0, 0, 800, 600, glContext()))
				.set("textureDim", new Vector2f(800, 600))
				.set("blurAmount", 10)
				.set("textureSampler", 0)
				.set("diffuseColour", rgb(255, 255, 255));
		fbo2.render(
				() -> {
					background(rgba(100, 100, 100, 255));
					blurTextureProgram
							.set("horizontal", 0)
							.use(new DrawFunction().textures(fbo1.texture()).vao(RectangleVertexArrayObject.instance()).glContext(glContext()));
				});
		DefaultFrameBuffer.instance().render(
				() -> {
					background(rgb(255, 255, 255));
					blurTextureProgram
							.set("horizontal", 1)
							.use(new DrawFunction().textures(fbo2.texture()).vao(RectangleVertexArrayObject.instance()).glContext(glContext()));
				});
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
