package model.card.effect;

import java.util.function.Supplier;

import event.game.logicprocessing.expression.TaskEvent;
import model.GameState;
import model.actor.CardPlayer;
import model.actor.GameObject;
import model.card.Task;
import model.chain.EffectChain;

public final class TaskExpression extends CardExpression {

	private Supplier<Task> taskSupplier;

	public TaskExpression(Supplier<Task> taskSupplier) {
		this.taskSupplier = taskSupplier;
	}

	@Override
	public void handle(CardPlayer playedBy, GameObject target, GameState state, EffectChain chain) {
		chain.add(new TaskEvent(playedBy, target, taskSupplier.get()));
	}

}
