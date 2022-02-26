package model.card.chain;

import java.util.Queue;

import common.event.GameEvent;
import model.actor.Actor;
import model.actor.CardPlayer;
import model.id.ID;
import model.state.GameState;

public class DestroyEvent extends FixedTimeChainEvent {

	private ID<? extends Actor> targetID;

	public DestroyEvent(ID<? extends CardPlayer> playerID, ID<? extends Actor> targetID) {
		super(playerID);
		this.targetID = targetID;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
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
		return super.cancelled(state) || targetID.getFrom(state).shouldRemove();
	}

	@Override
	public String textureName() {
		return "destroy";
	}

}
