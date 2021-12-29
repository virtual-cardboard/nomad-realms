package event.game.logicprocessing.expression;

import java.util.Queue;

import common.event.GameEvent;
import model.actor.CardPlayer;
import model.actor.GameObject;
import model.chain.VariableTimeChainEvent;
import model.state.GameState;
import model.task.Task;

public class TaskEvent extends VariableTimeChainEvent {

	private Task task;
	private GameObject target;

	public TaskEvent(CardPlayer player, GameObject target, Task task) {
		super(player);
		this.target = target;
		this.task = task;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		task.setTarget(target);
		player().cardDashboard().setTask(task);
		task.begin(player(), target, state);
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
	public boolean cancelled() {
		return task.cancelled();
	}

}
