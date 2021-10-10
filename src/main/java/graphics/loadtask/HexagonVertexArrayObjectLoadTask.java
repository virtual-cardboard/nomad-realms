package graphics.loadtask;

import static graphics.shape.HexagonVertexArrayObject.createHexagonVAO;

import java.io.IOException;

import common.loader.loadtask.OpenGLLoadTask;

public class HexagonVertexArrayObjectLoadTask extends OpenGLLoadTask {

	@Override
	public void loadGL() {

	}

	@Override
	public void loadIO() throws IOException {
		createHexagonVAO();
	}

}
