package model.chain.event;

import engine.common.QueueGroup;
import model.id.ActorID;
import model.id.CardPlayerID;
import model.state.GameState;

public class DestroyEvent extends FixedTimeChainEvent {

	private ActorID targetID;

	public DestroyEvent(CardPlayerID playerID, ActorID targetID) {
		super(playerID);
		this.targetID = targetID;
	}

	@Override
	public void process(long tick, GameState state, QueueGroup queueGroup) {
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
