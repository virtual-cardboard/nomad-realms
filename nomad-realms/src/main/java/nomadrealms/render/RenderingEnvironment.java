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
import nomadrealms.context.game.interaction.InteractionState;
import engine.visuals.builtin.TextureFragmentShader;
import engine.visuals.builtin.TexturedTransformationVertexShader;
import engine.visuals.lwjgl.GLContext;
import engine.visuals.lwjgl.render.CroppedTexture;
import engine.visuals.lwjgl.render.FragmentShader;
import engine.visuals.lwjgl.render.FrameBufferObject;
import engine.visuals.lwjgl.render.ShaderProgram;
import engine.visuals.lwjgl.render.Texture;
import engine.visuals.lwjgl.render.VertexShader;
import engine.visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import engine.visuals.rendering.text.GameFont;
import engine.visuals.rendering.geometry.CircleRenderer;
import engine.visuals.rendering.geometry.HexagonRenderer;
import engine.visuals.rendering.geometry.RectangleRenderer;
import engine.visuals.rendering.geometry.TriangleRenderer;
import engine.visuals.rendering.text.TextRenderer;
import engine.visuals.rendering.texture.SpriteSheet;
import engine.visuals.rendering.texture.TextureRenderer;
import java.util.HashMap;
import java.util.Map;
import nomadrealms.context.game.world.World;
import nomadrealms.render.ui.Camera;
import nomadrealms.user.Player;

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
	public RectangleRenderer rectangleRenderer;
	public TriangleRenderer triangleRenderer;
	public HexagonRenderer hexagonRenderer;
	public CircleRenderer circleRenderer;

	public VertexShader defaultVertexShader;
	public FragmentShader defaultFragmentShader;
	public ShaderProgram defaultShaderProgram;
	public ShaderProgram texturedShaderProgram;
	public ShaderProgram instancedShaderProgram;

	public VertexShader bloomVertexShader;
	public FragmentShader brightnessFragmentShader;
	public ShaderProgram brightnessShaderProgram;
	public VertexShader gaussianBlurVertexShader;
	public FragmentShader gaussianBlurFragmentShader;
	public ShaderProgram gaussianBlurShaderProgram;
	public FragmentShader bloomCombinationFragmentShader;
	public ShaderProgram bloomCombinationShaderProgram;

	public GameFont font;
	public Map<Object, CroppedTexture> imageMap = new HashMap<>();

	public InteractionState is;

	public World world;

	public RenderingEnvironment(GLContext glContext, NengenConfiguration config, Mouse mouse) {
		this.glContext = glContext;
		this.config = config;
		this.is = new InteractionState(mouse);

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
		rectangleRenderer = new RectangleRenderer(glContext);
		triangleRenderer = new TriangleRenderer(glContext);
		hexagonRenderer = new HexagonRenderer(glContext);

		defaultVertexShader = new VertexShader().source(new StringLoader("/shaders/defaultVertex.glsl").load())
				.load();
		circleRenderer = new CircleRenderer(glContext, defaultVertexShader);
	}

	private void loadShaders() {
		defaultFragmentShader = new FragmentShader().source(new StringLoader("/shaders/defaultFrag.glsl").load())
				.load();
		defaultShaderProgram = new ShaderProgram().attach(defaultVertexShader, defaultFragmentShader).load();
		texturedShaderProgram = new ShaderProgram().attach(TexturedTransformationVertexShader.instance(),
				TextureFragmentShader.instance()).load();
		instancedShaderProgram = new ShaderProgram().attach(
				new VertexShader().source(new StringLoader("/shaders/instancedVertex.glsl").load()).load(),
				new FragmentShader().source(new StringLoader("/shaders/instancedFrag.glsl").load()).load()
		).load();

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
		imageMap.put("button", new CroppedTexture(new Texture().image(loadImage("/images/button.png")).load()));

		imageMap.put("nomad", new CroppedTexture(new Texture().image(loadImage("/images/nomad.png")).load()));
		imageMap.put("farmer", new CroppedTexture(new Texture().image(loadImage("/images/farmer.png")).load()));
		imageMap.put("villager_lumberjack", new CroppedTexture(new Texture().image(loadImage("/images/villager_lumberjack.png")).load()));
		imageMap.put("chief", new CroppedTexture(new Texture().image(loadImage("/images/chief.png")).load()));
		imageMap.put("feral_monkey", new CroppedTexture(new Texture().image(loadImage("/images/feral_monkey.png")).load()));
		imageMap.put("wolf", new CroppedTexture(new Texture().image(loadImage("/images/wolf.png")).load()));
		imageMap.put("spiderling", new CroppedTexture(new Texture().image(loadImage("/images/spiderling.png")).load()));
		imageMap.put("oak_log", new CroppedTexture(new Texture().image(loadImage("/images/oak_log.png")).load()));
		imageMap.put("wheat_seed", new CroppedTexture(new Texture().image(loadImage("/images/wheat_seed.png")).load()));
		imageMap.put("gold_coin", new CroppedTexture(new Texture().image(loadImage("/images/wheat_seed.png")).load()));
		imageMap.put("rock_1", new CroppedTexture(new Texture().image(loadImage("/images/rock_1.png")).load()));
		imageMap.put("tree_1", new CroppedTexture(new Texture().image(loadImage("/images/tree_1.png")).load()));
		imageMap.put("fence", new CroppedTexture(new Texture().image(loadImage("/images/fence.png")).load()));
		imageMap.put("oak_tree", new CroppedTexture(new Texture().image(loadImage("/images/oak_tree.png")).load()));
		imageMap.put("pine_tree", new CroppedTexture(new Texture().image(loadImage("/images/pine_tree.png")).load()));
		imageMap.put("chest", new CroppedTexture(new Texture().image(loadImage("/images/chest.png")).load()));
		imageMap.put("deathbloom", new CroppedTexture(new Texture().image(loadImage("/images/deathbloom.png")).load()));

		imageMap.put("grass_1", new CroppedTexture(new Texture().image(loadImage("/images/decoration/grass_1.png")).load()));
		imageMap.put("grass_2", new CroppedTexture(new Texture().image(loadImage("/images/decoration/grass_2.png")).load()));
		imageMap.put("grass_3", new CroppedTexture(new Texture().image(loadImage("/images/decoration/grass_3.png")).load()));
		imageMap.put("grass_4", new CroppedTexture(new Texture().image(loadImage("/images/decoration/grass_4.png")).load()));
		imageMap.put("grass_5", new CroppedTexture(new Texture().image(loadImage("/images/decoration/grass_5.png")).load()));
		imageMap.put("grass_texture", new CroppedTexture(new Texture().image(loadImage("/images/textures/grass_texture.png")).load()));

		imageMap.put("clouds", new CroppedTexture(new Texture().image(loadImage("/images/clouds.png")).load()));

		imageMap.put("up_arrow", new CroppedTexture(new Texture().image(loadImage("/images/icons/ui/up.png")).load()));
		imageMap.put("triangle_indicator", new CroppedTexture(new Texture().image(loadImage("/images/triangle_indicator.png")).load()));

		SpriteSheet particleSpriteSheet = SpriteSheet.load("/images/particles/particles.png", "/images/particles/particles.txt");
		imageMap.putAll(particleSpriteSheet.sprites());

		imageMap.put("electrostatic_zapper",
				new CroppedTexture(new Texture().image(loadImage("/images/electrostatic_zapper.png")).load()));
		imageMap.put("card_back", new CroppedTexture(new Texture().image(loadImage("/images/card/card_back.png")).load()));
		imageMap.put("card_base", new CroppedTexture(new Texture().image(loadImage("/images/card/card_base.png")).load()));
		imageMap.put("card_bookmarks", new CroppedTexture(new Texture().image(loadImage("/images/card/card_bookmarks.png")).load()));
		imageMap.put("card_separator", new CroppedTexture(new Texture().image(loadImage("/images/card/card_separator.png")).load()));
		imageMap.put("card_text_banner", new CroppedTexture(new Texture().image(loadImage("/images/card/card_text_banner.png")).load()));
		imageMap.put("card_title_banner", new CroppedTexture(new Texture().image(loadImage("/images/card/card_title_banner.png")).load()));
		imageMap.put("card_white_backing", new CroppedTexture(new Texture().image(loadImage("/images/card/card_white_backing.png")).load()));
		imageMap.put("bash", new CroppedTexture(new Texture().image(loadImage("/images/card_art/bash.png")).load()));
		imageMap.put("big_punch", new CroppedTexture(new Texture().image(loadImage("/images/card_art/big_punch.png")).load()));
		imageMap.put("build_house", new CroppedTexture(new Texture().image(loadImage("/images/card_art/build_house.png")).load()));
		imageMap.put("cut_tree", new CroppedTexture(new Texture().image(loadImage("/images/card_art/cut_tree.png")).load()));
		imageMap.put("extra_preparation",
				new CroppedTexture(new Texture().image(loadImage("/images/card_art/extra_preparation.png")).load()));
		imageMap.put("gather", new CroppedTexture(new Texture().image(loadImage("/images/card_art/gather.png")).load()));
		imageMap.put("meteor", new CroppedTexture(new Texture().image(loadImage("/images/card_art/meteor.png")).load()));
		imageMap.put("move", new CroppedTexture(new Texture().image(loadImage("/images/card_art/move.png")).load()));
		imageMap.put("overclocked_machinery",
				new CroppedTexture(new Texture().image(loadImage("/images/card_art/overclocked_machinery.png")).load()));
		imageMap.put("refreshing_break",
				new CroppedTexture(new Texture().image(loadImage("/images/card_art/refreshing_break.png")).load()));
		imageMap.put("regenesis", new CroppedTexture(new Texture().image(loadImage("/images/card_art/regenesis.png")).load()));
		imageMap.put("restore", new CroppedTexture(new Texture().image(loadImage("/images/card_art/restore.png")).load()));
		imageMap.put("teleport", new CroppedTexture(new Texture().image(loadImage("/images/card_art/teleport.png")).load()));
		imageMap.put("zap", new CroppedTexture(new Texture().image(loadImage("/images/card_art/zap.png")).load()));
		imageMap.put("flame_circle", new CroppedTexture(new Texture().image(loadImage("/images/card_art/flame_circle.png")).load()));
		imageMap.put("ice_cube", new CroppedTexture(new Texture().image(loadImage("/images/card_art/ice_cube.png")).load()));
		imageMap.put("venomous_strike",
				new CroppedTexture(new Texture().image(loadImage("/images/card_art/venomous_strike.png")).load()));
		imageMap.put("purge_poison",
				new CroppedTexture(new Texture().image(loadImage("/images/card_art/purge_poison.png")).load()));
		imageMap.put("heavy_jump",
				new CroppedTexture(new Texture().image(loadImage("/images/card_art/heavy_jump.png")).load()));
		imageMap.put("mind_blast",
				new CroppedTexture(new Texture().image(loadImage("/images/card_art/mind_blast.png")).load()));

		imageMap.put(BURNED.image(), new CroppedTexture(new Texture().image(loadImage("/images/icons/status/burned.png")).load()));
		imageMap.put(FROZEN.image(), new CroppedTexture(new Texture().image(loadImage("/images/icons/status/frozen.png")).load()));
		imageMap.put(POISON.image(), new CroppedTexture(new Texture().image(loadImage("/images/icons/status/poison.png")).load()));
		imageMap.put(INVINCIBLE.image(),
				new CroppedTexture(new Texture().image(loadImage("/images/icons/status/invincible.png")).load()));
	}


}
