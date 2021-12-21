package event.game.logicprocessing.chain;

import java.util.Queue;

import common.event.GameEvent;
import model.GameState;
import model.actor.CardPlayer;
import model.chain.EffectChain;

public class ChainEndEvent extends ChainEvent {

	private EffectChain chain;

	public ChainEndEvent(CardPlayer source, EffectChain chain) {
		super(source);
		this.chain = chain;
	}

	public ChainEndEvent(long time, CardPlayer source, EffectChain chain) {
		super(time, source);
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
	public int processTime() {
		return 0;
	}

}
