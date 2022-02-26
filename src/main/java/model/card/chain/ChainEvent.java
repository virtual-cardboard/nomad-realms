package model.card.chain;

import java.util.Queue;

import common.event.GameEvent;
import event.game.logicprocessing.NomadRealmsLogicProcessingEvent;
import model.actor.CardPlayer;
import model.id.ID;
import model.state.GameState;

public abstract class ChainEvent extends NomadRealmsLogicProcessingEvent {

	public ChainEvent(ID<? extends CardPlayer> playerID) {
		super(playerID);
	}

	public abstract void process(GameState state, Queue<GameEvent> sync);

	public abstract int priority();

	public abstract boolean checkIsDone(GameState state);

	public abstract boolean cancelled(GameState state);

	public abstract boolean shouldDisplay();

	public abstract String textureName();

}
