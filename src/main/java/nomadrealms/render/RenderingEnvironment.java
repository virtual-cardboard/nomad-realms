package nomadrealms.render;

import static java.util.Objects.requireNonNull;

import java.io.File;
import engine.common.loader.FontLoader;
import engine.common.loader.GraphLoader;
import engine.common.loader.ImageLoader;
import engine.common.loader.StringLoader;
import engine.visuals.rendering.texture.Image;
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

	public RenderingEnvironment(GLContext glContext, NengenConfiguration config) {
		this.glContext = glContext;
		this.config = config;

		loadAll();
		loadFBOs();
		loadRenderers(glContext);
	}

	private void loadAll() {
		GraphLoader graphLoader = new GraphLoader();
		// Shaders
		graphLoader.add("defaultVertexShaderSource", loaded -> new StringLoader(getFile("/shaders/defaultVertex.glsl")));
		graphLoader.add("defaultFragmentShaderSource", loaded -> new StringLoader(getFile("/shaders/defaultFrag.glsl")));
		graphLoader.add("circleFragmentShaderSource", loaded -> new StringLoader(getFile("/shaders/circleFrag.glsl")));
		// Images
		graphLoader.add("baloo2Image", loaded -> new ImageLoader(getFile("/fonts/baloo2.png")));
		graphLoader.add("buttonImage", loaded -> new ImageLoader(getFile("/images/button.png")));
		graphLoader.add("nomadImage", loaded -> new ImageLoader(getFile("/images/nomad.png")));
		graphLoader.add("farmerImage", loaded -> new ImageLoader(getFile("/images/farmer.png")));
		graphLoader.add("chiefImage", loaded -> new ImageLoader(getFile("/images/chief.png")));
		graphLoader.add("feralMonkeyImage", loaded -> new ImageLoader(getFile("/images/feral_monkey.png")));
		graphLoader.add("oakLogImage", loaded -> new ImageLoader(getFile("/images/oak_log.png")));
		graphLoader.add("wheatSeedImage", loaded -> new ImageLoader(getFile("/images/wheat_seed.png")));
		graphLoader.add("rock1Image", loaded -> new ImageLoader(getFile("/images/rock_1.png")));
		graphLoader.add("tree1Image", loaded -> new ImageLoader(getFile("/images/tree_1.png")));
		graphLoader.add("fenceImage", loaded -> new ImageLoader(getFile("/images/fence.png")));
		graphLoader.add("chestImage", loaded -> new ImageLoader(getFile("/images/chest.png")));
		graphLoader.add("electrostaticZapperImage", loaded -> new ImageLoader(getFile("/images/electrostatic_zapper.png")));
		graphLoader.add("cardFrontImage", loaded -> new ImageLoader(getFile("/images/card_front.png")));
		// Font
		graphLoader.add("font", loaded -> new FontLoader(getFile("/fonts/baloo2.vcfont"), (Image) loaded.get("baloo2Image")), "baloo2Image");

		Map<String, Object> loaded = graphLoader.load();
		// Font
		font = (GameFont) loaded.get("font");
		// Shaders
		defaultVertexShader = new VertexShader().source((String) loaded.get("defaultVertexShaderSource")).load();
		defaultFragmentShader = new FragmentShader().source((String) loaded.get("defaultFragmentShaderSource")).load();
		defaultShaderProgram = new ShaderProgram().attach(defaultVertexShader, defaultFragmentShader).load();
		circleFragmentShader = new FragmentShader().source((String) loaded.get("circleFragmentShaderSource")).load();
		circleShaderProgram = new ShaderProgram().attach(defaultVertexShader, circleFragmentShader).load();
		// Images
		imageMap.put("button", new Texture().image((Image) loaded.get("buttonImage")).load());
		imageMap.put("nomad", new Texture().image((Image) loaded.get("nomadImage")).load());
		imageMap.put("farmer", new Texture().image((Image) loaded.get("farmerImage")).load());
		imageMap.put("chief", new Texture().image((Image) loaded.get("chiefImage")).load());
		imageMap.put("feral_monkey", new Texture().image((Image) loaded.get("feralMonkeyImage")).load());
		imageMap.put("oak_log", new Texture().image((Image) loaded.get("oakLogImage")).load());
		imageMap.put("wheat_seed", new Texture().image((Image) loaded.get("wheatSeedImage")).load());
		imageMap.put("rock_1", new Texture().image((Image) loaded.get("rock1Image")).load());
		imageMap.put("tree_1", new Texture().image((Image) loaded.get("tree1Image")).load());
		imageMap.put("fence", new Texture().image((Image) loaded.get("fenceImage")).load());
		imageMap.put("chest", new Texture().image((Image) loaded.get("chestImage")).load());
		imageMap.put("electrostatic_zapper", new Texture().image((Image) loaded.get("electrostaticZapperImage")).load());
		imageMap.put("card_front", new Texture().image((Image) loaded.get("cardFrontImage")).load());
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

	private File getFile(String name) {
		return new File(requireNonNull(getClass().getResource(name)).getFile());
	}

}
