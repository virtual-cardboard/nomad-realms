package engine.visuals.lwjgl.render.shader;

import engine.common.math.Vector4f;
import engine.visuals.lwjgl.render.ShaderProgram;

public class ShaderUniformVec4Data extends ShaderUniformData<Vector4f> {

	public ShaderUniformVec4Data(String name) {
		super(name, Vector4f.class);
	}

	@Override
	public void setForProgram(ShaderProgram program) {
		if (value != null)
			program.set(name, value);
	}

}
