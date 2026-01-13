package nomadrealms.render;

import static engine.common.loader.FontLoader.loadFont;
import static engine.common.loader.ImageLoader.loadImage;
import static java.util.Objects.requireNonNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import engine.common.loader.StringLoader;
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

/**
 * The data and resources needed for rendering.
 *
 * @author Lunkle
 */
public class RenderingEnvironment {

	public GLContext glContext;
	public final NengenConfiguration config;

	public FrameBufferObject fbo1;
	public FrameBufferObject fbo2;
	public FrameBufferObject fbo3;
	public TextRenderer textRenderer;
	public TextureRenderer textureRenderer;

	public VertexShader defaultVertexShader;
	public FragmentShader defaultFragmentShader;
	public ShaderProgram defaultShaderProgram;
	public FragmentShader circleFragmentShader;
	public ShaderProgram circleShaderProgram;

	public VertexShader bloomVertexShader;
	public FragmentShader brightnessFragmentShader;
	public ShaderProgram brightnessShaderProgram;
	public VertexShader gaussianBlurVertexShader;
	public FragmentShader gaussianBlurFragmentShader;
	public ShaderProgram gaussianBlurShaderProgram;
	public FragmentShader bloomCombinationFragmentShader;
	public ShaderProgram bloomCombinationShaderProgram;

	public GameFont font;
	public Map<String, Texture> imageMap = new HashMap<>();

	public Camera camera = new Camera(0, 0);
	public boolean showDebugInfo = false;

	public RenderingEnvironment(GLContext glContext, NengenConfiguration config) {
		this.glContext = glContext;
		this.config = config;

		loadFonts();
		loadFBOs();
		loadRenderers(glContext);
		loadShaders();
		loadImages();
	}

	private void loadFonts() {
		font = loadFont(getFile("/fonts/baloo2.vcfont"), getFile("/fonts/baloo2.png"));
	}

	private void loadFBOs() {
		fbo1 = new FrameBufferObject().texture(new Texture().dimensions(config.getWidth(), config.getHeight()).load()).load();
		fbo2 = new FrameBufferObject().texture(new Texture().dimensions(config.getWidth(), config.getHeight()).load()).load();
		fbo3 = new FrameBufferObject().texture(new Texture().dimensions(config.getWidth(), config.getHeight()).load()).load();
		DefaultFrameBuffer.instance().bind();
	}

	private void loadRenderers(GLContext glContext) {
		textRenderer = new TextRenderer(glContext);
		textureRenderer = new TextureRenderer(glContext);
	}

	private void loadShaders() {
		defaultVertexShader = new VertexShader().source(new StringLoader(getFile("/shaders/defaultVertex.glsl")).load())
				.load();
		defaultFragmentShader = new FragmentShader().source(new StringLoader(getFile("/shaders/defaultFrag.glsl")).load())
				.load();
		defaultShaderProgram = new ShaderProgram().attach(defaultVertexShader, defaultFragmentShader).load();
		circleFragmentShader = new FragmentShader().source(new StringLoader(getFile("/shaders/circleFrag.glsl")).load())
				.load();
		circleShaderProgram = new ShaderProgram().attach(defaultVertexShader, circleFragmentShader).load();

		bloomVertexShader = new VertexShader().source(new StringLoader(getFile("/shaders/bloomVertex.glsl")).load())
				.load();
		brightnessFragmentShader = new FragmentShader()
				.source(new StringLoader(getFile("/shaders/brightness.glsl")).load()).load();
		brightnessShaderProgram = new ShaderProgram().attach(bloomVertexShader, brightnessFragmentShader).load();
		gaussianBlurVertexShader = new VertexShader()
				.source(new StringLoader(getFile("/shaders/gaussian_blur_vertex.glsl")).load()).load();
		gaussianBlurFragmentShader = new FragmentShader()
				.source(new StringLoader(getFile("/shaders/gaussian_blur.glsl")).load()).load();
		gaussianBlurShaderProgram =
				new ShaderProgram().attach(gaussianBlurVertexShader, gaussianBlurFragmentShader).load();
		bloomCombinationFragmentShader = new FragmentShader()
				.source(new StringLoader(getFile("/shaders/bloom_combination.glsl")).load()).load();
		bloomCombinationShaderProgram = new ShaderProgram().attach(bloomVertexShader, bloomCombinationFragmentShader)
				.load();
	}

