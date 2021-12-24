package loading;

import static loading.NomadRealmsLoadingInfo.SOURCE_PATH;

import common.loader.loadtask.ShaderLoadTask;
import context.visuals.lwjgl.Shader;
import context.visuals.lwjgl.ShaderType;

public class NomadRealmsShaderLoadTask extends ShaderLoadTask {

	public NomadRealmsShaderLoadTask(ShaderType type, String sourceLocation) {
		super(type, SOURCE_PATH + sourceLocation);
	}

	public NomadRealmsShaderLoadTask(Shader shader, String sourceLocation) {
		super(shader, SOURCE_PATH + sourceLocation);
	}

}
