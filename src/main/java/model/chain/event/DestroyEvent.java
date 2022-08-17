package model.chain.event;

import engine.common.ContextQueues;
import math.IdGenerators;
import model.id.ActorId;
import model.id.CardPlayerId;
import model.state.GameState;

public class DestroyEvent extends FixedTimeChainEvent {

	private ActorId targetID;

	public DestroyEvent(CardPlayerId playerID, ActorId targetID) {
		super(playerID);
		this.targetID = targetID;
	}

	@Override
	public void process(long tick, GameState state, IdGenerators idGenerators, ContextQueues contextQueues) {
		targetID.getFrom(state).setShouldRemove(true);
	}

	@Override
	public int priority() {
		return 5;
	}

	@Override
	public int processTime() {
		return 6;
	}

	@Override
	public boolean cancelled(GameState state) {
		return super.cancelled(state) || targetID.getFrom(state) == null || targetID.getFrom(state).shouldRemove();
	}

	@Override
	public String textureName() {
		return "destroy";
	}

}
