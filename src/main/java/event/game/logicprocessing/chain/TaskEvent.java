package event.game.logicprocessing.chain;

import java.util.Queue;

import common.event.GameEvent;
import model.state.GameState;
import model.task.Task;

public class TaskEvent extends VariableTimeChainEvent {

	private Task task;

	public TaskEvent(long playerID, Task task) {
		super(playerID);
		this.task = task;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		state.cardPlayer(playerID()).cardDashboard().setTask(task);
	}

	@Override
	public int priority() {
		return 10;
	}

	@Override
	public boolean checkIsDone() {
		return task.isDone();
	}

	@Override
	public boolean cancelled(GameState state) {
		return task.cancelled() || state.actor(playerID()).shouldRemove();
	}

}
