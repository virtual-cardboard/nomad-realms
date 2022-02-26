package model.card.chain;

import java.util.Queue;

import common.event.GameEvent;
import model.actor.CardPlayer;
import model.actor.HealthActor;
import model.id.ID;
import model.state.GameState;

public class RestoreHealthEvent extends FixedTimeChainEvent {

	private ID<? extends HealthActor> targetID;
	private int amount;

	public RestoreHealthEvent(ID<? extends CardPlayer> playerID, ID<? extends HealthActor> targetID, int amount) {
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
	public void process(GameState state, Queue<GameEvent> sync) {
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
