package model.id;

import model.state.GameState;
import model.task.Task;

public class TaskID extends ID {

	public TaskID(long id) {
		super(id);
	}

	@Override
	public Task getFrom(GameState state) {
		return state.task(id);
	}

}
