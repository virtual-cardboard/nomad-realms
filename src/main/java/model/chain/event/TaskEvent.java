package model.chain.event;

import engine.common.ContextQueues;
import math.IdGenerators;
import model.id.CardPlayerId;
import model.state.GameState;
import model.task.Task;

public class TaskEvent extends VariableTimeChainEvent {

	private final Task task;
	private final String taskName;

	private boolean processed = false;

	public TaskEvent(CardPlayerId playerID, Task task) {
		super(playerID);
		this.task = task;
		this.taskName = task.name();
	}

	@Override
	public void process(long tick, GameState state, IdGenerators idGenerators, ContextQueues contextQueues) {
		playerID().getFrom(state).cardDashboard().setTask(task);
		processed = true;
	}

	@Override
	public int priority() {
		return 10;
	}

	@Override
	public boolean checkIsDone(GameState state) {
		return processed && task.isDone();
	}

	@Override
	public boolean cancelled(GameState state) {
		if (!processed) return false;
		Task task = playerID().getFrom(state).cardDashboard().task();
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