	private void loadImages() {
		imageMap.put("button", new Texture().image(loadImage(getFile("/images/button.png"))).load());

		imageMap.put("nomad", new Texture().image(loadImage(getFile("/images/nomad.png"))).load());
		imageMap.put("farmer", new Texture().image(loadImage(getFile("/images/farmer.png"))).load());
		imageMap.put("chief", new Texture().image(loadImage(getFile("/images/chief.png"))).load());
		imageMap.put("feral_monkey", new Texture().image(loadImage(getFile("/images/feral_monkey.png"))).load());
		imageMap.put("oak_log", new Texture().image(loadImage(getFile("/images/oak_log.png"))).load());
		imageMap.put("wheat_seed", new Texture().image(loadImage(getFile("/images/wheat_seed.png"))).load());
		imageMap.put("rock_1", new Texture().image(loadImage(getFile("/images/rock_1.png"))).load());
		imageMap.put("tree_1", new Texture().image(loadImage(getFile("/images/tree_1.png"))).load());
		imageMap.put("fence", new Texture().image(loadImage(getFile("/images/fence.png"))).load());
		imageMap.put("oak_tree", new Texture().image(loadImage(getFile("/images/oak_tree.png"))).load());
		imageMap.put("pine_tree", new Texture().image(loadImage(getFile("/images/pine_tree.png"))).load());
		imageMap.put("chest", new Texture().image(loadImage(getFile("/images/chest.png"))).load());

		imageMap.put("grass_1", new Texture().image(loadImage(getFile("/images/decoration/grass_1.png"))).load());
		imageMap.put("grass_2", new Texture().image(loadImage(getFile("/images/decoration/grass_2.png"))).load());
		imageMap.put("grass_3", new Texture().image(loadImage(getFile("/images/decoration/grass_3.png"))).load());
		imageMap.put("grass_4", new Texture().image(loadImage(getFile("/images/decoration/grass_4.png"))).load());
		imageMap.put("grass_5", new Texture().image(loadImage(getFile("/images/decoration/grass_5.png"))).load());

		imageMap.put("up_arrow", new Texture().image(loadImage(getFile("/images/icons/ui/up.png"))).load());

		imageMap.put("directional_fire_small",
				new Texture().image(loadImage(getFile("/images/particles/directional_fire_small.png"))).load());
		imageMap.put("pill_blue",
				new Texture().image(loadImage(getFile("/images/particles/pill_blue.png"))).load());

		imageMap.put("electrostatic_zapper",
				new Texture().image(loadImage(getFile("/images/electrostatic_zapper.png"))).load());
		imageMap.put("card_front", new Texture().image(loadImage(getFile("/images/card_front.png"))).load());
		imageMap.put("card_back", new Texture().image(loadImage(getFile("/images/card_back.png"))).load());
		imageMap.put("bash", new Texture().image(loadImage(getFile("/images/card_art/bash.png"))).load());
		imageMap.put("big_punch", new Texture().image(loadImage(getFile("/images/card_art/big_punch.png"))).load());
		imageMap.put("build_house", new Texture().image(loadImage(getFile("/images/card_art/build_house.png"))).load());
		imageMap.put("cut_tree", new Texture().image(loadImage(getFile("/images/card_art/cut_tree.png"))).load());
		imageMap.put("extra_preparation",
				new Texture().image(loadImage(getFile("/images/card_art/extra_preparation.png"))).load());
		imageMap.put("gather", new Texture().image(loadImage(getFile("/images/card_art/gather.png"))).load());
		imageMap.put("meteor", new Texture().image(loadImage(getFile("/images/card_art/meteor.png"))).load());
		imageMap.put("move", new Texture().image(loadImage(getFile("/images/card_art/move.png"))).load());
		imageMap.put("overclocked_machinery",
				new Texture().image(loadImage(getFile("/images/card_art/overclocked_machinery.png"))).load());
		imageMap.put("refreshing_break",
				new Texture().image(loadImage(getFile("/images/card_art/refreshing_break.png"))).load());
		imageMap.put("regenesis", new Texture().image(loadImage(getFile("/images/card_art/regenesis.png"))).load());
		imageMap.put("restore", new Texture().image(loadImage(getFile("/images/card_art/restore.png"))).load());
		imageMap.put("teleport", new Texture().image(loadImage(getFile("/images/card_art/teleport.png"))).load());
		imageMap.put("zap", new Texture().image(loadImage(getFile("/images/card_art/zap.png"))).load());
		imageMap.put("flame_circle", new Texture().image(loadImage(getFile("/images/card_art/flame_circle.png"))).load());
		imageMap.put("ice_cube", new Texture().image(loadImage(getFile("/images/card_art/ice_cube.png"))).load());
	}

	private File getFile(String name) {
		return new File(requireNonNull(getClass().getResource(name)).getFile());
	}

}
