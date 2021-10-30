package loading;

import common.loader.loadtask.ShaderLoadTask;
import context.visuals.lwjgl.Shader;
import context.visuals.lwjgl.ShaderType;
import graphics.shape.HexagonVertexArrayObject;

public class NomadsShaderLoadTask extends ShaderLoadTask {

	private static final String SOURCE_PATH = HexagonVertexArrayObject.class.getProtectionDomain().getCodeSource().getLocation().getPath();

	public NomadsShaderLoadTask(ShaderType type, String sourceLocation) {
		super(type, SOURCE_PATH + sourceLocation);
	}

	public NomadsShaderLoadTask(Shader shader, String sourceLocation) {
		super(shader, SOURCE_PATH + sourceLocation);
	}

}
