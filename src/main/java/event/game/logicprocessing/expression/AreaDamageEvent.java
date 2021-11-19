package event.game.logicprocessing.expression;

import java.util.Queue;

import common.event.GameEvent;
import model.GameState;
import model.actor.CardPlayer;

public class AreaDamageEvent extends CardExpressionEvent {

	public AreaDamageEvent(CardPlayer source) {
		super(source);
	}

	public AreaDamageEvent(long time, CardPlayer source) {
		super(time, source);
	}

	@Override
	public int priority() {
		return 1;
	}

	@Override
	public int processTime() {
		return 7;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		// TODO
	}

}
