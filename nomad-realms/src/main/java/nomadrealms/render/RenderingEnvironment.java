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
import engine.visuals.rendering.texture.Image;
import engine.visuals.rendering.texture.TextureRenderer;
import java.util.HashMap;
import java.util.Map;
import nomadrealms.context.game.card.GameCard;
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
	public Map<Object, Texture> imageMap = new HashMap<>();

	public InteractionState is;

	public World world;

	public RenderingEnvironment(GLContext glContext, NengenConfiguration config, Mouse mouse) {
		this(glContext, config, mouse, preloadImages());
	}

	public RenderingEnvironment(GLContext glContext, NengenConfiguration config, Mouse mouse, Map<Object, Image> preloadedImages) {
		this.glContext = glContext;
		this.config = config;
		this.is = new InteractionState(mouse, glContext.screen);

		loadFonts(preloadedImages != null ? preloadedImages.get("font") : null);
		loadFBOs();
		loadRenderers(glContext);
		loadShaders();
		loadImages(preloadedImages);
	}

	public static Map<Object, Image> preloadImages() {
		Map<Object, Image> preloaded = new HashMap<>();
		preloaded.put("font", loadImage("/fonts/baloo2.png"));

		preloaded.put("button", loadImage("/images/button.png"));

		preloaded.put("nomad", loadImage("/images/nomad.png"));
		preloaded.put("farmer", loadImage("/images/farmer.png"));
		preloaded.put("villager_lumberjack", loadImage("/images/villager_lumberjack.png"));
		preloaded.put("chief", loadImage("/images/chief.png"));
		preloaded.put("feral_monkey", loadImage("/images/feral_monkey.png"));
		preloaded.put("wolf", loadImage("/images/wolf.png"));
		preloaded.put("witch_bear", loadImage("/images/witch_bear.png"));
		preloaded.put("spiderling", loadImage("/images/spiderling.png"));
		preloaded.put("oak_log", loadImage("/images/oak_log.png"));
		preloaded.put("wheat_seed", loadImage("/images/wheat_seed.png"));
		preloaded.put("gold_coin", loadImage("/images/wheat_seed.png"));
		preloaded.put("rock_1", loadImage("/images/rock_1.png"));
		preloaded.put("tree_1", loadImage("/images/tree_1.png"));
		preloaded.put("fence", loadImage("/images/fence.png"));
		preloaded.put("oak_tree", loadImage("/images/oak_tree.png"));
		preloaded.put("pine_tree", loadImage("/images/pine_tree.png"));
		preloaded.put("chest", loadImage("/images/chest.png"));
		preloaded.put("deathbloom", loadImage("/images/deathbloom.png"));
		preloaded.put("totem_of_pain", loadImage("/images/structures/totem_of_pain.png"));

		preloaded.put("grass_1", loadImage("/images/decoration/grass_1.png"));
		preloaded.put("grass_2", loadImage("/images/decoration/grass_2.png"));
		preloaded.put("grass_3", loadImage("/images/decoration/grass_3.png"));
		preloaded.put("grass_4", loadImage("/images/decoration/grass_4.png"));
		preloaded.put("grass_5", loadImage("/images/decoration/grass_5.png"));
		preloaded.put("grass_texture", loadImage("/images/textures/grass_texture.png"));

		preloaded.put("clouds", loadImage("/images/clouds.png"));

		preloaded.put("up_arrow", loadImage("/images/icons/ui/up.png"));
		preloaded.put("triangle_indicator", loadImage("/images/triangle_indicator.png"));
		preloaded.put("heart", loadImage("/images/heart.png"));

		preloaded.put("directional_fire_small", loadImage("/images/particles/directional_fire_small.png"));
		preloaded.put("pill", loadImage("/images/particles/pill.png"));
		preloaded.put("small_gold_coin_0", loadImage("/images/particles/small_gold_coin_0.png"));
		preloaded.put("small_gold_coin_1", loadImage("/images/particles/small_gold_coin_1.png"));
		preloaded.put("small_gold_coin_2", loadImage("/images/particles/small_gold_coin_2.png"));
		preloaded.put("small_gold_coin_3", loadImage("/images/particles/small_gold_coin_3.png"));
		preloaded.put("small_gold_coin_4", loadImage("/images/particles/small_gold_coin_4.png"));
		preloaded.put("small_gold_coin_5", loadImage("/images/particles/small_gold_coin_5.png"));

		preloaded.put("electrostatic_zapper", loadImage("/images/electrostatic_zapper.png"));
		preloaded.put("card_back", loadImage("/images/card/card_back.png"));
		preloaded.put("card_base", loadImage("/images/card/card_base.png"));
		preloaded.put("card_bookmarks", loadImage("/images/card/card_bookmarks.png"));
		preloaded.put("card_separator", loadImage("/images/card/card_separator.png"));
		preloaded.put("card_text_banner", loadImage("/images/card/card_text_banner.png"));
		preloaded.put("card_title_banner", loadImage("/images/card/card_title_banner.png"));
		preloaded.put("card_white_backing", loadImage("/images/card/card_white_backing.png"));
		preloaded.put("bash", loadImage("/images/card_art/bash.png"));
		preloaded.put("big_punch", loadImage("/images/card_art/big_punch.png"));
		preloaded.put("build_house", loadImage("/images/card_art/build_house.png"));
		preloaded.put("cut_tree", loadImage("/images/card_art/cut_tree.png"));
		preloaded.put("fear", loadImage("/images/card_art/fear.png"));
		preloaded.put("voodoo_hex", loadImage("/images/card_art/voodoo_hex.png"));
		preloaded.put("extra_preparation", loadImage("/images/card_art/extra_preparation.png"));
		preloaded.put("gather", loadImage("/images/card_art/gather.png"));
		preloaded.put("meteor", loadImage("/images/card_art/meteor.png"));
		preloaded.put("move", loadImage("/images/card_art/move.png"));
		preloaded.put("overclocked_machinery", loadImage("/images/card_art/overclocked_machinery.png"));
		preloaded.put("refreshing_break", loadImage("/images/card_art/refreshing_break.png"));
		preloaded.put("regenesis", loadImage("/images/card_art/regenesis.png"));
		preloaded.put("restore", loadImage("/images/card_art/restore.png"));
		preloaded.put("teleport", loadImage("/images/card_art/teleport.png"));
		preloaded.put("zap", loadImage("/images/card_art/zap.png"));
		preloaded.put("flame_circle", loadImage("/images/card_art/flame_circle.png"));
		preloaded.put("ice_cube", loadImage("/images/card_art/ice_cube.png"));
		preloaded.put("venomous_strike", loadImage("/images/card_art/venomous_strike.png"));
		preloaded.put("purge_poison", loadImage("/images/card_art/purge_poison.png"));
		preloaded.put("heavy_jump", loadImage("/images/card_art/heavy_jump.png"));
		preloaded.put("mind_blast", loadImage("/images/card_art/mind_blast.png"));

		preloaded.put(BURNED.image(), loadImage("/images/icons/status/burned.png"));
		preloaded.put(FROZEN.image(), loadImage("/images/icons/status/frozen.png"));
		preloaded.put(POISON.image(), loadImage("/images/icons/status/poison.png"));
		preloaded.put(INVINCIBLE.image(), loadImage("/images/icons/status/invincible.png"));
		return preloaded;
	}

	private void loadFonts(Image fontImage) {
		if (fontImage != null) {
			font = loadFont("/fonts/baloo2.vcfont", fontImage);
		} else {
			font = loadFont("/fonts/baloo2.vcfont", "/fonts/baloo2.png");
		}
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

	private void loadImages(Map<Object, Image> preloadedImages) {
		if (preloadedImages != null) {
			for (Map.Entry<Object, Image> entry : preloadedImages.entrySet()) {
				if (!"font".equals(entry.getKey())) {
					imageMap.put(entry.getKey(), new Texture().image(entry.getValue()).load());
				}
			}
		} else {
			loadImages();
		}
		validateCardArtwork();
	}

	private void loadImages() {
		for (Map.Entry<Object, Image> entry : preloadImages().entrySet()) {
			if (!"font".equals(entry.getKey())) {
				imageMap.put(entry.getKey(), new Texture().image(entry.getValue()).load());
			}
		}
		validateCardArtwork();
	}

	private void validateCardArtwork() {
		for (GameCard card : GameCard.values()) {
			String artworkKey = card.artwork();
			if (!imageMap.containsKey(artworkKey) || imageMap.get(artworkKey) == null) {
				throw new IllegalStateException("Missing artwork texture for card " + card.name() + ": '" + artworkKey + "'");
			}
		}
	}


}
