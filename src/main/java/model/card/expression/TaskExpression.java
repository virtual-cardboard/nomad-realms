package model.card.expression;

import java.util.function.Supplier;

import event.game.logicprocessing.chain.TaskEvent;
import model.chain.EffectChain;
import model.state.GameState;
import model.task.Task;

public final class TaskExpression extends CardExpression {

	private Supplier<Task> taskSupplier;

	public TaskExpression(Supplier<Task> taskSupplier) {
		this.taskSupplier = taskSupplier;
	}

	@Override
	public void handle(long playerID, long targetID, GameState state, EffectChain chain) {
		chain.add(new TaskEvent(playerID, taskSupplier.get(), targetID));
	}

}
