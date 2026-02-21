package engine.visuals.builtin;

import engine.visuals.lwjgl.render.FragmentShader;
import engine.visuals.lwjgl.render.Shader;

public class TextureFragmentShader {

	private static Shader shader;

	private TextureFragmentShader() {
	}

	public static final String TEXT_FRAGMENT_SHADER_SOURCE = "#version 330 core\n"
			+ "out vec4 fragColor;"
			+ ""
			+ "in vec2 texCoord;"
			+ ""
			+ "uniform sampler2D textureSampler;"
			+ "uniform vec4 color = vec4(1, 1, 1, 1);"
			+ ""
			+ "void main() {"
			+ "    fragColor = texture(textureSampler, texCoord) * color;"
			+ "    if (fragColor.a == 0) {"
			+ "        discard;"
			+ "    }"
			+ "}";

	public static Shader instance() {
		if (shader == null) {
			shader = new FragmentShader()
					.source(TEXT_FRAGMENT_SHADER_SOURCE)
					.load();
		}
		return shader;
	}

}
