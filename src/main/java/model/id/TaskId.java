package model.id;

import model.state.GameState;
import model.task.Task;

public class TaskId extends Id {

	public TaskId(long id) {
		super(id);
	}

	@Override
	public Task getFrom(GameState state) {
		return state.task(id);
	}

}
