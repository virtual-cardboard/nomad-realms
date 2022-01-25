package model.chain;

import java.util.Queue;

import common.event.GameEvent;
import event.game.logicprocessing.chain.ChainEvent;
import model.state.GameState;

public final class ChainEndEvent extends ChainEvent {

	private EffectChain chain;

	public ChainEndEvent(long playerID, EffectChain chain) {
		super(playerID);
		this.chain = chain;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
	}

	@Override
	public int priority() {
		return Integer.MAX_VALUE - 1;
	}

	@Override
	public boolean checkIsDone() {
		return true;
	}

	@Override
	public boolean cancelled(GameState state) {
		return false;
	}

	@Override
	public boolean shouldDisplay() {
		return false;
	}

}
