package loading;

import java.util.concurrent.CountDownLatch;

import common.loader.loadtask.ShaderLoadTask;
import context.visuals.lwjgl.ShaderType;
import graphics.shape.HexagonVertexArrayObject;

public class NomadsShaderLoadTask extends ShaderLoadTask {

	public NomadsShaderLoadTask(CountDownLatch countDownLatch, ShaderType type, String sourceLocation) {
		super(countDownLatch, type, getSourcePath() + sourceLocation);
	}

	public NomadsShaderLoadTask(ShaderType type, String sourceLocation) {
		super(type, getSourcePath() + sourceLocation);
	}

	private static final String getSourcePath() {
		return HexagonVertexArrayObject.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	}

}
