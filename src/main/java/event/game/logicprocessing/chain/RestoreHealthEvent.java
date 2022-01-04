package event.game.logicprocessing.chain;

import java.util.Queue;

import common.event.GameEvent;
import model.actor.HealthActor;
import model.state.GameState;

public class RestoreHealthEvent extends FixedTimeChainEvent {

	private long targetID;
	private int amount;

	public RestoreHealthEvent(long playerID, long targetID, int amount) {
		super(playerID);
		this.targetID = targetID;
		this.amount = amount;
	}

	public int amount() {
		return amount;
	}

	@Override
	public int priority() {
		return 4;
	}

	@Override
	public int processTime() {
		return 2;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		((HealthActor) state.actor(targetID)).changeHealth(amount);
	}

	@Override
	public boolean cancelled(GameState state) {
		return super.cancelled(state);// || target.isDead();
	}

}
