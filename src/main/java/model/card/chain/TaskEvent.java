package model.card.chain;

import java.util.Queue;

import common.event.GameEvent;
import model.actor.CardPlayer;
import model.id.ID;
import model.state.GameState;
import model.task.Task;

public class TaskEvent extends VariableTimeChainEvent {

	private Task task;

	public TaskEvent(ID<? extends CardPlayer> playerID, Task task) {
		super(playerID);
		this.task = task;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		playerID().getFrom(state).cardDashboard().setTask(task);
	}

	@Override
	public int priority() {
		return 10;
	}

	@Override
	public boolean checkIsDone(GameState state) {
		return playerID().getFrom(state).cardDashboard().task().isDone();
	}

	@Override
	public boolean cancelled(GameState state) {
		CardPlayer player = playerID().getFrom(state);
		Task currentTask = player.cardDashboard().task();
		if (currentTask == null) {
			return true;
		}
		return task.cancelled() || playerID().getFrom(state).shouldRemove();
	}

	@Override
	public String textureName() {
		return task.name();
	}

}
