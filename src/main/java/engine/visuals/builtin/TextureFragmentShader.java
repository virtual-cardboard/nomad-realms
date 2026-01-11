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
			+ "uniform vec4 diffuseColour = vec4(1, 1, 1, 1);"
			+ "uniform vec4 crop;"
			+ ""
			+ "void main() {"
			+ "    vec2 texCoordCropped = vec2(texCoord.x * crop.z + crop.x, texCoord.y * crop.w + crop.y);"
			+ "    fragColor = texture(textureSampler, texCoordCropped);"
			+ "    if (fragColor.a == 0) {"
			+ "        fragColor = diffuseColour;"
			+ "        discard;"
			+ "    }"
//			+ "    fragColor = vec4(texCoord.x, texCoord.y, 0, 1);"
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
