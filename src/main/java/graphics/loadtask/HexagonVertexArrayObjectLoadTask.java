package graphics.loadtask;

import static graphics.shape.HexagonVertexArrayObject.createHexagonVAO;

import java.io.IOException;

import common.loader.GLLoadTask;

public class HexagonVertexArrayObjectLoadTask extends GLLoadTask {

	@Override
	public void loadIO() throws IOException {
	}

	@Override
	public void loadGL() {
		createHexagonVAO();
	}
}
