package nomadrealms.render;

import static engine.common.NengenFileUtil.loadFont;
import static engine.common.NengenFileUtil.loadImage;
import static engine.common.NengenFileUtil.readFileAsString;
import static java.util.Objects.requireNonNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import engine.nengen.NengenConfiguration;
import engine.visuals.lwjgl.GLContext;
import engine.visuals.lwjgl.render.FragmentShader;
import engine.visuals.lwjgl.render.FrameBufferObject;
import engine.visuals.lwjgl.render.ShaderProgram;
import engine.visuals.lwjgl.render.Texture;
import engine.visuals.lwjgl.render.VertexShader;
import engine.visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import engine.visuals.rendering.text.GameFont;
import engine.visuals.rendering.text.TextRenderer;
import engine.visuals.rendering.texture.TextureRenderer;
import nomadrealms.render.ui.Camera;
import nomadrealms.render.ui.content.ScreenContainerContent;

public class RenderingEnvironment {

	public GLContext glContext;
	public final NengenConfiguration config;

	public FrameBufferObject fbo1;
	public FrameBufferObject fbo2;
	public TextRenderer textRenderer;
	public TextureRenderer textureRenderer;
	public VertexShader defaultVertexShader;
	public FragmentShader defaultFragmentShader;
	public ShaderProgram defaultShaderProgram;
	public FragmentShader circleFragmentShader;
	public ShaderProgram circleShaderProgram;
	public GameFont font;
	public Map<String, Texture> imageMap = new HashMap<>();

	public Camera camera = new Camera(0, 0);
	public boolean showDebugInfo = false;

	public ScreenContainerContent screenContainerContent;

	public RenderingEnvironment(GLContext glContext, NengenConfiguration config) {
		this.glContext = glContext;
		this.config = config;

		loadFonts();
		loadFBOs();
		loadRenderers(glContext);
		loadShaders();
		loadImages();

		screenContainerContent = new ScreenContainerContent(glContext.screen);
	}

	private void loadFonts() {
		font = loadFont(getFile("/fonts/baloo2.vcfont"), getFile("/fonts/baloo2.png"));
	}

	private void loadFBOs() {
		fbo1 = new FrameBufferObject().texture(new Texture().dimensions(800, 600).load()).load();
		fbo2 = new FrameBufferObject().texture(new Texture().dimensions(800, 600).load()).load();
		DefaultFrameBuffer.instance().bind();
	}

	private void loadRenderers(GLContext glContext) {
		textRenderer = new TextRenderer(glContext);
		textureRenderer = new TextureRenderer(glContext);
	}

	private void loadShaders() {
		defaultVertexShader = new VertexShader().source(readFileAsString(getFile("/shaders/defaultVertex.glsl")))
				.load();
		defaultFragmentShader = new FragmentShader().source(readFileAsString(getFile("/shaders/defaultFrag.glsl")))
				.load();
		defaultShaderProgram = new ShaderProgram().attach(defaultVertexShader, defaultFragmentShader).load();
		circleFragmentShader = new FragmentShader().source(readFileAsString(getFile("/shaders/circleFrag.glsl")))
				.load();
		circleShaderProgram = new ShaderProgram().attach(defaultVertexShader, circleFragmentShader).load();
	}

	private void loadImages() {
		imageMap.put("nomad", new Texture().image(loadImage(getFile("/images/nomad.png"))).load());
		imageMap.put("farmer", new Texture().image(loadImage(getFile("/images/farmer.png"))).load());
		imageMap.put("feral_monkey", new Texture().image(loadImage(getFile("/images/feral_monkey.png"))).load());
		imageMap.put("oak_log", new Texture().image(loadImage(getFile("/images/oak_log.png"))).load());
		imageMap.put("wheat_seed", new Texture().image(loadImage(getFile("/images/wheat_seed.png"))).load());
		imageMap.put("rock_1", new Texture().image(loadImage(getFile("/images/rock_1.png"))).load());
		imageMap.put("tree_1", new Texture().image(loadImage(getFile("/images/tree_1.png"))).load());
		imageMap.put("fence", new Texture().image(loadImage(getFile("/images/fence.png"))).load());
		imageMap.put("chest", new Texture().image(loadImage(getFile("/images/chest.png"))).load());
		imageMap.put("electrostatic_zapper",
				new Texture().image(loadImage(getFile("/images/electrostatic_zapper.png"))).load());
		imageMap.put("card_front", new Texture().image(loadImage(getFile("/images/card_front.png"))).load());
	}

	private File getFile(String name) {
		return new File(requireNonNull(getClass().getResource(name)).getFile());
	}

}
