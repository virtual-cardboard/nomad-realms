package loading;

import common.loader.loadtask.ShaderLoadTask;
import context.visuals.lwjgl.Shader;
import context.visuals.lwjgl.ShaderType;

public class NomadRealmsShaderLoadTask extends ShaderLoadTask {

	static final String SOURCE_PATH = NomadRealmsShaderLoadTask.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1);

	public NomadRealmsShaderLoadTask(ShaderType type, String sourceLocation) {
		super(type, SOURCE_PATH + sourceLocation);
	}

	public NomadRealmsShaderLoadTask(Shader shader, String sourceLocation) {
		super(shader, SOURCE_PATH + sourceLocation);
	}

}
