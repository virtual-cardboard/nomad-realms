package graphics.renderer.hexagon;

import static context.visuals.lwjgl.ShaderType.FRAGMENT;

import context.visuals.builtin.TexturedTransformationVertexShader;
import context.visuals.lwjgl.Shader;
import context.visuals.lwjgl.ShaderProgram;

public class HexagonShaderProgram extends ShaderProgram {

	public HexagonShaderProgram(TexturedTransformationVertexShader vertexShader, Shader fragmentShader) {
		attachShader(vertexShader);
		attachShader(fragmentShader.verifyShaderType(FRAGMENT));
	}

}
