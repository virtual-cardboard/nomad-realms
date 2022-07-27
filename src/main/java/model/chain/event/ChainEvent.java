package model.chain.event;

import engine.common.QueueGroup;
import event.logicprocessing.NomadRealmsLogicProcessingEvent;
import math.IdGenerators;
import model.id.CardPlayerId;
import model.state.GameState;

public abstract class ChainEvent extends NomadRealmsLogicProcessingEvent {

	public ChainEvent(CardPlayerId playerID) {
		super(playerID);
	}

	public abstract void process(long tick, GameState state, IdGenerators idGenerators, QueueGroup queueGroup);

	public abstract int priority();

	public abstract boolean checkIsDone(GameState state);

	public abstract boolean cancelled(GameState state);

	public abstract boolean shouldDisplay();

	public abstract String textureName();

}
