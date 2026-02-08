package engine.visuals.lwjgl.render.shader;

import engine.visuals.lwjgl.render.ShaderProgram;

public class ShaderUniformFloatData extends ShaderUniformData<Float> {

	public ShaderUniformFloatData(String name) {
		super(name, Float.class);
	}

	@Override
	public void setForProgram(ShaderProgram program) {
		if (value != null)
			program.set(name, value);
	}

}
