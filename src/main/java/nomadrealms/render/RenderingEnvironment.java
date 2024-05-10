package nomadrealms.render;

import visuals.lwjgl.GLContext;
import visuals.lwjgl.render.*;
import visuals.rendering.text.GameFont;
import visuals.rendering.text.TextRenderer;
import visuals.rendering.texture.TextureRenderer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static common.NengenFileUtil.*;
import static java.util.Objects.requireNonNull;

public class RenderingEnvironment {

	public GLContext glContext;

	public FrameBufferObject fbo1;
	public FrameBufferObject fbo2;
	public TextRenderer textRenderer;
	public TextureRenderer textureRenderer;
	public VertexShader defaultVertexShader;
	public FragmentShader defaultFragmentShader;
	public ShaderProgram defaultShaderProgram;
	public GameFont font;
	public Map<String, Texture> imageMap = new HashMap<>();

	public RenderingEnvironment(GLContext glContext) {
		this.glContext = glContext;

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
		fbo1 = new FrameBufferObject().texture(new Texture().dimensions(800, 600).load()).load();
		fbo2 = new FrameBufferObject().texture(new Texture().dimensions(800, 600).load()).load();
	}

	private void loadRenderers(GLContext glContext) {
		textRenderer = new TextRenderer(glContext);
		textureRenderer = new TextureRenderer(glContext);
	}

	private void loadShaders() {
		defaultVertexShader =
				new VertexShader().source(readFileAsString(getFile("/shaders/defaultVertex.glsl"))).load();
		defaultFragmentShader =
				new FragmentShader().source(readFileAsString(getFile("/shaders/defaultFrag.glsl"))).load();
		defaultShaderProgram = new ShaderProgram().attach(defaultVertexShader, defaultFragmentShader).load();
	}

	private void loadImages() {
		imageMap.put("nomad", new Texture().image(loadImage(getFile("/images/nomad.png"))).load());
		imageMap.put("farmer", new Texture().image(loadImage(getFile("/images/farmer.png"))).load());
		imageMap.put("oak_log", new Texture().image(loadImage(getFile("/images/oak_log.png"))).load());
		imageMap.put("wheat_seed", new Texture().image(loadImage(getFile("/images/wheat_seed.png"))).load());
	}

	private File getFile(String name) {
		return new File(requireNonNull(getClass().getResource(name)).getFile());
	}

}
