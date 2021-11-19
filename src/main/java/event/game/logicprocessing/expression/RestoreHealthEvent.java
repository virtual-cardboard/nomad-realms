package event.game.logicprocessing.expression;

import java.util.Queue;

import common.event.GameEvent;
import model.GameState;
import model.actor.CardPlayer;
import model.actor.HealthActor;

public class RestoreHealthEvent extends CardExpressionEvent {

	private int num;
	private HealthActor target;

	public RestoreHealthEvent(CardPlayer source, HealthActor target, int num) {
		super(source);
		this.target = target;
		this.num = num;
	}

	public int num() {
		return num;
	}

	public HealthActor target() {
		return target;
	}

	@Override
	public int priority() {
		return 3;
	}

	@Override
	public int processTime() {
		return 2;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		target.changeHealth(num);
	}

}
