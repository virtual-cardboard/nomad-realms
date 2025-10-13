package engine.visuals.lwjgl.render.shader;

import engine.common.math.Vector2f;
import engine.visuals.lwjgl.render.ShaderProgram;

public class ShaderUniformVec2Data extends ShaderUniformData<Vector2f> {

	public ShaderUniformVec2Data(String name) {
		super(name, Vector2f.class);
	}

	@Override
	public void setForProgram(ShaderProgram program) {
		if (value != null)
			program.set(name, value);
	}

}
