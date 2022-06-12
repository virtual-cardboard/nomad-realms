package model.chain.event;

import engine.common.QueueGroup;
import event.logicprocessing.NomadRealmsLogicProcessingEvent;
import model.id.CardPlayerID;
import model.state.GameState;

public abstract class ChainEvent extends NomadRealmsLogicProcessingEvent {

	public ChainEvent(CardPlayerID playerID) {
		super(playerID);
	}

	public abstract void process(long tick, GameState state, QueueGroup queueGroup);

	public abstract int priority();

	public abstract boolean checkIsDone(GameState state);

	public abstract boolean cancelled(GameState state);

	public abstract boolean shouldDisplay();

	public abstract String textureName();

}
