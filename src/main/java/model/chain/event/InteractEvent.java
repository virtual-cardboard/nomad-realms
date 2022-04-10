package model.chain.event;

import engine.common.QueueGroup;
import model.id.ActorID;
import model.id.CardPlayerID;
import model.state.GameState;

public class InteractEvent extends FixedTimeChainEvent {

	private ActorID targetID;

	public InteractEvent(CardPlayerID playerID, ActorID targetID) {
		super(playerID);
		this.targetID = targetID;
	}

	@Override
	public int processTime() {
		return 15;
	}

	@Override
	public void process(long tick, GameState state, QueueGroup queueGroup) {
	}

	@Override
	public int priority() {
		return 13;
	}

	@Override
	public String textureName() {
		return "interact";
	}

	public ActorID targetID() {
		return targetID;
	}

}
