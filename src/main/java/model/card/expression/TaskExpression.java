package model.card.expression;

import java.util.List;
import java.util.function.Supplier;

import model.card.CardTag;
import model.chain.EffectChain;
import model.chain.event.TaskEvent;
import model.id.CardPlayerID;
import model.id.ID;
import model.state.GameState;
import model.task.Task;

public final class TaskExpression extends CardExpression {

	private Supplier<Task> taskSupplier;

	public TaskExpression(Supplier<Task> taskSupplier) {
		this.taskSupplier = taskSupplier;
	}

	@Override
	public void handle(CardPlayerID playerID, ID targetID, GameState state, EffectChain chain) {
		Task task = taskSupplier.get();
		task.setTarget(targetID);
		state.add(task);
		chain.addWheneverEvent(new TaskEvent(playerID, task));
	}

	@Override
	public void populateTags(List<CardTag> tags) {

	}

}
