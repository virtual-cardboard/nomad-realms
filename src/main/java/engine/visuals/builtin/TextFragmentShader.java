package engine.visuals.builtin;

import engine.visuals.lwjgl.render.FragmentShader;
import engine.visuals.lwjgl.render.Shader;

/**
 * A fragment {@link Shader} that renders text.
 */
public class TextFragmentShader {

	private static FragmentShader shader;

	private TextFragmentShader() {
	}

	public static final String TEXT_FRAGMENT_SHADER_SOURCE = "#version 330 core\n"
			+ "in vec2 texCoord;\n"
			+ "out vec4 fragColor;\n"
			+ "\n"
			+ "uniform sampler2D textureSampler;\n"
			+ "uniform vec4 fill;\n"
			+ "\n"
			+ "\n"
			+ "void main() {\n"
			+ "    fragColor = texture(textureSampler, texCoord) * fill;"
			+ "    if (fragColor.a <= 0.01) {"
			+ "        discard;"
			+ "    }"
			+ "}";

	public static FragmentShader instance() {
		if (shader == null) {
			shader = new FragmentShader()
					.source(TEXT_FRAGMENT_SHADER_SOURCE)
					.load();
		}
		return shader;
	}

}
