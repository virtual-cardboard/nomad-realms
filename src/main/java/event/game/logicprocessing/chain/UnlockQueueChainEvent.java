package event.game.logicprocessing.chain;

import java.util.Queue;

import common.event.GameEvent;
import model.GameState;
import model.actor.CardPlayer;

public class UnlockQueueChainEvent extends ChainEvent {

	public UnlockQueueChainEvent(CardPlayer source) {
		super(source);
	}

	public UnlockQueueChainEvent(long time, CardPlayer source) {
		super(time, source);
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		CardPlayer cardPlayer = state.cardPlayer(((CardPlayer) source()).id());
		state.dashboard(cardPlayer).queue().setLocked(false);
	}

	@Override
	public int priority() {
		return Integer.MAX_VALUE;
	}

	@Override
	public int processTime() {
		return 0;
	}

}
