package nomadrealms.render;

import static engine.common.loader.FontLoader.loadFont;
import static engine.common.loader.ImageLoader.loadImage;
import static nomadrealms.context.game.actor.status.StatusEffect.BURNED;
import static nomadrealms.context.game.actor.status.StatusEffect.FROZEN;
import static nomadrealms.context.game.actor.status.StatusEffect.INVINCIBLE;
import static nomadrealms.context.game.actor.status.StatusEffect.POISON;

import engine.common.loader.StringLoader;
import engine.context.input.Mouse;
import engine.nengen.NengenConfiguration;
import engine.visuals.builtin.TextureFragmentShader;
import engine.visuals.builtin.TexturedTransformationVertexShader;
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
import java.util.HashMap;
import java.util.Map;
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
	public ShaderProgram texturedShaderProgram;

	public VertexShader bloomVertexShader;
	public FragmentShader brightnessFragmentShader;
	public ShaderProgram brightnessShaderProgram;
	public VertexShader gaussianBlurVertexShader;
	public FragmentShader gaussianBlurFragmentShader;
	public ShaderProgram gaussianBlurShaderProgram;
	public FragmentShader bloomCombinationFragmentShader;
	public ShaderProgram bloomCombinationShaderProgram;

	public GameFont font;
	public Map<Object, Texture> imageMap = new HashMap<>();

	public Camera camera = new Camera(0, 0);
	public boolean showDebugInfo = false;

	public Mouse mouse;

	public RenderingEnvironment(GLContext glContext, NengenConfiguration config, Mouse mouse) {
		this.glContext = glContext;
		this.config = config;
		this.mouse = mouse;

		loadFonts();
		loadFBOs();
		loadRenderers(glContext);
		loadShaders();
		loadImages();
	}

	private void loadFonts() {
		font = loadFont("/fonts/baloo2.vcfont", "/fonts/baloo2.png");
	}

	private void loadFBOs() {
		fbo1 = new FrameBufferObject().glContext(glContext).texture(new Texture().dimensions(glContext.fbDim()).load()).load();
		fbo2 = new FrameBufferObject().glContext(glContext).texture(new Texture().dimensions(glContext.fbDim()).load()).load();
		fbo3 = new FrameBufferObject().glContext(glContext).texture(new Texture().dimensions(glContext.fbDim()).load()).load();
		DefaultFrameBuffer.instance().glContext(glContext).bind();
	}

	private void loadRenderers(GLContext glContext) {
		textRenderer = new TextRenderer(glContext);
		textureRenderer = new TextureRenderer(glContext);
	}

	private void loadShaders() {
		defaultVertexShader = new VertexShader().source(new StringLoader("/shaders/defaultVertex.glsl").load())
				.load();
		defaultFragmentShader = new FragmentShader().source(new StringLoader("/shaders/defaultFrag.glsl").load())
				.load();
		defaultShaderProgram = new ShaderProgram().attach(defaultVertexShader, defaultFragmentShader).load();
		circleFragmentShader = new FragmentShader().source(new StringLoader("/shaders/circleFrag.glsl").load())
				.load();
		circleShaderProgram = new ShaderProgram().attach(defaultVertexShader, circleFragmentShader).load();
		texturedShaderProgram = new ShaderProgram().attach(TexturedTransformationVertexShader.instance(),
				TextureFragmentShader.instance()).load();

		bloomVertexShader = new VertexShader().source(new StringLoader("/shaders/bloomVertex.glsl").load())
				.load();
		brightnessFragmentShader = new FragmentShader()
				.source(new StringLoader("/shaders/brightness.glsl").load()).load();
		brightnessShaderProgram = new ShaderProgram().attach(bloomVertexShader, brightnessFragmentShader).load();
		gaussianBlurVertexShader = new VertexShader()
				.source(new StringLoader("/shaders/gaussian_blur_vertex.glsl").load()).load();
		gaussianBlurFragmentShader = new FragmentShader()
				.source(new StringLoader("/shaders/gaussian_blur.glsl").load()).load();
		gaussianBlurShaderProgram =
				new ShaderProgram().attach(gaussianBlurVertexShader, gaussianBlurFragmentShader).load();
		bloomCombinationFragmentShader = new FragmentShader()
				.source(new StringLoader("/shaders/bloom_combination.glsl").load()).load();
		bloomCombinationShaderProgram = new ShaderProgram().attach(bloomVertexShader, bloomCombinationFragmentShader)
				.load();
	}

	private void loadImages() {
		imageMap.put("button", new Texture().image(loadImage("/images/button.png")).load());

		imageMap.put("nomad", new Texture().image(loadImage("/images/nomad.png")).load());
		imageMap.put("farmer", new Texture().image(loadImage("/images/farmer.png")).load());
		imageMap.put("villager_lumberjack", new Texture().image(loadImage("/images/villager_lumberjack.png")).load());
		imageMap.put("chief", new Texture().image(loadImage("/images/chief.png")).load());
		imageMap.put("feral_monkey", new Texture().image(loadImage("/images/feral_monkey.png")).load());
		imageMap.put("wolf", new Texture().image(loadImage("/images/wolf.png")).load());
		imageMap.put("oak_log", new Texture().image(loadImage("/images/oak_log.png")).load());
		imageMap.put("wheat_seed", new Texture().image(loadImage("/images/wheat_seed.png")).load());
		imageMap.put("rock_1", new Texture().image(loadImage("/images/rock_1.png")).load());
		imageMap.put("tree_1", new Texture().image(loadImage("/images/tree_1.png")).load());
		imageMap.put("fence", new Texture().image(loadImage("/images/fence.png")).load());
		imageMap.put("oak_tree", new Texture().image(loadImage("/images/oak_tree.png")).load());
		imageMap.put("pine_tree", new Texture().image(loadImage("/images/pine_tree.png")).load());
		imageMap.put("chest", new Texture().image(loadImage("/images/chest.png")).load());

		imageMap.put("grass_1", new Texture().image(loadImage("/images/decoration/grass_1.png")).load());
		imageMap.put("grass_2", new Texture().image(loadImage("/images/decoration/grass_2.png")).load());
		imageMap.put("grass_3", new Texture().image(loadImage("/images/decoration/grass_3.png")).load());
		imageMap.put("grass_4", new Texture().image(loadImage("/images/decoration/grass_4.png")).load());
		imageMap.put("grass_5", new Texture().image(loadImage("/images/decoration/grass_5.png")).load());
		imageMap.put("grass_texture", new Texture().image(loadImage("/images/textures/grass_texture.png")).load());

		imageMap.put("clouds", new Texture().image(loadImage("/images/clouds.png")).load());

		imageMap.put("up_arrow", new Texture().image(loadImage("/images/icons/ui/up.png")).load());
		imageMap.put("triangle_indicator", new Texture().image(loadImage("/images/triangle_indicator.png")).load());

		imageMap.put("directional_fire_small",
				new Texture().image(loadImage("/images/particles/directional_fire_small.png")).load());
		imageMap.put("pill_blue",
				new Texture().image(loadImage("/images/particles/pill_blue.png")).load());

		imageMap.put("electrostatic_zapper",
				new Texture().image(loadImage("/images/electrostatic_zapper.png")).load());
		imageMap.put("card_back", new Texture().image(loadImage("/images/card/card_back.png")).load());
		imageMap.put("card_base", new Texture().image(loadImage("/images/card/card_base.png")).load());
		imageMap.put("card_bookmarks", new Texture().image(loadImage("/images/card/card_bookmarks.png")).load());
		imageMap.put("card_separator", new Texture().image(loadImage("/images/card/card_separator.png")).load());
		imageMap.put("card_text_banner", new Texture().image(loadImage("/images/card/card_text_banner.png")).load());
		imageMap.put("card_title_banner", new Texture().image(loadImage("/images/card/card_title_banner.png")).load());
		imageMap.put("card_white_backing", new Texture().image(loadImage("/images/card/card_white_backing.png")).load());
		imageMap.put("bash", new Texture().image(loadImage("/images/card_art/bash.png")).load());
		imageMap.put("big_punch", new Texture().image(loadImage("/images/card_art/big_punch.png")).load());
		imageMap.put("build_house", new Texture().image(loadImage("/images/card_art/build_house.png")).load());
		imageMap.put("cut_tree", new Texture().image(loadImage("/images/card_art/cut_tree.png")).load());
		imageMap.put("extra_preparation",
				new Texture().image(loadImage("/images/card_art/extra_preparation.png")).load());
		imageMap.put("gather", new Texture().image(loadImage("/images/card_art/gather.png")).load());
		imageMap.put("meteor", new Texture().image(loadImage("/images/card_art/meteor.png")).load());
		imageMap.put("move", new Texture().image(loadImage("/images/card_art/move.png")).load());
		imageMap.put("overclocked_machinery",
				new Texture().image(loadImage("/images/card_art/overclocked_machinery.png")).load());
		imageMap.put("refreshing_break",
				new Texture().image(loadImage("/images/card_art/refreshing_break.png")).load());
		imageMap.put("regenesis", new Texture().image(loadImage("/images/card_art/regenesis.png")).load());
		imageMap.put("restore", new Texture().image(loadImage("/images/card_art/restore.png")).load());
		imageMap.put("teleport", new Texture().image(loadImage("/images/card_art/teleport.png")).load());
		imageMap.put("zap", new Texture().image(loadImage("/images/card_art/zap.png")).load());
		imageMap.put("flame_circle", new Texture().image(loadImage("/images/card_art/flame_circle.png")).load());
		imageMap.put("ice_cube", new Texture().image(loadImage("/images/card_art/ice_cube.png")).load());
		imageMap.put("venomous_strike",
				new Texture().image(loadImage("/images/card_art/venomous_strike.png")).load());
		imageMap.put("purge_poison",
				new Texture().image(loadImage("/images/card_art/purge_poison.png")).load());
		imageMap.put("heavy_jump",
				new Texture().image(loadImage("/images/card_art/heavy_jump.png")).load());

		imageMap.put(BURNED.image(), new Texture().image(loadImage("/images/icons/status/burned.png")).load());
		imageMap.put(FROZEN.image(), new Texture().image(loadImage("/images/icons/status/frozen.png")).load());
		imageMap.put(POISON.image(), new Texture().image(loadImage("/images/icons/status/poison.png")).load());
		imageMap.put(INVINCIBLE.image(),
				new Texture().image(loadImage("/images/icons/status/invincible.png")).load());
	}

}
