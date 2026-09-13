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
import engine.visuals.rendering.texture.SpriteSheet;
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
	public ShaderProgram decorationShaderProgram;

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
	public SpriteSheet decorationSpriteSheet;

	public InteractionState is;

	public World world;

	public RenderingEnvironment(GLContext glContext, NengenConfiguration config, Mouse mouse) {
		this(glContext, config, mouse, null);
	}

	public RenderingEnvironment(GLContext glContext, NengenConfiguration config, Mouse mouse, Map<Object, Image> preloadedImages) {
		this.glContext = glContext;
		this.config = config;
		this.is = new InteractionState(mouse, glContext.screen);

		loadFonts(preloadedImages);
		loadFBOs();
		loadRenderers(glContext);
		loadShaders();
		loadImages(preloadedImages);
	}

	public static Map<Object, Image> preloadImages() {
		Map<Object, Image> preloadedImages = new HashMap<>();
		preloadedImages.put("font_image", loadImage("/fonts/baloo2.png"));
		preloadedImages.put("button", loadImage("/images/button.png"));

		preloadedImages.put("nomad", loadImage("/images/nomad.png"));
		preloadedImages.put("farmer", loadImage("/images/farmer.png"));
		preloadedImages.put("villager_lumberjack", loadImage("/images/villager_lumberjack.png"));
		preloadedImages.put("chief", loadImage("/images/chief.png"));
		preloadedImages.put("feral_monkey", loadImage("/images/feral_monkey.png"));
		preloadedImages.put("wolf", loadImage("/images/wolf.png"));
		preloadedImages.put("witch_bear", loadImage("/images/witch_bear.png"));
		preloadedImages.put("spiderling", loadImage("/images/spiderling.png"));
		preloadedImages.put("oak_log", loadImage("/images/oak_log.png"));
		preloadedImages.put("wheat_seed", loadImage("/images/wheat_seed.png"));
		preloadedImages.put("gold_coin", loadImage("/images/wheat_seed.png"));
		preloadedImages.put("rock_1", loadImage("/images/rock_1.png"));
		preloadedImages.put("tree_1", loadImage("/images/tree_1.png"));
		preloadedImages.put("fence", loadImage("/images/fence.png"));
		preloadedImages.put("oak_tree", loadImage("/images/oak_tree.png"));
		preloadedImages.put("pine_tree", loadImage("/images/pine_tree.png"));
		preloadedImages.put("chest", loadImage("/images/chest.png"));
		preloadedImages.put("deathbloom", loadImage("/images/deathbloom.png"));
		preloadedImages.put("totem_of_pain", loadImage("/images/structures/totem_of_pain.png"));
		preloadedImages.put("wall-0-2", loadImage("/images/structures/wall-0-2.png"));
		preloadedImages.put("wall-0-3", loadImage("/images/structures/wall-0-3.png"));
		preloadedImages.put("wall-1-3", loadImage("/images/structures/wall-1-3.png"));
		preloadedImages.put("wall-1-4", loadImage("/images/structures/wall-1-4.png"));
		preloadedImages.put("wall-1-5", loadImage("/images/structures/wall-1-5.png"));
		preloadedImages.put("wall-2-4", loadImage("/images/structures/wall-2-4.png"));

		preloadedImages.put("decorations_spritesheet", loadImage("/images/decoration/decorations.png"));
		preloadedImages.put("grass_texture", loadImage("/images/textures/grass_texture.png"));
		preloadedImages.put("clouds", loadImage("/images/clouds.png"));

		preloadedImages.put("up_arrow", loadImage("/images/icons/ui/up.png"));
		preloadedImages.put("triangle_indicator", loadImage("/images/triangle_indicator.png"));
		preloadedImages.put("heart", loadImage("/images/heart.png"));

		preloadedImages.put("directional_fire_small", loadImage("/images/particles/directional_fire_small.png"));
		preloadedImages.put("pill", loadImage("/images/particles/pill.png"));
		preloadedImages.put("small_gold_coin_0", loadImage("/images/particles/small_gold_coin_0.png"));
		preloadedImages.put("small_gold_coin_1", loadImage("/images/particles/small_gold_coin_1.png"));
		preloadedImages.put("small_gold_coin_2", loadImage("/images/particles/small_gold_coin_2.png"));
		preloadedImages.put("small_gold_coin_3", loadImage("/images/particles/small_gold_coin_3.png"));
		preloadedImages.put("small_gold_coin_4", loadImage("/images/particles/small_gold_coin_4.png"));
		preloadedImages.put("small_gold_coin_5", loadImage("/images/particles/small_gold_coin_5.png"));

		preloadedImages.put("electrostatic_zapper", loadImage("/images/electrostatic_zapper.png"));
		preloadedImages.put("card_back", loadImage("/images/card/card_back.png"));
		preloadedImages.put("card_base", loadImage("/images/card/card_base.png"));
		preloadedImages.put("card_bookmarks", loadImage("/images/card/card_bookmarks.png"));
		preloadedImages.put("card_separator", loadImage("/images/card/card_separator.png"));
		preloadedImages.put("card_text_banner", loadImage("/images/card/card_text_banner.png"));
		preloadedImages.put("card_title_banner", loadImage("/images/card/card_title_banner.png"));
		preloadedImages.put("card_white_backing", loadImage("/images/card/card_white_backing.png"));
		preloadedImages.put("bash", loadImage("/images/card_art/bash.png"));
		preloadedImages.put("big_punch", loadImage("/images/card_art/big_punch.png"));
		preloadedImages.put("build_house", loadImage("/images/card_art/build_house.png"));
		preloadedImages.put("cut_tree", loadImage("/images/card_art/cut_tree.png"));
		preloadedImages.put("fear", loadImage("/images/card_art/fear.png"));
		preloadedImages.put("voodoo_hex", loadImage("/images/card_art/voodoo_hex.png"));
		preloadedImages.put("extra_preparation", loadImage("/images/card_art/extra_preparation.png"));
		preloadedImages.put("gather", loadImage("/images/card_art/gather.png"));
		preloadedImages.put("meteor", loadImage("/images/card_art/meteor.png"));
		preloadedImages.put("move", loadImage("/images/card_art/move.png"));
		preloadedImages.put("overclocked_machinery", loadImage("/images/card_art/overclocked_machinery.png"));
		preloadedImages.put("refreshing_break", loadImage("/images/card_art/refreshing_break.png"));
		preloadedImages.put("regenesis", loadImage("/images/card_art/regenesis.png"));
		preloadedImages.put("restore", loadImage("/images/card_art/restore.png"));
		preloadedImages.put("teleport", loadImage("/images/card_art/teleport.png"));
		preloadedImages.put("zap", loadImage("/images/card_art/zap.png"));
		preloadedImages.put("flame_circle", loadImage("/images/card_art/flame_circle.png"));
		preloadedImages.put("ice_cube", loadImage("/images/card_art/ice_cube.png"));
		preloadedImages.put("venomous_strike", loadImage("/images/card_art/venomous_strike.png"));
		preloadedImages.put("purge_poison", loadImage("/images/card_art/purge_poison.png"));
		preloadedImages.put("heavy_jump", loadImage("/images/card_art/heavy_jump.png"));
		preloadedImages.put("mind_blast", loadImage("/images/card_art/mind_blast.png"));

		preloadedImages.put(BURNED.image(), loadImage("/images/icons/status/burned.png"));
		preloadedImages.put(FROZEN.image(), loadImage("/images/icons/status/frozen.png"));
		preloadedImages.put(POISON.image(), loadImage("/images/icons/status/poison.png"));
		preloadedImages.put(INVINCIBLE.image(), loadImage("/images/icons/status/invincible.png"));

		return preloadedImages;
	}

	private Image getImage(Map<Object, Image> preloadedImages, Object key, String path) {
		if (preloadedImages != null && preloadedImages.containsKey(key)) {
			return preloadedImages.get(key);
		}
		return loadImage(path);
	}

	private void loadFonts(Map<Object, Image> preloadedImages) {
		Image fontImage = (preloadedImages != null) ? preloadedImages.get("font_image") : null;
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
		decorationShaderProgram = new ShaderProgram().attach(
				new VertexShader().source(new StringLoader("/shaders/decorationVertex.glsl").load()).load(),
				new FragmentShader().source(new StringLoader("/shaders/decorationFrag.glsl").load()).load()
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
		imageMap.put("button", new Texture().image(getImage(preloadedImages, "button", "/images/button.png")).load());

		imageMap.put("nomad", new Texture().image(getImage(preloadedImages, "nomad", "/images/nomad.png")).load());
		imageMap.put("farmer", new Texture().image(getImage(preloadedImages, "farmer", "/images/farmer.png")).load());
		imageMap.put("villager_lumberjack", new Texture().image(getImage(preloadedImages, "villager_lumberjack", "/images/villager_lumberjack.png")).load());
		imageMap.put("chief", new Texture().image(getImage(preloadedImages, "chief", "/images/chief.png")).load());
		imageMap.put("feral_monkey", new Texture().image(getImage(preloadedImages, "feral_monkey", "/images/feral_monkey.png")).load());
		imageMap.put("wolf", new Texture().image(getImage(preloadedImages, "wolf", "/images/wolf.png")).load());
		imageMap.put("witch_bear", new Texture().image(getImage(preloadedImages, "witch_bear", "/images/witch_bear.png")).load());
		imageMap.put("spiderling", new Texture().image(getImage(preloadedImages, "spiderling", "/images/spiderling.png")).load());
		imageMap.put("oak_log", new Texture().image(getImage(preloadedImages, "oak_log", "/images/oak_log.png")).load());
		imageMap.put("wheat_seed", new Texture().image(getImage(preloadedImages, "wheat_seed", "/images/wheat_seed.png")).load());
		imageMap.put("gold_coin", new Texture().image(getImage(preloadedImages, "gold_coin", "/images/wheat_seed.png")).load());
		imageMap.put("rock_1", new Texture().image(getImage(preloadedImages, "rock_1", "/images/rock_1.png")).load());
		imageMap.put("tree_1", new Texture().image(getImage(preloadedImages, "tree_1", "/images/tree_1.png")).load());
		imageMap.put("fence", new Texture().image(getImage(preloadedImages, "fence", "/images/fence.png")).load());
		imageMap.put("oak_tree", new Texture().image(getImage(preloadedImages, "oak_tree", "/images/oak_tree.png")).load());
		imageMap.put("pine_tree", new Texture().image(getImage(preloadedImages, "pine_tree", "/images/pine_tree.png")).load());
		imageMap.put("chest", new Texture().image(getImage(preloadedImages, "chest", "/images/chest.png")).load());
		imageMap.put("deathbloom", new Texture().image(getImage(preloadedImages, "deathbloom", "/images/deathbloom.png")).load());
		imageMap.put("totem_of_pain", new Texture().image(getImage(preloadedImages, "totem_of_pain", "/images/structures/totem_of_pain.png")).load());
		imageMap.put("wall-0-2", new Texture().image(getImage(preloadedImages, "wall-0-2", "/images/structures/wall-0-2.png")).load());
		imageMap.put("wall-0-3", new Texture().image(getImage(preloadedImages, "wall-0-3", "/images/structures/wall-0-3.png")).load());
		imageMap.put("wall-1-3", new Texture().image(getImage(preloadedImages, "wall-1-3", "/images/structures/wall-1-3.png")).load());
		imageMap.put("wall-1-4", new Texture().image(getImage(preloadedImages, "wall-1-4", "/images/structures/wall-1-4.png")).load());
		imageMap.put("wall-1-5", new Texture().image(getImage(preloadedImages, "wall-1-5", "/images/structures/wall-1-5.png")).load());
		imageMap.put("wall-2-4", new Texture().image(getImage(preloadedImages, "wall-2-4", "/images/structures/wall-2-4.png")).load());

		Image sheetImage = getImage(preloadedImages, "decorations_spritesheet", "/images/decoration/decorations.png");
		decorationSpriteSheet = SpriteSheet.load(sheetImage, "/images/decoration/decorations.txt");
		imageMap.put("decorations_spritesheet", decorationSpriteSheet.texture());
		for (int i = 1; i <= 5; i++) {
			imageMap.put("grass_" + i, decorationSpriteSheet.get("grass_" + i).texture());
		}
		imageMap.put("grass_texture", new Texture().image(getImage(preloadedImages, "grass_texture", "/images/textures/grass_texture.png")).load());

		imageMap.put("clouds", new Texture().image(getImage(preloadedImages, "clouds", "/images/clouds.png")).load());

		imageMap.put("up_arrow", new Texture().image(getImage(preloadedImages, "up_arrow", "/images/icons/ui/up.png")).load());
		imageMap.put("triangle_indicator", new Texture().image(getImage(preloadedImages, "triangle_indicator", "/images/triangle_indicator.png")).load());
		imageMap.put("heart", new Texture().image(getImage(preloadedImages, "heart", "/images/heart.png")).load());

		imageMap.put("directional_fire_small",
				new Texture().image(getImage(preloadedImages, "directional_fire_small", "/images/particles/directional_fire_small.png")).load());
		imageMap.put("pill",
				new Texture().image(getImage(preloadedImages, "pill", "/images/particles/pill.png")).load());
		imageMap.put("small_gold_coin_0",
				new Texture().image(getImage(preloadedImages, "small_gold_coin_0", "/images/particles/small_gold_coin_0.png")).load());
		imageMap.put("small_gold_coin_1",
				new Texture().image(getImage(preloadedImages, "small_gold_coin_1", "/images/particles/small_gold_coin_1.png")).load());
		imageMap.put("small_gold_coin_2",
				new Texture().image(getImage(preloadedImages, "small_gold_coin_2", "/images/particles/small_gold_coin_2.png")).load());
		imageMap.put("small_gold_coin_3",
				new Texture().image(getImage(preloadedImages, "small_gold_coin_3", "/images/particles/small_gold_coin_3.png")).load());
		imageMap.put("small_gold_coin_4",
				new Texture().image(getImage(preloadedImages, "small_gold_coin_4", "/images/particles/small_gold_coin_4.png")).load());
		imageMap.put("small_gold_coin_5",
				new Texture().image(getImage(preloadedImages, "small_gold_coin_5", "/images/particles/small_gold_coin_5.png")).load());

		imageMap.put("electrostatic_zapper",
				new Texture().image(getImage(preloadedImages, "electrostatic_zapper", "/images/electrostatic_zapper.png")).load());
		imageMap.put("card_back", new Texture().image(getImage(preloadedImages, "card_back", "/images/card/card_back.png")).load());
		imageMap.put("card_base", new Texture().image(getImage(preloadedImages, "card_base", "/images/card/card_base.png")).load());
		imageMap.put("card_bookmarks", new Texture().image(getImage(preloadedImages, "card_bookmarks", "/images/card/card_bookmarks.png")).load());
		imageMap.put("card_separator", new Texture().image(getImage(preloadedImages, "card_separator", "/images/card/card_separator.png")).load());
		imageMap.put("card_text_banner", new Texture().image(getImage(preloadedImages, "card_text_banner", "/images/card/card_text_banner.png")).load());
		imageMap.put("card_title_banner", new Texture().image(getImage(preloadedImages, "card_title_banner", "/images/card/card_title_banner.png")).load());
		imageMap.put("card_white_backing", new Texture().image(getImage(preloadedImages, "card_white_backing", "/images/card/card_white_backing.png")).load());
		imageMap.put("bash", new Texture().image(getImage(preloadedImages, "bash", "/images/card_art/bash.png")).load());
		imageMap.put("big_punch", new Texture().image(getImage(preloadedImages, "big_punch", "/images/card_art/big_punch.png")).load());
		imageMap.put("build_house", new Texture().image(getImage(preloadedImages, "build_house", "/images/card_art/build_house.png")).load());
		imageMap.put("cut_tree", new Texture().image(getImage(preloadedImages, "cut_tree", "/images/card_art/cut_tree.png")).load());
		imageMap.put("fear", new Texture().image(getImage(preloadedImages, "fear", "/images/card_art/fear.png")).load());
		imageMap.put("voodoo_hex", new Texture().image(getImage(preloadedImages, "voodoo_hex", "/images/card_art/voodoo_hex.png")).load());
		imageMap.put("extra_preparation",
				new Texture().image(getImage(preloadedImages, "extra_preparation", "/images/card_art/extra_preparation.png")).load());
		imageMap.put("gather", new Texture().image(getImage(preloadedImages, "gather", "/images/card_art/gather.png")).load());
		imageMap.put("meteor", new Texture().image(getImage(preloadedImages, "meteor", "/images/card_art/meteor.png")).load());
		imageMap.put("move", new Texture().image(getImage(preloadedImages, "move", "/images/card_art/move.png")).load());
		imageMap.put("overclocked_machinery",
				new Texture().image(getImage(preloadedImages, "overclocked_machinery", "/images/card_art/overclocked_machinery.png")).load());
		imageMap.put("refreshing_break",
				new Texture().image(getImage(preloadedImages, "refreshing_break", "/images/card_art/refreshing_break.png")).load());
		imageMap.put("regenesis", new Texture().image(getImage(preloadedImages, "regenesis", "/images/card_art/regenesis.png")).load());
		imageMap.put("restore", new Texture().image(getImage(preloadedImages, "restore", "/images/card_art/restore.png")).load());
		imageMap.put("teleport", new Texture().image(getImage(preloadedImages, "teleport", "/images/card_art/teleport.png")).load());
		imageMap.put("zap", new Texture().image(getImage(preloadedImages, "zap", "/images/card_art/zap.png")).load());
		imageMap.put("flame_circle", new Texture().image(getImage(preloadedImages, "flame_circle", "/images/card_art/flame_circle.png")).load());
		imageMap.put("ice_cube", new Texture().image(getImage(preloadedImages, "ice_cube", "/images/card_art/ice_cube.png")).load());
		imageMap.put("venomous_strike",
				new Texture().image(getImage(preloadedImages, "venomous_strike", "/images/card_art/venomous_strike.png")).load());
		imageMap.put("purge_poison",
				new Texture().image(getImage(preloadedImages, "purge_poison", "/images/card_art/purge_poison.png")).load());
		imageMap.put("heavy_jump",
				new Texture().image(getImage(preloadedImages, "heavy_jump", "/images/card_art/heavy_jump.png")).load());
		imageMap.put("mind_blast",
				new Texture().image(getImage(preloadedImages, "mind_blast", "/images/card_art/mind_blast.png")).load());

		imageMap.put(BURNED.image(), new Texture().image(getImage(preloadedImages, BURNED.image(), "/images/icons/status/burned.png")).load());
		imageMap.put(FROZEN.image(), new Texture().image(getImage(preloadedImages, FROZEN.image(), "/images/icons/status/frozen.png")).load());
		imageMap.put(POISON.image(), new Texture().image(getImage(preloadedImages, POISON.image(), "/images/icons/status/poison.png")).load());
		imageMap.put(INVINCIBLE.image(),
				new Texture().image(getImage(preloadedImages, INVINCIBLE.image(), "/images/icons/status/invincible.png")).load());

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
