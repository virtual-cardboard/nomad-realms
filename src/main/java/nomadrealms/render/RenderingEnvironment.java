package nomadrealms.render;

import static common.NengenFileUtil.loadFont;
import static common.NengenFileUtil.loadImage;
import static common.NengenFileUtil.readFileAsString;
import static java.util.Objects.requireNonNull;

import java.io.File;
import java.util.Objects;

import nomadrealms.render.vao.shape.HexagonVao;
import visuals.lwjgl.GLContext;
import visuals.lwjgl.render.FragmentShader;
import visuals.lwjgl.render.FrameBufferObject;
import visuals.lwjgl.render.ShaderProgram;
import visuals.lwjgl.render.Texture;
import visuals.lwjgl.render.VertexArrayObject;
import visuals.lwjgl.render.VertexShader;
import visuals.rendering.text.GameFont;
import visuals.rendering.text.TextRenderer;
import visuals.rendering.texture.TextureRenderer;

public class RenderingEnvironment {

	public GLContext glContext;

	public FrameBufferObject fbo1;
	public FrameBufferObject fbo2;
	public TextRenderer textRenderer;
	public TextureRenderer textureRenderer;
	public VertexArrayObject hexagonVao;
	public VertexShader defaultVertexShader;
	public FragmentShader defaultFragmentShader;
	public ShaderProgram defaultShaderProgram;
	private GameFont font;

	public RenderingEnvironment(GLContext glContext) {
		this.glContext = glContext;

		font = loadFont(getFile("/fonts/baloo2.vcfont"), getFile("/fonts/baloo2.png"));
		fbo1 = new FrameBufferObject().texture(new Texture().dimensions(800, 600).load()).load();
		fbo2 = new FrameBufferObject().texture(new Texture().dimensions(800, 600).load()).load();
		textRenderer = new TextRenderer(glContext);
		textureRenderer = new TextureRenderer(glContext);
		hexagonVao = HexagonVao.instance();
		defaultVertexShader =
				new VertexShader().source(readFileAsString(getFile("/shaders/defaultVertex.glsl"))).load();
		defaultFragmentShader =
				new FragmentShader().source(readFileAsString(getFile("/shaders/defaultFrag.glsl"))).load();
		defaultShaderProgram = new ShaderProgram().attach(defaultVertexShader, defaultFragmentShader).load();
	}

	private File getFile(String name) {
		return new File(requireNonNull(getClass().getResource(name)).getFile());
	}

}
