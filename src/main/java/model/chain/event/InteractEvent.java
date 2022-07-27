package model.chain.event;

import engine.common.QueueGroup;
import math.IdGenerators;
import model.id.ActorId;
import model.id.CardPlayerId;
import model.state.GameState;

public class InteractEvent extends FixedTimeChainEvent {

	private ActorId targetID;

	public InteractEvent(CardPlayerId playerID, ActorId targetID) {
		super(playerID);
		this.targetID = targetID;
	}

	@Override
	public int processTime() {
		return 15;
	}

	@Override
	public void process(long tick, GameState state, IdGenerators idGenerators, QueueGroup queueGroup) {
	}

	@Override
	public int priority() {
		return 13;
	}

	@Override
	public String textureName() {
		return "interact";
	}

	public ActorId targetID() {
		return targetID;
	}

}
