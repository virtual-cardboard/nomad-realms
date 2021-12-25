package model.chain;

import java.util.Queue;

import common.event.GameEvent;
import model.GameState;
import model.actor.CardPlayer;

public final class ChainEndEvent extends ChainEvent {

	private EffectChain chain;

	public ChainEndEvent(CardPlayer source, EffectChain chain) {
		super(source);
		this.chain = chain;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		CardPlayer cardPlayer = state.cardPlayer(player().id());
		cardPlayer.removeChain(chain);
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
	public boolean cancelled() {
		return false;
	}

	@Override
	public boolean shouldDisplay() {
		return false;
	}

}
