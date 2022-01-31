package model.card.chain;

import java.util.Queue;

import common.event.GameEvent;
import event.game.logicprocessing.NomadRealmsLogicProcessingEvent;
import model.state.GameState;

public abstract class ChainEvent extends NomadRealmsLogicProcessingEvent {

	public ChainEvent(long playerID) {
		super(playerID);
	}

	public abstract void process(GameState state, Queue<GameEvent> sync);

	public abstract int priority();

	public abstract boolean checkIsDone();

	public abstract boolean cancelled(GameState state);

	public abstract boolean shouldDisplay();

	public abstract String textureName();

}
