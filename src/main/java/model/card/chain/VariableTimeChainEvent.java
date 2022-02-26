package model.card.chain;

import model.actor.CardPlayer;
import model.id.ID;

public abstract class VariableTimeChainEvent extends ChainEvent {

	public VariableTimeChainEvent(ID<? extends CardPlayer> playerID) {
		super(playerID);
	}

	@Override
	public boolean shouldDisplay() {
		return true;
	}

}
