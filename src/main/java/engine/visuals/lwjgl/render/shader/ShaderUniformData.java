package engine.visuals.lwjgl.render.shader;

import engine.common.misc.Data;
import engine.visuals.lwjgl.render.ShaderProgram;

public abstract class ShaderUniformData<T> extends Data<T> {

	public ShaderUniformData(String name, Class<T> clazz) {
		super(name, clazz);
	}

	public ShaderUniformData(String name, T value, Class<T> clazz) {
		super(name, value, clazz);
	}

	public abstract void setForProgram(ShaderProgram program);

	@SuppressWarnings("rawtypes")
	public static ShaderUniformData fromType(String type, String name) {
		switch (type) {
			case "float":
				return new ShaderUniformFloatData(name);
			case "vec2":
				return new ShaderUniformVec2Data(name);
			case "vec3":
				return new ShaderUniformVec3Data(name);
			case "vec4":
				return new ShaderUniformVec4Data(name);
			case "mat4":
				return new ShaderUniformMat4Data(name);
			case "sampler2D":
			case "int":
				return new ShaderUniformIntegerData(name);
			case "float[]":
				return new ShaderUniformFloatArrayData(name);
			case "int[]":
				return new ShaderUniformIntegerArrayData(name);
			default:
				throw new IllegalArgumentException("Invalid uniform type: " + type);
		}
	}

}
