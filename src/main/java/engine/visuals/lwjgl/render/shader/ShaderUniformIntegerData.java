package engine.visuals.lwjgl.render.shader;

import engine.visuals.lwjgl.render.ShaderProgram;

public class ShaderUniformIntegerData extends ShaderUniformData<Integer> {

	public ShaderUniformIntegerData(String name) {
		super(name, Integer.class);
	}

	@Override
	public void setForProgram(ShaderProgram program) {
		if (value != null)
			program.set(name, value);
	}

}
