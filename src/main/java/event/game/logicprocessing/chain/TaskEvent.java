package event.game.logicprocessing.chain;

import java.util.Queue;

import common.event.GameEvent;
import model.card.expression.CardTargetType;
import model.state.GameState;
import model.task.Task;

public class TaskEvent extends VariableTimeChainEvent {

	private Task task;
	private long targetID;
	private CardTargetType targetType;

	public TaskEvent(long playerID, Task task, long targetID, CardTargetType targetType) {
		super(playerID);
		this.task = task;
		this.targetID = targetID;
		this.targetType = targetType;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
//		if (targetType == CardTargetType.TILE) {
//			task.setTarget(state.worldMap().);
//		}
//		player().cardDashboard().setTask(task);
//		task.begin(player(), target, state);
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
		return task.cancelled();
	}

}
