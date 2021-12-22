package event.game.logicprocessing.expression;

import java.util.Queue;

import common.event.GameEvent;
import model.GameState;
import model.actor.CardPlayer;
import model.card.Task;
import model.chain.VariableTimeChainEvent;

public class TaskEvent extends VariableTimeChainEvent {

	private Task task;

	public TaskEvent(CardPlayer player, Task task) {
		super(player);
		this.task = task;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		player().cardDashboard().setTask(task);
	}

	@Override
	public int priority() {
		return 0;
	}

	@Override
	public boolean checkIsDone() {
		return task.isDone();
	}

}
