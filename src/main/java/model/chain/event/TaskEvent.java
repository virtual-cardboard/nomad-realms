package model.chain.event;

import engine.common.QueueGroup;
import model.id.CardPlayerID;
import model.id.TaskID;
import model.state.GameState;
import model.task.Task;

public class TaskEvent extends VariableTimeChainEvent {

	private TaskID taskID;
	private String taskName;

	public TaskEvent(CardPlayerID playerID, Task task) {
		super(playerID);
		this.taskID = task.id();
		taskName = task.name();
	}

	@Override
	public void process(long tick, GameState state, QueueGroup queueGroup) {
		playerID().getFrom(state).cardDashboard().setTask(taskID.getFrom(state));
	}

	@Override
	public int priority() {
		return 10;
	}

	@Override
	public boolean checkIsDone(GameState state) {
		return taskID.getFrom(state).isDone();
	}

	@Override
	public boolean cancelled(GameState state) {
		Task task = taskID.getFrom(state);
		if (task == null) {
			return true;
		}
		return task.cancelled() || playerID().getFrom(state).shouldRemove();
	}

	@Override
	public String textureName() {
		return taskName;
	}

}
