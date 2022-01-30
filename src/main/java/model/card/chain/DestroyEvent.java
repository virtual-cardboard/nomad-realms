package model.card.chain;

import java.util.Queue;

import common.event.GameEvent;
import model.state.GameState;

public class DestroyEvent extends FixedTimeChainEvent {

	private long targetID;

	public DestroyEvent(long playerID, long targetID) {
		super(playerID);
		this.targetID = targetID;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		state.actor(targetID).setShouldRemove(true);
	}

	@Override
	public int priority() {
		return 5;
	}

	@Override
	public int processTime() {
		return 3;
	}

	@Override
	public boolean cancelled(GameState state) {
		return super.cancelled(state) || state.actor(targetID).shouldRemove();
	}

}
