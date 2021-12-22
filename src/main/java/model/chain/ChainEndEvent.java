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
		CardPlayer cardPlayer = state.cardPlayer(((CardPlayer) source()).id());
		cardPlayer.cardDashboard().queue().setLocked(false);
		cardPlayer.removeChain(chain);
	}

	@Override
	public int priority() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean checkIsDone() {
		return true;
	}

}
