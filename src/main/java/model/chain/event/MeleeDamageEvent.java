package model.chain.event;

import engine.common.QueueGroup;
import model.id.CardPlayerID;
import model.id.HealthActorID;
import model.state.GameState;

public class MeleeDamageEvent extends FixedTimeChainEvent {

	private HealthActorID targetID;
	private int amount;

	public MeleeDamageEvent(CardPlayerID playerID, HealthActorID targetID, int amount) {
		super(playerID);
		this.targetID = targetID;
		this.amount = amount;
	}

	public int amount() {
		return amount;
	}

	@Override
	public int priority() {
		return 3;
	}

	@Override
	public int processTime() {
		return 2;
	}

	@Override
	public void process(long tick, GameState state, QueueGroup queueGroup) {
		targetID.getFrom(state).changeHealth(-amount);
	}

	@Override
	public boolean cancelled(GameState state) {
		return super.cancelled(state) || targetID.getFrom(state).shouldRemove();
	}

	@Override
	public String textureName() {
		return "melee_damage";
	}

}
