package model.card.expression;

import java.util.List;
import java.util.function.Supplier;

import model.card.CardTag;
import model.chain.EffectChain;
import model.chain.event.TaskEvent;
import model.id.CardPlayerId;
import model.id.Id;
import model.state.GameState;
import model.task.Task;

public final class TaskExpression extends CardExpression {

	private Supplier<Task> taskSupplier;

	public TaskExpression(Supplier<Task> taskSupplier) {
		this.taskSupplier = taskSupplier;
	}

	@Override
	public void handle(CardPlayerId playerID, Id targetId, GameState state, EffectChain chain) {
		Task task = taskSupplier.get();
		task.setTarget(targetId);
		state.add(task);
		chain.addWheneverEvent(new TaskEvent(playerID, task));
	}

	@Override
	public void populateTags(List<CardTag> tags) {

	}

}
