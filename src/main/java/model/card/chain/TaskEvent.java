package model.card.chain;

import java.util.Queue;

import common.event.GameEvent;
import model.actor.CardPlayer;
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
	public boolean checkIsDone(GameState state) {
		return state.cardPlayer(playerID()).cardDashboard().task().isDone();
	}

	@Override
	public boolean cancelled(GameState state) {
		CardPlayer player = state.cardPlayer(playerID());
		Task currentTask = player.cardDashboard().task();
		if (currentTask == null) {
			return true;
		}
		return task.cancelled() || state.actor(playerID()).shouldRemove();
	}

	@Override
	public String textureName() {
		return task.name();
	}

}
