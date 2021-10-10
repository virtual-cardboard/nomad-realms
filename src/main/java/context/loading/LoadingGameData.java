package context.loading;

import context.data.GameData;
import graphics.loadtask.HexagonVertexArrayObjectLoadTask;

public class LoadingGameData extends GameData {

	private HexagonVertexArrayObjectLoadTask hexagonLoadTask;

	@Override
	protected void init() {
		hexagonLoadTask = new HexagonVertexArrayObjectLoadTask();
		loader().add(hexagonLoadTask);
	}

	public boolean isDone() {
		return hexagonLoadTask.countDownLatch().getCount() == 0;
	}

}
