package graphics.renderer.hexagon;

import static context.visuals.lwjgl.ShaderType.FRAGMENT;

import context.visuals.builtin.TransformationVertexShader;
import context.visuals.lwjgl.Shader;
import context.visuals.lwjgl.ShaderProgram;

public class HexagonShaderProgram extends ShaderProgram {

	public HexagonShaderProgram(TransformationVertexShader vs, Shader fs) {
		addShader(vs);
		addShader(fs.verifyShaderType(FRAGMENT));
	}

}
