package model.chain;

import java.util.Queue;

import common.event.GameEvent;
import event.game.logicprocessing.NomadRealmsLogicProcessingEvent;
import model.actor.CardPlayer;
import model.state.GameState;

public abstract class ChainEvent extends NomadRealmsLogicProcessingEvent {

	public ChainEvent(CardPlayer player) {
		super(player);
	}

	public final CardPlayer player() {
		return (CardPlayer) source();
	}

	public abstract void process(GameState state, Queue<GameEvent> sync);

	public abstract int priority();

	public abstract boolean checkIsDone();

	public abstract boolean cancelled();

	public abstract boolean shouldDisplay();

}
