package model.chain.event;

import engine.common.ContextQueues;
import math.IdGenerators;
import model.id.CardPlayerId;
import model.id.HealthActorId;
import model.state.GameState;

public class RestoreHealthEvent extends FixedTimeChainEvent {

	private HealthActorId targetID;
	private int amount;

	public RestoreHealthEvent(CardPlayerId playerID, HealthActorId targetID, int amount) {
		super(playerID);
		this.targetID = targetID;
		this.amount = amount;
	}

	public int amount() {
		return amount;
	}

	@Override
	public int priority() {
		return 2;
	}

	@Override
	public int processTime() {
		return 2;
	}

	@Override
	public void process(long tick, GameState state, IdGenerators idGenerators, ContextQueues contextQueues) {
		targetID.getFrom(state).changeHealth(amount);
	}

	@Override
	public boolean cancelled(GameState state) {
		return super.cancelled(state) || targetID.getFrom(state).shouldRemove();
	}

	@Override
	public String textureName() {
		return "restore_health";
	}

}
